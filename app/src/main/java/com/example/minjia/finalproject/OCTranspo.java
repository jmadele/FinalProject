package com.example.minjia.finalproject;



import android.app.Dialog;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import android.widget.Toast;



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


        Toast.makeText(this,"searching", Toast.LENGTH_SHORT).show();

        submitButton.setOnClickListener(e -> Snackbar.make(e, "submitting", Snackbar.LENGTH_LONG).show());

    }


}
