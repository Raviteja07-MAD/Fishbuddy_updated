package com.fishbuddy.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class FishBreeds extends Fragment {
    RecyclerView fishbreads_recyclerview;
    CustomRecyclerview customRecyclerview;
    TextView title_txt;
    ArrayList<HashMap<String, String>> fishbreeds_list=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishbreeds_listall=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishbreeds_listall_filter=new ArrayList<>();


    LinearLayout  nodatafound_lay;

    int pageCount = 1;
    int totalpages;
    String total_results ,total_pages= "0";
    HashMapRecycleviewadapter adapter;

    private ShimmerFrameLayout mShimmerViewContainer;



    EditText fishbreed_search_text;
    ImageView fishbreed_cancel_img;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fishbreeds,null,false );
        StoredObjects.page_type="home";
        StoredObjects.back_type="fish_breeds";
        SideMenu.updatemenu(StoredObjects.page_type);


        initilization(v);
        getfishbreedsdata(pageCount);
        return v;
    }

    public void getfishbreedsdata(int page_count){
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new FishBreddsTask().execute(StoredObjects.UserId,page_count+"");
        }else{
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
        }
    }

    private void initilization(View v) {

        mShimmerViewContainer =  v.findViewById(R.id.shimmer_fishbreeds_xml);

        fishbreads_recyclerview = (RecyclerView) v.findViewById( R.id.fishbreads_recyclerview );
        nodatafound_lay = (LinearLayout) v.findViewById( R.id.nodatafound_lay );



        customRecyclerview = new CustomRecyclerview(getActivity());

        title_txt = (TextView)v.findViewById( R.id. title_txt);
        title_txt.setText( R.string.fishbreads );


         fishbreed_search_text = (EditText)v.findViewById( R.id.fishbreed_search_text );
         fishbreed_cancel_img = (ImageView)v.findViewById( R.id.fishbreed_cancel_img );


        ImageView backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();

            }
        } );

      //  StoredObjects.hashmaplist(8);
         // customRecyclerview.Assigndatatorecyleviewhashmap(fishbreads_recyclerview, StoredObjects.dummy_list,"fishbreeds", StoredObjects.Gridview, 2, StoredObjects.ver_orientation, R.layout.fishbreeds_listitems);


       // final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        final GridLayoutManager linearLayoutManager=new GridLayoutManager(getActivity(),2);
        fishbreads_recyclerview.setLayoutManager(linearLayoutManager);
        //fishbreads_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        fishbreads_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                    //pageCount=pageCount+1;
                    if(pageCount<=Integer.parseInt(total_pages)){
                        pageCount=pageCount+1;
                        getfishbreedsdata(pageCount);
                        StoredObjects.LogMethod("val","val:::"+"yes"+fishbreeds_listall.size());
                    }

                    /*if(Integer.parseInt(total_pages)!= 0){
                        pageCount=pageCount+1;
                        orderhstrymethod(pageCount);

                    }else{
                        // StoredObjects.ToastMethod("All Records Loaded.",getActivity());
                    }*/

                    /*if(labslist_total.size() <= Integer.parseInt(totalRecord)){
                        orderhstrymethod(pageCount);

                    }*/

                }
            }
        });

        fishbreed_cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fishbreed_search_text.setText("");
            }
        });


        fishbreed_search_text.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int textlength = fishbreed_search_text.getText().length();

                fishbreeds_listall_filter.clear();
                for (int i = 0; i < fishbreeds_listall.size(); i++) {
                    if (textlength <= fishbreeds_listall.get(i).get("species").length()) {
                        if (fishbreeds_listall.get(i).get("species").toLowerCase().trim().contains(
                                fishbreed_search_text.getText().toString().toLowerCase().trim())) {
                            fishbreeds_listall_filter.add(fishbreeds_listall.get(i));
                        }
                    }
                }

                if(fishbreeds_listall_filter.size() == 0){
                    fishbreads_recyclerview.setVisibility( View.GONE );
                    nodatafound_lay.setVisibility( View.VISIBLE );
                }else{

                    adapter = new HashMapRecycleviewadapter(getActivity(),fishbreeds_listall_filter,"fishbreeds",fishbreads_recyclerview,R.layout.fishbreeds_listitems );//
                    fishbreads_recyclerview.setAdapter(adapter);

                    fishbreads_recyclerview.setVisibility( View.VISIBLE );
                    nodatafound_lay.setVisibility( View.GONE );
                }

            }
        });



    }

    public class FishBreddsTask extends AsyncTask<String, String, String> {
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
                nameValuePairs.add( new BasicNameValuePair( "method", StoredUrls.fish_breeds ) );
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
                        fishbreeds_listall.clear();
                        fishbreeds_list = JsonParsing.GetJsonData(results);
                        fishbreeds_listall.addAll(fishbreeds_list);

                        mShimmerViewContainer.setVisibility(View.GONE);
                        fishbreads_recyclerview.setVisibility( View.VISIBLE );
                        nodatafound_lay.setVisibility( View.GONE );

                        adapter = new HashMapRecycleviewadapter(getActivity(),fishbreeds_listall,"fishbreeds",fishbreads_recyclerview,R.layout.fishbreeds_listitems );//
                        fishbreads_recyclerview.setAdapter(adapter);


                    }else{

                        fishbreeds_list = JsonParsing.GetJsonData(results);
                        fishbreeds_listall.addAll(fishbreeds_list);
                        StoredObjects.LogMethod("val","val:::"+"yes"+fishbreeds_listall.size());
                        adapter.notifyDataSetChanged();


                    }

                }else{

                    if (fishbreeds_listall.size()==0){
                        mShimmerViewContainer.setVisibility(View.GONE);
                        fishbreads_recyclerview.setVisibility( View.GONE );
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
