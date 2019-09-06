package com.example.newstoday.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newstoday.Adapter.NewsAdapter;
import com.example.newstoday.News;
import com.example.newstoday.R;
import com.example.newstoday.User;
import com.example.newstoday.UserManager;
import com.example.newstoday.UserMessageManager;

import java.util.ArrayList;

import static com.example.newstoday.Activity.Table.PICK_IMAGE;

public class PersonalHomepage extends Fragment {
    private FragmentManager fragmentManager;
    private User user;
    private UserManager userManager;
    private UserMessageManager userMessageManager;
    private ArrayList<News> news;
    private View view;

    public PersonalHomepage() {}

    public PersonalHomepage(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    public PersonalHomepage(User user, FragmentManager fragmentManager){
        this.user = user;
        this.fragmentManager = fragmentManager;
    }

    public void updateHeader(Uri uri){
        ImageView header = view.findViewById(R.id.homepage_header);
        Glide.with(getActivity()).load(uri).into(header);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_homepage, container, false);
        userManager = UserManager.getUserManager(getActivity().getApplicationContext());
        userMessageManager = UserMessageManager.getUserMessageManager(getActivity().getApplicationContext());
        User currentUser = null;
        if(Table.header.getActiveProfile() != null)
            currentUser = userManager.getUserByEmail(Table.header.getActiveProfile().getEmail().toString())[0];

        TextView name = view.findViewById(R.id.homepage_name);
        name.setText(user.getName());
        TextView email = view.findViewById(R.id.homepage_email);
        email.setText(user.getEmail());
        ImageView header = view.findViewById(R.id.homepage_header);
        Glide.with(getActivity().getApplicationContext()).load(Uri.parse(user.getAvatar())).into(header);
        if(currentUser != null && currentUser.getEmail().equals(user.getEmail())) {
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            });
        }

        NewsAdapter.OnItemClickListener listenerNews = new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, final View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), NewsPage.class);
                intent.putExtra("news", news.get(position));
                startActivity(intent);
            }
        };

        news = userMessageManager.getCerternUserMessageByUserEmail(user.getEmail());
        RecyclerView recyclerView = view.findViewById(R.id.homepage_content_recycler);
        LinearLayoutManager layoutManagerNews = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManagerNews);
        NewsAdapter mAdapterNews = new NewsAdapter(news, getActivity(), this.fragmentManager, true,
                currentUser == null?false:currentUser.getEmail().equals(user.getEmail()));
        mAdapterNews.setOnItemClickListener(listenerNews);
        recyclerView.setAdapter(mAdapterNews);

        return view;
    }

}
