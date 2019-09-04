package com.example.newstoday.Adapter;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newstoday.CustomLayout.GifSizeFilter;
import com.example.newstoday.CustomLayout.PicassoEngine;
import com.example.newstoday.R;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.newstoday.Activity.NewsItem.PUBLISH_CHOOSE_IMAGE;

public class PublishImageAdapter extends RecyclerView.Adapter<PublishImageAdapter.MyViewHolder> {
    private ArrayList<Uri> images;
    private Activity activity;
    private final Uri ADD_PIC = Uri.parse("android.resource://com.example.newstoday/"  + R.drawable.plus_image);
    private int width;
    private int height;
    boolean add;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public MyViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.publish_image);
        }
    }

    public PublishImageAdapter(ArrayList<Uri> images, Activity activity){
        if(images.size() < 9) {
            images.add(ADD_PIC);
            add = true;
        }
        else
            add = false;
        this.images = images;
        this.activity = activity;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = ((displayMetrics.widthPixels) - (2 * 20)) / 3;
        height = width;
    }

    public void updateImages(ArrayList<Uri> images){
        if(images.size() < 9) {
            images.add(ADD_PIC);
            add = true;
        }
        else
            add = false;
        this.images = images;
    }

    @Override
    public PublishImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.publish_image, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if(getItemCount() != 9 && position != getItemCount() - 1)
        holder.imageView.getLayoutParams().width = width;
        holder.imageView.getLayoutParams().height = height;
        Picasso.get().load(images.get(position)).into(holder.imageView);
        holder.imageView.setOnClickListener(null);
        if(position == getItemCount() - 1 && add){
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Matisse.from(activity)
                        .choose(MimeType.ofAll())
                        .countable(true)
                        .maxSelectable(9)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(activity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new PicassoEngine())
                        .forResult(PUBLISH_CHOOSE_IMAGE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}