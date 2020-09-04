package com.fishbuddy.customcamara;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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

public class LogCatchFragment extends Fragment {

    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList PieEntryLabels;


    ArrayList<HashMap<String, String>> fishbreeds_list=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishbreeds_listall=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishbreeds_listall_filter=new ArrayList<>();

    int pageCount = 1;
    int totalpages;
    String total_results ,total_pages= "0";


    RecyclerView landing_recycle;
    CustomRecyclerview customRecyclerview;
    CustomRegularTextView nodatafound_txt;
    LinearLayout nodatafound_lay;
    private ShimmerFrameLayout mShimmerViewContainer;
    ProgressBar progressBar1;

    HashMapRecycleviewadapter adapter;


    Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview  = inflater.inflate(R.layout.checkfishbreedgraph,container,false);

        bundle = getArguments();
        try {
            Log.i("hos_id", "hos_id_addpost_checkbreed:--"+bundle);
            if (bundle != null) {
                fishbreeds_listall = (ArrayList<HashMap<String, String>>) bundle.getSerializable("YourHashMap");


            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        pieChart = rootview.findViewById(R.id.pieChart);
        initialisation(rootview);



        pieEntries = new ArrayList<>();
        for (int i = 0; i <fishbreeds_listall.size() ; i++) {
                        /*HashMap<String, String> dash_hash = new HashMap<String, String>();
                        dash_hash.put("id",fishbreeds_list.get(i).get("id"));*/
            Float aFloat = Float.parseFloat(fishbreeds_listall.get(i).get("keyname_val"));
            aFloat = aFloat/fishbreeds_listall.size();
            pieEntries.add(new PieEntry(aFloat, fishbreeds_listall.get(i).get("species")+"="+fishbreeds_listall.get(i).get("keyname_val")));
        }

        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5f);

        pieDataSet.notifyDataSetChanged();

        StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_result"+fishbreeds_listall.size());

        mShimmerViewContainer.setVisibility(View.GONE);
        landing_recycle.setVisibility( View.VISIBLE );
        //nodatafound_lay.setVisibility( View.GONE );
        adapter = new HashMapRecycleviewadapter(getActivity(),fishbreeds_listall,"identified_breed",landing_recycle,R.layout.identifiedbreed_listitem );//
        landing_recycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        return rootview;
    }



    ImageView backbtn_img;
    public void initialisation(View rootview){
        TextView title_txt = (TextView)rootview.findViewById( R.id. title_txt);
        title_txt.setText("Identified Breed");

        backbtn_img = (ImageView) rootview.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // getFragmentManager().gets` `

            }
        } );



        mShimmerViewContainer =  rootview.findViewById(R.id.shimmer_fishing_sopts_xml);
        landing_recycle = (RecyclerView) rootview.findViewById( R.id.landing_recycle );
        customRecyclerview = new CustomRecyclerview(getActivity());

        nodatafound_txt = (CustomRegularTextView)rootview.findViewById( R.id.nodatafound_txt );
        nodatafound_lay = (LinearLayout)rootview.findViewById( R.id.nodatafound_lay );
        //cordinator_layout = v.findViewById( R.id.cordinator_layout );
        progressBar1 = rootview.findViewById( R.id.progressBar1 );

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        landing_recycle.setLayoutManager(linearLayoutManager);
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
                //  nameValuePairs.add( new BasicNameValuePair( "page", params[1] ) );
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


                    fishbreeds_list.clear();
                    fishbreeds_list = JsonParsing.GetJsonData(results);
                    fishbreeds_listall.clear();
                    for (int i = 0; i <fishbreeds_list.size() ; i++) {

                        for (int j = 0; j <fishbreeds_listall_filter.size() ; j++) {
                            if(fishbreeds_list.get(i).get("species").toLowerCase().contains(fishbreeds_listall_filter.get(j).get("keyname"))){

                                HashMap<String, String> dash_hash = new HashMap<String, String>();
                                dash_hash.put("id",fishbreeds_list.get(i).get("id"));
                                dash_hash.put("species",fishbreeds_list.get(i).get("species"));
                                dash_hash.put("extra_info",fishbreeds_list.get(i).get("extra_info"));
                                dash_hash.put("appearance",fishbreeds_list.get(i).get("appearance"));
                                dash_hash.put("habitat",fishbreeds_list.get(i).get("habitat"));
                                dash_hash.put("behavior",fishbreeds_list.get(i).get("behavior"));
                                dash_hash.put("weight",fishbreeds_list.get(i).get("weight"));
                                dash_hash.put("length",fishbreeds_list.get(i).get("length"));
                                dash_hash.put("edibility",fishbreeds_list.get(i).get("edibility"));
                                dash_hash.put("uk_record",fishbreeds_list.get(i).get("uk_record"));
                                dash_hash.put("bait",fishbreeds_list.get(i).get("bait"));
                                dash_hash.put("season",fishbreeds_list.get(i).get("season"));
                                dash_hash.put("water",fishbreeds_list.get(i).get("water"));
                                dash_hash.put("fishing_methods",fishbreeds_list.get(i).get("fishing_methods"));
                                dash_hash.put("binomial_name",fishbreeds_list.get(i).get("binomial_name"));
                                dash_hash.put("images_count",fishbreeds_list.get(i).get("images_count"));
                                dash_hash.put("images",fishbreeds_list.get(i).get("images"));

                                dash_hash.put("keyname",fishbreeds_listall_filter.get(j).get("keyname"));
                                dash_hash.put("keyname_val",fishbreeds_listall_filter.get(j).get("keyname_val"));

                                fishbreeds_listall.add(dash_hash);
                            }
                        }

                    }

                    // StoredObjects.LogMethod("val","val:::"+"yes<><><>"+fishbreeds_listall.size()+"<><>"+fishbreeds_listall);
                    pieEntries = new ArrayList<>();
                    for (int i = 0; i <fishbreeds_listall.size() ; i++) {
                        /*HashMap<String, String> dash_hash = new HashMap<String, String>();
                        dash_hash.put("id",fishbreeds_list.get(i).get("id"));*/
                        Float aFloat = Float.parseFloat(fishbreeds_listall.get(i).get("keyname_val"));
                        aFloat = aFloat/fishbreeds_listall.size();
                        pieEntries.add(new PieEntry(aFloat, fishbreeds_listall.get(i).get("species")+"="+fishbreeds_listall.get(i).get("keyname_val")));
                    }




                    pieDataSet = new PieDataSet(pieEntries, "");
                    pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    pieDataSet.setSliceSpace(2f);
                    pieDataSet.setValueTextColor(Color.WHITE);
                    pieDataSet.setValueTextSize(10f);
                    pieDataSet.setSliceSpace(5f);

                    pieDataSet.notifyDataSetChanged();

                    StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_result"+fishbreeds_listall.size());

                    mShimmerViewContainer.setVisibility(View.GONE);
                    landing_recycle.setVisibility( View.VISIBLE );
                    //nodatafound_lay.setVisibility( View.GONE );
                    adapter = new HashMapRecycleviewadapter(getActivity(),fishbreeds_listall,"identified_breed",landing_recycle,R.layout.identifiedbreed_listitem );//
                    landing_recycle.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                    /*if (fishbreeds_listall.size() > 0) {
                        mShimmerViewContainer.setVisibility( View.GONE );
                        landing_recycle.setVisibility( View.VISIBLE );
                        nodatafound_lay.setVisibility( View.GONE );
                        adapter = new HashMapRecycleviewadapter(LogcatchClass.this,fishbreeds_listall,"identified_breed",landing_recycle,R.layout.identifiedbreed_listitem );//
                        landing_recycle.setAdapter(adapter);
                    } else {
                        mShimmerViewContainer.setVisibility( View.GONE );
                        landing_recycle.setVisibility( View.GONE );
                        nodatafound_lay.setVisibility( View.VISIBLE );
                    }*/


                }else{

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
