package com.example.minjia.finalproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Main Activity for the OCTranspo route information App.
 * User can enter a bus station number (4 digits) which is saved to the database and displayed on
 * screen in a list.
 * User can then select a route from the list and get the routes available from that station/stop.
 *
 * @author xinji on 2018-11-12
 */


public class OCTranspo extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "OCTransport Activity";
    private ProgressBar progressBar;
    private ImageView busImage;
    private EditText stationInput;
    private ListView stationsList;
    private Button submitButton;

    private Dialog stationDialog;


    /**
     * Called on activity start. Provides content view, loads resources and sets button functions.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo);

        stationsList = findViewById(R.id.stationsList);

        submitButton = findViewById(R.id.submitButton);

        stationInput = findViewById(R.id.stationInput);

        progressBar = findViewById(R.id.progress);

        busImage = findViewById(R.id.busImg);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A dialog")
                .setPositiveButton("Hello", new DialogInterface.OnClickListener() {
         @Override
            public void onClick(DialogInterface dialog, int which) {
             Toast.makeText(getApplicationContext(), "You clicked on ...", Toast.LENGTH_SHORT).show();
             }
        }).create().show();


        Toast.makeText(this,"searching", Toast.LENGTH_SHORT).show();

        submitButton.setOnClickListener(e ->{
         Snackbar.make(e, "submitting", Snackbar.LENGTH_LONG).show();


    });

    }


}
