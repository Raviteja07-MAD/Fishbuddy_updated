package com.fishbuddy.videoplayers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fishbuddy.R;
import com.fishbuddy.storedobjects.StoredUrls;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Youtube_newActivity extends YouTubeBaseActivity 
 implements YouTubePlayer.OnInitializedListener{
  
 public static final String DEVELOPER_KEY = "AIzaSyDOzGZXH8ySHrSjgpqRulanEjXt-Rb-YpY";
 private static final int RECOVERY_DIALOG_REQUEST = 1;
 private static final String VIDEO_ID = "fhWaJi1Hsfo";
  
 YouTubePlayerFragment myYouTubePlayerFragment;
 
 @SuppressLint("NewApi")
@Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.youtubeplayerfragment);

  myYouTubePlayerFragment = (YouTubePlayerFragment)getFragmentManager()
    .findFragmentById(R.id.youtubeplayerfragment__);
  myYouTubePlayerFragment.initialize(DEVELOPER_KEY, this);
 }
  
 @Override
 public void onInitializationFailure(Provider provider,
   YouTubeInitializationResult errorReason) {
  if (errorReason.isUserRecoverableError()) {
   errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show(); 
  } else {
   String errorMessage = String.format(
     "There was an error initializing the YouTubePlayer (%1$s)", 
     errorReason.toString());
   Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
  } 
 }
 
 @Override
 public void onInitializationSuccess(Provider provider, YouTubePlayer player,
   boolean wasRestored) {
  if (!wasRestored) {
	  
		  //Log.i("viii", "viii:--"+StoredObject.video_id_play);
		 player.loadVideo(getYoutubeVideoId(StoredUrls.youtubelink));
		 // player.loadVideo(getYoutubeVideoId("UTwAnSHqHgM")); 
		 // player.loadVideo("UTwAnSHqHgM"); 
	  
            
  }
 }
  
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   
  if (requestCode == RECOVERY_DIALOG_REQUEST) {
   // Retry initialization if user performed a recovery action
   getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this); 
  } 
 }
 
 protected Provider getYouTubePlayerProvider() {
  return (YouTubePlayerView)findViewById(R.id.youtubeplayerfragment__); 
 }
 
 
 public static String getYoutubeVideoId(String youtubeUrl)
 {
 String video_id = "";
  if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http"))
 {

String expression = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
 CharSequence input = youtubeUrl;
 Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
 Matcher matcher = pattern.matcher(input);
 if (matcher.matches())
 {
String groupIndex1 = matcher.group(7);
 if(groupIndex1!=null && groupIndex1.length()==11)
 video_id = groupIndex1;
 }
 }
  //Log.i("videoid:-", "videoid::---"+video_id);
  
 return video_id;
 }
 
}