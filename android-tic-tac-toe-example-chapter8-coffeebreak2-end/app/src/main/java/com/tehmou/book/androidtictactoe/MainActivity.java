package com.tehmou.book.androidtictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import android.widget.TextView;

import com.tehmou.book.androidtictactoe.pojo.GridPosition;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InteractiveGameGridView gameGridView =
                (InteractiveGameGridView) findViewById(R.id.grid_view);

        PlayerView playerInTurnImageView =
                (PlayerView) findViewById(R.id.player_in_turn_image_view);

        TextView lastMoveTextView =
                (TextView) findViewById(R.id.last_move_text);

        gameViewModel = new GameViewModel(
                gameGridView.getTouchesOnGrid()
        );

        gameViewModel.getGameGrid()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gameGridView::setData);

        gameViewModel.getPlayerInTurn()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playerInTurnImageView::setData);

        gameViewModel.getLastMove()
                .map(GridPosition::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lastMoveTextView::setText);

        gameViewModel.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameViewModel.unsubscribe();
    }
}
