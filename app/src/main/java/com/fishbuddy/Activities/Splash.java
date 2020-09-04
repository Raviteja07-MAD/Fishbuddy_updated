package com.fishbuddy.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.fishbuddy.R;
import com.fishbuddy.database.Database;
import com.fishbuddy.sidemenu.SideMenu;
import com.fishbuddy.storedobjects.StoredObjects;

import io.fabric.sdk.android.Fabric;


/**
 * Created by android-4 on 2/27/2019.
 */

public class Splash extends AppCompatActivity {

    private static final int TIME = 1 * 1000;// 4 seconds
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.splash);
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        database = new Database(Splash.this);
        database.getAllDevice();
        Fabric.with(this, new Crashlytics());
        StoredObjects.printHashKey(Splash.this);

    }

    @Override
    protected void onResume() {
        Log.i("onresume", "onresume");
        GenerateSplashScreenMethod();
        super.onResume();
    }


    public void GenerateSplashScreenMethod(){
        new Handler().postDelayed( new Runnable() {

            @Override
            public void run() {

              /*  Intent intent=new Intent(Splash.this, Sign_in_Sign_up.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/

                if(StoredObjects.UserId.equalsIgnoreCase("0")){
                    startActivity(new Intent(Splash.this, Sign_in_Sign_up.class));
                    Splash.this.finish();

                }else{
                    startActivity(new Intent(Splash.this, SideMenu.class));
                    Splash.this.finish();

                }



              /*//  StoredObjects.savedata(context,"slides","No");
                String val = StoredObjects.getsaveddata(Splash.this,"slides");
                Log.i("onresume", "onresume"+"<><>"+val);

               */



            }
        }, TIME);
    }


}