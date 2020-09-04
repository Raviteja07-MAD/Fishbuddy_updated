package com.fishbuddy.videoplayers;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.fishbuddy.R;


/**
 * Created by Kiran on 4/10/2017.
 */

public class VideoViewActivity extends Activity {

    String AudioURL = "";
    VideoView mVideoView;
    ImageButton videoplay_btn;
    ProgressBar progressBar;
    int count=0;
    ImageView backbutton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplayerview);
        initView();
    }

    /** This method initialise all the views in project*/
    private void initView() {
         mVideoView  = (VideoView)findViewById(R.id.video_view);
        videoplay_btn=(ImageButton) findViewById(R.id.videoplay_btn);
        progressBar = (ProgressBar) findViewById(R.id.progrss);
        backbutton=(ImageView) findViewById(R.id.backbutton);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            AudioURL = bd.getString("media_file");
        }
        Log.i("videourl","VideoURL:::"+AudioURL);



       //Uri video = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
        Uri video = Uri.parse(AudioURL);
        mVideoView.setVideoURI(video);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mVideoView.start();
            }
        });




        /*Uri video = Uri.parse(AudioURL);
        mVideoView.setVideoURI(video);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                mp.setLooping(true);
                mVideoView.start();
                count=1;
            }
        });
        videoplay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count==1){
                    videoplay_btn.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.playbutton));
                    mVideoView.start();
                }else{
                    count=0;
                    videoplay_btn.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.pausebutton));

                    mVideoView.pause();
                }
            }
        });*/

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }




}


