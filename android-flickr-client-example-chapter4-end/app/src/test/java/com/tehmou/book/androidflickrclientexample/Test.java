package com.tehmou.book.androidflickrclientexample;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * @author Administrator  on 2019/11/25.
 */
public class Test {
    @org.junit.Test
    public void testThread() {
        final Disposable d = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("start");
                e.onNext("finish");
                e.onComplete();
            }
        }).doOnNext(s -> System.out.println("onNext thread = " + Thread.currentThread().getName()))
                .subscribe(s -> System.out.println("onNext thread = " + Thread.currentThread().getName() + ",s = " + s),
                        e -> System.err.println(e.getLocalizedMessage()),
                        () -> System.out.println("onFinish thread = " + Thread.currentThread().getName()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println(e.getLocalizedMessage());
        }

    }
}
