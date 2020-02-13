package com.tehmou.book.androidtictactoe;

import android.support.v4.util.Pair;

import com.tehmou.book.androidtictactoe.pojo.GameGrid;
import com.tehmou.book.androidtictactoe.pojo.GameState;
import com.tehmou.book.androidtictactoe.pojo.GameSymbol;
import com.tehmou.book.androidtictactoe.pojo.GridPosition;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class GameViewModel {
    private static final int GRID_WIDTH = 3;
    private static final int GRID_HEIGHT = 3;
    private static final GameGrid EMPTY_GRID = new GameGrid(GRID_WIDTH, GRID_HEIGHT);
    private static final GameState EMPTY_GAME = new GameState(EMPTY_GRID, GameSymbol.EMPTY);

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private final Observable<GameSymbol> playerInTurnObservable;
    private final BehaviorSubject<GameState> gameStateSubject = BehaviorSubject.createDefault(EMPTY_GAME);
    private final BehaviorSubject<GridPosition> lastMoveSubject = BehaviorSubject.create();

    private final Observable<GridPosition> touchEventObservable;

    public GameViewModel(Observable<GridPosition> touchEventObservable) {
        this.touchEventObservable = touchEventObservable;
        playerInTurnObservable = gameStateSubject
                .map(GameState::getLastPlayedSymbol)
                .map(symbol -> {
                    switch (symbol) {
                        case CIRCLE:
                            return GameSymbol.CROSS;
                        case CROSS:
                            return GameSymbol.TRIANGLE;
                        case TRIANGLE:
                        case EMPTY:
                        default:
                            return GameSymbol.CIRCLE;
                    }
                });
    }

    public Observable<GameGrid> getGameGrid() {
        return gameStateSubject
                .hide()
                .map(GameState::getGameGrid);
    }

    public Observable<GameSymbol> getPlayerInTurn() {
        return playerInTurnObservable;
    }

    public Observable<GridPosition> getLastMove() {
        return lastMoveSubject.hide();
    }

    public void subscribe() {
        subscriptions.add(touchEventObservable
                .subscribe(lastMoveSubject::onNext)
        );

        Observable<Pair<GameState, GameSymbol>> gameInfoObservable =
                Observable.combineLatest(gameStateSubject, playerInTurnObservable, Pair::new);

        subscriptions.add(touchEventObservable
                .withLatestFrom(gameInfoObservable,
                        (gridPosition, gameInfo) ->
                                gameInfo.first.setSymbolAt(
                                        gridPosition, gameInfo.second))
                .subscribe(gameStateSubject::onNext));
    }

    public void unsubscribe() {
        subscriptions.clear();
    }
}
