package com.fishbuddy.storedobjects;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fishbuddy.dumpdata.DumpData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kiran on 22-08-2018.
 */

public class StoredObjects {

    public static String UserId = "0";
    public static String gcm_regid = "0";
    public static String User_type = "0";
    public static int listcount = 0;
    public static String page_type = "";
    public static String back_type = "";
    public static String Logintype="";//NoofPax
    public static String notific_count = "";
    public static String UserType="";
    public static int count;
    public static String Gridview="Grid";
    public static String Listview="List";
    public static int ver_orientation= LinearLayoutManager.VERTICAL;
    public static int horizontal_orientation= LinearLayoutManager.HORIZONTAL;

    public static String map_select_address="";
    public static String map_select_lattitude="";
    public static String map_select_longitude="";

    public static String scanned_imagepath="";
    public static String scanned_imagepath_type="";

    public static String Selected_fishspotname="";
    public static String Selected_fishspot_id="";

    public static String Selected_fishspot_lat="0";
    public static String Selected_fishspot_lng="0";
    public static String Selected_fishspot_address="";



    //Log
    public static void LogMethod(String keyval, String val) {
        Log.i(keyval, val);
    }

    //Displaying toast
    public static void ToastMethod(String message, Context context) {
        Toast.makeText(context, message, 0).show();
    }



    public static boolean inputValidation(EditText edittext, String message, Activity activity) {
        if (edittext.getText().toString().trim().equals("") || edittext.getText().toString().trim().equals(null)) {
            Toast.makeText(activity, message,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService( Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Check Email Validation
    public static boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static ArrayList<DumpData> homecatgrylst = new ArrayList<>();

/*"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
        "  <soap12:Header>\n" +
        "    <wsbdetails xmlns=\"http://tempuri.org/\">\n" +
        "      <Bearer>FB8CFB5C-8E26-44A2-B213-B9086242F4BC</Bearer>\n" +
        "    </wsbdetails>\n" +
        "  </soap12:Header>\n" +
        "  <soap12:Body>\n" +
        "    <User_Dashboard xmlns=\"http://tempuri.org/\">\n" +
        "      <userid>10001</userid>\n" +
        "    </User_Dashboard>\n" +
        "  </soap12:Body>\n" +
        "</soap12:Envelope>";*/
public static Spanned stripHtml(String html) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //Spanned htmlAsSpanned = Html.fromHtml(html);
        return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
    } else {
        return Html.fromHtml(html);
    }
}


    public static Spanned stripHtml1(String html) {


            return Html.fromHtml(html);
    }

    public static void savedata(Context context, String val, String name){

        SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(name, val);
        editor.commit();


    }
    public  static String getsaveddata(Context context,String name){
        SharedPreferences prefs = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String _name = prefs.getString(name, null);
        return _name;


    }
    public static String Inputstring(String inputtype, String parameters) {

        String return_text = "";
        String totalparameters = "";

        if(parameters.equalsIgnoreCase("")){
            totalparameters = "";
        }else{
            String[] parametersarray=parameters.split("&");

            for(int k=0;k<parametersarray.length;k++){
                String innerparameters[]=parametersarray[k].split("=");
                totalparameters=totalparameters+"<"+innerparameters[0]+">"+innerparameters[1]+"</"+innerparameters[0]+">\n";
            }
        }
        return_text="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Header>\n" +
                "    <wsbdetails xmlns=\"http://tempuri.org/\">\n" +
                "      <Bearer>"+StoredUrls.Bearer+"</Bearer>\n" +
                "    </wsbdetails>\n" +
                "  </soap12:Header>\n" +
                "  <soap12:Body>\n" +   "    <"+inputtype+ " xmlns=\"http://tempuri.org/\">\n" +
                totalparameters+ "    </"+inputtype+">\n" +"  </soap12:Body>\n" +
                "</soap12:Envelope>";
        return return_text;
    }
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    public static void hashmaplist(int val) {
        dummy_list.clear();

        for (int k = 0; k < val; k++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", "Alex Gosh");
            hashMap.put("email", "alex@gmail.com");
            hashMap.put("mobile", "120020909");
            hashMap.put("amount", "12");
            hashMap.put("english", "English");
            hashMap.put("experince", "20+ years of experience");
            hashMap.put("speclization", "General Obestetrics & Gynecology");
            hashMap.put("id", "#56789");
            hashMap.put("gift", "Raised Gift Voucher");
            hashMap.put("typeofamount", "Inward Amount");
            hashMap.put("addres_list", "Lorem Ipsum is simply dummy text of the printing");
            hashMap.put("date", "12 Dec 2018");
            hashMap.put("time", "12:00pm");
            hashMap.put("is_like", "No");

            if (k == 0) {
                hashMap.put("status", "Yes");
                hashMap.put("cat_name", "Vegetables");
            } else if (k == 1) {
                hashMap.put("status", "No");
                hashMap.put("cat_name", "Green leaves");
            } else {
                hashMap.put("status", "No");
                hashMap.put("cat_name", "Fruits");
            }

            hashMap.put("car_question", "How often does a car requires services?");
            hashMap.put("car_answer", "Car requires services yearly and major maintenance at 6,000 miles.");
            hashMap.put("is_count", "No");
            hashMap.put("pro_quantity", "0");
            hashMap.put("datetme", "3rd May at 12:00pm");
            hashMap.put("descr", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book");
            dummy_list.add(hashMap);
        }

    }

    public static void hide_dialogkeyboardtextview(Activity activity, AutoCompleteTextView editText) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }catch (Exception e){

        }
    }



    public static String getYouTubeId(String youTubeUrl) {
        String pattern = "https?://(?:[0-9A-Z-]+\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|</a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static void AlertForCall(final Activity activity, final String customer_care_number) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(customer_care_number)
                .setCancelable(false)
                .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                       /* Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + customer_care_number));*/

                        //activity.startActivity(new Intent (Intent.ACTION_CALL).setData(Uri.parse("tel:"+ SavePref.getPref(activity, customer_care_number))));



                        if (ContextCompat.checkSelfPermission(activity,
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(activity,
                                        Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 1);

                        } else {

                            // String mobile_number  = SavePref.getPref(activity, customer_care_number);

                            StoredObjects.LogMethod("response", "response<><><><>><:---"+customer_care_number);

                            activity.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+customer_care_number)));                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }

                });

//Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static String getCompleteAddressString(Activity activity, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.i("address_final", "address_final_converted:-"+strReturnedAddress.toString());

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();//+","+addresses.get(0).getAddressLine(1);

                Log.i("address_final", "address_final_converted:-"+address+"/n<><>"+city+"/n<><>"+state+"/n<><>"+
                        country+"/n<><>"+postalCode+"/n<><>"+knownName+"<><>"+addresses.get(0).getSubLocality()+"<><>"+addresses.get(0).getSubAdminArea());

                /*citynameedtxt.setText(city+"/"+state);
                pincode_edtxt.setText(postalCode);
                colony_area_sector_edtxt.setText(address);*///addresses.get(0).getSubLocality()


            } else {
                Log.i("address_final", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("address_final", "Canont get Address!");
        }
        return strAdd;
    }


    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("hashkey_print", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("hashkey_print", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("hashkey_print", "printHashKey()", e);
        }
    }


    public static int mathfloat(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        return (n - c) % 2 == 0 ? (int) f : c;
    }


}