package com.fishbuddy.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fishbuddy.Gps.GPSTracker;
import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
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
import java.util.HashMap;
import java.util.List;

public class FishingClubs extends Fragment {
    RecyclerView fishingspot_recyclerview;
    CustomRecyclerview customRecyclerview;
    TextView title_txt;


    int pageCount = 1;
    String page_type = "all";
    int totalpages;
    String total_results = "0" ,total_pages = "0";
    HashMapRecycleviewadapter adapter;


    ArrayList<HashMap<String, String>> fishspot_list=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishspot_listall=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishspot_listall_filter=new ArrayList<>();
    private ShimmerFrameLayout mShimmerViewContainer;
    LinearLayout nodatafound_lay;


    EditText fishbreed_search_text;
    ImageView fishbreed_cancel_img;
    ImageView filter_icon;


    private ListPopupWindow listPopupWindow;
  //  String[] filter_array = {"All","Near Me"};
    String[] filter_array = {"All","Near By 5 KM","Near By 10 KM","Near By 20 KM","Near By 50 KM","Near By 100 KM"};

    GPSTracker gpsTracker;

    double lattitude  = 0;
    double longitude  = 0;

    String distance = "";
    String search_text = "";

    CustomRegularTextView nodatafound_txt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fishingspots,null,false );
        StoredObjects.page_type="fishingclubs";
        StoredObjects.back_type="fishingclubs";
        SideMenu.updatemenu(StoredObjects.page_type);
        gpsTracker = new GPSTracker(getActivity(),getActivity());
        listPopupWindow = new ListPopupWindow (getActivity());


        lattitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        StoredObjects.LogMethod( "response", "response:---" + lattitude+"<><><>"+ longitude);

        initilization(v);

        getfishspotsdata(pageCount,page_type);
        return v;
    }
    public void getfishspotsdata(int page_count,String type_val){
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new FishingSpotsTask().execute(StoredObjects.UserId,page_count+"",type_val);
        }else{
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
        }
    }
    private void initilization(View v) {


        mShimmerViewContainer =  v.findViewById(R.id.shimmer_fishing_sopts_xml);
        nodatafound_lay = (LinearLayout) v.findViewById( R.id.nodatafound_lay );
        nodatafound_txt =  v.findViewById( R.id.nodatafound_txt );
        nodatafound_txt.setText(getActivity().getResources().getString(R.string.no_fish_clubs_found));

        fishbreed_search_text = (EditText)v.findViewById( R.id.fishbreed_search_text );
        fishbreed_cancel_img = (ImageView)v.findViewById( R.id.fishbreed_cancel_img );
        filter_icon = v.findViewById( R.id.filter_icon );


        fishingspot_recyclerview = (RecyclerView) v.findViewById( R.id.fishingspot_recyclerview );
        customRecyclerview = new CustomRecyclerview(getActivity());

        title_txt = (TextView)v.findViewById( R.id. title_txt);
        title_txt.setText( R.string.fishingclubs );

        ImageView backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();

            }
        } );

        //StoredObjects.hashmaplist(4);
        // customRecyclerview.Assigndatatorecyleviewhashmap(fishingspot_recyclerview, StoredObjects.dummy_list,"fishingspots", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.fishingspots_listitem);

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        fishingspot_recyclerview.setLayoutManager(linearLayoutManager);


        fishingspot_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                    //pageCount=pageCount+1;
                    if(pageCount<Integer.parseInt(total_pages)){
                        pageCount=pageCount+1;
                        if (InterNetChecker.isNetworkAvailable(getActivity())) {
                            new FishingSpotsloadTask().execute(StoredObjects.UserId,pageCount+"",page_type);
                        }else{
                            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                        }

                    }


                }
            }
        });


        filter_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterpopup(filter_icon);
            }
        });






        fishbreed_cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fishbreed_search_text.setText("");
                search_text = "";
                page_type = "all";
                distance = "";
                pageCount = 1;
                getfishspotsdata(pageCount,page_type);
            }
        });


        fishbreed_search_text.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if(fishbreed_search_text.getText().toString().trim().length() ==0){
                        StoredObjects.ToastMethod("Please Enter Fishing Spot Name to Search",getActivity());
                    }else{
                        StoredObjects.hide_keyboard(getActivity());
                        search_text = fishbreed_search_text.getText().toString().trim();
                        pageCount = 1;
                        getfishspotsdata(pageCount,page_type);
                    }

                    return true;
                }
                return false;
            }
        });



       /* fishbreed_search_text.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int textlength = fishbreed_search_text.getText().length();
                String search_txt = fishbreed_search_text.getText().toString().toLowerCase().trim();

                fishspot_listall_filter.clear();
                for (int i = 0; i < fishspot_listall.size(); i++) {
                    if (textlength <= fishspot_listall.get(i).get("name").length()) {
                        if (fishspot_listall.get(i).get("name").toLowerCase().trim().contains(search_txt)||fishspot_listall.get(i).get("address").toLowerCase().trim().contains(search_txt)) {
                            fishspot_listall_filter.add(fishspot_listall.get(i));
                        }
                    }
                }

                if(fishspot_listall_filter.size() == 0){
                    fishingspot_recyclerview.setVisibility( View.GONE );
                    nodatafound_lay.setVisibility( View.VISIBLE );
                }else{

                    adapter = new HashMapRecycleviewadapter(getActivity(),fishspot_listall_filter,"fishingspots",fishingspot_recyclerview,R.layout.fishingspots_listitem );//
                    fishingspot_recyclerview.setAdapter(adapter);

                    fishingspot_recyclerview.setVisibility( View.VISIBLE );
                    nodatafound_lay.setVisibility( View.GONE );
                }




            }
        });
*/
    }


    private void filterpopup(final ImageView prfilenme){

        listPopupWindow.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.filter_listpopupwindow,filter_array));//android.R.layout.simple_list_item_1
        listPopupWindow.setAnchorView(prfilenme);
       // int width = 300;// = mContext.getResources().getDimensionPixelSize(R.dimen.overflow_width);
      //  listPopupWindow.setWidth(width);
        listPopupWindow.setWidth(LinearLayout.MarginLayoutParams.MATCH_PARENT);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // prfilenme.setText(StoredObjects.familypersonname.get(position));
                pageCount = 1;
                if(position == 0){
                    page_type = "all";
                    distance = "";
                    getfishspotsdata(pageCount,page_type);
                }
                if(position == 1){
                    page_type = "near";
                    distance = "5";
                    getfishspotsdata(pageCount,page_type);
                }
                if(position == 2){
                    page_type = "near";
                    distance = "10";
                    getfishspotsdata(pageCount,page_type);
                }
                if(position == 3){
                    page_type = "near";
                    distance = "20";
                    getfishspotsdata(pageCount,page_type);
                }
                if(position == 4){
                    page_type = "near";
                    distance = "50";
                    getfishspotsdata(pageCount,page_type);
                }
                if(position == 5){
                    page_type = "near";
                    distance = "100";
                    getfishspotsdata(pageCount,page_type);
                }
                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }



    public class FishingSpotsTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            if(search_text.length()>0){
                CustomProgressbar.Progressbarshow( getActivity() );
            }
            //   CustomProgressbar.Progressbarshow( getActivity() );
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token", "Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method", StoredUrls.fishing_clubs ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id", params[0] ) );
                nameValuePairs.add( new BasicNameValuePair( "page", params[1] ) );


                if(params[2].equalsIgnoreCase("near")){
                    nameValuePairs.add( new BasicNameValuePair( "lat", lattitude+"" ) );
                    nameValuePairs.add( new BasicNameValuePair( "lng", longitude+"") );
                    nameValuePairs.add( new BasicNameValuePair( "distance", distance ) );
                }

                nameValuePairs.add( new BasicNameValuePair( "search_text", search_text ) );

                StoredObjects.LogMethod( "response", "nameValuePairs:---" + nameValuePairs );
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
                if(search_text.length()>0) {
                    CustomProgressbar.Progressbarcancel(getActivity());
                }
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    total_results = jsonObject.getString( "total_results" );
                    total_pages = jsonObject.getString( "total_pages" );

                    if(pageCount==1){
                        fishspot_listall.clear();
                        fishspot_list = JsonParsing.GetJsonData(results);
                        fishspot_listall.addAll(fishspot_list);

                        if(fishspot_listall.size()>0){
                            mShimmerViewContainer.setVisibility(View.GONE);
                            fishingspot_recyclerview.setVisibility( View.VISIBLE );
                            nodatafound_lay.setVisibility( View.GONE );
                            adapter = new HashMapRecycleviewadapter(getActivity(),fishspot_listall,"fishingspots",fishingspot_recyclerview,R.layout.fishingspots_listitem );//
                            fishingspot_recyclerview.setAdapter(adapter);
                        }else{
                            fishspot_listall.clear();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            fishingspot_recyclerview.setVisibility( View.GONE );
                            nodatafound_lay.setVisibility( View.VISIBLE );
                        }




                    }else{

                        fishspot_list = JsonParsing.GetJsonData(results);
                        fishspot_listall.addAll(fishspot_list);

                        mShimmerViewContainer.setVisibility(View.GONE);
                        fishingspot_recyclerview.setVisibility( View.VISIBLE );
                        nodatafound_lay.setVisibility( View.GONE );


                        StoredObjects.LogMethod("val","val:::"+"yes"+fishspot_listall.size());
                        adapter = new HashMapRecycleviewadapter(getActivity(),fishspot_listall,"fishingspots",fishingspot_recyclerview,R.layout.fishingspots_listitem );//
                        fishingspot_recyclerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        fishingspot_recyclerview.invalidate();
                    }

                }
                else if (status.equalsIgnoreCase( "500" )) {

                    fishspot_listall.clear();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    fishingspot_recyclerview.setVisibility( View.GONE );
                    nodatafound_lay.setVisibility( View.VISIBLE );

                    /*if(fishspot_list.size() >0) {
                        mShimmerViewContainer.setVisibility(View.GONE);
                        fishingspot_recyclerview.setVisibility( View.VISIBLE );
                        nodatafound_lay.setVisibility( View.GONE );
                        adapter = new HashMapRecycleviewadapter(getActivity(),fishspot_listall,"fishingspots",fishingspot_recyclerview,R.layout.fishingspots_listitem );//
                        fishingspot_recyclerview.setAdapter(adapter);
                    }else{
                        mShimmerViewContainer.setVisibility(View.GONE);
                        fishingspot_recyclerview.setVisibility( View.GONE );
                        nodatafound_lay.setVisibility( View.VISIBLE );
                    }*/

                }else{
                    mShimmerViewContainer.setVisibility( View.GONE );
                    fishingspot_recyclerview.setVisibility( View.GONE );
                    nodatafound_lay.setVisibility( View.VISIBLE );
                }

                /*if (fishspot_list.size() > 0) {
                    mShimmerViewContainer.setVisibility( View.GONE );
                    fishingspot_recyclerview.setVisibility( View.VISIBLE );
                    nodatafound_lay.setVisibility( View.GONE );
                    adapter = new HashMapRecycleviewadapter(getActivity(),fishspot_listall,"fishingspots",fishingspot_recyclerview,R.layout.fishingspots_listitem );//
                    fishingspot_recyclerview.setAdapter(adapter);
                } else {
                    mShimmerViewContainer.setVisibility( View.GONE );
                    fishingspot_recyclerview.setVisibility( View.GONE );
                    nodatafound_lay.setVisibility( View.VISIBLE );
                }*/


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

    public class FishingSpotsloadTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
          ///  CustomProgressbar.Progressbarshow( getActivity() );
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token", "Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method", StoredUrls.fishing_clubs ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id", params[0] ) );
                nameValuePairs.add( new BasicNameValuePair( "page", params[1] ) );

                if(params[2].equalsIgnoreCase("near")){
                    nameValuePairs.add( new BasicNameValuePair( "lat", lattitude+"" ) );
                    nameValuePairs.add( new BasicNameValuePair( "lng", longitude+"") );
                    nameValuePairs.add( new BasicNameValuePair( "distance", distance ) );
                }

                nameValuePairs.add( new BasicNameValuePair( "search_text", search_text ) );

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
               /// CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    total_results = jsonObject.getString( "total_results" );
                    total_pages = jsonObject.getString( "total_pages" );

                    if(pageCount==1){
                        fishspot_listall.clear();
                        fishspot_list = JsonParsing.GetJsonData(results);
                        fishspot_listall.addAll(fishspot_list);
                        mShimmerViewContainer.setVisibility(View.GONE);
                        fishingspot_recyclerview.setVisibility( View.VISIBLE );
                        nodatafound_lay.setVisibility( View.GONE );
                        adapter = new HashMapRecycleviewadapter(getActivity(),fishspot_listall,"fishingspots",fishingspot_recyclerview,R.layout.fishingspots_listitem );//
                        fishingspot_recyclerview.setAdapter(adapter);

                    }else{
                        fishspot_list = JsonParsing.GetJsonData(results);
                        fishspot_listall.addAll(fishspot_list);
                        StoredObjects.LogMethod("val","val:::"+"yes"+fishspot_listall.size());
                        adapter.notifyDataSetChanged();
                        fishingspot_recyclerview.invalidate();
                    }
                }
                else if(status.equalsIgnoreCase( "500" )){
                    if(fishspot_listall.size() == 0){
                        fishingspot_recyclerview.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        fishingspot_recyclerview.invalidate();
                        //nodatafound_txt.setVisibility(View.VISIBLE);
                    }else{
                        fishingspot_recyclerview.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        fishingspot_recyclerview.invalidate();
                        //nodatafound_txt.setVisibility(View.GONE);
                        //  mShimmerViewContainer.setVisibility(View.GONE);
                    }
                }else{
                    if(fishspot_listall.size() == 0){
                        fishingspot_recyclerview.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        fishingspot_recyclerview.invalidate();
                        //nodatafound_txt.setVisibility(View.VISIBLE);
                    }else{
                        fishingspot_recyclerview.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        fishingspot_recyclerview.invalidate();
                        //nodatafound_txt.setVisibility(View.GONE);
                        //  mShimmerViewContainer.setVisibility(View.GONE);
                    }
                }


                /*else if (status.equalsIgnoreCase( "500" )) {

                    if(fishspot_list.size() >0) {
                        mShimmerViewContainer.setVisibility(View.GONE);
                        fishingspot_recyclerview.setVisibility( View.VISIBLE );
                        nodatafound_lay.setVisibility( View.GONE );
                        adapter = new HashMapRecycleviewadapter(getActivity(),fishspot_listall,"fishingspots",fishingspot_recyclerview,R.layout.fishingspots_listitem );//
                        fishingspot_recyclerview.setAdapter(adapter);
                    }else{
                        mShimmerViewContainer.setVisibility(View.GONE);
                        fishingspot_recyclerview.setVisibility( View.GONE );
                        nodatafound_lay.setVisibility( View.VISIBLE );
                    }

                }

                if (fishspot_list.size() > 0) {
                    mShimmerViewContainer.setVisibility( View.GONE );
                    fishingspot_recyclerview.setVisibility( View.VISIBLE );
                    nodatafound_lay.setVisibility( View.GONE );
                    adapter = new HashMapRecycleviewadapter(getActivity(),fishspot_listall,"fishingspots",fishingspot_recyclerview,R.layout.fishingspots_listitem );//
                    fishingspot_recyclerview.setAdapter(adapter);
                } else {
                    mShimmerViewContainer.setVisibility( View.GONE );
                    fishingspot_recyclerview.setVisibility( View.GONE );
                    nodatafound_lay.setVisibility( View.VISIBLE );
                }*/


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
