package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.newstoday.Adapter.LoopRecyclerAdapter;
import com.example.newstoday.Adapter.NewsPageAdapter;
import com.example.newstoday.News;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.squareup.picasso.Picasso;

import me.relex.circleindicator.CircleIndicator2;
import me.relex.recyclerpager.SnapPageScrollListener;

import android.util.DisplayMetrics;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

public class NewsPage extends AppCompatActivity {
    private LoopRecyclerAdapter mAdapterImg;
    private NewsManager newsManager;
    private VideoView videoView;
    private CircleIndicator2 mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_page);

        Intent intent = getIntent();
        final News news = (News) ((Intent) intent).getSerializableExtra("news");

        newsManager = NewsManager.getNewsManager(getApplicationContext());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        TextView textView = findViewById(R.id.page_bottom_comment);
        textView.setWidth((int)(0.75*width));

        final ImageView starButton = findViewById(R.id.page_bottom_star);
        if(newsManager.inCollectionNews(news)){
            starButton.setImageResource(R.drawable.star_selected);
        }
        else {
            starButton.setImageResource(R.drawable.not_star);
        }
//
        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                News tmp = news;
                if(newsManager.inCollectionNews(tmp)){
                    newsManager.deleteOneCollection(tmp);
                    starButton.setImageResource(R.drawable.not_star);
                    newsManager.deleteOneCollection(tmp);
                } else{
                    newsManager.addInCollection(tmp);
                    starButton.setImageResource(R.drawable.star_selected);
                    newsManager.addInCollection(tmp);
                }
            }
        });
//
//        ImageView imageView = findViewById(R.id.page_backdrop);
//        if(!news.getImage()[0].equals(""))
//            Picasso.get().load(news.getImage()[0]).resize(width, 500).centerCrop().into(imageView);
//        else
//            imageView.setImageResource(R.drawable.default_pic);
//
        TextView pageTitle = findViewById(R.id.page_title);
        pageTitle.setText(news.getTitle());
        pageTitle.setTextColor(0xFF000000);

        TextView pagePublisher = findViewById(R.id.page_publisher);
        pagePublisher.setText(news.getPublisher() + "\t" + news.getDate());
        pagePublisher.setTextColor(0xAA878787);

//        recyclerView = findViewById(R.id.page_recycler_view);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        mAdapter = new NewsPageAdapter(news, height, width);
//        recyclerView.setAdapter(mAdapter);

        TextView pageContent = findViewById(R.id.page_content);
        pageContent.setText(news.getContent());
        pageContent.setMovementMethod(new ScrollingMovementMethod());

        String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        videoView = findViewById(R.id.videoView);
//        final MediaController controller = new MediaController(this);
//        controller.setMediaPlayer(videoView);
//        videoView.setMediaController(controller);
        videoView.setVideoURI(Uri.parse(url));
//        controller.setAnchorView(videoView);
//        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //         mp.setLooping(true);
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                     @Override
                     public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                         /*
                          * add media controller
                          */
                         MediaController controller = new MediaController(NewsPage.this);
                         videoView.setMediaController(controller);
                         /*
                          * and set its position on screen
                          */
                         controller.setAnchorView(videoView);
                     }
                 });
//                mp.start();// 播放
                Toast.makeText(NewsPage.this, "开始播放！", Toast.LENGTH_LONG).show();
            }
        });



        mAdapterImg = new LoopRecyclerAdapter(news.getImage().length, NewsPage.this, news);

        mIndicator = findViewById(R.id.page_indicator);
        final RecyclerView recyclerView = findViewById(R.id.page_image_recycler);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapterImg);
        mIndicator.createIndicators(mAdapterImg.getRealItemCount(), 0);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new SnapPageScrollListener() {
            @Override public void onPageSelected(int position) {
                mIndicator.animatePageSelected(mAdapterImg.getRealPosition(position));
            }

            @Override public void onPageScrolled(int position, float positionOffset,
                                                 int positionOffsetPixels) {
                if (positionOffsetPixels == 0) {
                    recyclerView.scrollToPosition(mAdapterImg.getLoopPosition(position));
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Table.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
