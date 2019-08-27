package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.newstoday.Adapter.NewsAdapter;
import com.example.newstoday.News;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNews;
    private NewsAdapter mAdapterNews;
    private RecyclerView.LayoutManager layoutManagerNews;
    private ArrayList<News> news;
    private NewsManager newsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_news);

        newsManager = NewsManager.getNewsManager(getApplicationContext());

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            news = newsManager.getNews(20, null, null, query, null, true);
        }

        NewsAdapter.OnItemClickListener listenerNews = new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, final View v) {
                Intent intent = new Intent(getApplicationContext(), NewsPage.class);
                intent.putExtra("news", news.get(position));
                startActivity(intent);
            }
        };

        recyclerViewNews = findViewById(R.id.table_recycler_view);
        layoutManagerNews = new LinearLayoutManager(this);
        recyclerViewNews.setLayoutManager(layoutManagerNews);
        mAdapterNews = new NewsAdapter(news);
        mAdapterNews.setOnItemClickListener(listenerNews);
        recyclerViewNews.setAdapter(mAdapterNews);
    }
}
