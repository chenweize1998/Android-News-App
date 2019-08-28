package com.example.newstoday.Adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newstoday.News;
import com.example.newstoday.R;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class BaseRecyclerAdapter
        extends RecyclerView.Adapter<BaseRecyclerAdapter.MyViewHolder> {

    protected int mCount;
    private News mNews;
    private int mWidth;

    public BaseRecyclerAdapter(int count, int width, News news) {
        mCount = count;
        mNews = news;
        mWidth = width;
    }

    @NonNull @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MyViewHolder.createViewHolder(parent);
    }

    @Override public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindView(position, mWidth, mNews);
    }

    @Override public int getItemCount() {
        return mCount;
    }

    public void add() {
        int position = mCount;
        mCount++;
        notifyItemInserted(position);
    }

    public void remove() {
        if (mCount == 0) {
            return;
        }
        mCount--;
        int position = mCount;
        notifyItemRemoved(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

//        private final Random random = new Random();
        private ImageView imageView;

        static MyViewHolder createViewHolder(@NonNull ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_pager, parent, false);
            return new MyViewHolder(v);
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.page_image);
        }

        void bindView(int position, int width, News news) {
//            TextView textView = (TextView) itemView;
//            textView.setText(String.valueOf(position + 1));
//            textView.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));
            if(news.getImage()[0] != "")
                Picasso.get().load(news.getImage()[position]).fit().centerCrop().into(imageView);
        }
    }
}