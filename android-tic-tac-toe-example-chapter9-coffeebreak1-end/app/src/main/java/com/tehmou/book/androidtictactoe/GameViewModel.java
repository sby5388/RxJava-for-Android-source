package com.tehmou.book.androidtictactoe;

import android.support.v4.util.Pair;

import com.tehmou.book.androidtictactoe.pojo.FullGameState;
import com.tehmou.book.androidtictactoe.pojo.GameGrid;
import com.tehmou.book.androidtictactoe.pojo.GameState;
import com.tehmou.book.androidtictactoe.pojo.GameStatus;
import com.tehmou.book.androidtictactoe.pojo.GameSymbol;
import com.tehmou.book.androidtictactoe.pojo.GridPosition;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class GameViewModel {
    private static final int GRID_WIDTH = 7;
    private static final int GRID_HEIGHT = 7;
    private static final GameGrid EMPTY_GRID = new GameGrid(GRID_WIDTH, GRID_HEIGHT);
    private static final GameState EMPTY_GAME = new GameState(EMPTY_GRID, GameSymbol.EMPTY);

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private final BehaviorSubject<GameState> gameStateSubject = BehaviorSubject.createDefault(EMPTY_GAME);
    private final Observable<GameSymbol> playerInTurnObservable;
    private final Observable<GameStatus> gameStatusObservable;

    private final Observable<GridPosition> touchEventObservable;
    private final Observable<Object> newGameEventObservable;

    public GameViewModel(Observable<GridPosition> touchEventObservable,
                         Observable<Object> newGameEventObservable) {
        this.touchEventObservable = touchEventObservable;
        this.newGameEventObservable = newGameEventObservable;
        playerInTurnObservable = gameStateSubject
                .map(GameState::getLastPlayedSymbol)
                .map(symbol -> {
                    if (symbol == GameSymbol.BLACK) {
                        return GameSymbol.RED;
                    } else {
                        return GameSymbol.BLACK;
                    }
                });
        gameStatusObservable = gameStateSubject
                .map(GameState::getGameGrid)
                .map(GameUtils::calculateGameStatus);
    }

    public Observable<GameStatus> getGameStatus() {
        return gameStatusObservable;
    }

    public Observable<FullGameState> getFullGameState() {
        return Observable.combineLatest(gameStateSubject, gameStatusObservable,
                FullGameState::new);
    }

    public Observable<GameSymbol> getPlayerInTurn() {
        return playerInTurnObservable;
    }

    public void subscribe() {
        subscriptions.add(newGameEventObservable
                .map(ignore -> EMPTY_GAME)
                .subscribe(gameStateSubject::onNext)
        );

        Observable<Pair<GameState, GameSymbol>> gameInfoObservable =
                Observable.combineLatest(gameStateSubject, playerInTurnObservable, Pair::new);

        Observable<GridPosition> gameNotEndedTouches =
                touchEventObservable
                        .withLatestFrom(gameStatusObservable, Pair::new)
                        .filter(pair -> !pair.second.isEnded())
                        .map(pair -> pair.first);

        Observable<GridPosition> filteredTouches =
                gameNotEndedTouches
                        .withLatestFrom(gameStateSubject, Pair::new)
                        .map(pair -> dropMarker(pair.first, pair.second.getGameGrid()))
                        .filter(position -> position.getY() >= 0);

        subscriptions.add(filteredTouches
                .withLatestFrom(gameInfoObservable,
                        (gridPosition, gameInfo) ->
                                gameInfo.first.setSymbolAt(
                                        gridPosition, gameInfo.second))
                .subscribe(gameStateSubject::onNext));
    }

    private static GridPosition dropMarker(GridPosition gridPosition, GameGrid gameGrid) {
        int i = gameGrid.getHeight() - 1;
        for (; i >= -1; i--) {
            if (i == -1) {
                // Let -1 fall through
                break;
            }
            GameSymbol symbol =
                    gameGrid.getSymbolAt(
                            gridPosition.getX(), i);
            if (symbol == GameSymbol.EMPTY) {
                break;
            }
        }
        return new GridPosition(gridPosition.getX(), i);
    }

    public void unsubscribe() {
        subscriptions.clear();
    }
}
