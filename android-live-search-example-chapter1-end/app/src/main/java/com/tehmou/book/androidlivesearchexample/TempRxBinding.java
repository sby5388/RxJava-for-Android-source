package com.tehmou.book.androidlivesearchexample;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Administrator  on 2019/11/8.
 */
public class TempRxBinding {
    private void testRxTextView(TextView textView) {
        final InitialValueObservable<CharSequence> charSequenceInitialValueObservable = RxTextView.textChanges(textView);

    }

    /**
     * TODO 与原生的RxJava差别在于View对象是如何变成RxView的，变成可以观察的数据源
     * @param editText
     */
    private void testRxEditText(EditText editText) {
        // TODO: 2019/11/8 一个初始化的值？
        final InitialValueObservable<CharSequence> initialValueObservable = RxTextView.textChanges(editText);
        final Observable<CharSequence> charSequenceObservable = initialValueObservable.doOnNext(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                System.out.println("doOnNext ->" + charSequence.toString());
            }
        });
        // TODO: 2019/11/8 过滤
        final Observable<CharSequence> filter = charSequenceObservable.filter(new Predicate<CharSequence>() {
            @Override
            public boolean test(CharSequence charSequence) throws Exception {
                return charSequence.length() > 3;
            }
        });
        // TODO: 2019/11/8 防抖动 ,还有另一个重载方法:500个单位毫秒内只能触发一次
        final Observable<CharSequence> debounce = filter.debounce(500, TimeUnit.MILLISECONDS);
        // TODO: 2019/11/8 工作线程
        final Observable<CharSequence> charSequenceObservable1 = debounce.subscribeOn(Schedulers.io());
        // TODO: 2019/11/8 UI线程
        final Observable<CharSequence> charSequenceObservable2 = charSequenceObservable.observeOn(AndroidSchedulers.mainThread());
        // TODO: 2019/11/8 绑定消费者，开始运行
        final Disposable disposable = charSequenceObservable2.subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                System.out.println("accept -> " + charSequence);
            }
        });
    }

}
