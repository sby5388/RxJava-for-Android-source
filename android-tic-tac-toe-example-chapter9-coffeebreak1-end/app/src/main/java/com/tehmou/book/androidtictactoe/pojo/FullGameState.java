package com.tehmou.book.androidtictactoe.pojo;

public class FullGameState {
    private final GameState gameState;
    private final GameStatus gameStatus;

    public FullGameState(GameState gameState, GameStatus gameStatus) {
        this.gameState = gameState;
        this.gameStatus = gameStatus;
    }

    public GameState getGameState() {
        return gameState;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
