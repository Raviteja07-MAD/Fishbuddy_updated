package com.fishbuddy.fragments;

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
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.fishbuddy.Activities.Sign_in_Sign_up;
import com.fishbuddy.R;
import com.fishbuddy.customfonts.CustomButton;
import com.fishbuddy.customfonts.CustomEditText;
import com.fishbuddy.customfonts.ImageCommpression;
import com.fishbuddy.customfonts.ScalingUtilities;
import com.fishbuddy.dumpdata.DumpData;
import com.fishbuddy.servicesparsing.CustomProgressbar;
import com.fishbuddy.servicesparsing.HttpPostClass;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.servicesparsing.JsonParsing;
import com.fishbuddy.servicesparsing.MultipartUtility;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.CameraUtils;
import com.fishbuddy.storedobjects.StoredObjects;
import com.fishbuddy.storedobjects.StoredUrls;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.fishbuddy.storedobjects.CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE;

public class Edit_profile extends Fragment {

    CustomButton edit_profile_submt_btn;
    CustomEditText edit_profile_frstname_edtxt,edit_profile_lastname_edtxt, edit_profile_email_edtxt,edit_profile_phnnumber_edtxt,edit_profile_adress_edtxt,edit_profile_about_edtxt,edit_profile_uploadpic_edtxt;
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
    //String file_name;
    ArrayList<HashMap<String, String>> getprofilelist=new ArrayList<>();
    String firstname="",lastname="",phone_number="",email_id="",adress="",about="";

    String file_name;

    ImageView profile_img;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.edit_profile,null,false );
        StoredObjects.page_type="home";
        StoredObjects.back_type="edit_profile";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new GetProfileTask().execute(StoredObjects.UserId);
        }else{
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.checkinternet),getActivity());
        }
        return v;
    }

    private void initilization(View v) {

        TextView title_txt = (TextView)v.findViewById( R.id. title_txt);
        title_txt.setText( R.string.edit_prfle );
        edit_profile_uploadpic_edtxt = (CustomEditText)v.findViewById( R.id.edit_profile_uploadpic_edtxt );
        edit_profile_lastname_edtxt = (CustomEditText)v.findViewById( R.id.edit_profile_lastname_edtxt );
        edit_profile_frstname_edtxt = (CustomEditText)v.findViewById( R.id.edit_profile_frstname_edtxt );
        edit_profile_email_edtxt = (CustomEditText)v.findViewById( R.id.edit_profile_email_edtxt );
        edit_profile_phnnumber_edtxt = (CustomEditText)v.findViewById( R.id.edit_profile_phnnumber_edtxt );
        edit_profile_adress_edtxt = (CustomEditText)v.findViewById( R.id.edit_profile_adress_edtxt );
        edit_profile_about_edtxt = (CustomEditText)v.findViewById( R.id.edit_profile_about_edtxt );
        profile_img = v.findViewById( R.id.profile_img );


        edit_profile_uploadpic_edtxt.setVisibility(View.GONE);

        edit_profile_uploadpic_edtxt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);

                    } else {

                        profile_pic();
                    }
                } catch (Exception e) {
                }


            }
        } );

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);

                    } else {

                        profile_pic();
                    }
                } catch (Exception e) {
                }
            }
        });



        ImageView backbtn_img = (ImageView)v.findViewById( R.id.backbtn_img );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay( new Profile() );
            }
        } );

        edit_profile_submt_btn = (CustomButton)v.findViewById( R.id.edit_profile_submt_btn );
        edit_profile_submt_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname = edit_profile_frstname_edtxt.getText().toString();
                lastname = edit_profile_lastname_edtxt.getText().toString();
                email_id = edit_profile_email_edtxt.getText().toString();
                phone_number = edit_profile_phnnumber_edtxt.getText().toString();
                adress = edit_profile_adress_edtxt.getText().toString();
                about = edit_profile_about_edtxt.getText().toString();

                if (StoredObjects.inputValidation( edit_profile_frstname_edtxt, getActivity().getResources().getString( R.string.enterfirstname ), getActivity() )) {
                    if (StoredObjects.inputValidation( edit_profile_lastname_edtxt, getActivity().getResources().getString( R.string.enterlastname ), getActivity() )) {
                        if (StoredObjects.inputValidation( edit_profile_email_edtxt, getActivity().getResources().getString( R.string.enteremail ), getActivity() )) {
                            if (StoredObjects.inputValidation( edit_profile_phnnumber_edtxt, getActivity().getResources().getString( R.string.enterphonenumber ), getActivity() )) {
                                if (StoredObjects.inputValidation( edit_profile_adress_edtxt, getActivity().getResources().getString( R.string.enteradress ), getActivity() )) {
                                    if (StoredObjects.inputValidation( edit_profile_about_edtxt, getActivity().getResources().getString( R.string.enterabout ), getActivity() )) {

                                        StoredObjects.hide_keyboard( getActivity() );
                                        if (InterNetChecker.isNetworkAvailable( getActivity() )) {
                                            new EditprofileTask().execute( firstname, lastname,email_id, phone_number, adress, about );


                                        } else {
                                            StoredObjects.ToastMethod( getActivity().getResources().getString( R.string.checkinternet ), getActivity() );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } );
    }
    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }

    public void profile_pic() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    captureImage();

                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (items[item].equals("Cancel")) {
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
        Uri fileUri = CameraUtils.getOutputMediaFileUri(getActivity(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void requestCameraPermission(final int type) {
        Dexter.withActivity(getActivity())
                .withPermissions( Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }


    private void showPermissionsAlert() {
        /*android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());*/

        AlertDialog .Builder builder  = new AlertDialog.Builder(getActivity());
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(getActivity());
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
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getActivity(), CameraUtils.imageStoragePath);
                try {

                    f_new = createNewFile("CROP_");
                    try {
                        f_new.createNewFile();
                    } catch (IOException ex) {
                        Log.e("io", ex.getMessage());
                    }

                    Photo_SHowDialog(getActivity(),CameraUtils.imageStoragePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    StoredObjects.LogMethod("", "imagepathexpection:--" + e);

                }
                // successfully captured the image

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
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
                StoredObjects.LogMethod("onresume", "deviceMan:-" + tempPath);
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
                    else {
                        StoredObjects.ToastMethod( "Cancelled to pick image",getActivity() );
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
        else if (requestCode == 2) {
            StoredObjects.LogMethod("resultcode","result code"+resultCode);
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);
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

                        Photo_SHowDialog(getActivity(),picturePath);
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
                Toast.makeText(getActivity(), "User cancelled image picking", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity(), "Sorry! Failed to pick the image", Toast.LENGTH_SHORT).show();
            }
        }

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
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
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
                Log.d(getActivity().getClass().getName(), newDirectory.getAbsolutePath() + " directory created");
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

   /* public void bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();

            File file = new File(getActivity().getFilesDir(), "ProfileImages"
                    + new Random().nextInt() + ".png");

            FileOutputStream out;
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion > Build.VERSION_CODES.M) {
                out = getActivity().openFileOutput(file.getName(),
                        Context.MODE_PRIVATE);
            }else{
                out = getActivity().openFileOutput(file.getName(),
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
            *//*if (InterNetChecker.isNetworkAvailable(getActivity())) {
                new ImageUploadTaskNew().execute(realPath);
            }else{
                Toast.makeText(getActivity(), "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
            }*//*
        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
    }*/



    public void bitmapToUriConverter(Bitmap mBitmap) {

        try {


            File file = new File(getActivity().getFilesDir(), "SurveyImages"
                    + new Random().nextInt() + ".png");

            FileOutputStream out;
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion > Build.VERSION_CODES.M) {
                out = getActivity().openFileOutput(file.getName(),
                        Context.MODE_PRIVATE);
            }else{
                out = getActivity().openFileOutput(file.getName(),
                        Context.MODE_WORLD_READABLE);
            }

            mBitmap.compress(Bitmap.CompressFormat.PNG, 4000, out);
            out.flush();
            out.close();

/*
            new Compressor(getActivity())
                    .compressToFileAsFlowable(file)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            compressedImage = file;
                            setCompressedImage();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            showError(throwable.getMessage());
                        }
                    });
*/



        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
    }
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }
    private File compressedImage;
    private void setCompressedImage() {

        Log.i("Compressor", "Compressed image save in " + compressedImage.getAbsolutePath());
        String realPath = compressedImage.getAbsolutePath();

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new ImageUploadTaskNew().execute(realPath);
        }else{
            //uploadimages("offline",realPath);
            Toast.makeText(getActivity(), "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }





    private String decodeFile(String path) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, 300, 300, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= 800 && unscaledBitmap.getHeight() <= 800)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 300, 300, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/myTmpDir");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

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

        myImg = Bitmap.createBitmap(CameraUtils.getResizedBitmap(CameraUtils.getUnRotatedImage(path, BitmapFactory.decodeFile(path)), 300));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myImg.compress(Bitmap.CompressFormat.JPEG, 80, stream);

        captured_image.setImageBitmap(myImg);


        saveandcontinue__btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (InterNetChecker.isNetworkAvailable(getActivity())) {

                    StoredObjects.LogMethod("", "pictureFile_uploading:--"+"::path::"+path+"<><>");//+decodeFile(path)

                    StoredObjects.LogMethod("uploadFile", "uploaded_file : "+path +"<><><>"+ ImageCommpression.compressImage(path) );//+ decodeFile(path)

                    profile_img.setImageBitmap(myImg);

                    //uploadImage();

                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        new ImageUploadTaskNew().execute(ImageCommpression.compressImage(path));
                    }else{
                        Toast.makeText(getActivity(), "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                    }
                   // bitmapToUriConverter(myImg);

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
            CustomProgressbar.Progressbarshow( getActivity());
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

            } catch (Exception e) {
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

                JSONObject jsonObject =  new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equalsIgnoreCase("200")){
                    StoredObjects.ToastMethod("Image uploaded succesfully",getActivity());
                     file_name= jsonObject.getString("file_name");

                    edit_profile_uploadpic_edtxt.setText(file_name);

                }else{
                    String error= jsonObject.getString("error");
                    StoredObjects.ToastMethod(error,getActivity());
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

 public class GetProfileTask extends AsyncTask<String, String, String> {
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
            nameValuePairs.add( new BasicNameValuePair( "method", "get-profile" ) );
            nameValuePairs.add( new BasicNameValuePair( "customer_id", params[0] ) );
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
                getprofilelist = JsonParsing.GetJsonData( results );
                /* edit_profile_frstname_edtxt,edit_profile_lastname_edtxt,
                    edit_profile_email_edtxt,edit_profile_phnnumber_edtxt
                    ,edit_profile_adress_edtxt,edit_profile_about_edtxt
                    ,edit_profile_uploadpic_edtxt;*/



                if(getprofilelist.get( 0 ).get( "name" ).equalsIgnoreCase("-")){

                }else{
                    edit_profile_frstname_edtxt.setText(  getprofilelist.get( 0 ).get( "name" ));
                }


                if(getprofilelist.get( 0 ).get( "surname" ).equalsIgnoreCase("-")){

                }else{
                    edit_profile_lastname_edtxt.setText( getprofilelist.get( 0 ).get( "surname" ) );
                }

                if(getprofilelist.get( 0 ).get( "email" ).equalsIgnoreCase("-")){

                }else{
                    edit_profile_email_edtxt.setText( getprofilelist.get( 0 ).get( "email" ) );
                }

                if(getprofilelist.get( 0 ).get( "phone" ).equalsIgnoreCase("-")){

                }else{
                    edit_profile_phnnumber_edtxt.setText( getprofilelist.get( 0 ).get( "phone" ) );
                }


                if(getprofilelist.get( 0 ).get( "image" ).equalsIgnoreCase("-")){
                    file_name = "";
                }else{
                    file_name = getprofilelist.get( 0 ).get( "image" ).replace("uploads/","");
                    edit_profile_uploadpic_edtxt.setText( getprofilelist.get( 0 ).get( "image" ) );
                }

                if(getprofilelist.get( 0 ).get( "house_number" ).equalsIgnoreCase("-")){

                }else{
                    edit_profile_adress_edtxt.setText( getprofilelist.get( 0 ).get( "house_number" ) );
                }

                if(getprofilelist.get( 0 ).get( "about" ).equalsIgnoreCase("-")){

                }else{
                    edit_profile_about_edtxt.setText( getprofilelist.get( 0 ).get( "about" ) );
                }









                try{
                    Glide.with(getActivity ())
                            .load(Uri.parse(StoredUrls.Uploadedimages + getprofilelist.get(0).get("image"))) // add your image url
                            .centerCrop() // scale to fill the ImageView and crop any extra
                            .fitCenter() // scale to fit entire image within ImageView
                            .placeholder(R.drawable.splash_logo_new)
                            .into(profile_img);
                }catch (Exception e){
                }

            } else {
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

    public class EditprofileTask extends AsyncTask<String, String, String> {
        String strResult = "";

        @Override
        protected void onPreExecute() {
            /*if (CustomProgressbar.dialog != null) {
                CustomProgressbar.dialog.dismiss();
                CustomProgressbar.dialog = null;
            }*/
            CustomProgressbar.Progressbarshow( getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 2 );

                nameValuePairs.add( new BasicNameValuePair( "token", "Mikel" ));
                nameValuePairs.add( new BasicNameValuePair( "method", "edit-profile" ));
                nameValuePairs.add( new BasicNameValuePair( "customer_id",StoredObjects.UserId) );
                nameValuePairs.add( new BasicNameValuePair( "mobile_type", "Android ") );
                nameValuePairs.add( new BasicNameValuePair( "name",params[0]));
                nameValuePairs.add( new BasicNameValuePair( "surname", params[1] ) );
                nameValuePairs.add( new BasicNameValuePair( "email",params[2] ) );
                nameValuePairs.add( new BasicNameValuePair( "phone",params[3] ) );
                nameValuePairs.add( new BasicNameValuePair( "house_number", params[4] ) );
                nameValuePairs.add( new BasicNameValuePair( "about",params[5] ) );
                nameValuePairs.add( new BasicNameValuePair( "image",file_name ) );
               /* if(file_name.equalsIgnoreCase("")){

                }else{
                    nameValuePairs.add( new BasicNameValuePair( "image",file_name ) );
                }*/




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
                    StoredObjects.ToastMethod("Successfully profile updated",getActivity());
                   //fragmentcallinglay( new Profile() );
                    getFragmentManager().popBackStack();
                } else {
                    String error = jsonObject.getString( "error" );
                    StoredObjects.ToastMethod(error,getActivity());
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
