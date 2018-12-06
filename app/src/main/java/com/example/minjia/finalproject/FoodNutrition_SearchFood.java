
package com.example.minjia.finalproject;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;

import static android.content.ContentValues.TAG;

public class FoodNutrition_SearchFood extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView foodImage;
    SQLiteOpenHelper dBHelper;
    TextView foodName,foodCalories,foodProcnt,foodFat, foodCHO, foodFabri;
    Button saveBtn, backBtn, toFavorite;


    protected static final String ACTIVITY_NAME = "Food Nutrition Search";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dBHelper = FoodNutrition_dbHelper.getHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail_page);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // foodImage = (ImageView) findViewById(R.id.foodImage);
        foodName = (TextView) findViewById(R.id.foodName_Value1);
        foodCalories=(TextView)findViewById(R.id.enerc_value1);
        foodProcnt = (TextView)findViewById(R.id.PROCNT_Value1);
        foodFat = findViewById(R.id.FAT_Value1);
        foodCHO = findViewById(R.id.CHOCDF_Value1);
        foodFabri = findViewById(R.id.fabric_Value1);




        String foodName = getIntent().getStringExtra("food");
        Log.e(TAG, "GET FOOD KEY WORD: "+ foodName +"  READY TO SEARCH");
        final FoodQuery foodQuery = new FoodQuery();
        foodQuery.execute(foodName);

        saveBtn =findViewById(R.id.food_save_button1);
        saveBtn.setOnClickListener(e->foodQuery.saveToDB());

        backBtn = findViewById(R.id.food_back_button1);
        backBtn.setOnClickListener(e->finish());

        toFavorite = findViewById(R.id.food_favorite_button1);
        toFavorite.setOnClickListener(e->{
            FragmentManager  fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.food_detail, new FoodNutrition_Fragement() );
            ft.commit();
        });

    }


    public class FoodQuery extends AsyncTask<String, Integer, String> {
        String name;
        String label;
        String ENRC_KCAL;
        String PROCNT;
        String FAT;
        String CHOCDF;
        String FIBTG;

        ByteArrayOutputStream os;

        /**
         * connect to the url link, and grab the values from the web page using api app_id and app_key
         *
         * @param args
         * @return
         */
        @Override
        protected String doInBackground(String... args) {
//            InputStream stream;
            name =args[0];
            FoodNutrition_HttpHandler sh = new FoodNutrition_HttpHandler();
            // Making a request to url and getting response
            publishProgress(10);
            String url = "https://api.edamam.com/api/food-database/parser?&app_id=7a31a1cc&app_key=287e7c1c77ff233e0d46d08eab7a6e98&ingr=";
            String jsonStr = sh.makeServiceCall(url+name.toLowerCase());
            Log.e(TAG, "URL CONCAT DONE: " +url+args[0]);
            Log.e(TAG, "Response from url: " + jsonStr);
            publishProgress(20);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray pared = jsonObj.getJSONArray("parsed");
                    publishProgress(30);
                    Log.e(TAG, "GET JSONArray pared:" + pared.toString());
                    JSONObject food1 = pared.getJSONObject(0);
                    JSONObject food = food1.getJSONObject("food");
                    publishProgress(40);
                    Log.e(TAG, "GET JSONObject food:" + food.toString());
                    label = food.getString("label");
                    publishProgress(50);
                    Log.e(TAG, "GET JSONObject label:" + label);
                    JSONObject nutrition = food.getJSONObject("nutrients");
                    Log.e(TAG, "GET JSONObject nutrients:" + nutrition.toString());
                    publishProgress(60);
                    ENRC_KCAL = nutrition.getString("ENERC_KCAL");
                    Log.e(TAG, "GET STRING ENRC_KCAL:" + ENRC_KCAL);
                    publishProgress(70);
                    PROCNT = nutrition.getString("PROCNT");
                    Log.e(TAG, "GET STRING PROCNT:" + PROCNT);
                    publishProgress(80);
                    FAT = nutrition.getString("FAT");
                    Log.e(TAG, "GET STRING FAT:" + FAT);
                    CHOCDF = nutrition.getString("CHOCDF");
                    Log.e(TAG, "GET STRING CHOCDF:" + CHOCDF);
                    publishProgress(90);
                    FIBTG = nutrition.getString("FIBTG");
                    Log.e(TAG, "GET STRING FIBTG:" + FIBTG);
                    publishProgress(100);
                    // Getting JSON Array node
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            }


            return null;
        }

        /**
         * animate a progress bar in the interface while the background computation is still executing
         *
         * @param value
         */
        @Override
        protected void onProgressUpdate(Integer... value) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");
        }

        /**
         * after the progress is done, make progress bar disappear, and pass the values to result
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
            foodName.setText(label);
            foodCalories.setText(ENRC_KCAL == null? "N/A" : ENRC_KCAL);
            foodProcnt.setText(PROCNT == null? "N/A" : PROCNT);
            foodFat.setText(FAT == null? "N/A" : FAT);
            foodCHO.setText(CHOCDF == null? "N/A" : CHOCDF);
            foodFabri.setText(FIBTG == null? "N/A" : FIBTG);
        }

        /**
         * bundle the values together to be used later
         *
         * @return
         */
        public void saveToDB() {
            SQLiteDatabase database = dBHelper.getWritableDatabase();
            ContentValues cValues = new ContentValues();
            cValues.put(FoodNutrition_dbHelper.KEY_NAME,name);
            cValues.put(FoodNutrition_dbHelper.KEY_LABEL,label);
            cValues.put(FoodNutrition_dbHelper.KEY_CALORIES,ENRC_KCAL);
            cValues.put(FoodNutrition_dbHelper.KEY_PROCNT,PROCNT);
            cValues.put(FoodNutrition_dbHelper.KEY_FAT,FAT);

            cValues.put(FoodNutrition_dbHelper.KEY_CHOCDF,CHOCDF);
            cValues.put(FoodNutrition_dbHelper.KEY_FIBTG,FIBTG);
            database.insert(FoodNutrition_dbHelper.TABLE_NAME,"NullColumnName",cValues);

        }
    }
}
