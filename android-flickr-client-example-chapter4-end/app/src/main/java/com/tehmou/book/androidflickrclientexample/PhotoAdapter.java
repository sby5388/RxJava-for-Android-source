package com.tehmou.book.androidflickrclientexample;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tehmou.book.androidflickrclientexample.pojo.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    final private Context context;
    final private List<Photo> photos;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_list_card_view, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder photoViewHolder, int position) {
        final Photo photo = photos.get(position);
        photoViewHolder.personName.setText(photo.getTitle());
        photoViewHolder.personAge.setText(photo.getUsername());
        // TODO: 2019/11/9 这个网站Api的使用与Android编程权威指南第三版有点类似 ，也是使用了Picasso这个
        //  工具类来加载图片
        Picasso.with(context).load(photo.getThumbnailUrl())
                .into(photoViewHolder.personPhoto);
        //photoViewHolder.personPhoto.setImageResource(photos.get(position).photoId);
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private final CardView cv;
        private final TextView personName;
        private final TextView personAge;
        private final ImageView personPhoto;

        PhotoViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personAge = (TextView) itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
        }

        private void bind(Context context, Photo photo) {
            this.personName.setText(photo.getTitle());
            this.personAge.setText(photo.getUsername());
            Picasso.with(context).load(photo.getThumbnailUrl())
                    .into(this.personPhoto);
        }
    }
}
