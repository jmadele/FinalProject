package com.example.minjia.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String ACTIVITY_NAME ="MainActivity";
    Button movie_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    movie_button=(Button) findViewById(R.id.MovieButton);
    movie_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, Movie.class);
            startActivity(intent);
        }
    });




    }

    public void onActivityResult ( int requestCode, int responseCode, Intent data){
        if (requestCode == 50 && responseCode == Activity.RESULT_OK) {
            Log.i(ACTIVITY_NAME,"returned to MainActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");
            Toast toast = Toast.makeText(this, messagePassed, Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public void onStart () {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }
    public void onResume () {
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");
    }
    public void onPause () {
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause()");
    }
    public void onStrop () {
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop()");
    }
    public void onDestroy () {
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }

}
