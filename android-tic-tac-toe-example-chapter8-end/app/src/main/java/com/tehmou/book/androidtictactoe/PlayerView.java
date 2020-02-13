package com.tehmou.book.androidtictactoe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tehmou.book.androidtictactoe.pojo.GameSymbol;

public class PlayerView extends ImageView {
    public PlayerView(Context context) {
        super(context);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(GameSymbol gameSymbol) {
        switch(gameSymbol) {
            case CIRCLE:
                setImageResource(R.drawable.symbol_circle);
                break;
            case CROSS:
                setImageResource(R.drawable.symbol_cross);
                break;
            default:
                setImageResource(0);
        }
    }
}
