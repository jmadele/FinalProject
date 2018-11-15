package com.example.minjia.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Used to create and manage the database of OCTranspo route numbers entered by the user.
 * Provides the table, column names, and version number.
 *
 * @author xinji on 2018-11-11
 */
public class OCTranspoDAO extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "BusRoutes.db";
    private static int VERSION_NUM = 1;
    static String TABLE_NAME = "BusTable";
    final static String KEY_ID = "_id";

    /**
     * COLUMN name in BusRoutes database. Station refers to a bus station number.
     */
    final static String KEY_STATION = "STATION";

    OCTranspoDAO(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Called if database doesn't exist. Create the database tables.
     * @param db Database manager
     */
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_STATION+" number);");
        Log.i("OCTranspoDAO", "Calling onCreate");

    }

    /**
     * On Database version update.
     * @param db Database Manager
     * @param oldVersion previous Database version
     * @param newVersion new Database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.i("OCTranspoDAO", "Calling onUpgrade, oldVersion= "+ VERSION_NUM+" newVersion= "+newVersion);
        onCreate(db);
    }

    /**
     * On Databse version down grade.
     * @param db Database Manager
     * @param oldVersion previous Database version
     * @param newVersion new Database version
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onDowngrade(db, oldVersion, newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        oldVersion = newVersion;
        VERSION_NUM = oldVersion;
        onCreate(db);
    }
}
