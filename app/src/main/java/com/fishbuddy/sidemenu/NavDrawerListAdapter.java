package com.fishbuddy.sidemenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.RequiresApi;

import com.fishbuddy.R;
import com.fishbuddy.storedobjects.StoredObjects;

import java.util.ArrayList;

/**
 * Created by android-4 on 5/31/2018.
 */

public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    public static TextView txtTitle,ntfc_count_txt;
    public static LinearLayout navigation_lay;

    public static ImageView icon;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
            context.getSystemService( Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate( R.layout.sidemenu_drawablelist, null,false);
        }

        txtTitle=(TextView) convertView.findViewById(R.id.title);
        ntfc_count_txt = (TextView) convertView.findViewById(R.id.ntfc_count_txt);
        ImageView image_icon= (ImageView) convertView.findViewById(R.id.image_icon);
        navigation_lay = (LinearLayout) convertView.findViewById(R.id.navigation_lay);


        txtTitle.setText(navDrawerItems.get(position).getTitle());
        image_icon.setImageResource(navDrawerItems.get(position).getimage());

        String ntfccount = StoredObjects.notific_count;
        ntfc_count_txt.setText( ntfccount);
      /*  if (ntfccount.equalsIgnoreCase( "" )){
            ntfc_count_txt.setVisibility( View.GONE );
        }else {
            ntfc_count_txt.setVisibility( View.GONE );
        }*/

        if (position==2){
            txtTitle.setTypeface( txtTitle.getTypeface(), Typeface.BOLD );
          //  txtTitle.setTextSize( 18 );
           // txtTitle.setTextColor( context.getResources().getColor(R.color.black));
            image_icon.setVisibility( View.GONE );
        }
        if (position == 2){

            ntfc_count_txt.setVisibility( View.GONE );

        }
        else {
            ntfc_count_txt.setVisibility( View.GONE );

        }

        /*if(StoredObjects.count == position){
            txtTitle.setTextColor(Color.parseColor("#FFFFFF"));
            navigation_lay.setBackgroundResource(R.color.colorPrimary);
        }else{
            txtTitle.setTextColor(R.color.black);
            navigation_lay.setBackgroundResource(R.color.white);
        }*/


        return convertView;
    }

}

