package com.fishbuddy.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fishbuddy.R;
import com.fishbuddy.circularimageview.CircularImageView;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
import com.fishbuddy.customfonts.CustomBoldTextView;
import com.fishbuddy.customfonts.CustomEditText;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.servicesparsing.CustomProgressbar;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hb.xvideoplayer.MxVideoPlayer;

import static com.fishbuddy.sidemenu.SideMenu.btm_addpost_txt;
import static com.fishbuddy.sidemenu.SideMenu.buttonchangemethod;
import static com.fishbuddy.sidemenu.SideMenu.custm_menu_img;

public class CommentsNew extends Fragment {
    public static RecyclerView comments_recycle;
    public static CustomRecyclerview customRecyclerview;
    TextView title_txt;
    public static LinearLayout comments_lay,comments_clik_lay;
    // public int comments_count = 0;
    String comments_count = "";
    String likes_count = "";
    Bundle bundle;

    int position;
    CircularImageView landing_circular_img;
    CustomBoldTextView profile_txt;
    public static CustomRegularTextView custm_title_txt,like_txt_count, comments_count_txt,share_count_txt,profile_updatedtime_txt,custm_title__link,likes_txt;
    ImageView postimage,likes_img;
    public static ArrayList<HashMap<String, String>> getposdetailslist = new ArrayList<>();

    public static String postid = "", files_count = "";
    CustomEditText commnt_edittxt;
    ImageView cmntsend_btn;
    String comment = "";
    String posttype = "";
    public static LinearLayout like_layout,comments_main_layout;
    public static ShimmerFrameLayout mShimmerViewContainer;
    public static HashMapRecycleviewadapter adapter;
    RecyclerView multiple_images_recycle;
    ArrayList<HashMap<String, String>> getmultipleimageslist = new ArrayList<>();
    private LinearLayout  share__lay, comment__lay, like__lay;
    public static ArrayList<HashMap<String, String>> getlikeslist = new ArrayList<>();
    RecyclerView liked_recycle;
    MxVideoPlayer simpleVideoView;

    CustomRegularTextView  fishingspots_text;

    LinearLayout  fishingspots_lay;

    String type = "";
    @Override
    public void onResume() {
        buttonchangemethod (getActivity() , comments_lay , custm_menu_img , btm_addpost_txt , "5");
        super.onResume();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.comments_test,null,false );
        StoredObjects.page_type="home";
        StoredObjects.back_type="comments";
        SideMenu.updatemenu(StoredObjects.page_type);

        bundle = getArguments();
        try {
            Log.i("hos_id", "hos_id:--"+bundle);
            if (bundle != null) {
                postid = bundle.getString( "customer_id" );
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        initilization(v);
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new GetPostdetailsTask().execute(StoredObjects.UserId,postid);
        }else{
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
        }

        return v;
    }

    private void initilization(View v) {

        mShimmerViewContainer =  v.findViewById(R.id.shimmer_view_container);
        comments_recycle = (RecyclerView) v.findViewById( R.id.comments_recycle );
        comments_lay = (LinearLayout)v.findViewById( R.id.comments_lay );
        comments_clik_lay = (LinearLayout)v.findViewById( R.id.comments_clik_lay );
        customRecyclerview = new CustomRecyclerview(getActivity());
        title_txt = (TextView)v.findViewById( R.id. title_txt);
        /*title_txt.setText( R.string.largemouthbasskg );*/
        title_txt.setText( "Details");
        landing_circular_img = (CircularImageView)v.findViewById( R.id.landing_circular_img );
        profile_txt = (CustomBoldTextView)v.findViewById( R.id.profile_txt );
        custm_title_txt = (CustomRegularTextView)v.findViewById( R.id.custm_title_txt );
        like_txt_count = (CustomRegularTextView)v.findViewById( R.id.like_txt_count );
        comments_count_txt = (CustomRegularTextView)v.findViewById( R.id.comments_count_txt );
        share_count_txt = (CustomRegularTextView)v.findViewById( R.id.share_count_txt );
        postimage = (ImageView)v.findViewById( R.id.postimage );
        cmntsend_btn = (ImageView)v.findViewById( R.id.cmntsend_btn );
        commnt_edittxt = (CustomEditText)v.findViewById( R.id.commnt_edittxt );
        share__lay = v.findViewById(R.id.share__lay);
        comment__lay = v.findViewById(R.id.comment__lay);
        like__lay = v.findViewById(R.id.like__lay);
        likes_img = (ImageView)v.findViewById( R.id.likes_img );
        likes_txt = (CustomRegularTextView)v.findViewById( R.id.likes_txt );
        profile_updatedtime_txt = (CustomRegularTextView) v.findViewById( R.id.profile_updatedtime_txt );
        custm_title__link = (CustomRegularTextView) v.findViewById( R.id.custm_title__link );
        multiple_images_recycle = (RecyclerView)v.findViewById( R.id.multiple_images_recycle );
        like_layout = v.findViewById(R.id.like_layout);
        comments_main_layout = v.findViewById(R.id.comments_main_layout);

        simpleVideoView = v.findViewById( R.id.simpleVideoView );
        comments_main_layout.setVisibility(View.GONE);

        fishingspots_text = v.findViewById(R.id.fishingspots_text);

        fishingspots_lay = v.findViewById(R.id.fishingspots_lay);



        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        comments_recycle.setLayoutManager(linearLayoutManager);



        final LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getActivity());
        multiple_images_recycle.setLayoutManager(linearLayoutManager1);


       /* String commentscount = postslist.get( position ).get( "comments_count" );
        if (commentscount.equals( "0" )){
            comments_clik_lay.setClickable( false );
        }
        else
        {
            comments_clik_lay.setClickable( true );

            comments_clik_lay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        new GecommentsdetailsTask().execute(StoredObjects.UserId,postslist.get( position ).get( "post_id" ));
                    }else {
                        StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                    }
                }
            } );

        }*/
        cmntsend_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = commnt_edittxt.getText().toString();
                if (StoredObjects.inputValidation( commnt_edittxt, getActivity().getResources().getString( R.string.pleasewritecomment ), getActivity() )) {
                    if (InterNetChecker.isNetworkAvailable( getActivity() )) {
                        StoredObjects.hide_keyboard( getActivity() );
                        new sendcommentTask().execute( StoredObjects.UserId, postid, comment );
                    } else {
                        StoredObjects.ToastMethod( getActivity().getResources().getString( R.string.checkinternet ), getActivity() );
                    }

                }
            }
        } );




        ImageView backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();

            }
        } );

    }

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }

    public  void dataassign(final ArrayList<HashMap<String, String>> postslist, final int position ) {









        /*try{
            String likes = getposdetailslist.get( 0 ).get( "likes" );
            getlikeslist = JsonParsing.GetJsonData( likes );
            if (getlikeslist.size() > 0) {
                liked_recycle.setVisibility( View.VISIBLE );
                adapter = new HashMapRecycleviewadapter(getActivity(),getlikeslist,"likes",liked_recycle,R.layout.search_listitems );
                liked_recycle.setAdapter(adapter);
                //customRecyclerview.Assigndatatorecyleviewhashmap( comments_recycle, getcommentslist, "comments", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.comments_listitems );
            } else {
                liked_recycle.setVisibility( View.GONE );
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/





        String likescount = postslist.get( position ).get( "likes_count" );
        if (likescount.equals( "0" )){
            like_layout.setClickable( false );
        }
        else
        {
            like_layout.setClickable( true );

            like_layout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        new likesdetailsTask().execute(StoredObjects.UserId,postslist.get( position ).get( "post_id" ));
                    }else {
                        StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                    }
                }
            } );

        }



        likes_count = postslist.get( position ).get( "is_liked" );

        if (likes_count.equalsIgnoreCase( "No" )) {
            likes_img.setImageResource( R.drawable.like_button );
            likes_txt.setTextColor( getActivity().getResources().getColor( R.color.lite_color_text ) );

        } else {
            likes_img.setImageResource( R.drawable.liked );
            likes_txt.setTextColor( getActivity().getResources().getColor( R.color.orange ) );

        }

        like__lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (likes_count.equalsIgnoreCase( "No" )) {
                    if (InterNetChecker.isNetworkAvailable( getActivity() )) {
                        new LikepostTask().execute( StoredObjects.UserId, postslist.get( position ).get( "post_id" ) );
                        //updateqtydata( datalist, datalist.get( position ), position, "Yes", "landingpage" );


                    } else {
                        StoredObjects.ToastMethod( getActivity().getResources().getString( R.string.checkinternet ), getActivity() );
                    }
                } else {
                    //StoredObjects.ToastMethod("Already liked", activity);

                    if (InterNetChecker.isNetworkAvailable( getActivity() )) {
                        new UnLikepostTask().execute( StoredObjects.UserId, postslist.get( position ).get( "post_id" ) );
                        // updateqtydata( datalist, datalist.get( position ), position, "No", "landingpage" );

                    } else {
                        StoredObjects.ToastMethod( getActivity().getResources().getString( R.string.checkinternet ), getActivity() );
                    }
                }
            }
        } );


        share__lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (posttype.equalsIgnoreCase( "Direct Post" )){
                    Sharepostpopup( getActivity(), postslist, position, postslist.get( position ).get("post_id"),"" );
                }
                else {
                    Sharepostpopup( getActivity(), postslist, position, postslist.get( position ).get("original_post_id"),"" );
                }

            }
        } );


        comment__lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* commnt_edittxt.requestFocus();
                getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);*/

                commnt_edittxt.requestFocus();
                commnt_edittxt.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService( Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(commnt_edittxt, InputMethodManager.SHOW_FORCED);
            }
        } );


        if (type.equalsIgnoreCase( "video" )){
            postimage.setVisibility( View.GONE );
            multiple_images_recycle.setVisibility( View.GONE );
            simpleVideoView.setVisibility( View.VISIBLE );

            try{
                //  Uri uri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4");
                Uri uri = Uri.parse(StoredUrls.Uploadedimages + postslist.get( position ).get( "filename"));
                StoredObjects.LogMethod( "uri","++"+uri );
                // simpleVideoView.startPlay("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4", MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "video name");
                simpleVideoView.startPlay( String.valueOf( uri ), MxVideoPlayer.NORMAL_ORIENTATION, "");
                simpleVideoView.mFullscreenButton.setVisibility( View.GONE );

            }catch (Exception e){
            }


        } else if
        ((type.equalsIgnoreCase( "picture" ))&(getmultipleimageslist.size()==1)){
            postimage.setVisibility( View.VISIBLE );
            multiple_images_recycle.setVisibility( View.GONE );
            simpleVideoView.setVisibility( View.GONE );
        }

        else {
           if(getmultipleimageslist.size()>0){
               postimage.setVisibility( View.GONE );
               simpleVideoView.setVisibility( View.GONE );
               multiple_images_recycle.setVisibility( View.VISIBLE );
               adapter = new HashMapRecycleviewadapter(getActivity(),getmultipleimageslist,"multipleimages",multiple_images_recycle,R.layout.multipleimageslistitem );
               multiple_images_recycle.setAdapter(adapter);

           }
        }

        try{
            Glide.with(getActivity() )
                    .load( Uri.parse( StoredUrls.Uploadedimages +postslist.get(position).get("filename"))) // add your image url
                    .centerCrop() // scale to fill the ImageView and crop any extra
                    .fitCenter() // scale to fit entire image within ImageView
                    .placeholder(R.drawable.splash_logo_new)
                    .into(postimage);
        }catch (Exception e){
        }


        if (posttype.equalsIgnoreCase( "Shared Post" )){
            custm_title__link .setText( postslist.get( position ).get( "title" ) );
            custm_title_txt.setText( postslist.get( position ).get( "description"));

        }else{
            custm_title__link.setText(postslist.get(position).get("title"));
            custm_title_txt.setText(postslist.get(position).get("description"));
        }

        profile_txt.setText( postslist.get( position ).get( "posted_by" ) );

        like_txt_count.setText( postslist.get( position ).get( "likes_count" ) );
        comments_count_txt.setText( postslist.get( position ).get( "comments_count" ) );
        share_count_txt.setText( postslist.get( position ).get( "shared_users_count" )+ " " +"Shares");
        profile_updatedtime_txt.setText( postslist.get( position ).get( "posted_date" ) );



        final SpannableString spanString = new SpannableString(Html.fromHtml(postslist.get( position ).get( "title" )));
        String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
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

                        startActivity(httpIntent);

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

        custm_title__link.setText(spanString);
        custm_title__link.setMovementMethod(LinkMovementMethod.getInstance());



        if(postslist.get(position).get("description").equalsIgnoreCase("-")){
            custm_title_txt.setVisibility(View.GONE);
        }else{
            custm_title_txt.setVisibility(View.VISIBLE);
        }

      /*  if(postslist.get(position).get("filename").equalsIgnoreCase("-")){
            postimage.setVisibility(View.GONE);
        }else{
            postimage.setVisibility(View.VISIBLE);
        }*/

        if(postslist.get(position).get("title").equalsIgnoreCase("-")){
            custm_title__link.setVisibility(View.GONE);
        }else{
            custm_title__link.setVisibility(View.VISIBLE);
        }

        try{
            Glide.with(getActivity() )
                    .load(Uri.parse(StoredUrls.Uploadedimages +postslist.get(position).get("profile_picture"))) // add your image url
                    .centerCrop() // scale to fill the ImageView and crop any extra
                    .fitCenter() // scale to fit entire image within ImageView
                    .placeholder(R.drawable.man)
                    .into(landing_circular_img);
        }catch (Exception e){
        }

        comments_count = postslist.get( position ).get( "comments_count" );

        if (comments_count.equalsIgnoreCase( "0" )){
            comments_recycle.setVisibility( View.GONE );
        }
        else {
            comments_recycle.setVisibility( View.VISIBLE );

        }
    }

    public class GetPostdetailsTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            //CustomProgressbar.Progressbarshow( getActivity());
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token","Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method","get-post-details" ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0] ) );
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
                //  CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    getposdetailslist.clear();
                    getposdetailslist = JsonParsing.GetJsonData( results );

                    postid =  getposdetailslist.get( 0 ).get( "post_id" );
                    files_count =  getposdetailslist.get( 0 ).get( "files_count" );
                    posttype =  getposdetailslist.get( 0 ).get( "post_type" );
                    type = getposdetailslist.get( 0 ).get( "type" );
                    String files = getposdetailslist.get( 0 ).get( "files" );
                    getmultipleimageslist = JsonParsing.GetJsonData( files );

                    StoredObjects.LogMethod( "response", "response<><><>images:---" + getmultipleimageslist.size() );
                    dataassign(getposdetailslist,position);







                    try{
                       /* if(getposdetailslist.get(0).get("fishing_spot").equalsIgnoreCase("-")){
                            fishingspots_text.setVisibility(View.GONE);
                        }else{
                            fishingspots_text.setVisibility(View.VISIBLE);
                        }*/
                        ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
                        images_array.clear();
                        images_array = JsonParsing.GetJsonData(getposdetailslist.get(0).get("fishings_spots_list"));
                        if (images_array.size() > 0) {

                            String value = "";
                            fishingspots_lay.removeAllViews();

                            for (int i = 0; i <images_array.size() ; i++) {
                                addlayoutfishingspots(fishingspots_lay,images_array,i);
                            }

                            /*for (int i = 0; i <images_array.size() ; i++) {
                                value += "#"+images_array.get(i).get("name")+",";
                            }
                            value = value.substring(0,value.length()-1);
                            fishingspots_text.setText(value);*/

                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                        String comments = getposdetailslist.get( 0 ).get( "comments" );
                        ArrayList<HashMap<String, String>> getcommentslist = new ArrayList<>();
                        getcommentslist.clear();
                        getcommentslist = JsonParsing.GetJsonData( comments );
                        StoredObjects.LogMethod( "response", "response<><><>:---" + getcommentslist.size() );
                        Collections.reverse(getcommentslist);
                        mShimmerViewContainer.setVisibility(View.GONE);

                        comments_main_layout.setVisibility(View.VISIBLE);
                        if (getcommentslist.size() > 0) {
                            comments_recycle.setVisibility( View.VISIBLE );
                            comments_count_txt.setText( getcommentslist.size()+"");
                            adapter = new HashMapRecycleviewadapter(getActivity(),getcommentslist,"comments",comments_recycle,R.layout.comments_listitems );
                            comments_recycle.setAdapter(adapter);
                            //customRecyclerview.Assigndatatorecyleviewhashmap( comments_recycle, getcommentslist, "comments", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.comments_listitems );
                        } else {
                            comments_recycle.setVisibility( View.GONE );


                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }




                    /*if (getcommentslist.size() <= 0) {

                        comments_recycle.setVisibility( View.GONE );
                    } else {

                        customRecyclerview.Assigndatatorecyleviewhashmap( comments_recycle, getcommentslist, "comments", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.comments_listitems );

                    }*/
                }else {
                    mShimmerViewContainer.setVisibility(View.GONE);
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

    public class sendcommentTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow( getActivity());
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token","Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method","comment-on-post" ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0] ) );
                nameValuePairs.add( new BasicNameValuePair( "post_id", params[1] ) );
                nameValuePairs.add( new BasicNameValuePair( "comments", params[2] ) );

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
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    // StoredObjects.ToastMethod( "commented",getActivity() );
                    commnt_edittxt.setText( "" );


                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        new GetPostdetailsTask().execute(StoredObjects.UserId,postid);
                    }else{
                        StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                    }

                }
                else {
                    String error = jsonObject.getString( "error" );
                    StoredObjects.ToastMethod(error,getActivity());
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



    public class LikepostTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(getActivity());
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
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    StoredObjects.ToastMethod("liked", getActivity());

                    try{
                        int like_count = Integer.parseInt(getposdetailslist.get( position ).get( "likes"));
                        like_txt_count.setText((like_count+1) +"");
                    }catch (Exception e){

                    }

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, getActivity());
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

    private void Sharepostpopup(final Activity activity, final ArrayList<HashMap<String, String>> list, final int position, final String post_id, String type) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sharepost_confirmation );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setGravity( Gravity.BOTTOM);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
        ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
        CustomRegularTextView sharenow_text = (CustomRegularTextView)dialog.findViewById( R.id.sharenow_text );
        CircularImageView profile_circular_img = (CircularImageView)dialog.findViewById( R.id.profile_circular_img );
        CustomRegularTextView profile_name_txt = (CustomRegularTextView)dialog.findViewById( R.id.profile_name_txt );
        CustomRegularTextView share_date_time_txt = (CustomRegularTextView)dialog.findViewById( R.id.share_date_time_txt );
        final CustomEditText commnt_edittxt = (CustomEditText)dialog.findViewById( R.id.commnt_edittxt );

        commnt_edittxt.requestFocus();
        dialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        try {
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

    public class SharePostTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(getActivity());
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
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    StoredObjects.ToastMethod("Successfully Post Shared.", getActivity());

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, getActivity());
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
            CustomProgressbar.Progressbarshow(getActivity());
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
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    StoredObjects.ToastMethod("unliked", getActivity());

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod(error, getActivity());
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
    private void liked_details_popup(final Activity activity) {
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
        getlikeslist.clear();
        ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
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

    public class likesdetailsTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow( getActivity());
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
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    getposdetailslist = JsonParsing.GetJsonData( results );
                    liked_details_popup( getActivity());

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
            CustomProgressbar.Progressbarshow( getActivity());
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
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    getposdetailslist = JsonParsing.GetJsonData( results );
                    comments_details_popup( getActivity() );

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
    private void comments_details_popup(final Activity activity) {
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
        liked_recycle =(RecyclerView) dialog.findViewById( R.id.liked_recycle );

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity);
        liked_recycle.setLayoutManager(linearLayoutManager);
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



    public void addlayoutfishingspots(LinearLayout layout,final ArrayList<HashMap<String,String>> arrayList, final int position){

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fishingspots_addlititem, null);

        CustomRegularTextView fishingspots_text = v.findViewById (R.id.fishingspots_text);
        StoredObjects.LogMethod( "response", "images_array_size:---" + arrayList.size()+"<<<><><>"+""+"<<><>"+position );
        fishingspots_text.setText("#"+arrayList.get(position).get("name"));

        fishingspots_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FishingSpotsDetailsActivity.class);
                intent.putExtra("YourHashMap", arrayList);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        layout.addView(v);

    }


}
