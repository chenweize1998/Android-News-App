package com.example.newstoday;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import android.util.DisplayMetrics;

import static java.security.AccessController.getContext;

public class NewsPageAdapter extends RecyclerView.Adapter <NewsPageAdapter.MyViewHolder> {
    private News news;
    private int height;
    private int width;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title;
        private TextView publisher;
        private TextView content;
        public MyViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.page_top_pic);
            title = v.findViewById(R.id.page_title);
            publisher = v.findViewById(R.id.page_publisher);
            content = v.findViewById(R.id.page_content);
        }
    }

    public NewsPageAdapter(News news, int height, int width) {
        this.news = news;
        this.width = width;
        this.height = height;
    }

    @Override
    public NewsPageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_content, parent, false);
        NewsPageAdapter.MyViewHolder vh = new NewsPageAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NewsPageAdapter.MyViewHolder holder, final int position) {
        if(!news.getImage()[0].equals(""))
            Picasso.get().load(news.getImage()[0]).resize(width, 500).centerCrop().into(holder.imageView);
        else
            holder.imageView.setImageResource(R.mipmap.default_pic);

        holder.title.setText(news.getTitle());
        holder.title.setTextColor(0xFF000000);

        holder.publisher.setText(news.getPublisher());
        holder.publisher.setTextColor(0xAA878787);

        holder.content.setText(news.getContent());
        holder.content.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}