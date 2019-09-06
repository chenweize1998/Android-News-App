package com.example.newstoday.Adapter;


import android.app.Activity;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newstoday.R;
import com.example.newstoday.User;
import com.example.newstoday.UserManager;

import java.util.ArrayList;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private ArrayList<String> emails;
    private ArrayList<String> comments;
    private User[] users;
    private Activity activity;
    private UserManager userManager;

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

    public CommentAdapter(ArrayList<String> emails, ArrayList<String> comments, Activity activity){
        this.emails = emails;
        this.comments = comments;
        this.activity = activity;
        userManager = UserManager.getUserManager(activity.getApplicationContext());
        users = userManager.getUserByEmail(emails.toArray(new String[emails.size()]));
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
        if(users.length != 0) {
            Glide.with(activity).load(users[position].getAvatar()).into(holder.header);
            holder.email.setText(users[position].getEmail());
            holder.name.setText(users[position].getName());
            holder.comment.setText(comments.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

}