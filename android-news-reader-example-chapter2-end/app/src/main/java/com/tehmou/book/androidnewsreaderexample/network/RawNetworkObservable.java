package com.tehmou.book.androidnewsreaderexample.network;

import android.util.Log;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RawNetworkObservable {
    private static final String TAG = RawNetworkObservable.class.getSimpleName();

    private RawNetworkObservable() {

    }

    public static Observable<Response> create(final String url) {
        return Observable.create(
                new ObservableOnSubscribe<Response>() {
                    final OkHttpClient client = new OkHttpClient();

                    @Override
                    public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                        try {
                            Response response = client.newCall(new Request.Builder().url(url).build()).execute();
                            emitter.onNext(response);
                            emitter.onComplete();
                            if (!response.isSuccessful()) {
                                // TODO: 2019/11/8 这里出现了闪退现象
                                emitter.onError(new Exception("error"));
                            }
                        } catch (IOException e) {
                            emitter.onError(e);
                        }

                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<String> getString(String url) {
        return create(url)
                .map(response -> {
                    try {
                        return response.body().string();
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading url " + url);
                    }
                    return null;
                });
    }

    /**
     * todo 没有与Retrofit、RxJava相结合的情况下的使用
     *
     * @param client
     */
    private void tempOkHttpClient(OkHttpClient client, String httpUrl) throws IOException {

        final Observable<Response> responseObservable = Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> e) throws Exception {
                // TODO: 2019/11/8 添加请求内容
                final Request request = new Request.Builder().url(httpUrl).build();

                final Call call = client.newCall(request);
                // TODO: 2019/11/8 请求结果(异步)
                final Response response = call.execute();
                e.onNext(response);
                if (response.isSuccessful()) {
                    e.onComplete();
                } else {
                    e.onError(new Exception("!response.isSuccessful()"));
                }

            }
        });
        responseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {
                        final ResponseBody body = response.body();
                        // TODO: 2019/11/8 实际访问的结果类型多种多样,结合实际结果来处理
                        final byte[] bytes = body.bytes();
                        final String result = new String(bytes);
                        System.out.println(result);
                    }
                });

    }
}
