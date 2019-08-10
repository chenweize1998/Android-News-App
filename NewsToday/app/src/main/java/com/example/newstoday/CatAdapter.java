package com.example.newstoday;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.MyViewHolder> {
    private String[] category = {"娱乐", "军事", "教育", "文化",
            "健康", "财经", "体育", "汽车", "科技", "社会"};

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.cat_text);
        }
    }

    @Override
    public CatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_text, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(category[position]);
    }

    @Override
    public int getItemCount() {
        return category.length;
    }
}