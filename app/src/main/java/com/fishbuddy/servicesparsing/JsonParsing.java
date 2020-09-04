package com.fishbuddy.servicesparsing;




import com.fishbuddy.storedobjects.StoredObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kiran on 22-08-2018.
 */

public class JsonParsing {


   public static ArrayList<HashMap<String, String>> GetJsonData(String results) throws JSONException {
       ArrayList<HashMap<String, String>> d_child = new ArrayList<HashMap<String, String>>();
       d_child.clear();
       try {
           StoredObjects.LogMethod("status_res", "status_res:--"+results);
           JSONArray cast = new JSONArray(results);
          // JSONObject jsonObject = cast.getJSONObject(0);
           //StoredObjects.LogMethod("tag", "tag >>>" + jsonObject.names());
          // JSONArray myStringArray = jsonObject.names();
           for(int i = 0;i<cast.length();i++){
               JSONObject jsonObject1 = cast.getJSONObject(i);
               JSONArray myStringArray = jsonObject1.names();
               HashMap<String, String> dash_hash = new HashMap<String, String>();
               for(int j = 0;j<myStringArray.length();j++){
                   if(jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase("0000-00-00 00:00:00")||jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase("")||jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase("null")||jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase(null)){
                       dash_hash.put(myStringArray.getString(j), "-");
                   }else{
                       dash_hash.put(myStringArray.getString(j), jsonObject1.getString(myStringArray.getString(j)));
                   }

                   StoredObjects.LogMethod("tag", "values >>>" +myStringArray.getString(j)+"---"+jsonObject1.getString(myStringArray.getString(j)));
               }
               dash_hash.put("addedtocart","No");
               dash_hash.put("pro_quantity","0");
               dash_hash.put("status_val","No");
               dash_hash.put("sel_favorites","No");
               d_child.add(dash_hash);
           }


       } catch (JSONException e) {
           e.printStackTrace();
       }





       return d_child;
   }


    public static ArrayList<HashMap<String, String>> GetJsonData1(String results) throws JSONException {
        ArrayList<HashMap<String, String>> d_child = new ArrayList<HashMap<String, String>>();
        d_child.clear();
        try {
            StoredObjects.LogMethod("status_res", "status_res:--"+results);
            JSONArray cast = new JSONArray(results);
           // JSONObject jsonObject = cast.getJSONObject(0);
            //StoredObjects.LogMethod("tag", "tag >>>" + jsonObject.names());

            for(int i = 0;i<cast.length();i++){
                JSONObject jsonObject1 = cast.getJSONObject(i);
                JSONArray myStringArray = jsonObject1.names();
                HashMap<String, String> dash_hash = new HashMap<String, String>();
                for(int j = 0;j<myStringArray.length();j++){
                    if(jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase("0000-00-00 00:00:00")||jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase("")||jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase("null")||jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase(null)){
                        dash_hash.put(myStringArray.getString(j), "-");
                        dash_hash.put("keyname", myStringArray.getString(j));
                        dash_hash.put("keyname_val", jsonObject1.getString(myStringArray.getString(j)));
                    }else{
                        dash_hash.put(myStringArray.getString(j), jsonObject1.getString(myStringArray.getString(j)));
                        dash_hash.put("keyname", myStringArray.getString(j));
                        dash_hash.put("keyname_val", jsonObject1.getString(myStringArray.getString(j)));
                    }

                    StoredObjects.LogMethod("tag", "values >>>" +myStringArray.getString(j)+"---"+jsonObject1.getString(myStringArray.getString(j)));
                }
                dash_hash.put("addedtocart","No");
                dash_hash.put("pro_quantity","0");
                dash_hash.put("status_val","No");
                dash_hash.put("sel_favorites","No");
                d_child.add(dash_hash);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }





        return d_child;
    }


}
