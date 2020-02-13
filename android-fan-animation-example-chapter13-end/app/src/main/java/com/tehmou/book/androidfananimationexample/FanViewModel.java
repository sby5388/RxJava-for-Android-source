package com.tehmou.book.androidfananimationexample;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class FanViewModel {
    private static final String TAG = FanViewModel.class.getSimpleName();

    private final BehaviorSubject<List<FanItem>> fanItems;
    private final BehaviorSubject<Boolean> isOpen = BehaviorSubject.createDefault(false);
    private final BehaviorSubject<Float> openRatio = BehaviorSubject.create();

    public FanViewModel(Observable<Object> clickObservable,
                        List<FanItem> fanItems) {
        this.fanItems = BehaviorSubject.createDefault(fanItems);
        clickObservable
                .doOnNext(click -> Log.d(TAG, "click"))
                .subscribe(click -> isOpen.onNext(!isOpen.getValue()));
         AnimateToOperator.animate(
                 isOpen.map(value -> value ? 1f : 0f), 200)
                 .doOnNext(value -> Log.d(TAG, "value: " + value))
                 .subscribe(openRatio::onNext);
    }

    public Observable<Float> getOpenRatio() {
        return openRatio;
    }

    public Observable<List<FanItem>> getFanItems() {
        return fanItems;
    }
}
