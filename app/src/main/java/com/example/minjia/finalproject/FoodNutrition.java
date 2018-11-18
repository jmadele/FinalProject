package com.example.minjia.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class FoodNutrition extends Activity {

    private static final String ACTIVITY_NAME = "Food Nutrition Activity";
    private ProgressBar FoodNutritionProgressBar;
    private ImageView FoodNutritionImage;
    private EditText searchEditText;
    private ListView foodListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);

        FoodNutritionProgressBar = findViewById(R.id.foodNutritionProgressBar);
        FoodNutritionImage = findViewById(R.id.FoodNutritionImage);
        searchEditText = findViewById(R.id.searchEditText);
        foodListView = findViewById(R.id.foodListView);

    }
}
