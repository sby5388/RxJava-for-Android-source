package com.tehmou.book.androidtictactoe.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.tehmou.book.androidtictactoe.pojo.GameGrid;
import com.tehmou.book.androidtictactoe.pojo.GameState;
import com.tehmou.book.androidtictactoe.pojo.GameSymbol;
import com.tehmou.book.androidtictactoe.pojo.SavedGame;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class GameModel {
    private static final String SAVED_GAMES_FILE_NAME = "saved_games";

    private static final int GRID_WIDTH = 7;
    private static final int GRID_HEIGHT = 7;
    private static final GameGrid EMPTY_GRID = new GameGrid(GRID_WIDTH, GRID_HEIGHT);
    private static final GameState EMPTY_GAME = new GameState(EMPTY_GRID, GameSymbol.EMPTY);

    private final BehaviorSubject<GameState> activeGameState = BehaviorSubject.createDefault(EMPTY_GAME);
    private final PersistedGameStore persistedGameStore;

    public GameModel(Context context) {
        SharedPreferences savedGames = context.getSharedPreferences(SAVED_GAMES_FILE_NAME, 0);
        persistedGameStore = new PersistedGameStore(savedGames);
    }

    public void newGame() {
        activeGameState.onNext(EMPTY_GAME);
    }

    public void putActiveGameState(GameState value) {
        activeGameState.onNext(value);
    }

    public Observable<GameState> getActiveGameState() {
        return activeGameState.hide();
    }

    public Observable<List<SavedGame>> getSavedGamesStream() {
        return persistedGameStore.getSavedGamesStream();
    }

    public Observable<Void> saveActiveGame() {
        return persistedGameStore.put(activeGameState.getValue());
    }
}
