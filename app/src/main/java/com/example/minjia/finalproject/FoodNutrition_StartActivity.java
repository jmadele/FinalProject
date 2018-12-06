package com.example.minjia.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
    final int SEARCH_REQUEST = 6;
    private SQLiteDatabase db;



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
        foodListView = findViewById(R.id.food_Favorite_list);
        foodNutritionProgressBar = findViewById(R.id.progress);
        foodNutritionImage = findViewById(R.id.foodNutritionImage);
        searchButton = findViewById(R.id.searchButton);


        /**
         * Create a button for searches
         */
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (searchEditText.getText() != null && searchEditText.getText().length() > 0)

                    return true;
            }


            return false;

        });


        Toast.makeText(this, "searching", Toast.LENGTH_SHORT).show();

        searchButton.setOnClickListener(v-> {
            if (searchEditText.getText() != null && searchEditText.getText().length() > 0) {
                Intent intent = new Intent(FoodNutrition_StartActivity.this, FoodNutrition_SearchFood.class);
                intent.putExtra("food",searchEditText.getText().toString().trim());
                startActivityForResult(intent, SEARCH_REQUEST);
            }
        });


//        if(FoodNutrition_dbHelper.getHelper(this).getAllDate().size()>0){
//            Log.e("数据库判断", "有数据");
//            FragmentManager fm = this.getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.addToBackStack(null);
//            ft.attach(new FoodNutrition_Fragement());
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    /**
     * set event hanlder for tool bar including Home, OCTransport, Nutrition,CBC NEWS
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int menuItemID = menuItem.getItemId();

        switch (menuItemID) {
            case R.id.action_bar_Item1:
                finish();
                break;
            case R.id.action_bar_Item2:
                Intent intent2 = new Intent(this, OCTranspo.class);
                startActivity(intent2);
                break;
            case R.id.action_bar_Item3:

                // Intent intent3 = new Intent(this, MOVIE.class);
                //  startActivity(intent3);
                break;
            case R.id.action_bar_Item4:
                Intent intent4 = new Intent(this, CBCNewsActivity.class);
                startActivity(intent4);
                break;
            case R.id.action_bar_Item5:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(getApplicationContext().getString(R.string.food_about))
                        .setMessage(getApplicationContext().getString(R.string.help))

                        /**
                         * set yes/no button on the alertDialog
                         */
                        .setNegativeButton("OK", (dg, which) -> dg.dismiss())// cancel current operate
                        .create();

                dialog.show();

                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

