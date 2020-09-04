/*
package com.fishbuddy.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
import com.fishbuddy.customfonts.CustomRegularTextView;
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

import static com.fishbuddy.fragments.PaginationListener.PAGE_START;
import static com.fishbuddy.sidemenu.SideMenu.btm_addpost_txt;
import static com.fishbuddy.sidemenu.SideMenu.buttonchangemethod;
import static com.fishbuddy.sidemenu.SideMenu.list_slidermenu_lay;
import static com.fishbuddy.sidemenu.SideMenu.mDrawerLayout;

public class Landing_page_backup_moreloading extends Fragment {
    RecyclerView landing_recycle;
    CustomRecyclerview customRecyclerview;
    LinearLayout fishing_spots_lay,top_lay,fish_breads_lay,guide_lay,popular_lay;
    ImageView custm_menu_img,custm_search_img;
    public static int drawablecount = 0;
    public ArrayList<HashMap<String, String>> getpostlist = new ArrayList<>();
    public ArrayList<HashMap<String, String>> getpostlistall = new ArrayList<>();
    CustomRegularTextView nodatafound_txt;;
    LinearLayout nodatafound_lay;
    private int currentPage = PAGE_START;
    private ShimmerFrameLayout mShimmerViewContainer;
    SwipeRefreshLayout swipeRefreshLayout ;

    String total_results = "0",total_pages = "0";
    int pageCount = 1;
    public static HashMapRecycleviewadapter adapter;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    //activityResumeTrigger: not whiteListedcom.fishbuddy/com.fishbuddy.Activities.Sign_in_Sign_up/15

    @Override
    public void onResume() {
        buttonchangemethod (getActivity() , fish_breads_lay , custm_menu_img , btm_addpost_txt , "5");
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

        return v;
    }


    public void loadpostsdata(int pageCount_){

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new GetPostsTask().execute(StoredObjects.UserId);
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
        custm_menu_img = (ImageView)v.findViewById( R.id.custm_menu_img );
        custm_search_img = (ImageView)v.findViewById( R.id.custm_search_img );
        nodatafound_txt = (CustomRegularTextView)v.findViewById( R.id.nodatafound_txt );
        nodatafound_lay = (LinearLayout) v.findViewById( R.id.nodatafound_lay );
        //swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_view);
        fishing_spots_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentcallinglay( new Fishingspots() );
            }
        } );

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

                fragmentcallinglay( new Popular() );
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

        landing_recycle.addOnScrollListener( new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= 10) {
                        if (InterNetChecker.isNetworkAvailable(getActivity())) {
                            new GetPostsTaskload().execute(StoredObjects.UserId);
                        }else{
                            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                        }                        StoredObjects.LogMethod("status_res", "status_res_chek_size:--"+firstVisibleItemPosition+"<><>><>"+totalItemCount);

                    }
                }
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        } );
*/
/*
        landing_recycle.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                if (InterNetChecker.isNetworkAvailable(getActivity())) {
                    new GetPostsTaskload().execute(StoredObjects.UserId);
                }else{
                    StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
*//*


*/
/*
        landing_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastvisibleitemposition == adapter.getItemCount() - 1) {
                    if(pageCount<=Integer.parseInt(total_pages)){
                        StoredObjects.LogMethod("status_res", "status_res_chek_size:--"+pageCount+"<><>><>"+total_pages);
                        loadpostsdata(pageCount);
                    }


                }
            }
        });
*//*




*/
/*
    landing_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount()-1) {

                    pageCount = pageCount+1;

                    if(getpostlistall.size()<Integer.parseInt(total_results)){
                       // loadpostsdata(pageCount);
                        StoredObjects.LogMethod("status_res", "status_res_chek_size:--"+getpostlistall.size()+"<><>><>"+total_results);
                    }



                }
            }
        });
*//*



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
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0] ) );
                nameValuePairs.add( new BasicNameValuePair( "sort_by", "Latestpostonthetop" ) );
                nameValuePairs.add( new BasicNameValuePair( "page",pageCount+"" ));

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
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    total_results = jsonObject.getString( "total_results" );
                    total_pages = jsonObject.getString( "total_pages" );
                    getpostlist = JsonParsing.GetJsonData( results );

                    if (getpostlist.size() > 0) {
                        mShimmerViewContainer.setVisibility( View.GONE );
                        adapter = new HashMapRecycleviewadapter( getActivity(), getpostlist, "landingpage", landing_recycle, R.layout.landing_listitems_testing );//
                        landing_recycle.setAdapter( adapter );

                        if (currentPage != PAGE_START) adapter.removeLoading();
                        adapter.addItems( getpostlist );

                        // check weather is last page or not
                        if (currentPage < Integer.parseInt( total_pages )) {
                            adapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;


                    }else{

                        mShimmerViewContainer.setVisibility(View.VISIBLE);
                        landing_recycle.setVisibility( View.GONE );
                       nodatafound_lay.setVisibility( View.VISIBLE );

                    }
                }else{

                    mShimmerViewContainer.setVisibility(View.VISIBLE);
                    landing_recycle.setVisibility( View.GONE );
                    nodatafound_lay.setVisibility( View.VISIBLE );

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
            //   CustomProgressbar.Progressbarshow( getActivity());
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token","Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method","get-posts" ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0] ) );
                nameValuePairs.add( new BasicNameValuePair( "sort_by", "Latestpostonthetop" ) );
                nameValuePairs.add( new BasicNameValuePair( "page",currentPage+"" ));

                strResult = HttpPostClass.PostMethod( StoredUrls.BaseUrl, nameValuePairs );


*/
/*
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getpostlistall.remove(getpostlistall.size() - 1);
                        adapter.notifyItemRemoved(getpostlistall.size());
                    }
                });
*//*

               */
/* new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        getpostlistall.remove(getpostlistall.size() - 1);
                        adapter.notifyItemRemoved(getpostlistall.size());
                    }
                }, 100);*//*


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
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    total_results = jsonObject.getString( "total_results");
                    total_pages = jsonObject.getString( "total_pages" );

                    getpostlist = JsonParsing.GetJsonData(results);
                    getpostlistall.addAll(getpostlist);
                    adapter.notifyDataSetChanged();
                    adapter.setLoaded();


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
*/
