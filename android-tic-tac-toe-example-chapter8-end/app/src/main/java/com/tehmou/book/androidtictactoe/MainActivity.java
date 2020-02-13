package com.tehmou.book.androidtictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.tehmou.book.androidtictactoe.pojo.GameStatus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CompositeDisposable viewSubscriptions = new CompositeDisposable();
    private GameViewModel gameViewModel;

    private InteractiveGameGridView gameGridView;
    private PlayerView playerInTurnImageView;
    private View winnerView;
    private TextView winnerTextView;
    private Button newGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameGridView = (InteractiveGameGridView) findViewById(R.id.grid_view);
        playerInTurnImageView = (PlayerView) findViewById(R.id.player_in_turn_image_view);
        winnerView = findViewById(R.id.winner_view);
        winnerTextView = (TextView) findViewById(R.id.winner_text_view);
        newGameButton = (Button) findViewById(R.id.new_game_button);

        gameViewModel = new GameViewModel(
                gameGridView.getTouchesOnGrid(),
                RxView.clicks(newGameButton)
        );
        gameViewModel.subscribe();
        makeViewBinding();
    }

    private void makeViewBinding() {
        viewSubscriptions.add(
            gameViewModel.getGameGrid()
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
}
