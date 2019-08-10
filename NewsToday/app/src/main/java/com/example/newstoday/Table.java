package com.example.newstoday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

public class Table extends AppCompatActivity {
    private RecyclerView recyclerViewNews;
    private RecyclerView.Adapter mAdapterNews;
    private RecyclerView.LayoutManager layoutManagerNews;
    private RecyclerView recyclerViewCat;
//    private RecyclerView.Adapter mAdapterCat;
    private CatAdapter mAdapterCat;
    private RecyclerView.LayoutManager layoutManagerCat;
    private String[] title, abs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_activity);

        title = new String[20];
        abs = new String[20];
        for(int i = 0; i < 20; ++i)
        {
            title[i] = "Title" + i;
            abs[i] = "Abstract" + i;
        }

        recyclerViewCat = findViewById(R.id.cat_recycler_view);
        layoutManagerCat = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManagerCat).setOrientation(LinearLayout.HORIZONTAL);
        recyclerViewCat.setLayoutManager(layoutManagerCat);
        mAdapterCat = new CatAdapter();
//        CatAdapter.OnItemClickListener listener = new CatAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                view.setBackgroundColor(0xAA151515);
//            }
//        };
//        mAdapterCat.setOnItemClickListener(listener);
        recyclerViewCat.setAdapter(mAdapterCat);

        recyclerViewNews = findViewById(R.id.table_recycler_view);
        layoutManagerNews = new LinearLayoutManager(this);
        recyclerViewNews.setLayoutManager(layoutManagerNews);
        mAdapterNews = new NewsAdapter(title, abs);
        recyclerViewNews.setAdapter(mAdapterNews);


    }
}