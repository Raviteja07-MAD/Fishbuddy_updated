package com.fishbuddy.customfonts;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.fishbuddy.Activities.Splash;
import com.fishbuddy.R;
import com.fishbuddy.storedobjects.StoredObjects;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationPayload;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

public class NotificationExtenderBareBonesExample extends NotificationExtenderService {


    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        // Read properties from result.
        Log.d("debug", "registrationId:" + "notifcation_dtectd_service");
        Log.d("debug", "registrationId:" + receivedResult.toString());




        String notification = receivedResult.toString();
        String notificationBody = receivedResult.payload.body;

        Log.d("debug", "registrationId:" + notificationBody.toString());
        /*JSONObject notificationBodyJSON = null;
        try {
            notificationBodyJSON = new JSONObject(notificationBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject pushNotificationData = notificationBodyJSON;
        boolean hidden = false;

        if (pushNotificationData.has("update")) {
            Log.i("NOTIFICATION MANAGER", "PREVENT DISPLAY NOTIFICATION");

            hidden = true;
        }

        Log.d("debug", "registrationId:" + notificationBodyJSON.toString());*/


        String[] res_notif = notificationBody.split("@%");
        try {
            generateNotification_new_test(getApplicationContext(), res_notif[0] ,URLEncoder.encode(res_notif[1], "utf-8"));//URLEncoder.encode(res_notif[0], "utf-8")
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

       /* OSNotificationAction.ActionType actionType = receivedResult.action.type;
        JSONObject data = result.notification.payload.additionalData;*/
        // Returning true tells the OneSignal SDK you have processed the notification and not to display it's own.
        return true;
    }


    public  void generateNotification_new_test(Context context, String content, String notificationContent) {

        try {

            int icon = R.drawable.install_icon;
            long when = System.currentTimeMillis();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder.setChannelId("com.fishbuddy");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(
                        "com.fishbuddy",
                        "fishbuddy",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }
            String title = context.getString(R.string.app_name);
            Intent notificationIntent = null;

            notificationIntent = new Intent(context, Splash.class);
            notificationIntent.putExtra("page_url",notificationContent);
            // notificationIntent.putExtra("date",uri);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent intent =
                    PendingIntent.getActivity(context,  new Random().nextInt(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT );
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;


            Notification notification1 = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    //.setContentTitle(content)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                    .setContentIntent(intent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.install_icon))
                    .setContentText(content).build();//notificationContent

            notification1.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(m, notification1);
            if(checksettings(context,content)){
           //    notificationManager.notify(m, notification1);
            }
            StoredObjects.LogMethod( "response", "registrationId:---" + checksettings(context,content));




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





    public boolean checksettings(Context context,String title){
         boolean checkval = false;

        try{

            String on_new_user_followed = "" ;
            String on_another_user_commented_on_your_post= "" ;
            String on_new_post_by_user_you_following = "" ;
            String on_another_user_liked_your_post= "" ;
            String on_new_post_by_your_follower = "" ;


            on_new_user_followed = StoredObjects.getsaveddata(context,"on_new_user_followed");
            on_another_user_commented_on_your_post = StoredObjects.getsaveddata(context,"on_another_user_commented_on_your_post");
            on_new_post_by_user_you_following = StoredObjects.getsaveddata(context,"on_new_post_by_user_you_following");
            on_another_user_liked_your_post = StoredObjects.getsaveddata(context,"on_another_user_liked_your_post");
            on_new_post_by_your_follower = StoredObjects.getsaveddata(context,"on_new_post_by_your_follower");

            StoredObjects.LogMethod( "response", "registrationId:---" + on_new_user_followed+"<>"+on_another_user_commented_on_your_post+"<>"+on_new_post_by_user_you_following
                    +"<>"+on_another_user_liked_your_post+"<>"+on_new_post_by_your_follower);


            if(title.equalsIgnoreCase("Another user followed your profile")){
                if(on_new_user_followed.equalsIgnoreCase("Yes")){
                    return  true;
                }else{
                    return  false;
                }
            }

            if(title.equalsIgnoreCase("Another user commented on your post")){
                if(on_another_user_commented_on_your_post.equalsIgnoreCase("Yes")){
                    return  true;
                }else{
                    return  false;
                }
            }


            if(title.equalsIgnoreCase("New post from user you follow")){
                if(on_new_post_by_user_you_following.equalsIgnoreCase("Yes")){
                    return  true;
                }else{
                    return  false;
                }
            }


            if(title.equalsIgnoreCase("Another user liked your post")){
                if(on_another_user_liked_your_post.equalsIgnoreCase("Yes")){
                    return  true;
                }else{
                    return  false;
                }
            }


            if(title.equalsIgnoreCase("Another user followed your profile")){
                if(on_new_post_by_your_follower.equalsIgnoreCase("Yes")){
                    return  true;
                }else{
                    return  false;
                }
            }


        }catch (Exception e){

        }

        return  checkval;
    }
}
///Another user followed your profile  follow

//Another user commented on your post   comment
//New post from user you follow     new post

//Another user followed your profile  another user foollow

//Another user liked your post like post