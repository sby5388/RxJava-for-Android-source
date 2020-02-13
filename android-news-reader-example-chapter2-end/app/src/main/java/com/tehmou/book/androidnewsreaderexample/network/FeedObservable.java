package com.tehmou.book.androidnewsreaderexample.network;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class FeedObservable {
    private static final String TAG = FeedObservable.class.getSimpleName();

    private FeedObservable() {
        throw new RuntimeException();
    }

    public static Observable<List<Entry>> getFeed(final String url) {
        return RawNetworkObservable.create(url)
                .map(response -> {
                    // TODO: 2019/11/8 Response-->转换成实体类
                    FeedParser parser = new FeedParser();
                    try {
                        // TODO: 2019/11/27 xml解释器
                        List<Entry> entries = parser.parse(response.body().byteStream());
                        Log.v(TAG, "Number of entries from url " + url + ": " + entries.size());
                        return entries;
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing feed", e);
                    }
                    return new ArrayList<>();
                });
    }
}
