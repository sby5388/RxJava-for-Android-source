package com.tehmou.book.androidtictactoe.load;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tehmou.book.androidtictactoe.pojo.SavedGame;

import java.io.File;
import java.util.Date;
import java.util.List;

public class SavedGamesListAdapter extends ArrayAdapter<SavedGame> {
    public SavedGamesListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public SavedGamesListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SavedGamesListAdapter(Context context, int resource, SavedGame[] objects) {
        super(context, resource, objects);
    }

    public SavedGamesListAdapter(Context context, int resource, int textViewResourceId, SavedGame[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SavedGamesListAdapter(Context context, int resource, List<SavedGame> objects) {
        super(context, resource, objects);
    }

    public SavedGamesListAdapter(Context context, int resource, int textViewResourceId, List<SavedGame> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        final SavedGame savedGame = getItem(position);
        ((TextView) convertView).setText(new Date(savedGame.getTimestamp()).toString());
        convertView.setTag(savedGame);
        return convertView;
    }
}
