package com.fishbuddy.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fishbuddy.Gps.GPSTracker;
import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.PlaceArrayAdapter;
import com.fishbuddy.customfonts.CustomBoldTextView;
import com.fishbuddy.customfonts.CustomButton;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.custommap.WorkaroundMapFragment;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.os.Build.VERSION.SDK_INT;
import static com.fishbuddy.sidemenu.SideMenu.btm_profile_img;
import static com.fishbuddy.sidemenu.SideMenu.btm_profile_lay;
import static com.fishbuddy.sidemenu.SideMenu.btm_profile_txt;
import static com.fishbuddy.sidemenu.SideMenu.buttonchangemethod;

public  class Profile extends Fragment implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    RecyclerView followers_recycle, following_recycle, catches_recycle;
    CustomRecyclerview customRecyclerview;
    LinearLayout se_all_flwrs_txt, se_all_following_txt, se_all_catches_txt,se_all_posts_txt;
    CustomButton edit_profile_btn;
    ImageView backbtn_img,profile_img;
    CustomRegularTextView profile_txt,nodatafound_txt,custm_title_txt,profile_address_text;
    Context mContext;
    GPSTracker gpsTracker;
    Double latitude_value = 0.0;
    Double langitude_value = 0.0;
    MarkerOptions othermarkerOptions;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private HashMap<String, String> markerNames = new HashMap<>();
    private HashMap<String, String> markerAddress = new HashMap<>();
    private HashMap<String, String> markerIDs = new HashMap<>();

    private HashMap<String, String> markerImages = new HashMap<>();
    private HashMap<String, String> marker_descriptionn = new HashMap<>();


    public ArrayList<HashMap<String, String>> followerslist = new ArrayList<>();
    public ArrayList<HashMap<String, String>> followinglist = new ArrayList<>();

    public ArrayList<HashMap<String, String>> catches = new ArrayList<>();


    String state_name = "";
    LinearLayout nocurntcustomer_lay;
    ArrayList<HashMap<String, String>> getprofilelist=new ArrayList<>();

    //   public static CustomTextView wholesalers_name;

    String state_id = "",userlat= "17.3685",userlong = "78.5316";
    String latval = "";
    String lngval = "";
    Double lat = 0.0;
    Double lng = 0.0;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng( 37.398160, -122.180831 ), new LatLng( 37.430610, -121.972090 ) );
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();

    private ShimmerFrameLayout shimmer_view_container_follwers,shimmer_view_container__textviews;

    View following_layout,catches_shimmerlay;
    LinearLayout top_profile_layout;

    CustomRegularTextView no_folloingdata_found;

    CustomRegularTextView no_catches_found;
   // no_catches_found,catches_shimmerlay

        ImageView custm_search_img;

    ScrollView scrollview_profile;
    LinearLayout maplayout;



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
            mGoogleApiClient.stopAutoManage( (FragmentActivity) mContext );
            mGoogleApiClient.disconnect();
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.profile, null, false );
        StoredObjects.page_type = "home";
        StoredObjects.back_type = "profile";
        //initilizeMap();
        buttonchangemethod (getActivity() , btm_profile_lay , btm_profile_img , btm_profile_txt , "4");
        SideMenu.updatemenu( StoredObjects.page_type );
        initilization( v );

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new GetProfileTask().execute(StoredObjects.UserId);
          //  new GetFollowersTask().execute(StoredObjects.UserId);
           // new GetFollowingTask().execute(StoredObjects.UserId);
        }else{
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
        }

/*        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new GetFollowersTask().execute(StoredObjects.UserId);
            new GetFollowingTask().execute(StoredObjects.UserId);
        }else{
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
        }*/
        mGoogleApiClient = new GoogleApiClient.Builder( getActivity() )
                .addApi( Places.GEO_DATA_API )
                .enableAutoManage( getActivity(), GOOGLE_API_CLIENT_ID, this )
                .addConnectionCallbacks( this )
                .build();
        mPlaceArrayAdapter = new PlaceArrayAdapter( getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null );

        return v;
    }

    private void initilization(View v) {

        shimmer_view_container_follwers = v.findViewById(R.id.shimmer_view_container_follwers);
        shimmer_view_container__textviews = v.findViewById(R.id.shimmer_view_container__textviews);
        top_profile_layout = v.findViewById(R.id.top_profile_layout);
        top_profile_layout.setVisibility(View.GONE);
        following_layout = v.findViewById(R.id.following_layout);

        catches_shimmerlay = v.findViewById(R.id.catches_shimmerlay);

        custm_search_img = v.findViewById(R.id.custm_search_img);


        followers_recycle = (RecyclerView) v.findViewById( R.id.followers_recycle );
        following_recycle = (RecyclerView) v.findViewById( R.id.following_recycle );
        catches_recycle = (RecyclerView) v.findViewById( R.id.catches_recycle );
        customRecyclerview = new CustomRecyclerview( getActivity() );
        se_all_flwrs_txt = (LinearLayout) v.findViewById( R.id.se_all_flwrs_txt );
        se_all_following_txt = (LinearLayout) v.findViewById( R.id.se_all_following_txt );

        se_all_posts_txt = (LinearLayout) v.findViewById( R.id.se_all_posts_txt );
        se_all_catches_txt = (LinearLayout) v.findViewById( R.id.se_all_catches_txt );
        edit_profile_btn = (CustomButton) v.findViewById( R.id.edit_profile_btn );
        profile_txt = (CustomRegularTextView)v.findViewById( R.id.profile_txt );

        custm_title_txt = (CustomRegularTextView)v.findViewById( R.id.custm_title_txt );
        profile_address_text = (CustomRegularTextView)v.findViewById( R.id.profile_address_text );

        no_folloingdata_found = (CustomRegularTextView)v.findViewById( R.id.no_folloingdata_found );
        nodatafound_txt = (CustomRegularTextView)v.findViewById( R.id.nodatafound_txt );

        no_catches_found = v.findViewById( R.id.no_catches_found );

        profile_img = (ImageView)v.findViewById( R.id.profile_img );


        scrollview_profile = v.findViewById(R.id.scrollview_profile);


        maplayout = v.findViewById(R.id.maplayout);
        maplayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                fragmentcalling1(new CtachesOnMap(),catches,0);

                return false;
            }
        });


        custm_search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay( new Searchpage() );
            }
        });


       /* scrollview_profile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                       // scrollViewParent.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                       // scrollViewParent.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //scrollViewParent.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
*/
        edit_profile_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentcallinglay( new Edit_profile() );
            }
        } );

        se_all_flwrs_txt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentcallinglay( new Followers() );
            }
        } );
        se_all_posts_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcalling2( new MyPosts(),"Self Normal Posts" ,StoredObjects.UserId);
            }
        });
        se_all_following_txt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   fragmentcallinglay( new Following() );
            }
        } );
        se_all_catches_txt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 //  fragmentcallinglay( new Profile_catches() );
                fragmentcalling2( new MyPosts(),"Self Catches",StoredObjects.UserId );
            }
        } );

        TextView title_txt = (TextView)v.findViewById( R.id. title_txt);
        title_txt.setText( R.string.profile );

        backbtn_img = (ImageView) v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();

            }
        } );

       // StoredObjects.hashmaplist( 8 );
/*
        customRecyclerview.Assigndatatorecyleviewhashmap( followers_recycle, StoredObjects.dummy_list, "profile_followers", StoredObjects.Listview, 0, StoredObjects.horizontal_orientation, R.layout.prfle_following_listitems );
*/
/*
        customRecyclerview.Assigndatatorecyleviewhashmap( following_recycle, StoredObjects.dummy_list, "profile_following", StoredObjects.Listview, 0, StoredObjects.horizontal_orientation, R.layout.prfle_following_listitems );
*/
      //  customRecyclerview.Assigndatatorecyleviewhashmap( catches_recycle, StoredObjects.dummy_list, "profile_catches", StoredObjects.Listview, 0, StoredObjects.horizontal_orientation, R.layout.catches_listitems );

        othermarkerOptions = new MarkerOptions();
        gpsTracker = new GPSTracker( getActivity(), getActivity() );
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        Log.i( "", "lattitude:--" + gpsTracker.getLatitude() + "<>longitude:--" + gpsTracker.getLongitude() );

        /*if (ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION )
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gpsTracker = new GPSTracker( mContext, getActivity() );

            // Check if GPS enabled
            if (gpsTracker.canGetLocation()) {

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                Log.i( "", "gpsvalues:--" + gpsTracker.getLatitude() + "<>longitude:--" + gpsTracker.getLongitude() );

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
        }*/
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );


        /*mapFragment.getChildFragmentManager().findFragmentById( R.id.map ).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollview_profile.requestDisallowInterceptTouchEvent(true);
            }
        });*/



        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy( policy );
            //your codes here
        }

        String[] latitude = {"17.3685", "55.3781", "25.2744"};
        String[] longitude = {"78.5316", "3.4360", "133.7751"};
        String[] names = {"Loch Morar Freshwater Lake", "Loch Morar Freshwater Lake", "Loch Morar Freshwater Lake"};

        for (int k = 0; k < latitude.length; k++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put( "lat", latitude[k] );
            hashMap.put( "lng", longitude[k] );
            hashMap.put( "name", names[k] );
            dummy_list.add( hashMap );
        }

       // assigndatatomap( dummy_list );
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission( getActivity(), ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.setMyLocationEnabled( true );
        mMap.setPadding( 20, 20, 20, 20 );
        mMap.getUiSettings().setMyLocationButtonEnabled( true );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));//12

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById( R.id.map );
            if (mapFragment != null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove( mapFragment );
                // ft.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {

        Log.i( "", "Dessss<><>" );

        try {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById( R.id.map );
            if (mapFragment != null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove( mapFragment );
                // ft.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void onResume() {

        Log.i( "", "Dessss<><>" );

        try {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById( R.id.map );
            if (mapFragment == null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove( mapFragment );
                ft.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i( "", "Dessss<><>:::::::pause" );
        if (mMap != null) {
            Log.i( "", "Dessss<><>:::::::pause_inner" );
            mMap = null;
        }
        mGoogleApiClient.stopAutoManage( getActivity() );
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

                        }

                    } catch (Exception e) {

                    }
                    googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(markerAddress, markerNames,markerIDs,markerImages,marker_descriptionn));
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








    public void assigndatatomap12314(final ArrayList<HashMap<String, String>> salerslist) {
        mapFragment.getMapAsync( new OnMapReadyCallback() {


            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMap.clear();
                Marker newMarker;

                for (int j = 0; j < salerslist.size(); j++) {

                    double latitude = 0.0;
                    double longitude = 0.0;
                    try {
                        if (salerslist.get( j ).get( "lat" ).equalsIgnoreCase( "" ) ||
                                salerslist.get( j ).get( "lat" ).equalsIgnoreCase( "null" ) ||
                                salerslist.get( j ).get( "lat" ).equalsIgnoreCase( null )) {
                            latitude = 0.0;

                        } else {
                            latitude = Double.parseDouble( salerslist.get( j ).get( "lat" ) );

                        }
                        if (salerslist.get( j ).get( "lng" ).equalsIgnoreCase( "" ) ||
                                salerslist.get( j ).get( "lng" ).equalsIgnoreCase( "null" ) ||
                                salerslist.get( j ).get( "lng" ).equalsIgnoreCase( null )) {
                            longitude = 0.0;

                        } else {
                            longitude = Double.parseDouble( salerslist.get( j ).get( "lng" ) );

                        }

                        LatLng point = new LatLng( latitude, longitude );

                        int height = 35;
                        int width = 35;
                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable( R.drawable.location_icon );
                        Bitmap b = bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap( b, width, height, false );

                        newMarker = googleMap.addMarker( new MarkerOptions()
                                .position( point )
                                .snippet( "" ).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_point_new_icon_)));//.icon( BitmapDescriptorFactory.fromBitmap( smallMarker ) ) );
                        newMarker.setTitle( newMarker.getId() );

                        newMarker.setTag( j );

                        if (lng == 0.0 && lat == 0.0) {
                          //  googleMap.moveCamera( CameraUpdateFactory.newLatLng( point ) );
                           // googleMap.animateCamera( CameraUpdateFactory.zoomTo( 5 ) );
                        } else {

                            LatLng point1 = new LatLng( lat, lng );
                            //googleMap.moveCamera( CameraUpdateFactory.newLatLng( point1 ) );
                           // googleMap.animateCamera( CameraUpdateFactory.zoomTo( 5 ) );
                        }

                        if (markerIDs != null) {
                            markerIDs.put( newMarker.getId(), j + "" );
                        }
                        if (markerNames != null) {
                            markerNames.put( newMarker.getId(), salerslist.get( j ).get( "name" ) );
                        }
/*
                            if (markerAddress != null) {
                                String address1="";
                                String address2="";
                                String address3="";
                                String address4="";
                                String address5="";

                                if(salerslist.get(j).get("street").equalsIgnoreCase("-")|| salerslist.get(j).get("street").equalsIgnoreCase("null")){
                                    address1= salerslist.get(j).get("description");
                                }else{
                                    address1= salerslist.get(j).get("description")
                                            +", "+salerslist.get(j).get("street");
                                }
                                if(salerslist.get(j).get("city").equalsIgnoreCase("-")|| salerslist.get(j).get("city").equalsIgnoreCase("null")){
                                    address2="";
                                }else{
                                    address2=", "+ salerslist.get(j).get("city");
                                }
                                if(salerslist.get(j).get("postcode").equalsIgnoreCase("-")|| salerslist.get(j).get("postcode").equalsIgnoreCase("null")){
                                    address3="";
                                }else{
                                    address3=", "+ salerslist.get(j).get("postcode");
                                }
                                if(salerslist.get(j).get("short_name").equalsIgnoreCase("-")|| salerslist.get(j).get("short_name").equalsIgnoreCase("null")){
                                    address4="";
                                }else{
                                    address4=", "+ salerslist.get(j).get("short_name");
                                }

                                address5=address1+address2+address3+address4;
                                markerAddress.put(newMarker.getId(), address5);

                                */
/*wholesalerslist.get(j).get("description")
                                +", "+ wholesalerslist.get(j).get("street")
                                +", "+ wholesalerslist.get(j).get("city")
                                        +", "+ wholesalerslist.get(j).get("postcode")
                                        +", "+ wholesalerslist.get(j).get("short_name")*//*

                            }
*/

                    } catch (Exception e) {

                    }


                  //  googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(markerAddress, markerNames,markerIDs));


                    googleMap.setOnInfoWindowClickListener( new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            int position = (int) (marker.getTag());


                            // Mapdetailspopup(getActivity(),position,salerslist);


                        }
                    } );


                }


            }
        } );


    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;
        boolean not_first_time_showing_info_window=false;
        private HashMap<String, String> names=null;
        private HashMap<String, String> images=null;
        private HashMap<String, String> descri=null;
        private HashMap<String, String> addressInfo = null;
        private HashMap<String, String> iDsarray = null;
        MyInfoWindowAdapter(HashMap<String, String> addressInfo, HashMap<String, String> names, HashMap<String, String> iDsarray,HashMap<String, String> images,HashMap<String, String> description){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.detailsmappointer, null);
            myContentsView.setLayoutParams(new ViewGroup.LayoutParams(
                    600,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            this.names = names;
            this.addressInfo = addressInfo;
            this.iDsarray = iDsarray;
            this.images = images;
            this.descri = description;
        }

        @Override
        public View getInfoContents(final Marker marker) {
            CustomRegularTextView lakename_txt =(CustomRegularTextView)myContentsView.findViewById(R.id.lakename_txt);
            CustomRegularTextView lakedetails_txt =(CustomRegularTextView)myContentsView.findViewById(R.id.lakedetails_txt);
            CustomRegularTextView directions_txt =(CustomRegularTextView)myContentsView.findViewById(R.id.directions_txt);
            ImageView lake_image =(ImageView)myContentsView.findViewById(R.id.lake_image);
            LinearLayout location_directions = myContentsView.findViewById(R.id.location_directions);

            directions_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            lakename_txt.setText(StoredObjects.stripHtml(names.get(marker.getId())));
            lakedetails_txt.setText(descri.get(marker.getId()));
            directions_txt.setText("Details");

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
                Log.i("<>><><>","cheking_second"+images);
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

/*
    public class WholesalerslistTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar_test.Progressbarshow(getActivity());
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("state_id",params[0]));
                nameValuePairs.add(new BasicNameValuePair("lat",params[1]));
                nameValuePairs.add(new BasicNameValuePair("lng",params[2]));
                strResult = HttpPostClass.PostMethod( StoredUrls.getwholesalers_url,nameValuePairs);
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
            StoredObjects.LogMethod("Details", "e:----"+strResult);
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar_test.Progressbarcancel(getActivity());
            }
            StoredObjects.LogMethod("Details", "e:----"+result);
            try {


                JSONObject jsonObject =  new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equalsIgnoreCase("200")){
                    String results = jsonObject.getString("results");
                    wholesalerslist = JsonParsing.GetJsonData(results);
                    assigndatatomap(wholesalerslist);
                }else{
                    String error= jsonObject.getString("error");
                    StoredObjects.ToastMethod(error,getActivity());
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                // TODO: handle exception
            }catch (IllegalStateException e) {
                // TODO: handle exception
            }catch (IllegalArgumentException e) {
                // TODO: handle exception
            }catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            }catch (RuntimeException e) {
                // TODO: handle exception
            }
            catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---"+e);
            }
        }
    }
*/


    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace( R.id.frame_container, fragment ).addToBackStack( "" ).commit();

    }

    public void fragmentcalling1(Fragment fragment, ArrayList<HashMap<String, String>> list, int position) {
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable("YourHashMap", list);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }

    public void fragmentcalling2(Fragment fragment,String post_option, String customer_id) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        //  bundle.putParcelableArrayList("mylist", (ArrayList<? extends Parcelable>) dumpData);
        bundle.putString("post_option", post_option);
        bundle.putString("customer_id", customer_id);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();

    }

/*
    private void Mapdetailspopup(final Activity activity, final int j, final ArrayList<HashMap<String, String>> salerslist) {

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.wholesalerdetailspopup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setLayout( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        CustomTextView ws_name_txt = (CustomTextView)dialog.findViewById(R.id.ws_name_txt);
        CustomTextView ws_address_txt = (CustomTextView)dialog.findViewById(R.id.ws_address_txt);
        CustomTextView ws_phone_txt = (CustomTextView)dialog.findViewById(R.id.ws_phone_txt);
        CustomTextView ws_fax_txt = (CustomTextView)dialog.findViewById(R.id.ws_fax_txt);
        CustomTextView ws_email_txt = (CustomTextView)dialog.findViewById(R.id.ws_email_txt);
        CustomTextView ws_direction_txt = (CustomTextView)dialog.findViewById(R.id.ws_direction_txt);

        CustomTextView mn_fromtme_txt = (CustomTextView)dialog.findViewById(R.id.mn_fromtme_txt);
        CustomTextView mn_totme_txt = (CustomTextView)dialog.findViewById(R.id.mn_totme_txt);
        CustomTextView tue_fromtme_txt = (CustomTextView)dialog.findViewById(R.id.tue_fromtme_txt);
        CustomTextView tue_totme_txt = (CustomTextView)dialog.findViewById(R.id.tue_totme_txt);
        CustomTextView wed_frmtme_txt = (CustomTextView)dialog.findViewById(R.id.wed_frmtme_txt);
        CustomTextView wed_totme_txt = (CustomTextView)dialog.findViewById(R.id.wed_totme_txt);

        CustomTextView thurs_frmtme_txt = (CustomTextView)dialog.findViewById(R.id.thurs_frmtme_txt);
        CustomTextView thurs_totme_txt = (CustomTextView)dialog.findViewById(R.id.thurs_totme_txt);
        CustomTextView fri_fromtme_txt = (CustomTextView)dialog.findViewById(R.id.fri_fromtme_txt);
        CustomTextView fri_totme_txt = (CustomTextView)dialog.findViewById(R.id.fri_totme_txt);
        CustomTextView sat_frmtme_txt = (CustomTextView)dialog.findViewById(R.id.sat_frmtme_txt);
        CustomTextView sat_totme_txt = (CustomTextView)dialog.findViewById(R.id.sat_totme_txt);

        CustomTextView sun_frmtme_txt = (CustomTextView)dialog.findViewById(R.id.sun_frmtme_txt);
        CustomTextView sun_totme_txt = (CustomTextView)dialog.findViewById(R.id.sun_totme_txt);
        CustomTextView ws_web_txt = (CustomTextView)dialog.findViewById(R.id.ws_web_txt);

        CustomButton select_btn = (CustomButton)dialog.findViewById(R.id.select_btn);
        CustomButton cancel_btn = (CustomButton)dialog.findViewById(R.id.cancel_btn);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                RegisterMain.warehouseid=salerslist.get(j).get("wholesaler_id");
                wholesalers_name.setText(StoredObjects.stripHtml(salerslist.get(j).get("name")));
                RegisterMain.wholesalername=salerslist.get(j).get("name");

                RegisterMain.warehousecontact=salerslist.get(j).get("contact_name");
                RegisterMain.warehouseemail=salerslist.get(j).get("email");
                RegisterMain.warehousephone=salerslist.get(j).get("phone");
                RegisterMain.warehouseaddress=salerslist.get(j).get("street");
            }
        });



        ws_direction_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double lattitude= 0.0;
                    Double longitude= 0.0;
                    lattitude = Double.parseDouble(salerslist.get(j).get("lat"));
                    longitude = Double.parseDouble(salerslist.get(j).get("lng"));

                    String url = "https://www.google.com/maps/dir/?api=1&destination=" + lattitude + "," + longitude+ "&travelmode=driving";
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }catch (Exception e){

                }

            }




              */
/*  try {


                    if(getActivity()!=null){
                        Handler h = new Handler();
                        h.postAtTime(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Double lattitude= 0.0;
                                        Double longitude= 0.0;
                                        Double s_lattitude= 0.0;
                                        Double s_longitude= 0.0;
                                        lattitude = lat;
                                        longitude = lng;
                                        s_lattitude = gpsTracker.getLatitude();
                                        s_longitude =  gpsTracker.getLongitude();
                                        String url_="http://maps.google.com/maps?saddr="+s_lattitude+","+s_longitude+"&daddr="+lattitude+","+longitude;

                                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse(url_));
                                        startActivity(intent);


                                    }
                                });
                            }
                        }, 200);

                    }


                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
            }*//*

        });

    */
/*    monday_closed	"No"
        tuesday_closed	"No"
        wednesday_closed	"No"
        thursday_closed	"No"
        friday_closed	"No"
        saturday_closed	"No"
        sunday_closed	"No"
        lat	"-33.42571"
        lng	"149.619951"*//*


         String m_starthr="",m_endhr="",t_starthr="",t_endhr="",w_starthr="",w_endhr="",ts_starthr="",ts_endhr=""
                        ,f_starthr="",f_endhr="",st_starthr="",st_endhr="",s_starthr="",s_endhr="";

        if(salerslist.get(j).get("sunday_hours").contains("-")&&salerslist.get(j).get("sunday_hours").length()>2){
            String[] sunday_hours=salerslist.get(j).get("sunday_hours").split("-");
            s_starthr=sunday_hours[0];
            s_endhr=sunday_hours[1];
        }else{
            s_starthr=salerslist.get(j).get("sunday_hours");
            s_endhr=salerslist.get(j).get("sunday_hours");
        }
        if(salerslist.get(j).get("saturday_hours").contains("-")&&salerslist.get(j).get("saturday_hours").length()>2){
            String[] saturday_hours=salerslist.get(j).get("saturday_hours").split("-");
            st_starthr=saturday_hours[0];
            st_endhr=saturday_hours[1];
        }else{
            st_starthr=salerslist.get(j).get("saturday_hours");
            st_endhr=salerslist.get(j).get("saturday_hours");
        }
        if(salerslist.get(j).get("friday_hours").contains("-")&&salerslist.get(j).get("friday_hours").length()>2){
            String[] friday_hours=salerslist.get(j).get("friday_hours").split("-");
            f_starthr=friday_hours[0];
            f_endhr=friday_hours[1];
        }else{
            f_starthr=salerslist.get(j).get("thursday_hours");
            f_endhr=salerslist.get(j).get("thursday_hours");
        }
        if(salerslist.get(j).get("thursday_hours").contains("-")&&salerslist.get(j).get("thursday_hours").length()>2){
            String[] thursday_hours=salerslist.get(j).get("thursday_hours").split("-");
            ts_starthr=thursday_hours[0];
            ts_endhr=thursday_hours[1];
        }else{
            ts_starthr=salerslist.get(j).get("thursday_hours");
            ts_endhr=salerslist.get(j).get("thursday_hours");
        }

        if(salerslist.get(j).get("wednesday_hours").contains("-")&&salerslist.get(j).get("wednesday_hours").length()>2){
            String[] wednesday_hours=salerslist.get(j).get("wednesday_hours").split("-");
            w_starthr=wednesday_hours[0];
            w_endhr=wednesday_hours[1];
        }else{
            w_starthr=salerslist.get(j).get("wednesday_hours");
            w_endhr=salerslist.get(j).get("wednesday_hours");
        }
         if(salerslist.get(j).get("monday_hours").contains("-")&&salerslist.get(j).get("monday_hours").length()>2){
             String[] monday_hours=salerslist.get(j).get("monday_hours").split("-");
             m_starthr=monday_hours[0];
             m_endhr=monday_hours[1];
         }else{
             m_starthr=salerslist.get(j).get("monday_hours");
             m_endhr=salerslist.get(j).get("monday_hours");
         }
        if(salerslist.get(j).get("tuesday_hours").contains("-")&&salerslist.get(j).get("tuesday_hours").length()>2){
            String[] tuesday_hours=salerslist.get(j).get("tuesday_hours").split("-");
            t_starthr=tuesday_hours[0];
            t_endhr=tuesday_hours[1];
        }else{
            t_starthr=salerslist.get(j).get("tuesday_hours");
            t_endhr=salerslist.get(j).get("tuesday_hours");
        }
        String address1="";
        String address2="";
        String address3="";
        String address4="";
        String address5="";

        if(salerslist.get(j).get("street").equalsIgnoreCase("-")||salerslist.get(j).get("street").equalsIgnoreCase("null")){
            address1=salerslist.get(j).get("description");
        }else{
            address1=salerslist.get(j).get("description")
                    +", "+ salerslist.get(j).get("street");
        }
        if(salerslist.get(j).get("city").equalsIgnoreCase("-")||salerslist.get(j).get("city").equalsIgnoreCase("null")){
            address2="";
        }else{
            address2=", "+salerslist.get(j).get("city");
        }
        if(salerslist.get(j).get("postcode").equalsIgnoreCase("-")||salerslist.get(j).get("postcode").equalsIgnoreCase("null")){
            address3="";
        }else{
            address3=", "+salerslist.get(j).get("postcode");
        }
        if(salerslist.get(j).get("short_name").equalsIgnoreCase("-")||salerslist.get(j).get("short_name").equalsIgnoreCase("null")){
            address4="";
        }else{
            address4=", "+salerslist.get(j).get("short_name");
        }

        address5=address1+address2+address3+address4;
        ws_address_txt.setText(address5);

        ws_name_txt.setText(StoredObjects.stripHtml(salerslist.get(j).get("name")));
        ws_phone_txt.setText(salerslist.get(j).get("phone"));
        ws_fax_txt.setText(salerslist.get(j).get("fax"));
        ws_email_txt.setText(salerslist.get(j).get("email"));
        mn_fromtme_txt.setText(m_starthr);
        mn_totme_txt.setText(m_endhr);
        tue_fromtme_txt.setText(t_starthr);
        tue_totme_txt.setText(t_endhr);
        wed_frmtme_txt.setText(w_starthr);
        wed_totme_txt.setText(w_endhr);
        thurs_frmtme_txt.setText(ts_starthr);
        thurs_totme_txt.setText(ts_endhr);
        fri_fromtme_txt.setText(f_starthr);
        fri_totme_txt.setText(f_endhr);
        sat_frmtme_txt.setText(st_starthr);
        sat_totme_txt.setText(st_endhr);
        sun_frmtme_txt.setText(s_starthr);
        sun_totme_txt.setText(s_endhr);
        ws_web_txt.setText(salerslist.get(j).get("web"));

        ws_web_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(salerslist.get(j).get("web").equalsIgnoreCase("")||
                        salerslist.get(j).get("web").equalsIgnoreCase("-")){

                }else{


                    Intent myWebLink = new Intent( Intent.ACTION_VIEW);
                    myWebLink.setData( Uri.parse("http://"+salerslist.get(j).get("web")));
                    getActivity().startActivity(myWebLink);
                }

            }
        });


        ws_email_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendEmail(salerslist.get(j).get("email"));

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
        ImageView cancel_img = (ImageView)dialog.findViewById(R.id.cancel_img);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }
*/

    protected void sendEmail(String emailid) {

        Intent emailIntent = new Intent( Intent.ACTION_SENDTO );
        emailIntent.setData( Uri.parse( "mailto:" + emailid ) );


        try {
            startActivity( emailIntent );


        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText( getActivity(),
                    "There is no email client installed.", Toast.LENGTH_SHORT ).show();
        }
    }

    /* private ListPopupWindow listPopupWindow;
     AutoCompleteTextView map_loc_edt;
     RecyclerView map_recyler;
     CustomTextView nodata_txt;
     Dialog filterdialog;*/
/*
    private void Mapstatesfilterpopup(final Activity activity) {
        filterdialog = new Dialog(activity);
        listPopupWindow = new ListPopupWindow(getActivity());
        filterdialog.getWindow();
        filterdialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        filterdialog.setContentView(R.layout.mapstatesfiltermain);
        filterdialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        filterdialog.getWindow().setLayout( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        map_recyler = (RecyclerView)filterdialog.findViewById(R.id.map_recyler );
        final CustomEditText map_select_edtx = (CustomEditText)filterdialog.findViewById(R.id.map_select_edtx );
        map_loc_edt=(AutoCompleteTextView) filterdialog.findViewById(R.id.map_loc_edt) ;
        ImageView map_cancel_img = (ImageView) filterdialog.findViewById(R.id.map_cancel_img );
        nodata_txt=(CustomTextView) filterdialog.findViewById(R.id.nodata_txt) ;


        state_id="";
        latval="";
        lngval="";
        lng=0.0;
        lat=0.0;
        map_loc_edt.setThreshold(1);
        map_loc_edt.setOnItemClickListener(mAutocompleteClickListener);

        map_loc_edt.setAdapter(mPlaceArrayAdapter);

        selctdwholesalerslist.clear();

        for(int i=0;i<wholesalerslist.size();i++){
            selctdwholesalerslist.add(wholesalerslist.get(i));
        }
        map_recyler.setVisibility( View.VISIBLE);
        nodata_txt.setVisibility( View.GONE);

        customRecyclerview.Assigndatatorecyleviewhashmap(map_recyler, selctdwholesalerslist, "map_states", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.mapstatesfilterlistitems);

        map_recyler.addOnItemTouchListener(new RecylerTouchListener(getActivity(), map_recyler, new RecylerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                SearchMapdetailspopup(activity,position, selctdwholesalerslist);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        map_select_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DropdwnPopup(map_select_edtx,map_recyler);
            }
        });

        map_cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterdialog.dismiss();
            }
        });

        filterdialog.show();
    }
*/
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem( position );
            final String placeId = String.valueOf( item.placeId );

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById( mGoogleApiClient, placeId );
            placeResult.setResultCallback( mUpdatePlaceDetailsCallback );
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
            final Place place = places.get( 0 );
            CharSequence attributions = places.getAttributions();
            // map_loc_edt.setText(Html.fromHtml(place.getAddress()+""));
            lng = place.getLatLng().longitude;
            lat = place.getLatLng().latitude;
            //  StoredObjects.hide_dialogkeyboardtextview(getActivity(),map_loc_edt);
           /* if (InterNetChecker.isNetworkAvailable(getActivity())) {
                lngval=lng+"";
                latval=lat+"";
                new SearchWholesalerslistTask().execute(state_id,latval,lngval);
            }else{
                Toast.makeText(getActivity(),"Please check Internet connection..", Toast.LENGTH_SHORT).show();
            }

*/
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient( mGoogleApiClient );
        // Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText( getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG ).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient( null );
        // Log.e(LOG_TAG, "Google Places API connection suspended.");
    }
/*
    private void DropdwnPopup(final EditText prfilenme, final RecyclerView map_recyler){

        listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.regtr_slectchoice_drpdwn,Registerrone.statesnameslist));
        listPopupWindow.setAnchorView(prfilenme);
        listPopupWindow.setHeight( LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                state_name="";
                prfilenme.setText(Registerrone.statesnameslist.get(position));


                StoredObjects.hide_keyboard(getActivity());
                if (InterNetChecker.isNetworkAvailable(getActivity())) {
                    state_id=Registerrone.stateslist.get(position).get("id");
                    latval="";
                    lngval="";
                    lng=0.0;
                    lat=0.0;
                    map_loc_edt.setText("");
                    new SearchWholesalerslistTask().execute(state_id,latval,lngval);
                }else{
                    Toast.makeText(getActivity(),"Please check Internet connection..", Toast.LENGTH_SHORT).show();
                }

                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }
*/

/*
    public class SearchWholesalerslistTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar_test.Progressbarshow(getActivity());
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("state_id",params[0]));
                nameValuePairs.add(new BasicNameValuePair("lat",params[1]));
                nameValuePairs.add(new BasicNameValuePair("lng",params[2]));
                strResult = HttpPostClass.PostMethod(StoredUrls.getwholesalers_url,nameValuePairs);
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
            StoredObjects.LogMethod("Details", "e:----"+strResult);
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar_test.Progressbarcancel(getActivity());
            }
            StoredObjects.LogMethod("Details", "e:----"+result);
            try {


                JSONObject jsonObject =  new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equalsIgnoreCase("200")){
                    String results = jsonObject.getString("results");
                    selctdwholesalerslist = JsonParsing.GetJsonData(results);

                    map_recyler.setVisibility( View.VISIBLE);
                    nodata_txt.setVisibility( View.GONE);
                    customRecyclerview.Assigndatatorecyleviewhashmap(map_recyler, selctdwholesalerslist, "map_states", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.mapstatesfilterlistitems);

                    assigndatatomap(selctdwholesalerslist);
                }else{
                    map_recyler.setVisibility( View.GONE);
                    nodata_txt.setVisibility( View.VISIBLE);
                    */
/*String error= jsonObject.getString("error");
                    StoredObjects.ToastMethod(error,getActivity());*//*

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                // TODO: handle exception
            }catch (IllegalStateException e) {
                // TODO: handle exception
            }catch (IllegalArgumentException e) {
                // TODO: handle exception
            }catch (NetworkOnMainThreadException e) {
                // TODO: handle exception
            }catch (RuntimeException e) {
                // TODO: handle exception
            }
            catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---"+e);
            }
        }
    }
*/


/*
    private void SearchMapdetailspopup(final Activity activity, final int j, final ArrayList<HashMap<String, String>> wholesalerslist) {

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.wholesalerdetailspopup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setLayout( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        CustomTextView ws_name_txt = (CustomTextView)dialog.findViewById(R.id.ws_name_txt);
        CustomTextView ws_address_txt = (CustomTextView)dialog.findViewById(R.id.ws_address_txt);
        CustomTextView ws_phone_txt = (CustomTextView)dialog.findViewById(R.id.ws_phone_txt);
        CustomTextView ws_fax_txt = (CustomTextView)dialog.findViewById(R.id.ws_fax_txt);
        CustomTextView ws_email_txt = (CustomTextView)dialog.findViewById(R.id.ws_email_txt);
        CustomTextView ws_direction_txt = (CustomTextView)dialog.findViewById(R.id.ws_direction_txt);

        CustomTextView mn_fromtme_txt = (CustomTextView)dialog.findViewById(R.id.mn_fromtme_txt);
        CustomTextView mn_totme_txt = (CustomTextView)dialog.findViewById(R.id.mn_totme_txt);
        CustomTextView tue_fromtme_txt = (CustomTextView)dialog.findViewById(R.id.tue_fromtme_txt);
        CustomTextView tue_totme_txt = (CustomTextView)dialog.findViewById(R.id.tue_totme_txt);
        CustomTextView wed_frmtme_txt = (CustomTextView)dialog.findViewById(R.id.wed_frmtme_txt);
        CustomTextView wed_totme_txt = (CustomTextView)dialog.findViewById(R.id.wed_totme_txt);

        CustomTextView thurs_frmtme_txt = (CustomTextView)dialog.findViewById(R.id.thurs_frmtme_txt);
        CustomTextView thurs_totme_txt = (CustomTextView)dialog.findViewById(R.id.thurs_totme_txt);
        CustomTextView fri_fromtme_txt = (CustomTextView)dialog.findViewById(R.id.fri_fromtme_txt);
        CustomTextView fri_totme_txt = (CustomTextView)dialog.findViewById(R.id.fri_totme_txt);
        CustomTextView sat_frmtme_txt = (CustomTextView)dialog.findViewById(R.id.sat_frmtme_txt);
        CustomTextView sat_totme_txt = (CustomTextView)dialog.findViewById(R.id.sat_totme_txt);

        CustomTextView sun_frmtme_txt = (CustomTextView)dialog.findViewById(R.id.sun_frmtme_txt);
        CustomTextView sun_totme_txt = (CustomTextView)dialog.findViewById(R.id.sun_totme_txt);
        CustomTextView ws_web_txt = (CustomTextView)dialog.findViewById(R.id.ws_web_txt);

        CustomButton select_btn = (CustomButton)dialog.findViewById(R.id.select_btn);
        CustomButton cancel_btn = (CustomButton)dialog.findViewById(R.id.cancel_btn);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                filterdialog.dismiss();

                RegisterMain.warehouseid=wholesalerslist.get(j).get("wholesaler_id");
                RegisterMain.wholesalername=wholesalerslist.get(j).get("name");
                RegisterMain.warehousecontact=wholesalerslist.get(j).get("contact_name");
                RegisterMain.warehouseemail=wholesalerslist.get(j).get("email");
                RegisterMain.warehousephone=wholesalerslist.get(j).get("phone");
                RegisterMain.warehouseaddress=wholesalerslist.get(j).get("street");
                wholesalers_name.setText(StoredObjects.stripHtml(wholesalerslist.get(j).get("name")));
            }
        });

        ws_direction_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double lattitude= 0.0;
                    Double longitude= 0.0;
                    lattitude = Double.parseDouble(wholesalerslist.get(j).get("lat"));
                    longitude = Double.parseDouble(wholesalerslist.get(j).get("lng"));

                    String url = "https://www.google.com/maps/dir/?api=1&destination=" + lattitude + "," + longitude+ "&travelmode=driving";
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url));
                    activity.startActivity(intent);
                }catch (Exception e){

                }
            }
        });

    */
/*    monday_closed	"No"
        tuesday_closed	"No"
        wednesday_closed	"No"
        thursday_closed	"No"
        friday_closed	"No"
        saturday_closed	"No"
        sunday_closed	"No"
        lat	"-33.42571"
        lng	"149.619951"*//*


        String m_starthr="",m_endhr="",t_starthr="",t_endhr="",w_starthr="",w_endhr="",ts_starthr="",ts_endhr=""
                ,f_starthr="",f_endhr="",st_starthr="",st_endhr="",s_starthr="",s_endhr="";

        if(wholesalerslist.get(j).get("sunday_hours").contains("-")&&wholesalerslist.get(j).get("sunday_hours").length()>2){
            String[] sunday_hours=wholesalerslist.get(j).get("sunday_hours").split("-");
            s_starthr=sunday_hours[0];
            s_endhr=sunday_hours[1];
        }else{
            s_starthr=wholesalerslist.get(j).get("sunday_hours");
            s_endhr=wholesalerslist.get(j).get("sunday_hours");
        }
        if(wholesalerslist.get(j).get("saturday_hours").contains("-")&&wholesalerslist.get(j).get("saturday_hours").length()>2){
            String[] saturday_hours=wholesalerslist.get(j).get("saturday_hours").split("-");
            st_starthr=saturday_hours[0];
            st_endhr=saturday_hours[1];
        }else{
            st_starthr=wholesalerslist.get(j).get("saturday_hours");
            st_endhr=wholesalerslist.get(j).get("saturday_hours");
        }
        if(wholesalerslist.get(j).get("friday_hours").contains("-")&&wholesalerslist.get(j).get("friday_hours").length()>2){
            String[] friday_hours=wholesalerslist.get(j).get("friday_hours").split("-");
            f_starthr=friday_hours[0];
            f_endhr=friday_hours[1];
        }else{
            f_starthr=wholesalerslist.get(j).get("thursday_hours");
            f_endhr=wholesalerslist.get(j).get("thursday_hours");
        }
        if(wholesalerslist.get(j).get("thursday_hours").contains("-")&&wholesalerslist.get(j).get("thursday_hours").length()>2){
            String[] thursday_hours=wholesalerslist.get(j).get("thursday_hours").split("-");
            ts_starthr=thursday_hours[0];
            ts_endhr=thursday_hours[1];
        }else{
            ts_starthr=wholesalerslist.get(j).get("thursday_hours");
            ts_endhr=wholesalerslist.get(j).get("thursday_hours");
        }

        if(wholesalerslist.get(j).get("wednesday_hours").contains("-")&&wholesalerslist.get(j).get("wednesday_hours").length()>2){
            String[] wednesday_hours=wholesalerslist.get(j).get("wednesday_hours").split("-");
            w_starthr=wednesday_hours[0];
            w_endhr=wednesday_hours[1];
        }else{
            w_starthr=wholesalerslist.get(j).get("wednesday_hours");
            w_endhr=wholesalerslist.get(j).get("wednesday_hours");
        }
        if(wholesalerslist.get(j).get("monday_hours").contains("-")&&wholesalerslist.get(j).get("monday_hours").length()>2){
            String[] monday_hours=wholesalerslist.get(j).get("monday_hours").split("-");
            m_starthr=monday_hours[0];
            m_endhr=monday_hours[1];
        }else{
            m_starthr=wholesalerslist.get(j).get("monday_hours");
            m_endhr=wholesalerslist.get(j).get("monday_hours");
        }
        if(wholesalerslist.get(j).get("tuesday_hours").contains("-")&&wholesalerslist.get(j).get("tuesday_hours").length()>2){
            String[] tuesday_hours=wholesalerslist.get(j).get("tuesday_hours").split("-");
            t_starthr=tuesday_hours[0];
            t_endhr=tuesday_hours[1];
        }else{
            t_starthr=wholesalerslist.get(j).get("tuesday_hours");
            t_endhr=wholesalerslist.get(j).get("tuesday_hours");
        }
        String address1="";
        String address2="";
        String address3="";
        String address4="";
        String address5="";

        if(wholesalerslist.get(j).get("street").equalsIgnoreCase("-")||wholesalerslist.get(j).get("street").equalsIgnoreCase("null")){
            address1=wholesalerslist.get(j).get("description");
        }else{
            address1=wholesalerslist.get(j).get("description")
                    +", "+ wholesalerslist.get(j).get("street");
        }
        if(wholesalerslist.get(j).get("city").equalsIgnoreCase("-")||wholesalerslist.get(j).get("city").equalsIgnoreCase("null")){
            address2="";
        }else{
            address2=", "+wholesalerslist.get(j).get("city");
        }
        if(wholesalerslist.get(j).get("postcode").equalsIgnoreCase("-")||wholesalerslist.get(j).get("postcode").equalsIgnoreCase("null")){
            address3="";
        }else{
            address3=", "+wholesalerslist.get(j).get("postcode");
        }
        if(wholesalerslist.get(j).get("short_name").equalsIgnoreCase("-")||wholesalerslist.get(j).get("short_name").equalsIgnoreCase("null")){
            address4="";
        }else{
            address4=", "+wholesalerslist.get(j).get("short_name");
        }

        address5=address1+address2+address3+address4;
        ws_address_txt.setText(address5);

        ws_name_txt.setText(StoredObjects.stripHtml(wholesalerslist.get(j).get("name")));
        ws_phone_txt.setText(wholesalerslist.get(j).get("phone"));
        ws_fax_txt.setText(wholesalerslist.get(j).get("fax"));
        ws_email_txt.setText(wholesalerslist.get(j).get("email"));
        mn_fromtme_txt.setText(m_starthr);
        mn_totme_txt.setText(m_endhr);
        tue_fromtme_txt.setText(t_starthr);
        tue_totme_txt.setText(t_endhr);
        wed_frmtme_txt.setText(w_starthr);
        wed_totme_txt.setText(w_endhr);
        thurs_frmtme_txt.setText(ts_starthr);
        thurs_totme_txt.setText(ts_endhr);
        fri_fromtme_txt.setText(f_starthr);
        fri_totme_txt.setText(f_endhr);
        sat_frmtme_txt.setText(st_starthr);
        sat_totme_txt.setText(st_endhr);
        sun_frmtme_txt.setText(s_starthr);
        sun_totme_txt.setText(s_endhr);
        ws_web_txt.setText(wholesalerslist.get(j).get("web"));

        ws_web_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wholesalerslist.get(j).get("web").equalsIgnoreCase("")||
                        wholesalerslist.get(j).get("web").equalsIgnoreCase("-")){

                }else{


                    Intent myWebLink = new Intent( Intent.ACTION_VIEW);
                    myWebLink.setData( Uri.parse("http://"+wholesalerslist.get(j).get("web")));
                    getActivity().startActivity(myWebLink);
                }

            }
        });


        ws_email_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendEmail(wholesalerslist.get(j).get("email"));

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

        ImageView cancel_img = (ImageView)dialog.findViewById(R.id.cancel_img);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }
*/
 public class GetProfileTask extends AsyncTask<String, String, String> {
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
            nameValuePairs.add( new BasicNameValuePair( "method", "get-profile" ) );
            nameValuePairs.add( new BasicNameValuePair( "customer_id", params[0] ) );
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
                getprofilelist = JsonParsing.GetJsonData( results );
                String followerscount = getprofilelist.get( 0 ).get( "followers_count" );
                String followingcount = getprofilelist.get( 0 ).get( "following_count" );

                String catches_count = getprofilelist.get( 0 ).get( "catches_count" );



                try{
                    String on_new_user_followed = getprofilelist.get(0).get("on_new_user_followed");
                    String on_another_user_commented_on_your_post = getprofilelist.get(0).get("on_another_user_commented_on_your_post");
                    String on_new_post_by_user_you_following = getprofilelist.get(0).get("on_new_post_by_user_you_following");
                    String on_another_user_liked_your_post = getprofilelist.get(0).get("on_another_user_liked_your_post");
                    String on_new_post_by_your_follower = getprofilelist.get(0).get("on_new_post_by_your_follower");


                    StoredObjects.savedata(getActivity(),on_new_user_followed,"on_new_user_followed");
                    StoredObjects.savedata(getActivity(),on_another_user_commented_on_your_post,"on_another_user_commented_on_your_post");
                    StoredObjects.savedata(getActivity(),on_new_post_by_user_you_following,"on_new_post_by_user_you_following");
                    StoredObjects.savedata(getActivity(),on_another_user_liked_your_post,"on_another_user_liked_your_post");
                    StoredObjects.savedata(getActivity(),on_new_post_by_your_follower,"on_new_post_by_your_follower");


                }catch (Exception e){

                }

                if (followerscount.equalsIgnoreCase( "0" )){
                    shimmer_view_container_follwers.setVisibility( View.GONE );
                    followers_recycle.setVisibility( View.GONE );
                    nodatafound_txt.setVisibility( View.VISIBLE );
                }
                else {
                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        new GetFollowersTask().execute(StoredObjects.UserId);
                    }else{
                        StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                    }
                }
                if (followingcount.equalsIgnoreCase( "0" )){
                    following_layout.setVisibility( View.GONE );
                    following_recycle.setVisibility( View.GONE );
                    no_folloingdata_found.setVisibility( View.VISIBLE );
                }
                else {
                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        new GetFollowingTask().execute(StoredObjects.UserId);
                    }else{
                        StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                    }
                }





                try{

                    if (catches_count.equalsIgnoreCase( "0" )){
                         catches_shimmerlay.setVisibility( View.GONE );
                        catches_recycle.setVisibility( View.GONE );
                        no_catches_found.setVisibility( View.VISIBLE );
                    }
                    else {

                          catches_shimmerlay.setVisibility( View.GONE );
                        no_catches_found.setVisibility(View.GONE);
                        catches_recycle.setVisibility( View.VISIBLE );
                        catches = JsonParsing.GetJsonData( getprofilelist.get( 0 ).get( "catches" ) );
                        // customRecyclerview.Assigndatatorecyleviewhashmap( following_recycle, followinglist, "profile_following", StoredObjects.Listview, 0, StoredObjects.horizontal_orientation, R.layout.prfle_following_listitems );
                        customRecyclerview.Assigndatatorecyleviewhashmap( catches_recycle, catches, "profile_catches", StoredObjects.Listview, 0, StoredObjects.horizontal_orientation, R.layout.catches_listitems );

                    }

                }catch (Exception e){
                    e.printStackTrace();
                    StoredObjects.LogMethod("<><>>","<><><><><>>"+"clicked"+e);

                }



                StoredObjects.LogMethod("<><>>","<><><><><>>"+"clicked"+catches.size());


                try{
                    assigndatatomap( catches );
                }catch (Exception e){

                }







                shimmer_view_container__textviews.setVisibility(View.GONE);
                top_profile_layout.setVisibility(View.VISIBLE);
                profile_txt.setText( getprofilelist.get( 0 ).get( "name" ) );
                custm_title_txt .setText( getprofilelist.get( 0 ).get( "about" ) );
                profile_address_text .setText( getprofilelist.get( 0 ).get( "house_number" ) );


                try{
                    Glide.with(getActivity ())
                            .load(Uri.parse(StoredUrls.Uploadedimages + getprofilelist.get(0).get("image"))) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder(R.drawable.splash_logo_new)
                            .override(600, 200)
                            .into(profile_img);
                }catch (Exception e){
                }

            } else {
                shimmer_view_container__textviews.setVisibility(View.GONE);
                top_profile_layout.setVisibility(View.VISIBLE);
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

    public class GetFollowersTask extends AsyncTask<String, String, String> {
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
                nameValuePairs.add( new BasicNameValuePair( "method", "followers" ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id", params[0] ) );
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
                    followerslist = JsonParsing.GetJsonData( results );
                    if (followerslist.size()>0){
                        shimmer_view_container_follwers.setVisibility( View.GONE );
                        followers_recycle.setVisibility(View.VISIBLE);
                        nodatafound_txt.setVisibility( View.GONE );
                        customRecyclerview.Assigndatatorecyleviewhashmap( followers_recycle, followerslist, "profile_followers", StoredObjects.Listview, 0, StoredObjects.horizontal_orientation, R.layout.prfle_followers_listitems );
                    }
                } else {
                    shimmer_view_container_follwers.setVisibility( View.GONE );
                    followers_recycle.setVisibility( View.GONE );
                    nodatafound_txt.setVisibility( View.VISIBLE );
                }
              /*  if (InterNetChecker.isNetworkAvailable(getActivity())) {
                    new GetFollowingTask().execute(StoredObjects.UserId);
                }else{
                    StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
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
                    followinglist = JsonParsing.GetJsonData( results );
                    if (followinglist.size()>0){
                        following_layout.setVisibility( View.GONE );
                        no_folloingdata_found.setVisibility(View.GONE);
                        following_recycle.setVisibility( View.VISIBLE );
                        customRecyclerview.Assigndatatorecyleviewhashmap( following_recycle, followinglist, "profile_following", StoredObjects.Listview, 0, StoredObjects.horizontal_orientation, R.layout.prfle_following_listitems );
                    }
                } else {
                    following_layout.setVisibility( View.GONE );
                    following_recycle.setVisibility( View.GONE );
                    no_folloingdata_found.setVisibility( View.VISIBLE );
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