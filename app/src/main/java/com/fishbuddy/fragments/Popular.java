package com.fishbuddy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.StoredObjects;

public class Popular extends Fragment {
    RecyclerView popular_recyclerview;
    CustomRecyclerview customRecyclerview;
    TextView title_txt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.popular,null,false );
        StoredObjects.page_type="home";
        StoredObjects.back_type="popular";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        return v;
    }

    private void initilization(View v) {
        popular_recyclerview = (RecyclerView) v.findViewById( R.id.popular_recyclerview );
        customRecyclerview = new CustomRecyclerview(getActivity());

        title_txt = (TextView)v.findViewById( R.id. title_txt);
        title_txt.setText( R.string.popular );

        ImageView backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();

            }
        } );


        StoredObjects.hashmaplist(8);
          customRecyclerview.Assigndatatorecyleviewhashmap(popular_recyclerview, StoredObjects.dummy_list,"popular", StoredObjects.Gridview, 2, StoredObjects.ver_orientation, R.layout.popular_listitems);


    }
}
