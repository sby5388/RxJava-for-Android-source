package com.tehmou.book.androidflickrclientexample.network;

import java.util.List;

public class FlickrPhotosGetSizesResponse {
    private final Sizes sizes;

    public FlickrPhotosGetSizesResponse(Sizes sizes) {
        this.sizes = sizes;
    }

    public List<Size> getSizes() {
        return sizes.size;
    }

    public static class Sizes {
        private final List<Size> size;

        public Sizes(List<Size> size) {
            this.size = size;
        }
    }

    public static class Size {
        private final String label;
        private final int width;
        private final int height;
        private final String source;

        public Size(String label, int width, int height, String source) {
            this.label = label;
            this.width = width;
            this.height = height;
            this.source = source;
        }

        public String getLabel() {
            return label;
        }

        public String getSource() {
            return source;
        }
    }
}
