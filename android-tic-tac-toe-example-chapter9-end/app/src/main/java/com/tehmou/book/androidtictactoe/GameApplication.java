package com.tehmou.book.androidtictactoe;

import android.app.Application;

import com.tehmou.book.androidtictactoe.data.GameModel;

public class GameApplication extends Application {
    private GameModel gameModel;

    @Override
    public void onCreate() {
        super.onCreate();
        gameModel = new GameModel(this);
    }

    public GameModel getGameModel() {
        return gameModel;
    }
}
