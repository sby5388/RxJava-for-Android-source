package com.tehmou.book.androidtictactoe.pojo;

public class GameStatus {
    private final boolean isEnded;
    private final GameSymbol winner;

    protected GameStatus(boolean isEnded) {
        this(isEnded, null);
    }

    protected GameStatus(boolean isEnded, GameSymbol winner) {
        this.isEnded = isEnded;
        this.winner = winner;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public GameSymbol getWinner() {
        return winner;
    }

    public static GameStatus ended(GameSymbol winner) {
        return new GameStatus(true, winner);
    }

    public static GameStatus ongoing() {
        return new GameStatus(false);
    }
}
