package com.tehmou.book.androidflickrclientexample.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrApi {
    @GET("/services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1")
    Observable<FlickrSearchResponse> searchPhotos(@Query("api_key") String apiKey,
                                                  @Query("tags") String tags,
                                                  @Query("per_page") int limit);

    @GET("/services/rest/?method=flickr.photos.getInfo&format=json&nojsoncallback=1")
    Observable<FlickrPhotoInfoResponse> photoInfo(@Query("api_key") String apiKey,
                                                  @Query("photo_id") String photoId);

    @GET("/services/rest/?method=flickr.photos.getSizes&format=json&nojsoncallback=1")
    Observable<FlickrPhotosGetSizesResponse> getSizes(@Query("api_key") String apiKey,
                                                      @Query("photo_id") String photoId);
}
