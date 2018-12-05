package com.example.minjia.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CBCDatabaseHelper extends SQLiteOpenHelper {
    public static SQLiteDatabase db;
    protected static final String DATABASE_NAME="CBCNews.db";
    protected static final String TABLE_NAME="SavedNews";
    protected static final String KEY_ID ="id";
    protected static final String COLUMN_NEWS= "TITLE";
    protected static final int VERSION_NUM =1;



    public CBCDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT)");
        Log.i("CBCNewsDatabaseHelper", "Calling onCreate");
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

}
