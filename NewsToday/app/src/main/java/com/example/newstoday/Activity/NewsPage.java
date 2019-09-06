package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newstoday.Adapter.CommentAdapter;
import com.example.newstoday.Adapter.LoopRecyclerAdapter;
import com.example.newstoday.ForwordingNewsManager;
import com.example.newstoday.News;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.example.newstoday.User;
import com.example.newstoday.UserMessageManager;
import com.sackcentury.shinebuttonlib.ShineButton;

import cn.jzvd.JZVideoPlayerStandard;
import me.relex.circleindicator.CircleIndicator2;
import me.relex.recyclerpager.SnapPageScrollListener;

import android.util.DisplayMetrics;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.HashMap;

public class NewsPage extends AppCompatActivity {
    private LoopRecyclerAdapter mAdapterImg;
    private NewsManager newsManager;
    private VideoView mVideoView;
    private CircleIndicator2 mIndicator;
    private ForwordingNewsManager forwordingNewsManager;
    private UserMessageManager userMessageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_page);

        Intent intent = getIntent();
        final News news = (News) ((Intent) intent).getSerializableExtra("news");

        newsManager = NewsManager.getNewsManager(getApplicationContext());
        forwordingNewsManager = ForwordingNewsManager.getForwordingNewsManager(getApplicationContext());
        userMessageManager = UserMessageManager.getUserMessageManager(getApplicationContext());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        TextView textView = findViewById(R.id.page_bottom_comment);
        textView.setWidth((int)(0.75*width));

        final ShineButton starButton = findViewById(R.id.page_bottom_star);
        if(newsManager.inCollectionNews(news)){
            starButton.setChecked(true);
        }
        else {
            starButton.setChecked(false);
        }
//
        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                News tmp = news;
                if(newsManager.inCollectionNews(tmp)){
                    newsManager.deleteOneCollection(tmp);
//                    starButton.setImageResource(R.drawable.not_star);
//                    newsManager.deleteOneCollection(tmp);
                } else{
//                    newsManager.addInCollection(tmp);
//                    starButton.setImageResource(R.drawable.star_selected);
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
//        pageTitle.setTextColor(0xFF000000);

        TextView pagePublisher = findViewById(R.id.page_publisher);
        pagePublisher.setText(news.getPublisher() + "\t" + news.getDate());

        TextView pageContent = findViewById(R.id.page_content);
        pageContent.setText(news.getContent());
        pageContent.setMovementMethod(new ScrollingMovementMethod());

//        final String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        if(news.getVideo() != null && !news.getVideo().equals("")) {
//        if(true){
//            RelativeLayout relativeLayout = findViewById(R.id.page_video_layout);
//            relativeLayout.setVisibility(View.VISIBLE);
//            mVideoView = (VideoView) findViewById(R.id.videoView);
////            mVideoView.setVideoURI(Uri.parse(news.getVideo()));
//            mVideoView.setVideoURI(Uri.parse(url));
//            mVideoView.seekTo(1);
//            mVideoView.setVisibility(View.VISIBLE);
////            final ImageView cover = findViewById(R.id.page_video_cover);
////            cover.setVisibility(View.VISIBLE);
//
//
//            mVideoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
////                    intent.putExtra("url", url);
////                    startActivity(intent);
//                }
//            });

            String url = news.getVideo();
            findViewById(R.id.page_video_layout).setVisibility(View.VISIBLE);
//            String url = "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4";
            JZVideoPlayerStandard jzVideoPlayerStandard = findViewById(R.id.videoPlayer);
            jzVideoPlayerStandard.setUp(url,
                    JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);
            Glide.with(this).load(url).into(jzVideoPlayerStandard.thumbImageView);
        }

        String[] tmp = new String[1];
        tmp[0] = "";
        if(news.getImage().length == 0)
            news.setImage(tmp);
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

        ImageButton shareButton = findViewById(R.id.page_bottom_share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(news);
                bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");
            }
        });

        final EditText editText = findViewById(R.id.page_bottom_comment);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(i == KeyEvent.KEYCODE_ENTER){
                        if(Table.header.getActiveProfile() == null){
                            Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        news.addComment(Table.header.getActiveProfile().getEmail().toString(),
                                editText.getText().toString());
                        newsManager.updateNews(news);
                        forwordingNewsManager.updataForwardingNews(news);
                        userMessageManager.updateNews(news);
                        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        editText.setText("");
                        Toast.makeText(getApplicationContext(), "评论成功", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                return false;
            }
        });

        final RecyclerView commentRecycler = findViewById(R.id.page_comment_recycler);
//        News newsDb = newsManager.getNewsByNewsID(news.getNewsID());
//        if(newsDb != null) {
            CommentAdapter commentAdapter = new CommentAdapter(news.getEmails(), news.getComments(), this);
            LinearLayoutManager commentLayoutManager = new LinearLayoutManager(getApplicationContext());
            commentRecycler.setLayoutManager(commentLayoutManager);
            commentRecycler.setAdapter(commentAdapter);
//        }
    }


    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), Table.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        super.onBackPressed();
        if (JZVideoPlayerStandard.backPress()) {
            finish();
            return;
        }
        finish();
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayerStandard.releaseAllVideos();
    }

}
