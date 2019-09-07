package com.example.newstoday.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.ArraySet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.newstoday.Adapter.FriendAdapter;
import com.example.newstoday.Adapter.NewsAdapter;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.example.newstoday.User;
import com.example.newstoday.UserManager;
import com.google.android.material.textfield.TextInputEditText;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;

public class FindFriend extends Fragment {
    private UserManager userManager;
    private User currentUser;
    private FragmentManager fragmentManager;

    public FindFriend(){}

    public FindFriend(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_friend, container, false);

        userManager = UserManager.getUserManager(getActivity().getApplicationContext());
        IProfile profile = Table.header.getActiveProfile();
        if(profile != null) {
            currentUser = userManager.getUserByEmail(profile.getEmail().toString())[0];
        }
        else
            currentUser = null;

        RecyclerView recyclerViewNews = view.findViewById(R.id.friend_recycler);
        RecyclerView.LayoutManager layoutManagerNews = new LinearLayoutManager(getContext());
        layoutManagerNews.setItemPrefetchEnabled(true);
        ((LinearLayoutManager) layoutManagerNews).setInitialPrefetchItemCount(20);
        recyclerViewNews.setLayoutManager(layoutManagerNews);
//        User[] users = new User[1];
//        users[0] = new User("wei10@mails.tsinghua.edu.cn", "Weize Chen", "123456",
//                "Hao Peng", "sadf;ljdfbknwero;i");

//        String[] following;
        ArraySet<String> following;
        if(currentUser == null)
            following = new ArraySet<>();
        else {
            following = currentUser.getFollowig();
        }
        final FriendAdapter mAdapter = new FriendAdapter(following, currentUser, getActivity(), fragmentManager);
        recyclerViewNews.setAdapter(mAdapter);

        final TextInputEditText input = view.findViewById(R.id.friend_email);
        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(i == KeyEvent.KEYCODE_ENTER){
                        if(Table.header.getActiveProfile() == null){
                            Toast.makeText(getActivity().getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        String email = input.getText().toString();
                        User friend = userManager.getUserByEmail(email)[0];
                        User[] users = new User[1];
                        users[0] = friend;
                        mAdapter.updateUser(users);
                        mAdapter.notifyDataSetChanged();
                        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });

        return view;
    }
}
