package com.tehmou.book.androidtictactoe.pojo;

public class GameStatus {
    private final GameSymbol winner;
    private final GridPosition winningPositionStart;
    private final GridPosition winningPositionEnd;

    protected GameStatus(GameSymbol winner,
                         GridPosition winningPositionStart,
                         GridPosition winningPositionEnd) {
        this.winner = winner;
        this.winningPositionStart = winningPositionStart;
        this.winningPositionEnd = winningPositionEnd;
    }

    public boolean isEnded() {
        return winner != null;
    }

    public GameSymbol getWinner() {
        return winner;
    }

    public GridPosition getWinningPositionStart() {
        return winningPositionStart;
    }

    public GridPosition getWinningPositionEnd() {
        return winningPositionEnd;
    }

    public static GameStatus ended(GameSymbol winner,
                                   GridPosition winningPositionStart,
                                   GridPosition winningPositionEnd) {
        return new GameStatus(winner, winningPositionStart, winningPositionEnd);
    }

    public static GameStatus ongoing() {
        return new GameStatus(null, null, null);
    }
}
