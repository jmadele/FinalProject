package com.example.minjia.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CBCDatabaseHelper extends SQLiteOpenHelper {
    public static SQLiteDatabase db;
    protected static final String DATABASE_NAME="CBCNews.db";
    protected static final String TABLE_NAME="CBC News";
    protected static final String KEY_ID ="id";
    protected static final String KEY_NEWS= "CBC News";
    protected static final int VERSION_NUM =2;

    static final String DATABASE_CREATE = "Create table CBCNews (id integer primary key autoincrement,"
            + " CBC News not null)";

    public CBCDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        Log.i("CBCNewsDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
//    public boolean insertData(String title){
//        db = this.getWritableDatabase();
//        ContentValues contentValues=new ContentValues();
//        contentValues.put(COL_1, id);
//        contentValues.put(COL_2, title);
//        long result = db.insert(TABLE_NAME,null, contentValues);
//        db.close();
//        if(result==-1){
//            return false;
//        }else
//            return true;
//    }
}
