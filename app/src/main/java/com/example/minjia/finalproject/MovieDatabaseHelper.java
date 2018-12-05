package com.example.minjia.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
This class extend SQLiteOpenHelper, is used to create database and table.
 */
public class MovieDatabaseHelper extends SQLiteOpenHelper{
    private static String DATABASE_NAME = "MovieInformation.db"; // DATABASE  name
    private static int VERSION_NUM = 1;
    public static final String KEY_ID = "ID";    // column ID
    public static final String KEY_TITLE = "Title"; // table column title
    public static final String KEY_ACTOR= "Actors"; // table column Actors
    public static final String KEY_YEAR = "Year";  // Table columx Year
    public static final String KEY_RUNTIME = "Runtime"; // Table column Runtime
    public static final String KEY_RATING = "Rating"; //Table column Rating
    public static final String KEY_PLOT = "Plot"; // Table column Plot
    public static final String KEY_URL = "URL"; // Table column URL
    public static final String TABLE_NAME = "MovieTable"; // TABLE NAME
    public static final String KEY_IMAGE="MoviePoster"; // Table column image data.

    // This string is used to create table.
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME
            + "( " + KEY_ID + " integer primary key autoincrement, "
            +KEY_IMAGE + " blob,"
            + KEY_TITLE  + " text ,"
            + KEY_ACTOR + " text ,"
            + KEY_YEAR + " text ,"
            + KEY_RUNTIME + " text ,"
            + KEY_RATING + " text ,"
            + KEY_PLOT + " text ,"
            + KEY_URL + " text );";

    public MovieDatabaseHelper(Context ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);//create table.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteItem(int key){
        this.getWritableDatabase().execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+KEY_ID
       +" = "+ key);
    }
  // Delete the whole table
    public void delete(){
        this.getWritableDatabase().execSQL("DELETE FROM "+TABLE_NAME);
    }
}
