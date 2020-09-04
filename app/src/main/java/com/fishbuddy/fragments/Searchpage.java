package com.fishbuddy.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fishbuddy.R;
import com.fishbuddy.customadapter.CustomRecyclerview;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
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

public class Searchpage extends Fragment {
    TextView prdct_nodata_txt;
    CustomRecyclerview customRecyclerview;
    LinearLayout backbtn_lay;
    EditText prfle_srch_edtx;
    ImageView cancel_img;
    RecyclerView profile_search_recycle;
    CardView cardview;

    public ArrayList<HashMap<String, String>> userslist = new ArrayList<>();
    public ArrayList<HashMap<String, String>> userslist_all = new ArrayList<>();
    public static ArrayList<HashMap<String,String>> searchlistarray = new ArrayList<>();



    int pageCount = 1;
    int totalpages;
    String total_results ,total_pages= "0";
    public static HashMapRecycleviewadapter adapter;


    String search_enterd_text = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.searchpage,null,false );
        StoredObjects.page_type="searchpage";
        StoredObjects.back_type="searchpage";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);

        return v;
    }


    public void getuserslistdata(int page_count){
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            search_enterd_text = prfle_srch_edtx.getText().toString().trim();
            new GetUsersListTask().execute(search_enterd_text,page_count+"");
        }else{
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
        }
    }




    private void initilization(View v) {

        profile_search_recycle = (RecyclerView) v.findViewById( R.id.profile_search_recycle );
        customRecyclerview = new CustomRecyclerview(getActivity());
        backbtn_lay = v.findViewById( R.id.backbtn_lay );
        prfle_srch_edtx = v.findViewById( R.id.prfle_srch_edtx );
        cancel_img = v.findViewById( R.id.cancel_img );
        prdct_nodata_txt = v.findViewById( R.id.prdct_nodata_txt );
        cardview = v.findViewById( R.id.cardview );

        prfle_srch_edtx.setFocusable(true);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(prfle_srch_edtx, InputMethodManager.SHOW_IMPLICIT);


        cancel_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prfle_srch_edtx.setText( "" );
            }
        } );
        backbtn_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();

            }
        } );


        prfle_srch_edtx.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if(prfle_srch_edtx.getText().toString().trim().length() ==0){
                        StoredObjects.ToastMethod("Please Enter Name to Search",getActivity());
                    }else{
                        StoredObjects.hide_keyboard(getActivity());
                        getuserslistdata(pageCount);
                    }

                    return true;
                }
                return false;
            }
        });




        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        profile_search_recycle.setLayoutManager(linearLayoutManager);
        profile_search_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastvisibleitemposition == adapter.getItemCount() - 1) {
                    if(pageCount<=Integer.parseInt(total_pages)){
                        pageCount=pageCount+1;
                        getuserslistdata(pageCount);
                    }


                }
            }
        });




        prfle_srch_edtx.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = prfle_srch_edtx.getText().length();
                if (textlength>0){
                    cancel_img.setVisibility( View.VISIBLE );
                }
                else {
                    cancel_img.setVisibility( View.GONE );
                }

                searchlistarray.clear();
                for (int i = 0; i < userslist_all.size(); i++) {
                    if ((textlength <= userslist_all.get(i).get("name").length())||(textlength <= userslist_all.get(i).get("email").length())
                            ||(textlength <= userslist_all.get(i).get("phone").length())) {
                        if ((userslist_all.get(i).get("name").toLowerCase().trim().contains(  prfle_srch_edtx.getText().toString().toLowerCase().trim()))
                                ||(userslist_all.get(i).get("email").toLowerCase().trim().contains(prfle_srch_edtx.getText().toString().toLowerCase().trim()))
                                ||(userslist_all.get(i).get("phone").toLowerCase().trim().contains(prfle_srch_edtx.getText().toString().toLowerCase().trim()))) {
                            searchlistarray.add(userslist_all.get(i));
                        }
                    }
                }
                StoredObjects.LogMethod( "<><><>","<>><><"+searchlistarray.size()+searchlistarray );

                if (textlength==0){

                    prdct_nodata_txt.setVisibility(View.VISIBLE);
                    profile_search_recycle.setVisibility(View.GONE);
                    cardview.setVisibility(View.GONE);

                }else{

                    if(searchlistarray.size()>0){
                        profile_search_recycle.setVisibility(View.VISIBLE);
                        cardview.setVisibility(View.VISIBLE);
                        prdct_nodata_txt.setVisibility(View.GONE);

                       // customRecyclerview.Assigndatatorecyleviewhashmap( profile_search_recycle, searchlistarray, "Searchpage", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.search_listitems );
                       // profile_search_recycle.setVisibility(View.VISIBLE);
                       // adapter.notifyDataSetChanged();

                        adapter = new HashMapRecycleviewadapter(getActivity(),searchlistarray,"Searchpage",profile_search_recycle,R.layout.search_listitems );//
                        profile_search_recycle.setAdapter(adapter);


                    }else{
                        prdct_nodata_txt.setVisibility(View.VISIBLE);
                        profile_search_recycle.setVisibility(View.GONE);
                        cardview.setVisibility(View.GONE);
                    }
                }


            }
        });


    }

    public class GetUsersListTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow( getActivity());
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token", "Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method", StoredUrls.users_list ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id",StoredObjects.UserId ) );
                nameValuePairs.add( new BasicNameValuePair( "search_text",params[0] ) );
                nameValuePairs.add( new BasicNameValuePair( "page",params[1] ) );
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
                    total_pages = jsonObject.getString( "total_pages" );

                    //userslist = JsonParsing.GetJsonData( results );
                   /* if (userslist.size()>0){
                        customRecyclerview.Assigndatatorecyleviewhashmap( profile_search_recycle, userslist, "Searchpage", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.search_listitems );
                        profile_search_recycle.setVisibility(View.VISIBLE);
                        cardview.setVisibility(View.VISIBLE);
                        prdct_nodata_txt.setVisibility(View.GONE);
                    }
                } else {
                    profile_search_recycle.setVisibility( View.GONE );
                    prdct_nodata_txt.setVisibility( View.VISIBLE );
                    cardview.setVisibility( View.GONE );*/



                    if(pageCount==1){
                        userslist_all.clear();
                        userslist = JsonParsing.GetJsonData(results);
                        userslist_all.addAll(userslist);


                        profile_search_recycle.setVisibility(View.VISIBLE);
                        cardview.setVisibility(View.VISIBLE);
                        prdct_nodata_txt.setVisibility(View.GONE);

                        adapter = new HashMapRecycleviewadapter(getActivity(),userslist_all,"Searchpage",profile_search_recycle,R.layout.search_listitems );//
                        profile_search_recycle.setAdapter(adapter);



                    }else{

                        userslist = JsonParsing.GetJsonData(results);
                        userslist_all.addAll(userslist);
                        StoredObjects.LogMethod("val","val:::"+"yes"+userslist_all.size());
                        adapter.notifyDataSetChanged();


                    }

                }
                else if (status.equalsIgnoreCase( "500" )) {
                    if(userslist_all.size() ==0){
                        prdct_nodata_txt.setVisibility(View.VISIBLE);
                        profile_search_recycle.setVisibility(View.GONE);
                        cardview.setVisibility(View.GONE);
                    }
                }

                else{
                    if(userslist_all.size() ==0){
                        prdct_nodata_txt.setVisibility(View.VISIBLE);
                        profile_search_recycle.setVisibility(View.GONE);
                        cardview.setVisibility(View.GONE);
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

}
