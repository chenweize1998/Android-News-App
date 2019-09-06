package com.example.newstoday.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.example.newstoday.Activity.Login;
import com.example.newstoday.Activity.PersonalHomepage;
import com.example.newstoday.Activity.Table;
import com.example.newstoday.AsyncServerNews;
import com.example.newstoday.R;
import com.example.newstoday.User;
import com.example.newstoday.UserManager;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {
    private User[] users;
    private UserManager userManager;
    private Activity activity;
    private User currentUser;
    private AsyncServerNews asyncServerNews;
    public FragmentManager fragmentManager;

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

    public FriendAdapter(ArraySet<String> emails, User currentUser, Activity activity, FragmentManager fragmentManager){
//        this.users = users;
        this.currentUser = currentUser;
        this.fragmentManager = fragmentManager;
//        if(emails.length == 0)
//            users = new User[0];
//        else
//            users = userManager.getUserByEmail(emails);
        userManager = UserManager.getUserManager(activity.getApplicationContext());
        if(emails == null) {
            users = new User[0];
        }
        else {
            users = userManager.getUserByEmail(emails.toArray(new String[emails.size()]));
        }
        this.activity = activity;
        asyncServerNews = AsyncServerNews.getAsyncServerNews(activity.getApplicationContext());
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
        Picasso.get().load(userManager.getUserByEmail(users[position].getEmail())[0].getAvatar())
                .into(holder.header);
        if(currentUser != null && currentUser.getFollowig() != null &&
                currentUser.getFollowig().contains(users[position].getEmail()))
            holder.follow.setChecked(true);
        else
            holder.follow.setChecked(false);
        holder.follow.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                if(currentUser == null){
                    holder.follow.setChecked(false);
                    Toast.makeText(activity.getApplicationContext(), "关注失败，请先登录", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(activity.getApplicationContext(), Login.class);
//                    activity.startActivityForResult(intent, Table.LOGIN_REQUEST);
                }
                else{
                    if(checked) {
//                        currentUser.addFollowig(users[position].getEmail());
                        currentUser.addFollowig(users[position].getEmail());
                    }
                    else {
                        currentUser.deleteFollowig(users[position].getEmail());
                    }
                    userManager.updateUser(currentUser);//更新到数据库
                    asyncServerNews.asyncUserToServer(currentUser);//更新到服务器
                }
            }
        });

        holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                PersonalHomepage homepage = new PersonalHomepage(
                        userManager.getUserByEmail(users[position].getEmail())[0],
                        fragmentManager
                );
                fragmentTransaction.replace(R.id.table_fragment, homepage);
//                if(fragmentManager.getBackStackEntryCount() == 0)
                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return users.length;
    }
}
