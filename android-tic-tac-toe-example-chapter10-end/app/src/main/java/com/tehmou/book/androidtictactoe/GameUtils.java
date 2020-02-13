package com.tehmou.book.androidtictactoe;

import android.support.v4.util.Pair;

import com.tehmou.book.androidtictactoe.pojo.GameGrid;
import com.tehmou.book.androidtictactoe.pojo.GameState;
import com.tehmou.book.androidtictactoe.pojo.GameStatus;
import com.tehmou.book.androidtictactoe.pojo.GameSymbol;
import com.tehmou.book.androidtictactoe.pojo.GridPosition;

import io.reactivex.Observable;

public class GameUtils {
    public static GameStatus calculateGameStatus(GameGrid gameGrid) {
        final int WIDTH = gameGrid.getWidth();
        final int HEIGHT = gameGrid.getHeight();
        for (int r = 0; r < WIDTH; r++) {
            for (int c = 0; c < HEIGHT; c++) {
                GameSymbol player = gameGrid.getSymbolAt(r, c);
                if (player == GameSymbol.EMPTY)
                    continue;

                if (c + 3 < WIDTH &&
                        player == gameGrid.getSymbolAt(r, c+1) &&
                        player == gameGrid.getSymbolAt(r, c+2) &&
                        player == gameGrid.getSymbolAt(r, c+3))
                    return GameStatus.ended(player,
                            new GridPosition(r, c),
                            new GridPosition(r, c+3));
                if (r + 3 < HEIGHT) {
                    if (player == gameGrid.getSymbolAt(r+1, c) &&
                            player == gameGrid.getSymbolAt(r+2, c) &&
                            player == gameGrid.getSymbolAt(r+3, c))
                        return GameStatus.ended(player,
                                new GridPosition(r, c),
                                new GridPosition(r+3, c));
                    if (c + 3 < WIDTH &&
                            player == gameGrid.getSymbolAt(r+1, c+1) &&
                            player == gameGrid.getSymbolAt(r+2, c+2) &&
                            player == gameGrid.getSymbolAt(r+3, c+3))
                        return GameStatus.ended(player,
                                new GridPosition(r, c),
                                new GridPosition(r+3, c+3));
                    if (c - 3 >= 0 &&
                            player == gameGrid.getSymbolAt(r+1, c-1) &&
                            player == gameGrid.getSymbolAt(r+2, c-2) &&
                            player == gameGrid.getSymbolAt(r+3, c-3))
                        return GameStatus.ended(player,
                                new GridPosition(r, c),
                                new GridPosition(r+3, c-3));
                }
            }
        }
        return GameStatus.ongoing();
    }

    public static Observable<GameState> processGamesMoves(Observable<GameState> gameStateObservable,
                                                          Observable<GameStatus> gameStatusObservable,
                                                          Observable<GameSymbol> playerInTurnObservable,
                                                          Observable<GridPosition> touchEventObservable) {
        Observable<Pair<GameState, GameSymbol>> gameInfoObservable =
                Observable.combineLatest(gameStateObservable, playerInTurnObservable, Pair::new);

        Observable<GridPosition> gameNotEndedTouches =
                touchEventObservable
                        .withLatestFrom(gameStatusObservable, Pair::new)
                        .filter(pair -> !pair.second.isEnded())
                        .map(pair -> pair.first);

        Observable<GridPosition> filteredTouches =
                gameNotEndedTouches
                        .withLatestFrom(gameStateObservable, Pair::new)
                        .map(pair -> dropMarker(pair.first, pair.second.getGameGrid()))
                        .filter(position -> position.getY() >= 0);

        Observable<GameState> updatedGameStateObservable =
                filteredTouches
                        .withLatestFrom(gameInfoObservable,
                                (gridPosition, gameInfo) ->
                                        gameInfo.first.setSymbolAt(
                                                gridPosition, gameInfo.second));

        return updatedGameStateObservable;
    }

    public static GridPosition dropMarker(GridPosition gridPosition, GameGrid gameGrid) {
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
}
