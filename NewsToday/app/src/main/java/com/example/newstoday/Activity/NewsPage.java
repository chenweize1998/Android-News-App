package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.newstoday.Adapter.NewsPageAdapter;
import com.example.newstoday.News;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.squareup.picasso.Picasso;

import android.util.DisplayMetrics;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

public class NewsPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewsPageAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private NewsManager newsManager;
    private VideoView videoView;

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
        int height = displayMetrics.heightPixels;
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
        ImageView imageView = findViewById(R.id.page_backdrop);
        if(!news.getImage()[0].equals(""))
            Picasso.get().load(news.getImage()[0]).resize(width, 500).centerCrop().into(imageView);
        else
            imageView.setImageResource(R.drawable.default_pic);
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


        System.out.println("视频开始展示");
        String url = "https://developers.google.com/training/images/tacoma_narrows.mp4";
        videoView = (VideoView) findViewById(R.id.videoView);
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);
        videoView.setVideoURI(Uri.parse(url));
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //         mp.setLooping(true);
                mp.start();// 播放
                Toast.makeText(NewsPage.this, "开始播放！", Toast.LENGTH_LONG).show();
                System.out.println("准备播放视频");
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
