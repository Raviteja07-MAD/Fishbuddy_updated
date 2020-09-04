package com.fishbuddy.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.dumpdata.DumpData;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.StoredObjects;

import java.util.ArrayList;
import java.util.HashMap;

public class Viewpager_gallery extends Activity {

    ViewPager viewpager;
    LinearLayout viewPagerDots;
    ArrayList<HashMap<String, String>> fishspotdetails;
    int position = 0;
    String position_;

    Bundle bundle;
    Handler handler;
    Runnable animateViewPager;
    TextView[] dots;
    final long ANIM_VIEWPAGER_DELAY = 4000;
    boolean stopSliding = false;
    TextView title_txt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(  R.layout.fishingspots_viewpager_popup );
        Intent intent = getIntent();

        if (intent!=null){
            position_ = intent.getStringExtra( "position" );
            position = Integer.parseInt( position_ );
            fishspotdetails = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra( "YourHashMap" );
        }
        StoredObjects.LogMethod("status_res", "images_array_fishingspots:--<><sizeinneer>"+position_);

        initilization();
    }

    private void initilization() {


        viewpager = (ViewPager)findViewById(R.id.viewpager);
        viewPagerDots = (LinearLayout)findViewById(R.id.viewPagerDots);
        Viewpageradpter adapter = new Viewpageradpter(Viewpager_gallery.this, null,fishspotdetails);
        viewpager.setAdapter(adapter);


        handler = new Handler();
        viewpagerdatainitialisation();
        setUiPageViewController(fishspotdetails.size());

        title_txt = (TextView)findViewById( R.id. title_txt);
        title_txt.setText("photos");
        ImageView backbtn_img = (ImageView)findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        } );
    }

    public void viewpagerdatainitialisation() {

        try {
            viewpager.setCurrentItem(position);
            viewpager.setOnPageChangeListener(viewPagerPageChangeListener);

          //  runnable(3);
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);

        } catch (Exception e) {
            // TODO: handle exception
        }

    }


    public void setUiPageViewController(int size) {

        try {

            dots = new TextView[size];

            for (int i = 0; i < size; i++) {
                dots[i] = new TextView(Viewpager_gallery.this);
                dots[i].setText( Html.fromHtml("&#8226;"));
                dots[i].setTextSize(50);

                dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));

                viewPagerDots.addView(dots[i]);
            }

            dots[0].setTextColor(getResources().getColor(R.color.orange));

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            try {

                for (int i = 0; i < fishspotdetails.size(); i++) {
                    dots[i].setTextColor(getResources().getColor(R.color.form_text_color));

                }
                dots[position].setTextColor(getResources().getColor(R.color.orange));

            } catch (Exception e) {
                // TODO: handle exception
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    public void runnable(final int size) {
        //handler = new Handler();
        try {
            if (Viewpager_gallery.this!= null) {

                animateViewPager = new Runnable() {
                    public void run() {
                        if (!stopSliding) {
                            if (viewpager.getCurrentItem() == size - 1) {
                                viewpager.setCurrentItem(0);
                            } else {
                                viewpager.setCurrentItem(
                                        viewpager.getCurrentItem() + 1, true);
                            }
                            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                        }
                    }
                };
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }



}
