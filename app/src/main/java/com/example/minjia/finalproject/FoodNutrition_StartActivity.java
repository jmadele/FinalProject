package com.example.minjia.finalproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.design.widget.Snackbar;
import android.widget.Toolbar;

public class FoodNutrition_StartActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "Food Nutrition Activity";
    private ProgressBar foodNutritionProgressBar;
    private ImageView foodNutritionImage;
    private EditText searchEditText;
    private ListView foodListView;
    private Button searchButton;
    private Dialog foodNutritionDialog;
    final int SEARCH_REQUEST=6;
    //private Toolbar foodNutritionToolBar;


    /**
     * Called on activity start. Provides content view, loads resources and sets button functions.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition_start_activity);

        foodNutritionProgressBar = findViewById(R.id.foodNutritionProgressBar);
        foodNutritionImage = findViewById(R.id.foodNutritionImage);
        searchEditText = findViewById(R.id.searchEditText);
        foodListView = findViewById(R.id.foodListView);
        foodNutritionProgressBar = findViewById(R.id.progress);
        foodNutritionImage = findViewById(R.id.foodNutritionImage);
        searchButton = findViewById(R.id.searchButton);
        //foodNutritionToolBar = findViewById(R.id.foodNutritionToolBar);

        /**
         * Create a button for searches
         */

        Toast.makeText(this,"searching", Toast.LENGTH_SHORT).show();
//
           searchButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(FoodNutrition_StartActivity.this, FoodNutrition_SearchFood.class);
                   startActivityForResult(intent, SEARCH_REQUEST);
               }
           });
    }
}

