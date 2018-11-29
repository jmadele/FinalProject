package com.example.minjia.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
/*
This class is the main class of movie information.
 */
public class Movie extends AppCompatActivity{
    protected final static String ACTIVITY_NAME = "Movie";
    public static final int ADD_MOVIE_REQUEST = 1;
    private ListView movieListV;
    private Button movieAddButton;
    private Button statisticsButton;
    private Toolbar movieToolBar;
    private  MovieDatabaseHelper movieDatabaseHelper;
    private ArrayList<String> movieList = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private SQLiteDatabase movieDB;
    private Cursor cursor;
    public static final String aboutMovie ="Movie Activity is developed by Yanyu Li";
    RelativeLayout movie_layout;
    ContentValues contentValues;
    Movie_fragment movie_fragment;
    String author ="YanYu Li";
    String version = "Movie_Version_01";
    String instruction = "You are in the movie information activities. Click icons for other functions";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieAdapter = new MovieAdapter(this);
        movieDatabaseHelper = new MovieDatabaseHelper(Movie.this);
        movieDB = movieDatabaseHelper.getWritableDatabase();//open writable database.
        movieListV = (ListView)findViewById(R.id.movie_listView);
        movieListV.setAdapter(movieAdapter);
        movie_layout = (RelativeLayout)findViewById(R.id.movie_layout);

        contentValues = new ContentValues();

        statisticsButton = (Button)findViewById(R.id.movie_statistics);
        movieAddButton = (Button)findViewById(R.id.addMovie);

        movieToolBar = (Toolbar)findViewById(R.id.movie_tollBar);
        setSupportActionBar(movieToolBar); //set Toolbar.
        getSupportActionBar().setTitle("Movie Information");

        showMovie();

        /*
         This button is used to go to the movie_import class. there could add new movie.
         */
        movieAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            Intent intent = new Intent(Movie.this, Movie_import.class);
            startActivityForResult(intent, ADD_MOVIE_REQUEST);}
        });

        statisticsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createRuntimeDialog().show();//show movie statistics information.
            }
        });
/*
    click listView item display detail of the item.
 */
        movieListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = movieDB.rawQuery("SELECT * FROM "+ MovieDatabaseHelper.TABLE_NAME,null );//query the record of table.
                if (cursor != null)
                    cursor.moveToFirst();
                cursor.moveToPosition(position);
                Bundle bundle = new Bundle(); //put the table record information to this bundle.
                bundle.putInt(MovieDatabaseHelper.KEY_ID, cursor.getInt(cursor.getColumnIndex(MovieDatabaseHelper.KEY_ID)));
                if (!cursor.getString(2).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_TITLE, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_TITLE)));
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_TITLE, "");
                }
                if (!cursor.getString(3).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_ACTOR, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_ACTOR)));
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_ACTOR, "");
                }
                if (!cursor.getString(4).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_YEAR, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_YEAR)));
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_YEAR, "");
                }
                if (!cursor.getString(5).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_RUNTIME, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_RUNTIME)));
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_RUNTIME, "");
                }
                if (!cursor.getString(6).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_RATING, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_RATING)));
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_RATING, "");
                }
                if (!cursor.getString(7).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_PLOT, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_PLOT)));
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_PLOT, "");
                }
                if (!cursor.getString(8).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_URL, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_URL)));
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_URL, "");
                }
                if(findViewById(R.id.movie_frameLayout)!=null){
                    bundle.putBoolean("isTablet",true);
                    movie_fragment=new Movie_fragment();
                    movie_fragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.movie_frameLayout,movie_fragment);//if is tablet, replace the frame layoout.
                    ft.commit();
                }else{
                    Intent intent = new Intent(Movie.this, Movie_detail.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,2);// if is phone, go to movie_detail class.
                }
            }
        });
    }
    private Dialog helpDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.movie_help);
        builder.setMessage(author+"   "+version+"\n"+instruction);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User click OK button, do nothing
            }
        });
        return builder.create();
    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.movie_menu, m); //inflate movie menu.
        return true;
    }
    /*
    This function is used to set the items in the menu.
     */
    public boolean onOptionsItemSelected(MenuItem mItem){
        switch(mItem.getItemId()){
            case R.id.movie_about:
                Snackbar.make(movie_layout, aboutMovie, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;
            case R.id.movie_help:
                helpDialog().show();
                break;
            case R.id.movie_main:
               Intent intent = new Intent(Movie.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.movie_bus:
         //       Intent intent1 = new Intent(Movie.this, .class);
          //      startActivity(intent1);
                break;
            case R.id.movie_cbc:
                Intent intent2 = new Intent(Movie.this, CBCNewsActivity.class);
                startActivity(intent2);
                break;
            case R.id.movie_food:
                Intent intent3 = new Intent(Movie.this, FoodNutrition.class);
                startActivity(intent3);
                break;
        }   return true;
    }
/*
This inner class is used to set movie listView's adapter.
 */
 private class MovieAdapter extends ArrayAdapter<String>{
        public MovieAdapter(Context ctx){super(ctx,0);}
        public int getCount(){return movieList.size();}
        public String getItem(int position){return movieList.get(position);}
        public Bitmap getBitmap(int position){return bitmaps.get(position);}
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = Movie.this.getLayoutInflater();
            View movieView = inflater.inflate(R.layout.movie_list,null);
            TextView movie_list = (TextView)movieView.findViewById(R.id.movie_list);
            ImageView movie_image=(ImageView)movieView.findViewById(R.id.movie_list_image);
            movie_list.setText(getItem(position));
            movie_image.setImageBitmap(getBitmap(position));
            return movieView;
        }
        public long getId(int position){return position;}
        public long getItemId(int position){
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(movieDatabaseHelper.KEY_ID));
        }
 }
 /*
 deal the activity result from other class.
  */
 public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Movie_fragment.DELETE_MOVIE_RESULT){
            int key = data.getIntExtra(MovieDatabaseHelper.KEY_ID,-1);
            deleteItem(key); //delete the item
            showMovie();
        }
        else if(resultCode == Movie_import.DOWNLOAD_MOVIE_RESULT){
            Bundle bundle = data.getExtras(); //get the new movie information.
            String titleA= bundle.getString(MovieDatabaseHelper.KEY_TITLE);
            String actorA= bundle.getString(MovieDatabaseHelper.KEY_ACTOR);
            String yearA= bundle.getString(MovieDatabaseHelper.KEY_YEAR);
            String runtimeA= bundle.getString(MovieDatabaseHelper.KEY_RUNTIME);
            String ratingA= bundle.getString(MovieDatabaseHelper.KEY_RATING);
            String plotA= bundle.getString(MovieDatabaseHelper.KEY_PLOT);
            String urlA= bundle.getString(MovieDatabaseHelper.KEY_URL);

            contentValues.put(MovieDatabaseHelper.KEY_IMAGE,bundle.getByteArray(MovieDatabaseHelper.KEY_IMAGE));
            contentValues.put(MovieDatabaseHelper.KEY_TITLE,titleA);
            contentValues.put(MovieDatabaseHelper.KEY_ACTOR,actorA);
            contentValues.put(MovieDatabaseHelper.KEY_YEAR,yearA);
            contentValues.put(MovieDatabaseHelper.KEY_RUNTIME,runtimeA);
            contentValues.put(MovieDatabaseHelper.KEY_RATING,ratingA);
            contentValues.put(MovieDatabaseHelper.KEY_PLOT,plotA);
            contentValues.put(MovieDatabaseHelper.KEY_URL,urlA);

            movieDB.insert(MovieDatabaseHelper.TABLE_NAME,null,contentValues);//put new movie information to the database.
            showMovie();
        }
 }
/*
Show the movie storned information in the listView.
 */
 protected void showMovie(){
        movieList.clear();
        bitmaps.clear();
        SQLiteDatabase movieDBR =  movieDatabaseHelper.getReadableDatabase();
         cursor = movieDBR.query(MovieDatabaseHelper.TABLE_NAME,new String[]{MovieDatabaseHelper.KEY_ID,MovieDatabaseHelper.KEY_IMAGE,MovieDatabaseHelper.KEY_TITLE},
          null,null,null,null,null);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String title = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_TITLE));
                byte[] in = cursor.getBlob(cursor.getColumnIndex(MovieDatabaseHelper.KEY_IMAGE));
                Bitmap imageOut = BitmapFactory.decodeByteArray(in,0,in.length);
                bitmaps.add(imageOut);
                movieList.add(title);
                cursor.moveToNext();
            }
        }  movieAdapter.notifyDataSetChanged();
 }
    public void deleteItem(int id){
        movieDB.delete(MovieDatabaseHelper.TABLE_NAME,MovieDatabaseHelper.KEY_ID + "="+id, null);
        showMovie();
        movieAdapter.notifyDataSetChanged();
    }
  /*
   This method is used to create a custom dialog which display the runtime and year information.
   */
    private Dialog createRuntimeDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_movie_statistics);
        TextView maxValue = (TextView)dialog.findViewById(R.id.movie_longest_value);
        TextView minValue = (TextView)dialog.findViewById(R.id.movie_shortest_value);
        TextView avgValue = (TextView)dialog.findViewById(R.id.movie_avgRuntime_value);
        TextView newYear = (TextView)dialog.findViewById(R.id.movie_new_value);
        TextView oldYear = (TextView)dialog.findViewById(R.id.movie_old_value) ;
        TextView aveY = (TextView)dialog.findViewById(R.id.movie_avgY_value);
        Button button = (Button)dialog.findViewById(R.id.button_statistics);
        SQLiteDatabase movieDB1 = movieDatabaseHelper.getReadableDatabase();

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        double sum = 0;
        int sumY = 0;
        int maxY = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        cursor = movieDB1.query(MovieDatabaseHelper.TABLE_NAME,new String[]{ MovieDatabaseHelper.KEY_ID, MovieDatabaseHelper.KEY_RUNTIME, MovieDatabaseHelper.KEY_YEAR},null,null,null,null,null);
        cursor.moveToFirst() ;
        while (!cursor.isAfterLast()) {
            String runtimeS  = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_RUNTIME));
            String yearS = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_YEAR));
            String[] runtimeSplit = runtimeS.split(" ");
            String[] yearSplit = yearS.split(" ");
            double runtime= Double.parseDouble(runtimeSplit[0]);
            int year = Integer.parseInt(yearSplit[0]);
            max = runtime > max ? runtime:max;
            min = runtime < min ? runtime:min;
            sum += runtime;
            maxY = year > maxY ? year : maxY;
            minY = year < minY ? year : minY;
            sumY += year;
            cursor.moveToNext();
        }
        double avg = sum/cursor.getCount();
        int yearA = sumY/cursor.getCount();

        maxValue.setText(Double.toString(max) + " min");
        minValue.setText(Double.toString(min) + " min");
        avgValue.setText(Double.toString(avg) + " min");

        oldYear.setText(Integer.toString(minY));
        newYear.setText(Integer.toString(maxY));
        aveY.setText(Integer.toString(yearA));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
        });
        return dialog;
    }
}
