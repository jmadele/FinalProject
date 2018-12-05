package com.example.minjia.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
/*
This class is used to display the movie detail when it is the phone.
 */
public class Movie_detail extends Activity {
    Bundle bundle; // Bundle with movie information
    Movie_fragment movieFragment;//frame layout in the activity_movie_detail layout
    FragmentTransaction ft;//FragmentTransaction
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        bundle = getIntent().getExtras();// get the information of the movie.
        movieFragment = new Movie_fragment();
        movieFragment.setArguments(bundle);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.phone_frameLayout, movieFragment);//replace the phone frame layout.
        ft.commit();

    }
}
