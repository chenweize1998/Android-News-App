package com.example.newstoday;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.MediaController;
import android.util.AttributeSet;
import android.widget.VideoView;

public class MyVideoView {

    private PlayPauseListener mListener;
    private VideoView videoView;

    public MyVideoView(VideoView videoView) {
        this.videoView = videoView;
    }

    public void setPlayPauseListener(PlayPauseListener listener) {
        mListener = listener;
    }

    public void pause() {
        videoView.pause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    public void start() {
        videoView.start();
        if (mListener != null) {
            mListener.onPlay();
        }
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

    public static interface PlayPauseListener {
        void onPlay();
        void onPause();
    }

}
