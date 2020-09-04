package com.fishbuddy.customcamara;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fishbuddy.R;
import com.fishbuddy.circularimageview.CircularCompletionView;
import com.fishbuddy.custom_lib.CircularStatusView;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class LogcatchClass extends Activity {

    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList<Integer> colors_array_n = new ArrayList<>();
    ArrayList PieEntryLabels;


    ArrayList<HashMap<String, String>> fishbreeds_list=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishbreeds_listall=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishbreeds_listall_filter=new ArrayList<>();

    ArrayList<HashMap<String, String>> fishbreeds_listall_final=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishbreeds_listall_finalnew=new ArrayList<>();

    ArrayList<Float> floatvalus_array=new ArrayList<>();


    ArrayList<HashMap<String, String>> commonspecies_array = new ArrayList<>();

    int pageCount = 1;
    int totalpages;
    String total_results ,total_pages= "0";


    RecyclerView landing_recycle;
    CustomRecyclerview customRecyclerview;
    CustomRegularTextView nodatafound_txt;
    LinearLayout nodatafound_lay;
    private ShimmerFrameLayout mShimmerViewContainer;

    HashMapRecycleviewadapter adapter;
    HashMapRecycleviewadapter adapter1;

    LinearLayout child_layout;
    ImageView camera_image_view;
    ProgressBar progressbar_identified;
    RelativeLayout progressbar_background;

    LinearLayout custom_progress_check;

    CircularStatusView circularStatusView;

    BottomSheetBehavior mBottomSheetBehavior2;

    CircularCompletionView ccv;

    CustomRegularTextView identified_breedname,common_breedname;
    View identified_breedname_view,common_breedname_view;
    LinearLayout  common_spices_recycle_lay,identified_recycle_lay;
    RecyclerView common_spices_recycle;

LinearLayout image_drag_able_lay,image_drag_able_lay_one;

    ImageView image_drag_able;


    String[] colorsarray_string = {"#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0",
            "#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0",
            "#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0",
            "#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0",
            "#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0",
            "#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0","#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0",
            "#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0",
            "#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0",
            "#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0",
            "#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0",
            "#80D95C16","#8033D916","#80CC16D9","#80FFEB3B","#809C27B0","#802745B0"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkbreed_activitymain);//checkfishbreedgraph

        pieChart = findViewById(R.id.pieChart);
        //getEntries();



        Intent intent = getIntent();
        if(intent!=null){
            Bundle args = intent.getBundleExtra("BUNDLE");
            fishbreeds_listall = (ArrayList<HashMap<String, String>>) args.getSerializable("ARRAYLIST");
            commonspecies_array = (ArrayList<HashMap<String, String>>) args.getSerializable("ARRAYLIST1");
        }
        initialisation();


        try{
            File imgFile = new  File(StoredObjects.scanned_imagepath);

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                // mCameraImage.setImageBitmap(myBitmap);

                if(StoredObjects.scanned_imagepath_type.equalsIgnoreCase("camara")){
                    if (myBitmap.getWidth() > myBitmap.getHeight()) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        myBitmap = Bitmap.createBitmap(myBitmap , 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
                    }

                    camera_image_view.setImageBitmap(myBitmap);
                }else{
                    try {
                           camera_image_view.setImageBitmap(CameraActivity.modifyOrientation(myBitmap,imgFile.getAbsolutePath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }





            }
        }catch (Exception e){
            e.printStackTrace();
        }






        //piechartdataasign();





        fishbreeds_listall_final.clear();
        floatvalus_array.clear();
        for (int i = 0; i <fishbreeds_listall.size() ; i++) {

                HashMap<String, String> dash_hash = new HashMap<String, String>();
                dash_hash.put("id",fishbreeds_listall.get(i).get("id"));
                dash_hash.put("species",fishbreeds_listall.get(i).get("species"));
                dash_hash.put("extra_info",fishbreeds_listall.get(i).get("extra_info"));
                dash_hash.put("appearance",fishbreeds_listall.get(i).get("appearance"));
                dash_hash.put("habitat",fishbreeds_listall.get(i).get("habitat"));
                dash_hash.put("behavior",fishbreeds_listall.get(i).get("behavior"));
                dash_hash.put("weight",fishbreeds_listall.get(i).get("weight"));
                dash_hash.put("length",fishbreeds_listall.get(i).get("length"));
                dash_hash.put("edibility",fishbreeds_listall.get(i).get("edibility"));
                dash_hash.put("uk_record",fishbreeds_listall.get(i).get("uk_record"));
                dash_hash.put("bait",fishbreeds_listall.get(i).get("bait"));
                dash_hash.put("season",fishbreeds_listall.get(i).get("season"));
                dash_hash.put("water",fishbreeds_listall.get(i).get("water"));
                dash_hash.put("fishing_methods",fishbreeds_listall.get(i).get("fishing_methods"));
                dash_hash.put("binomial_name",fishbreeds_listall.get(i).get("binomial_name"));
                dash_hash.put("images_count",fishbreeds_listall.get(i).get("images_count"));
                dash_hash.put("images",fishbreeds_listall.get(i).get("images"));

                dash_hash.put("keyname",fishbreeds_listall.get(i).get("keyname"));
                dash_hash.put("keyname_val",fishbreeds_listall.get(i).get("keyname_val"));
            dash_hash.put("colorstrip",colorsarray_string[i]);



            float aFloat = 0;
            try{
                 aFloat = Float.parseFloat(fishbreeds_listall.get(i).get("keyname_val"));
                // aFloat = (aFloat/fishbreeds_listall.size());
               // floatvalus_array.add(aFloat*100);
            }catch (Exception e){

            }

            dash_hash.put("percentageval",""+(aFloat*100));//"Identified:"++"%"
            fishbreeds_listall_final.add(dash_hash);
        }


        for (int i = 0; i <fishbreeds_listall.size() ; i++) {
            Float aFloat = Float.parseFloat(fishbreeds_listall.get(i).get("keyname_val"));
            floatvalus_array.add(aFloat*100);
        }

        Collections.sort(floatvalus_array, Collections.reverseOrder());

        Collections.sort(fishbreeds_listall_final, new Comparator<HashMap< String,String >>() {

            @Override
            public int compare(HashMap<String, String> lhs,
                               HashMap<String, String> rhs) {

                int val1 = StoredObjects.mathfloat(Float.parseFloat(lhs.get("percentageval")));
                int val2 = StoredObjects.mathfloat(Float.parseFloat(rhs.get("percentageval")));
                int result = Float.compare(val1, val2);
                if (result == 0)
                    result = Float.compare(val1, val2);
                return result;
            }
        });
        Collections.reverse(fishbreeds_listall_final);
       // Collections.sort(fishbreeds_listall_final, Collections.reverseOrder());


       /* for (int i = 0; i <fishbreeds_listall.size() ; i++) {
            StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_result"+fishbreeds_listall.get(i));
        }*/


        /*fishbreeds_listall_finalnew.clear();
        for (int i = 0; i <fishbreeds_listall.size() ; i++) {

            for (int j = 0; j <floatvalus_array.size() ; j++) {

                if(fishbreeds_listall.get(i).get("keyname_val").equalsIgnoreCase(floatvalus_array.get(j)+"")){
                    fishbreeds_listall_finalnew.add(fishbreeds_listall.get(i));
                }

            }

        }

        StoredObjects.LogMethod("<><>>","<><><><><>mikel>"+"final_result"+fishbreeds_listall_finalnew.size());


        for (int i = 0; i <fishbreeds_listall_finalnew.size() ; i++) {
            StoredObjects.LogMethod("<><>>","<><><><><>mikel>"+"final_result"+fishbreeds_listall_finalnew.get(i).get("keyname_val"));
        }
*/

       // landing_recycle.setNestedScrollingEnabled(false);
        mShimmerViewContainer.setVisibility(View.GONE);
        landing_recycle.setVisibility( View.VISIBLE );
        //nodatafound_lay.setVisibility( View.GONE );
        adapter = new HashMapRecycleviewadapter(LogcatchClass.this,fishbreeds_listall_final,"identified_breed",landing_recycle,R.layout.identifiedbreed_listitem_new );//
        landing_recycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();






        for (int i = 0; i <floatvalus_array.size() ; i++) {
            StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_result"+floatvalus_array.get(i)+"<><>"+i);

        }

        float [] float_array = {27.0f,21.0f,52.0f,21.0f};
       // int[] colors = {R.color.trorange,R.color.tryellow,R.color.trpink,R.color.trgreen};
         // int[] colors =  {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};
        int[] colors =  {Color.parseColor("#80D95C16"), Color.parseColor("#8033D916"),
                Color.parseColor("#80CC16D9"), Color.parseColor("#80FFEB3B")};
        // int[] colors_lenth =  {30, 200, 200, 40};
        circularStatusView.setPortionsCount(colors.length);
        for (int i = 0; i < colors.length; i++) {
            circularStatusView.setPortionColorForIndex(i, colors[i]);
        }




       /* AnimationDrawable ad = getProgressBarAnimation();
        progressbar_identified.setBackgroundDrawable(ad);*/



        //getfishbreedsdata(pageCount);

        piechartdataasign();
    }




    public void piechartdataasign(){

        pieEntries = new ArrayList<>();

        StoredObjects.LogMethod("<><>>","<><><><><>>"+"internal<><>"+fishbreeds_listall_final.size());

        for (int i = 0; i <fishbreeds_listall_final.size() ; i++) {
                      /*  HashMap<String, String> dash_hash = new HashMap<String, String>();
                        dash_hash.put("id",fishbreeds_list.get(i).get("id"));*/
            Float aFloat = Float.parseFloat(fishbreeds_listall_final.get(i).get("keyname_val"));
            aFloat = (aFloat/fishbreeds_listall_final.size())*100;
            pieEntries.add(new PieEntry(aFloat, fishbreeds_listall_final.get(i).get("species")+"="+aFloat+"%"));//fishbreeds_listall.get(i).get("keyname_val")

            colors_array_n.add(Color.parseColor(colorsarray_string[i]));
        }


       // getEntries();

        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(colors_array_n);
        //pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        //  pieDataSet.setSliceSpace(2f);
        //pieDataSet.setValueTextColor(Color.WHITE);
        //  pieDataSet.setValueTextSize(10f);
        // pieDataSet.setSliceSpace(5f);
        pieDataSet.notifyDataSetChanged();
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        l.getCalculatedLineSizes();

        l.setYOffset(10f);

        pieChart.setTouchEnabled(false);
        pieChart.setDrawSliceText(false); // To remove slice text
        pieChart.setDrawCenterText(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getData().setDrawValues(false);
        pieChart.setDrawMarkers(false); // To remove markers when click
        pieChart.setDrawEntryLabels(false); // To remove labels from piece of pie
        pieChart.getDescription().setEnabled(false); // To remove description of pie

        pieChart.setHoleRadius(80);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setAlpha(0.6f);
        final PieDataSet set = new PieDataSet(pieEntries, "");
        set.setColor(R.color.trgreen);
        set.setDrawValues(false);

        pieChart.getLegend().setEnabled(false);
    }



    public static float getMax(float[] inputArray){
        float maxValue = inputArray[0];
        for(int i=1;i < inputArray.length;i++){
            if(inputArray[i] > maxValue){
                maxValue = inputArray[i];
            }
        }
        return maxValue;
    }
    public static float fmax(final float... values) {
        float max = Float.MIN_VALUE;
        for (final float v : values) {
            max = Math.max(v, max);
        }//ww w  .  j  a v a 2 s . c  o m
        return max;
    }


    ImageView backbtn_img;
    public void initialisation(){
        TextView title_txt = (TextView)findViewById( R.id. title_txt);
        title_txt.setText("Identified Breed");

        backbtn_img = (ImageView) findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();

            }
        } );


        child_layout =  findViewById(R.id.child_layout);
        mShimmerViewContainer =  findViewById(R.id.shimmer_fishing_sopts_xml);
        landing_recycle = (RecyclerView) findViewById( R.id.landing_recycle );
        customRecyclerview = new CustomRecyclerview(this);

        nodatafound_txt = (CustomRegularTextView)findViewById( R.id.nodatafound_txt );
        nodatafound_lay = (LinearLayout)findViewById( R.id.nodatafound_lay );
        //cordinator_layout = v.findViewById( R.id.cordinator_layout );

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(LogcatchClass.this);
        landing_recycle.setLayoutManager(linearLayoutManager);

        camera_image_view = findViewById(R.id.camera_image_view);
        progressbar_identified = findViewById(R.id.progressbar_identified);
        progressbar_background = findViewById(R.id.progressbar_background);

        custom_progress_check = findViewById(R.id.custom_progress_check);


        identified_breedname = findViewById(R.id.identified_breedname);
        common_breedname = findViewById(R.id.common_breedname);
        identified_breedname_view = findViewById(R.id.identified_breedname_view);
        common_breedname_view = findViewById(R.id.common_breedname_view);
        common_spices_recycle_lay = findViewById(R.id.common_spices_recycle_lay);
        identified_recycle_lay = findViewById(R.id.identified_recycle_lay);
        common_spices_recycle = findViewById(R.id.common_spices_recycle);
        image_drag_able_lay = findViewById(R.id.image_drag_able_lay);

        final LinearLayoutManager linearLayoutManager1 =new LinearLayoutManager(LogcatchClass.this);
        common_spices_recycle.setLayoutManager(linearLayoutManager1);





         circularStatusView = findViewById(R.id.circular_status_view);

        image_drag_able = findViewById(R.id.image_drag_able);
        image_drag_able_lay_one = findViewById(R.id.image_drag_able_lay_one);
        LinearLayout bottm_sheet_layout = findViewById(R.id.bottm_sheet_layout);
        mBottomSheetBehavior2 = BottomSheetBehavior.from(bottm_sheet_layout);
        mBottomSheetBehavior2.setPeekHeight(590);


        ccv = findViewById(R.id.ccv);
        /*ccv.setCompletionPercentage(66);
        ccv.setTextSize(16);
        ccv.setStrokeSize(20);*/



        image_drag_able.setVisibility(View.VISIBLE);
        image_drag_able_lay.setVisibility(View.VISIBLE);
        image_drag_able_lay_one.setVisibility(View.VISIBLE);
        mBottomSheetBehavior2.setHideable(false);
       // mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior2.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior2.setPeekHeight(590);
                    image_drag_able.setVisibility(View.VISIBLE);
                    image_drag_able_lay.setVisibility(View.VISIBLE);
                    image_drag_able_lay_one.setVisibility(View.VISIBLE);
                    // fragmentcalling(new HomepageRestaurants());
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior2.setPeekHeight(200);
                    image_drag_able.setVisibility(View.VISIBLE);
                    image_drag_able_lay.setVisibility(View.VISIBLE);
                    image_drag_able_lay_one.setVisibility(View.VISIBLE);
                }
                /*else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    mBottomSheetBehavior2.setPeekHeight(100);
                    image_drag_able.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events

            }
        });

        image_drag_able.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });



        common_spices_recycle_lay.setVisibility(View.GONE);
        identified_recycle_lay.setVisibility(View.VISIBLE);
        common_breedname_view.setBackgroundResource(R.color.yashbg);
        identified_breedname_view.setBackgroundResource(R.color.orange);

        identified_breedname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                common_spices_recycle_lay.setVisibility(View.GONE);
                identified_recycle_lay.setVisibility(View.VISIBLE);
                common_breedname_view.setBackgroundResource(R.color.yashbg);
                identified_breedname_view.setBackgroundResource(R.color.orange);
            }
        });
        common_breedname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identified_recycle_lay.setVisibility(View.GONE);
                common_spices_recycle_lay.setVisibility(View.VISIBLE);
               // common_breedname.setVisibility(View.VISIBLE);
                identified_breedname_view.setBackgroundResource(R.color.yashbg);
                common_breedname_view.setBackgroundResource(R.color.orange);

                adapter1 = new HashMapRecycleviewadapter(LogcatchClass.this,commonspecies_array,"identified_breed_common",common_spices_recycle,R.layout.identifiedbreed_listitem_new );//
                common_spices_recycle.setAdapter(adapter1);
            }
        });







        // circularStatusView.setPortionsCount(2);
       // circularStatusView.setPortionsColor(getResources().getColor(R.color.colorAccent));

       /* int[] colors = {R.color.trorange,R.color.trgreen,R.color.trpink,R.color.trmaroon};


       // int[] colors =  {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};
       // int[] colors_lenth =  {30, 200, 200, 40};
        circularStatusView.setPortionsCount(colors.length);
       // circularStatusView.setPortionWidth(20);
       // circularStatusView.setScrollBarSize(40);
        //circularStatusView.setPortionColorForIndex(2,1);
       // circularStatusView.setPortionColorForIndex(2,1);
        for (int i = 0; i < colors.length; i++) {
           circularStatusView.setPortionColorForIndex(i, colors[i]);
            //circularStatusView.setFadingEdgeLength(20);
           // circularStatusView.setPortionWidth(colors_lenth[i]);
            *//*Paint p = new Paint();
            p.setAntiAlias(true);
            p.setAntiAlias(true);
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(10);
            circularStatusView.setCustomPaint(circularStatusView);*//*
        }*/

    }

    public void getfishbreedsdata(int page_count){
        if (InterNetChecker.isNetworkAvailable(LogcatchClass.this)) {
            new FishBreddsTask().execute(StoredObjects.UserId,page_count+"");
        }else{
            StoredObjects.ToastMethod(getResources().getString(R.string.checkinternet),LogcatchClass.this);
        }
    }

    private void getEntries() {
        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(2f, 0));
        pieEntries.add(new PieEntry(4f, 1));
        pieEntries.add(new PieEntry(6f, 2));
        pieEntries.add(new PieEntry(8f, 3));
        pieEntries.add(new PieEntry(7f, 4));
        pieEntries.add(new PieEntry(3f, 5));
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
                    adapter = new HashMapRecycleviewadapter(LogcatchClass.this,fishbreeds_listall,"identified_breed",landing_recycle,R.layout.identifiedbreed_listitem );//
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



       /* Collections.sort(fishbreeds_listall_final, new Comparator<HashMap< String,String >>() {

            @Override
            public int compare(HashMap<String, String> lhs,
                               HashMap<String, String> rhs) {
                // Do your comparison logic here and retrn accordingly.
                Integer first = (float)
                Integer second = (float)e2.getValue();
                return first.compareTo( second );
               // return 0;
            }
        });*/




      /*  try {

        int maxval = Math.round(Collections.max(floatvalus_array));
        progressbar_identified.setProgress(maxval);
        StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_result"+fishbreeds_listall_final.size()+"<<>>"+Collections.max(floatvalus_array));
    }catch (Exception e){
        e.printStackTrace();
    }
*/

}
