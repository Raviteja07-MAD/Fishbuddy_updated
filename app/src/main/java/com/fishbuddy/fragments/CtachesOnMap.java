package com.fishbuddy.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fishbuddy.Gps.GPSTracker;
import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.PlaceArrayAdapter;
import com.fishbuddy.customfonts.CustomButton;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.servicesparsing.CustomProgressbar;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

import static android.os.Build.VERSION.SDK_INT;
import static com.fishbuddy.sidemenu.SideMenu.btm_map_img;
import static com.fishbuddy.sidemenu.SideMenu.btm_map_lay;
import static com.fishbuddy.sidemenu.SideMenu.btm_map_txt;
import static com.fishbuddy.sidemenu.SideMenu.buttonchangemethod;

public class CtachesOnMap extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    //CustomTextView rgstr_curent_customer, rgstr_not_curent_customer;
    RadioButton rgstr_customer_radio_btn, rgstr_not_customer_radio_btn;
    CustomButton rgstr_btn;
    ImageView rgstr_mapfltr_img;

    Context mContext;
    GPSTracker gpsTracker;
    Double latitude_value = 0.0;
    Double langitude_value = 0.0;
    MarkerOptions othermarkerOptions;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private HashMap<String, String> markerNames = new HashMap<>();
    private HashMap<String, String> fishingspots = new HashMap<>();
    private HashMap<String, String> markerAddress = new HashMap<>();
    private HashMap<String, String> markerIDs = new HashMap<>();
    private HashMap<String, String> markerImages = new HashMap<>();
    private HashMap<String, String> marker_descriptionn = new HashMap<>();
  /*  private HashMap<String, String> marker_lattitude = new HashMap<>();
    private HashMap<String, String> marker_longitude = new HashMap<>();*/

    public ArrayList<HashMap<String, String>> wholesalerslist = new ArrayList<>();
    public ArrayList<HashMap<String, String>> selctdwholesalerslist = new ArrayList<>();
    String state_id = "",userlat= "17.3685",userlong = "78.5316";
    LinearLayout nocurntcustomer_lay;
    CustomRecyclerview customRecyclerview;

    //   public static CustomTextView wholesalers_name;


    String latval = "";
    String lngval = "";
    Double lat = 0.0;
    Double lng = 0.0;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();



    int pageCount = 1;
    int totalpages;
    String total_results ,total_pages= "0";
    ArrayList<HashMap<String, String>> fishspot_list=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishspot_listall=new ArrayList<>();


    double lattitude  = 0;
    double longitude  = 0;

    String page_type = "all";

    CustomButton loadmore_btn;

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage((FragmentActivity) mContext);
            mGoogleApiClient.disconnect();
        }
    }

    Bundle bundle;
    int position;
    ArrayList<HashMap<String, String>> postslist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.map, container, false);
        StoredObjects.page_type="home";
        StoredObjects.back_type="map";
        buttonchangemethod (getActivity() , btm_map_lay , btm_map_img , btm_map_txt , "2");
        SideMenu.updatemenu(StoredObjects.page_type);
        mContext = this.getActivity();
        customRecyclerview = new CustomRecyclerview(getActivity());


        bundle = getArguments();
        try {
            Log.i("hos_id", "hos_id:--"+bundle);
            if (bundle != null) {
                postslist = (ArrayList<HashMap<String, String>>) bundle.getSerializable("YourHashMap");
                position = bundle.getInt( "position" );

            }

        } catch (Exception e) {
            // TODO: handle exception
        }


        intialization(v);


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi( Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);


        assigndatatomap(postslist);


        return v;
    }



    public void intialization(View v) {


        TextView title_txt = (TextView)v.findViewById( R.id. title_txt);
       // title_txt.setText( R.string.map );
        title_txt.setText( R.string.catches_onmap );

        loadmore_btn = v.findViewById(R.id.loadmore_btn);

        loadmore_btn.setVisibility(View.GONE);

        ImageView backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();

            }
        } );

        othermarkerOptions = new MarkerOptions();
        gpsTracker = new GPSTracker(getActivity(), getActivity());
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        Log.i("", "lattitude:--" + gpsTracker.getLatitude() + "<>longitude:--" + gpsTracker.getLongitude());

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gpsTracker = new GPSTracker(mContext, getActivity());

            // Check if GPS enabled
            if (gpsTracker.canGetLocation()) {

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                Log.i("", "gpsvalues:--" + gpsTracker.getLatitude() + "<>longitude:--" + gpsTracker.getLongitude());

                try {
                    langitude_value = longitude;
                    latitude_value = latitude;
                } catch (Exception e) {
                    langitude_value = 0.0;
                    latitude_value = 0.0;
                }

                //Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude_value + "\nLong: " + langitude_value, Toast.LENGTH_LONG).show();
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gpsTracker.showSettingsAlert();
            }
        }
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }


      /*  if (InterNetChecker.isNetworkAvailable(getActivity())) {
            state_id = "";
            latval = "";
            lngval = "";
            lng = 0.0;
            lat = 0.0;
            new WholesalerslistTask().execute(state_id, latval, lngval);
        } else {
            Toast.makeText(getActivity(), "Please check Internet connection..", Toast.LENGTH_SHORT).show();
        }

        rgstr_mapfltr_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assigndatatomap(wholesalerslist);
              //  Mapstatesfilterpopup(getActivity());
            }
        });*/

        String[] latitude={"17.3685","55.3781","25.2744"};
        String[] longitude={"78.5316","3.4360","133.7751"};
        String[] names={"Loch Morar Freshwater Lake","Loch Morar Freshwater Lake","Loch Morar Freshwater Lake"};

        for (int k = 0; k < latitude.length; k++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("lat", latitude[k]);
            hashMap.put("lng", longitude[k]);
            hashMap.put("name", names[k]);
            dummy_list.add(hashMap);
        }

        //assigndatatomap(dummy_list);



    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LatLng latLng = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());//current location

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setPadding(20, 20, 20, 20);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
            if(mapFragment!=null){
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(mapFragment);
                // ft.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onDestroy() {

        Log.i("", "Dessss<><>");

        try {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
            if(mapFragment!=null){
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(mapFragment);
                // ft.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }
    @Override
    public void onResume() {

        Log.i("", "Dessss<><>");

        try {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
            if(mapFragment==null){
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(mapFragment);
                ft.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        super.onResume();
    }
    @Override
    public void onPause() {
        Log.i("", "Dessss<><>:::::::pause");
        if(mMap != null) {
            Log.i("", "Dessss<><>:::::::pause_inner");
            mMap = null;
        }
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
        //mMap = null;
        super.onPause();
    }


    public void assigndatatomap(final ArrayList<HashMap<String, String>> salerslist){
        mapFragment.getMapAsync(new OnMapReadyCallback() {


            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMap.clear();
                Marker newMarker;

                for (int j = 0; j < salerslist.size(); j++) {

                    double latitude=0.0;
                    double longitude=0.0;
                    try {
                        if(salerslist.get(j).get("lat").equalsIgnoreCase("")||
                                salerslist.get(j).get("lat").equalsIgnoreCase("null")||
                                salerslist.get(j).get("lat").equalsIgnoreCase(null)){
                            latitude = 0.0;

                        }else{
                            latitude = Double.parseDouble( salerslist.get(j).get("lat"));

                        }
                        if(salerslist.get(j).get("lng").equalsIgnoreCase("")||
                                salerslist.get(j).get("lng").equalsIgnoreCase("null")||
                                salerslist.get(j).get("lng").equalsIgnoreCase(null)){
                            longitude = 0.0;

                        }else{
                            longitude = Double.parseDouble( salerslist.get(j).get("lng"));

                        }

                        LatLng point = new LatLng(latitude, longitude);

                        int height = 35;
                        int width = 35;
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.home_yeloow_marker);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                        //markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination));

                        newMarker = googleMap.addMarker(new MarkerOptions()
                                .position(point)
                                .snippet("").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_point)));////.icon( BitmapDescriptorFactory.fromBitmap(smallMarker)));
                        newMarker.setTitle(newMarker.getId());

                        newMarker.setTag(j);

                        if(lng==0.0&&lat==0.0){
                            googleMap.moveCamera( CameraUpdateFactory.newLatLng(point));
                            googleMap.animateCamera( CameraUpdateFactory.zoomTo(5));
                        }else{

                            LatLng point1= new LatLng(lat, lng);
                            googleMap.moveCamera( CameraUpdateFactory.newLatLng(point1));
                            googleMap.animateCamera( CameraUpdateFactory.zoomTo(5));
                        }



                        if (markerImages != null) {
                            try {
                                ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
                                images_array.clear();
                                images_array = JsonParsing.GetJsonData(salerslist.get(j).get( "files" ));
                                markerImages.put(newMarker.getId(), images_array.get(0).get( "filename" ));
                            }catch (Exception e){

                            }
                        }
                        if (markerIDs != null) {
                            markerIDs.put(newMarker.getId(), j+"");
                        }

                        if (marker_descriptionn != null) {
                            marker_descriptionn.put(newMarker.getId(),salerslist.get(j).get("description"));
                        }

                        try{

                            if (fishingspots != null) {

                                ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
                                images_array.clear();
                                images_array = JsonParsing.GetJsonData(salerslist.get(j).get( "fishings_spots_list" ));
                                StoredObjects.LogMethod( "<><><>files","<><><<><><><><><><><innermap>"+images_array.size() );
                                String value = "";
                                try{
                                    if (images_array.size() > 0) {

                                        for (int i = 0; i <images_array.size() ; i++) {
                                            value += "#"+images_array.get(i).get("name")+",";
                                        }
                                        value = value.substring(0,value.length()-1);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                fishingspots.put(newMarker.getId(),value);
                            }

                        }catch (Exception e){

                        }



                        if (markerNames != null) {
                           /* markerNames.put(newMarker.getId(),salerslist.get(j).get("name"));
                            markerNames.put("description",salerslist.get(j).get("description"));
                            markerNames.put("images",salerslist.get(j).get("images"));
                            markerNames.put("lat",salerslist.get(j).get("lat"));
                            markerNames.put("lng",salerslist.get(j).get("lng"));*/

                            markerNames.put(newMarker.getId(),salerslist.get(j).get("title"));
                            markerNames.put("description",salerslist.get(j).get("description"));
                            markerNames.put("images",salerslist.get(j).get("files"));
                            markerNames.put("lat",salerslist.get(j).get("lat"));
                            markerNames.put("lng",salerslist.get(j).get("lng"));
                            //markerNames.put("fishings_spots_list",salerslist.get(j).get("fishings_spots_list"));
                          //  fishingspots.put("lng",salerslist.get(j).get("lng"));

                        }

                    } catch (Exception e) {

                    }
                    googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(markerAddress, markerNames,markerIDs,markerImages,marker_descriptionn,fishingspots));
                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            int position = (int) (marker.getTag());
                            // Mapdetailspopup(getActivity(),position,salerslist);
                            fragmentcalling1(new Comments(),salerslist,position);
                        }
                    });
                }
            }
        });
    }



    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;
        boolean not_first_time_showing_info_window=false;
        private HashMap<String, String> names=null;
        private HashMap<String, String> images=null;
        private HashMap<String, String> descri=null;
        private HashMap<String, String> fishing_spots_taken=null;
        private HashMap<String, String> addressInfo = null;
        private HashMap<String, String> iDsarray = null;
        MyInfoWindowAdapter(HashMap<String, String> addressInfo, HashMap<String, String> names, HashMap<String, String> iDsarray,HashMap<String, String> images,HashMap<String, String> description,HashMap<String, String> fishing_spots){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.detailsmappointer_two, null);
            myContentsView.setLayoutParams(new ViewGroup.LayoutParams(
                    600,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            this.names = names;
            this.addressInfo = addressInfo;
            this.iDsarray = iDsarray;
            this.images = images;
            this.descri = description;
            this.fishing_spots_taken = fishing_spots;

            StoredObjects.LogMethod( "<><><>files","<><><<><><><><><><><>"+fishing_spots_taken );

        }

        @Override
        public View getInfoContents(final Marker marker) {
            CustomRegularTextView lakename_txt =(CustomRegularTextView)myContentsView.findViewById(R.id.lakename_txt);
            CustomRegularTextView lakedetails_txt =(CustomRegularTextView)myContentsView.findViewById(R.id.lakedetails_txt);
            CustomRegularTextView directions_txt =(CustomRegularTextView)myContentsView.findViewById(R.id.directions_txt);
            ImageView lake_image =(ImageView)myContentsView.findViewById(R.id.lake_image);
            LinearLayout location_directions = myContentsView.findViewById(R.id.location_directions);
            CustomRegularTextView fishingspots_text =(CustomRegularTextView)myContentsView.findViewById(R.id.fishingspots_text);

            directions_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            try{
                lakename_txt.setText(StoredObjects.stripHtml(names.get(marker.getId())));
                lakedetails_txt.setText(descri.get(marker.getId()));
            }catch (Exception e){
                e.printStackTrace();
            }

            directions_txt.setText("Details");

            try{
                fishingspots_text.setText(fishing_spots_taken.get(marker.getId()));
            }catch (Exception e){
                e.printStackTrace();
            }



           // fishingspots_text.setVisibility(View.GONE);

            /*try{

                if (fishing_spots_taken.size() > 0) {
                    String value = "";
                    *//*fishingspots_lay.removeAllViews();
                    for (int i = 0; i <images_array.size() ; i++) {
                        addlayoutfishingspots(fishingspots_lay,images_array,i);
                    }*//*

                            for (int i = 0; i <fishing_spots_taken.size() ; i++) {
                                value += "#"+fishing_spots_taken.get(i).get("name")+",";
                            }
                            value = value.substring(0,value.length()-1);
                            fishingspots_text.setText(value);
                }
            }catch (Exception e){
                e.printStackTrace();
            }*/




           /* lakedetails_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StoredObjects.ToastMethod(" Address Not found", getActivity());
                }
            });

            location_directions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String url = "https://www.google.com/maps/dir/?api=1&destination=" + names.get("lat") + "° N" + "," +names.get("lng") + "° W" *//*"&travelmode=driving"*//*;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                        StoredObjects.ToastMethod(" Address Not found", getActivity());
                    }
                }
            });*/




            try {
               /* ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
                images_array.clear();
                images_array = JsonParsing.GetJsonData(names.get( "images" ));*/
                /*if(images_array.size()>0){
                    Glide.with( getActivity() )
                            .load( Uri.parse( StoredUrls.Uploadedimages + images_array.get(0).get( "image" ) ) ) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder( R.drawable.splash_logo_new )
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .crossFade()
                            //.override(600,600)
                            .into( lake_image );
                }*/

                String image_new =images.get(marker.getId());// images_array.get(0).get( "image" );
                //Log.i("<>><><>","cheking_second"+images);
                if (not_first_time_showing_info_window) {
                    Log.i("<>><><>","cheking_second"+StoredUrls.Uploadedimages +image_new);
                    // Picasso.with(MainActivity.this).load("http://47.88.149.112/lyra/uploads/shop7.png").into(imageView);
                    Picasso.with(getActivity()).load(StoredUrls.Uploadedimages +image_new).placeholder(R.drawable.splash_logo_new).into(lake_image);
                    not_first_time_showing_info_window=false;
                } else { // if it's the first time, load the image with the callback set
                    Log.i("<>><><>","cheking_second:::"+StoredUrls.Uploadedimages +image_new);
                    not_first_time_showing_info_window=true;
                    // Picasso.with(MainActivity.this).load("http://47.88.149.112/lyra/uploads/shop7.png").into(imageView,new InfoWindowRefresher(marker));
                    //   Picasso.with(MainActivity.this).load("http://assets3.parliament.uk/iv/main-large//ImageVault/Images/id_7382/scope_0/ImageVaultHandler.aspx.jpg").into(imageView,new InfoWindowRefresher(marker));
                    Picasso.with(getActivity()).load(StoredUrls.Uploadedimages +image_new).placeholder(R.drawable.splash_logo_new).into(lake_image,new MyInfoWindowAdapter.InfoWindowRefresher(marker));

                }




                //  adapter.notifyItemInserted(position);
            } catch (Exception e) {
            }

            return myContentsView;
        }

        private class InfoWindowRefresher implements Callback {
            private Marker markerToRefresh;

            private InfoWindowRefresher(Marker markerToRefresh) {
                this.markerToRefresh = markerToRefresh;
            }

            @Override
            public void onSuccess() {
                markerToRefresh.showInfoWindow();
            }

            @Override
            public void onError() {}
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }



    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {

                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            // map_loc_edt.setText(Html.fromHtml(place.getAddress()+""));
            lng=place.getLatLng().longitude;
            lat=place.getLatLng().latitude;
            //  StoredObjects.hide_dialogkeyboardtextview(getActivity(),map_loc_edt);

        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        // Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        // Log.e(LOG_TAG, "Google Places API connection suspended.");
    }







    public void fragmentcalling1(Fragment fragment, ArrayList<HashMap<String, String>> list, int position) {
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable("YourHashMap", list);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }









}
