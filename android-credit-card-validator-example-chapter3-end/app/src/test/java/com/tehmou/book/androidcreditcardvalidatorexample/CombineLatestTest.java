package com.tehmou.book.androidcreditcardvalidatorexample;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * @author Administrator  on 2019/11/9.
 */
public class CombineLatestTest {


    @Test
    public void combineLatest() {
        // TODO: 2019/11/9 多个重载函数，了解最简单的
        //R:String
        //T:Integer

        final int bufferSize = 10;

        Observable<Integer> integerObservable1 = null;
        Observable<Integer> integerObservable2 = null;


    }

    @Test
    public void zipTest() {
        final Observable<Long> observable = Observable.intervalRange(10, 10, 0, 1, TimeUnit.SECONDS);
        final Observable<Long> observable2 = Observable.intervalRange(10, 10, 0, 2, TimeUnit.SECONDS);


    }
}
