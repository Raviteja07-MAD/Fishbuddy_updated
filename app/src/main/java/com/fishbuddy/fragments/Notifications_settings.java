package com.fishbuddy.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
import com.fishbuddy.customfonts.CustomButton;
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
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Notifications_settings extends Fragment {
    RecyclerView popular_recyclerview;
    CustomRecyclerview customRecyclerview;
    TextView title_txt;
    Switch switch_on_new_user_followed,switch_comments,switch_newposts,switch_likes,switch_newposts_follower;
   // on_new_post_by_user_you_followers_lay,switch_newposts_follower

    CustomButton update_settings_btn;


    String on_new_user_followed = "" ;
    String on_another_user_commented_on_your_post= "" ;
    String on_new_post_by_user_you_following = "" ;
    String on_another_user_liked_your_post= "" ;
    String on_new_post_by_your_follower = "" ;

    ArrayList<HashMap<String, String>> getprofilelist=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.notifications_settings,null,false );
        StoredObjects.page_type="home";
        StoredObjects.back_type="notificationssettings";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);




        return v;
    }

    private void initilization(View v) {
        title_txt = (TextView)v.findViewById( R.id. title_txt);
        title_txt.setText( R.string.notifications_settings );


        switch_on_new_user_followed = (Switch)v.findViewById( R.id.switch_on_new_user_followed );
        switch_comments = (Switch)v.findViewById( R.id.switch_comments );
        switch_newposts = (Switch)v.findViewById( R.id.switch_newposts );
        switch_newposts_follower = (Switch)v.findViewById( R.id.switch_newposts_follower );
        switch_likes = (Switch)v.findViewById( R.id.switch_likes );

        update_settings_btn = v.findViewById(R.id.update_settings_btn);

        ImageView backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();

            }
        } );



        update_settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(switch_on_new_user_followed.isChecked() ==true){
                    on_new_user_followed = "Yes";
                }else{
                    on_new_user_followed = "No";
                }

                if(switch_comments.isChecked() ==true){
                    on_another_user_commented_on_your_post = "Yes";
                }else{
                    on_another_user_commented_on_your_post = "No";
                }


                if(switch_newposts.isChecked() ==true){
                    on_new_post_by_user_you_following = "Yes";
                }else{
                    on_new_post_by_user_you_following = "No";
                }

                if(switch_likes.isChecked() ==true){
                    on_another_user_liked_your_post = "Yes";
                }else{
                    on_another_user_liked_your_post = "No";
                }

                if(switch_newposts_follower.isChecked() ==true){
                    on_new_post_by_your_follower = "Yes";
                }else{
                    on_new_post_by_your_follower = "No";
                }



                if (InterNetChecker.isNetworkAvailable(getActivity())) {
                    new UpdateNotificationSettingsTask().execute(StoredObjects.UserId);
                }else{
                    StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                }


            }
        });





        asigndata();



        /*  if(checkbox.equalsIgnoreCase("dont")){
            route_selection_checkbox.setChecked(true);
            StoredObjects.savedata(getActivity(),"dont","checkbox");
        }else{
            route_selection_checkbox.setChecked(false);
            StoredObjects.savedata(getActivity(),"show","checkbox");
        }   */
    }

/* String on_new_user_followed = StoredObjects.getsaveddata(getApplicationContext(),"on_new_user_followed");
        String on_another_user_commented_on_your_post = StoredObjects.getsaveddata(getApplicationContext(),"on_another_user_commented_on_your_post");
        String on_new_post_by_user_you_following = StoredObjects.getsaveddata(getApplicationContext(),"on_new_post_by_user_you_following");
        String on_another_user_liked_your_post = StoredObjects.getsaveddata(getApplicationContext(),"on_another_user_liked_your_post");
        String on_new_post_by_your_follower = StoredObjects.getsaveddata(getApplicationContext(),"on_new_post_by_your_follower");*/




public void asigndata(){


    try{
        on_new_user_followed = StoredObjects.getsaveddata(getActivity(),"on_new_user_followed");
        on_another_user_commented_on_your_post = StoredObjects.getsaveddata(getActivity(),"on_another_user_commented_on_your_post");
        on_new_post_by_user_you_following = StoredObjects.getsaveddata(getActivity(),"on_new_post_by_user_you_following");
        on_another_user_liked_your_post = StoredObjects.getsaveddata(getActivity(),"on_another_user_liked_your_post");
        on_new_post_by_your_follower = StoredObjects.getsaveddata(getActivity(),"on_new_post_by_your_follower");

        StoredObjects.LogMethod( "response", "response_settings:---" + on_new_user_followed+"<>"+on_another_user_commented_on_your_post+"<>"+on_new_post_by_user_you_following
                +"<>"+on_another_user_liked_your_post+"<>"+on_new_post_by_your_follower);

        if(on_new_user_followed.equalsIgnoreCase("Yes")){
            switch_on_new_user_followed.setChecked(true);
        }else{
            switch_on_new_user_followed.setChecked(false);
        }


        if(on_another_user_commented_on_your_post.equalsIgnoreCase("Yes")){
            switch_comments.setChecked(true);
        }else{
            switch_comments.setChecked(false);
        }


        if(on_new_post_by_user_you_following.equalsIgnoreCase("Yes")){
            switch_newposts.setChecked(true);
        }else{
            switch_newposts.setChecked(false);
        }


        if(on_another_user_liked_your_post.equalsIgnoreCase("Yes")){
            switch_likes.setChecked(true);
        }else{
            switch_likes.setChecked(false);
        }


        if(on_new_post_by_your_follower.equalsIgnoreCase("Yes")){
            switch_newposts_follower.setChecked(true);
        }else{
            switch_newposts_follower.setChecked(false);
        }


    }catch (Exception e){

    }

}




public class UpdateNotificationSettingsTask extends AsyncTask<String, String, String> {
    String strResult = "";

    @Override
    protected void onPreExecute() {
        CustomProgressbar.Progressbarshow( getActivity() );
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
            //nameValuePairs.add( new BasicNameValuePair( "token", "Mikel") );
            nameValuePairs.add( new BasicNameValuePair( "method", "update-notification-settings" ) );
            nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0] ) );

            nameValuePairs.add( new BasicNameValuePair( "on_new_user_followed", on_new_user_followed ) );
            nameValuePairs.add( new BasicNameValuePair( "on_another_user_commented_on_your_post", on_another_user_commented_on_your_post ) );
            nameValuePairs.add( new BasicNameValuePair( "on_new_post_by_user_you_following", on_new_post_by_user_you_following ) );
            nameValuePairs.add( new BasicNameValuePair( "on_another_user_liked_your_post", on_another_user_liked_your_post ) );
            nameValuePairs.add( new BasicNameValuePair( "on_new_post_by_your_follower",on_new_post_by_your_follower ) );



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

                StoredObjects.ToastMethod("Successfully Updated.",getActivity());

                if (InterNetChecker.isNetworkAvailable(getActivity())) {
                    new GetProfileTask().execute(StoredObjects.UserId);
                }else{
                    StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                }

            }else{

                StoredObjects.ToastMethod("Try Again.",getActivity());


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



    public class GetProfileTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            // CustomProgressbar.Progressbarshow( getActivity());
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token", "Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method", "get-profile" ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id", params[0] ) );
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
                // CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    getprofilelist = JsonParsing.GetJsonData( results );
                    String followerscount = getprofilelist.get( 0 ).get( "followers_count" );
                    String followingcount = getprofilelist.get( 0 ).get( "following_count" );



                    try{
                        String on_new_user_followed = getprofilelist.get(0).get("on_new_user_followed");
                        String on_another_user_commented_on_your_post = getprofilelist.get(0).get("on_another_user_commented_on_your_post");
                        String on_new_post_by_user_you_following = getprofilelist.get(0).get("on_new_post_by_user_you_following");
                        String on_another_user_liked_your_post = getprofilelist.get(0).get("on_another_user_liked_your_post");
                        String on_new_post_by_your_follower = getprofilelist.get(0).get("on_new_post_by_your_follower");


                        StoredObjects.savedata(getActivity(),on_new_user_followed,"on_new_user_followed");
                        StoredObjects.savedata(getActivity(),on_another_user_commented_on_your_post,"on_another_user_commented_on_your_post");
                        StoredObjects.savedata(getActivity(),on_new_post_by_user_you_following,"on_new_post_by_user_you_following");
                        StoredObjects.savedata(getActivity(),on_another_user_liked_your_post,"on_another_user_liked_your_post");
                        StoredObjects.savedata(getActivity(),on_new_post_by_your_follower,"on_new_post_by_your_follower");


                    }catch (Exception e){

                    }


                    asigndata();

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


}