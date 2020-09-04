package com.fishbuddy.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
import com.fishbuddy.customadapter.OnLoadMoreListener;
import com.fishbuddy.customcamara.AddpostBreedActivity;
import com.fishbuddy.customcamara.CameraActivity;
import com.fishbuddy.customcamara.LogcatchClass;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.servicesparsing.ProgressBarClass;
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

import static com.fishbuddy.sidemenu.SideMenu.btm_addpost_txt;
import static com.fishbuddy.sidemenu.SideMenu.buttonchangemethod;
import static com.fishbuddy.sidemenu.SideMenu.list_slidermenu_lay;
import static com.fishbuddy.sidemenu.SideMenu.mDrawerLayout;

public class Landing_page extends Fragment {
    RecyclerView landing_recycle;
    CustomRecyclerview customRecyclerview;
    LinearLayout fishing_spots_lay,top_lay,fish_breads_lay,guide_lay,popular_lay,fishing_club_lay;
    ImageView custm_menu_img,custm_search_img;

    public static int drawablecount = 0;
    public ArrayList<HashMap<String, String>> getpostlist = new ArrayList<>();
    public ArrayList<HashMap<String, String>> getpostlistall = new ArrayList<>();
    CustomRegularTextView nodatafound_txt;
    LinearLayout nodatafound_lay;
    CoordinatorLayout cordinator_layout;
    private ShimmerFrameLayout mShimmerViewContainer;
    SwipeRefreshLayout swipeRefreshLayout ;

    String total_results = "0",total_pages = "0",results_count = "0";
    int pageCount = 1;
    public static HashMapRecycleviewadapter adapter;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount,lastVisibleItem;
    //activityResumeTrigger: not whiteListedcom.fishbuddy/com.fishbuddy.Activities.Sign_in_Sign_up/15
    NestedScrollView mNestedScrollView;
    ProgressBar progressBar1;



    @Override
    public void onResume() {
        buttonchangemethod (getActivity() , fish_breads_lay , custm_menu_img , btm_addpost_txt , "5");

        StoredObjects.LogMethod( "response", "pageback_checking:---" + StoredObjects.page_type);
        if(StoredObjects.page_type.equalsIgnoreCase("fromscanpost")){
            pageCount= 1;
            loadpostsdata(pageCount);
        }
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.landingpage_test,null,false );
        StoredObjects.page_type="landingpage";
        StoredObjects.back_type="landingpage";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);

        loadpostsdata(pageCount);


        /*Intent intent = new Intent(getActivity(), AddpostBreedActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",null);
        intent.putExtra("BUNDLE",args);
        // intent.putStringArrayListExtra("array", fishbreeds_list);
        startActivity(intent);*/


       /* Intent intent = new Intent(getActivity(), LogcatchClass.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",getpostlist);
        intent.putExtra("BUNDLE",args);
        // intent.putStringArrayListExtra("array", fishbreeds_list);
        startActivity(intent);
*/
        return v;
    }

    public void loadpostsdata(int pageCount){

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new GetPostsTask().execute(StoredObjects.UserId,pageCount+"");
        }else{
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
        }
    }



    private void initilization(View v) {

        mShimmerViewContainer =  v.findViewById(R.id.shimmer_view_container);

        landing_recycle = (RecyclerView) v.findViewById( R.id.landing_recycle );
        customRecyclerview = new CustomRecyclerview(getActivity());
        fishing_spots_lay = (LinearLayout)v.findViewById( R.id.fishing_spots_lay );
        top_lay = (LinearLayout)v.findViewById( R.id.top_lay );
        fish_breads_lay = (LinearLayout)v.findViewById( R.id.fish_breads_lay );
        guide_lay = (LinearLayout)v.findViewById(R.id.guide_lay);
        popular_lay = (LinearLayout)v.findViewById(R.id.popular_lay);
        fishing_club_lay = v.findViewById(R.id.fishing_club_lay);
        custm_menu_img = (ImageView)v.findViewById( R.id.custm_menu_img );
        custm_search_img = (ImageView)v.findViewById( R.id.custm_search_img );
        nodatafound_txt = (CustomRegularTextView)v.findViewById( R.id.nodatafound_txt );
        nodatafound_lay = (LinearLayout) v.findViewById( R.id.nodatafound_lay );
        //cordinator_layout = v.findViewById( R.id.cordinator_layout );
        mNestedScrollView = (NestedScrollView) v.findViewById(R.id.mNestedScrollView);
        progressBar1 = v.findViewById( R.id.progressBar1 );
        fishing_spots_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentcallinglay( new Fishingspots() );
            }
        } );

        fishing_club_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay(new FishingClubs());
            }
        });

        fish_breads_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentcallinglay( new FishBreeds() );
            }
        } );

        guide_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentcallinglay( new Guide() );
            }
        } );
        popular_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  fragmentcallinglay( new Popular() );
                fragmentcallinglay( new Tackle() );
            }
        } );

        custm_menu_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawablecount++;
                if (drawablecount == 1) {
                    mDrawerLayout.openDrawer(list_slidermenu_lay);
                } else {
                    drawablecount = 0;
                    mDrawerLayout.closeDrawer(list_slidermenu_lay);
                }
            }

        });




        custm_search_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentcallinglay( new Searchpage() );
            }
        } );



        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        landing_recycle.setLayoutManager(linearLayoutManager);



        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();
                        visibleItemCount = linearLayoutManager.getChildCount();
                        totalItemCount = linearLayoutManager.getItemCount();

                        try {



                            if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                                pageCount++;
                                StoredObjects.LogMethod("status_res", "status_res_chek_sizescroll:--"+getpostlist.size()+"<><>><>"+total_results);

                                if(getpostlist.size()<Integer.parseInt(total_results)){
                                   // loadpostsdata(pageCount);
                                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                                        new GetPostsTaskload().execute(StoredObjects.UserId,pageCount+"");
                                    }else{
                                        StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                                    }
                                    progressBar1.setVisibility( View.VISIBLE );

                                }else {
                                    progressBar1.setVisibility( View.GONE );
                                }
                            }


                        }catch (Exception e){

                        }
                    }
                }
            }
        });



/*
    landing_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();


                if (lastvisibleitemposition == adapter.getItemCount()-1) {



                    if(getpostlistall.size()<Integer.parseInt(total_results)){
                        pageCount = pageCount+1;
                        loadpostsdata(pageCount);
                        progressBar1.setVisibility( View.VISIBLE );
                        StoredObjects.LogMethod("status_res", "status_res_chek_size:--"+getpostlistall.size()+"<><>><>"+total_results);
                    }else {
                        progressBar1.setVisibility( View.GONE );
                    }


                }
            }
        });
*/


       // StoredObjects.hashmaplist(4);

    }

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }


    public class GetPostsTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
          //   CustomProgressbar.Progressbarshow( getActivity());
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token","Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method","get-posts" ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0]) );
                nameValuePairs.add( new BasicNameValuePair( "sort_by", "Latestpostonthetop" ) );
                nameValuePairs.add( new BasicNameValuePair( "page",params[1] ));

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
              //   CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                StoredObjects.LogMethod( "response", "response_landing_check:---" + status );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    total_results = jsonObject.getString( "total_results" );
                    results_count = jsonObject.getString( "results_count" );
                    total_pages = jsonObject.getString( "total_pages" );
                    progressBar1.setVisibility( View.GONE );

                    if(pageCount==1){
                        getpostlist.clear();
                        getpostlistall = JsonParsing.GetJsonData( results );
                        getpostlist.addAll(getpostlistall);
                        mShimmerViewContainer.setVisibility(View.GONE);
                        landing_recycle.setVisibility( View.VISIBLE );
                        nodatafound_lay.setVisibility( View.GONE );
                        adapter = new HashMapRecycleviewadapter( getActivity(), getpostlist, "landingpage", landing_recycle, R.layout.landing_listitems_testing );//
                        landing_recycle.setAdapter( adapter );




                    }else{
                        getpostlistall = JsonParsing.GetJsonData(results);
                        getpostlist.addAll(getpostlistall);
                        StoredObjects.LogMethod("val","val:::"+"yes"+getpostlist.size());
                        adapter = new HashMapRecycleviewadapter( getActivity(), getpostlist, "landingpage", landing_recycle, R.layout.landing_listitems_testing );//
                        landing_recycle.setAdapter( adapter );
                        adapter.notifyDataSetChanged();
                        landing_recycle.invalidate();
                    }


                }
                else if (status.equalsIgnoreCase( "500" )) {
                    mShimmerViewContainer.setVisibility(View.GONE);
                    landing_recycle.setVisibility(View.GONE);
                    nodatafound_lay.setVisibility(View.VISIBLE);
                    nodatafound_txt.setVisibility(View.VISIBLE);
                }
                else{

                    if(getpostlist.size() == 0){

                        landing_recycle.setVisibility(View.GONE);
                        nodatafound_lay.setVisibility(View.VISIBLE);
                        nodatafound_txt.setVisibility(View.VISIBLE);
                    }else{
                        landing_recycle.setVisibility(View.VISIBLE);
                        adapter = new HashMapRecycleviewadapter( getActivity(), getpostlist, "landingpage", landing_recycle, R.layout.landing_listitems_testing );//
                        landing_recycle.setAdapter( adapter );
                        landing_recycle.invalidate();
                        nodatafound_lay.setVisibility(View.GONE);
                        nodatafound_txt.setVisibility(View.GONE);
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }


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

    public class GetPostsTaskload extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            ///ProgressBarClass.Progressbarshow( getActivity() );
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token","Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method","get-posts" ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0] ) );
                nameValuePairs.add( new BasicNameValuePair( "sort_by", "Latestpostonthetop" ) );
                nameValuePairs.add( new BasicNameValuePair( "page",params[1] ));

                strResult = HttpPostClass.PostMethod( StoredUrls.BaseUrl, nameValuePairs );


              /*  getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getpostlistall.remove(getpostlistall.size() - 1);
                        adapter.notifyItemRemoved(getpostlistall.size());

                    }
                });*/


/*
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getpostlistall.remove(getpostlistall.size() - 1);
                        adapter.notifyItemRemoved(getpostlistall.size());
                        mShimmerViewContainer.setVisibility(View.GONE);

                    }
                });
*/
               /* new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        getpostlistall.remove(getpostlistall.size() - 1);
                        adapter.notifyItemRemoved(getpostlistall.size());
                    }
                }, 100);*/

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
                //ProgressBarClass.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    total_results = jsonObject.getString( "total_results" );
                    results_count = jsonObject.getString( "results_count" );
                    total_pages = jsonObject.getString( "total_pages" );

                    if(pageCount==1){
                        getpostlist.clear();
                        getpostlistall = JsonParsing.GetJsonData( results );
                        getpostlist.addAll(getpostlistall);
                        mShimmerViewContainer.setVisibility(View.GONE);
                        landing_recycle.setVisibility( View.VISIBLE );
                        nodatafound_lay.setVisibility( View.GONE );
                        adapter = new HashMapRecycleviewadapter( getActivity(), getpostlist, "landingpage", landing_recycle, R.layout.landing_listitems_testing );//
                        landing_recycle.setAdapter( adapter );
                        landing_recycle.invalidate();

                    }else{

                        getpostlistall = JsonParsing.GetJsonData(results);
                        getpostlist.addAll(getpostlistall);
                        adapter = new HashMapRecycleviewadapter( getActivity(), getpostlist, "landingpage", landing_recycle, R.layout.landing_listitems_testing );//
                        landing_recycle.setAdapter( adapter );
                        adapter.notifyDataSetChanged();
                        landing_recycle.invalidate();
                    }


                }else{


                    if(getpostlist.size() == 0){
                        landing_recycle.setVisibility(View.GONE);
                        nodatafound_txt.setVisibility(View.VISIBLE);
                    }else{
                        landing_recycle.setVisibility(View.VISIBLE);
                        nodatafound_txt.setVisibility(View.GONE);
                        adapter = new HashMapRecycleviewadapter( getActivity(), getpostlist, "landingpage", landing_recycle, R.layout.landing_listitems_testing );//
                        landing_recycle.setAdapter( adapter );
                        landing_recycle.invalidate();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }


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
    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }


}
