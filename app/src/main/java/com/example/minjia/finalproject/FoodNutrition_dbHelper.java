package com.example.minjia.finalproject;

import android.app.Activity;
import android.os.Bundle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * This class extend SQLiteOpenHelper, is used to create database and table.
 */
public class FoodNutrition_dbHelper extends SQLiteOpenHelper{
    private static String DATABASE_NAME = "FoodNutrition.db";
    private static int VERSION_NUM = 1;
    public static final String KEY_ID = "ID";
    public static final String KEY_NAME = "Name";
    public static final String KEY_CALORIES= "Calories";
    public static final String KEY_URI = "URI";
    public static final String TABLE_NAME = "FoodNutritionTable";
    public static final String KEY_IMAGE="FoodNutritionPoster";

    // This string is used to create table.
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME
            + "( " + KEY_ID + " integer primary key autoincrement, "
            + KEY_IMAGE + " blob,"
            + KEY_ID  + " text ,"
            + KEY_NAME + " text ,"
            + KEY_CALORIES + " text ,"
            + KEY_URI + " text );";

    public FoodNutrition_dbHelper(Context ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }

    /**
     * To create table
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
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

    public void delete(){
        this.getWritableDatabase().execSQL("DELETE FROM "+TABLE_NAME);
    }
}
