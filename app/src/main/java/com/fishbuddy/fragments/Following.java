package com.fishbuddy.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
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

public class Following extends Fragment {
    RecyclerView profile_following_recyclerview;
    CustomRecyclerview customRecyclerview;
    TextView title_txt;

    private ShimmerFrameLayout shimmer_view_container_follers_list;

    LinearLayout nodatafound_lay;


    int pageCount = 1;
    int totalpages;
    String total_results ,total_pages= "0";
    public static HashMapRecycleviewadapter adapter;


    public ArrayList<HashMap<String, String>> followinglist = new ArrayList<>();
    ArrayList<HashMap<String, String>> followinglistall=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.profile_see_all_following,null,false );
        StoredObjects.page_type="home";
        StoredObjects.back_type="Following";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        getfollowingdata(pageCount);
        return v;
    }


    public void getfollowingdata(int page_count){

        if (InterNetChecker.isNetworkAvailable(getActivity())) {

            new GetFollowingTask().execute(StoredObjects.UserId,page_count+"");
        }else{
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
        }
    }


    private void initilization(View v) {

        shimmer_view_container_follers_list = v.findViewById(R.id.shimmer_view_container_follers_list);
        nodatafound_lay = v.findViewById(R.id.nodatafound_lay);
        profile_following_recyclerview = (RecyclerView) v.findViewById( R.id.profile_following_recyclerview );
        customRecyclerview = new CustomRecyclerview(getActivity());

        title_txt = (TextView)v.findViewById( R.id. title_txt);
        title_txt.setText("Following");

        ImageView backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();

            }
        } );

       // StoredObjects.hashmaplist(8);
         // customRecyclerview.Assigndatatorecyleviewhashmap(profile_following_recyclerview, StoredObjects.dummy_list,"following", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.profile_see_all_following_listitems);

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        profile_following_recyclerview.setLayoutManager(linearLayoutManager);
        profile_following_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastvisibleitemposition == adapter.getItemCount() - 1) {
                    if(pageCount<=Integer.parseInt(total_pages)){
                        pageCount=pageCount+1;
                        getfollowingdata(pageCount);
                    }


                }
            }
        });
    }

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace( R.id.frame_container, fragment ).commit();

    }

    public class GetFollowingTask extends AsyncTask<String, String, String> {
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
                nameValuePairs.add( new BasicNameValuePair( "method", "following" ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id", params[0] ) );
                nameValuePairs.add( new BasicNameValuePair( "page", params[1] ) );
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
                    total_results = jsonObject.getString( "total_results" );
                    total_pages = jsonObject.getString( "total_pages" );
                   // followinglist = JsonParsing.GetJsonData( results );
                    /*if (followinglist.size()>0){
                        shimmer_view_container_follers_list.setVisibility(View.GONE);
                        profile_following_recyclerview.setVisibility(View.VISIBLE);
                        customRecyclerview.Assigndatatorecyleviewhashmap( profile_following_recyclerview, followinglist, "following", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.profile_see_all_following_listitems );
                    }else{

                    }*/

                    if(pageCount==1){
                        followinglistall.clear();
                        followinglist = JsonParsing.GetJsonData(results);
                        followinglistall.addAll(followinglist);

                        shimmer_view_container_follers_list.setVisibility(View.GONE);
                        nodatafound_lay.setVisibility(View.GONE);
                        profile_following_recyclerview.setVisibility( View.VISIBLE );

                        adapter = new HashMapRecycleviewadapter(getActivity(),followinglistall,"following",profile_following_recyclerview,R.layout.profile_see_all_following_listitems );//
                        profile_following_recyclerview.setAdapter(adapter);


                    }else{

                        followinglist = JsonParsing.GetJsonData(results);
                        followinglistall.addAll(followinglist);
                        StoredObjects.LogMethod("val","val:::"+"yes"+followinglistall.size());
                        adapter.notifyDataSetChanged();

                    }

                } else {


                if (followinglistall.size()==0){
                    shimmer_view_container_follers_list.setVisibility(View.GONE);
                    nodatafound_lay.setVisibility(View.VISIBLE);
                    profile_following_recyclerview.setVisibility( View.GONE );
                }

                    //nodatafound_txt.setVisibility( View.VISIBLE );
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
