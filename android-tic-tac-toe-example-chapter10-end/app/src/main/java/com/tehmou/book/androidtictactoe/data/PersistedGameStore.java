package com.tehmou.book.androidtictactoe.data;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tehmou.book.androidtictactoe.pojo.GameState;
import com.tehmou.book.androidtictactoe.pojo.SavedGame;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class PersistedGameStore {
    private static final String TAG = PersistedGameStore.class.getSimpleName();

    private Gson gson = new Gson();
    private final SharedPreferences sharedPreferences;
    private List<SavedGame> savedGames = new ArrayList<>();
    private final PublishSubject<List<SavedGame>> savedGamesSubject = PublishSubject.create();

    public PersistedGameStore(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        String gamesJson = sharedPreferences.getString("saved_games", "[]");
        try {
            savedGames = gson.fromJson(gamesJson, new TypeToken<List<SavedGame>>(){}.getType());
            Log.d(TAG, "Loaded " + savedGames.size() + " games");
        } catch (JsonSyntaxException e) {
            Log.d(TAG, "Failed to load games");
        }
    }

    public Observable<List<SavedGame>> getSavedGamesStream() {
        return savedGamesSubject.hide().startWith(savedGames);
    }

    public Observable<Void> put(GameState gameState) {
        final long timestamp = new Date().getTime();
        final SavedGame savedGame = new SavedGame(gameState, timestamp);
        savedGames.add(savedGame);
        persistGames();
        savedGamesSubject.onNext(savedGames);
        return Observable.empty();
    }

    private void persistGames() {
        String jsonString = gson.toJson(savedGames);
        sharedPreferences.edit()
                .putString("saved_games", jsonString)
                .commit();
        Log.d(TAG, "Games saved");
    }
}
