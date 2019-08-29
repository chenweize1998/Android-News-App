package com.example.newstoday.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newstoday.R;

public class VideoActivity extends AppCompatActivity{

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);


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
                        controller.setAnchorView(mVideoView);
                    }
                });
//                mp.start();// 播放
            }
        });

        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    if (mVideoView.isPlaying()) {
//                        mVideoView.pause();
//                        cover.setVisibility(View.VISIBLE);
//                    } else {
//                        mVideoView.start();
//                        cover.setVisibility(View.INVISIBLE);
//                    }
        }
        });





    }

}
