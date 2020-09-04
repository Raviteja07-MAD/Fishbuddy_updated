package com.fishbuddy.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fishbuddy.R;
import com.fishbuddy.customfonts.CustomButton;
import com.fishbuddy.customfonts.CustomEditText;
import com.fishbuddy.servicesparsing.CustomProgressbar;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
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

public class Forgot_password extends AppCompatActivity {
    ImageView backbutton_img;
    CustomButton frgtpasswrd_submt_btn;
    CustomEditText frgtpasswrd_email_edtxt;
    String frgtpswrd_email_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.forgot_password );
        initialization();
    }

    private void initialization() {
        ImageView backbtn_img = (ImageView)findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );

       TextView title_txt = (TextView)findViewById( R.id. title_txt);
        title_txt.setText( "Forgot Password" );
        frgtpasswrd_submt_btn = (CustomButton)findViewById( R.id.frgtpasswrd_submt_btn );
        frgtpasswrd_email_edtxt = (CustomEditText)findViewById( R.id.frgtpasswrd_email_edtxt );

        frgtpasswrd_submt_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frgtpswrd_email_id = frgtpasswrd_email_edtxt.getText().toString();
                if (StoredObjects.inputValidation( frgtpasswrd_email_edtxt, getApplicationContext().getResources().getString( R.string.enteremail ), Forgot_password.this )) {
                        StoredObjects.hide_keyboard( Forgot_password.this );
                        if (InterNetChecker.isNetworkAvailable( Forgot_password.this )) {
                            new ForgotPasswordTask().execute( frgtpswrd_email_id );
                        } else {
                            StoredObjects.ToastMethod( getApplicationContext().getResources().getString( R.string.checkinternet ), Forgot_password.this );
                        }
                    }
                }

        } );

    }
    public class ForgotPasswordTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(Forgot_password.this);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("token","Mikel"));
                nameValuePairs.add(new BasicNameValuePair("email",params[0]));
                nameValuePairs.add(new BasicNameValuePair("method","forgot-password"));
                strResult = HttpPostClass.PostMethod( StoredUrls.BaseUrl,nameValuePairs);
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
                CustomProgressbar.Progressbarcancel(Forgot_password.this);
            }
            try {

                JSONObject jsonObject =  new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equalsIgnoreCase("200")){
                    StoredObjects.ToastMethod(" Set password Link Successfully Sent to your email id.",Forgot_password.this);
                     startActivity(new Intent(Forgot_password.this, Sign_in_Sign_up.class));
                }else{
                    String error= jsonObject.getString("error");
                    StoredObjects.ToastMethod(error,Forgot_password.this);
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

}




