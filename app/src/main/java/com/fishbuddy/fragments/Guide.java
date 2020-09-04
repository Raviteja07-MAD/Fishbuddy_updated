package com.fishbuddy.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
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

public class Guide extends Fragment {
    TextView title_txt;
    CustomRecyclerview customRecyclerview;
    RecyclerView fishing_tricks_recycle;


    ArrayList<HashMap<String, String>> guide_list=new ArrayList<>();
    ArrayList<HashMap<String, String>> guide_listall=new ArrayList<>();
    private ShimmerFrameLayout mShimmerViewContainer;
    LinearLayout nodatafound_lay;


    int pageCount = 1;
    int totalpages;
    String total_results ,total_pages= "0";
    HashMapRecycleviewadapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.guide,null,false );
        StoredObjects.page_type="home";
        StoredObjects.back_type="guide";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        getguidesdata(pageCount);
        return v;
    }
    public void getguidesdata(int page_count){
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new GuideTask().execute(StoredObjects.UserId,page_count+"");
        }else{
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
        }
    }

    private void initilization(View v) {

        mShimmerViewContainer =  v.findViewById(R.id.shimmer_fishguides_xml);
        nodatafound_lay = (LinearLayout) v.findViewById( R.id.nodatafound_lay );


        fishing_tricks_recycle = (RecyclerView) v.findViewById( R.id.fishing_tricks_recycle );
        customRecyclerview = new CustomRecyclerview(getActivity());
        title_txt = (TextView)v.findViewById( R.id. title_txt);
        title_txt.setText( R.string.fishingtips );
        ImageView backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();

            }
        } );

        //StoredObjects.hashmaplist(5);
        //customRecyclerview.Assigndatatorecyleviewhashmap( fishing_tricks_recycle, StoredObjects.dummy_list,"fishing_tricks", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.guide_listitems);


        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        fishing_tricks_recycle.setLayoutManager(linearLayoutManager);
        fishing_tricks_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastvisibleitemposition == adapter.getItemCount() - 1) {
                    if(pageCount<=Integer.parseInt(total_pages)){
                        pageCount=pageCount+1;
                        getguidesdata(pageCount);
                    }


                }
            }
        });


    }






    public class GuideTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            //   CustomProgressbar.Progressbarshow( getActivity() );
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token", "Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method", StoredUrls.user_guide ) );
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

                    // fishbreeds_list = JsonParsing.GetJsonData( results );
                   /* if (fishbreeds_list.size()>0){
                        customRecyclerview.Assigndatatorecyleviewhashmap(fishbreads_recyclerview, fishbreeds_list,"fishbreeds", StoredObjects.Gridview, 2, StoredObjects.ver_orientation, R.layout.fishbreeds_listitems);
                    }
                    else {
                        StoredObjects.ToastMethod( "No Data Found", getActivity());
                    }*/




                    if(pageCount==1){
                        guide_listall.clear();
                        guide_list = JsonParsing.GetJsonData(results);
                        guide_listall.addAll(guide_list);

                        mShimmerViewContainer.setVisibility(View.GONE);
                        fishing_tricks_recycle.setVisibility( View.VISIBLE );
                        nodatafound_lay.setVisibility( View.GONE );

                        adapter = new HashMapRecycleviewadapter(getActivity(),guide_listall,"fishing_tricks",fishing_tricks_recycle,R.layout.guide_listitems );//
                        fishing_tricks_recycle.setAdapter(adapter);


                    }else{

                        guide_list = JsonParsing.GetJsonData(results);
                        guide_listall.addAll(guide_list);
                        StoredObjects.LogMethod("val","val:::"+"yes"+guide_listall.size());
                        adapter.notifyDataSetChanged();


                    }

                }else{

                    if (guide_listall.size()==0){
                        mShimmerViewContainer.setVisibility(View.GONE);
                        fishing_tricks_recycle.setVisibility( View.GONE );
                        nodatafound_lay.setVisibility( View.VISIBLE );
                    }



                    String error = jsonObject.getString( "error" );
                    //StoredObjects.ToastMethod(error,SidemenuIndividual.this);
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
