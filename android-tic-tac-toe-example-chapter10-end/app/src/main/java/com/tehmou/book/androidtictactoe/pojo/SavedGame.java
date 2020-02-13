package com.tehmou.book.androidtictactoe.pojo;

public class SavedGame {
    private final GameState gameState;
    private final long timestamp;

    public SavedGame(GameState gameState, long timestamp) {
        this.gameState = gameState;
        this.timestamp = timestamp;
    }

    public GameState getGameState() {
        return gameState;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
