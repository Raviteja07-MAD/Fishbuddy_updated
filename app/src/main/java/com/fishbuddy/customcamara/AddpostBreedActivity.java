package com.fishbuddy.customcamara;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.toolbox.ImageLoader;
import com.fishbuddy.Activities.SearchFishingSpot;
import com.fishbuddy.Gps.GPSTracker;
import com.fishbuddy.R;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
import com.fishbuddy.customfonts.AppController;
import com.fishbuddy.customfonts.CustomButton;
import com.fishbuddy.customfonts.CustomEditText;
import com.fishbuddy.customfonts.CustomRegularTextView;
import com.fishbuddy.customfonts.FeedImageView;
import com.fishbuddy.customfonts.ImageCommpression;
import com.fishbuddy.dumpdata.DumpData;
import com.fishbuddy.fragments.AddPostFromBreedCheck;
import com.fishbuddy.fragments.Landing_page;
import com.fishbuddy.servicesparsing.CustomProgressbar;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.servicesparsing.MultipartUtility;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.CameraUtils;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.fishbuddy.sidemenu.SideMenu.btm_addpost_img;
import static com.fishbuddy.sidemenu.SideMenu.btm_addpost_txt;
import static com.fishbuddy.sidemenu.SideMenu.btmaddpost_lay;
import static com.fishbuddy.storedobjects.CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE;

public class AddpostBreedActivity extends Activity {
    TextView title_txt;
    ImageView backbtn_img;
    CustomEditText addpost_photo_video_edtxt,addpost_url_edtxt,addpost_comment_edtxt;
    CustomButton post_btn;
    LinearLayout uploaded_images_layout,selective_fishignspots_layout;
    String PathHolder;
    Intent intent ;

    //capture camera
    // Activity request codes
    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final int MEDIA_TYPE_IMAGE = 1;
    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;
    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "ProfileImages";
    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    private static String imageStoragePath;

    protected static final int SELECT_FILE = 1;
    private static final int PIC_CROP = 3;
    String image_str="";
    private Bitmap myImg;
    private String fileName;
    String file_name;
    String browsephoto_video="",addurl="",writecomment="";

    ArrayList<DumpData> uploaded_images_array = new ArrayList<>();

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();



    Bundle bundle;
    int position;
    ArrayList<HashMap<String, String>> postslist;
    ArrayList<HashMap<String, String>> images_array = new ArrayList<>();
    ArrayList<HashMap<String, String>> selected_fishspots_array = new ArrayList<>();

   /* String postid = "";
    String posttype = "";*/

    GPSTracker gpsTracker;

    double lattitude = 0.00;
    double longitude = 0.00;
    String address,fishingspots;

    CustomEditText tag_fishignspots;

    @Override
    protected void onResume() {
        super.onResume();

        if(StoredObjects.Selected_fishspot_id.equalsIgnoreCase("")){

        }else{
            HashMap<String, String> dash_hash = new HashMap<String, String>();
            dash_hash.put("name",StoredObjects.Selected_fishspotname);
            dash_hash.put("id",StoredObjects.Selected_fishspot_id);
            selected_fishspots_array.add(dash_hash);

           /* StoredObjects.Selected_fishspotname = "";
            StoredObjects.Selected_fishspot_id = "";*/

            /*try {
                // addpost_comment_edtxt.setText("Caught Breed: "+postslist.get(position).get("species")+" in location :"+address);
                addpost_comment_edtxt.setText("Caught Breed: "+postslist.get(position).get("species")+" in location :"+StoredObjects.Selected_fishspot_address);
            }catch (Exception e){
            }*/

            selective_fishignspots_layout.removeAllViews();
            for (int i = 0; i < selected_fishspots_array.size(); i++) {
                addlayoutfishingselected(selective_fishignspots_layout,selected_fishspots_array,i);
            }


        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        StoredObjects.page_type = "fromscanpost";
        gpsTracker = new GPSTracker(AddpostBreedActivity.this,AddpostBreedActivity.this);
        Intent intent = getIntent();
        if(intent!=null){
            Bundle args = intent.getBundleExtra("BUNDLE");
            postslist = (ArrayList<HashMap<String, String>>) args.getSerializable("ARRAYLIST");

            position = intent.getIntExtra("position", 0);
        }
        initilization();


        if(intent!=null){
            // datasign();

            lattitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

             address = StoredObjects.getCompleteAddressString(AddpostBreedActivity.this,gpsTracker.getLatitude(),gpsTracker.getLongitude());


             try {
                // addpost_comment_edtxt.setText("Caught Breed: "+postslist.get(position).get("species")+" in location :"+address);
                 addpost_comment_edtxt.setText("Caught Breed: "+postslist.get(position).get("species"));
             }catch (Exception e){
             }

            images_array.clear();
            if (InterNetChecker.isNetworkAvailable(AddpostBreedActivity.this)) {
                new ImageUploadTaskNew().execute(ImageCommpression.compressImage( StoredObjects.scanned_imagepath)  );
            }else{
                Toast.makeText(AddpostBreedActivity.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        };
    }




    private void initilization() {

        title_txt = (TextView)findViewById( R.id. title_txt);
        backbtn_img = (ImageView)findViewById( R.id.backbtn_img );
        addpost_photo_video_edtxt = (CustomEditText)findViewById( R.id.addpost_photo_video_edtxt );
        addpost_url_edtxt = (CustomEditText)findViewById( R.id.addpost_url_edtxt );
        addpost_comment_edtxt = (CustomEditText)findViewById( R.id.addpost_comment_edtxt );
        post_btn = (CustomButton)findViewById( R.id.post_btn );
        uploaded_images_layout = (LinearLayout)findViewById( R.id.uploaded_images_layout );

        selective_fishignspots_layout = (LinearLayout)findViewById( R.id.selective_fishignspots_layout );

        tag_fishignspots = findViewById( R.id.tag_fishignspots );

        title_txt.setText( R.string.addpost );
        post_btn.setText( R.string.post );


        tag_fishignspots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(selected_fishspots_array.size() == 0){
                    StoredObjects.Selected_fishspotname = "";
                    StoredObjects.Selected_fishspot_id = "";
                    StoredObjects.Selected_fishspot_lat="0";
                    StoredObjects.Selected_fishspot_lng="0";
                    StoredObjects.Selected_fishspot_address="";
                    Intent intent = new Intent(AddpostBreedActivity.this, SearchFishingSpot.class);
                    startActivity(intent);
                }else{
                    StoredObjects.ToastMethod( "Already sishing spot selected.", AddpostBreedActivity.this );

                }

            }
        });



        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  fragmentcallinglay( new Landing_page() );

                finish();
            }
        } );
        addpost_photo_video_edtxt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ContextCompat.checkSelfPermission(AddpostBreedActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddpostBreedActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddpostBreedActivity.this, new String[]{Manifest.permission.CAMERA}, 1);

                    } else {
                        profile_pic();
                    }
                } catch (Exception e) {
                }
            }
        } );

        // addpost_photo_video_edtxt.setText( "1570096811_1504970984_photoshop-kopona_com_2.png" );

        post_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                fishingspots = "";
                for (int i = 0; i < selected_fishspots_array.size() ; i++) {
                    fishingspots += selected_fishspots_array.get(i).get("id")+",";
                }

                if(selected_fishspots_array.size() ==0){

                }else{
                    fishingspots = fishingspots.substring(0,fishingspots.length()-1);
                }






                String files_toadded = "";

                for (int i = 0; i < images_array.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject();
                        if(bundle!=null){
                            jsonObject.put("id",images_array.get(i).get("id"));
                        };

                        String filename = images_array.get(i).get("filename");
                        if (filename.contains( "mp4" )||filename.contains( "MP4" )){
                            jsonObject.put("type","video");

                        }
                        else {
                            jsonObject.put("type","Picture");
                        }


                        jsonObject.put("filename",filename);

                        files_toadded += jsonObject.toString()+",";

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                if(images_array.size() ==0){

                }else{
                    files_toadded = files_toadded.substring(0,files_toadded.length()-1);
                }

                StoredObjects.LogMethod( "response", "files_toadded:---" + files_toadded );



                browsephoto_video = addpost_photo_video_edtxt.getText().toString();

                addurl = addpost_url_edtxt.getText().toString();
                writecomment = addpost_comment_edtxt.getText().toString();



                /*if(selected_fishspots_array.size() == 0){
                    StoredObjects.ToastMethod( "Please Select Fishing Spot.", AddpostBreedActivity.this );

                }else{*/
                    if(addurl.length()>0||writecomment.length()>0||browsephoto_video.length()>0){

                        StoredObjects.hide_keyboard( AddpostBreedActivity.this );


                        if(StoredObjects.Selected_fishspot_id.equalsIgnoreCase("")){
                            lattitude = gpsTracker.getLatitude();
                            longitude = gpsTracker.getLongitude();
                            StoredObjects.Selected_fishspot_lat = lattitude+"";
                            StoredObjects.Selected_fishspot_lng = longitude+"";
                        }else{


                        }

                        if (InterNetChecker.isNetworkAvailable( AddpostBreedActivity.this )) {
                            new AddpostTask().execute(  browsephoto_video, addurl, writecomment ,"["+files_toadded+"]");
                        } else {
                            StoredObjects.ToastMethod( AddpostBreedActivity.this.getResources().getString( R.string.checkinternet ), AddpostBreedActivity.this );
                        }


                    /*if(bundle!=null){
                        if (InterNetChecker.isNetworkAvailable( getActivity() )) {
                            new EditPostTask().execute(  browsephoto_video, addurl, writecomment ,"["+files_toadded+"]");
                        } else {
                            StoredObjects.ToastMethod( getActivity().getResources().getString( R.string.checkinternet ), getActivity() );
                        }
                    }else {


                    }*/


                    }else{
                        StoredObjects.ToastMethod( "Please Enter Any image/url/comment", AddpostBreedActivity.this );
                    }
                //}


            }

            // }
            //  }
            // }
        } );

    }


    //   if (StoredObjects.inputValidation( addpost_photo_video_edtxt, getActivity().getResources().getString( R.string.selectphoto_video ), getActivity() )) {
    //     if (StoredObjects.inputValidation( addpost_url_edtxt, getActivity().getResources().getString( R.string.enterurl ), getActivity() )) {
    //if (StoredObjects.inputValidation( addpost_comment_edtxt, getActivity().getResources().getString( R.string.pleasewritecomment ), getActivity() )) {





    public void profile_pic() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};//"video"
        AlertDialog.Builder builder = new AlertDialog.Builder(AddpostBreedActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    captureImage();

                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (items[item].equals("video")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 3);

                }else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(CameraUtils.MEDIA_TYPE_IMAGE);
        if (file != null) {
            CameraUtils.imageStoragePath = file.getAbsolutePath();

        }
        StoredObjects.LogMethod("imagepath", "imagepath:--" + file.getAbsolutePath());
        Uri fileUri = CameraUtils.getOutputMediaFileUri(AddpostBreedActivity.this, file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }



    private void showPermissionsAlert() {
        /*android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());*/

        AlertDialog .Builder builder  = new AlertDialog.Builder(AddpostBreedActivity.this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(AddpostBreedActivity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private Uri picUri;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //user is returning from capturing an image using the camera

        StoredObjects.LogMethod("response", "response:---"+CAMERA_CAPTURE_IMAGE_REQUEST_CODE+"<><>"+resultCode+"<>"+RESULT_OK);
        if (requestCode == CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(AddpostBreedActivity.this, CameraUtils.imageStoragePath);
                try {

                    f_new = createNewFile("CROP_");
                    try {
                        f_new.createNewFile();
                    } catch (IOException ex) {
                        Log.e("io", ex.getMessage());
                    }

                    Photo_SHowDialog(AddpostBreedActivity.this,CameraUtils.imageStoragePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    StoredObjects.LogMethod("", "imagepathexpection:--" + e);

                }
                // successfully captured the image

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(AddpostBreedActivity.this, "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(AddpostBreedActivity.this, "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode ==3){
            if (resultCode == RESULT_OK) {
                Uri selectedvideo = data.getData();
                StoredObjects.LogMethod("picturePath","picturePath"+selectedvideo);
                String[] filePath = {MediaStore.Video.Media.DATA};
                Cursor c = AddpostBreedActivity.this.getContentResolver().query( selectedvideo, filePath, null, null, null );
                c.moveToFirst();
                int columnIndex = c.getColumnIndex( filePath[0] );
                String picturePath = c.getString( columnIndex );
                StoredObjects.LogMethod( ">><><><><",">><><><><"+picturePath );

                try{
                    File file = new File(picturePath);
                    long length = file.length();
                    length = length/1024;
                    long fileSizeInMB = length / 1024;

                    if (fileSizeInMB<=5){
                        if (InterNetChecker.isNetworkAvailable(AddpostBreedActivity.this)) {
                            new ImageUploadTaskNew().execute( picturePath );
                        }else{
                            Toast.makeText(AddpostBreedActivity.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        addpost_popup( AddpostBreedActivity.this,"" );
                    }

                }catch(Exception e){
                    System.out.println("File not found : " + e.getMessage() + e);
                }


                c.close();
            }

        }
        else if (requestCode == 2) {
            StoredObjects.LogMethod("resultcode","result code"+resultCode);
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = AddpostBreedActivity.this.getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                try {
                    Bitmap myBitmap=null;
                    picUri = data.getData();
                    myBitmap = (BitmapFactory.decodeFile(picturePath));
                    try {
                        f_new = createNewFile("CROP_");
                        try {
                            f_new.createNewFile();
                        } catch (IOException ex) {
                            Log.e("io", ex.getMessage());
                        }
                        StoredObjects.LogMethod("path", "path:::"+picturePath+"--"+myBitmap);
                        CameraUtils.imageStoragePath=picturePath;

                        Photo_SHowDialog(AddpostBreedActivity.this,picturePath);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        StoredObjects.LogMethod("", "Exception:--" + e1);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    StoredObjects.LogMethod("", "Exception:--" + e);
                }
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(AddpostBreedActivity.this, "User cancelled image picking", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(AddpostBreedActivity.this, "Sorry! Failed to pick the image", Toast.LENGTH_SHORT).show();
            }
        }

/*
        else if (requestCode == SELECT_FILE) {


            String deviceMan = Build.MANUFACTURER;
            StoredObjects.LogMethod("onresume", "deviceMan:-" + deviceMan);
            picUri = data.getData();
            String url = data.getData().toString();
            if (url.startsWith("content://com.google.android.apps.photos.content")){
                String tempPath = getPath(picUri, getActivity());
                try {
                    InputStream is = getActivity().getContentResolver().openInputStream(picUri);
                    if (is != null) {
                        Bitmap pictureBitmap = BitmapFactory.decodeStream(is);
                        f_new = createNewFile("CROP_");
                        try {
                            f_new.createNewFile();
                        } catch (IOException ex) {
                            Log.e("io", ex.getMessage());
                        }
                        Photo_SHowDialog(getActivity(),f_new,tempPath,pictureBitmap);
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{


                if (Build.VERSION.SDK_INT >Build.VERSION_CODES.N||deviceMan.contains("HUAWEI")||deviceMan.contains("motorola")|| deviceMan.equalsIgnoreCase("motorola")||deviceMan.contains("Meizu")|| deviceMan.equalsIgnoreCase("VIVO")||deviceMan.contains("meizu")||deviceMan.equalsIgnoreCase("mi") || deviceMan.equalsIgnoreCase("Mi") || deviceMan.equalsIgnoreCase("MI")||deviceMan.equalsIgnoreCase("LG nexus 5x")||deviceMan.equalsIgnoreCase("ZTE")||deviceMan.contains("LG")||deviceMan.contains("nexus")||deviceMan.equalsIgnoreCase("LG nexus 5")||deviceMan.equalsIgnoreCase("LG Nexus 5X")||deviceMan.equalsIgnoreCase("LG Nexus 5")||deviceMan.equalsIgnoreCase("oppo")||deviceMan.equalsIgnoreCase("OPPO")||deviceMan.equalsIgnoreCase("Oppo")||deviceMan.equalsIgnoreCase("ZTE Blade V6")||deviceMan.equalsIgnoreCase("zte blade v6")||deviceMan.equalsIgnoreCase("Zte blade v6")||deviceMan.equalsIgnoreCase("ZTE V6")||deviceMan.equalsIgnoreCase("zte v6")||deviceMan.equalsIgnoreCase("Zte v6")) {//||deviceMan.equalsIgnoreCase("YU")
                    // if (deviceMan.contains("Meizu")||deviceMan.contains("meizu")|| deviceMan.contains("oppo") || deviceMan.contains("OPPO") || deviceMan.contains("Oppo")||deviceMan.contains("mi") ||deviceMan.contains("Mi") || deviceMan.contains("MI") ||deviceMan.contains("vivo")||deviceMan.contains("VIVO")||deviceMan.equalsIgnoreCase("Meizu")|| deviceMan.equalsIgnoreCase("oppo")  || deviceMan.equalsIgnoreCase("MI")|| deviceMan.equalsIgnoreCase("VIVO")) {//||deviceMan.equalsIgnoreCase("YU")
                    StoredObjects.LogMethod("onresume", "deviceMan:-" + deviceMan);
                    try {
                        Bitmap myBitmap=null;
                        picUri = data.getData();
                        final String docFilePath = getFileNameByUri(getActivity(), picUri);

                        StoredObjects.LogMethod("", "imagepath:--" + docFilePath);

                        myBitmap = BitmapFactory.decodeFile(docFilePath);

                        try {

                            f_new = createNewFile("CROP_");
                            try {
                                f_new.createNewFile();
                            } catch (IOException ex) {
                                Log.e("io", ex.getMessage());
                            }
                            Photo_SHowDialog(getActivity(),f_new,docFilePath,myBitmap);
                            //new ImageUploadTaskNew().execute(docFilePath.toString());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            StoredObjects.LogMethod("", "Exception:--" + e1);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        StoredObjects.LogMethod("", "Exception:--" + e);
                    }


                } else {
                    picUri = data.getData();
                    try {
                        Log.d("uriGallery", picUri.toString());
                        performCrop(picUri);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }




        }
*/

        //user is returning from cropping the image
/*
        else if (requestCode == PIC_CROP) {
            //get the returned data

            try {
                try {
                    Bitmap myBitmap = BitmapFactory.decodeFile(mCropImagedUri.getPath());
                    //ed_pro_image.setImageBitmap(myBitmap);
                    Photo_SHowDialog(getActivity(),f_new,mCropImagedUri.getPath(),myBitmap);

                } catch (Exception e) {
                    StoredObjects.LogMethod("Exception", "Exception:::" +e);
                    e.printStackTrace();
                }

                StoredObjects.LogMethod("Exception", "Exception::" + mCropImagedUri.getPath());


            } catch (Exception e1) {
                StoredObjects.LogMethod("Exception", "Exception::" +e1);
                e1.printStackTrace();
            }

        }
*/


    }
    private void performCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("scale", true);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 500);
            cropIntent.putExtra("outputY", 500);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            // startActivityForResult(cropIntent, PIC_CROP);

            f_new = createNewFile("CROP_");
            try {
                f_new.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }

            mCropImagedUri = Uri.fromFile(f_new);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }

        //respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(AddpostBreedActivity.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    public String getPath(Uri uri, Activity activity) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.MediaColumns.DATA};
            cursor = activity.getContentResolver().query(uri, projection, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return "";
    }

    private String getFileNameByUri(Context context, Uri uri) {
        String filepath = "";//default fileName
        //Uri filePathUri = uri;
        File file;
        StoredObjects.LogMethod("", "imagepath:--" + uri);
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = context
                    .getContentResolver()
                    .query(uri,
                            new String[] {
                                    MediaStore.Images.ImageColumns.DATA,
                                    MediaStore.Images.Media.ORIENTATION },
                            null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;

        }else if (uri.getScheme().compareTo("file") == 0) {
            try {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            filepath = uri.getPath();
        }
        return filepath;
    }

    private Uri mCropImagedUri;
    File f_new;

    /**
     * Crop the image
     *
     * @return returns <tt>true</tt> if crop supports by the device,otherwise false
     */

    private File createNewFile(String prefix) {
        if (prefix == null || "".equalsIgnoreCase(prefix)) {
            prefix = "IMG_";
        }
        File newDirectory = new File( Environment.getExternalStorageDirectory() + "/mypics/");
        if (!newDirectory.exists()) {
            if (newDirectory.mkdir()) {
                Log.d(AddpostBreedActivity.this.getClass().getName(), newDirectory.getAbsolutePath() + " directory created");
            }
        }
        File file = new File(newDirectory, (prefix + System.currentTimeMillis() + ".jpg"));
        if (file.exists()) {
            //this wont be executed
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }


    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap getUnRotatedImage(String imagePath, Bitmap rotatedBitmap)
    {
        int rotate = 0;
        try
        {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(rotatedBitmap, 0, 0, rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), matrix,
                true);
    }

    public void bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();

            File file = new File(AddpostBreedActivity.this.getFilesDir(), "ProfileImages"
                    + new Random().nextInt() + ".png");

            FileOutputStream out;
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion > Build.VERSION_CODES.M) {
                out = AddpostBreedActivity.this.openFileOutput(file.getName(),
                        Context.MODE_PRIVATE);
            }else{
                out = AddpostBreedActivity.this.openFileOutput(file.getName(),
                        Context.MODE_WORLD_READABLE);
            }

            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);
            StoredObjects.LogMethod("onresume", "realPath:-" + realPath);
            if (InterNetChecker.isNetworkAvailable(AddpostBreedActivity.this)) {
                // new ImageUploadTaskNew ().execute(realPath);
            }else{
                Toast.makeText(AddpostBreedActivity.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
    }

    Dialog dialog_show;
    public void Photo_SHowDialog(final Context context,final String path){
        dialog_show = new Dialog(context);
        dialog_show.getWindow();

        dialog_show.requestWindowFeature( Window.FEATURE_NO_TITLE);
        dialog_show.setContentView(R.layout.photoshow_popup );
        dialog_show.getWindow().setLayout( WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog_show.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView captured_image = (ImageView) dialog_show.findViewById(R.id.captured_image);
        Button cancel_btn = (Button) dialog_show.findViewById(R.id.cancel_btn);
        Button saveandcontinue__btn = (Button) dialog_show.findViewById(R.id.saveandcontinue__btn);

        String fileNameSegments[] = path.split("/");
        fileName = fileNameSegments[fileNameSegments.length - 1];

       /* myImg = Bitmap.createBitmap(getResizedBitmap(getUnRotatedImage(path, BitmapFactory.decodeFile(path)), 300));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myImg.compress(Bitmap.CompressFormat.JPEG, 80, stream);

        captured_image.setImageBitmap(myImg);*/
        myImg = Bitmap.createBitmap(CameraUtils.getResizedBitmap(CameraUtils.getUnRotatedImage(path, BitmapFactory.decodeFile(path)), 300));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myImg.compress(Bitmap.CompressFormat.JPEG, 80, stream);

        captured_image.setImageBitmap(myImg);

        saveandcontinue__btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (InterNetChecker.isNetworkAvailable(AddpostBreedActivity.this)) {

                    StoredObjects.LogMethod("<><><>", "pictureFile_uploading:--"+"::path::"+path);

                    //profile_image.setImageBitmap(myImg);

                    //uploadImage();
                    if (InterNetChecker.isNetworkAvailable(AddpostBreedActivity.this)) {
                        new ImageUploadTaskNew().execute( ImageCommpression.compressImage(path) );
                    }else{
                        Toast.makeText(AddpostBreedActivity.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                    }

                    bitmapToUriConverter(myImg);

                    dialog_show.dismiss();
                }else{
                    Toast toast = Toast.makeText(context, "Please Check Internet Connection.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_show.dismiss();

            }
        });
        dialog_show.show();
    }


    public class ImageUploadTaskNew extends AsyncTask<String, String, String> {
        String strResult = "";
        String filepath  = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow123( AddpostBreedActivity.this);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                //strResult = HttpPostClass.uploadFile( params[0], StoredUrls.imageupload );
                filepath = params[0];


                String charset = "UTF-8";
                String requestURL = StoredUrls.imageupload;
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFilePart("uploaded_file", new File(params[0]));
                strResult = multipart.finish();

          /*   nameValuePairs.add(new BasicNameValuePair("method","upload-file"));
             nameValuePairs.add(new BasicNameValuePair("uploaded_file ",params[0]));
             strResult = HttpPostClass.PostMethod( StoredUrls.BaseUrl,nameValuePairs);
*/
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

               /* nameValuePairs.add(new BasicNameValuePair("method","upload-file"));
                nameValuePairs.add(new BasicNameValuePair("uploaded_file ",params[0]));*/

            // strResult = HttpPostClass.PostMethod( StoredUrls.BaseUrl,nameValuePairs);
            return strResult;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                CustomProgressbar.Progressbarcancel123(AddpostBreedActivity.this);
            }
            try {

                JSONObject jsonObject =  new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equalsIgnoreCase("200")){
                    StoredObjects.ToastMethod("uploaded succesfully",AddpostBreedActivity.this);
                    String file_name= jsonObject.getString("file_name");


                    HashMap<String, String> dash_hash = new HashMap<String, String>();
                    dash_hash.put("type","video");
                    dash_hash.put("id","1");
                    dash_hash.put("filename",file_name);
                    dash_hash.put("image_path",filepath);
                    images_array.add(dash_hash);



                    /*DumpData dumpData = new DumpData();
                    dumpData.image = file_name;
                    dumpData.image_path = filepath;
                    uploaded_images_array.add(dumpData);*/

                    addpost_photo_video_edtxt.setText( file_name );
                    StoredObjects.LogMethod( "<>>><><","<><><><>"+file_name );

                    uploaded_images_layout.removeAllViews();
                    for (int i = 0; i < images_array.size(); i++) {
                        addlayout(uploaded_images_layout,images_array,i);
                    }


                }else{
                    String error= jsonObject.getString("error");
                    StoredObjects.ToastMethod(error,AddpostBreedActivity.this);
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

    public class AddpostTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow123( AddpostBreedActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token","Mikel" ));
                nameValuePairs.add( new BasicNameValuePair( "method","add-post" ));
                nameValuePairs.add( new BasicNameValuePair( "customer_id",StoredObjects.UserId) );
                nameValuePairs.add( new BasicNameValuePair( "type","video") );
                nameValuePairs.add( new BasicNameValuePair( "description",params[2]));
                nameValuePairs.add( new BasicNameValuePair( "filename",params[0] ) );
                nameValuePairs.add( new BasicNameValuePair( "title",params[1] ) );

                nameValuePairs.add( new BasicNameValuePair( "files",params[3] ) );

                nameValuePairs.add( new BasicNameValuePair( "post_format","Scan Post" ) );

               /* nameValuePairs.add( new BasicNameValuePair( "location",address ) );
                nameValuePairs.add( new BasicNameValuePair( "lat",lattitude+"") );
                nameValuePairs.add( new BasicNameValuePair( "lng",longitude+"" ) );*/


                nameValuePairs.add( new BasicNameValuePair( "location",StoredObjects.Selected_fishspot_address ) );
                nameValuePairs.add( new BasicNameValuePair( "lat",StoredObjects.Selected_fishspot_lat+"") );
                nameValuePairs.add( new BasicNameValuePair( "lng",StoredObjects.Selected_fishspot_lng+"" ) );

                nameValuePairs.add( new BasicNameValuePair( "fishing_spot",fishingspots) );


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
                CustomProgressbar.Progressbarcancel123(AddpostBreedActivity.this);
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    StoredObjects.ToastMethod("Successfully post uploaded",AddpostBreedActivity.this);
                    //fragmentcallinglay( new Landing_page() );
                    finish();
                } else {
                    String error = jsonObject.getString( "error" );
                    StoredObjects.ToastMethod(error,AddpostBreedActivity.this);
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











    public void addlayout(LinearLayout layout,final ArrayList<HashMap<String,String>> arrayList, final int position){

        View v = LayoutInflater.from(AddpostBreedActivity.this).inflate(R.layout.photoshow_layout, null);

        FeedImageView uploaded_images_view = v.findViewById (R.id.uploaded_images_view);
        StoredObjects.LogMethod( "response", "images_array_size:---" + arrayList.size()+"<<<><><>"+""+"<<><>"+position );
        ImageView delimages_icon = v.findViewById (R.id.delimages_icon);

        delimages_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrayList.remove(position);
                uploaded_images_layout.removeAllViews();

                if(arrayList.size()-1 < 0){
                    addpost_photo_video_edtxt.setText( "" );
                }
                for(int i = arrayList.size()-1 ; i >= 0; i--){
                    StoredObjects.LogMethod( "<><<>><><><><<>", "><><><><<><>:---" + arrayList.size());
                    addlayout(uploaded_images_layout,arrayList,i);
                    //arrayList.remove (arrayList.get(i));
                }
                //bitmapToUriConverter(myImg);
            }
        });
        /*StoredObjects.LogMethod( "response", "response:---" + arrayList.get(position).get("filename") );
        myImg = Bitmap.createBitmap(getResizedBitmap(getUnRotatedImage(arrayList.get(position).get("image_path"), BitmapFactory.decodeFile(arrayList.get(position).get("image_path"))), 300));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myImg.compress(Bitmap.CompressFormat.JPEG, 80, stream);
*/
        // uploaded_images_view.setImageBitmap(myImg);



        if (arrayList.get(position).get("filename")  != null) {

            String image_url;

            if(bundle!=null){
                image_url = StoredUrls.Uploadedimages +"/"+ arrayList.get(position).get("filename");

            }else{
                image_url = StoredUrls.Uploadedimages +"/uploads/"+ arrayList.get(position).get("filename");
            }
            StoredObjects.LogMethod( "response", "images_array_size:---" + image_url );

            uploaded_images_view.setImageUrl(image_url , imageLoader);
            uploaded_images_view.setVisibility(View.VISIBLE);
            uploaded_images_view
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        }

        layout.addView(v);

    }


    public void addlayoutfishingselected(LinearLayout layout,final ArrayList<HashMap<String,String>> arrayList, final int position){

        View v = LayoutInflater.from(AddpostBreedActivity.this).inflate(R.layout.fishing_selected_textview, null);

        CustomRegularTextView uploaded_images_view = v.findViewById (R.id.uploaded_images_view);
        StoredObjects.LogMethod( "response", "images_array_size:---" + arrayList.size()+"<<<><><>"+""+"<<><>"+position );
        ImageView delimages_icon = v.findViewById (R.id.delimages_icon);

        delimages_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrayList.remove(position);
                selective_fishignspots_layout.removeAllViews();

                if(arrayList.size()-1 < 0){
                    tag_fishignspots.setText( "" );
                }
                for(int i = arrayList.size()-1 ; i >= 0; i--){
                    StoredObjects.LogMethod( "<><<>><><><><<>", "><><><><<><>:---" + arrayList.size());
                    addlayoutfishingselected(selective_fishignspots_layout,arrayList,i);
                    //arrayList.remove (arrayList.get(i));
                }
                //bitmapToUriConverter(myImg);
            }
        });

        uploaded_images_view.setText(arrayList.get(position).get("name"));

        layout.addView(v);

    }



    private void addpost_popup(final Activity activity, final String type) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addpost_popup );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
        Button ok_btn = (Button)dialog.findViewById(R.id.ok_btn);
        ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
        CustomRegularTextView logout_txt = (CustomRegularTextView)dialog.findViewById( R.id.logout_txt );

        cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        canclimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }




}

