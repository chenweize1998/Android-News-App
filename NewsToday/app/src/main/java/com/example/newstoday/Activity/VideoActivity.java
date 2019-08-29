package com.example.newstoday.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newstoday.MyVideoView;
import com.example.newstoday.R;

import java.io.Serializable;

public class VideoActivity extends AppCompatActivity{

    private MyVideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);

        Intent intent = getIntent();
        mVideoView = (MyVideoView) intent.getSerializableExtra("videoView");

        final MediaController controller = new MediaController(getApplicationContext());

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //         mp.setLooping(true);
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        /*
                         * add media controller
                         */

                        mVideoView.setMediaController(controller);
                        /*
                         * and set its position on screen
                         */
                        controller.setAnchorView(mVideoView.getVideoView());
                    }
                });
//                mp.start();// 播放
            }
        });





    }

}
