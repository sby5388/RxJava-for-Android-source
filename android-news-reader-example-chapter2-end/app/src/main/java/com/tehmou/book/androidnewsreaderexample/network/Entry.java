package com.tehmou.book.androidnewsreaderexample.network;

import android.support.annotation.NonNull;

import java.util.Date;

public class Entry implements Comparable<Entry> {
    public final String id;
    public final String title;
    public final String link;
    public final long updated;

    Entry(String id, String title, String link, long updated) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.updated = updated;
    }

    @Override
    public String toString() {
        return new Date(updated).toString() + "\n" + title;
    }

    @Override
    public int compareTo(@NonNull Entry another) {
        if (this.updated > another.updated) {
            return -1;
        } else if (this.updated < another.updated) {
            return 1;
        }
        return 0;
    }
}
