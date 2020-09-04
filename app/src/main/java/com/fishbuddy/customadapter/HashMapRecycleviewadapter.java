package com.fishbuddy.customadapter;

import android.app.Activity;
import android.os.Build;


import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;


import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fishbuddy.R;
import com.fishbuddy.dumpdata.DumpData;
import com.fishbuddy.storedobjects.StoredObjects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by VINI on 02-11-2018.
 */

public class HashMapRecycleviewadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements Filterable {

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


    public HashMapRecycleviewadapter(Activity activity2, ArrayList<HashMap<String, String>> data_list, String string, RecyclerView recyclerView, int recylerviewlistitem) { //ArrayList<HashMap<String, String>> data_list

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


    public HashMapRecycleviewadapter(Activity activity2, ArrayList<DumpData> data_list, String string, RecyclerView recyclerView, int recylerviewlistitem, String newtype) { //ArrayList<HashMap<String, String>> data_list

        this.datalist_dump = data_list;
        this.activity = activity2;
        formtype = string;
        page_type = newtype;
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(list_itemview, parent, false);
           // return new HashmapViewHolder (view,formtype,activity);

            if(formtype.equalsIgnoreCase("fishingspots_multipleimages")){
                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
// layoutParams.height = (int) (width * 0.3);
                layoutParams.width = (int) (width * 0.4);

                view.setLayoutParams(layoutParams);
                return new HashmapViewHolder (view,formtype,activity);
            }
            else{
                return new HashmapViewHolder (view,formtype,activity);
            }

        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = null;
            view = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false);


            return new LoadingViewHolder(view);
        }

       /* View view = LayoutInflater.from(activity).inflate(list_itemview, parent, false);
        return new HashmapViewHolder (view,formtype,activity);*/

       return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {

        /*if (holder1 instanceof HashmapViewHolder) {
            HashmapViewHolder holder = (HashmapViewHolder) holder1;
            holder.assign_data(datalist,position,formtype);


        }*/

        if(page_type.equalsIgnoreCase ("dumpdata")){
            if (holder1 instanceof HashmapViewHolder) {
                HashmapViewHolder holder = (HashmapViewHolder) holder1;
                //holder.assign_data123(datalist_dump,position,formtype);

            } else if (holder1 instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder1;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }

            }
        else if (holder1 instanceof HashmapViewHolder) {
            HashmapViewHolder holder = (HashmapViewHolder) holder1;
            holder.assign_data(datalist,position,formtype);

        } else if (holder1 instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder1;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {


        if(page_type.equalsIgnoreCase ("dumpdata")){
            return datalist_dump == null ? 0 : datalist_dump.size();
        }else{
            return datalist == null ? 0 : datalist.size();
        }


    }

    public void setLoaded() {
        isLoading = false;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById( R.id.progressBar_new);
        }
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


   /* @Override
    public int getItemViewType(int position)
    {


        return datalist.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
*/

}
