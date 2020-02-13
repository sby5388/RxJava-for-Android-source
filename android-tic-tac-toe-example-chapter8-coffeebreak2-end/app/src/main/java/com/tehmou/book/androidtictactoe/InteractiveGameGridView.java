package com.tehmou.book.androidtictactoe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jakewharton.rxbinding2.view.RxView;
import com.tehmou.book.androidtictactoe.pojo.GridPosition;

import io.reactivex.Observable;

public class InteractiveGameGridView extends GameGridView {
    public InteractiveGameGridView(Context context) {
        super(context);
    }

    public InteractiveGameGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Observable<GridPosition> getTouchesOnGrid() {
        Observable<MotionEvent> userTouchEventObservable =
                RxView.touches(this)
                        .filter(ev -> ev.getAction() == MotionEvent.ACTION_UP);

        Observable<GridPosition> gridPositionEventObservable =
                userTouchEventObservable
                        .map(ev ->
                                getGridPosition(
                                        ev.getX(), ev.getY(),
                                        getWidth(), getHeight(),
                                        getGridWidth(),
                                        getGridHeight()));

        return gridPositionEventObservable;
    }

    private static GridPosition getGridPosition(float touchX, float touchY,
                                                int viewWidthPixels, int viewHeightPixels,
                                                int gridWidth, int gridHeight) {
        float rx = touchX /
                (float)(viewWidthPixels+1);
        int i = (int)(rx * gridWidth);

        float ry = touchY /
                (float)(viewHeightPixels+1);
        int n = (int)(ry * gridHeight);

        return new GridPosition(i, n);
    }
}
