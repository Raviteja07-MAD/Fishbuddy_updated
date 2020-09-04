package com.fishbuddy.customadapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fishbuddy.R;
import com.fishbuddy.circularimageview.CircularImageView;
import com.fishbuddy.customfonts.CustomBoldTextView;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.dumpdata.DumpData;
import com.fishbuddy.fragments.FishingSpotsDetailsActivity;
import com.fishbuddy.fragments.Viewpager_gallery;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;

import java.util.ArrayList;
import java.util.HashMap;

public class FishingClubsAdapter extends RecyclerView.Adapter<FishingClubsAdapter.ViewHolder>  implements Filterable {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final int HEADER_ITEM = 2;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 8;
    private int lastVisibleItem, totalItemCount;

    Activity activity;
    public  static ArrayList<HashMap<String, String>> datalist=new ArrayList<>();  //ArrayList<HashMap<String, String>> datalist;
    ArrayList<HashMap<String, Integer>> datalist1=new ArrayList<>();  //ArrayList<HashMap<String, String>> datalist;

    ArrayList<DumpData> datalist_dump=new ArrayList<>();

    String formtype;
    String page_type ="";
    int list_itemview;

    private ItemFilter mFilter = new ItemFilter();

    public FishingClubsAdapter(Activity activity2, ArrayList<HashMap<String, String>> data_list, String string, RecyclerView recyclerView, int recylerviewlistitem) { //ArrayList<HashMap<String, String>> data_list

        this.datalist = data_list;
        this.activity = activity2;
        formtype = string;
        list_itemview=recylerviewlistitem;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();

                    }
                    isLoading = true;
                }
            }
        });
    }


    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {

        if(page_type.equalsIgnoreCase ("dumpdata")){
            return datalist_dump.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }else{
            return datalist.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

    }
    @Override
    public Filter getFilter() {
        return mFilter;
    }


    @NonNull
    @Override
    public FishingClubsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(list_itemview, parent, false);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
// layoutParams.height = (int) (width * 0.3);
           // layoutParams.width = (int) (width * 0.4);
            view.setLayoutParams(layoutParams);

            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;

        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = null;
            view = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false);


            return new LoadingViewHolder(view);
        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FishingClubsAdapter.ViewHolder holder1, final int position) {


        ViewHolder viewHolder = (ViewHolder) holder1;



        viewHolder.lakename_txt.setText(datalist.get(position).get("name"));
        viewHolder.lakelocation_txt.setText(datalist.get(position).get("address"));
        viewHolder.lakedetails_txt.setText(datalist.get(position).get("description"));

        viewHolder.area_count_txt.setText(datalist.get(position).get("lat"));
        viewHolder.maxdepth_count_txt.setText(datalist.get(position).get("lat"));
        try {

            ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
            images_array.clear();
            images_array = JsonParsing.GetJsonData(datalist.get(position).get("fishing_club_images"));
            if (images_array.size() > 0) {
                Glide.with(activity)
                        .load(Uri.parse(StoredUrls.Uploadedimages + images_array.get(0).get("image"))) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder(R.drawable.splash_logo_new)
                        // .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        // .crossFade()
                        //.override(600,600)
                        .into(viewHolder.lake_image);
            }
            StoredObjects.LogMethod("<><>>","<><><><><>>"+"post_type"+StoredUrls.Uploadedimages + images_array.get(0).get("image"));


        } catch (Exception e) {
            e.printStackTrace();
        }




        viewHolder.fishingspots_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FishingSpotsDetailsActivity.class);
                intent.putExtra("YourHashMap", datalist);
                intent.putExtra("position", position);
                activity.startActivity(intent);
               // fragmentcalling1(new Fishingspots_details(),datalist,position);
            }
        });
        viewHolder.directions_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //  String url = "https://www.google.com/maps/dir/?api=1&destination=" + datalist.get(position).get("lat") + "° N" + "," +datalist.get(position).get("lng") + "° W" /*"&travelmode=driving"*/;

                    String url = "https://www.google.com/maps/dir/?api=1&destination=" + datalist.get(position).get("lat") + "," +datalist.get(position).get("lng") /*"&travelmode=driving"*/;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    activity.startActivity(intent);
                } catch (Exception e) {
                    StoredObjects.ToastMethod(" Address Not found", activity);
                }
            }
        });






    }

    @Override
    public int getItemCount() {
        return datalist == null ? 0 : datalist.size();
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            ArrayList<HashMap<String, String>> list = datalist;
            int count = list.size();
            ArrayList<HashMap<String, String>> nlist=new ArrayList<>();
            String filterableString = null;
            for (int i = 0; i < count; i++) {

                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(datalist.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            datalist = (ArrayList<HashMap<String, String>>) results.values;
            notifyDataSetChanged();
        }

    }

    private class LoadingViewHolder extends ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById( R.id.progressBar_new);
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout fishingspots_lay, directions_lay;
        CustomBoldTextView lakename_txt;
        CustomRegularTextView lakelocation_txt,lakedetails_txt, maxdepth_count_txt,area_count_txt,directions_txt;
        CircularImageView lake_image;

        public ViewHolder(View convertView) {
            super(convertView);

            fishingspots_lay = convertView.findViewById(R.id.fishingspots_lay);
            directions_lay = convertView.findViewById(R.id.directions_lay);
            lakename_txt = convertView.findViewById(R.id.lakename_txt);
            lakelocation_txt = convertView.findViewById(R.id.lakelocation_txt);
            lakedetails_txt = convertView.findViewById(R.id.lakedetails_txt);
            lake_image = convertView.findViewById(R.id.lake_image);
            maxdepth_count_txt = convertView.findViewById(R.id.maxdepth_count_txt);
            area_count_txt = convertView.findViewById(R.id.area_count_txt);
            directions_txt = convertView.findViewById(R.id.directions_txt);
        }
    }

}
