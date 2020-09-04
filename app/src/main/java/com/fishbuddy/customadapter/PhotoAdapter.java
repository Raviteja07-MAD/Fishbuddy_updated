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
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fishbuddy.R;
import com.fishbuddy.dumpdata.DumpData;
import com.fishbuddy.fragments.Viewpager_gallery;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;

import java.util.ArrayList;
import java.util.HashMap;

public class PhotoAdapter  extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>  implements Filterable {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final int HEADER_ITEM = 2;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 8;
    private int lastVisibleItem, totalItemCount;

    Activity activity;
    public  static  ArrayList<HashMap<String, String>> datalist=new ArrayList<>();  //ArrayList<HashMap<String, String>> datalist;
    ArrayList<HashMap<String, Integer>> datalist1=new ArrayList<>();  //ArrayList<HashMap<String, String>> datalist;

    ArrayList<DumpData> datalist_dump=new ArrayList<>();

    String formtype;
    String page_type ="";
    int list_itemview;

    private ItemFilter mFilter = new ItemFilter();

    public PhotoAdapter(Activity activity2, ArrayList<HashMap<String, String>> data_list, String string, RecyclerView recyclerView, int recylerviewlistitem) { //ArrayList<HashMap<String, String>> data_list

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
    public PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(list_itemview, parent, false);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
// layoutParams.height = (int) (width * 0.3);
            layoutParams.width = (int) (width * 0.4);
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
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder1, final int position) {


            ViewHolder viewHolder = (ViewHolder) holder1;


            try {
                StoredObjects.LogMethod( "<<><>>>>", "><><><><>>123" + StoredUrls.Uploadedimages+datalist.get( position ).get( "image" ) );
                Glide.with( activity )
                        .load( Uri.parse( StoredUrls.Uploadedimages+datalist.get( position ).get( "image" ) ) ) // add your image url
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .fitCenter() // scale to fit entire image within ImageView
                        .placeholder( R.drawable.splash_logo_new )
                        // .diskCacheStrategy( DiskCacheStrategy.SOURCE )
                        // .crossFade()
                        //.override(600,600)
                        .into( viewHolder.image_view_icon );
                //  adapter.notifyItemInserted(position);

            } catch (Exception e) {
            }

            viewHolder.image_view_icon.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent( activity, Viewpager_gallery.class );
                    intent.putExtra( "position",position+"" );
                    intent.putExtra("YourHashMap", datalist);
                    activity.startActivity( intent );
                }
            } );


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

    private class LoadingViewHolder extends PhotoAdapter.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById( R.id.progressBar_new);
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_view_icon;

        public ViewHolder(View convertView) {
            super(convertView);

            image_view_icon = convertView.findViewById(R.id.image_view_icon);
        }
    }

}
