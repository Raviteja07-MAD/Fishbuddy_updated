package com.fishbuddy.customadapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fishbuddy.Activities.Sign_in_Sign_up;
import com.fishbuddy.R;
import com.fishbuddy.circularimageview.CircularImageView;
import com.fishbuddy.customcamara.AddpostBreedActivity;
import com.fishbuddy.customcamara.CameraActivity;
import com.fishbuddy.customcamara.LogcatchClass;
import com.fishbuddy.customfonts.AppController;
import com.fishbuddy.customfonts.CustomBoldTextView;
import com.fishbuddy.customfonts.CustomButton;
import com.fishbuddy.customfonts.CustomEditText;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.customfonts.FeedImageView;
import com.fishbuddy.customfonts.ReadMoreTextView;
import com.fishbuddy.database.Database;
import com.fishbuddy.dumpdata.DumpData;
import com.fishbuddy.fragments.AddPostFromBreedCheck;
import com.fishbuddy.fragments.Addpost;
import com.fishbuddy.fragments.Comments;
import com.fishbuddy.fragments.CommentsNew;
import com.fishbuddy.fragments.FishingSpotsDetailsActivity;
import com.fishbuddy.fragments.Fishingspots_details;
import com.fishbuddy.fragments.Followers;
import com.fishbuddy.fragments.Followers_profile;
import com.fishbuddy.fragments.Following;
import com.fishbuddy.fragments.Landing_page;
import com.fishbuddy.fragments.Landingdetails;
import com.fishbuddy.fragments.Notifications;
import com.fishbuddy.fragments.Profile;
import com.fishbuddy.fragments.Searchpage;
import com.fishbuddy.fragments.Viewpager_gallery;
import com.fishbuddy.servicesparsing.CustomProgressbar;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;
import com.fishbuddy.videoplayers.VideoViewActivity;
import com.fishbuddy.videoplayers.Youtube_newActivity;


import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hb.xvideoplayer.MxVideoPlayer;

import static com.fishbuddy.customadapter.CustomRecyclerview.hashMapRecycleviewadapter;
import static com.fishbuddy.fragments.Comments.comments_lay;
import static com.fishbuddy.fragments.Comments.customRecyclerview;
import static com.fishbuddy.fragments.Comments.getposdetailslist;
import static com.fishbuddy.fragments.Comments.postid;
import static io.fabric.sdk.android.Fabric.TAG;

class HashmapViewHolder extends RecyclerView.ViewHolder {
    private Activity activity;
    HashMapRecycleviewadapter adapter;

    CustomRegularTextView level_txt, count_txt;

    CustomRegularTextView document_txt, image_txt, status_txt;

    Database database;
 //   RecyclerView liked_recycle;
    //landingpage
    private LinearLayout landing_listitems_lay, share__lay, comment__lay, like__lay,liked_by_layout,commented_by_layout,images_lay;
    private ImageView like_img;
    CustomRegularTextView more_imagestxt;
    FrameLayout framelay_img,shared_framelay_img,video_layout;
    //VideoView simpleVideoView;
    CustomRegularTextView fishingspots_text;

    View mBottomLayout;
    View mVideoLayout;
   // UniversalVideoView simpleVideoView;
   // UniversalMediaController mMediaController;
    MxVideoPlayer simpleVideoView;


    //Feed
    ImageView post_img,post_img2;
    CircularImageView landing_circular_img;
    private CustomRegularTextView like_txt, comments_count_txt, like_txt_count, share_count_txt,profile_updatedtime_txt,share_txt;

    ReadMoreTextView dscrption_txt;

    CustomBoldTextView profile_txt;
    ImageView landing_dots_image;
    LinearLayout posted_layout;
    View shared_view;

    // shared post
    LinearLayout shared_layout;
    ImageView shared_post_img,shared_post_img2;
    CircularImageView shared_landing_circular_img,postedby_landing_circular_img;
    CustomRegularTextView shared_by_profile_updatedtime_txt,shared_url_txt,
            shared_by_dscrption_txt,shared_more_imagestxt,shared_profile_updatedtime_txt,shared_dscrption_txt;
    CustomBoldTextView shared_profile_txt,shared_by_profile_txt;
    ImageView shared_landing_dots_image;

    //fishingspots
    private LinearLayout fishingspots_lay, directions_lay;
    CustomBoldTextView lakename_txt;
    CustomRegularTextView lakelocation_txt,lakedetails_txt, maxdepth_count_txt,area_count_txt,directions_txt;
    CircularImageView lake_image;


    View  view_color;
    CustomRegularTextView percentagevalue;


    // fishbreeds
    private LinearLayout fishbreed_lay;
    ImageView image_view_icon;
    CustomRegularTextView track_txt,fishbreeds_name;

    //fishing_tricks
    private CustomRegularTextView int_count_txt;
    private ImageView fishingtricks_img;
    RelativeLayout video_play_layout;
    CustomBoldTextView fishing_title_txt;
   // CustomRegularTextView fishing_tricks__txt;

    ReadMoreTextView fishing_tricks__txt;


    // following
    CircularImageView profile_circular_img;
    CustomButton follow_btn;

    //profile_following
    CircularImageView following_circular_img;
    CustomRegularTextView follwing_profile_name_txt;
    //profile_followers
    LinearLayout followers_profile_lay;
    Bitmap bmThumbnail;
    private static String count = "";
    CircularImageView followers_circular_img;
    CustomRegularTextView followers_profile_name_txt, fishing_tricks_txt, profile_name_txt;

    // notifications
    LinearLayout notifications_lay;
    CustomRegularTextView ntfc_profile_name_txt;
    String comments_count = "";
    CheckBox delte_checkboxselection;
    ArrayList<HashMap<String, String>> datalist_test = new ArrayList<>();

    // comments
    CircularImageView commnt_profile_circular_img;
    CustomBoldTextView comment_profile_txt;
    CustomRegularTextView comment_txt,url_txt,title;
    LinearLayout comments_listitems_lay;

    //Searchpage
    CustomRegularTextView profile_email_txt;
    LinearLayout profile_listitems_lay;
   /* public static ArrayList<HashMap<String, String>> getlikeslist = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> getcommentslist = new ArrayList<>();

*/
    // Notifications
    CustomRegularTextView ntfc_description_txt,ntfc_date_time;
    LinearLayout notific_listitems_lay,notificationdetails_lay;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    ViewPager viewpager;
    LinearLayout viewPagerDots;

    //profile_catches
    CircularImageView catch_circular_img;



    HashmapViewHolder(View convertView, String type, final Activity activity) {

        super(convertView);
        this.activity = activity;
        database = new Database(activity);




        if (type.equalsIgnoreCase( "profile_catches" )) {
            catch_circular_img = convertView.findViewById( R.id.catch_circular_img );
        }

        if (type.equalsIgnoreCase( "Searchpage" )) {
            profile_circular_img = convertView.findViewById( R.id.profile_circular_img );
            profile_name_txt = convertView.findViewById( R.id.profile_name_txt );
            profile_email_txt = convertView.findViewById( R.id.profile_email_txt );
            profile_listitems_lay = convertView.findViewById( R.id.profile_listitems_lay );
            follow_btn  = convertView.findViewById( R.id.follow_btn );
        }
        if (type.equalsIgnoreCase( "likes" )) {
            profile_circular_img = convertView.findViewById( R.id.profile_circular_img );
            profile_name_txt = convertView.findViewById( R.id.profile_name_txt );
            profile_email_txt = convertView.findViewById( R.id.profile_email_txt );
            profile_listitems_lay = convertView.findViewById( R.id.profile_listitems_lay );
            follow_btn  = convertView.findViewById( R.id.follow_btn );
        }

        if (type.equalsIgnoreCase("landingpage")) {

            landing_listitems_lay = convertView.findViewById(R.id.landing_listitems_lay);
            images_lay = convertView.findViewById(R.id.images_lay);
            share__lay = convertView.findViewById(R.id.share__lay);
            comment__lay = convertView.findViewById(R.id.comment__lay);
            like__lay = convertView.findViewById(R.id.like__lay);
            liked_by_layout = convertView.findViewById(R.id.liked_by_layout);
            commented_by_layout = convertView.findViewById(R.id.commented_by_layout);
            like_img = convertView.findViewById(R.id.like_img);
            like_txt = convertView.findViewById(R.id.like_txt);
            comments_count_txt = convertView.findViewById(R.id.comments_count_txt);
            like_txt_count = convertView.findViewById(R.id.like_txt_count);
            share_count_txt = convertView.findViewById(R.id.share_count_txt);
            shared_view = convertView.findViewById( R.id.shared_view );

            landing_circular_img = convertView.findViewById(R.id.landing_circular_img);
            dscrption_txt = convertView.findViewById(R.id.dscrption_txt);
            post_img = convertView.findViewById(R.id.post_img);
            profile_txt = convertView.findViewById(R.id.profile_txt);
            url_txt = convertView.findViewById(R.id.url_txt);
           // title = convertView.findViewById(R.id.title);
            profile_updatedtime_txt =  convertView.findViewById(R.id.profile_updatedtime_txt);
            share_txt  =  convertView.findViewById(R.id.share_txt);
            landing_dots_image = convertView.findViewById(R.id.landing_dots_image);
            more_imagestxt = convertView.findViewById(R.id.more_imagestxt);
            framelay_img = convertView.findViewById( R.id.framelay_img );
            post_img2 = convertView.findViewById(R.id.post_img2);
            posted_layout = convertView.findViewById( R.id.posted_layout );


            shared_landing_circular_img = convertView.findViewById(R.id.shared_landing_circular_img);
            postedby_landing_circular_img = convertView.findViewById(R.id.postedby_landing_circular_img);
            shared_by_dscrption_txt = convertView.findViewById(R.id.shared_by_dscrption_txt);
            shared_dscrption_txt = convertView.findViewById(R.id.shared_dscrption_txt);
            shared_post_img = convertView.findViewById(R.id.shared_post_img);
            shared_by_profile_txt = convertView.findViewById(R.id.shared_by_profile_txt);
            shared_profile_txt = convertView.findViewById(R.id.shared_profile_txt);
           // shared_url_txt = convertView.findViewById(R.id.shared_url_txt);
            shared_profile_updatedtime_txt =  convertView.findViewById(R.id.shared_profile_updatedtime_txt);
            shared_by_profile_updatedtime_txt =  convertView.findViewById(R.id.shared_by_profile_updatedtime_txt);
            shared_landing_dots_image = convertView.findViewById(R.id.shared_landing_dots_image);
            shared_more_imagestxt = convertView.findViewById(R.id.shared_more_imagestxt);
            shared_framelay_img = convertView.findViewById( R.id.shared_framelay_img );
            shared_post_img2 = convertView.findViewById(R.id.shared_post_img2);
            shared_layout = convertView.findViewById( R.id.shared_layout );
           // video_layout = convertView.findViewById( R.id.video_layout );

            simpleVideoView = convertView.findViewById( R.id.simpleVideoView );


            fishingspots_text = convertView.findViewById( R.id.fishingspots_text );
            fishingspots_lay = convertView.findViewById(R.id.fishingspots_lay);

          //  mMediaController = (UniversalMediaController) convertView.findViewById(R.id.media_controller);

        }
        if (type.equalsIgnoreCase("multipleimages")) {

            image_view_icon = convertView.findViewById(R.id.image_view_icon);

        }
        if (type.equalsIgnoreCase("fishingspots_multipleimages")) {

            image_view_icon = convertView.findViewById(R.id.image_view_icon);

        }

        if (type.equalsIgnoreCase("fishingspots_multipleimages_")) {

            image_view_icon = convertView.findViewById(R.id.image_view_icon);

        }


        if (type.equalsIgnoreCase("fishingspots_fishbreeds")) {

            fishbreeds_name = convertView.findViewById(R.id.fishbreeds_name);

        }



        if (type.equalsIgnoreCase("fishbreeds_multipleimages")) {

            image_view_icon = convertView.findViewById(R.id.image_view_icon);

        }


        if (type.equalsIgnoreCase("fishingspots")) {

            fishingspots_lay = convertView.findViewById(R.id.fishingspots_lay);
            directions_lay = convertView.findViewById(R.id.directions_lay);
            lakename_txt = convertView.findViewById(R.id.lakename_txt);
            lakelocation_txt = convertView.findViewById(R.id.lakelocation_txt);
            lakedetails_txt = convertView.findViewById(R.id.lakedetails_txt);
            lake_image = convertView.findViewById(R.id.lake_image);
            maxdepth_count_txt = convertView.findViewById(R.id.maxdepth_count_txt);
            area_count_txt = convertView.findViewById(R.id.area_count_txt);
            directions_txt = convertView.findViewById(R.id.directions_txt);
        }


        if (type.equalsIgnoreCase("fishingspots_")) {

            fishingspots_lay = convertView.findViewById(R.id.fishingspots_lay);
            directions_lay = convertView.findViewById(R.id.directions_lay);
            lakename_txt = convertView.findViewById(R.id.lakename_txt);
            lakelocation_txt = convertView.findViewById(R.id.lakelocation_txt);
            lakedetails_txt = convertView.findViewById(R.id.lakedetails_txt);
            lake_image = convertView.findViewById(R.id.lake_image);
            maxdepth_count_txt = convertView.findViewById(R.id.maxdepth_count_txt);
            area_count_txt = convertView.findViewById(R.id.area_count_txt);
            directions_txt = convertView.findViewById(R.id.directions_txt);
        }


        if (type.equalsIgnoreCase("identified_breed")) {

            fishingspots_lay = convertView.findViewById(R.id.fishingspots_lay);
            directions_lay = convertView.findViewById(R.id.directions_lay);
            lakename_txt = convertView.findViewById(R.id.lakename_txt);
            lakelocation_txt = convertView.findViewById(R.id.lakelocation_txt);
            lakedetails_txt = convertView.findViewById(R.id.lakedetails_txt);
            lake_image = convertView.findViewById(R.id.lake_image);
            maxdepth_count_txt = convertView.findViewById(R.id.maxdepth_count_txt);
            area_count_txt = convertView.findViewById(R.id.area_count_txt);

            view_color = convertView.findViewById(R.id.view_color);
            percentagevalue = convertView.findViewById(R.id.percentagevalue);


        }

        if (type.equalsIgnoreCase("identified_breed_common")) {

            fishingspots_lay = convertView.findViewById(R.id.fishingspots_lay);
            directions_lay = convertView.findViewById(R.id.directions_lay);
            lakename_txt = convertView.findViewById(R.id.lakename_txt);
            lakelocation_txt = convertView.findViewById(R.id.lakelocation_txt);
            lakedetails_txt = convertView.findViewById(R.id.lakedetails_txt);
            lake_image = convertView.findViewById(R.id.lake_image);
            maxdepth_count_txt = convertView.findViewById(R.id.maxdepth_count_txt);
            area_count_txt = convertView.findViewById(R.id.area_count_txt);

            view_color = convertView.findViewById(R.id.view_color);
            percentagevalue = convertView.findViewById(R.id.percentagevalue);


        }



        if (type.equalsIgnoreCase("comments")) {

            comment_profile_txt = convertView.findViewById(R.id.comment_profile_txt);
            commnt_profile_circular_img = convertView.findViewById(R.id.commnt_profile_circular_img);
            comment_txt = convertView.findViewById(R.id.comment_txt);
            comments_listitems_lay = convertView.findViewById(R.id.comments_listitems_lay);
        }
        if (type.equalsIgnoreCase("comments_popup")) {

            comment_profile_txt = convertView.findViewById(R.id.comment_profile_txt);
            commnt_profile_circular_img = convertView.findViewById(R.id.commnt_profile_circular_img);
            comment_txt = convertView.findViewById(R.id.comment_txt);
            comments_listitems_lay = convertView.findViewById(R.id.comments_listitems_lay);
        }

        if (type.equalsIgnoreCase("fishbreeds")) {

            fishbreed_lay = convertView.findViewById(R.id.fishbreed_lay);
            image_view_icon = convertView.findViewById(R.id.image_view_icon);
            track_txt = convertView.findViewById(R.id.track_txt);
        }
        if (type.equalsIgnoreCase("fishing_tricks")) {

            int_count_txt = convertView.findViewById(R.id.int_count_txt);
            fishing_tricks_txt = convertView.findViewById(R.id.fishing_tricks_txt);
            fishingtricks_img = convertView.findViewById(R.id.fishingtricks_img);

            video_play_layout = convertView.findViewById(R.id.video_play_layout);
            fishing_title_txt = convertView.findViewById(R.id.fishing_title_txt);
            fishing_tricks__txt = convertView.findViewById(R.id.fishing_tricks__txt);


        }
        if (type.equalsIgnoreCase("following")) {

            profile_circular_img = convertView.findViewById(R.id.profile_circular_img);
            profile_name_txt = convertView.findViewById(R.id.profile_name_txt);
            follow_btn = convertView.findViewById(R.id.follow_btn);
        }
        if (type.equalsIgnoreCase("Followers")) {

            profile_circular_img = convertView.findViewById(R.id.profile_circular_img);
            profile_name_txt = convertView.findViewById(R.id.profile_name_txt);
            follow_btn = convertView.findViewById(R.id.follow_btn);
        }

        if (type.equalsIgnoreCase("profile_followers")) {
            followers_profile_lay = convertView.findViewById(R.id.followers_profile_lay);
            followers_circular_img = convertView.findViewById(R.id.followers_circular_img);
            followers_profile_name_txt = convertView.findViewById(R.id.followers_profile_name_txt);
        }
        if (type.equalsIgnoreCase("profile_following")) {
            followers_profile_lay = convertView.findViewById(R.id.followers_profile_lay);
            following_circular_img = convertView.findViewById(R.id.following_circular_img);
            follwing_profile_name_txt = convertView.findViewById(R.id.follwing_profile_name_txt);
        }
        if (type.equalsIgnoreCase("notifications")) {
            ntfc_profile_name_txt = convertView.findViewById(R.id.ntfc_profile_name_txt);
            notifications_lay = convertView.findViewById(R.id.notifications_lay);
            ntfc_description_txt = convertView.findViewById( R.id.ntfc_description_txt );
            ntfc_date_time = convertView.findViewById( R.id.ntfc_date_time );
            landing_circular_img = convertView.findViewById(R.id.landing_circular_img);
            notific_listitems_lay = convertView.findViewById( R.id.notific_listitems_lay );
            delte_checkboxselection = convertView.findViewById( R.id.delte_checkboxselection );
            notificationdetails_lay= convertView.findViewById( R.id.notificationdetails_lay );
        }
    }

    void assign_data(final ArrayList<HashMap<String, String>> datalist, final int position, String formtype) {


/*
        if (formtype.equalsIgnoreCase("landingpage")) {
            landing_listitems_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentcalling1(new Comments(), datalist, position);
                }
            });
            share__lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   */
/* Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT,
                            "www.google.com");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    activity.startActivity(Intent.createChooser(sharingIntent, "Share using"));*//*


                   // Sharepostpopup(activity,"Do you want to share this post?",datalist.get(position).get("post_id"));
                    Sharepostpopup(activity,datalist,position,datalist.get(position).get("post_id"));

                }
            });
            comment__lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentcalling1(new Comments(), datalist, position);
                }
            });
            comments_count = datalist.get(position).get("is_liked");

            if (comments_count.equalsIgnoreCase("No")) {
                like_img.setImageResource(R.drawable.like_button);
                like_txt.setTextColor(activity.getResources().getColor(R.color.lite_color_text));
            } else {
                like_img.setImageResource(R.drawable.liked);
                like_txt.setTextColor(activity.getResources().getColor(R.color.orange));
            }

            like__lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (comments_count.equalsIgnoreCase("No")) {
                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            datalist_test = datalist;
                             new LikepostTask().execute(  StoredObjects.UserId,datalist.get( position ).get( "post_id" ) );
                                updateqtydata(datalist,datalist.get(position),position,"Yes","landingpage");

                        } else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet), activity);
                        }
                    } else {
                        //StoredObjects.ToastMethod("Already liked", activity);

                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            datalist_test = datalist;
                            new UnLikepostTask().execute(  StoredObjects.UserId,datalist.get( position ).get( "post_id" ) );
                            updateqtydata(datalist,datalist.get(position),position,"No","landingpage");

                        } else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet), activity);
                        }

                    }




                }

            });
            profile_txt.setText(datalist.get(position).get("posted_by"));
            profile_updatedtime_txt.setText(datalist.get(position).get("created_at"));

            //  dscrption_txt.setText( datalist.get( position ).get( "description" ) );
            String text = datalist.get(position).get("description");

            if (text.length() > 120) {
               // text = text.substring(0, 120) + "...";
               // dscrption_txt.setText(Html.fromHtml(text + "<font color='hash'> <u>View More</u></font>"));
                //  dscrption_txt.setText( datalist.get( position ).get( "description" ) );
                dscrption_txt.setText(text);
                makeTextViewResizable(dscrption_txt, 2, "More", true);

            } else {
                dscrption_txt.setText(text);
            }



            if(datalist.get(position).get("description").equalsIgnoreCase("-")){
                dscrption_txt.setVisibility(View.GONE);
            }else{
                dscrption_txt.setVisibility(View.VISIBLE);
            }

            if(datalist.get(position).get("filename").equalsIgnoreCase("-")){
                post_img.setVisibility(View.GONE);
            }else{
                post_img.setVisibility(View.VISIBLE);
            }


                    if(datalist.get(position).get("title").equalsIgnoreCase("-")){
                        url_txt.setVisibility(View.GONE);
                    }else{
                        url_txt.setVisibility(View.VISIBLE);
                    }

            url_txt.setText(datalist.get(position).get("title"));

            comments_count_txt.setText(datalist.get(position).get("comments_count"));
            like_txt_count.setText(datalist.get(position).get("likes"));
            share_txt.setText(datalist.get(position).get("share_count")+" Shares");

            try {
                Glide.with( activity )
                        .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "filename" ) ) ) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.splash_logo )
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .crossFade()
                       //.override(600,600)
                        .into( post_img );
              //  adapter.notifyItemInserted(position);
            } catch (Exception e) {
            }


            landing_dots_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditDeleteConfirmation(activity,datalist,position);
                }
            });


           */
/* if (datalist.get(position).get("filename") != null) {
                post_img.setImageUrl(StoredUrls.Uploadedimages + datalist.get(position).get("filename"), imageLoader);
                post_img.setVisibility(View.VISIBLE);
                post_img
                        .setResponseObserver(new FeedImageView.ResponseObserver() {
                            @Override
                            public void onError() {
                            }

                            @Override
                            public void onSuccess() {
                            }
                        });
            }*//*



            try {
                //RequestOptions options = new RequestOptions();
                //options.optionalFitCenter();
                Glide.with(activity)
                        .load(Uri.parse(StoredUrls.Uploadedimages + datalist.get(position).get("profile_picture"))) // add your image url
                  //      .apply(options)
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder(R.drawable.man)
                        .into(landing_circular_img);
            } catch (Exception e) {
            }
        }
*/
/*
        if (formtype.equalsIgnoreCase( "landingpage" )) {

            final String post_type = datalist.get( position ).get( "post_type" );
            if (post_type.equalsIgnoreCase( "Direct Post" )){
                posted_layout.setVisibility( View.VISIBLE );
                shared_layout.setVisibility( View.GONE );

            }
            else {
                posted_layout.setVisibility( View.GONE );
                shared_layout.setVisibility( View.VISIBLE );
            }

            landing_listitems_lay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentcalling1( new Comments(), datalist, position );
                }
            } );
            share__lay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (post_type.equalsIgnoreCase( "Direct Post" )){
                        Sharepostpopup( activity, datalist, position, datalist.get( position ).get("post_id"),"" );
                    }
                    else {
                        Sharepostpopup( activity, datalist, position, datalist.get( position ).get("original_post_id"),"" );
                    }

                }
            } );
            comment__lay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentcalling1( new Comments(), datalist, position );
                }
            } );
            comments_count = datalist.get( position ).get( "is_liked" );


            if (comments_count.equalsIgnoreCase( "No" )) {
                like_img.setImageResource( R.drawable.like_button );
                like_txt.setTextColor( activity.getResources().getColor( R.color.lite_color_text ) );

            } else {
                like_img.setImageResource( R.drawable.liked );
                like_txt.setTextColor( activity.getResources().getColor( R.color.orange ) );

            }

            String likescount = datalist.get( position ).get( "likes_count" );
            if (likescount.equals( "0" )){
                liked_by_layout.setClickable( false );
            }
            else
            {
                liked_by_layout.setClickable( true );

                liked_by_layout.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        liked_details_popup( activity,datalist,position );
                    }
                } );

            }

            like__lay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (comments_count.equalsIgnoreCase( "No" )) {
                        if (InterNetChecker.isNetworkAvailable( activity )) {
                            datalist_test = datalist;
                            new LikepostTask().execute( StoredObjects.UserId, datalist.get( position ).get( "post_id" ) );
                             updateqtydata( datalist, datalist.get( position ), position, "Yes", "landingpage" );


                        } else {
                            StoredObjects.ToastMethod( activity.getResources().getString( R.string.checkinternet ), activity );
                        }
                    } else {
                        //StoredObjects.ToastMethod("Already liked", activity);

                        if (InterNetChecker.isNetworkAvailable( activity )) {
                            datalist_test = datalist;
                            new UnLikepostTask().execute( StoredObjects.UserId, datalist.get( position ).get( "post_id" ) );
                            updateqtydata( datalist, datalist.get( position ), position, "No", "landingpage" );

                        } else {
                            StoredObjects.ToastMethod( activity.getResources().getString( R.string.checkinternet ), activity );
                        }
                    }
                }
            } );

            profile_txt.setText( datalist.get( position ).get( "posted_by" ) );
            shared_profile_txt.setText( datalist.get( position ).get( "posted_by" ) );
            profile_updatedtime_txt.setText( datalist.get( position ).get( "created_at" ) );
            shared_profile_updatedtime_txt.setText( datalist.get( position ).get( "created_at" ) );

              dscrption_txt.setText( datalist.get( position ).get( "description" ) );
             shared_dscrption_txt.setText( datalist.get( position ).get( "description" ) );

            String text = datalist.get( position ).get( "description" );

            if (text.length() > 120) {
                 //text = text.substring(0, 120) + "...";
                // dscrption_txt.setText(Html.fromHtml(text + "<font color='hash'> <u>View More</u></font>"));
                 // dscrption_txt.setText( datalist.get( position ).get( "description" ) );
                dscrption_txt.setText( text );
                shared_dscrption_txt.setText( text );
                //makeTextViewResizable( dscrption_txt, 2, "More", true );

            } else {
                dscrption_txt.setText( text );
                shared_dscrption_txt.setText( text );
            }

            if (datalist.get( position ).get( "description" ).equalsIgnoreCase( "-" )) {
                dscrption_txt.setVisibility( View.GONE );
                shared_dscrption_txt.setVisibility( View.GONE );
            } else {
                dscrption_txt.setVisibility( View.VISIBLE );
                shared_dscrption_txt.setVisibility( View.VISIBLE );
            }

            if (datalist.get( position ).get( "title" ).equalsIgnoreCase( "-" )) {
                url_txt.setVisibility( View.GONE );
                title.setVisibility( View.GONE );

            } else {
                url_txt.setVisibility( View.VISIBLE );
               // title.setVisibility( View.VISIBLE );
            }
            if (datalist.get( position ).get( "shared_text" ).equalsIgnoreCase( "-" )) {
                shared_by_dscrption_txt.setVisibility( View.GONE );

            } else {
                shared_by_dscrption_txt.setVisibility( View.VISIBLE );
            }

            url_txt.setText( datalist.get( position ).get( "title" ) );
            //title.setText( datalist.get( position ).get( "title" ) );
            comments_count_txt.setText( datalist.get( position ).get( "comments_count" ) );
            like_txt_count.setText( datalist.get( position ).get( "likes_count" ) );
            share_txt.setText( datalist.get( position ).get( "shared_users_count" ) + " Shares" );

            shared_landing_dots_image.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (datalist.get( position ).get( "shared_by_customer_id" ).equalsIgnoreCase( StoredObjects.UserId )){
                        Sharepostpopup( activity,datalist,position,datalist.get( position ).get( "original_post_id" ),"sharedpost");
                    }
                    else {
                         EditDeleteConfirmation( activity, datalist, position,"sharedpost" );

                    }

                }
            } );

            landing_dots_image.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditDeleteConfirmation( activity, datalist, position ,"");

                   */
/* if (post_type.equalsIgnoreCase( "Direct Post" )){
                        EditDeleteConfirmation( activity, datalist, position );
                    }else {
                        Sharepostpopup( activity,datalist,position,datalist.get( position ).get( "original_post_id" ),"sharedpost");
                    }*//*

                }
            } );



            try {
                ArrayList<HashMap<String, String>> getmultipleimageslist = new ArrayList<>();
                getmultipleimageslist.clear();
                String files = datalist.get( position ).get( "files" );
                String files_count = datalist.get( position ).get( "files_count" );
                getmultipleimageslist = JsonParsing.GetJsonData( files );
                if (post_type.equalsIgnoreCase( "Direct Post" )) {
                    if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "0" )) {
                        post_img.setVisibility( View.GONE );
                        post_img2.setVisibility( View.GONE );
                        framelay_img.setVisibility( View.GONE );
                    }
                    if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "1" )) {
                        post_img.setVisibility( View.VISIBLE );
                        post_img2.setVisibility( View.GONE );
                        framelay_img.setVisibility( View.GONE );
                    }
                    if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "2" )) {
                        post_img.setVisibility( View.VISIBLE );
                        post_img2.setVisibility( View.VISIBLE );
                        framelay_img.setVisibility( View.VISIBLE );
                    }
                    if (getmultipleimageslist.size() > 2) {
                        more_imagestxt.setVisibility( View.VISIBLE );
                        post_img.setVisibility( View.VISIBLE );
                        post_img2.setVisibility( View.VISIBLE );
                        framelay_img.setVisibility( View.VISIBLE );
                        more_imagestxt.setText( "+" + (getmultipleimageslist.size() - 2) + "" );
                        // more_imagestxt.setText("1245");
                    } else {
                        more_imagestxt.setVisibility( View.GONE );
                    }
                }
                else {
                    if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "0" )) {
                        shared_post_img.setVisibility( View.GONE );
                        shared_post_img2.setVisibility( View.GONE );
                        shared_framelay_img.setVisibility( View.GONE );
                    }
                    if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "1" )) {
                        shared_post_img.setVisibility( View.VISIBLE );
                        shared_post_img2.setVisibility( View.GONE );
                        shared_framelay_img.setVisibility( View.GONE );
                    }
                    if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "2" )) {
                        shared_post_img.setVisibility( View.VISIBLE );
                        shared_post_img2.setVisibility( View.VISIBLE );
                        shared_framelay_img.setVisibility( View.VISIBLE );
                    }
                    if (getmultipleimageslist.size() > 2) {
                        shared_more_imagestxt.setVisibility( View.VISIBLE );
                        shared_post_img.setVisibility( View.VISIBLE );
                        shared_post_img2.setVisibility( View.VISIBLE );
                        shared_framelay_img.setVisibility( View.VISIBLE );
                        shared_more_imagestxt.setText( "+" + (getmultipleimageslist.size() - 2) + "" );
                        // more_imagestxt.setText("1245");
                    } else {
                        shared_more_imagestxt.setVisibility( View.GONE );
                        //shared_framelay_img.setVisibility( View.GONE );
                    }
                }
                try {
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + getmultipleimageslist.get( 0 ).get( "filename" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                            .crossFade()
                            //.override(600,600)
                            .into( post_img );
                    //  adapter.notifyItemInserted(position);
                } catch (Exception e) {
                }

                try {
                    StoredObjects.LogMethod( "<<><>>>>", "><><><><>>123" + getmultipleimageslist.get( 1 ).get( "filename" ) );
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "filename" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                            .crossFade()
                            //.override(600,600)
                            .into( post_img2 );
                    //  adapter.notifyItemInserted(position);

                } catch (Exception e) {
                }


                try {
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + getmultipleimageslist.get( 0 ).get( "filename" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                            .crossFade()
                            //.override(600,600)
                            .into( shared_post_img );
                    //  adapter.notifyItemInserted(position);
                } catch (Exception e) {
                }
                try {
                    StoredObjects.LogMethod( "<<><>>>>", "><><><><>>123" + getmultipleimageslist.get( 1 ).get( "filename" ) );
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "filename" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                            .crossFade()
                            //.override(600,600)
                            .into( shared_post_img2 );
                    //  adapter.notifyItemInserted(position);

                } catch (Exception e) {
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                //RequestOptions options = new RequestOptions();
                //options.optionalFitCenter();
                Glide.with( activity )
                        .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "profile_picture" ) ) ) // add your image url
                        //      .apply(options)
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.man )
                        .into( landing_circular_img );
            } catch (Exception e) {
            }
            landing_circular_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (datalist.get(position).get("customer_id").equalsIgnoreCase(StoredObjects.UserId)) {
                        fragmentcalling(new Profile());
                    } else {
                        fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "customer_id"));
                    }
                }
            });


            shared_by_profile_txt.setText( datalist.get( position ).get( "shared_customer" ) );
            shared_by_profile_updatedtime_txt.setText( datalist.get( position ).get( "created_at" ) );
            shared_by_dscrption_txt.setText( datalist.get( position ).get( "shared_text" ) );
            try {

                Glide.with( activity )
                        .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "shared_customer_profile_picture" ) ) )
                        .centerCrop()
                        .fitCenter()
                        .placeholder( R.drawable.man )
                        .into( shared_landing_circular_img );
            } catch (Exception e) {
            }
            shared_landing_circular_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (datalist.get(position).get("shared_by_customer_id").equalsIgnoreCase(StoredObjects.UserId)) {
                        fragmentcalling(new Profile());
                    } else {

                        fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "shared_by_customer_id" ));
                    }
                }
            });

            try {

                Glide.with( activity )
                        .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "profile_picture") ) )
                        .centerCrop()
                        .fitCenter()
                        .placeholder( R.drawable.man )
                        .into( postedby_landing_circular_img );
            } catch (Exception e) {
            }
            postedby_landing_circular_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (datalist.get(position).get("customer_id").equalsIgnoreCase(StoredObjects.UserId)) {
                        fragmentcalling(new Profile());
                    } else {

                        fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "customer_id" ));
                    }
                }
            });


           */
/* if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "0" )) {
                post_img.setVisibility( View.GONE );
                post_img2.setVisibility( View.GONE );
                shared_post_img.setVisibility( View.GONE );
                framelay_img.setVisibility( View.GONE );
            } else  if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "1" )){
                post_img.setVisibility( View.VISIBLE );
                post_img2.setVisibility( View.GONE );
                shared_post_img.setVisibility( View.VISIBLE );
                framelay_img.setVisibility( View.GONE );
            }
            else  if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "2" )){
                post_img.setVisibility( View.VISIBLE );
                post_img2.setVisibility( View.VISIBLE );
                shared_post_img.setVisibility( View.VISIBLE );
                shared_post_img2.setVisibility( View.VISIBLE );
                framelay_img.setVisibility( View.GONE );
            }
*//*





                //getmultipleimageslist = JsonParsing.GetJsonData( files );






               */
/* if (getmultipleimageslist.size() == 1) {
                    post_img.setVisibility( View.VISIBLE );
                    framelay_img.setVisibility( View.GONE );
                    shared_post_img.setVisibility( View.VISIBLE );
                    shared_framelay_img.setVisibility( View.GONE );

                } else {
                    // multiple_images_recycler.setVisibility( View.VISIBLE );
                    // post_img.setVisibility( View.GONE );
                    // customRecyclerview.Assigndatatorecyleviewhashmap1( multiple_images_recycler, getmultipleimageslist, "multipleimages", "Gridnew", 2, StoredObjects.ver_orientation, R.layout.multipleimageslistitem );
                }
                if (getmultipleimageslist.size() > 1) {
                    //  multiple_images_recycler.setVisibility( View.GONE );
                    framelay_img.setVisibility( View.VISIBLE );
                    shared_framelay_img.setVisibility( View.VISIBLE );
                } else {
                    // multiple_images_recycler.setVisibility( View.VISIBLE );
                    // post_img2.setVisibility( View.GONE );
                    // customRecyclerview.Assigndatatorecyleviewhashmap1( multiple_images_recycler, getmultipleimageslist, "multipleimages", "Gridnew", 2, StoredObjects.ver_orientation, R.layout.multipleimageslistitem );
                }

                if (getmultipleimageslist.size() > 2) {
                    more_imagestxt.setVisibility( View.VISIBLE );
                    shared_more_imagestxt.setVisibility( View.VISIBLE );
                    more_imagestxt.setText( "+" + (getmultipleimageslist.size() - 2) + "" );
                    // more_imagestxt.setText("1245");
                } else {
                    more_imagestxt.setVisibility( View.GONE );
                }*//*




        }
*/



        if (formtype.equalsIgnoreCase( "profile_catches" )) {



            try {
                ArrayList<HashMap<String, String>> getmultipleimageslist = new ArrayList<>();
                getmultipleimageslist.clear();
                String files = datalist.get( position ).get( "files" );
                String files_count = datalist.get( position ).get( "files_count" );
                getmultipleimageslist = JsonParsing.GetJsonData( files );
                String type = datalist.get( position ).get( "type" );


                try {
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + getmultipleimageslist.get( 0 ).get( "filename" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            // .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                            // .crossFade()
                            //.override(600,600)
                            .into( catch_circular_img );
                    //  adapter.notifyItemInserted(position);
                } catch (Exception e) {
                }


                catch_circular_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragmentcalling1( new Comments(), datalist, position );
                    }
                });



            } catch (Exception e) {
                e.printStackTrace();
            }



        }
        if (formtype.equalsIgnoreCase( "landingpage" )) {



            final String post_type = datalist.get( position ).get( "post_type" );

            StoredObjects.LogMethod("<><>>","<><><><><>>"+"post_type"+post_type+"<>><><><>"+datalist.size());

            if (post_type.equalsIgnoreCase( "Direct Post" )){
                shared_layout.setVisibility( View.GONE );
                try {
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "profile_picture" ) ) ) // add your image url
                            //      .apply(options)
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.man )
                            .into( landing_circular_img );
                } catch (Exception e) {
                }
                landing_circular_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (datalist.get(position).get("customer_id").equalsIgnoreCase(StoredObjects.UserId)) {
                            fragmentcalling(new Profile());
                        } else {
                            fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "customer_id"));
                        }
                    }
                });





                profile_txt.setText( datalist.get( position ).get( "posted_by" ) );
                profile_updatedtime_txt.setText( datalist.get( position ).get( "created_at" ) );

                if (datalist.get( position ).get( "title" ).equalsIgnoreCase( "-" )) {
                    url_txt.setVisibility( View.GONE );
                   // title.setVisibility( View.GONE );

                } else {
                    url_txt.setVisibility( View.VISIBLE );
                    // title.setVisibility( View.VISIBLE );
                }
                url_txt.setText( datalist.get( position ).get( "title" ) );
              //  url_txt.setText( Html.fromHtml(datalist.get( position ).get( "title" ) ));


               /* SpannableString ss = new SpannableString(
                        "text4: Click here to dial the phone.");

                ss.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new URLSpan(datalist.get( position ).get( "title" )), 13, 17,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
               // url_txt.setText(ss);*/


                /*String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

                Pattern p = Pattern.compile(URL_REGEX);
                Matcher m = p.matcher(datalist.get( position ).get( "title" ) );//replace with string to compare
                if(m.find()) {
                    System.out.println("String contains URL"+m);
                }*/



                //String text = StoredObjects.stripHtml1("");
                final SpannableString spanString = new SpannableString(Html.fromHtml(datalist.get( position ).get( "title" )));
                String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

               //  Pattern urlPattern = Pattern.compile( "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*" + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

               Matcher matcher = Pattern.compile(URL_REGEX).matcher(spanString);
             //   Matcher matcher = urlPattern.matcher(spanString);


                while (matcher.find())//activity.getResources().getColor(R.color.orange)
                {
                    spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#D95C16")), matcher.start(), matcher.end(), 0); //to highlight word havgin '@'

                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View textView) {
                            Log.i("click", "click "+"clicked" );
                            try {
                               // sendEmail("privacy@hyla.my");

                                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                                httpIntent.setData(Uri.parse(spanString+""));

                                activity.startActivity(httpIntent);

                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                            ds.setColor(Color.RED);

                        }
                    };
                    spanString.setSpan(clickableSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                url_txt.setText(spanString);
                url_txt.setMovementMethod(LinkMovementMethod.getInstance());






                if (datalist.get( position ).get( "description" ).equalsIgnoreCase( "-" )) {
                    dscrption_txt.setVisibility( View.GONE );

                } else {
                    dscrption_txt.setVisibility( View.VISIBLE );
                }
                String text = datalist.get( position ).get( "description" );
                 dscrption_txt.setText( text );
               /* if (text.length() > 300){
                    makeTextViewResizable( dscrption_txt, 4, "More", true );
                }*/



                /*if (text.length() > 600) {
                    text = text.substring(0, 600) + "...";
                     dscrption_txt.setText(Html.fromHtml(text + "<font color='hash'> <u>View More</u></font>"));
                     dscrption_txt.setText( datalist.get( position ).get( "description" ) );
                    dscrption_txt.setText( text );
                    makeTextViewResizable( dscrption_txt, 20, "More", true );

                } else {
                    dscrption_txt.setText( text );
                }*/
               // dscrption_txt.setText( datalist.get( position ).get( "description" ) );

                landing_dots_image.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditDeleteConfirmation( activity, datalist, position ,"");

                    }
                } );


                try{
                    ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
                    images_array.clear();
                    images_array = JsonParsing.GetJsonData(datalist.get(position).get("fishings_spots_list"));
                    if (images_array.size() > 0) {
                        fishingspots_lay.removeAllViews();

                        for (int i = 0; i <images_array.size() ; i++) {
                            addlayoutfishingspots(fishingspots_lay,images_array,i);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }



                shared_view.setVisibility( View.GONE );
            }



            else {
                shared_layout.setVisibility( View.VISIBLE );
                try {

                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "shared_customer_profile_picture" ) ) )
                            .centerCrop()
                            .fitCenter()
                            .placeholder( R.drawable.man )
                            .into( landing_circular_img );
                } catch (Exception e) {
                }
                landing_circular_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (datalist.get(position).get("shared_by_customer_id").equalsIgnoreCase(StoredObjects.UserId)) {
                            fragmentcalling(new Profile());
                        } else {

                            fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "shared_by_customer_id" ));
                        }
                    }
                });
                profile_txt.setText( datalist.get( position ).get( "shared_customer" ) );
                profile_updatedtime_txt.setText( datalist.get( position ).get( "created_at" ) );
              /*  if (datalist.get( position ).get( "title" ).equalsIgnoreCase( "-" )) {
                    url_txt.setVisibility( View.GONE );
                    //title.setVisibility( View.GONE );

                } else {
                    url_txt.setVisibility( View.VISIBLE );
                    // title.setVisibility( View.VISIBLE );
                }*/

               // url_txt.setText( datalist.get( position ).get( "title" ) );

                if (datalist.get( position ).get( "shared_text" ).equalsIgnoreCase( "-" )) {
                    dscrption_txt.setVisibility( View.GONE );

                } else {
                    dscrption_txt.setVisibility( View.VISIBLE );
                }
                String text = datalist.get( position ).get( "shared_text" );
               // dscrption_txt.setText( text );
                url_txt.setVisibility( View.GONE );

                dscrption_txt.setText( text );
                /*if (text.length() > 300){
                    makeTextViewResizable( dscrption_txt, 4, "More", true );
                }*/

                /*if (text.length() > 300) {
                    text = text.substring(0, 300) + "...";
                     dscrption_txt.setText(Html.fromHtml(text + "<font color='hash'> <u>View More</u></font>"));
                     dscrption_txt.setText( datalist.get( position ).get( "shared_text" ) );
                    dscrption_txt.setText( text );
                    makeTextViewResizable( dscrption_txt, 20, "More", true );

                } else {
                    dscrption_txt.setText( text );
                }*/
                //dscrption_txt.setText( datalist.get( position ).get( "shared_text" ) );

                try {

                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "profile_picture") ) )
                            .centerCrop()
                            .fitCenter()
                            .placeholder( R.drawable.man )
                            .into( postedby_landing_circular_img );
                } catch (Exception e) {
                }
                shared_profile_txt.setText( datalist.get( position ).get("posted_by"));
                postedby_landing_circular_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (datalist.get(position).get("customer_id").equalsIgnoreCase(StoredObjects.UserId)) {
                            fragmentcalling(new Profile());
                        } else {

                            fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "customer_id" ));
                        }
                    }
                });
                shared_profile_updatedtime_txt.setText( datalist.get( position ).get( "created_at" ) );
                if (datalist.get( position ).get( "description" ).equalsIgnoreCase( "-" )) {
                    shared_by_dscrption_txt.setVisibility( View.GONE );

                } else {
                    shared_by_dscrption_txt.setVisibility( View.VISIBLE );
                }
                String sharedtext = datalist.get( position ).get( "description" );
                shared_by_dscrption_txt.setText( sharedtext );
                //shared_by_dscrption_txt.setText( sharedtext );

                if (sharedtext.length() > 300) {
                    makeTextViewResizable( shared_by_dscrption_txt, 2, "More", true );

                } else {
                    shared_by_dscrption_txt.setText( sharedtext );
                }
                /*if (sharedtext.length() > 120) {
                    sharedtext = sharedtext.substring(0, 120) + "...";
                    shared_by_dscrption_txt.setText(Html.fromHtml(text + "<font color='hash'> <u>View More</u></font>"));
                    shared_by_dscrption_txt.setText( datalist.get( position ).get( "description" ) );
                    shared_by_dscrption_txt.setText( sharedtext );
                    makeTextViewResizable( shared_by_dscrption_txt, 2, "More", true );

                } else {
                    shared_by_dscrption_txt.setText( sharedtext );
                }*/
               // shared_by_dscrption_txt.setText( datalist.get( position ).get( "description" ) );

                landing_dots_image.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (datalist.get( position ).get( "shared_by_customer_id" ).equalsIgnoreCase( StoredObjects.UserId )){
                            //Sharepostpopup( activity,datalist,position,datalist.get( position ).get( "original_post_id" ),"sharedpost");
                            EditDeleteConfirmation( activity, datalist, position,"sharedpost_own" );
                        }
                        else {
                            EditDeleteConfirmation( activity, datalist, position,"sharedpost" );
                        }
                    }
                } );


                try{
                    ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
                    images_array.clear();
                    images_array = JsonParsing.GetJsonData(datalist.get(position).get("fishings_spots_list"));
                    if (images_array.size() > 0) {
                        fishingspots_lay.removeAllViews();

                        for (int i = 0; i <images_array.size() ; i++) {
                            addlayoutfishingspots(fishingspots_lay,images_array,i);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


                shared_view.setVisibility( View.VISIBLE );
            }


            images_lay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentcalling1( new Comments(), datalist, position );
                }
            } );
            share__lay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (post_type.equalsIgnoreCase( "Direct Post" )){
                        Sharepostpopup( activity, datalist, position, datalist.get( position ).get("post_id"),"" );
                    }
                    else {
                        Sharepostpopup( activity, datalist, position, datalist.get( position ).get("original_post_id"),"" );
                    }

                }
            } );
            comment__lay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentcalling1( new Comments(), datalist, position );
                }
            } );

            comments_count = datalist.get( position ).get( "is_liked" );

            if (comments_count.equalsIgnoreCase( "No" )) {
                like_img.setImageResource( R.drawable.like_button );
                like_txt.setTextColor( activity.getResources().getColor( R.color.lite_color_text ) );

            } else {
                like_img.setImageResource( R.drawable.liked );
                like_txt.setTextColor( activity.getResources().getColor( R.color.orange ) );

            }


            String likescount = datalist.get( position ).get( "likes_count" );
            if (likescount.equals( "0" )){
                liked_by_layout.setClickable( false );
            }
            else
            {
                liked_by_layout.setClickable( true );

                liked_by_layout.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            new GetPostdetailsTask().execute(StoredObjects.UserId,datalist.get( position ).get( "post_id" ));
                        }else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                        }

                    }
                } );

            }

            String commentscount = datalist.get( position ).get( "comments_count" );
            if (commentscount.equals( "0" )){
                commented_by_layout.setClickable( false );
            }
            else
            {
                commented_by_layout.setClickable( true );

                commented_by_layout.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            new GetPostdetailsTaskNew().execute(StoredObjects.UserId,datalist.get( position ).get( "post_id" ));
                        }else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                        }

                    }
                } );

            }

            like__lay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (comments_count.equalsIgnoreCase( "No" )) {
                        if (InterNetChecker.isNetworkAvailable( activity )) {
                            datalist_test = datalist;
                            new LikepostTask().execute( StoredObjects.UserId, datalist.get( position ).get( "post_id" ) );
                            updateqtydata( datalist, datalist.get( position ), position, "Yes", "landingpage" );


                        } else {
                            StoredObjects.ToastMethod( activity.getResources().getString( R.string.checkinternet ), activity );
                        }
                    } else {
                        //StoredObjects.ToastMethod("Already liked", activity);

                        if (InterNetChecker.isNetworkAvailable( activity )) {
                            datalist_test = datalist;
                            new UnLikepostTask().execute( StoredObjects.UserId, datalist.get( position ).get( "post_id" ) );
                            updateqtydata( datalist, datalist.get( position ), position, "No", "landingpage" );

                        } else {
                            StoredObjects.ToastMethod( activity.getResources().getString( R.string.checkinternet ), activity );
                        }
                    }
                }
            } );


            //shared_profile_txt.setText( datalist.get( position ).get( "posted_by" ) );




            //shared_dscrption_txt.setText( datalist.get( position ).get( "description" ) );



          /*  if (datalist.get( position ).get( "description" ).equalsIgnoreCase( "-" )) {
                dscrption_txt.setVisibility( View.GONE );
                shared_dscrption_txt.setVisibility( View.GONE );
            } else {
                dscrption_txt.setVisibility( View.VISIBLE );
                shared_dscrption_txt.setVisibility( View.VISIBLE );
            }*/





            //title.setText( datalist.get( position ).get( "title" ) );
            comments_count_txt.setText( datalist.get( position ).get( "comments_count" ) );
            like_txt_count.setText( datalist.get( position ).get( "likes_count" ) );
            share_txt.setText( datalist.get( position ).get( "shared_users_count" ) + " Shares" );





            try {
                ArrayList<HashMap<String, String>> getmultipleimageslist = new ArrayList<>();
                getmultipleimageslist.clear();
                String files = datalist.get( position ).get( "files" );
                String files_count = datalist.get( position ).get( "files_count" );
                getmultipleimageslist = JsonParsing.GetJsonData( files );
                String type = datalist.get( position ).get( "type" );

                if (type.equalsIgnoreCase( "video" )){
                    images_lay.setVisibility( View.GONE );
                    simpleVideoView.setVisibility( View.VISIBLE );

                   // Uri uri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4");
                    Uri uri = Uri.parse(StoredUrls.Uploadedimages + datalist.get( position ).get( "filename"));
                    StoredObjects.LogMethod( "uri","++"+uri );
                   // simpleVideoView.startPlay("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4", MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "video name");
                    simpleVideoView.startPlay( String.valueOf( uri ), MxVideoPlayer.NORMAL_ORIENTATION, "");
                    simpleVideoView.mFullscreenButton.setVisibility( View.GONE );

                }else {
                    images_lay.setVisibility( View.VISIBLE );
                    simpleVideoView.setVisibility( View.GONE );
                }
                    if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "0" )) {
                        post_img.setVisibility( View.GONE );
                        post_img2.setVisibility( View.GONE );
                        framelay_img.setVisibility( View.GONE );
                    }
                    if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "1" )) {
                        post_img.setVisibility( View.VISIBLE );
                        post_img2.setVisibility( View.GONE );
                        framelay_img.setVisibility( View.GONE );
                    }
                    if (datalist.get( position ).get( "files_count" ).equalsIgnoreCase( "2" )) {
                        post_img.setVisibility( View.VISIBLE );
                        post_img2.setVisibility( View.VISIBLE );
                        framelay_img.setVisibility( View.VISIBLE );
                    }
                    if (getmultipleimageslist.size() > 2) {
                        more_imagestxt.setVisibility( View.VISIBLE );
                        post_img.setVisibility( View.VISIBLE );
                        post_img2.setVisibility( View.VISIBLE );
                        framelay_img.setVisibility( View.VISIBLE );
                        more_imagestxt.setText( "+" + (getmultipleimageslist.size() - 2) + "" );
                        // more_imagestxt.setText("1245");
                    } else {
                        more_imagestxt.setVisibility( View.GONE );
                    }

                    try {
                        Glide.with( activity )
                                .load( Uri.parse( StoredUrls.Uploadedimages + getmultipleimageslist.get( 0 ).get( "filename" ) ) ) // add your image url
                                .centerCrop() // scale to fill the ImageView and crop any extra
                                .fitCenter() // scale to fit entire image within ImageView
                                .placeholder( R.drawable.splash_logo_new )
                                // .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                                // .crossFade()
                                //.override(600,600)
                                .into( post_img );
                        //  adapter.notifyItemInserted(position);
                    } catch (Exception e) {
                    }

                    try {
                        StoredObjects.LogMethod( "<<><>>>>", "><><><><>>123" + getmultipleimageslist.get( 1 ).get( "filename" ) );
                        Glide.with( activity )
                                .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "filename" ) ) ) // add your image url
                                .centerCrop() // scale to fill the ImageView and crop any extra
                                .fitCenter() // scale to fit entire image within ImageView
                                .placeholder( R.drawable.splash_logo_new )
                                //.diskCacheStrategy( DiskCacheStrategy.SOURCE )
                                // .crossFade()
                                //.override(600,600)
                                .into( post_img2 );
                        //  adapter.notifyItemInserted(position);

                    } catch (Exception e) {
                    }


               /* if (post_type.equalsIgnoreCase( "Direct Post" )) {*/



/*
                try {
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + getmultipleimageslist.get( 0 ).get( "filename" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                            .crossFade()
                            //.override(600,600)
                            .into( shared_post_img );
                    //  adapter.notifyItemInserted(position);
                } catch (Exception e) {
                }
*/
/*
                try {
                    StoredObjects.LogMethod( "<<><>>>>", "><><><><>>123" + getmultipleimageslist.get( 1 ).get( "filename" ) );
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "filename" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                            .crossFade()
                            //.override(600,600)
                            .into( shared_post_img2 );
                    //  adapter.notifyItemInserted(position);

                } catch (Exception e) {
                }
*/

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        if (formtype.equalsIgnoreCase( "fishingspots_multipleimages" )) {

            try {
                StoredObjects.LogMethod( "<<><>>>>", "><><><><>>123" + StoredUrls.Uploadedimages+datalist.get( position ).get( "image" ) );

               // StoredUrls.Uploadedimages +
                Glide.with( activity )
                        .load( Uri.parse( StoredUrls.Uploadedimages+datalist.get( position ).get( "image" ) ) ) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.splash_logo_new )
                        // .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                        // .crossFade()
                        //.override(600,600)
                        .into( image_view_icon );
                //  adapter.notifyItemInserted(position);

            } catch (Exception e) {
            }

            image_view_icon.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent intent = new Intent( activity,Viewpager_gallery.class );
                    intent.putExtra( "position",position+"" );
                    intent.putExtra("YourHashMap", datalist);
                    activity.startActivity( intent );*/
                }
            } );

        }

        if (formtype.equalsIgnoreCase( "fishingspots_multipleimages_" )) {

            try {
                StoredObjects.LogMethod( "<<><>>>>", "><><><><>>123" + StoredUrls.Uploadedimages+datalist.get( position ).get( "image" ) );

                // StoredUrls.Uploadedimages +
                Glide.with( activity )
                        .load( Uri.parse( StoredUrls.Uploadedimages+datalist.get( position ).get( "image" ) ) ) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.splash_logo_new )
                        // .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                        // .crossFade()
                        //.override(600,600)
                        .into( image_view_icon );
                //  adapter.notifyItemInserted(position);

            } catch (Exception e) {
            }

            image_view_icon.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent( activity,Viewpager_gallery.class );
                    intent.putExtra( "position",position+"" );
                    intent.putExtra("YourHashMap", datalist);
                    activity.startActivity( intent );
                }
            } );

        }


        if (formtype.equalsIgnoreCase( "fishingspots_fishbreeds" )) {
            StoredObjects.LogMethod( "<<><>>>>", "><><><><>>123" + datalist.get( position ).get( "species" ) );

            try {
                fishbreeds_name.setText(datalist.get(position).get("species"));

            } catch (Exception e) {
                StoredObjects.LogMethod( "<<><>>>>", "fishingspots_fishbreeds<><hashmapviewholder>" + e );

            }

            fishbreeds_name.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //StoredObjects.LogMethod( "<<><>>>>", "><><><><>>123" + datalist.get( position ).get( "species" ) );
                    fishbreeds_details_popup(activity,datalist,position);

                }
            } );

        }


        if (formtype.equalsIgnoreCase( "multipleimages" )) {


            try {
                StoredObjects.LogMethod( "<<><>>>>", "><><><><>>123" + datalist.get( position ).get( "filename" ) );
                Glide.with( activity )
                        .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get( position ).get( "filename" ) ) ) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.splash_logo_new )
                       // .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                       // .crossFade()
                        //.override(600,600)
                        .into( image_view_icon );
                //  adapter.notifyItemInserted(position);

            } catch (Exception e) {
            }



        }



        if (formtype.equalsIgnoreCase("identified_breed_common")) {

            lakename_txt.setText(datalist.get(position).get("species"));
            lakelocation_txt.setText(datalist.get(position).get("season"));
            lakedetails_txt.setText(datalist.get(position).get("extra_info"));

            try{
                view_color.setVisibility(View.GONE);
                percentagevalue.setVisibility(View.GONE);
            }catch (Exception e){

            }



            try {
                ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
                images_array.clear();
                images_array = JsonParsing.GetJsonData(datalist.get( position ).get( "images" ));
                if(images_array.size()>0){
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + images_array.get(0).get( "image" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            // .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            // .crossFade()
                            //.override(600,600)
                            .into( lake_image );
                }

                //  adapter.notifyItemInserted(position);
            } catch (Exception e) {
            }




            fishingspots_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fishbreeds_details_popup(activity,datalist,position);
                }
            });
            directions_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    StoredObjects.Selected_fishspotname = "";
                    StoredObjects.Selected_fishspot_id = "";
                    StoredObjects.Selected_fishspot_lat="0";
                    StoredObjects.Selected_fishspot_lng="0";
                    StoredObjects.Selected_fishspot_address="";

                    activity.finish();
                    Intent intent = new Intent(activity, AddpostBreedActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST",datalist);
                    // args.putInt("position", position);
                    intent.putExtra("BUNDLE",args);
                    intent.putExtra("position", position);
                    // intent.putStringArrayListExtra("array", fishbreeds_list);
                    activity.startActivity(intent);

                }
            });
        }



        if (formtype.equalsIgnoreCase("identified_breed")) {

            lakename_txt.setText(datalist.get(position).get("species"));
            lakelocation_txt.setText(datalist.get(position).get("season"));
            lakedetails_txt.setText(datalist.get(position).get("extra_info"));

            try{
                view_color.setBackgroundColor(Color.parseColor(datalist.get(position).get("colorstrip")));
            }catch (Exception e){

            }


              try{
                  percentagevalue.setText("Identified:"+StoredObjects.mathfloat(Float.parseFloat(datalist.get(position).get("percentageval")))+"%");
              }catch (Exception e){
                  percentagevalue.setText("Identified:"+datalist.get(position).get("percentageval")+"%");
              }



            try {
                ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
                images_array.clear();
                images_array = JsonParsing.GetJsonData(datalist.get( position ).get( "images" ));
                if(images_array.size()>0){
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + images_array.get(0).get( "image" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            // .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            // .crossFade()
                            //.override(600,600)
                            .into( lake_image );
                }

                //  adapter.notifyItemInserted(position);
            } catch (Exception e) {
            }




            fishingspots_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fishbreeds_details_popup(activity,datalist,position);
                }
            });
            directions_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StoredObjects.Selected_fishspotname = "";
                    StoredObjects.Selected_fishspot_id = "";
                    StoredObjects.Selected_fishspot_lat="0";
                    StoredObjects.Selected_fishspot_lng="0";
                    StoredObjects.Selected_fishspot_address="";

                    if(StoredObjects.mathfloat(Float.parseFloat(datalist.get(position).get("percentageval"))) < 50){
                        AddpostConfitmation(activity,"Fish Breed identified with an accuracy score lower than 50%. Do you still want to post your catch?",datalist,position);
                    }else{
                         activity.finish();
                    Intent intent = new Intent(activity, AddpostBreedActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST",datalist);
                   // args.putInt("position", position);
                    intent.putExtra("BUNDLE",args);
                        intent.putExtra("position", position);
                    // intent.putStringArrayListExtra("array", fishbreeds_list);
                    activity.startActivity(intent);
                    }




                  //  fishbreeds_details_popup(activity,datalist,position);
                   // activity.finish();
                   // fragmentcalling1(new AddPostFromBreedCheck(),datalist,position);


                }
            });
        }


        if (formtype.equalsIgnoreCase("fishingspots_")) {

            lakename_txt.setText(datalist.get(position).get("name"));
            lakelocation_txt.setText(datalist.get(position).get("address"));
            lakedetails_txt.setText(datalist.get(position).get("description"));

            area_count_txt.setText(datalist.get(position).get("lat"));
            maxdepth_count_txt.setText(datalist.get(position).get("lat"));

            StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_result<><>"+datalist.get( position ).get( "repository_image" ));
            try {
                if(datalist.get( position ).get( "repository_image" ).equalsIgnoreCase("-")
                        ||datalist.get( position ).get( "repository_image" ).equalsIgnoreCase("")
                        ||datalist.get( position ).get( "repository_image" ).equalsIgnoreCase("null")
                        ||datalist.get( position ).get( "repository_image" ).equalsIgnoreCase(null)) {
                    ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
                    images_array.clear();
                    images_array = JsonParsing.GetJsonData(datalist.get(position).get("images"));
                    if (images_array.size() > 0) {
                        Glide.with(activity)
                                .load(Uri.parse(StoredUrls.Uploadedimages + images_array.get(0).get("image"))) // add your image url
                                .centerCrop() // scale to fill the ImageView and crop any extra
                                .fitCenter() // scale to fit entire image within ImageView
                                .placeholder(R.drawable.splash_logo_new)
                                // .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                // .crossFade()
                                //.override(600,600)
                                .into(lake_image);
                    }

                }else{

                    Glide.with(activity)
                            .load(Uri.parse(datalist.get( position ).get( "repository_image" ))) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder(R.drawable.splash_logo_new)
                            // .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            // .crossFade()
                            //.override(600,600)
                            .into(lake_image);
                }
                //  adapter.notifyItemInserted(position);
            } catch (Exception e) {
            }


            directions_txt.setText("Tag Spot");

            fishingspots_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();
                    StoredObjects.Selected_fishspotname = datalist.get(position).get("name");
                    StoredObjects.Selected_fishspot_id = datalist.get(position).get("id");
                    StoredObjects.Selected_fishspot_lat = datalist.get(position).get("lat");
                    StoredObjects.Selected_fishspot_lng = datalist.get(position).get("lng");
                    StoredObjects.Selected_fishspot_address = datalist.get(position).get("address");

                }
            });
            directions_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();

                    StoredObjects.Selected_fishspotname = datalist.get(position).get("name");
                    StoredObjects.Selected_fishspot_id = datalist.get(position).get("id");

                    StoredObjects.Selected_fishspot_lat = datalist.get(position).get("lat");
                    StoredObjects.Selected_fishspot_lng = datalist.get(position).get("lng");
                    StoredObjects.Selected_fishspot_address = datalist.get(position).get("address");

                }
            });
        }


        if (formtype.equalsIgnoreCase("fishingspots")) {

            lakename_txt.setText(datalist.get(position).get("name"));
            lakelocation_txt.setText(datalist.get(position).get("address"));
            lakedetails_txt.setText(datalist.get(position).get("description"));

            area_count_txt.setText(datalist.get(position).get("lat"));
            maxdepth_count_txt.setText(datalist.get(position).get("lat"));

           // StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_result<><>"+datalist.get( position ).get( "repository_image" ));
            try {

                ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
                images_array.clear();
                images_array = JsonParsing.GetJsonData(datalist.get(position).get("images"));
                if (images_array.size() > 0) {
                    Glide.with(activity)
                            .load(Uri.parse(StoredUrls.Uploadedimages + images_array.get(0).get("image"))) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder(R.drawable.splash_logo_new)
                            // .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            // .crossFade()
                            //.override(600,600)
                            .into(lake_image);
                }


               /* if(datalist.get( position ).get( "repository_image" ).equalsIgnoreCase("-")
                        ||datalist.get( position ).get( "repository_image" ).equalsIgnoreCase("")
                ||datalist.get( position ).get( "repository_image" ).equalsIgnoreCase("null")
                        ||datalist.get( position ).get( "repository_image" ).equalsIgnoreCase(null)) {


                }else{

                    Glide.with(activity)
                            .load(Uri.parse(datalist.get( position ).get( "repository_image" ))) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder(R.drawable.splash_logo_new)
                            // .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            // .crossFade()
                            //.override(600,600)
                            .into(lake_image);
                }*/
                //  adapter.notifyItemInserted(position);
            } catch (Exception e) {
            }




            fishingspots_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentcalling1(new Fishingspots_details(),datalist,position);
                }
            });
            directions_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                      //  String url = "https://www.google.com/maps/dir/?api=1&destination=" + datalist.get(position).get("lat") + "° N" + "," +datalist.get(position).get("lng") + "° W" /*"&travelmode=driving"*/;

                        String url = "https://www.google.com/maps/dir/?api=1&destination=" + datalist.get(position).get("lat") + "," +datalist.get(position).get("lng") /*"&travelmode=driving"*/;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        StoredObjects.ToastMethod(" Address Not found", activity);
                    }
/*
                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            Uri gmmIntentUri = Uri.parse("geo:25.2744,133.7751?q=");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            activity.startActivity(mapIntent);
                        }
                    }, 1000);
*/
                }
            });
        }
        if (formtype.equalsIgnoreCase("fishbreeds")) {

            track_txt.setText(datalist.get( position ).get( "species" ));

            try {
                ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
                images_array.clear();
                images_array = JsonParsing.GetJsonData(datalist.get( position ).get( "images" ));
                if(images_array.size()>0){
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + images_array.get(0).get( "image" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                           // .crossFade()
                            //.override(600,600)
                            .into( image_view_icon );
                }else{

                }

                //  adapter.notifyItemInserted(position);
            } catch (Exception e) {
            }

            fishbreed_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fishbreeds_details_popup(activity,datalist,position);
                }
            });
            //track_txt.setText( datalist.get( position ).get( "name" ) );
        }
        if (formtype.equalsIgnoreCase("fishing_tricks")) {
            int_count_txt.setText(Integer.toString(position));

            fishing_title_txt.setText(datalist.get(position).get("title"));
            //fishing_tricks__txt.setText(datalist.get(position).get("description"));


            String text = datalist.get(position).get("description");
            fishing_tricks__txt.setText(StoredObjects.stripHtml(text));
            /*if (text.length() > 300) {
                // text = text.substring(0, 120) + "...";
                // dscrption_txt.setText(Html.fromHtml(text + "<font color='hash'> <u>View More</u></font>"));
                //  dscrption_txt.setText( datalist.get( position ).get( "description" ) );
                fishing_tricks__txt.setText(StoredObjects.stripHtml(text));
                makeTextViewResizable(fishing_tricks__txt, 4, "More", true);

            } else {
                fishing_tricks__txt.setText(StoredObjects.stripHtml(text));
            }*/


            if(datalist.get(position).get("file_type").equalsIgnoreCase("Mp4")){


                if(datalist.get(position).get( "video_file" ).equalsIgnoreCase("-")){
                    video_play_layout.setVisibility(View.GONE);
                    fishingtricks_img.setVisibility(View.GONE);

                }else{
                    video_play_layout.setVisibility(View.VISIBLE);
                }

            }else if(datalist.get(position).get("file_type").equalsIgnoreCase("Youtube")){
                video_play_layout.setVisibility(View.VISIBLE);

                try {

                    String youtube_link = StoredObjects.getYouTubeId(datalist.get(position).get( "video_file" ));
                    String image_link = "https://img.youtube.com/vi/"+youtube_link+"/hqdefault.jpg";
                    Glide.with( activity )
                            .load( Uri.parse( image_link ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            //.crossFade()
                            //.override(600,600)
                            .into( fishingtricks_img );
                }catch (Exception e){

                }



            }else if(datalist.get(position).get("file_type").equalsIgnoreCase("Image")){
                video_play_layout.setVisibility(View.GONE);

                try {
                    Glide.with( activity )
                            .load( Uri.parse( StoredUrls.Uploadedimages + datalist.get(position).get( "video_file" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                           // .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                           // .crossFade()
                            //.override(600,600)
                            .into( fishingtricks_img );
                }catch (Exception e){

                }
            }


            video_play_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(datalist.get(position).get("file_type").equalsIgnoreCase("Youtube")){
                        StoredUrls.youtubelink = datalist.get(position).get( "video_file" );
                        activity.startActivity(new Intent(activity, Youtube_newActivity.class));

                    }else if(datalist.get(position).get("file_type").equalsIgnoreCase("Mp4")){
                        Intent intent = new Intent(activity, VideoViewActivity.class);
                        intent.putExtra("media_file", StoredUrls.Uploadedimages+datalist.get(position).get( "video_file" ));
                        activity.startActivity(intent);
                    }

                }
            });









           /* RelativeLayout video_play_layout;
            CustomBoldTextView fishing_title_txt;
            CustomRegularTextView fishing_tricks__txt;*/

          /*  Bitmap thumb = ThumbnailUtils.createVideoThumbnail("https://www.youtube.com/watch?v=uilkmUoXoLU",
                    MediaStore.Images.Thumbnails.MINI_KIND);
            fishingtricks_img.setImageBitmap( thumb );*/
/*
            Glide.with(activity)
                    .load("https://www.youtube.com/watch?v=uilkmUoXoLU") // or URI/path
                    .into(fishingtricks_img);
*/

/*
            Glide.with(activity)
                    .load("file:///storage/emulated/0/WhatsApp/Media/WhatsApp Video/VID-20190923-WA0000.mp4")
                    .centerCrop()
                    .placeholder(Color.BLUE)
                    .crossFade()
                    .into(fishingtricks_img);
*/
        }
        if (formtype.equalsIgnoreCase("following")) {
            profile_circular_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "customer_id" ));
                }
            });
            profile_name_txt.setText(datalist.get(position).get("name"));

            try {
                Glide.with(activity)
                        .load(Uri.parse(StoredUrls.Uploadedimages +datalist.get(position).get("image"))) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder(R.drawable.man)
                        .into(profile_circular_img);
            } catch (Exception e) {
            }


            if( datalist.get(position).get("is_following").equalsIgnoreCase("Yes")){
                follow_btn.setText(R.string.unfollow);
            }else{
                follow_btn.setText(R.string.follow);
            }

            follow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if( datalist.get(position).get("is_following").equalsIgnoreCase("Yes")){
                        follow_btn.setText(R.string.following);

                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            new UnFollowTask().execute(datalist.get(position).get("customer_id"));
                        } else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet), activity);
                        }
                        updateqtydata(datalist,datalist.get(position),position,"No","following_profile");

                    }else{

                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            new FollowTask().execute(StoredObjects.UserId, datalist.get(position).get("customer_id"));
                        } else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet), activity);
                        }
                    }



                }
            });
        }
        if (formtype.equalsIgnoreCase("Followers")) {
            String id = "";
            id = datalist.get(position).get("customer_id");
            final String finalId = id;


            profile_circular_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "customer_id" ));
                }
            });

            profile_name_txt.setText(datalist.get(position).get("name"));

            try {
                Glide.with(activity)
                        .load(Uri.parse(StoredUrls.Uploadedimages +datalist.get(position).get("image"))) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder(R.drawable.man)
                        .into(profile_circular_img);
            } catch (Exception e) {
            }


            if( datalist.get(position).get("is_following").equalsIgnoreCase("Yes")){
                follow_btn.setText(R.string.unfollow);
            }else{
                follow_btn.setText(R.string.follow);
            }



            follow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if( datalist.get(position).get("is_following").equalsIgnoreCase("Yes")){
                        follow_btn.setText(R.string.following);

                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            new UnFollowTask().execute(datalist.get(position).get("customer_id"));
                        } else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet), activity);
                        }
                        updateqtydata(datalist,datalist.get(position),position,"No","followers_profile");

                    }else{

                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            new FollowTask().execute(StoredObjects.UserId, datalist.get(position).get("customer_id"));
                        } else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet), activity);
                        }
                        updateqtydata(datalist,datalist.get(position),position,"Yes","followers_profile");
                    }
                }
            });

        }
        if (formtype.equalsIgnoreCase("profile_followers")) {
            followers_profile_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "customer_id" ));
                }
            });
            followers_profile_name_txt.setText(datalist.get(position).get("name"));
            try {
                Glide.with(activity)
                        .load(Uri.parse(StoredUrls.Uploadedimages +datalist.get(position).get("image"))) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder(R.drawable.man)
                        .into(followers_circular_img);
            } catch (Exception e) {
            }

        }
        if (formtype.equalsIgnoreCase("profile_following")) {
            followers_profile_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "customer_id" ));
                }
            });

            follwing_profile_name_txt.setText(datalist.get(position).get("name"));
            try {
                Glide.with(activity)
                        .load(Uri.parse(StoredUrls.Uploadedimages +datalist.get(position).get("image"))) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder(R.drawable.man)
                        .into(following_circular_img);
            } catch (Exception e) {
            }
        }

        if (formtype.equalsIgnoreCase("notifications")) {


            ntfc_profile_name_txt.setText( datalist.get( position ).get("customer_name"));//+"<>"+datalist.get( position ).get("notification_id" ));
            ntfc_date_time.setText( datalist.get( position ).get("created_at" ));
            ntfc_description_txt.setText( datalist.get( position ).get("description"));

            try{
                if(datalist.get( position ).get( "notification_type" ).equalsIgnoreCase("User commented on Post")){
                    ntfc_description_txt.setText( datalist.get( position ).get("description")+"  "+datalist.get( position ).get("comments"));
                }else{
                    ntfc_description_txt.setText( datalist.get( position ).get("description"));
                }
            }catch (Exception e){

            }


            String status = datalist.get( position ).get( "status" );


            final String sel_favorites = datalist.get( position ).get( "sel_favorites" );
            if ( sel_favorites.equalsIgnoreCase( "No" )){
                delte_checkboxselection.setVisibility(View.GONE);
            }
            else {
                delte_checkboxselection.setVisibility(View.VISIBLE);
            }
            final String addedtocart = datalist.get( position ).get( "addedtocart" );

            if ( addedtocart.equalsIgnoreCase( "No" )){
                delte_checkboxselection.setChecked(false);
            }
            else {
                delte_checkboxselection.setChecked(true);
            }

            delte_checkboxselection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( addedtocart.equalsIgnoreCase( "No" )){
                        updateqtydata(datalist,datalist.get( position ),position,"Yes","checkboxchecked");
                    }
                    else {
                        updateqtydata(datalist,datalist.get( position ),position,"No","checkboxchecked");
                    }

                }
            });


            notificationdetails_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        fragmentcalling2( new CommentsNew(), datalist.get(position).get("post_id"));
                    }catch (Exception e){

                    }
                }
            });



            notific_listitems_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( addedtocart.equalsIgnoreCase( "No" )){
                        updateqtydata(datalist,datalist.get( position ),position,"Yes","checkboxchecked");
                    }
                    else {
                        updateqtydata(datalist,datalist.get( position ),position,"No","checkboxchecked");
                    }
                }
            });


            notific_listitems_lay.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                   DeleteNotificationpopup( activity,datalist,position );
                    return false;
                }
            } );

            if ( status.equalsIgnoreCase( "UnRead" )){
                notific_listitems_lay.setBackgroundColor( activity.getResources().getColor(R.color.yashbg));

            }
            else {
                notific_listitems_lay.setBackgroundColor(activity.getResources().getColor(R.color.white) );
            }

            try {
                Glide.with(activity)
                        .load(Uri.parse(StoredUrls.Uploadedimages +datalist.get(position).get("profile_picture"))) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder(R.drawable.man)
                        .into(landing_circular_img);
            } catch (Exception e) {
            }
            landing_circular_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (InterNetChecker.isNetworkAvailable(activity)) {
                        new  ChangeNotificationstatusTask().execute(StoredObjects.UserId,datalist.get(position).get("notification_id"));
                        //  updateqtydata(datalist,datalist.get( position ),position,"No","landingpage_hidepost");

                    }else{
                        StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                    }

                    if (datalist.get(position).get("created_customer_id").equalsIgnoreCase(StoredObjects.UserId)) {
                        fragmentcalling(new Profile());
                    } else {
                        fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "created_customer_id"));
                    }
                }
            });
        }

        if (formtype.equalsIgnoreCase("comments_popup")) {
            // Collections.reverse(datalist);
            comment_profile_txt.setText(datalist.get(position).get("name"));
            comment_txt.setText( datalist.get( position).get( "comments" ) );
            try {
                Glide.with(activity)
                        .load(Uri.parse(StoredUrls.Uploadedimages +datalist.get(position).get("customer_picture"))) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder(R.drawable.man)
                        .into(commnt_profile_circular_img);
            } catch (Exception e) {
            }

/*
            commnt_profile_circular_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (datalist.get(position).get("customer_id").equalsIgnoreCase(StoredObjects.UserId)) {
                        fragmentcalling(new Profile());
                    } else {
                        fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "customer_id"));
                    }
                }
            });
*/
           /* if (datalist.get( position ).get( "customer_id" ).equalsIgnoreCase(StoredObjects.UserId)){
                comments_listitems_lay.setOnLongClickListener( new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        CommentEditDeleteConfirmation( activity,datalist,position );
                        return false;
                    }
                } );
            }
            else {
                comments_listitems_lay.setOnLongClickListener( new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Toast.makeText( activity, "You are not suposed to edit/delete", Toast.LENGTH_SHORT ).show();
                        return false;
                    }
                } );
            }*/
        }

        if (formtype.equalsIgnoreCase("comments")) {
           // Collections.reverse(datalist);
            comment_profile_txt.setText(datalist.get(position).get("name"));
            comment_txt.setText( datalist.get( position).get( "comments" ) );
            try {
                Glide.with(activity)
                        .load(Uri.parse(StoredUrls.Uploadedimages +datalist.get(position).get("customer_picture"))) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder(R.drawable.man)
                        .into(commnt_profile_circular_img);
            } catch (Exception e) {
            }

            commnt_profile_circular_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (datalist.get(position).get("customer_id").equalsIgnoreCase(StoredObjects.UserId)) {
                        fragmentcalling(new Profile());
                    } else {
                        fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "customer_id"));
                    }
                }
            });
            if (datalist.get( position ).get( "customer_id" ).equalsIgnoreCase(StoredObjects.UserId)){
                comments_listitems_lay.setOnLongClickListener( new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        CommentEditDeleteConfirmation( activity,datalist,position );
                        return false;
                    }
                } );
            }
            else {
                comments_listitems_lay.setOnLongClickListener( new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Toast.makeText( activity, "You are not suposed to edit/delete", Toast.LENGTH_SHORT ).show();
                        return false;
                    }
                } );
            }
        }
        if (formtype.equalsIgnoreCase("Searchpage")) {
            if (datalist.get( position ).get( "customer_id" ).equalsIgnoreCase( StoredObjects.UserId )){
                follow_btn.setVisibility( View.GONE );
            }
            else {
                follow_btn.setVisibility( View.VISIBLE );
            }
            profile_name_txt.setText( datalist.get( position).get( "name" ) );
            profile_email_txt.setText( datalist.get( position).get( "email" ) );
            try{
                Glide.with(activity )
                        .load(Uri.parse(StoredUrls.Uploadedimages +datalist.get(position).get("image"))) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.man )
                       // .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.crossFade()
                        .into(profile_circular_img);
            }catch (Exception e){
            }

            profile_circular_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (datalist.get(position).get("customer_id").equalsIgnoreCase(StoredObjects.UserId)) {
                        fragmentcalling(new Profile());
                    } else {

                        fragmentcalling2(new Followers_profile(), datalist.get( position ).get( "customer_id" ));//shared_by_customer_id
                    }
                }
            });

            if( datalist.get(position).get("is_following").equalsIgnoreCase("Yes")){
                follow_btn.setText(R.string.unfollow);
            }else{
                follow_btn.setText(R.string.follow);
            }


            follow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if( datalist.get(position).get("is_following").equalsIgnoreCase("Yes")){
                        follow_btn.setText(R.string.following);

                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            new UnFollowTask().execute(datalist.get(position).get("customer_id"));
                        } else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet), activity);
                        }
                        updateqtydata(datalist,datalist.get(position),position,"No","followers_profile_search");

                    }else{

                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            new FollowTask().execute(StoredObjects.UserId, datalist.get(position).get("customer_id"));
                        } else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet), activity);
                        }
                        updateqtydata(datalist,datalist.get(position),position,"Yes","followers_profile_search");
                    }
                }
            });


            profile_listitems_lay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   /* if (datalist.get( position ).get( "customer_id" ).equalsIgnoreCase( StoredObjects.UserId ))
                    {
                        fragmentcalling( new Profile() );
                    }
                    else {

                        fragmentcalling1( new Followers_profile(), datalist, position );
                    }*/

                }
            } );

        }
        if (formtype.equalsIgnoreCase("likes")) {
            if (datalist.get( position ).get( "customer_id" ).equalsIgnoreCase( StoredObjects.UserId )){
                follow_btn.setVisibility( View.GONE );
            }
            else {
                follow_btn.setVisibility( View.VISIBLE );
            }
            profile_name_txt.setText( datalist.get( position).get( "name" ) );
            profile_email_txt.setText( datalist.get( position).get( "email" ) );
            try{
                Glide.with(activity )
                        .load(Uri.parse(StoredUrls.Uploadedimages +datalist.get(position).get("customer_picture"))) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.man )
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.crossFade()
                        .into(profile_circular_img);
            }catch (Exception e){
            }

            if( datalist.get(position).get("is_following").equalsIgnoreCase("Yes")){
                follow_btn.setText(R.string.unfollow);
            }else{
                follow_btn.setText(R.string.follow);
            }

            follow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if( datalist.get(position).get("is_following").equalsIgnoreCase("Yes")){
                        follow_btn.setText(R.string.following);

                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            new UnFollowTask().execute(datalist.get(position).get("customer_id"));
                        } else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet), activity);
                        }
                        updateqtydata(datalist,datalist.get(position),position,"No","likes");

                    }else{

                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            new FollowTask().execute(StoredObjects.UserId, datalist.get(position).get("customer_id"));
                        } else {
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet), activity);
                        }
                        updateqtydata(datalist,datalist.get(position),position,"Yes","likes");
                    }
                }
            });


            profile_listitems_lay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   /* if (datalist.get( position ).get( "customer_id" ).equalsIgnoreCase( StoredObjects.UserId ))
                    {
                        fragmentcalling( new Profile() );
                    }
                    else {

                        fragmentcalling1( new Followers_profile(), datalist, position );
                    }*/

                }
            } );

        }

    }

    private void fragmentcalling(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }

    public void fragmentcalling1(Fragment fragment, ArrayList<HashMap<String, String>> list, int position) {
        FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable("YourHashMap", list);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }

    public void fragmentcalling2(Fragment fragment, String customer_id) {

        FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        //  bundle.putParcelableArrayList("mylist", (ArrayList<? extends Parcelable>) dumpData);
        bundle.putString("customer_id", customer_id);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();

    }

    private void fishbreeds_details_popup(final Activity activity, final ArrayList<HashMap<String, String>> data_array, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fishbreeddetails_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        CustomButton fishbreed_additiondetails_btn = (CustomButton) dialog.findViewById(R.id.fishbreed_additiondetails_btn);
        ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
        ImageView fishbreed_img = (ImageView) dialog.findViewById(R.id.fishbreed_img);
        RecyclerView multiple_images_recycle = (RecyclerView)dialog.findViewById( R.id.multiple_images_recycle);
        customRecyclerview = new CustomRecyclerview(activity);
        final LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(activity);
        multiple_images_recycle.setLayoutManager(linearLayoutManager1);


        CustomRegularTextView lakelocation_txt,season_month_txt,water_txt,
                weight_txt,length_txt,habitat_txt,measure_count_txt,record_count_txt;

        CustomBoldTextView fishname_txt =  dialog.findViewById(R.id.fishname_txt);
        lakelocation_txt =  dialog.findViewById(R.id.lakelocation_txt);
        season_month_txt =  dialog.findViewById(R.id.season_month_txt);
        water_txt =  dialog.findViewById(R.id.water_txt);
        weight_txt =  dialog.findViewById(R.id.weight_txt);
        length_txt =  dialog.findViewById(R.id.length_txt);
        habitat_txt =  dialog.findViewById(R.id.habitat_txt);
      //  measure_count_txt =  dialog.findViewById(R.id.measure_count_txt);
        record_count_txt =  dialog.findViewById(R.id.record_count_txt);


        fishname_txt.setText(data_array.get( position ).get( "species" ));
       // lakelocation_txt.setText(data_array.get( position ).get( "appearance" ));

        lakelocation_txt.setText(data_array.get( position ).get( "binomial_name" ));

        season_month_txt.setText(data_array.get( position ).get( "season" ));
        water_txt.setText(data_array.get( position ).get( "water" ));
        weight_txt.setText(data_array.get( position ).get( "weight" ));
        length_txt.setText(data_array.get( position ).get( "length" ));
        habitat_txt.setText(data_array.get( position ).get( "habitat" ));
       // measure_count_txt.setText(data_array.get( position ).get( "season" ));
        record_count_txt.setText(data_array.get( position ).get( "uk_record" ));



        try {
            ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
            images_array.clear();
            try{
                images_array = JsonParsing.GetJsonData(data_array.get( position ).get( "images" ));
            }catch (Exception e){
                images_array = JsonParsing.GetJsonData(data_array.get( position ).get( "fish_breed_images" ));
            }



           /* ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
            images_array.clear();
            for (int i = 0; i < images_arrayall.size(); i++) {
                HashMap<String, String> dash_hash = new HashMap<String, String>();
                dash_hash.put("image",StoredUrls.Uploadedimages + images_arrayall.get(i).get( "image" ) );
                images_array.add(dash_hash);
            }*/





        //StoredUrls.Uploadedimages +
            if(images_array.size()>0){
                Glide.with( activity )
                        .load( Uri.parse(StoredUrls.Uploadedimages + images_array.get(0).get( "image" ) ) ) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.splash_logo_new )
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.crossFade()
                        //.override(600,600)
                        .into( fishbreed_img );
            }else{

            }
            if (images_array.size()>0){
                // postimage.setVisibility( View.VISIBLE );
                multiple_images_recycle.setVisibility( View.VISIBLE );

                multiple_images_recycle.setLayoutManager(new LinearLayoutManager(activity,StoredObjects.horizontal_orientation,false));
                PhotoAdapter hashMapRecycleviewadapter123 = new PhotoAdapter(activity,images_array,"fishingspots_multipleimages",multiple_images_recycle,R.layout.multipleimageslistitem_fishingspots);//
                multiple_images_recycle.setAdapter(hashMapRecycleviewadapter123);

               // customRecyclerview.Assigndatatorecyleviewhashmap(multiple_images_recycle,images_array,"fishingspots_multipleimages_", StoredObjects.Listview, 0, StoredObjects.horizontal_orientation, R.layout.multipleimageslistitem_fishingspots);
            }
            else {
                multiple_images_recycle.setVisibility( View.GONE );

            }
            //  adapter.notifyItemInserted(position);
        } catch (Exception e) {

        }




        fishbreed_additiondetails_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fishbreeds_additionaldetails_popup(activity,data_array,position);
                dialog.dismiss();
            }
        });




       /* fishbreed_additiondetails_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fishbreeds_additionaldetails_popup(activity);
                dialog.dismiss();
            }
        });*/

        canclimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /*private void fishbreeds_additionaldetails_popup(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fishbreed_additiondetails_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
        canclimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }*/



    private void fishbreeds_additionaldetails_popup(final Activity activity,ArrayList<HashMap<String, String>> data_array,int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fishbreed_additiondetails_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
        CustomRegularTextView fishname_txt =(CustomRegularTextView)dialog.findViewById( R.id.fishname_txt );
        CustomRegularTextView fish_details_txt =(CustomRegularTextView)dialog.findViewById( R.id.fish_details_txt );
        CustomRegularTextView fishing_methods =(CustomRegularTextView)dialog.findViewById( R.id.fishing_methods );


        fishname_txt.setText(data_array.get( position ).get( "species" ));
        fish_details_txt.setText(data_array.get( position ).get( "extra_info" ));
        fishing_methods.setText(data_array.get( position ).get( "fishing_methods" ));

        canclimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void liked_details_popup(final Activity activity,ArrayList<HashMap<String, String>> getposdetailslist) {
        final Dialog dialog = new Dialog(activity);
        CustomRecyclerview customRecyclerview;
        customRecyclerview = new CustomRecyclerview(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.liked_details_popup);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations( R.style.SpinKitView_Small_FadingCircle );
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

         ArrayList<HashMap<String, String>> getlikeslist = new ArrayList<>();
         ArrayList<HashMap<String, String>> getcommentslist = new ArrayList<>();


        getlikeslist.clear();
        getcommentslist.clear();
        ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
        RecyclerView liked_recycle;
        liked_recycle =(RecyclerView) dialog.findViewById( R.id.liked_recycle );

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity);
        liked_recycle.setLayoutManager(linearLayoutManager);
        String likes = getposdetailslist.get( 0 ).get( "likes" );
        try {
            getlikeslist = JsonParsing.GetJsonData( likes );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (getlikeslist.size() > 0) {
            liked_recycle.setVisibility( View.VISIBLE );
            adapter = new HashMapRecycleviewadapter(activity,getlikeslist,"likes",liked_recycle,R.layout.search_listitems );
            liked_recycle.setAdapter(adapter);
            //customRecyclerview.Assigndatatorecyleviewhashmap( comments_recycle, getcommentslist, "comments", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.comments_listitems );
        } else {
            liked_recycle.setVisibility( View.GONE );
        }
        StoredObjects.LogMethod( "<><><","<><><>"+getlikeslist );


        canclimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void liked_details_popupnew(final Activity activity,ArrayList<HashMap<String, String>> getposdetailslist) {
        final Dialog dialog = new Dialog(activity);
        CustomRecyclerview customRecyclerview;
        customRecyclerview = new CustomRecyclerview(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.liked_details_popup);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations( R.style.SpinKitView_Small_FadingCircle );
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ArrayList<HashMap<String, String>> getlikeslist = new ArrayList<>();
        ArrayList<HashMap<String, String>> getcommentslist = new ArrayList<>();


        getlikeslist.clear();
        getcommentslist.clear();
        ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
        RecyclerView liked_recycle;
        liked_recycle =(RecyclerView) dialog.findViewById( R.id.liked_recycle );

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity);
        liked_recycle.setLayoutManager(linearLayoutManager);
        String likes = getposdetailslist.get( 0 ).get( "comments" );
        try {
            getlikeslist = JsonParsing.GetJsonData( likes );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (getlikeslist.size() > 0) {
            liked_recycle.setVisibility( View.VISIBLE );
            adapter = new HashMapRecycleviewadapter(activity,getlikeslist,"comments_popup",liked_recycle,R.layout.comments_listitems );
            liked_recycle.setAdapter(adapter);
            //customRecyclerview.Assigndatatorecyleviewhashmap( comments_recycle, getcommentslist, "comments", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.comments_listitems );
        } else {
            liked_recycle.setVisibility( View.GONE );
        }
        StoredObjects.LogMethod( "<><><","<><><>"+getlikeslist );


        canclimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void comments_details_popup(final Activity activity,ArrayList<HashMap<String, String>> getposdetailslist) {
        final Dialog dialog = new Dialog(activity);
        CustomRecyclerview customRecyclerview;
        customRecyclerview = new CustomRecyclerview(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.liked_details_popup);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations( R.style.SpinKitView_Small_FadingCircle );
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //getlikeslist.clear();
        //getcommentslist.clear();
        ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
        RecyclerView liked_recycle;
        liked_recycle =(RecyclerView) dialog.findViewById( R.id.liked_recycle );

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity);
        liked_recycle.setLayoutManager(linearLayoutManager);

         ArrayList<HashMap<String, String>> getlikeslist = new ArrayList<>();
         ArrayList<HashMap<String, String>> getcommentslist = new ArrayList<>();



        try {
            String comments = getposdetailslist.get( 0 ).get( "comments" );
            getcommentslist = JsonParsing.GetJsonData( comments );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.reverse(getcommentslist);

        if (getcommentslist.size() > 0) {
            liked_recycle.setVisibility( View.VISIBLE );
            comments_count_txt.setText( getcommentslist.size()+"");
            adapter = new HashMapRecycleviewadapter(activity,getcommentslist,"comments_popup",liked_recycle,R.layout.comments_listitems );
            liked_recycle.setAdapter(adapter);
            //customRecyclerview.Assigndatatorecyleviewhashmap( comments_recycle, getcommentslist, "comments", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.comments_listitems );
        } else {
            liked_recycle.setVisibility( View.GONE );


        }

        canclimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    public class FollowTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token", "Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method", StoredUrls.follow_profile));
                nameValuePairs.add(new BasicNameValuePair("followed_by", params[0]));
                nameValuePairs.add(new BasicNameValuePair("followed_to", params[1]));
                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    StoredObjects.ToastMethod("Following successfully", activity);
                   // follow_btn.setText(R.string.following);

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, activity);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }


    public class UnFollowTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token", "Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method", StoredUrls.unfollow_user));
                nameValuePairs.add(new BasicNameValuePair("customer_id", StoredObjects.UserId));
                nameValuePairs.add(new BasicNameValuePair("following_customer_id", params[0]));
                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    StoredObjects.ToastMethod("successfully Unfollowed", activity);

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, activity);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }


    public class LikepostTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("method", "like-post"));
                nameValuePairs.add(new BasicNameValuePair("customer_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("post_id", params[1]));
                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {


                   // StoredObjects.ToastMethod("liked", activity);

                } else {
                    String error = jsonObject.getString("error");
                   // StoredObjects.ToastMethod(error, activity);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }

    public class UnLikepostTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("method", StoredUrls.unlike_post));
                nameValuePairs.add(new BasicNameValuePair("customer_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("post_id", params[1]));
                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    // StoredObjects.ToastMethod("liked", activity);

                } else {
                    String error = jsonObject.getString("error");
                    // StoredObjects.ToastMethod(error, activity);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }

    public static void updateqtydata(ArrayList<HashMap<String, String>> list, HashMap<String, String> HashMapdata, int postion, String val, String type) {

        int i = list.indexOf(HashMapdata);

        if (i == -1) {
            throw new IndexOutOfBoundsException();
        }
        /*
    "is_unliked":"No",
    "is_shared":"Yes",
    "comments":{
    },

    "unlikes":{
    },
    "shared_users":[]*/

        if (type.equalsIgnoreCase("landingpage")) {
            HashMap<String, String> dumpData_update = new HashMap<String, String>();
            dumpData_update.put("post_id", HashMapdata.get("post_id"));
            dumpData_update.put("customer_id", HashMapdata.get("customer_id"));
            dumpData_update.put("posted_by", HashMapdata.get("posted_by"));
            dumpData_update.put("profile_picture", HashMapdata.get("profile_picture"));
            dumpData_update.put("title", HashMapdata.get("title"));
            dumpData_update.put("description", HashMapdata.get("description"));
            dumpData_update.put("type", HashMapdata.get("type"));
            dumpData_update.put("files", HashMapdata.get("files"));
            dumpData_update.put("filename", HashMapdata.get("filename"));
            dumpData_update.put("is_commented", HashMapdata.get("is_commented"));
            dumpData_update.put("comments_count", HashMapdata.get("comments_count"));
            dumpData_update.put("likes", HashMapdata.get("likes"));
            dumpData_update.put("post_type", HashMapdata.get("post_type"));
            dumpData_update.put("shared_by_customer_id", HashMapdata.get("shared_by_customer_id"));
            dumpData_update.put("shared_text", HashMapdata.get("shared_text"));
            dumpData_update.put("created_at", HashMapdata.get("created_at"));
            dumpData_update.put("original_post_id", HashMapdata.get("original_post_id"));
            dumpData_update.put("shared_customer", HashMapdata.get("shared_customer"));
            dumpData_update.put("shared_customer_profile_picture", HashMapdata.get("shared_customer_profile_picture"));
            dumpData_update.put("files_count", HashMapdata.get("files_count"));
           // dumpData_update.put("likes_count", HashMapdata.get("likes_count"));
            dumpData_update.put("unlikes_count", HashMapdata.get("unlikes_count"));
            dumpData_update.put("shared_users_count", HashMapdata.get("shared_users_count"));
            dumpData_update.put("is_unliked", HashMapdata.get("is_unliked"));
            dumpData_update.put("is_shared", HashMapdata.get("is_shared"));
            dumpData_update.put("comments", HashMapdata.get("comments"));
            dumpData_update.put("unlikes", HashMapdata.get("unlikes"));
            dumpData_update.put("shared_users", HashMapdata.get("shared_users"));


            dumpData_update.put("is_liked", val);

            try{
                if(val.equalsIgnoreCase("Yes")){
                    int likes = Integer.parseInt(HashMapdata.get("likes_count"));
                    dumpData_update.put("likes_count", (likes+1)+"");
                }else{
                    int likes = Integer.parseInt(HashMapdata.get("likes_count"));
                    dumpData_update.put("likes_count", (likes-1)+"");
                }

            }catch (Exception e){

            }



            list.remove(HashMapdata);
            list.add(postion, dumpData_update);
            //CustomRecyclerview.hashMapRecycleviewadapter.notifyDataSetChanged();
            Landing_page.adapter.notifyDataSetChanged();
        }

        if (type.equalsIgnoreCase("landingpage_hidepost")) {
            list.remove(HashMapdata);
            Landing_page.adapter.notifyDataSetChanged();
            //  CustomRecyclerview.hashMapRecycleviewadapter.notifyDataSetChanged();
        }

        if (type.equalsIgnoreCase("landingpage_delete")) {
            list.remove(HashMapdata);
            Landing_page.adapter.notifyDataSetChanged();
          //  CustomRecyclerview.hashMapRecycleviewadapter.notifyDataSetChanged();
        }
        if (type.equalsIgnoreCase("editcomment")) {
            HashMap<String, String> dumpData_update = new HashMap<String, String>();
            dumpData_update.put("post_id", HashMapdata.get("post_id"));
            dumpData_update.put("customer_id", HashMapdata.get("customer_id"));
            dumpData_update.put("customer_picture", HashMapdata.get("customer_picture"));
            dumpData_update.put("type", HashMapdata.get("type"));
            dumpData_update.put("created_at", HashMapdata.get("created_at"));
            dumpData_update.put("id", HashMapdata.get("id"));
            dumpData_update.put("name", HashMapdata.get("name"));
            dumpData_update.put("comments", val);

            list.remove(HashMapdata);
            list.add(postion, dumpData_update);
            Comments.adapter.notifyDataSetChanged();
        }

        if (type.equalsIgnoreCase("comment_delete")) {


            try{
                Comments.comments_count_txt.setText( list.size()-1+"" );
                list.remove(HashMapdata);
            }
            catch (Exception e){

            }

            Comments.adapter.notifyDataSetChanged();
        }



        if (type.equalsIgnoreCase("following_profile")) {
            list.remove(HashMapdata);
            Following.adapter.notifyDataSetChanged();
            //  CustomRecyclerview.hashMapRecycleviewadapter.notifyDataSetChanged();
        }



        if (type.equalsIgnoreCase("followers_profile")) {
            HashMap<String, String> dumpData_update = new HashMap<String, String>();
            dumpData_update.put("customer_id", HashMapdata.get("customer_id"));
            dumpData_update.put("name", HashMapdata.get("name"));
            dumpData_update.put("email", HashMapdata.get("email"));
            dumpData_update.put("phone", HashMapdata.get("phone"));
            dumpData_update.put("image", HashMapdata.get("image"));
            dumpData_update.put("catches_count", HashMapdata.get("catches_count"));
            dumpData_update.put("catches", HashMapdata.get("catches"));
            dumpData_update.put("is_following", HashMapdata.get("is_following"));
            dumpData_update.put("is_follower", HashMapdata.get("is_follower"));
            dumpData_update.put("followers_count", HashMapdata.get("followers_count"));
            dumpData_update.put("following_count", HashMapdata.get("following_count"));
            dumpData_update.put("is_following", val);

           /* try{
                if(val.equalsIgnoreCase("Yes")){
                    dumpData_update.put("is_following", "No");
                }else{
                    dumpData_update.put("is_following", "Yes");
                }

            }catch (Exception e){

            }*/
            StoredObjects.LogMethod( "response", "response_follwing:---" + dumpData_update.get("is_following"));


            list.remove(HashMapdata);
            list.add(postion, dumpData_update);
            //CustomRecyclerview.hashMapRecycleviewadapter.notifyDataSetChanged();
            Followers.adapter.notifyDataSetChanged();
        }



        if (type.equalsIgnoreCase("followers_profile_search")) {
            HashMap<String, String> dumpData_update = new HashMap<String, String>();
            dumpData_update.put("customer_id", HashMapdata.get("customer_id"));
            dumpData_update.put("name", HashMapdata.get("name"));
            dumpData_update.put("email", HashMapdata.get("email"));
            dumpData_update.put("phone", HashMapdata.get("phone"));
            dumpData_update.put("image", HashMapdata.get("image"));
            dumpData_update.put("catches_count", HashMapdata.get("catches_count"));
            dumpData_update.put("catches", HashMapdata.get("catches"));
            dumpData_update.put("is_following", HashMapdata.get("is_following"));
            dumpData_update.put("is_follower", HashMapdata.get("is_follower"));
            dumpData_update.put("followers_count", HashMapdata.get("followers_count"));
            dumpData_update.put("following_count", HashMapdata.get("following_count"));
            dumpData_update.put("is_following", val);

            list.remove(HashMapdata);
            list.add(postion, dumpData_update);
            //CustomRecyclerview.hashMapRecycleviewadapter.notifyDataSetChanged();
            Searchpage.adapter.notifyDataSetChanged();
        }

        if (type.equalsIgnoreCase("likes")) {
            HashMap<String, String> dumpData_update = new HashMap<String, String>();
            dumpData_update.put("customer_id", HashMapdata.get("customer_id"));
            dumpData_update.put("name", HashMapdata.get("name"));
            dumpData_update.put("email", HashMapdata.get("email"));
            dumpData_update.put("phone", HashMapdata.get("phone"));
            dumpData_update.put("image", HashMapdata.get("image"));
            dumpData_update.put("catches_count", HashMapdata.get("catches_count"));
            dumpData_update.put("catches", HashMapdata.get("catches"));
            dumpData_update.put("is_following", HashMapdata.get("is_following"));
            dumpData_update.put("is_follower", HashMapdata.get("is_follower"));
            dumpData_update.put("followers_count", HashMapdata.get("followers_count"));
            dumpData_update.put("following_count", HashMapdata.get("following_count"));
            dumpData_update.put("is_following", val);

            list.remove(HashMapdata);
            list.add(postion, dumpData_update);
           // CustomRecyclerview.hashMapRecycleviewadapter.notifyDataSetChanged();
           // Searchpage.adapter.notifyDataSetChanged();
        }


        if (type.equalsIgnoreCase("notification_delete")) {


            try{
               // Comments.comments_count_txt.setText( list.size()-1+"" );
                list.remove(HashMapdata);
            }
            catch (Exception e){

            }

            Notifications.adapter.notifyDataSetChanged();
        }

        if (type.equalsIgnoreCase("checkboxchecked")) {

            //for (int j = 0; j < list.size(); j++) {
                HashMap<String, String> dumpData_update = new HashMap<String, String>();
                dumpData_update.put("notification_id", HashMapdata.get("notification_id"));
                dumpData_update.put("title", HashMapdata.get("title"));
                dumpData_update.put("description", HashMapdata.get("description"));
                dumpData_update.put("status", HashMapdata.get("status"));
                dumpData_update.put("created_at",HashMapdata.get("created_at"));
                dumpData_update.put("notification_type", HashMapdata.get("notification_type"));
                dumpData_update.put("post_id", HashMapdata.get("post_id"));
                dumpData_update.put("is_following", HashMapdata.get("is_following"));
                dumpData_update.put("comment_id", HashMapdata.get("comment_id"));
                dumpData_update.put("created_customer_id", HashMapdata.get("created_customer_id"));
                dumpData_update.put("comments", HashMapdata.get("comments"));
                dumpData_update.put("profile_picture", HashMapdata.get("profile_picture"));
                dumpData_update.put("customer_name", HashMapdata.get("customer_name"));
                dumpData_update.put("addedtocart",val);
                dumpData_update.put("pro_quantity","0");
                dumpData_update.put("status_val","No");
                dumpData_update.put("sel_favorites",HashMapdata.get("sel_favorites"));//val
                list.remove(HashMapdata);
                list.add(postion, dumpData_update);
           // }

            Notifications.adapter.notifyDataSetChanged();
        }




    }


    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "Less", false);
                    } else {
                        makeTextViewResizable(tv, 4, "More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }





    private void Sharepostpopup1234(final Activity activity, final String message, final String post_id) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logooutpopup );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
        Button ok_btn = (Button)dialog.findViewById(R.id.ok_btn);
        Button cancel_btn = (Button)dialog.findViewById(R.id.cancel_btn);
        ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
        CustomRegularTextView logout_txt = (CustomRegularTextView)dialog.findViewById( R.id.logout_txt );
        CustomRegularTextView exit_txt = (CustomRegularTextView)dialog.findViewById( R.id.exit_txt );


        logout_txt.setText(message);




        cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        canclimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (InterNetChecker.isNetworkAvailable(activity)) {
                    new SharePostTask().execute(post_id);
                }else{
                    StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                }

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void EditDeleteConfirmation(final Activity activity, final ArrayList<HashMap<String, String>> list, final int position,String posttype) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editdelete_confirmation );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);

        LinearLayout edit_layout = (LinearLayout)dialog.findViewById(R.id.edit_layout);
        LinearLayout delet_layout = (LinearLayout)dialog.findViewById(R.id.delet_layout);
        LinearLayout blockuser_layout = (LinearLayout)dialog.findViewById(R.id.blockuser_layout);
        LinearLayout hidepost_layout = (LinearLayout)dialog.findViewById(R.id.hidepost_layout);


        if (posttype.equalsIgnoreCase( "sharedpost" )) {
            if (list.get( position ).get( "shared_by_customer_id" ).equalsIgnoreCase( StoredObjects.UserId )) {
                edit_layout.setVisibility( View.VISIBLE );
                delet_layout.setVisibility( View.VISIBLE );
                blockuser_layout.setVisibility( View.GONE );
                hidepost_layout.setVisibility( View.GONE );

            } else {
                edit_layout.setVisibility( View.GONE );
                delet_layout.setVisibility( View.GONE );
                blockuser_layout.setVisibility( View.VISIBLE );
                hidepost_layout.setVisibility( View.VISIBLE );
            }
        }
            if (posttype.equalsIgnoreCase( "sharedpost_own" )) {
                if(list.get( position ).get( "shared_by_customer_id" ).equalsIgnoreCase(StoredObjects.UserId)){
                    edit_layout.setVisibility(View.VISIBLE);
                    delet_layout.setVisibility(View.VISIBLE);
                    blockuser_layout.setVisibility(View.GONE);
                    hidepost_layout.setVisibility(View.GONE);

                }else{
                    edit_layout.setVisibility(View.GONE);
                    delet_layout.setVisibility(View.GONE);
                    blockuser_layout.setVisibility(View.VISIBLE);
                    hidepost_layout.setVisibility(View.VISIBLE);
                }
        }else {
            if(list.get( position ).get( "customer_id" ).equalsIgnoreCase(StoredObjects.UserId)){
                edit_layout.setVisibility(View.VISIBLE);
                delet_layout.setVisibility(View.VISIBLE);
                blockuser_layout.setVisibility(View.GONE);
                hidepost_layout.setVisibility(View.GONE);
            }else{
                edit_layout.setVisibility(View.GONE);
                delet_layout.setVisibility(View.GONE);
                blockuser_layout.setVisibility(View.VISIBLE);
                hidepost_layout.setVisibility(View.VISIBLE);
            }

        }

        hidepost_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                if (InterNetChecker.isNetworkAvailable(activity)) {
                    new HidepostTask().execute(StoredObjects.UserId,list.get( position ).get("post_id"));
                    updateqtydata(list,list.get( position ),position,"No","landingpage_hidepost");

                }else{
                    StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                }
            }
        });


        blockuser_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                if (InterNetChecker.isNetworkAvailable(activity)) {
                    new BlockUserTask().execute(list.get( position ).get( "customer_id" ));
                }else{
                    StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                }
            }
        });

        if (posttype.equalsIgnoreCase( "sharedpost_own" )){
            edit_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*fragmentcalling1(new Addpost(),list,position);*/
                    Sharepostpopup( activity,list,position,list.get( position ).get( "original_post_id" ),"sharedpost");
                    dialog.dismiss();
                }
            });
        }else{
            edit_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    StoredObjects.Selected_fishspotname = "";
                    StoredObjects.Selected_fishspot_id = "";
                    StoredObjects.Selected_fishspot_lat="0";
                    StoredObjects.Selected_fishspot_lng="0";
                    StoredObjects.Selected_fishspot_address="";

                    fragmentcalling1(new Addpost(),list,position);
                    dialog.dismiss();
                }
            });
        }



        delet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                if (InterNetChecker.isNetworkAvailable(activity)) {
                    new DeletePostTask().execute(list.get( position ).get( "post_id" ));
                    updateqtydata(list,list.get( position ),position,"No","landingpage_delete");
                }else{
                    StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                }

            }
        });



        cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });



        dialog.show();
    }

    public class SharePostTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token", "Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method", "share-post"));
                nameValuePairs.add(new BasicNameValuePair("customer_id",StoredObjects.UserId));
                nameValuePairs.add(new BasicNameValuePair("post_id",params[0]));
                nameValuePairs.add(new BasicNameValuePair("shared_text",params[1]));


                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    StoredObjects.ToastMethod("Successfully Post Shared.", activity);

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, activity);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }



    public class DeletePostTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token", "Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method", StoredUrls.delete_post));
                nameValuePairs.add(new BasicNameValuePair("customer_id", StoredObjects.UserId));
                nameValuePairs.add(new BasicNameValuePair("post_id", params[0]));

                String pairsstrig = "";
                for (int i = 0; i <nameValuePairs.size() ; i++) {
                    pairsstrig += nameValuePairs.get(i)+"&";
                }
                Log.i("HttpTAG:1", "namevaluepairs:--"+StoredUrls.BaseUrl+pairsstrig);


                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    StoredObjects.ToastMethod("Successfully Deleted.", activity);

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, activity);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }

    public class HidepostTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token", "Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method", StoredUrls.hidepost));
                nameValuePairs.add(new BasicNameValuePair("customer_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("post_id", params[1]));
                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    StoredObjects.ToastMethod("Successfully Post hidden.", activity);

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, activity);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }


    public class BlockUserTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token", "Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method", StoredUrls.block_user));
                nameValuePairs.add(new BasicNameValuePair("customer_id", StoredObjects.UserId));
                nameValuePairs.add(new BasicNameValuePair("following_customer_id", params[0]));
                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    StoredObjects.ToastMethod("Successfully Blocked.", activity);

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, activity);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }

    private void CommentEditDeleteConfirmation(final Activity activity, final ArrayList<HashMap<String, String>> list, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editdelete_confirmation );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);

        LinearLayout edit_layout = (LinearLayout)dialog.findViewById(R.id.edit_layout);
        LinearLayout delet_layout = (LinearLayout)dialog.findViewById(R.id.delet_layout);
        LinearLayout blockuser_layout = (LinearLayout)dialog.findViewById(R.id.blockuser_layout);
        LinearLayout hidepost_layout = (LinearLayout)dialog.findViewById(R.id.hidepost_layout);
        blockuser_layout.setVisibility(View.GONE);

        if(list.get( position ).get( "customer_id" ).equalsIgnoreCase(StoredObjects.UserId)){
            edit_layout.setVisibility(View.VISIBLE);
            delet_layout.setVisibility(View.VISIBLE);
            hidepost_layout.setVisibility(View.GONE);

        }


        edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentEditpopup( activity,list,position );
                dialog.dismiss();
            }
        });
        delet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                if (InterNetChecker.isNetworkAvailable(activity)) {
                    new DeleteCommentTask().execute(getposdetailslist.get( 0 ).get( "post_id" ),list.get( position ).get( "id" ));
                   // Collections.reverse(list);
                    updateqtydata(list,list.get( position ),position,"No","comment_delete");
                }else{
                    StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                }

            }
        });

        cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    CustomEditText commnt_edittxt;
    private void CommentEditpopup(final Activity activity, final ArrayList<HashMap<String, String>> list, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editcommentpopup );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);

         commnt_edittxt = (CustomEditText)dialog.findViewById(R.id.commnt_edittxt);
        ImageView cmntsend_btn = (ImageView)dialog.findViewById(R.id.cmntsend_btn);

        if (commnt_edittxt.hasFocus()) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }


        commnt_edittxt.setText(list.get( position ).get( "comments" ) );
        commnt_edittxt.requestFocus();
        dialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        cmntsend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                if (InterNetChecker.isNetworkAvailable(activity)) {
                    new EditCommentTask().execute(getposdetailslist.get( 0 ).get( "post_id" ),list.get( position ).get( "id" ),commnt_edittxt.getText().toString());
                   // Collections.reverse(list);
                    updateqtydata(list,list.get(position),position,commnt_edittxt.getText().toString(),"editcomment");
                }else{
                    StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                }
            }
        });
        cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public class EditCommentTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token", "Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method", "edit-comment-on-post"));
                nameValuePairs.add(new BasicNameValuePair("customer_id", StoredObjects.UserId));
                nameValuePairs.add(new BasicNameValuePair("post_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("comment_id", params[1]));
                nameValuePairs.add(new BasicNameValuePair("comments", params[2]));
                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    StoredObjects.ToastMethod("Successfully Edited.", activity);
                    commnt_edittxt.setText( "" );
                    /*if (InterNetChecker.isNetworkAvailable(activity)) {
                        new Comments.GetPostdetailsTask().execute(StoredObjects.UserId,postid);
                    }else{
                        StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                    }*/

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, activity);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }
    public class DeleteCommentTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token", "Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method", "delete-comment-on-post"));
                nameValuePairs.add(new BasicNameValuePair("customer_id", StoredObjects.UserId));
                nameValuePairs.add(new BasicNameValuePair("post_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("comment_id", params[1]));
                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    StoredObjects.ToastMethod("Successfully Deleted.", activity);
                   /* if (InterNetChecker.isNetworkAvailable(activity)) {
                        new Comments.GetPostdetailsTask().execute(StoredObjects.UserId,postid);
                    }else{
                        StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                    }*/
                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, activity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }

    private void Sharepostpopup(final Activity activity,final ArrayList<HashMap<String, String>> list, final int position, final String post_id,String type) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sharepost_confirmation );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
        ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
        CustomRegularTextView sharenow_text = (CustomRegularTextView)dialog.findViewById( R.id.sharenow_text );
        CircularImageView profile_circular_img = (CircularImageView)dialog.findViewById( R.id.profile_circular_img );
        CustomRegularTextView profile_name_txt = (CustomRegularTextView)dialog.findViewById( R.id.profile_name_txt );
        CustomRegularTextView share_date_time_txt = (CustomRegularTextView)dialog.findViewById( R.id.share_date_time_txt );
        final CustomEditText commnt_edittxt = (CustomEditText)dialog.findViewById( R.id.commnt_edittxt );

        if (type.equalsIgnoreCase( "sharedpost" )){
            commnt_edittxt.setText( list.get( position ).get( "shared_text" ) );
            commnt_edittxt.requestFocus();
            dialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            try {
                profile_name_txt.setText(list.get( position ).get( "shared_customer" ) );
                Glide.with( activity )
                        .load( Uri.parse( StoredUrls.Uploadedimages + list.get( position ).get( "shared_customer_profile_picture" )  ) ) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.man )
                        .into( profile_circular_img );
                sharenow_text.setText( "Edit shared post" );
                sharenow_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            //new SharePostTask().execute(post_id,commnt_edittxt.getText().toString());
                            new EditPostTask().execute(list.get( position ).get("shared_by_customer_id"), list.get( position ).get( "post_id" ), commnt_edittxt.getText().toString());

                        }else{
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                        }

                    }
                });


            } catch (Exception e) {
            }

        }else {
            try {
                commnt_edittxt.requestFocus();
                dialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                profile_name_txt.setText(SideMenu.getprofilelist.get( 0 ).get( "name" ) );
                Glide.with( activity )
                        .load( Uri.parse( StoredUrls.Uploadedimages + SideMenu.getprofilelist.get( 0 ).get( "image" )  ) ) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.man )
                        .into( profile_circular_img );

                sharenow_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (InterNetChecker.isNetworkAvailable(activity)) {
                            new SharePostTask().execute(post_id,commnt_edittxt.getText().toString());
                        }else{
                            StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                        }

                    }
                });

            } catch (Exception e) {
            }

        }

        //share_date_time_txt.setText( list.get( position ).get( "created_at") );



        cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        canclimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        dialog.show();
    }


    public class GetPostdetailsTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow( activity);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token","Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method","get-post-details") );
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0]));
                nameValuePairs.add( new BasicNameValuePair( "post_id", params[1] ) );
                strResult = HttpPostClass.PostMethod( StoredUrls.BaseUrl, nameValuePairs );
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    ArrayList<HashMap<String, String>> getposdetailslist = new ArrayList<>();
                    getposdetailslist = JsonParsing.GetJsonData( results );
                    liked_details_popup( activity,getposdetailslist);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod( "response", "response:---" + e );
            }
        }
    }


    public class GetPostdetailsTaskNew extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow( activity);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token","Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method","get-post-details") );
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0]));
                nameValuePairs.add( new BasicNameValuePair( "post_id", params[1] ) );
                strResult = HttpPostClass.PostMethod( StoredUrls.BaseUrl, nameValuePairs );
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );


                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    ArrayList<HashMap<String, String>> getposdetailslist = new ArrayList<>();
                    getposdetailslist.clear();
                    getposdetailslist = JsonParsing.GetJsonData( results );
                    liked_details_popupnew( activity ,getposdetailslist);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod( "response", "response:---" + e );
            }
        }
    }


    public class GecommentsdetailsTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow( activity);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token","Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method","get-post-details") );
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0]));
                nameValuePairs.add( new BasicNameValuePair( "post_id", params[1] ) );
                strResult = HttpPostClass.PostMethod( StoredUrls.BaseUrl, nameValuePairs );
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                  CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    ArrayList<HashMap<String, String>> getposdetailslist = new ArrayList<>();
                    getposdetailslist.clear();
                    getposdetailslist = JsonParsing.GetJsonData( results );
                    comments_details_popup( activity ,getposdetailslist);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod( "response", "response:---" + e );
            }
        }
    }
    public class EditPostTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow( activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token","Mikel" ));
                nameValuePairs.add( new BasicNameValuePair( "method","edit-shared-post"));
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0]));
                nameValuePairs.add( new BasicNameValuePair( "post_id",params[1] ) );
                nameValuePairs.add( new BasicNameValuePair( "shared_text",params[2]));

                strResult = HttpPostClass.PostMethod( StoredUrls.BaseUrl, nameValuePairs );
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    StoredObjects.ToastMethod("Successfully post Edited",activity);
                    fragmentcalling( new Landing_page() );
                } else {
                    String error = jsonObject.getString( "error" );
                    StoredObjects.ToastMethod(error,activity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod( "response", "response:---" + e );
            }
        }
    }


    private void DeleteNotificationpopup(final Activity activity, final ArrayList<HashMap<String, String>> list, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editdelete_confirmation );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);

        LinearLayout edit_layout = (LinearLayout)dialog.findViewById(R.id.edit_layout);
        LinearLayout delet_layout = (LinearLayout)dialog.findViewById(R.id.delet_layout);
        LinearLayout blockuser_layout = (LinearLayout)dialog.findViewById(R.id.blockuser_layout);
        LinearLayout hidepost_layout = (LinearLayout)dialog.findViewById(R.id.hidepost_layout);
        blockuser_layout.setVisibility(View.GONE);
            edit_layout.setVisibility(View.GONE);
            delet_layout.setVisibility(View.VISIBLE);
            hidepost_layout.setVisibility(View.GONE);


        delet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                if (InterNetChecker.isNetworkAvailable(activity)) {
                    new DeleteNotificationTask().execute(list.get( position ).get( "notification_id" ));
                    // Collections.reverse(list);
                    updateqtydata(list,list.get( position ),position,"No","notification_delete");
                }else{
                    StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                }

            }
        });

        cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public class DeleteNotificationTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token", "Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method", "delete-notification"));
                nameValuePairs.add(new BasicNameValuePair("customer_id", StoredObjects.UserId));
                nameValuePairs.add(new BasicNameValuePair("notification_id", params[0]));
                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    StoredObjects.ToastMethod("Successfully Deleted.", activity);
                   /* if (InterNetChecker.isNetworkAvailable(activity)) {
                        new Comments.GetPostdetailsTask().execute(StoredObjects.UserId,postid);
                    }else{
                        StoredObjects.ToastMethod(activity.getResources().getString(R.string.checkinternet),activity);
                    }*/
                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, activity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }

    public class ChangeNotificationstatusTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token", "Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method", "change-notification-status"));
                nameValuePairs.add(new BasicNameValuePair("customer_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("notification_id", params[1]));
                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel(activity);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    //StoredObjects.ToastMethod("Successfully Post hidden.", activity);

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, activity);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (IllegalStateException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            } catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            } catch (RuntimeException e) {
                // TODO: handle exception
            } catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }


    public void addlayoutfishingspots(LinearLayout layout,final ArrayList<HashMap<String,String>> arrayList, final int position){

        View v = LayoutInflater.from(activity).inflate(R.layout.fishingspots_addlititem, null);

        CustomRegularTextView fishingspots_text = v.findViewById (R.id.fishingspots_text);
        StoredObjects.LogMethod( "response", "images_array_size:---" + arrayList.size()+"<<<><><>"+""+"<<><>"+position );
        fishingspots_text.setText("#"+arrayList.get(position).get("name"));

        fishingspots_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FishingSpotsDetailsActivity.class);
                intent.putExtra("YourHashMap", arrayList);
                intent.putExtra("position", position);
                activity.startActivity(intent);
            }
        });
        layout.addView(v);

    }


    private void AddpostConfitmation(final Activity activity, final String type, final ArrayList<HashMap<String, String>> datalist, final int pos) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logooutpopup );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
        Button ok_btn = (Button)dialog.findViewById(R.id.ok_btn);
        Button cancel_btn = (Button)dialog.findViewById(R.id.cancel_btn);
        ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
        CustomRegularTextView logout_txt = (CustomRegularTextView)dialog.findViewById( R.id.logout_txt );
        CustomRegularTextView exit_txt = (CustomRegularTextView)dialog.findViewById( R.id.exit_txt );


        logout_txt.setVisibility( View.VISIBLE );
        logout_txt.setText(type);
        /*if (type.equals( "1" )){
            logout_txt.setVisibility( View.VISIBLE );
        }
        else {
            exit_txt.setVisibility( View.VISIBLE );
            logout_txt.setVisibility( View.GONE );
        }*/

        cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        canclimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                activity.finish();
                Intent intent = new Intent(activity, AddpostBreedActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",datalist);
                //args.putInt("position", pos);
                intent.putExtra("BUNDLE",args);
                intent.putExtra("position", pos);
                // intent.putStringArrayListExtra("array", fishbreeds_list);
                activity.startActivity(intent);

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
