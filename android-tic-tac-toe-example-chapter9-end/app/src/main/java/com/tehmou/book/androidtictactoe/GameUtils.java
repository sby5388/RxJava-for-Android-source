package com.tehmou.book.androidtictactoe;

import com.tehmou.book.androidtictactoe.pojo.GameGrid;
import com.tehmou.book.androidtictactoe.pojo.GameStatus;
import com.tehmou.book.androidtictactoe.pojo.GameSymbol;
import com.tehmou.book.androidtictactoe.pojo.GridPosition;

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
}
