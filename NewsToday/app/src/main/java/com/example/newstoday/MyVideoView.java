package com.example.newstoday;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.MediaController;
import android.util.AttributeSet;
import android.widget.VideoView;

import java.io.Serializable;

public class MyVideoView implements Serializable {

    private VideoView videoView;

    public MyVideoView(VideoView videoView) {
        this.videoView = videoView;
    }

    public void pause() {
        videoView.pause();
    }

    public void start() {
        videoView.start();
    }

    public void setVideoURI(Uri uri){
        videoView.setVideoURI(uri);
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener){
        videoView.setOnPreparedListener(onPreparedListener);
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        videoView.setOnClickListener(onClickListener);
    }

    public boolean isPlaying(){
        return videoView.isPlaying();
    }

    public void setMediaController(MediaController controller){
        videoView.setMediaController(controller);
    }

    public VideoView getVideoView(){
        return videoView;
    }

    public void setVisibility(int visiibility){
        videoView.setVisibility(visiibility);
    }

    public void seekTo(int msec){
        videoView.seekTo(msec);
    }

}