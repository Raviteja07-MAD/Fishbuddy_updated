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

import com.fishbuddy.R;
import com.fishbuddy.customfonts.CustomButton;
import com.fishbuddy.customfonts.CustomEditText;
import com.fishbuddy.servicesparsing.CustomProgressbar;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
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
import java.util.List;

public class Changepassword extends Fragment {

    TextView title_txt;
    ImageView backbtn_img;
    CustomEditText oldpasswrdedtxt, newpasswrdedtxt, confrmpasswrdedtxt;
    CustomButton submit_btn;

    String oldpasswrd = "";
    String newpasswrd = "";
    String confrmpassword = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate ( R.layout.change_password , null , false);
        StoredObjects.page_type="home";
        StoredObjects.back_type="change_password";
        SideMenu.updatemenu(StoredObjects.page_type);

        init (v);
        return v;
    }

    private void init(View v) {

        title_txt = (TextView)v.findViewById( R.id. title_txt);
        backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        oldpasswrdedtxt = v.findViewById (R.id.oldpasswrdedtxt);
        newpasswrdedtxt = v.findViewById (R.id.newpasswrdedtxt);
        confrmpasswrdedtxt = v.findViewById (R.id.confrmpasswrdedtxt);
        submit_btn = v.findViewById (R.id.submit_btn);
        title_txt.setText( R.string.change_password );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay( new Landing_page() );
            }
        } );

        submit_btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                oldpasswrd = oldpasswrdedtxt.getText ().toString ();
                newpasswrd = newpasswrdedtxt.getText ().toString ();
                confrmpassword = confrmpasswrdedtxt.getText ().toString ();

                if (StoredObjects.inputValidation (oldpasswrdedtxt , getActivity ().getResources ().getString (R.string.oldpassword) , getActivity ())) {
                    if (StoredObjects.inputValidation( newpasswrdedtxt, getActivity().getResources().getString( R.string.newpassword ), getActivity() )) {
                        if (StoredObjects.inputValidation( confrmpasswrdedtxt, getActivity().getResources().getString( R.string.cnfrmpassword_again ), getActivity() )) {

                            StoredObjects.hide_keyboard( getActivity() );
                            if (InterNetChecker.isNetworkAvailable( getActivity() )) {
                                if (newpasswrd.equals( confrmpassword )) {
                                    new ChangepasswordTask().execute( StoredObjects.UserId, oldpasswrd, newpasswrd, confrmpassword );
                                } else {
                                    StoredObjects.ToastMethod( getActivity().getResources().getString( R.string.errorpasswrd ), getActivity() );
                                }
                            } else {
                                StoredObjects.ToastMethod( getActivity().getResources().getString( R.string.checkinternet ), getActivity() );
                            }
                        }
                    }
                }
            }

        });
    }

    public class ChangepasswordTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(getActivity ());
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token","Mikel"));
                nameValuePairs.add(new BasicNameValuePair("method","change-password"));
                nameValuePairs.add(new BasicNameValuePair("customer_id",params[0]));
                nameValuePairs.add(new BasicNameValuePair("old_password",params[1]));
                nameValuePairs.add(new BasicNameValuePair("new_password",params[2]));

                strResult = HttpPostClass.PostMethod( StoredUrls.BaseUrl,nameValuePairs);

                /*http://demos.customerwebdemo.com/FishBuddy/app/index.php?method=change-password&old_password=1234&new_password=123&customer_id=1*/

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
                CustomProgressbar.Progressbarcancel(getActivity ());
            }
            StoredObjects.LogMethod("Details", "e:----"+result);
            try {

                JSONObject jsonObject =  new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equalsIgnoreCase("200")){

                    oldpasswrdedtxt.setText ("");
                    StoredObjects.ToastMethod("Succesfully password updated.",getActivity ());
                    fragmentcallinglay( new Landing_page() );

                }else{
                    String error= jsonObject.getString("message");
                    StoredObjects.ToastMethod(error,getActivity ());
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

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }

}
