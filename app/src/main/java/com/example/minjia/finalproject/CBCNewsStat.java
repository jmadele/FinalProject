package com.example.minjia.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class CBCNewsStat extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcnews_stats);

        String title = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("desc");
        String pubDate =getIntent().getExtras().getString("pubDate");
        String author =getIntent().getExtras().getString("author");
        int position = getIntent().getExtras().getInt("position");
        long id =getIntent().getExtras().getLong("id");

        Bundle bundle = new Bundle();
        CBCNewsFragment fragment = new CBCNewsFragment();
        fragment.isTablet=false;
        bundle.putString("desc",description);
        bundle.putString("title",title);
        bundle.putString("pubDate",pubDate);
        bundle.putString("author",author);
        bundle.putLong("id",id);
        bundle.putInt("position",position);
        bundle.putBoolean("isTablet",false);

        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}
