package com.tehmou.book.androidtictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.tehmou.book.androidtictactoe.data.GameModel;
import com.tehmou.book.androidtictactoe.load.LoadGameActivity;
import com.tehmou.book.androidtictactoe.pojo.GameStatus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CompositeDisposable viewSubscriptions = new CompositeDisposable();
    private GameModel gameModel;
    private GameViewModel gameViewModel;

    private InteractiveGameGridView gameGridView;
    private PlayerView playerInTurnImageView;
    private View winnerView;
    private TextView winnerTextView;
    private Button newGameButton;
    private Button saveGameButton;
    private Button loadGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the shared GameModel
        gameModel = ((GameApplication) getApplication()).getGameModel();

        resolveViews();
        createViewModel();
        makeViewBinding();
    }

    private void resolveViews() {
        gameGridView = (InteractiveGameGridView) findViewById(R.id.grid_view);
        playerInTurnImageView = (PlayerView) findViewById(R.id.player_in_turn_image_view);
        winnerView = findViewById(R.id.winner_view);
        winnerTextView = (TextView) findViewById(R.id.winner_text_view);
        newGameButton = (Button) findViewById(R.id.new_game_button);
        saveGameButton = (Button) findViewById(R.id.save_game_button);
        loadGameButton = (Button) findViewById(R.id.load_game_button);
    }

    private void createViewModel() {
        gameViewModel = new GameViewModel(
                gameModel.getActiveGameState(),
                gameModel::putActiveGameState,
                gameGridView.getTouchesOnGrid()
        );
        gameViewModel.subscribe();
    }

    private void makeViewBinding() {
        // Handle new game, saving and loading games
        viewSubscriptions.add(RxView.clicks(newGameButton)
                .subscribe(ignore -> gameModel.newGame())
        );
        viewSubscriptions.add(RxView.clicks(saveGameButton)
                .subscribe(ignore -> gameModel.saveActiveGame())
        );
        viewSubscriptions.add(RxView.clicks(loadGameButton)
                .subscribe(ignore -> showLoadGameActivity())
        );

        // Bind the View Model
        viewSubscriptions.add(
            gameViewModel.getFullGameState()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(gameGridView::setData)
        );

        viewSubscriptions.add(
            gameViewModel.getPlayerInTurn()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(playerInTurnImageView::setData)
        );

        viewSubscriptions.add(
            gameViewModel.getGameStatus()
                    .map(GameStatus::isEnded)
                    .map(isEnded -> isEnded ? View.VISIBLE : View.GONE)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(winnerView::setVisibility)
        );

        viewSubscriptions.add(
            gameViewModel.getGameStatus()
                    .map(gameStatus ->
                            gameStatus.isEnded() ?
                                    "Winner: " + gameStatus.getWinner() : "")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(winnerTextView::setText)
        );
    }

    private void releaseViewBinding() {
        viewSubscriptions.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseViewBinding();
        gameViewModel.unsubscribe();
    }

    private void showLoadGameActivity() {
        Intent intent = new Intent(this, LoadGameActivity.class);
        startActivity(intent);
    }
}
