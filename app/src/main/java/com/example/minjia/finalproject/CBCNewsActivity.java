package com.example.minjia.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CBCNewsActivity extends Activity {
    private static final String ACTIVITY_NAME = "CBCNews Reader Activity";
    private ProgressBar progressBar;
    private ImageView CBCNewsImage;
    private TextView newsTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcnews);

        progressBar = findViewById(R.id.progressBar);
        CBCNewsImage=findViewById(R.id.CBCNewsImage);



    }
}
