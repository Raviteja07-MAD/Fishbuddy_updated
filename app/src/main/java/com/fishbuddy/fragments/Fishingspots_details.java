package com.fishbuddy.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.FishingClubsAdapter;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
import com.fishbuddy.customadapter.PhotoAdapter;
import com.fishbuddy.customfonts.CustomBoldTextView;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import static com.fishbuddy.fragments.Comments.customRecyclerview;

public class Fishingspots_details extends Fragment {
    TextView title_txt;
    LinearLayout locate_lake_lay;
    RecyclerView multiple_images_recycle,multiple_fishbreeds_recycle;

    Bundle bundle;
    ArrayList<HashMap<String, String>> fishspotdetails;
    int position;

    public static HashMapRecycleviewadapter adapter;
    ImageView lake_image;
    CustomRegularTextView lakedetails_txt,website_txt,email_txt,phone_txt,address_value_text,fishing_methods;

    LinearLayout address_value_text_lay,phone_txt_lay,email_txt_lay,website_txt_lay;

    LinearLayout fishingspot_recyclerview_lay;
    CustomBoldTextView fishingspots_title_text;
    RecyclerView fishingspot_recyclerview;

    ArrayList<HashMap<String, String>> fish_breeds_list = new ArrayList<>();

    ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
    ArrayList<HashMap<String, String>> fishing_clubs_list = new ArrayList<>();

    CustomRegularTextView no_fishing_clubs_text;

    HashMapRecycleviewadapter  hashMapRecycleviewadapter;
    PhotoAdapter hashMapRecycleviewadapter123;
    FishingClubsAdapter fishingClubsAdapter;

    LinearLayout fishbreeds_layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fishingspotsdetails,null,false );
        StoredObjects.page_type="fishingspotsdetails";
        StoredObjects.back_type="fishingspotsdetails";
        SideMenu.updatemenu(StoredObjects.page_type);


        bundle = getArguments();
        try {
            Log.i("hos_id", "hos_id:--"+bundle);
            if (bundle != null) {
                fishspotdetails = (ArrayList<HashMap<String, String>>) bundle.getSerializable("YourHashMap");
                position = bundle.getInt( "position" );

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        initilization(v);
        dataasign();


        return v;
    }


    public  void dataasign(){


        try{
            title_txt.setText(StoredObjects.stripHtml(fishspotdetails.get(position).get("name")));
            website_txt.setText(StoredObjects.stripHtml(fishspotdetails.get(position).get("website")));
            email_txt.setText(StoredObjects.stripHtml(fishspotdetails.get(position).get("email")));
            phone_txt.setText(StoredObjects.stripHtml(fishspotdetails.get(position).get("phone")));
            address_value_text.setText(StoredObjects.stripHtml(fishspotdetails.get(position).get("address")));
            lakedetails_txt.setText(StoredObjects.stripHtml(fishspotdetails.get(position).get("description")));
            //area_count_txt.setText(fishspotdetails.get(position).get("lat"));
            //maxdepth_count_txt.setText(fishspotdetails.get(position).get("lat"));
            //lakelocation_txt.setText(fishspotdetails.get(position).get("address"));
        }catch (Exception e){
            e.printStackTrace();
        }




        try{
            fishbreeds_layout.setVisibility(View.VISIBLE);
            fish_breeds_list.clear();
            fish_breeds_list = JsonParsing.GetJsonData(fishspotdetails.get( position ).get( "fish_breeds_list" ));
            if (fish_breeds_list.size()>0){
                multiple_fishbreeds_recycle.setVisibility( View.VISIBLE );
                fishing_methods.setVisibility(View.GONE);

                multiple_fishbreeds_recycle.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                hashMapRecycleviewadapter = new HashMapRecycleviewadapter(getActivity(),fish_breeds_list,"fishingspots_fishbreeds",multiple_fishbreeds_recycle,R.layout.fishbreeds_small_layout);//
                multiple_fishbreeds_recycle.setAdapter(hashMapRecycleviewadapter);
               // customRecyclerview.Assigndatatorecyleviewhashmap(multiple_fishbreeds_recycle,fish_breeds_list,"fishingspots_fishbreeds", StoredObjects.Gridview, 2, StoredObjects.ver_orientation, R.layout.fishbreeds_small_layout);
            }
            else {
                fishing_methods.setVisibility(View.VISIBLE);
                multiple_fishbreeds_recycle.setVisibility( View.VISIBLE );

            }

        }catch (Exception e){
            fishbreeds_layout.setVisibility(View.GONE);
            StoredObjects.LogMethod("status_res", "images_array_fishingspots:--"+e);
        }


        images_array.clear();
        try{
            images_array = JsonParsing.GetJsonData(fishspotdetails.get( position ).get( "images" ));
        }catch (Exception e){

        }

        if(images_array.size() == 0){
            try{
                images_array = JsonParsing.GetJsonData(fishspotdetails.get( position ).get( "fishing_club_images" ));
            }catch (Exception e){

            }
        }
        if(images_array.size() == 0) {
            try {
                images_array = JsonParsing.GetJsonData(fishspotdetails.get(position).get("fishing_spot_images"));
            } catch (Exception e) {

            }
        }

        try {


            if(images_array.size()>0){
                Glide.with( getActivity() )
                        .load( Uri.parse( StoredUrls.Uploadedimages+images_array.get(0).get( "image" ) ) ) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.splash_logo_new )
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        // .crossFade()
                        //.override(600,600)
                        .into( lake_image );
            }
            if (images_array.size()>0){
                multiple_images_recycle.setVisibility( View.VISIBLE );

                multiple_images_recycle.setLayoutManager(new LinearLayoutManager(getActivity(),StoredObjects.horizontal_orientation,false));
                hashMapRecycleviewadapter123 = new PhotoAdapter(getActivity(),images_array,"fishingspots_multipleimages",multiple_images_recycle,R.layout.multipleimageslistitem_fishingspots);//
                multiple_images_recycle.setAdapter(hashMapRecycleviewadapter123);
                //customRecyclerview.Assigndatatorecyleviewhashmap(multiple_images_recycle,images_array,"fishingspots_multipleimages", StoredObjects.Listview, 0, StoredObjects.horizontal_orientation, R.layout.multipleimageslistitem_fishingspots);
            }
            else {
                multiple_images_recycle.setVisibility( View.GONE );
            }
        } catch (Exception e) {
            StoredObjects.LogMethod("status_res", "images_array_fishingspots:--"+e);
        }


        StoredObjects.LogMethod("status_res", "images_array_fishingspots:--<><sizeinneer>"+images_array.size()+"<><>"+fish_breeds_list.size());



        try{

            fishingspot_recyclerview_lay.setVisibility( View.VISIBLE );
            fishing_clubs_list.clear();
            fishing_clubs_list = JsonParsing.GetJsonData(fishspotdetails.get( position ).get( "fishing_clubs_list" ));
            if (fishing_clubs_list.size()>0){
                no_fishing_clubs_text.setVisibility( View.GONE );
                fishingspot_recyclerview.setVisibility(View.VISIBLE);
                fishingspot_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                fishingClubsAdapter = new FishingClubsAdapter(getActivity(),fishing_clubs_list,"fishingspots",fishingspot_recyclerview,R.layout.fishingspots_listitem );//
                fishingspot_recyclerview.setAdapter(fishingClubsAdapter);

                // customRecyclerview.Assigndatatorecyleviewhashmap(multiple_fishbreeds_recycle,fish_breeds_list,"fishingspots_fishbreeds", StoredObjects.Gridview, 2, StoredObjects.ver_orientation, R.layout.fishbreeds_small_layout);
            }
            else {
                no_fishing_clubs_text.setVisibility( View.VISIBLE );
                fishingspot_recyclerview.setVisibility(View.GONE);

            }

        }catch (Exception e){
            fishingspot_recyclerview_lay.setVisibility( View.GONE );
            StoredObjects.LogMethod("status_res", "images_array_fishingspots:--"+e);
        }




        /*
        try{


            ArrayList<HashMap<String, String>> fishings_spots_list = new ArrayList<>();
            fishings_spots_list.clear();

        try {
            fishings_spots_list = JsonParsing.GetJsonData(fishspotdetails.get( position ).get( "fishings_spots_list" ));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StoredObjects.LogMethod("status_res", "images_array_fishingspots_size:--"+fishings_spots_list.size());

            final LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(getActivity());
            fishingspot_recyclerview.setLayoutManager(linearLayoutManager2);
            if (fishings_spots_list.size()>0){
                // postimage.setVisibility( View.VISIBLE );
                fishingspot_recyclerview.setVisibility( View.VISIBLE );
                fishingspot_recyclerview_lay.setVisibility(View.VISIBLE);
                adapter = new HashMapRecycleviewadapter(getActivity(),fishings_spots_list,"fishingspots",fishingspot_recyclerview,R.layout.fishingspots_listitem );//
                fishingspot_recyclerview.setAdapter(adapter);


            }
            else {
                fishingspot_recyclerview.setVisibility( View.GONE );
                fishingspot_recyclerview_lay.setVisibility(View.GONE);

            }
        }catch (Exception e){
            fishingspot_recyclerview_lay.setVisibility(View.GONE);
            StoredObjects.LogMethod("status_res", "images_array_fishingspots_spots<3>:--"+e);
        }


*/


    }

    private void initilization(View v) {

        customRecyclerview = new CustomRecyclerview(getActivity());

         title_txt = (TextView)v.findViewById( R.id. title_txt);
        title_txt.setText( R.string.lakename );


        lake_image = v.findViewById( R.id. lake_image);
        lakedetails_txt = v.findViewById( R.id. lakedetails_txt);
        website_txt = v.findViewById( R.id. website_txt);
        email_txt = v.findViewById( R.id. email_txt);
        phone_txt = v.findViewById( R.id. phone_txt);


        address_value_text_lay = v.findViewById( R.id. address_value_text_lay);
        phone_txt_lay = v.findViewById( R.id. phone_txt_lay);
        email_txt_lay = v.findViewById( R.id. email_txt_lay);
        website_txt_lay = v.findViewById( R.id. website_txt_lay);




        address_value_text = v.findViewById(R.id.address_value_text);
        fishing_methods = v.findViewById(R.id.fishing_methods);


        multiple_images_recycle = (RecyclerView)v.findViewById( R.id.multiple_images_recycle );

        multiple_fishbreeds_recycle = (RecyclerView)v.findViewById( R.id.multiple_fishbreeds_recycle );


        fishingspot_recyclerview = (RecyclerView)v.findViewById( R.id.fishingspot_recyclerview );
        fishingspot_recyclerview_lay = (LinearLayout) v.findViewById( R.id.fishingspot_recyclerview_lay );
        fishingspots_title_text = v.findViewById( R.id.fishingspots_title_text );


        no_fishing_clubs_text = v.findViewById( R.id.no_fishing_clubs_text );
        fishbreeds_layout = v.findViewById(R.id.fishbreeds_layout);
        /* LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(getActivity());
        multiple_fishbreeds_recycle.setLayoutManager(linearLayoutManager2);


         LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getActivity());
        multiple_images_recycle.setLayoutManager(linearLayoutManager1);
*/


        ImageView backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        } );

        locate_lake_lay = (LinearLayout)v.findViewById( R.id.locate_lake_lay );
        locate_lake_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //String url = "https://www.google.com/maps/dir/?api=1&destination=" + fishspotdetails.get(position).get("lat") + "° N"+"," + fishspotdetails.get(position).get("lng")+"° W" /*"&travelmode=driving"*/;

                    String url = "https://www.google.com/maps/dir/?api=1&destination=" + fishspotdetails.get(position).get("lat") +"," + fishspotdetails.get(position).get("lng") /*"&travelmode=driving"*/;

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    getContext().startActivity(intent);
                }catch (Exception e){
                    StoredObjects.ToastMethod("No Lakes found",getActivity());
                }

            }
        } );


        address_value_text_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //String url = "https://www.google.com/maps/dir/?api=1&destination=" + fishspotdetails.get(position).get("lat") + "° N"+"," + fishspotdetails.get(position).get("lng")+"° W" /*"&travelmode=driving"*/;

                    String url = "https://www.google.com/maps/dir/?api=1&destination=" + fishspotdetails.get(position).get("lat") +"," + fishspotdetails.get(position).get("lng") /*"&travelmode=driving"*/;

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    getContext().startActivity(intent);
                }catch (Exception e){
                    StoredObjects.ToastMethod("No Lakes found",getActivity());
                }
            }
        });



        phone_txt_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    StoredObjects.AlertForCall(getActivity(),StoredObjects.stripHtml(fishspotdetails.get(position).get("phone"))+"");//"customer_care_number"

                   /* Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + bundle.getString(StoredObjects.stripHtml(fishspotdetails.get(position).get("phone"))+"")));
                    startActivity(intent);*/
                }catch (Exception e){

                }

            }
        });
        website_txt_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                    httpIntent.setData(Uri.parse(StoredObjects.stripHtml(fishspotdetails.get(position).get("website"))+""));

                    startActivity(httpIntent);
                }catch (Exception e){

                }

            }
        });
        email_txt_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Uri uri = Uri.parse("mailto:" + StoredObjects.stripHtml(fishspotdetails.get(position).get("email")))
                        .buildUpon()
                       // .appendQueryParameter("subject", subject)
                        //.appendQueryParameter("body", body)
                        .build();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(emailIntent);

            }
        });

    }
}
