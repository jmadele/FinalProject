package com.example.minjia.finalproject;

import android.database.Cursor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class extend SQLiteOpenHelper, is used to create database and table.
 */
public class FoodNutrition_dbHelper extends SQLiteOpenHelper{
    private static  FoodNutrition_dbHelper helper;
    private static String DATABASE_NAME = "FoodNutrition.db";
    private static int VERSION_NUM = 1;

    public static final String KEY_ID = "ID";
    public static final String KEY_NAME = "Name";
    public static final String KEY_LABEL = "label";
    public static final String KEY_CALORIES= "Calories";
    public static final String KEY_PROCNT = "PROCNT";
    public static final String KEY_FAT = "FAT";
    public static final String KEY_CHOCDF = "CHOCDF";
    public static final String KEY_FIBTG = "FIBTG";
    public static final String TABLE_NAME = "FoodNutritionTable";


    // This string is used to create table.
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME
            + "( " + KEY_ID + " integer primary key autoincrement, "
            + KEY_NAME + " text,"
            + KEY_LABEL  + " text ,"
            + KEY_CALORIES + " text ,"
            + KEY_PROCNT + " text ,"
            + KEY_FAT + " text ,"
            + KEY_CHOCDF + " text ,"
            + KEY_FIBTG + " text );";

    private FoodNutrition_dbHelper(Context ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }

    public static FoodNutrition_dbHelper getHelper(Context ctx){
        if(helper == null) helper = new FoodNutrition_dbHelper(ctx);
        return helper;
    }

    /**
     * this getAllData method will put the data into the table
     * @return
     */

    public List<Map<String, String>> getAllDate(){
        List<Map<String, String>> foods = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        //use cursor to represents a 2 dimensional table of the food database.
        Cursor cursor = db.rawQuery(query, null);
        //the Hashmap permits null values and the null key while implements map class
        Map<String, String> food = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                food.put("name", cursor.getString(1));
                food.put("label",cursor.getString(2));
                food.put("calories",cursor.getString(3));
                food.put("procnt",cursor.getString(4));
                food.put("fat",cursor.getString(5));
                food.put("chocdf",cursor.getString(6));
                food.put("fibtg",cursor.getString(7));
                foods.add(food);
            } while (cursor.moveToNext());
        }
        db.close();
        return foods;
    }

    /**
     * To create a table
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    /**
     * drop old table if newVersion exists
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * drop new table and downgrade to old table
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * deleteitem when needed if id avaliable
     * @param key
     */
    public void deleteItem(int key){
        this.getWritableDatabase().execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+KEY_ID
                +" = "+ key);
    }

    /**
     * delete from table when needed
     */
    public void delete(){
        this.getWritableDatabase().execSQL("DELETE FROM "+TABLE_NAME);
    }
}
