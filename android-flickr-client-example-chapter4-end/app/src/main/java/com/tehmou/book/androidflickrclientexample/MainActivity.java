package com.tehmou.book.androidflickrclientexample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tehmou.book.androidflickrclientexample.network.FlickrApi;
import com.tehmou.book.androidflickrclientexample.network.FlickrPhotoInfoResponse;
import com.tehmou.book.androidflickrclientexample.network.FlickrPhotosGetSizesResponse;
import com.tehmou.book.androidflickrclientexample.network.FlickrSearchResponse;
import com.tehmou.book.androidflickrclientexample.pojo.Photo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Retrofit restAdapter = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.flickr.com")
                .build();

        final FlickrApi api = restAdapter.create(FlickrApi.class);
        // TODO: 2019/11/25 直接引用app#build.gradle之中定义的全局变量
        final String apiKey = BuildConfig.FLICKR_API_KEY;

        final Button searchButton = (Button) findViewById(R.id.search_button);
        final Observable<Object> buttonClickObservable = RxView.clicks(searchButton);

        final TextView searchTextView = (TextView) findViewById(R.id.search_text);
        final Observable<String> searchTextInput =
                RxTextView.textChanges(searchTextView).map(CharSequence::toString);

        // TODO: 2019/11/25 这些非耗时操作的，也不需要切换到后台线程及主线程，线程切换消耗比较大
        searchTextInput
                .map(searchText -> searchText.length() >= 3)
                .subscribe(searchButton::setEnabled);

        buttonClickObservable
                .doOnNext(e -> Log.d(TAG, "Search button clicked"))
                // TODO: 2019/11/25 这个API 的作用是什么
                .withLatestFrom(searchTextInput, (e, searchText) -> searchText)
                .doOnNext(searchText -> Log.d(TAG, "Start search with '" + searchText + "'"))
                .flatMap(searchText ->
                        api.searchPhotos(apiKey, searchText, 10)
                                .subscribeOn(Schedulers.io()))
                .map(FlickrSearchResponse::getPhotos)
                .doOnNext(photos -> Log.d(TAG, "Found " + photos.size() + " photos to process"))
                .flatMap((Function<List<FlickrSearchResponse.Photo>, Observable<List<Photo>>>) photos -> {
                    if (photos.size() > 0) {
                        return Observable.fromIterable(photos)
                                .doOnNext(photo -> Log.d(TAG, "Processing photo  " + photo.getId()))
                                // TODO: 2019/11/25 concatMap ??
                                .concatMap(photo ->
                                        // TODO: 2019/11/25 combineLatest ??
                                        Observable.combineLatest(
                                                api.photoInfo(apiKey, photo.getId())
                                                        .subscribeOn(Schedulers.io())
                                                        .map(FlickrPhotoInfoResponse::getPhotoInfo),
                                                api.getSizes(apiKey, photo.getId())
                                                        .subscribeOn(Schedulers.io())
                                                        .map(FlickrPhotosGetSizesResponse::getSizes),
                                                Photo::createPhoto))
                                .doOnNext(photo -> Log.d(TAG, "Finished processing photo " + photo.getId()))
                                .toList()
                                .doOnSuccess(photo -> Log.d(TAG, "Finished processing all photos"))
                                .toObservable();

                    } else {
                        return Observable.just(new ArrayList<>());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        photos -> {
                            final RecyclerView rv = (RecyclerView) findViewById(R.id.main_list);
                            rv.setLayoutManager(new LinearLayoutManager(this));

                            Log.d(TAG, "Found " + photos.size() + " photos");
                            final PhotoAdapter photoAdapter = new PhotoAdapter(this, photos);
                            rv.setAdapter(photoAdapter);
                        },
                        e -> Log.e(TAG, "Error getting photos", e));
    }
}
