package com.example.minjia.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Movie extends Activity{
    protected final static String ACTIVITY_NAME = "Movie";
    ListView movieListV;
    Button movieAddButton;
    Button statisticsButton;
    MovieDatabaseHelper movieDatabaseHelper;
    ArrayList<String> movieList = new ArrayList<>();
    MovieAdapter movieAdapter;
    SQLiteDatabase movieDB;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieAdapter = new MovieAdapter(this);
        movieDatabaseHelper = new MovieDatabaseHelper(Movie.this);
        movieDB = movieDatabaseHelper.getWritableDatabase();
        movieListV = (ListView)findViewById(R.id.movie_listView);
        movieListV.setAdapter(movieAdapter);
        statisticsButton = (Button)findViewById(R.id.movie_statistics);

        statisticsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            Intent intent = new Intent(Movie.this,Movie_statistics.class);
            startActivity(intent);
            }
        });
    }

 private class MovieAdapter extends ArrayAdapter<String>{
        public MovieAdapter(Context ctx){super(ctx,0);}
        public int getCount(){return movieList.size();}
        public String getIntem(int position){return movieList.get(position);}
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = Movie.this.getLayoutInflater();
            View movieView = inflater.inflate(R.layout.movie_list,null);
            TextView movie_list = (TextView)movieView.findViewById(R.id.movie_list);
            movie_list.setText(getItem(position));
            return movieView;
        }
        public long getId(int position){return position;}
        public long getItemId(int position){
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(movieDatabaseHelper.KEY_ID));
        }
 }
}
