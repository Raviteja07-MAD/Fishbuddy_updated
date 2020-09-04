package com.fishbuddy.customcamara;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.fishbuddy.R;
import com.fishbuddy.customadapter.HashMapRecycleviewadapter;
import com.fishbuddy.customfonts.CustomButton;
import com.fishbuddy.customfonts.ImageCommpression;
import com.fishbuddy.fragments.Addpost;
import com.fishbuddy.servicesparsing.CustomProgressbar;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.CameraUtils;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.fishbuddy.storedobjects.CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE;

public class CameraActivity extends Activity implements Camera.PictureCallback, SurfaceHolder.Callback {

    public static final String EXTRA_CAMERA_DATA = "camera_data";

    private static final String KEY_IS_CAPTURING = "is_capturing";

    private Camera mCamera;
    private ImageView mCameraImage;
    private SurfaceView mCameraPreview;
    RelativeLayout layout_frame;
    private CustomButton mCaptureImageButton,doneButton,galley_pick_btn;
    private byte[] mCameraData;
    private boolean mIsCapturing;


    String picturePath = "";

    String fromwhere = "";



    ArrayList<HashMap<String, String>> fishbreeds_list=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishbreeds_listall=new ArrayList<>();
    ArrayList<HashMap<String, String>> fishbreeds_listall_filter=new ArrayList<>();

    int pageCount = 1;
    int totalpages;
    String total_results ,total_pages= "0";

    private View.OnClickListener mCaptureImageButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            captureImage();
        }
    };

    private View.OnClickListener mRecaptureImageButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setupImageCapture();
        }
    };

    private View.OnClickListener mDoneButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            StoredObjects.LogMethod("<><>>","<><><><><>>"+"clicked"+mCameraData+"<><>"+CameraUtils.imageStoragePath);
           // finish();

            if(picturePath.equalsIgnoreCase("")){


                if (mCameraData != null) {
                /*Intent intent = new Intent();
                intent.putExtra(EXTRA_CAMERA_DATA, mCameraData);
                setResult(RESULT_OK, intent);
                finish();*/

                    File saveFile = openFileForImage();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
                    //mCameraImage.setImageBitmap(bitmap);
                    String file_path = saveImageToFile(saveFile,bitmap);

                    StoredObjects.LogMethod("<><>>","<><><><><>>"+"clicked"+file_path);


                    StoredObjects.scanned_imagepath_type   = "camara";
                    if (InterNetChecker.isNetworkAvailable(CameraActivity.this)) {
                        StoredObjects.scanned_imagepath = saveFile.getPath();
                        new ImageUploadTaskNew().execute( saveFile.getPath() );
                    }else{
                        Toast.makeText(CameraActivity.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                    }

                /*Intent returnIntent = new Intent();
                returnIntent.putExtra("data", mCameraData);// problem lies here
                setResult(RESULT_OK, returnIntent);*/
                    // CameraActivity.this.finish();


                } else {
                    setResult(RESULT_CANCELED);
                    // finish();
                    Toast.makeText(CameraActivity.this, "No Image Captured." ,
                            Toast.LENGTH_LONG).show();
                }

            }else{
                StoredObjects.LogMethod("<><>>","<><><><><>>"+"clicked<gallery><"+picturePath);


                if (InterNetChecker.isNetworkAvailable(CameraActivity.this)) {
                    StoredObjects.scanned_imagepath_type  = "gallery";
                    StoredObjects.scanned_imagepath = picturePath;
                    new ImageUploadTaskNew().execute( picturePath );
                }else{
                    Toast.makeText(CameraActivity.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }



            //CameraActivity.this.finish();
        }
    };


    private Uri picUri;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //user is returning from capturing an image using the camera

        StoredObjects.LogMethod("response", "response:---" + CAMERA_CAPTURE_IMAGE_REQUEST_CODE + "<><>" + resultCode + "<>" + RESULT_OK+"<><>"+requestCode);
        //if (requestCode == CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            /*if (resultCode == RESULT_OK) {

            } else if (requestCode == 3) {

            } else*/ if (requestCode == 2) {
                StoredObjects.LogMethod("resultcode", "result code" + resultCode);
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = CameraActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                     picturePath = c.getString(columnIndex);
                    c.close();
                    StoredObjects.LogMethod("<><>>","<><><><><>>"+"clicked<gallery123><"+picturePath);
                   // StoredObjects.LogMethod("resultcode", "picturepath" + picturePath);


                    mCameraPreview.setVisibility(View.INVISIBLE);
                    mCameraImage.setVisibility(View.VISIBLE);


                    File imgFile = new  File(picturePath);

                    if(imgFile.exists()){

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                       // mCameraImage.setImageBitmap(myBitmap);

                        try {
                            mCameraImage.setImageBitmap(modifyOrientation(myBitmap,imgFile.getAbsolutePath()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }


                   /* if (InterNetChecker.isNetworkAvailable(CameraActivity.this)) {
                        new ImageUploadTaskNew().execute( picturePath );
                    }else{
                        Toast.makeText(CameraActivity.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                    }*/
/*
                    try {
                        Bitmap myBitmap = null;
                        picUri = data.getData();
                        myBitmap = (BitmapFactory.decodeFile(picturePath));
                        try {
                            f_new = createNewFile("CROP_");
                            try {
                                f_new.createNewFile();
                            } catch (IOException ex) {
                                Log.e("io", ex.getMessage());
                            }
                            StoredObjects.LogMethod("path", "path:::" + picturePath + "--" + myBitmap);
                            CameraUtils.imageStoragePath = picturePath;

                            //Photo_SHowDialog(getActivity(), picturePath);

                            if (InterNetChecker.isNetworkAvailable(CameraActivity.this)) {
                                new ImageUploadTaskNew().execute( picturePath );
                            }else{
                                Toast.makeText(CameraActivity.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            StoredObjects.LogMethod("response", "Exception:--" + e1);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        StoredObjects.LogMethod("response", "Exception:--" + e);
                    }
*/
                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(CameraActivity.this, "User cancelled image picking", Toast.LENGTH_SHORT).show();
                } else {
                    // failed to capture image
                    Toast.makeText(CameraActivity.this, "Sorry! Failed to pick the image", Toast.LENGTH_SHORT).show();
                }
            }
      //  }
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        StoredObjects.scanned_imagepath = "";

        mCameraImage = (ImageView) findViewById(R.id.camera_image_view);
        mCameraImage.setVisibility(View.INVISIBLE);

        mCameraPreview = (SurfaceView) findViewById(R.id.preview_view);
            layout_frame = findViewById(R.id.layout_frame);
        final SurfaceHolder surfaceHolder = mCameraPreview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCaptureImageButton = (CustomButton) findViewById(R.id.capture_image_button);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);

         doneButton = (CustomButton) findViewById(R.id.done_button);
        doneButton.setOnClickListener(mDoneButtonClickListener);

        galley_pick_btn = findViewById(R.id.galley_pick_btn);

        galley_pick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, 1);

                    } else {
                        Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                } catch (Exception e) {
                }
            }
        });

        mIsCapturing = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_IS_CAPTURING, mIsCapturing);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mIsCapturing = savedInstanceState.getBoolean(KEY_IS_CAPTURING, mCameraData == null);
        if (mCameraData != null) {
            setupImageDisplay();
        } else {
            setupImageCapture();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                camaraparameters();
                mCamera.setPreviewDisplay(mCameraPreview.getHolder());
                if (mIsCapturing) {
                    mCamera.startPreview();
                }
            } catch (Exception e) {
                Toast.makeText(CameraActivity.this, "Unable to open camera.", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    public void camaraparameters(){
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        //Parameters parameters = mCamera.getParameters();
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0) {
            mCamera.setDisplayOrientation(90);
        }


        if(display.getRotation() == Surface.ROTATION_270) {
            mCamera.setDisplayOrientation(180);
        }

        //mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        mCameraData = data;
        setupImageDisplay();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            try {


               /* Camera.Parameters parameters = mCamera.getParameters();
                Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

                if(display.getRotation() == Surface.ROTATION_0) {
                    parameters.setPreviewSize(height, width);
                    mCamera.setDisplayOrientation(90);
                }

                if(display.getRotation() == Surface.ROTATION_90) {
                    parameters.setPreviewSize(width, height);
                }

                if(display.getRotation() == Surface.ROTATION_180) {
                    parameters.setPreviewSize(height, width);
                }

                if(display.getRotation() == Surface.ROTATION_270) {
                    parameters.setPreviewSize(width, height);
                    mCamera.setDisplayOrientation(180);
                }

                mCamera.setParameters(parameters);*/



                mCamera.setPreviewDisplay(holder);
                if (mIsCapturing) {
                    mCamera.startPreview();
                }
            } catch (IOException e) {
                Toast.makeText(CameraActivity.this, "Unable to start camera preview.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private void captureImage() {
        mCamera.takePicture(null, null, this);
    }

    private void setupImageCapture() {
        mCameraImage.setVisibility(View.INVISIBLE);
        mCameraPreview.setVisibility(View.VISIBLE);
        mCamera.startPreview();
        mCaptureImageButton.setText("Take Photo");
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);
    }

    private void setupImageDisplay() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
        //mCameraImage.setImageBitmap(bitmap);

        try {

            File saveFile = openFileForImage();
            String file_path = saveImageToFile(saveFile,bitmap);

            /*ExifInterface exif = new ExifInterface(file_path);     //Since API Level 5
            String exifOrientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            StoredObjects.LogMethod("status_res", "images_array_fishingspots:--"+exifOrientation);
*/
            if (bitmap.getWidth() > bitmap.getHeight()) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            mCameraImage.setImageBitmap(bitmap);
          //  mCameraImage.setImageBitmap(modifyOrientation(bitmap,file_path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCamera.stopPreview();
        mCameraPreview.setVisibility(View.INVISIBLE);
        mCameraImage.setVisibility(View.VISIBLE);
       // mCameraImage.setRotationX(90);
       // mCameraImage.setRotationY(90);
        //mCameraImage.setRotation(90);

        mCaptureImageButton.setText("Re Take Photo");
        mCaptureImageButton.setOnClickListener(mRecaptureImageButtonClickListener);
    }


    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            parameters.set("s3d-prv-frame-layout", "none");
            parameters.set("s3d-cap-frame-layout", "none");
            parameters.set("iso", "auto");
            parameters.set("contrast", 100);
            parameters.set("brightness", 50);
            parameters.set("saturation", 100);
            parameters.set("sharpness", 100);
            parameters.setAntibanding("auto");
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.set("jpeg-quality", 100);
            /*if (params.isZoomSupported())
                params.setZoom(0);*/
            parameters.setPictureSize(800, 600);
            parameters.setRotation(180);
            camera.setDisplayOrientation(90);
            camera.setParameters(parameters);
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }



    public class ImageUploadTaskNew extends AsyncTask<String, String, String> {
        String strResult = "";
        String filepath  = "";
        @Override
        protected void onPreExecute() {
            CustomProgressbar.Progressbarshow123( CameraActivity.this);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                strResult = HttpPostClass.uploadFile123( params[0], StoredUrls.imageupload_new );
                filepath = params[0];

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
                CustomProgressbar.Progressbarcancel123(CameraActivity.this);
            }
            StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_imagename"+result);
//[{"barbel":"0.31"},{"bream":"0.06"},{"chub":"0.63"},{"identified":"chub"}]
            try {
                if (InterNetChecker.isNetworkAvailable(CameraActivity.this)) {
                    new CheckBreedTask().execute( result );
                }else{
                    Toast.makeText(CameraActivity.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---"+e);
            }
        }
    }
    public class CheckBreedTask extends AsyncTask<String, String, String> {
        String strResult = "";
        String filepath  = "";
        @Override
        protected void onPreExecute() {
          //  CustomProgressbar.Progressbarshow( CameraActivity.this);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
               /* strResult = HttpPostClass.uploadFile123( params[0], StoredUrls.imageupload_new );
                filepath = params[0];*/

             /*nameValuePairs.add(new BasicNameValuePair("method","upload-file"));
             nameValuePairs.add(new BasicNameValuePair("uploaded_file ",params[0]));*/
                String Url = StoredUrls.breedimagecheck_url+params[0];
                StoredObjects.LogMethod("<><>>","<><><><><>>"+"clicked"+Url);

                strResult = HttpPostClass.GET( Url);
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
                //CustomProgressbar.Progressbarcancel(CameraActivity.this);
            }
            StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_result"+result);

            try {

              /*  AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);

                builder.setMessage("Fish Breed Response:"+result)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();                       }

                        });


            //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();*/
              //  ArrayList<HashMap<String, String>> fishbreeds_listall_filter=new ArrayList<>();

               // ArrayList<HashMap<String, String>> fishbreeds_list_final=new ArrayList<>();
                fishbreeds_listall_filter.clear();
                fishbreeds_listall_filter = JsonParsing.GetJsonData1(result);

                /*Intent intent = new Intent(CameraActivity.this,LogcatchClass.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",fishbreeds_list);
                intent.putExtra("BUNDLE",args);
               // intent.putStringArrayListExtra("array", fishbreeds_list);
                startActivity(intent);
                CameraActivity.this.finish();*/

                /*StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_result"+fishbreeds_listall_filter);
                ArrayList<Float> floatvalus_array=new ArrayList<>();
                for (int i = 0; i <fishbreeds_listall_filter.size() ; i++) {

                    StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_result"+fishbreeds_listall_filter.get(i).get("keyname_val"));
                    Float aFloat = Float.parseFloat(fishbreeds_listall_filter.get(i).get("keyname_val"));
                    floatvalus_array.add(aFloat*100);
                }
                Collections.sort(floatvalus_array, Collections.reverseOrder());


                ArrayList<Float> floatvalus_array1 = new ArrayList<>();
                for (int i = 0; i <fishbreeds_listall_filter.size() ; i++) {

                    for (int j = 0; j <floatvalus_array.size() ; j++) {
                        StoredObjects.LogMethod("<><>>","<><><><><>>"+"timepasscheck"+fishbreeds_listall_filter.get(i).get("keyname_val")+"<><>"+floatvalus_array.get(j));
                        if(fishbreeds_listall_filter.get(i).get("keyname_val").equalsIgnoreCase(floatvalus_array.get(j)+"")){
                            floatvalus_array1.add(floatvalus_array.get(j));
                        }

                    }

                }
                for (int i = 0; i <floatvalus_array1.size() ; i++) {
                    StoredObjects.LogMethod("<><>>","<><><><><>>"+"timepasscheck<><><>"+floatvalus_array1.get(i)+"<><>"+i);

                }*/
                //dash_hash.put("keyname",fishbreeds_listall_filter.get(j).get("keyname"));
                //dash_hash.put("keyname_val",fishbreeds_listall_filter.get(j).get("keyname_val"));


                getfishbreedsdata(pageCount);


            }
            catch (Exception e) {
                StoredObjects.LogMethod("response", "response:---"+e);
            }
        }
    }


    public void getfishbreedsdata(int page_count){
        if (InterNetChecker.isNetworkAvailable(CameraActivity.this)) {
            new FishBreddsTask().execute(StoredObjects.UserId,page_count+"");
        }else{
            StoredObjects.ToastMethod(getResources().getString(R.string.checkinternet),CameraActivity.this);
        }
    }

    private Uri mCropImagedUri;
    File f_new;

    private File createNewFile(String prefix) {
        if (prefix == null || "".equalsIgnoreCase(prefix)) {
            prefix = "IMG_";
        }
        File newDirectory = new File( Environment.getExternalStorageDirectory() + "/mypics/");
        if (!newDirectory.exists()) {
            if (newDirectory.mkdir()) {
                Log.d(CameraActivity.this.getClass().getName(), newDirectory.getAbsolutePath() + " directory created");
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



    private File openFileForImage() {
        File imageDirectory = null;
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            imageDirectory = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "FishBuddyCamara");//com.oreillyschool.android2.camera
            if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
                imageDirectory = null;
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd_hh_mm",
                        Locale.getDefault());

                return new File(imageDirectory.getPath() +
                        File.separator + "image_" +
                        dateFormat.format(new Date()) + ".png");
            }
        }
        return null;
    }

    private String saveImageToFile(File file,Bitmap mCameraBitmap) {
        String file_path = "";
        if (mCameraBitmap != null) {
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(file);
                if (!mCameraBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)) {
                    Toast.makeText(CameraActivity.this, "Unable to save image to file.",
                            Toast.LENGTH_LONG).show();
                } else {
                    file_path = file.getPath();
                    /*Toast.makeText(CameraActivity.this, "Saved image to: " + file.getPath(),
                            Toast.LENGTH_LONG).show();*/
                }
                outStream.close();
            } catch (Exception e) {
                Toast.makeText(CameraActivity.this, "Unable to save image to file.",
                        Toast.LENGTH_LONG).show();
            }
        }
        return  file_path;
    }




    public class FishBreddsTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
              CustomProgressbar.Progressbarshow123( CameraActivity.this );
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );
                nameValuePairs.add( new BasicNameValuePair( "token", "Mikel" ) );
                nameValuePairs.add( new BasicNameValuePair( "method", StoredUrls.fish_breeds ) );
                nameValuePairs.add( new BasicNameValuePair( "customer_id", params[0] ) );
                //  nameValuePairs.add( new BasicNameValuePair( "page", params[1] ) );
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
                CustomProgressbar.Progressbarcancel123(CameraActivity.this);
            }
            try {

                JSONObject jsonObject = new JSONObject( result );
                String status = jsonObject.getString( "status" );
                if (status.equalsIgnoreCase( "200" )) {
                    String results = jsonObject.getString( "results" );
                    total_results = jsonObject.getString( "total_results" );
                    total_pages = jsonObject.getString( "total_pages" );

                    // fishbreeds_list = JsonParsing.GetJsonData( results );
                   /* if (fishbreeds_list.size()>0){
                        customRecyclerview.Assigndatatorecyleviewhashmap(fishbreads_recyclerview, fishbreeds_list,"fishbreeds", StoredObjects.Gridview, 2, StoredObjects.ver_orientation, R.layout.fishbreeds_listitems);
                    }
                    else {
                        StoredObjects.ToastMethod( "No Data Found", getActivity());
                    }*/


                    fishbreeds_list.clear();
                    fishbreeds_list = JsonParsing.GetJsonData(results);
                    fishbreeds_listall.clear();
                    for (int i = 0; i <fishbreeds_list.size() ; i++) {

                        for (int j = 0; j <fishbreeds_listall_filter.size() ; j++) {
                            if(fishbreeds_list.get(i).get("species").toLowerCase().contains(fishbreeds_listall_filter.get(j).get("keyname"))){

                                HashMap<String, String> dash_hash = new HashMap<String, String>();
                                dash_hash.put("id",fishbreeds_list.get(i).get("id"));
                                dash_hash.put("species",fishbreeds_list.get(i).get("species"));
                                dash_hash.put("extra_info",fishbreeds_list.get(i).get("extra_info"));
                                dash_hash.put("appearance",fishbreeds_list.get(i).get("appearance"));
                                dash_hash.put("habitat",fishbreeds_list.get(i).get("habitat"));
                                dash_hash.put("behavior",fishbreeds_list.get(i).get("behavior"));
                                dash_hash.put("weight",fishbreeds_list.get(i).get("weight"));
                                dash_hash.put("length",fishbreeds_list.get(i).get("length"));
                                dash_hash.put("edibility",fishbreeds_list.get(i).get("edibility"));
                                dash_hash.put("uk_record",fishbreeds_list.get(i).get("uk_record"));
                                dash_hash.put("bait",fishbreeds_list.get(i).get("bait"));
                                dash_hash.put("season",fishbreeds_list.get(i).get("season"));
                                dash_hash.put("water",fishbreeds_list.get(i).get("water"));
                                dash_hash.put("fishing_methods",fishbreeds_list.get(i).get("fishing_methods"));
                                dash_hash.put("binomial_name",fishbreeds_list.get(i).get("binomial_name"));
                                dash_hash.put("images_count",fishbreeds_list.get(i).get("images_count"));
                                dash_hash.put("images",fishbreeds_list.get(i).get("images"));

                                dash_hash.put("keyname",fishbreeds_listall_filter.get(j).get("keyname"));
                                dash_hash.put("keyname_val",fishbreeds_listall_filter.get(j).get("keyname_val"));

                                fishbreeds_listall.add(dash_hash);
                            }
                        }

                    }




                    Intent intent = new Intent(CameraActivity.this,LogcatchClass.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST",fishbreeds_listall);
                    args.putSerializable("ARRAYLIST1",fishbreeds_list);
                    intent.putExtra("BUNDLE",args);
                    // intent.putStringArrayListExtra("array", fishbreeds_list);
                    startActivity(intent);
                    CameraActivity.this.finish();


                  //  fragmentcalling1(new LogCatchFragment(),fishbreeds_listall,0);






                    // StoredObjects.LogMethod("val","val:::"+"yes<><><>"+fishbreeds_listall.size()+"<><>"+fishbreeds_listall);
                    /*pieEntries = new ArrayList<>();
                    for (int i = 0; i <fishbreeds_listall.size() ; i++) {
                        *//*HashMap<String, String> dash_hash = new HashMap<String, String>();
                        dash_hash.put("id",fishbreeds_list.get(i).get("id"));*//*
                        Float aFloat = Float.parseFloat(fishbreeds_listall.get(i).get("keyname_val"));
                        aFloat = aFloat/fishbreeds_listall.size();
                        pieEntries.add(new PieEntry(aFloat, fishbreeds_listall.get(i).get("species")+"="+fishbreeds_listall.get(i).get("keyname_val")));
                    }*/




                   /* pieDataSet = new PieDataSet(pieEntries, "");
                    pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    pieDataSet.setSliceSpace(2f);
                    pieDataSet.setValueTextColor(Color.WHITE);
                    pieDataSet.setValueTextSize(10f);
                    pieDataSet.setSliceSpace(5f);

                    pieDataSet.notifyDataSetChanged();

                    StoredObjects.LogMethod("<><>>","<><><><><>>"+"final_result"+fishbreeds_listall.size());

                    mShimmerViewContainer.setVisibility(View.GONE);
                    landing_recycle.setVisibility( View.VISIBLE );
                    //nodatafound_lay.setVisibility( View.GONE );
                    adapter = new HashMapRecycleviewadapter(LogcatchClass.this,fishbreeds_listall,"identified_breed",landing_recycle,R.layout.identifiedbreed_listitem );//
                    landing_recycle.setAdapter(adapter);
                    adapter.notifyDataSetChanged();*/


                    /*if (fishbreeds_listall.size() > 0) {
                        mShimmerViewContainer.setVisibility( View.GONE );
                        landing_recycle.setVisibility( View.VISIBLE );
                        nodatafound_lay.setVisibility( View.GONE );
                        adapter = new HashMapRecycleviewadapter(LogcatchClass.this,fishbreeds_listall,"identified_breed",landing_recycle,R.layout.identifiedbreed_listitem );//
                        landing_recycle.setAdapter(adapter);
                    } else {
                        mShimmerViewContainer.setVisibility( View.GONE );
                        landing_recycle.setVisibility( View.GONE );
                        nodatafound_lay.setVisibility( View.VISIBLE );
                    }*/


                }else{

                    String error = jsonObject.getString( "error" );
                    //StoredObjects.ToastMethod(error,SidemenuIndividual.this);
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


   /* public void fragmentcalling1(Fragment fragment, ArrayList<HashMap<String, String>> list, int position) {
        FragmentManager fragmentManager = CameraActivity.this.getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable("YourHashMap", list);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }*/


    public  static  Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        StoredObjects.LogMethod("status_res", "images_array_fishingspots:--"+orientation);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}