package com.fishbuddy.servicesparsing;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by android-4 on 1/25/2018.
 */

public class ProgressBarClass {

    public static ProgressDialog progressDialog;

    public static Dialog dialog;

    public static void Progressbarshow(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }
    public static void Progressbarcancel(Context context) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


}
