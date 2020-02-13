package com.tehmou.book.androidnewsreaderexample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tehmou.book.androidnewsreaderexample.network.Entry;
import com.tehmou.book.androidnewsreaderexample.network.FeedObservable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<String> feedUrls = Arrays.asList(
                // TODO: 2019/11/8 这个链接不可用 
                "https://news.google.com/?output=atom",
                "http://www.theregister.co.uk/software/headlines.atom",
                // TODO: 2019/11/8 这个链接超时，访问异常
                "http://www.linux.com/news/soware?format=feed&type=atom"
        );

        final List<Observable<List<Entry>>> observableList = new ArrayList<>();
        for (String feedUrl : feedUrls) {
            final Observable<List<Entry>> feedObservable =
                    FeedObservable.getFeed(feedUrl)
                            .onErrorReturn(e -> new ArrayList<Entry>());
            observableList.add(feedObservable);
        }

        Observable<List<Entry>> combinedObservable =
                Observable.combineLatest(observableList,
                        (listOfLists) -> {
                            final List<Entry> combinedList = new ArrayList<>();
                            for (Object list : listOfLists) {
                                combinedList.addAll((List<Entry>) list);
                            }
                            return combinedList;
                        }
                );

        combinedObservable
                .map(list -> {
                    List<Entry> sortedList = new ArrayList<>();
                    sortedList.addAll(list);
                    Collections.sort(sortedList);
                    return sortedList;
                })
                .flatMap(Observable::<Entry>fromIterable)
                .take(6)
                .map(Entry::toString)
                .toList()
                // TODO: 2019/11/8 切换到主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::drawList);
    }

    private void drawList(List<String> listItems) {
        final ListView list = (ListView) findViewById(R.id.list);
        final ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(itemsAdapter);
    }
}
