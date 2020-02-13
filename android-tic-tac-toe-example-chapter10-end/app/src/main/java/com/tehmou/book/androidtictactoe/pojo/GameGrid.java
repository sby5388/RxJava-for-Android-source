package com.tehmou.book.androidtictactoe.pojo;

import java.lang.reflect.Array;
import java.util.Arrays;

public class GameGrid {
    private final int width;
    private final int height;
    private final GameSymbol[][] grid;

    public GameGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new GameSymbol[width][height];
        for (int i = 0; i < width; i++) {
            for (int n = 0; n < height; n++) {
                this.grid[i][n] = GameSymbol.EMPTY;
            }
        }
    }

    public GameGrid(int width, int height, GameSymbol[][] grid) {
        this.width = width;
        this.height = height;
        this.grid = grid;
    }

    private GameGrid copy() {
        GameSymbol[][] grid = new GameSymbol[width][height];
        for (int i = 0; i < width; i ++) {
            System.arraycopy(this.grid[i], 0, grid[i], 0, height);
        }
        return new GameGrid(width, height, grid);
    }

    public GameSymbol getSymbolAt(GridPosition gridPosition) {
        return getSymbolAt(gridPosition.getX(), gridPosition.getY());
    }

    public GameSymbol getSymbolAt(int x, int y) {
        return grid[x][y];
    }

    public GameGrid setSymbolAt(GridPosition position, GameSymbol symbol) {
        GameGrid copy = this.copy();
        copy.grid[position.getX()][position.getY()] = symbol;
        return copy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GameGrid &&
                ((GameGrid) obj).width == width &&
                ((GameGrid) obj).height == height &&
                contentEquals(((GameGrid) obj).grid);
    }

    private boolean contentEquals(GameSymbol[][] grid) {
        if (this.grid.length != grid.length) {
            return false;
        }
        for (int i = 0; i < grid.length; i++) {
            if (!Arrays.equals(this.grid[i], grid[i])) {
                return false;
            }
        }
        return true;
    }
}
