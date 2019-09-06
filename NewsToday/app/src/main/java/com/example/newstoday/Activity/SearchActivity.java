package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.newstoday.Adapter.NewsAdapter;
import com.example.newstoday.News;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.example.newstoday.SearchProvider;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNews;
    private NewsAdapter mAdapterNews;
    private RecyclerView.LayoutManager layoutManagerNews;
    private ArrayList<News> news;
    private NewsManager newsManager;
    private final String category = "娱乐,军事,教育,文化,健康,财经,体育,汽车,科技,社会";
    private final int DISMISS_TIMEOUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_news);

        newsManager = NewsManager.getNewsManager(getApplicationContext());

        Intent intent = getIntent();
        String query = null;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SearchProvider.AUTHORITY, SearchProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
            news = newsManager.getNews(20, null, today, query, category, true, true);
        }
        final String querySaved = query;

        NewsAdapter.OnItemClickListener listenerNews = new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, final View v) {
                Intent intent = new Intent(getApplicationContext(), NewsPage.class);
                intent.putExtra("news", news.get(position));
                startActivity(intent);
            }
        };

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.search_result_title);

        RefreshLayout refreshLayout = findViewById(R.id.history_refresh_layout);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SearchActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
                                ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
                                        today, querySaved, category, true, false);
                                mAdapterNews.refreshNews(newsTmp);
                                refreshlayout.finishLoadMore();
                                mAdapterNews.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "新返回"+newsTmp.size()+"条新闻", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, DISMISS_TIMEOUT);
            }
        });

        recyclerViewNews = findViewById(R.id.history_recycler_view);
        layoutManagerNews = new LinearLayoutManager(this);
        recyclerViewNews.setLayoutManager(layoutManagerNews);
        mAdapterNews = new NewsAdapter(news, SearchActivity.this, this.getSupportFragmentManager());
        mAdapterNews.setOnItemClickListener(listenerNews);
        recyclerViewNews.setAdapter(mAdapterNews);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Table.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
