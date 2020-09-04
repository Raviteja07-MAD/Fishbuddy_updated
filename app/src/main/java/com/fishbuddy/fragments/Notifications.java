package com.fishbuddy.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.servicesparsing.CustomProgressbar;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;

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

import static com.fishbuddy.sidemenu.SideMenu.btm_notifications_img;
import static com.fishbuddy.sidemenu.SideMenu.btm_notifications_lay;
import static com.fishbuddy.sidemenu.SideMenu.btm_notifications_txt;
import static com.fishbuddy.sidemenu.SideMenu.buttonchangemethod;

public class Notifications extends Fragment {
    RecyclerView notifications_recyclerview;
    CustomRecyclerview customRecyclerview;
    TextView title_txt;
    public ArrayList<HashMap<String, String>> getpostlist = new ArrayList<>();
    public ArrayList<HashMap<String, String>> getpostlistall = new ArrayList<>();
    String total_results = "0",total_pages = "0",results_count = "0";
    int pageCount = 1;
    public static HashMapRecycleviewadapter adapter;
    CustomRegularTextView nodatafound_txt;

    ImageView delete_icon,edit_icon;

    int editcount = 0;
    ArrayList<String> seleted_postions = new ArrayList<>();
    ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.notifications,null,false );
        StoredObjects.page_type="notifications";
        StoredObjects.back_type="notifications";
        buttonchangemethod (getActivity() , btm_notifications_lay , btm_notifications_img , btm_notifications_txt , "3");
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        StoredObjects.LogMethod("val","val:::"+"yes"+getpostlist.size());
        loadpostsdata(pageCount);
        return v;
    }

    public void loadpostsdata(int pageCount){

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new NotificationTask().execute(StoredObjects.UserId,pageCount+"");
        }else{
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
        }
    }

    private void initilization(View v) {
        notifications_recyclerview = (RecyclerView) v.findViewById( R.id.notifications_recyclerview );
        customRecyclerview = new CustomRecyclerview(getActivity());
        nodatafound_txt = (CustomRegularTextView)v.findViewById( R.id.nodatafound_txt );
        title_txt = (TextView)v.findViewById( R.id. title_txt);
        title_txt.setText( R.string.notfications );
        ImageView settings_img = (ImageView)v.findViewById( R.id.settings_img );

        delete_icon = (ImageView)v.findViewById( R.id.delete_icon );
        edit_icon = (ImageView)v.findViewById( R.id.edit_icon );


        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Selected_ids = "";
                seleted_postions.clear();

                for (int i = 0; i < HashMapRecycleviewadapter.datalist.size(); i++) {

                    if(HashMapRecycleviewadapter.datalist.get(i).get("addedtocart").equalsIgnoreCase("Yes")){
                        Selected_ids +=  HashMapRecycleviewadapter.datalist.get(i).get("notification_id")+",";
                        seleted_postions.add(HashMapRecycleviewadapter.datalist.get(i).get("notification_id"));
                    }

                }
                StoredObjects.LogMethod( "response", "Selected_ids:---" + Selected_ids );
                if(Selected_ids.length()>1){
                    Selected_ids = Selected_ids.substring(0,Selected_ids.length()-1);
                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        new DeleteNotificationTask().execute(Selected_ids);
                    }else{
                        StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                    }

                }


            }
        });
        edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(editcount == 0){
                    editcount=1;
                    edit_icon.setImageResource(R.drawable.cancel_icon_ne);
                    delete_icon.setVisibility(View.VISIBLE);
                    filter_list = dataupdatemethodfordelete(HashMapRecycleviewadapter.datalist,"Yes");
                    adapter = new HashMapRecycleviewadapter( getActivity(), filter_list, "notifications", notifications_recyclerview, R.layout.notifications_listitems );
                    notifications_recyclerview.setAdapter( adapter );




                }else{
                    editcount = 0;
                    edit_icon.setImageResource(R.drawable.edit_icon_n);
                    delete_icon.setVisibility(View.INVISIBLE);
                    filter_list = dataupdatemethodfordelete(HashMapRecycleviewadapter.datalist,"No");
                    adapter = new HashMapRecycleviewadapter( getActivity(), filter_list, "notifications", notifications_recyclerview, R.layout.notifications_listitems );
                    notifications_recyclerview.setAdapter( adapter );

                }

            }
        });


        settings_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentcallinglay( new Notifications_settings() );

            }
        } );

        ImageView backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               fragmentcallinglay( new Landing_page() );

            }
        } );
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        notifications_recyclerview.setLayoutManager(linearLayoutManager);


        notifications_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {


                    if(editcount == 0){
                        if(pageCount<Integer.parseInt(total_pages)){
                            pageCount=pageCount+1;
                            if (InterNetChecker.isNetworkAvailable(getActivity())) {
                                new NotificationLoadTask().execute(StoredObjects.UserId,pageCount+"");
                            }else{
                                StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
                            }

                        }
                    }
                    //pageCount=pageCount+1;



                }
            }
        });


      /*  StoredObjects.hashmaplist(10);
          customRecyclerview.Assigndatatorecyleviewhashmap(notifications_recyclerview, StoredObjects.dummy_list,"notifications", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.notifications_listitems);
    */

    }


    public ArrayList<HashMap<String ,String>> dataupdatemethodfordelete(ArrayList<HashMap<String ,String>> getpostlist,String val){
        ArrayList<HashMap<String ,String>> filterlist = new ArrayList<>();
        filterlist.clear();


        for (int j = 0; j < getpostlist.size(); j++) {
            HashMap<String, String> dumpData_update = new HashMap<String, String>();
            dumpData_update.put("notification_id", getpostlist.get(j).get("notification_id"));
            dumpData_update.put("title", getpostlist.get(j).get("title"));
            dumpData_update.put("description", getpostlist.get(j).get("description"));
            dumpData_update.put("status", getpostlist.get(j).get("status"));
            dumpData_update.put("created_at",getpostlist.get(j).get("created_at"));
            dumpData_update.put("notification_type", getpostlist.get(j).get("notification_type"));
            dumpData_update.put("post_id", getpostlist.get(j).get("post_id"));
            dumpData_update.put("is_following", getpostlist.get(j).get("is_following"));
            dumpData_update.put("comment_id", getpostlist.get(j).get("comment_id"));
            dumpData_update.put("created_customer_id", getpostlist.get(j).get("created_customer_id"));
            dumpData_update.put("comments", getpostlist.get(j).get("comments"));
            dumpData_update.put("profile_picture", getpostlist.get(j).get("profile_picture"));
            dumpData_update.put("customer_name", getpostlist.get(j).get("customer_name"));
            dumpData_update.put("addedtocart","No");
            dumpData_update.put("pro_quantity","0");
            dumpData_update.put("status_val","No");
            dumpData_update.put("sel_favorites",val);
            filterlist.add(dumpData_update);
        }

        return  filterlist;
    }




    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }


    public class NotificationTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
               CustomProgressbar.Progressbarshow( getActivity() );
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                //nameValuePairs.add( new BasicNameValuePair( "token", "Mikel") );
                nameValuePairs.add( new BasicNameValuePair( "method", "notifications" ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0] ) );
                nameValuePairs.add( new BasicNameValuePair( "page", params[1] ) );
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
                   CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    total_results = jsonObject.getString( "total_results" );
                    results_count = jsonObject.getString( "results_count" );
                    total_pages = jsonObject.getString( "total_pages" );
                   // progressBar1.setVisibility( View.GONE );

                    if(pageCount==1){
                        getpostlist.clear();
                        getpostlistall = JsonParsing.GetJsonData( results );
                        getpostlist.addAll(getpostlistall);
                       // mShimmerViewContainer.setVisibility(View.GONE);
                        notifications_recyclerview.setVisibility( View.VISIBLE );
                        nodatafound_txt.setVisibility( View.GONE );
                       // nodatafound_lay.setVisibility( View.GONE );
                        adapter = new HashMapRecycleviewadapter( getActivity(), getpostlist, "notifications", notifications_recyclerview, R.layout.notifications_listitems );
                        notifications_recyclerview.setAdapter( adapter );


                    }else{
                        getpostlistall = JsonParsing.GetJsonData(results);
                        getpostlist.addAll(getpostlistall);
                        adapter = new HashMapRecycleviewadapter( getActivity(), getpostlist, "notifications", notifications_recyclerview, R.layout.notifications_listitems );
                        notifications_recyclerview.setAdapter( adapter );
                        adapter.notifyDataSetChanged();
                        notifications_recyclerview.invalidate();
                    }


                }else{

                    if(getpostlist.size() > 0){
                        notifications_recyclerview.setVisibility(View.GONE);
                        adapter = new HashMapRecycleviewadapter( getActivity(), getpostlist, "notifications", notifications_recyclerview, R.layout.notifications_listitems );
                        notifications_recyclerview.setAdapter( adapter );
                        adapter.notifyDataSetChanged();
                        notifications_recyclerview.invalidate();
                        nodatafound_txt.setVisibility(View.VISIBLE);
                    }else{
                        notifications_recyclerview.setVisibility(View.GONE);
                        nodatafound_txt.setVisibility(View.VISIBLE);
                      //  mShimmerViewContainer.setVisibility(View.GONE);
                    }


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

    public class NotificationLoadTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
           // CustomProgressbar.Progressbarshow( getActivity() );
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                //nameValuePairs.add( new BasicNameValuePair( "token", "Mikel") );
                nameValuePairs.add( new BasicNameValuePair( "method", "notifications" ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id",params[0] ) );
                nameValuePairs.add( new BasicNameValuePair( "page", params[1] ) );
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
                    total_results = jsonObject.getString( "total_results" );
                    results_count = jsonObject.getString( "results_count" );
                    total_pages = jsonObject.getString( "total_pages" );
                    // progressBar1.setVisibility( View.GONE );

                    if(pageCount==1){
                        getpostlist.clear();
                        getpostlistall = JsonParsing.GetJsonData( results );
                        getpostlist.addAll(getpostlistall);
                        // mShimmerViewContainer.setVisibility(View.GONE);
                        notifications_recyclerview.setVisibility( View.VISIBLE );
                        // nodatafound_lay.setVisibility( View.GONE );
                        adapter = new HashMapRecycleviewadapter( getActivity(), getpostlist, "notifications", notifications_recyclerview, R.layout.notifications_listitems );
                        notifications_recyclerview.setAdapter( adapter );


                    }else{
                        getpostlistall = JsonParsing.GetJsonData(results);
                        getpostlist.addAll(getpostlistall);
                        //StoredObjects.LogMethod("val","val:::"+"yes"+getpostlist.size());
                        adapter.notifyDataSetChanged();
                        notifications_recyclerview.invalidate();
                    }


                }else{

                    if(getpostlist.size() == 0){
                        notifications_recyclerview.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        notifications_recyclerview.invalidate();
                        //nodatafound_txt.setVisibility(View.VISIBLE);
                    }else{
                        notifications_recyclerview.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        notifications_recyclerview.invalidate();
                        //nodatafound_txt.setVisibility(View.GONE);
                        //  mShimmerViewContainer.setVisibility(View.GONE);
                    }


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



    public class DeleteNotificationTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token", "Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method", "delete-notification"));
                nameValuePairs.add(new BasicNameValuePair("customer_id", StoredObjects.UserId));
                nameValuePairs.add(new BasicNameValuePair("notification_id", params[0]));
                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl, nameValuePairs);
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
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    //String results = jsonObject.getString( "results" );
                    StoredObjects.ToastMethod("Successfully Deleted.", getActivity());



                    for (int j = 0; j < seleted_postions.size(); j++) {
                        StoredObjects.LogMethod( "response", "Selected_ids<><>:---" + seleted_postions.get(j) );
                    }
                    if(seleted_postions.size()>0){
                        for (int i = 0; i < HashMapRecycleviewadapter.datalist.size(); i++) {

                            for (int j = 0; j < seleted_postions.size(); j++) {
                                if(HashMapRecycleviewadapter.datalist.get(i).get("notification_id") .equalsIgnoreCase(seleted_postions.get(j)) ){
                                    StoredObjects.LogMethod( "response", "Selected_ids<><innervals>:---" + i+"<><>"+seleted_postions.get(j));
                                    HashMapRecycleviewadapter.datalist.remove(i);
                                   // break;
                                }
                            }
                        }
                        //adapter.notifyDataSetChanged();

                        editcount = 0;
                        edit_icon.setImageResource(R.drawable.edit_icon_n);
                        delete_icon.setVisibility(View.INVISIBLE);
                        filter_list = dataupdatemethodfordelete(HashMapRecycleviewadapter.datalist,"No");
                        if(filter_list.size() ==0){
                            loadpostsdata(pageCount);
                        }else{
                            adapter = new HashMapRecycleviewadapter( getActivity(), filter_list, "notifications", notifications_recyclerview, R.layout.notifications_listitems );
                            notifications_recyclerview.setAdapter( adapter );
                        }


                    }

                } else {
                    String error = jsonObject.getString("error");
                    StoredObjects.ToastMethod("Try Again.", getActivity());
                    pageCount = 1;
                    loadpostsdata(pageCount);

                   /* for (int j = 0; j < seleted_postions.size(); j++) {
                        StoredObjects.LogMethod( "response", "Selected_ids<error><>:---" + seleted_postions.get(j) );
                    }
                    if(seleted_postions.size()>0) {
                        for (int i = 0; i < HashMapRecycleviewadapter.datalist.size(); i++) {

                            for (int j = 0; j < seleted_postions.size(); j++) {
                                if (HashMapRecycleviewadapter.datalist.get(i).get("notification_id").equalsIgnoreCase(seleted_postions.get(j))) {
                                    StoredObjects.LogMethod("response", "Selected_ids<><innervals<>error>:---" + i + "<><>" + seleted_postions.get(j));
                                    HashMapRecycleviewadapter.datalist.remove(i);
                                    break;
                                }
                            }
                        }
                        //adapter.notifyDataSetChanged();

                        editcount = 0;
                        edit_icon.setImageResource(R.drawable.edit_icon_n);
                        delete_icon.setVisibility(View.INVISIBLE);
                        filter_list = dataupdatemethodfordelete(HashMapRecycleviewadapter.datalist, "No");
                        if (filter_list.size() == 0) {
                            loadpostsdata(pageCount);
                        } else {
                            adapter = new HashMapRecycleviewadapter(getActivity(), filter_list, "notifications", notifications_recyclerview, R.layout.notifications_listitems);
                            notifications_recyclerview.setAdapter(adapter);
                        }


                    }*/

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
                StoredObjects.LogMethod("response", "response:---" + e);
            }
        }
    }


}
