package com.fishbuddy.servicesparsing;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fishbuddy.R;
import com.github.ybq.android.spinkit.SpinKitView;

import java.io.IOException;
import java.io.InputStream;




public class CustomProgressbar {

	public static Dialog dialog1;
	public static void Progressbarshow123(Context context) {

		if(context!=null){
			dialog1 = new Dialog(context);
			dialog1.getWindow();
			dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog1.setContentView( R.layout.customprogressbar);
			dialog1.setCancelable(true);
			dialog1.setCanceledOnTouchOutside(true);
			dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2BFFFFFF")));

			dialog1.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			dialog1.show();
		}

	}

	public static void Progressbarcancel123(Context context) {
		if(context != null){

			if (dialog1 != null) {
				dialog1.dismiss();
			}
		}

	}



	/*public static ProgressDialog progressDialog;

	public static void Progressbarshow(Context context) {
		progressDialog = new ProgressDialog(context);
		//progressDialog.setMessage("Please wait...");
		//progressDialog.setCancelable(false);
		progressDialog.setCancelable(true);
		progressDialog.setIndeterminate(true);
		progressDialog.show();
	}

	public static void Progressbarcancel(Context context) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}*/



	public static Dialog dialog;

	public static void Progressbarshow(final Context context) {

		/*runOnUiThread(new Runnable() {
			public void run() {*/
				dialog = new Dialog(context);
				dialog.getWindow();
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.loadingprogress);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
				dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

				LinearLayout giflayout = (LinearLayout)dialog.findViewById(R.id.giflayout);
				SpinKitView spin_kit = dialog.findViewById(R.id.spin_kit);
				spin_kit.setVisibility(View.VISIBLE);
				//giflayout.setVisibility(View.GONE);
			//	InputStream stream = null;
				/*try {
					//stream = context.getAssets().open("piggy.gif");
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				/*GifWebView view = new GifWebView(context, "file:///android_asset/animated_new.gif");
				view.setBackgroundColor(Color.TRANSPARENT);
				giflayout.addView(view);*/

				dialog.show();
			}
		/*});


	}*/
	public static void Progressbarcancel(Context context) {

		/*runOnUiThread(new Runnable() {

			@Override
			public void run() {*/
				if (dialog != null) {
					dialog.dismiss();
				}
			}
	/*	});

	}
*/



}
