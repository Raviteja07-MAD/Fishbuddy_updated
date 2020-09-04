package com.fishbuddy.customfonts;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.fishbuddy.Activities.Splash;
import com.fishbuddy.R;
import com.fishbuddy.database.Database;
import com.fishbuddy.storedobjects.StoredObjects;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.Random;

import io.fabric.sdk.android.Fabric;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	LruBitmapCache mLruBitmapCache;

	private static AppController mInstance;


	Database database;

	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());



		/*OneSignal.startInit(this)
				.inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
				.unsubscribeWhenNotificationsAreDisabled(true)
				.init();*/

		database = new Database(this);
		database.getAllDevice();


		OneSignal.startInit(this)
				.inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)//Notification// to hide dialog
				// .setNotificationOpenedHandler(f)
				//.setNotificationReceivedHandler(new ExampleNotificationOpenedHandler123())
				// .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
				.init();
		;
		// OneSignal Initialization
              /*  OneSignal.startInit(this)
                        .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                        .unsubscribeWhenNotificationsAreDisabled(true)
                        .init();*/

		OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
			@Override
			public void idsAvailable(String userId, String registrationId) {
				Log.i("debug", "User:" + userId);
				if (registrationId != null)
                                    /*    StoredObjects.Udid=userId;
                                        database.UpdateUdid(StoredObjects.Udid);*/

					//database.UpdateUsertype(registrationId);
					database.UpdateUsertype(userId);
					database.getAllDevice();
					Log.d("debug", "registrationId:" + registrationId);

			}
		});




		mInstance = this;
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			getLruBitmapCache();
			mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
		}

		return this.mImageLoader;
	}

	public LruBitmapCache getLruBitmapCache() {
		if (mLruBitmapCache == null)
			mLruBitmapCache = new LruBitmapCache();
		return this.mLruBitmapCache;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}





	class ExampleNotificationOpenedHandler123 implements OneSignal.NotificationReceivedHandler {
		// This fires when a notification is opened by tapping on it.


		@Override
		public void notificationReceived(OSNotification notification) {

			Log.d("debug", "registrationId:" + "notifcation_dtectd");
			Log.d("debug", "registrationId:" + notification);
           /* OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String customKey;
            Log.d("debug", "registrationId:" + result.notification.payload.title+"<><>"+"<><>"+result.notification.payload.body);

            String[] res_notif = result.notification.payload.body.split("%@");

            Log.d("debug", "registrationId:" + result.notification.payload.title+"<><>"+res_notif[0]+"<><>"+res_notif[1]+"<><>"+result.notification.payload.body);




            generateNotification_new_test(getApplicationContext(),res_notif[0],res_notif[1],"");*/
		}
	}




	class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
		// This fires when a notification is opened by tapping on it.
		@Override
		public void notificationOpened(OSNotificationOpenResult result) {
			OSNotificationAction.ActionType actionType = result.action.type;
			JSONObject data = result.notification.payload.additionalData;
			String customKey;
			Log.d("debug", "registrationId:" + result.notification.payload.title+"<><>"+"<><>"+result.notification.payload.body);

			String[] res_notif = result.notification.payload.body.split("%@");

			Log.d("debug", "registrationId:" + result.notification.payload.title+"<><>"+res_notif[0]+"<><>"+res_notif[1]+"<><>"+result.notification.payload.body);




			generateNotification_new_test(getApplicationContext(),res_notif[0],res_notif[1],"");


                  /*      Intent intent = new Intent(getApplicationContext(), NotifcationActiviy.class);
                    intent.putExtra("page_url",res_notif);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);


                        int requestCode = 0;

                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_install);

                        android.support.v4.app.NotificationCompat.Builder noBuilder = new android.support.v4.app.NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.logo_install).setLargeIcon(largeIcon).setContentTitle(res_notif[0])//result.notification.payload.title)
                                .setContentText(res_notif[0])//result.notification.payload.body
                                .setAutoCancel(true).setDefaults(android.app.Notification.DEFAULT_ALL)
                                .setContentIntent(pendingIntent).setSound(sound);


                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification


                        if (data != null) {
                                customKey = data.optString("customkey", null);
                                if (customKey != null)
                                        Log.i("OneSignalExample", "customkey set with value: " + customKey);
                        }

                        if (actionType == OSNotificationAction.ActionType.ActionTaken)
                                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);


                        Log.i("OneSignalExample", "ExampleNotificationOpenedHandler");*/
		}

	}


/*
    class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String customKey;
            Log.d("debug", "registrationId:" + result.notification.payload.title+"<><>"+"<><>"+result.notification.payload.body);
            String[] res_notif = result.notification.payload.body.split("%@");
            Log.d("debug", "registrationId:" + result.notification.payload.title+"<><>"+res_notif[0]+"<><>"+res_notif[1]+"<><>"+result.notification.payload.body);
            generateNotification_new_test(getApplicationContext(),res_notif[0],res_notif[1],"");


        }
*/




	public  void generateNotification_new_test(Context context, String content, String notificationContent, String uri) {

		try {

			int icon = R.drawable.install_icon;
			long when = System.currentTimeMillis();

			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				mBuilder.setChannelId("com.mictvapp");
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				@SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(
						"com.mictvapp",
						"MicTv",
						NotificationManager.IMPORTANCE_DEFAULT
				);
				if (notificationManager != null) {
					notificationManager.createNotificationChannel(channel);
				}
			}
			String title = context.getString(R.string.app_name);
			Intent notificationIntent = null;

			notificationIntent = new Intent(context, Splash.class);
			notificationIntent.putExtra("title",content);
			notificationIntent.putExtra("desc",notificationContent);
			// notificationIntent.putExtra("date",uri);

			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
					Intent.FLAG_ACTIVITY_SINGLE_TOP);

			PendingIntent intent =
					PendingIntent.getActivity(context,  new Random().nextInt(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT );
			Random random = new Random();
			int m = random.nextInt(9999 - 1000) + 1000;


			Notification notification1 = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
					.setAutoCancel(true)
					.setContentTitle(title)
					.setStyle(new NotificationCompat.BigTextStyle().bigText(content))
					.setContentIntent(intent)
					.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
					.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.install_icon))
					.setContentText(notificationContent).build();

			notification1.flags |= Notification.FLAG_AUTO_CANCEL;

			notificationManager.notify(m, notification1);



		} catch (NullPointerException e) {
			// TODO: handle exception
		}catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
		}catch (IllegalArgumentException e) {
			// TODO: handle exception
		}catch (IllegalStateException e) {
			// TODO: handle exception
		}

	}







}
