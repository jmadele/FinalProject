package com.example.minjia.finalproject;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FoodNutrition extends Activity {

    private static final String ACTIVITY_NAME = "Food Nutrition Activity";
    private ProgressBar FoodNutritionProgressBar;
    private ImageView FoodNutritionImage;
    private ListView FoodNutritionView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);

        FoodNutritionProgressBar = (ProgressBar) findViewById(R.id.FoodNutritionProgressBar);
        FoodNutritionImage=(ImageView)findViewById(R.id.FoodNutritionImage);
        FoodNutritionView=(ListView)findViewById(R.id.FoodNutritionView);

    }
}
