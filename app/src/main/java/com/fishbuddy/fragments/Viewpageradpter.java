package com.fishbuddy.fragments;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.fishbuddy.R;
import com.fishbuddy.customfonts.CustomEditText;
import com.fishbuddy.dumpdata.DumpData;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by android-4 on 2/28/2019.
 */

public class Viewpageradpter extends PagerAdapter {

    Context context;
    ArrayList<DumpData> datalist;

    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> images_array_d;

    public Viewpageradpter(Context context, ArrayList<DumpData> dumpDatas,ArrayList<HashMap<String, String>> images_array) {

        this.context = context;
        this.datalist = dumpDatas;
        images_array_d = images_array;
    }

    @Override
    public int getCount() {
        return images_array_d.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate( R.layout.multipleimageslistitem_viewpager, container, false);
        TouchImageView image_view_icon = v.findViewById(R.id.image_view_icon);

        try {
            StoredObjects.LogMethod( "<<><>>>>", "><><><><>>123" + images_array_d.get( position ).get( "image" ) );
            //StoredUrls.Uploadedimages +
            Glide.with( context )
                    .load( Uri.parse(StoredUrls.Uploadedimages + images_array_d.get( position ).get( "image" ) ) ) // add your image url
                    .centerCrop() // scale to fill the ImageView and crop any extra
                    .fitCenter() // scale to fit entire image within ImageView
                    .placeholder( R.drawable.splash_logo_new )
                    // .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                    // .crossFade()
                    //.override(600,600)
                    .into( image_view_icon );
            //  adapter.notifyItemInserted(position);

        } catch (Exception e) {
        }


        StoredObjects.LogMethod( "><><><><",">><><<><>"+ datalist);

       /* if(position == 0){
            unicorn_edt_dtls.setText(datalist.get(0).homepage_unicorn_txt_detls);
        }if(position == 1){
            unicorn_edt_dtls.setText(datalist.get(1).homepage_unicorn_txt_detls);
        }
        if(position == 2){
            unicorn_edt_dtls.setText(datalist.get(2).homepage_unicorn_txt_detls);
        }
*/

        ((ViewPager) container).addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
