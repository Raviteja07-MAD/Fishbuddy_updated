package com.fishbuddy.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;


import com.fishbuddy.dumpdata.DumpData;
import com.fishbuddy.storedobjects.StoredObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Database extends SQLiteOpenHelper {

    public String UserId= "user_id";
    public String Product_id= "pro_id";
    public String Product_image="pro_img";
    public String Product_name="pro_name";
    public String Product_code="pro_code";
    public String Product_SKU="pro_sku";
    public String Product_soldas="pro_soldas";
    public String ProQtypercarton="qtypercarton";
    public String ProQty="pro_qty";
    public String ProPrice="pro_price";
    public String Mycart_table= "mycart";

    Context context;
    public Database(Context applicationcontext) {
        super(applicationcontext, "fishbuddy.db", null, 2);
        Log.d("database","Created");
        context = applicationcontext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        String mycart_query;
        query = "CREATE TABLE userdata ( idno INTEGER PRIMARY KEY,Userid Text,usertype Text,ReqDelDate Text,gcm_regid Text)";
        db.execSQL(query);
        mycart_query = "CREATE TABLE "+ Mycart_table
                +"( idno INTEGER PRIMARY KEY,"+ UserId + " Text,"+ Product_id + " Text,"+ Product_name+" Text,"
                + Product_image+" Text,"+ Product_code+" Text,"+ Product_SKU+" Text,"+ Product_soldas+" Text,"
                + ProQtypercarton+" Text,"+ ProQty+" Text,"+ ProPrice+" Text"+")";//ProPrice


        db.execSQL(mycart_query);
        query = "insert into userdata(Userid,usertype,ReqDelDate,gcm_regid)values('0','0','0','0')";
        Log.i("query", "query"+query);
        db.execSQL(query);


        //for language chnages
        String languageToLoad = "en"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("language", languageToLoad);
        editor.apply();


      /*  StoredObjects.savedata(getApplicationContext(),"Yes","on_new_user_followed");
        StoredObjects.savedata(getApplicationContext(),"Yes","on_another_user_commented_on_your_post");
        StoredObjects.savedata(getApplicationContext(),"Yes","on_new_post_by_user_you_following");
        StoredObjects.savedata(getApplicationContext(),"Yes","on_another_user_liked_your_post");
        StoredObjects.savedata(getApplicationContext(),"Yes","on_new_post_by_your_follower");*/



        Log.d("userdata","userdata Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS userdata";
        db.execSQL(query);
        onCreate(db);

    }

    public ArrayList<HashMap<String, String>> getAllDevice() {

        StoredObjects.LogMethod("hello","hello");
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM userdata";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);



        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    StoredObjects.LogMethod("hello","hello"+"<>"+cursor.getColumnNames()+"<>"+database.getVersion());
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("idno", cursor.getString(cursor.getColumnIndex("idno")));
                    map.put("Userid", cursor.getString(cursor.getColumnIndex("Userid")));
                    map.put("usertype", cursor.getString(cursor.getColumnIndex("usertype")));
                    map.put("ReqDelDate", cursor.getString(cursor.getColumnIndex("ReqDelDate")));
                    map.put("gcm_regid", cursor.getString(cursor.getColumnIndex("gcm_regid")));
                    wordList.add(map);
                    StoredObjects.UserId = cursor.getString(1);
                    StoredObjects.User_type=cursor.getString(2);
                    StoredObjects.gcm_regid = cursor.getString(4);
                  //  StoredObjects.ReqDelvrydate=cursor.getString(3);
                 //   StoredObjects.PONumber=cursor.getString(4);
                    StoredObjects.LogMethod("hello","hello"+StoredObjects.UserId +"<><>"+StoredObjects.User_type+"<><>"+StoredObjects.gcm_regid);
                } while (cursor.moveToNext());

            }
        }
        StoredObjects.LogMethod("hello","wordList"+wordList);

        return wordList;
    }
    public void UpdateLogintype(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Logintype", userId);
        database.update("userdata", values,null, null);//insert("userdata", null, values);
        database.close();

    }

    public void insertID(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Userid", userId);
        database.insert("userdata", null, values);
        database.close();

    }

    public void deleteLastDataTable() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("userdata", null, null);
    }
    public void UpdateUserId(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Userid", userId);
        database.update("userdata", values,null, null);//insert("userdata", null, values);
        database.close();

    }

    public void UpdateGcmRegid(String usertype) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usertype", usertype);
        database.update("userdata", values,null, null);//insert("userdata", null, values);
        database.close();

    }



    public void UpdateUsertype(String gcm_regid) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("gcm_regid", gcm_regid);
        database.update("userdata", values,null, null);//insert("userdata", null, values);
        database.close();

    }

    public void UpdateReqDelDate(String ReqDelDate) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ReqDelDate", ReqDelDate);
        database.update("userdata", values,null, null);//insert("userdata", null, values);
        database.close();

    }
    public void UpdatePONumber(String PONumber) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PONumber", PONumber);
        database.update("userdata", values,null, null);//insert("userdata", null, values);
        database.close();

    }
    public void Additemtocart(String user_id, String product_id, String pro_name, String pro_img, String pro_code
            , String pro_sku, String pro_soldas, String proqtypercarton, String proqty) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(UserId, user_id);
        values.put(Product_id, product_id);
        values.put(Product_name, pro_name);
        values.put(Product_image, pro_img);
        values.put(Product_code, pro_code);
        values.put(Product_SKU, pro_sku);
        values.put(Product_soldas, pro_soldas);
        values.put(ProQtypercarton, proqtypercarton);
        values.put(ProQty, proqty);


        database.insert(Mycart_table, null, values);
        database.close();

    }


    public boolean checkitemfromcart(String User_id, String ItemId_) {

        String[] columns = {
                UserId
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = UserId + " = ?" + " AND " + Product_id + " = ?";
        String[] selectionArgs = {User_id, ItemId_};
        Cursor cursor = db.query(Mycart_table, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    public void UpdateCart(String user_id, String item_id, String quanity) {

        ArrayList<DumpData> wordList;
        wordList = new ArrayList<DumpData>();
        wordList.clear();
        String selectQuery = "UPDATE " + Mycart_table +" SET "+ ProQty +" = "+ quanity  +" where "+UserId +"="+user_id +" and " +Product_id +" = "+item_id ;

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(selectQuery);


    }


    public void DeleteCartitem(String user_id, String item_id) {

        StoredObjects.LogMethod("hello","hello"+user_id+"------"+item_id);

        String selectQuery = "DELETE  FROM " + Mycart_table +" where "+UserId +"="+user_id +" and " +Product_id +"="+item_id ;
        StoredObjects.LogMethod("hello","hello:--"+selectQuery);
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(selectQuery);

    }
    public void DeleteCart(String user_id){
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete(Mycart_table, UserId + "=?", new String[] { user_id });
    }




}
