/*
package com.fishbuddy.servicesparsing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.fishbuddy.R;

import java.io.IOException;
import java.io.InputStream;



public class CustomProgressbar_test {

	public static MyProgressDialog progressDialog;

	public static Dialog dialog;

	*/
/*public static void Progressbarshow(final Context context) {

		runOnUiThread(new Runnable() {
			public void run() {
				progressDialog = new MyProgressDialog(context);
				progressDialog.setMessage("Please wait...");
				//progressDialog.setCancelable(false);
				progressDialog.setCancelable(true);
				progressDialog.setIndeterminate(true);
				progressDialog.show();
			}
		});


	}
	public static void Progressbarcancel(Context context) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		});

	}*//*





	public static void Progressbarshow(final Context context) {

		runOnUiThread( new Runnable() {
			@Override
			public void run() {
				dialog = new Dialog(context);
				dialog.getWindow();
				dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
				dialog.setContentView( R.layout.loadingprogress);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
				dialog.getWindow().setLayout( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

				LinearLayout giflayout = (LinearLayout)dialog.findViewById(R.id.giflayout);
				giflayout.setVisibility( View.GONE);
				InputStream stream = null;
				try {
					stream = context.getAssets().open("piggy.gif");
				} catch (IOException e) {
					e.printStackTrace();
				}
				//GifWebView view = new GifWebView(context, "file:///android_asset/piggy.gif");

				//giflayout.addView(view);

				dialog.show();
			}
		});


	}
	public static void Progressbarcancel(Context context) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});

	}


*/
/*
	public static void Logoutpopup(final Activity activity, String message) {
		 dialog = new Dialog(activity);
		dialog.getWindow();
		dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.errormessagepopup);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
		dialog.getWindow().setLayout( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
		Button ok_btn = (Button)dialog.findViewById(R.id.ok_btn);
		Button cancel_btn = (Button)dialog.findViewById(R.id.cancel_btn);
		ImageView cancel_img = (ImageView)dialog.findViewById(R.id.cancel_img);
		NimbusRegularTextView logout_txt = dialog.findViewById(R.id.logout_txt);

		logout_txt.setText(message);

		cancel_lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				dialog.dismiss();
			}
		});

		cancel_img.setOnClickListener(new View.OnClickListener() {
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

		cancel_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				dialog.dismiss();
			}
		});

		dialog.show();
	}
*//*









}
*/
