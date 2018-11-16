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
        //Log.i(ACTIVITY_NAME, "In onCreate()");

        foodNutritionProgressBar = findViewById(R.id.foodNutritionProgressBar);
        foodNutritionImage = findViewById(R.id.foodNutritionImage);
        searchEditText = findViewById(R.id.searchEditText);
        foodListView = findViewById(R.id.foodListView);
        foodNutritionProgressBar = findViewById(R.id.progress);
        foodNutritionImage = findViewById(R.id.foodNutritionImage);
        searchButton = findViewById(R.id.searchButton);

        /**
         * Create a button for searches
         */

        Toast.makeText(this,"searching", Toast.LENGTH_SHORT).show();

           searchButton.setOnClickListener(e ->
             Snackbar.make(e, "submitting", Snackbar.LENGTH_LONG).show() );

        //searchButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //       Context context = getApplicationContext();
        //        Toast.makeText(context, "In searching", Toast.LENGTH_SHORT).show();
        //    }
        //});

    }
}
            //public void onClick(View v) {
                //Intent intent = new Intent(FoodNutrition_StartActivity.this,
                //       FoodNutrition_ListItemsActivity.class);
              //  startActivityForResult(intent, 50);
            //}

    //@Override
    //public void onActivityResult(int requestCode,int responseCode, Intent data){
        //Lab3.11
        //if (requestCode == 50) {
        //    Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
       // }
        //if (responseCode == Activity.RESULT_OK) {
          //  String messagePassed = data.getStringExtra("Response");
         //   CharSequence text = "ListItemActivity passed: My information to share";
        //    Toast toast = Toast.makeText(this, text+messagePassed,
       //             Toast.LENGTH_LONG);
      //      toast.show();
     //   }
    //}
