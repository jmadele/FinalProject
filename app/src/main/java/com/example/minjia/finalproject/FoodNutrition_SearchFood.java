
package com.example.minjia.finalproject;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.net.URL;


import static android.content.ContentValues.TAG;

public class FoodNutrition_SearchFood extends Activity {

    ProgressBar progressBar;
    ImageView foodImage;
    TextView foodID;
    TextView foodName;
    TextView foodCalories;
    protected static final String ACTIVITY_NAME = "Food Nutrition Search";
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition__search_food);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        foodImage = (ImageView) findViewById(R.id.foodImage);
        foodID = (TextView) findViewById(R.id.foodIdText);
        foodName = (TextView) findViewById(R.id.foodName);
        //foodCalories=(TextView)findViewById(R.id.foodCaloriesText);
        final FoodQuery foodQuery = new FoodQuery();
        foodQuery.execute();
    }


        public class FoodQuery extends AsyncTask<String, Integer, String> {
        String food_Id;
        String food_Label;
        String food_Calories;
        String food_uri;
        Bitmap bitmap;

        @Override
        protected String doInBackground(String ...args) {
//            InputStream stream;
//
            FoodNutrition_HttpHandler sh = new FoodNutrition_HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.edamam.com/api/food-database/parser?ingr=%22%20+%20food%20+%20%22&app_id=7a31a1cc&app_key=287e7c1c77ff233e0d46d08eab7a6e98";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray hints = jsonObj.getJSONArray("hints");
                    JSONObject food1 = hints.getJSONObject(0);

                    JSONObject food = food1.getJSONObject("food");
                     food_Id = food.getString("foodId");
                     food_Label=food.getString("label");
                     food_uri = food.getString("uri");
                     JSONObject food2 = food.getJSONObject("nutrients");
                     food_Calories=food2.getString("ENERC_KCAL");


                    // Getting JSON Array node
                }catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());}}
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer ... value){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
           foodID.setText(food_Id);
           foodName.setText(food_Label);
           foodCalories.setText(food_Calories);
        }

            public Bundle getBundle(){
                bundle = new Bundle();
               // bundle.putByteArray(FoodNutrition_dbHelper.KEY_IMAGE,os.toByteArray());
                bundle.putString(FoodNutrition_dbHelper.KEY_ID,food_Id);
                bundle.putString(FoodNutrition_dbHelper.KEY_NAME,food_Label);
                bundle.putString(FoodNutrition_dbHelper.KEY_CALORIES,food_Calories);
                //bundle.putString(FoodNutrition_dbHelper.KEY_URL,URL);
                return bundle;
            }
    }
}
