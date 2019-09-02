package com.example.newstoday.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.newstoday.Adapter.FriendAdapter;
import com.example.newstoday.Adapter.NewsAdapter;
import com.example.newstoday.News;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.example.newstoday.User;
import com.example.newstoday.UserManager;
import com.google.android.material.textfield.TextInputEditText;
import com.sackcentury.shinebuttonlib.ShineButton;

public class FindFriend extends Fragment {
    private UserManager userManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_friend, container, false);

        userManager = UserManager.getUserManager(getActivity().getApplicationContext());

        RecyclerView recyclerViewNews = view.findViewById(R.id.friend_recycler);
        RecyclerView.LayoutManager layoutManagerNews = new LinearLayoutManager(getContext());
        layoutManagerNews.setItemPrefetchEnabled(true);
        ((LinearLayoutManager) layoutManagerNews).setInitialPrefetchItemCount(20);
        recyclerViewNews.setLayoutManager(layoutManagerNews);
//        User[] users = new User[1];
//        users[0] = new User("wei10@mails.tsinghua.edu.cn", "Weize Chen", "123456",
//                "Hao Peng", "sadf;ljdfbknwero;i");
        User[] users = userManager.getAllUsers();

        final FriendAdapter mAdapter = new FriendAdapter(userManager.getAllUsers(), getActivity());
        recyclerViewNews.setAdapter(mAdapter);

        final TextInputEditText input = view.findViewById(R.id.friend_email);
        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(i == KeyEvent.KEYCODE_ENTER){
                        String email = input.getText().toString();
                        User friend = userManager.getUserByEmail(email);
                        User[] users = new User[1];
                        users[0] = friend;
                        mAdapter.updateUser(users);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });

        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
}
