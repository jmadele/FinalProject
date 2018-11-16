package com.example.minjia.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FoodNutrition extends Activity {

    private static final String ACTIVITY_NAME = "Food Nutrition Activity";
    private ProgressBar FoodNutritionProgressBar;
    private ImageView FoodNutritionImage;
    private EditText searchEditText;
    private ListView foodListView;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);

        FoodNutritionProgressBar = findViewById(R.id.foodNutritionProgressBar);
        FoodNutritionImage = findViewById(R.id.FoodNutritionImage);
        searchEditText = findViewById(R.id.searchEditText);
        foodListView = findViewById(R.id.foodListView);
        btn = findViewById(R.id.searchButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                Toast.makeText(context, "In searching food nutrition...message", Toast.LENGTH_SHORT).show();
            }

    });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A dialog")
                .setPositiveButton("Hello", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You clicked on ...", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();


    }
}