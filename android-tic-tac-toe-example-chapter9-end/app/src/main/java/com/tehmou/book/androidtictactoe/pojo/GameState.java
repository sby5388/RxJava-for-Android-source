package com.tehmou.book.androidtictactoe.pojo;

public class GameState {
    private final GameGrid gameGrid;
    private final GameSymbol lastPlayedSymbol;

    public GameState(GameGrid gameGrid, GameSymbol lastPlayedSymbol) {
        this.gameGrid = gameGrid;
        this.lastPlayedSymbol = lastPlayedSymbol;
    }

    public GameGrid getGameGrid() {
        return gameGrid;
    }

    public GameSymbol getLastPlayedSymbol() {
        return lastPlayedSymbol;
    }

    public GameState setSymbolAt(GridPosition gridPosition, GameSymbol symbol) {
        return new GameState(
                gameGrid.setSymbolAt(gridPosition, symbol), symbol
        );
    }

    public boolean isEmpty(GridPosition gridPosition) {
        return gameGrid.getSymbolAt(gridPosition) == GameSymbol.EMPTY;
    }
}
