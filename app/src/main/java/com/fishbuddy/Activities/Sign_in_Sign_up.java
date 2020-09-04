package com.fishbuddy.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.fishbuddy.R;
import com.fishbuddy.customfonts.CustomButton;
import com.fishbuddy.customfonts.CustomEditText;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.database.Database;
import com.fishbuddy.servicesparsing.CustomProgressbar;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import org.apache.commons.lang.StringEscapeUtils;
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

import static com.fishbuddy.servicesparsing.HttpPostClass.GET;
import static com.fishbuddy.sidemenu.SideMenu.btm_addpost_img;
import static com.fishbuddy.sidemenu.SideMenu.btm_addpost_txt;
import static com.fishbuddy.sidemenu.SideMenu.btmaddpost_lay;

public class Sign_in_Sign_up extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    CustomRegularTextView sign_in_text,sign_up_text,frgt_password_text;
    LinearLayout sign_in_layout,signup_layout,google_signin_btn,login_fbbtn,fb_signin_btn;
    CustomButton sign_in_btn,sign_up_btn;
    String name="",phone_number="",email_id="",password="",confrm_password="",signin_email_id="",signin_password="";
    CustomEditText sign_up_name_edtxt,sign_up_phone_edtxt,sign_up_email_edtxt,sign_up_password_edtxt,sign_up_cnfrmpsswrd_edtxt,sign_in_email_edtxt,sign_in_passwrd_edtxt;

    Database database;

    //facebook
    private static String APP_ID = "2449506161993850"; //Replace with your App ID/*2449506161993850*///1969725583287411
    private Facebook facebook = new Facebook(APP_ID);
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;

    //google
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private static final String TAG = Sign_in_Sign_up.class.getSimpleName();
    private ProgressDialog mProgressDialog;



    //Google
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        //ATTENTION: This was auto-generated to implement the App Indexing API.
        //See https://g.co/AppIndexing/AndroidStudio for more information.

        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        mGoogleApiClient.disconnect();

    }


    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
        //See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
       /* if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/

        //ATTENTION: This was auto-generated to implement the App Indexing API.
        //See https://g.co/AppIndexing/AndroidStudio for more information.

        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            //Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();



            String personName = "-";
           /* try{
                personName = acct.getDisplayName();
                Log.e(TAG, "display name: " + acct.getDisplayName());
            }catch (NullPointerException e){
                personName ="-";
            }*/

            String email = acct.getEmail();
            personName= acct.getEmail();

            Log.e(TAG,  " email: " + email
                    + ", Image: " /*+ personPhotoUrl*/);

            if (personName.equalsIgnoreCase(null)||personName.equalsIgnoreCase("")||personName.equalsIgnoreCase("null")){
                personName = "-";
            }



            if (InterNetChecker.isNetworkAvailable(getApplicationContext())) {
                String reg_id = StoredObjects.getsaveddata(getApplicationContext(),"reg_id");
                new GoogleLoginTask().execute(personName,email,"",StoredObjects.gcm_regid);
            } else {
                StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.checkinternet), Sign_in_Sign_up.this);
            }

            //txtName.setText(personName);
            //txtEmail.setText(email);
            /*Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);*/

            //   updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);

        }

    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {

    }




    //Facebook
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        facebook.authorizeCallback(requestCode, resultCode, data);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.sign_in );
        database = new Database(Sign_in_Sign_up.this);
        database.getAllDevice();
        //Facebook
        mAsyncRunner = new AsyncFacebookRunner(facebook);

        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).addApi(AppIndex.API).build();

      /*  GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestScopes(new Scope(Scopes.PROFILE))
                //.requestScopes(new Scope(Scopes.PLUS_LOGIN))
                //.requestProfile()
                .requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).addApi(AppIndex.API).build();*/





        initialization();
    }
    private void initialization() {

        sign_in_text = (CustomRegularTextView)findViewById( R.id.sign_in_text );
        sign_up_text = (CustomRegularTextView)findViewById( R.id.sign_up_text );
        frgt_password_text = (CustomRegularTextView)findViewById( R.id.frgt_password_text );
        sign_in_layout = (LinearLayout)findViewById( R.id.sign_in_layout );
        signup_layout = (LinearLayout)findViewById( R.id.signup_layout );
        google_signin_btn = (LinearLayout)findViewById( R.id.google_signin_btn );
        sign_in_btn = (CustomButton)findViewById( R.id.sign_in_btn );
        login_fbbtn = (LinearLayout)findViewById( R.id.login_fbbtn );
        fb_signin_btn = (LinearLayout)findViewById( R.id.fb_signin_btn );
        sign_up_btn = (CustomButton)findViewById( R.id.sign_up_btn );
        sign_up_name_edtxt =(CustomEditText)findViewById( R.id.sign_up_name_edtxt);
        sign_up_phone_edtxt =(CustomEditText)findViewById( R.id.sign_up_phone_edtxt);
        sign_up_email_edtxt =(CustomEditText)findViewById( R.id.sign_up_email_edtxt);
        sign_up_password_edtxt =(CustomEditText)findViewById( R.id.sign_up_password_edtxt);
        sign_up_cnfrmpsswrd_edtxt =(CustomEditText)findViewById( R.id.sign_up_cnfrmpsswrd_edtxt);
        sign_in_email_edtxt =(CustomEditText)findViewById( R.id.sign_in_email_edtxt);
        sign_in_passwrd_edtxt =(CustomEditText)findViewById( R.id.sign_in_passwrd_edtxt);

        //sign_in_email_edtxt.setText("android@gmail.com");
        //sign_in_passwrd_edtxt.setText("0000");

        login_fbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginToFacebook();
            }
        });

        fb_signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToFacebook();
            }
        });
        sign_in_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signin_email_id = sign_in_email_edtxt.getText().toString();
                signin_password = sign_in_passwrd_edtxt.getText().toString();

                if (StoredObjects.inputValidation( sign_in_email_edtxt, getApplicationContext().getResources().getString( R.string.enteremail ), Sign_in_Sign_up.this )) {
                    if (StoredObjects.inputValidation( sign_in_passwrd_edtxt, getApplicationContext().getResources().getString( R.string.enterpassword ), Sign_in_Sign_up.this )) {

                        StoredObjects.hide_keyboard( Sign_in_Sign_up.this );
                        if (InterNetChecker.isNetworkAvailable( Sign_in_Sign_up.this )) {
                            new Signin_Task().execute( signin_email_id, signin_password,StoredObjects.gcm_regid );

                        } else {
                            StoredObjects.ToastMethod( getApplicationContext().getResources().getString( R.string.checkinternet ), Sign_in_Sign_up.this );
                        }
                    }
                }
            }
        } );

        google_signin_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        } );
        sign_in_text.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonchangemethod (Sign_in_Sign_up.this ,sign_in_layout, sign_in_text  ,"0");
            }
        } );

        sign_up_text.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonchangemethod (Sign_in_Sign_up.this ,signup_layout, sign_up_text  ,"1");
            }
        } );

        frgt_password_text.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Sign_in_Sign_up.this, Forgot_password.class));
            }
        } );
        sign_up_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                name = sign_up_name_edtxt.getText().toString();
                phone_number = sign_up_phone_edtxt.getText().toString();
                email_id = sign_up_email_edtxt.getText().toString();
                password = sign_up_password_edtxt.getText().toString();
                confrm_password = sign_up_cnfrmpsswrd_edtxt.getText().toString();

                if (StoredObjects.inputValidation( sign_up_name_edtxt, getApplicationContext().getResources().getString( R.string.entername ), Sign_in_Sign_up.this )) {
                    if (StoredObjects.inputValidation( sign_up_phone_edtxt, getApplicationContext().getResources().getString( R.string.entermobile ), Sign_in_Sign_up.this )) {
                        if (StoredObjects.inputValidation( sign_up_email_edtxt, getApplicationContext().getResources().getString( R.string.enteremail ), Sign_in_Sign_up.this )) {
                            if (StoredObjects.inputValidation( sign_up_password_edtxt, getApplicationContext().getResources().getString( R.string.enterpassword ), Sign_in_Sign_up.this )) {
                                if (StoredObjects.inputValidation( sign_up_cnfrmpsswrd_edtxt, getApplicationContext().getResources().getString( R.string.cnfrmpassword ), Sign_in_Sign_up.this )) {
                                    if (confrm_password.equals( password ) ) {
                                        StoredObjects.hide_keyboard( Sign_in_Sign_up.this );
                                        if (InterNetChecker.isNetworkAvailable( Sign_in_Sign_up.this )) {
                                            if(StoredObjects.isValidEmail(email_id)){

                                                new Signup_Task().execute( name, phone_number, email_id, password ,confrm_password );

                                            }else{
                                                StoredObjects.ToastMethod( getApplicationContext().getResources().getString( R.string.please_entervalidemail), Sign_in_Sign_up.this );
                                            }
                                        } else {
                                            StoredObjects.ToastMethod( getApplicationContext().getResources().getString( R.string.checkinternet ), Sign_in_Sign_up.this );
                                        }
                                    } else {
                                        StoredObjects.ToastMethod( "passwords not matched", Sign_in_Sign_up.this );
                                    }

                                }
                            }
                        }
                    }
                }
            }
        } );

    }

    public void buttonchangemethod(Activity activity ,LinearLayout layout1,  TextView text1 , String type) {

        signup_layout.setVisibility( View.GONE );
        sign_in_layout.setVisibility( View.GONE );

        sign_in_text.setBackgroundResource( R.drawable.signin_white_btn_bg );
        sign_up_text.setBackgroundResource( R.drawable.signin_white_btn_bg );

        sign_up_text.setTextColor( getResources().getColor(R.color.form_text_color));
        sign_in_text.setTextColor( getResources().getColor(R.color.form_text_color));


        layout1.setVisibility( View.VISIBLE );
        text1.setTextColor (activity.getResources ().getColor (R.color.white));
        text1.setBackgroundResource( R.drawable.signin_btn_bg );
    }

    public class Signup_Task extends AsyncTask<String, String, String> {
        String strResult = "";
        String enter_email = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(Sign_in_Sign_up.this);        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("method","register"));
                nameValuePairs.add(new BasicNameValuePair("name",params[0]));
                nameValuePairs.add(new BasicNameValuePair("mobile_type","Android"));
                nameValuePairs.add(new BasicNameValuePair("phone",params[1]));
                nameValuePairs.add(new BasicNameValuePair("email",params[2]));
                nameValuePairs.add(new BasicNameValuePair("password",params[3]));

                enter_email = params[2];
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
                CustomProgressbar.Progressbarcancel(Sign_in_Sign_up.this);            }
            try {

                JSONObject jsonObject =  new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equalsIgnoreCase("200")){
                    enter_email = "";
                    buttonchangemethod (Sign_in_Sign_up.this ,sign_in_layout, sign_in_text  ,"0");

                    sign_up_name_edtxt.setText("");
                    sign_up_phone_edtxt.setText("");
                    sign_up_email_edtxt.setText("");
                    sign_up_password_edtxt.setText("");
                    sign_up_cnfrmpsswrd_edtxt.setText("");

                    StoredObjects.ToastMethod("Successfully Registered.",Sign_in_Sign_up.this);


                    String customer_id = jsonObject.getString("customer_id");
                }else{
                    String error= jsonObject.getString("error");
                   // StoredObjects.ToastMethod("Sign Up failed..",Sign_in_Sign_up.this);
                    StoredObjects.ToastMethod(error,Sign_in_Sign_up.this);
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

    public class Signin_Task extends AsyncTask<String, String, String> {
        String strResult = "";
        String enterd_email = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(Sign_in_Sign_up.this);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("method","login"));
                nameValuePairs.add(new BasicNameValuePair("email",params[0]));
                nameValuePairs.add(new BasicNameValuePair("password",params[1]));
                nameValuePairs.add(new BasicNameValuePair("gcm_regid",params[2]));
                nameValuePairs.add(new BasicNameValuePair("phone_type","Android"));

                enterd_email = params[0];
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
                 CustomProgressbar.Progressbarcancel(Sign_in_Sign_up.this);
            }
            try {

                JSONObject jsonObject =  new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equalsIgnoreCase("200")){
                    StoredObjects.UserId = jsonObject.getString("customer_id");
                    database.UpdateUserId(StoredObjects.UserId);
                    logUser(email_id,StoredObjects.UserId);
                    Intent intent = new Intent( Sign_in_Sign_up.this, SideMenu.class );
                    intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(intent);
                    Sign_in_Sign_up.this.finish();
                }else{
                    String error= jsonObject.getString("error");
                    StoredObjects.ToastMethod(error,Sign_in_Sign_up.this);
                    //StoredObjects.ToastMethod("Invalid Email/Password",Sign_in_Sign_up.this);
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


    private void logUser(String email,String id) {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier(id);
        Crashlytics.setUserEmail(email);
        Crashlytics.setUserName("Test User");
    }


    //Facebook Login
    public void loginToFacebook() {

        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);

        Log.i("accesstoken", "accesstoken:--"+access_token);
        long expires = mPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            facebook.setAccessToken(access_token);

            try {
                Log.i("accesstoken", "login:--access_token");

                getProfileInformation();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
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

            Log.d("FB Sessions", "" + facebook.isSessionValid());
        }

        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (!facebook.isSessionValid()) { //public_profile
            facebook.authorize(this,
                    //new String[] { "email", "publish_stream" },
                    new String[] { "email", "public_profile" },
                    new Facebook.DialogListener() {

                        @Override
                        public void onCancel() {
                            // Function to handle cancel event
                        }

                        @Override
                        public void onComplete(Bundle values) {
                            // Function to handle complete event
                            // Edit Preferences and update facebook acess_token
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();

                            try {
                                Log.i("accesstoken", "login:--isSessionValid");

                                getProfileInformation();

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            Log.i("accesstoken", "login:--");
                        }

                        @Override
                        public void onError(DialogError error) {
                            // Function to handle error

                        }

                        @Override
                        public void onFacebookError(FacebookError fberror) {
                            // Function to handle Facebook errors

                        }

                    });
        }

    }

    public void getProfileInformation() throws JSONException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
        StrictMode.setThreadPolicy(policy);
        String access_Code = facebook.getAccessToken();
        String url = "https://graph.facebook.com/me?&fields=id,name,email&access_token=" + access_Code;

        String output = GET(url);
        Log.i("Data","Data>>>" + output );

        JSONObject profile = new JSONObject(output);

        // getting name of the user

        final String Id = profile.getString("id");
        final String name = profile.getString("name");

        // getting email of the user
        String email = profile.getString("email");

        Log.i("Id","Id:::::" + Id);

        email = StringEscapeUtils.unescapeJava(email);
        String  image_url="";
        image_url = "http://graph.facebook.com/"+Id+"/picture?width=200&height=200";
        StoredObjects.savedata(getApplicationContext(),image_url,"f_image_url");
        Log.i("Email","Email<><>---" + email);

        if (InterNetChecker.isNetworkAvailable(getApplicationContext())) {
            String reg_id = StoredObjects.getsaveddata(getApplicationContext(),"reg_id");
            new FacebookloginTask().execute(name,email,access_Code,Id,StoredObjects.gcm_regid/*reg_id*/);
        } else {
            StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.checkinternet), Sign_in_Sign_up.this);
        }
    }

    public class FacebookloginTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(Sign_in_Sign_up.this);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("name",params[0]));
                nameValuePairs.add(new BasicNameValuePair("email",params[1]));
                nameValuePairs.add(new BasicNameValuePair("facebook_oauth_token",params[2]));
                nameValuePairs.add(new BasicNameValuePair("oauth_uid", params[3]));
                nameValuePairs.add(new BasicNameValuePair("gcm_regid", params[4]));
                nameValuePairs.add(new BasicNameValuePair("method", "login-with-facebook"));
                nameValuePairs.add(new BasicNameValuePair("phone_type","Android"));

                String pairsstrig = "";
                for (int i = 0; i <nameValuePairs.size() ; i++) {
                    pairsstrig += nameValuePairs.get(i)+"&";
                }
                Log.i("HttpTAG:1", "namevaluepairs:--"+StoredUrls.BaseUrl+pairsstrig);

                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl,nameValuePairs);
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
                CustomProgressbar.Progressbarcancel(Sign_in_Sign_up.this);
            }

            try {
                JSONObject jsonObject =  new JSONObject(result == null ? "" :result);
                String status = jsonObject.getString("status");
                if(status.equalsIgnoreCase("200")){

                    //StoredObjects.LogMethod("Status", "Status_of_FB:----"+status);

                    //{"status":200,"message":"success","old_user":"No","customer_id":16}

                    StoredObjects.UserId = jsonObject.getString("customer_id");
                    database.UpdateUserId(StoredObjects.UserId);
                    startActivity(new Intent(Sign_in_Sign_up.this, SideMenu.class));

                }else{
                    StoredObjects.ToastMethod( "Try again",Sign_in_Sign_up.this );
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
                StoredObjects.LogMethod("login", "login:---"+e);
            }

        }

    }


    public class GoogleLoginTask extends AsyncTask<String, String, String> {
        String strResult = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow(Sign_in_Sign_up.this);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("name",params[0]));
                nameValuePairs.add(new BasicNameValuePair("email",params[1]));
                nameValuePairs.add(new BasicNameValuePair("phone",params[2]));
                nameValuePairs.add(new BasicNameValuePair("gcm_regid", params[3]));
                nameValuePairs.add(new BasicNameValuePair("method", "login-with-google"));
                nameValuePairs.add(new BasicNameValuePair("phone_type","Android"));
                StoredObjects.LogMethod( "response", "response:---" + nameValuePairs);

                strResult = HttpPostClass.PostMethod(StoredUrls.BaseUrl,nameValuePairs);
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
                CustomProgressbar.Progressbarcancel(Sign_in_Sign_up.this);
            }

            try {
                JSONObject jsonObject =  new JSONObject(result == null ? "" :result);
                String status = jsonObject.getString("status");
                if(status.equalsIgnoreCase("200")){

                    //StoredObjects.LogMethod("Status", "Status_of_FB:----"+status);

                    //{"status":200,"message":"success","old_user":"No","customer_id":16}

                    StoredObjects.UserId = jsonObject.getString("customer_id");
                    database.UpdateUserId(StoredObjects.UserId);
                    startActivity(new Intent(Sign_in_Sign_up.this, SideMenu.class));

                }else{
                    StoredObjects.ToastMethod( "Try again",Sign_in_Sign_up.this );
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
                StoredObjects.LogMethod("login", "login:---"+e);
            }

        }

    }

}
