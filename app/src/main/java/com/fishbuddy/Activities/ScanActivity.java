package com.fishbuddy.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fishbuddy.R;
import com.fishbuddy.database.Database;
import com.fishbuddy.servicesparsing.InterNetChecker;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.StoredObjects;
import com.google.zxing.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.fishbuddy.sidemenu.SideMenu.btm_addpost_txt;
import static com.fishbuddy.sidemenu.SideMenu.btm_log_catch_img;
import static com.fishbuddy.sidemenu.SideMenu.btm_log_catch_lay;
import static com.fishbuddy.sidemenu.SideMenu.btm_log_catch_txt;
import static com.fishbuddy.sidemenu.SideMenu.buttonchangemethod;


public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    Database database;
    private ZXingScannerView mScannerView;
    public ArrayList<HashMap<String, String>> productsdetailslist=new ArrayList<>();
    ImageView fish_info_img;
    String scanresults="";


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        database=new Database(ScanActivity.this);
        database.getAllDevice();
        StoredObjects.back_type="Scan_activity";
        setContentView( R.layout.scannerview);
        SideMenu.buttonchangemethod (ScanActivity.this , btm_log_catch_lay , btm_log_catch_img , btm_log_catch_txt ,"0");
        fish_info_img = findViewById( R.id.fish_info_img );
        fish_info_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fish_details_popup(ScanActivity.this);
            }
        } );

        mScannerView=(ZXingScannerView) findViewById(R.id.camerascanview);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        /*1D barcodes: EAN-13, EAN-8, UPC-A, UPC-E, Code-39, Code-93, Code-128, ITF, Codabar.
        2D barcodes: QR Code, Data Matrix, PDF-417, AZTEC.*/
        scanresults="";
        scanresults= rawResult.getText();
        if (InterNetChecker.isNetworkAvailable(ScanActivity.this)) {
           // new GetProductDetailsTask().execute(scanresults);
        }else{
            Toast.makeText(ScanActivity.this,"Please check Internet connection..", Toast.LENGTH_SHORT).show();
        }

        // onBackPressed();

        //If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);

    }


    private void fish_details_popup(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logcatch_details_popup);
        Objects.requireNonNull( dialog.getWindow() ).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
        canclimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



}