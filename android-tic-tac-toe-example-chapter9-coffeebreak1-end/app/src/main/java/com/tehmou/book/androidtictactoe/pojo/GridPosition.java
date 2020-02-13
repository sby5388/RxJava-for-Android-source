package com.tehmou.book.androidtictactoe.pojo;

public class GridPosition {
    private final int x;
    private final int y;

    public GridPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "GridPosition(" + x + ", " + y + ")";
    }
}
