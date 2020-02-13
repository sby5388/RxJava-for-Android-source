package com.tehmou.book.androidflickrclientexample.pojo;

import com.tehmou.book.androidflickrclientexample.network.FlickrPhotoInfoResponse;
import com.tehmou.book.androidflickrclientexample.network.FlickrPhotosGetSizesResponse;

import java.util.List;

public class Photo {
    final private String id;
    final private String title;
    final private String username;
    final private String thumbnailUrl;

    public Photo(String id, String title, String username, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public static Photo createPhoto(FlickrPhotoInfoResponse.PhotoInfo photoInfo,
                                    List<FlickrPhotosGetSizesResponse.Size> sizes) {
        String thumbnailUrl = null;
        for (FlickrPhotosGetSizesResponse.Size size : sizes) {
            if (size.getLabel().equals("Square")) {
                thumbnailUrl = size.getSource();
                break;
            }
        }
        return new Photo(photoInfo.getId(), photoInfo.getTitle(), photoInfo.getUsername(), thumbnailUrl);
    }
}
