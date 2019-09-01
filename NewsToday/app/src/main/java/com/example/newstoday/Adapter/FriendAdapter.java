package com.example.newstoday.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newstoday.Activity.Login;
import com.example.newstoday.Activity.Table;
import com.example.newstoday.R;
import com.example.newstoday.User;
import com.example.newstoday.UserManager;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.Arrays;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {
    private User[] users;
    private UserManager userManager;
    private Activity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView email;
        private ImageView header;
        private ShineButton follow;

        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.friend_name);
            email = v.findViewById(R.id.friend_email);
            follow = v.findViewById(R.id.friend_follow_button);
            header = v.findViewById(R.id.friend_header);
        }
    }

    public FriendAdapter(User[] users, Activity activity){
        this.users = users;
        this.activity = activity;
        userManager = UserManager.getUserManager(activity.getApplicationContext());
    }

    public void updateUser(User user[]){
        users = user;
    }

    @Override
    public FriendAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_item, parent, false);
        FriendAdapter.MyViewHolder vh = new FriendAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FriendAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(users[position].getName());
        holder.email.setText(users[position].getEmail());
        holder.header.setImageResource(R.drawable.header);
        holder.follow.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                if(Table.header.getProfiles().size() == 2){
                    holder.follow.setChecked(false);
                    Toast.makeText(activity.getApplicationContext(), "请先登录", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity.getApplicationContext(), Login.class);
                    activity.startActivityForResult(intent, Table.LOGIN_REQUEST);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return users.length;
    }
}
