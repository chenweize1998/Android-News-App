package com.example.newstoday.Adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newstoday.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private ArrayList<String> comments;
    private Activity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView header;
        private TextView email;
        private TextView name;
        private TextView comment;
        public MyViewHolder(View v) {
            super(v);
            header = v.findViewById(R.id.page_comment_header);
            email = v.findViewById(R.id.page_comment_email);
            name = v.findViewById(R.id.page_comment_name);
            comment = v.findViewById(R.id.page_comment);
        }
    }

    public CommentAdapter(ArrayList<String> comments, Activity activity){
        this.comments = comments;
        this.activity = activity;
    }

    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

}