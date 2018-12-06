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
public class Movie extends AppCompatActivity {
    protected final static String ACTIVITY_NAME = "Movie";
    public static final int ADD_MOVIE_REQUEST = 1;
    private ListView movieListV; // show saved movie
    private Button movieAddButton; // button to search movie
    private Button statisticsButton; // button to show the movie statistics
    private Toolbar movieToolBar; // Toolbar
    private MovieDatabaseHelper movieDatabaseHelper; // MovieDatabaseHelper
    private ArrayList<String> movieList = new ArrayList<>(); // all saved movie titles
    private ArrayList<Bitmap> bitmaps = new ArrayList<>(); // all saved movie images
    private MovieAdapter movieAdapter;
    private SQLiteDatabase movieDB;
    private Cursor cursor;
    public static final String aboutMovie = "Movie Activity is developed by Yanyu Li";
    RelativeLayout movie_layout; // The layout which the Snackbar show
    ContentValues contentValues; // used to put new movie information to database.
    Movie_fragment movie_fragment;
    String author = "YanYu Li";
    String version = "Movie_Version_01";
    String instruction = "You are in the movie information activities. Click icons for other functions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieAdapter = new MovieAdapter(this);
        movieDatabaseHelper = new MovieDatabaseHelper(Movie.this);
        movieDB = movieDatabaseHelper.getWritableDatabase();//open writable database.
        movieListV = (ListView) findViewById(R.id.movie_listView); // ListView to list saved movie.
        movieListV.setAdapter(movieAdapter);
        movie_layout = (RelativeLayout) findViewById(R.id.movie_layout); // main layout in this layout xml

        contentValues = new ContentValues();

        statisticsButton = (Button) findViewById(R.id.movie_statistics);// Button to show the statistics
        movieAddButton = (Button) findViewById(R.id.addMovie); // Button to go to the search activity.

        movieToolBar = (Toolbar) findViewById(R.id.movie_tollBar); // ToolBar with menu.
        setSupportActionBar(movieToolBar); //set Toolbar.
        getSupportActionBar().setTitle("Movie Information");

        showMovie();

        /*
         This button is used to go to the movie_import class. there could add new movie.
         */
        movieAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Movie.this, Movie_import.class);
                startActivityForResult(intent, ADD_MOVIE_REQUEST);
            }
        });

        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRuntimeDialog().show();//show movie statistics information.
            }
        });
/*
    click listView item display detail of the item.
 */
        movieListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = movieDB.rawQuery("SELECT * FROM " + MovieDatabaseHelper.TABLE_NAME, null);//query the record of table.
                if (cursor != null)
                    cursor.moveToFirst();
                cursor.moveToPosition(position);
                Bundle bundle = new Bundle(); //put the table record information to this bundle.
                bundle.putInt(MovieDatabaseHelper.KEY_ID, cursor.getInt(cursor.getColumnIndex(MovieDatabaseHelper.KEY_ID)));
                if (!cursor.getString(2).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_TITLE, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_TITLE)));// Put title to the bundle
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_TITLE, "");
                }
                if (!cursor.getString(3).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_ACTOR, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_ACTOR)));// Put actors to the bundle.
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_ACTOR, "");
                }
                if (!cursor.getString(4).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_YEAR, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_YEAR)));// Put year to the bundle
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_YEAR, "");
                }
                if (!cursor.getString(5).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_RUNTIME, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_RUNTIME)));// Put runtime to the bundle
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_RUNTIME, "");
                }
                if (!cursor.getString(6).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_RATING, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_RATING)));// Put rating to the bundle
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_RATING, "");
                }
                if (!cursor.getString(7).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_PLOT, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_PLOT)));// Put plot to the bundle.
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_PLOT, "");
                }
                if (!cursor.getString(8).isEmpty()) {
                    bundle.putString(MovieDatabaseHelper.KEY_URL, cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_URL))); // Put url to ethe bundle
                } else {
                    bundle.putString(MovieDatabaseHelper.KEY_URL, "");
                }
                if (findViewById(R.id.movie_frameLayout) != null) {
                    bundle.putBoolean("isTablet", true);
                    movie_fragment = new Movie_fragment();
                    movie_fragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.movie_frameLayout, movie_fragment);//if is tablet, replace the frame layoout.
                    ft.commit();
                } else {
                    Intent intent = new Intent(Movie.this, Movie_detail.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 2);// if is phone, go to movie_detail class.
                }
            }
        });
    }

    /**
     * Create help dialog
     * @return  dialog
     */
    private Dialog helpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.movie_help);
        builder.setMessage(author + "   " + version + "\n" + instruction);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User click OK button, do nothing
            }
        });
        return builder.create();
    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.movie_menu, m); //inflate movie menu.
        return true;
    }

    /*
    This function is used to set the items in the menu.
     */
    public boolean onOptionsItemSelected(MenuItem mItem) {
        switch (mItem.getItemId()) {
            case R.id.movie_about:// If select about, show a Snackbar
                Snackbar.make(movie_layout, aboutMovie, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;
            case R.id.movie_help:
                helpDialog().show();// If select help, show help dialog.
                break;
            case R.id.movie_main:
                Intent intent = new Intent(Movie.this, MainActivity.class);
                startActivity(intent);// If select "go to main", then go back to main.
                break;
            case R.id.movie_bus:
                       Intent intent1 = new Intent(Movie.this, OCTranspo.class);
                      startActivity(intent1);
                break;
            case R.id.movie_cbc: // If click cbc icon, go to CBC activity
                Intent intent2 = new Intent(Movie.this, CBCNewsActivity.class);
                startActivity(intent2);
                break;
            case R.id.movie_food: // If click FOOD icon, go to FoodNutrition activity.
                Intent intent3 = new Intent(Movie.this, FoodNutrition.class);
                startActivity(intent3);
                break;
        }
        return true;
    }

    /*
    This inner class is used to set movie listView's adapter.
     */
    private class MovieAdapter extends ArrayAdapter<String> {
        public MovieAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return movieList.size();
        }//Get total saved movie.

        public String getItem(int position) {
            return movieList.get(position);
        }//Get movie title in the position

        public Bitmap getBitmap(int position) {
            return bitmaps.get(position);
        }//Get movie picture in the positon

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = Movie.this.getLayoutInflater();
            View movieView = inflater.inflate(R.layout.movie_list, null);
            TextView movie_list = (TextView) movieView.findViewById(R.id.movie_list);
            ImageView movie_image = (ImageView) movieView.findViewById(R.id.movie_list_image);
            movie_list.setText(getItem(position)); // set title of the position
            movie_image.setImageBitmap(getBitmap(position));// set picture of the position
            return movieView;
        }

        public long getId(int position) {
            return position;
        }
 /*
 Return movie ID
  */
        public long getItemId(int position) {
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(movieDatabaseHelper.KEY_ID));
        }
    }

    /*
    deal the activity result from other class.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Movie_fragment.DELETE_MOVIE_RESULT) {
            int key = data.getIntExtra(MovieDatabaseHelper.KEY_ID, -1);
            deleteItem(key); //delete the item
            showMovie();
        } else if (resultCode == Movie_import.DOWNLOAD_MOVIE_RESULT) {
            Bundle bundle = data.getExtras(); //get the new movie information.
            String titleA = bundle.getString(MovieDatabaseHelper.KEY_TITLE);//nre movie title
            String actorA = bundle.getString(MovieDatabaseHelper.KEY_ACTOR);// new movie actor
            String yearA = bundle.getString(MovieDatabaseHelper.KEY_YEAR); // new movie year
            String runtimeA = bundle.getString(MovieDatabaseHelper.KEY_RUNTIME);// new movie runtime
            String ratingA = bundle.getString(MovieDatabaseHelper.KEY_RATING); // new movie rating
            String plotA = bundle.getString(MovieDatabaseHelper.KEY_PLOT); // new movie plot
            String urlA = bundle.getString(MovieDatabaseHelper.KEY_URL); // new movie url

            // put now movie information to ContentValues.
            contentValues.put(MovieDatabaseHelper.KEY_IMAGE, bundle.getByteArray(MovieDatabaseHelper.KEY_IMAGE));
            contentValues.put(MovieDatabaseHelper.KEY_TITLE, titleA);
            contentValues.put(MovieDatabaseHelper.KEY_ACTOR, actorA);
            contentValues.put(MovieDatabaseHelper.KEY_YEAR, yearA);
            contentValues.put(MovieDatabaseHelper.KEY_RUNTIME, runtimeA);
            contentValues.put(MovieDatabaseHelper.KEY_RATING, ratingA);
            contentValues.put(MovieDatabaseHelper.KEY_PLOT, plotA);
            contentValues.put(MovieDatabaseHelper.KEY_URL, urlA);

            movieDB.insert(MovieDatabaseHelper.TABLE_NAME, null, contentValues);//put new movie information to the database.
            showMovie();
        }
    }

    /*
    Show the movie storned information in the listView.
     */
    protected void showMovie() {
        movieList.clear();// clear the titles in the list view
        bitmaps.clear(); // clear the picture in the list view.
        SQLiteDatabase movieDBR = movieDatabaseHelper.getReadableDatabase();
        cursor = movieDBR.query(MovieDatabaseHelper.TABLE_NAME, new String[]{MovieDatabaseHelper.KEY_ID, MovieDatabaseHelper.KEY_IMAGE, MovieDatabaseHelper.KEY_TITLE},
                null, null, null, null, null); // Query the table, get saved movies' title and picture.
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String title = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_TITLE));// Get movie title
                byte[] in = cursor.getBlob(cursor.getColumnIndex(MovieDatabaseHelper.KEY_IMAGE)); // Get picture data.
                Bitmap imageOut = BitmapFactory.decodeByteArray(in, 0, in.length); // decode data to a picture.
                bitmaps.add(imageOut); // set picture
                movieList.add(title); // set title
                cursor.moveToNext();
            }
        }
        movieAdapter.notifyDataSetChanged();
    }

    /**
     * Delete the movie in the database.
     * @param id movie Id
     */
    public void deleteItem(int id) {
        movieDB.delete(MovieDatabaseHelper.TABLE_NAME, MovieDatabaseHelper.KEY_ID + "=" + id, null);
        showMovie();
        movieAdapter.notifyDataSetChanged();
    }

    /*
     This method is used to create a custom dialog which display the runtime and year information.
     */
    private Dialog createRuntimeDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_movie_statistics);// Set custom Dialog's layout.
        TextView maxValue = (TextView) dialog.findViewById(R.id.movie_longest_value);// to show longest runtime
        TextView minValue = (TextView) dialog.findViewById(R.id.movie_shortest_value);// to show shortest runtime
        TextView avgValue = (TextView) dialog.findViewById(R.id.movie_avgRuntime_value);//  to show average runtime
        TextView newYear = (TextView) dialog.findViewById(R.id.movie_new_value); // to show newest year
        TextView oldYear = (TextView) dialog.findViewById(R.id.movie_old_value); // to show oldest year
        TextView aveY = (TextView) dialog.findViewById(R.id.movie_avgY_value); // to show average year
        Button button = (Button) dialog.findViewById(R.id.button_statistics); //  button when click showing the movies' statistics information
        SQLiteDatabase movieDB1 = movieDatabaseHelper.getReadableDatabase(); // get readable database.

        double max = Double.MIN_VALUE;// initial the max runtime
        double min = Double.MAX_VALUE; // initial the min runtime
        double sum = 0; // initial the total runtime
        int sumY = 0; // initial the total year
        int maxY = Integer.MIN_VALUE; // initial the newest year
        int minY = Integer.MAX_VALUE;// initial the oldest year

        // Query the movie table, get the runtime and year information
        cursor = movieDB1.query(MovieDatabaseHelper.TABLE_NAME, new String[]{MovieDatabaseHelper.KEY_ID, MovieDatabaseHelper.KEY_RUNTIME, MovieDatabaseHelper.KEY_YEAR}, null, null, null, null, null);
        if (cursor.moveToFirst()!=false){ // If there are some saved movies.
        while (!cursor.isAfterLast()) {
            String runtimeS = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_RUNTIME));// Get the runtime
            String yearS = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_YEAR)); // Get the year
            String[] runtimeSplit = runtimeS.split(" ");
            String year1 = yearS.substring(0, 4);
            double runtime = Double.parseDouble(runtimeSplit[0]);// Get the number of the runtime

            int year = Integer.parseInt(year1);// Get the number of the year
            max = runtime > max ? runtime : max; // max equal the bigger one (max, runtime)
            min = runtime < min ? runtime : min; // min equal the smaller one (min, runtime)
            sum += runtime; // add runtime to total runtime
            maxY = year > maxY ? year : maxY;
            minY = year < minY ? year : minY;
            sumY += year; // add year to total year
            cursor.moveToNext();
        }
        double avg = sum / cursor.getCount(); // Get the average runtime
        int yearA = sumY / cursor.getCount() ; // Get the average year

        // Set runtime information
        maxValue.setText(Double.toString(max) + " min");
        minValue.setText(Double.toString(min) + " min");
        avgValue.setText(Double.toString(avg) + " min");

        //Set year information
        oldYear.setText(Integer.toString(minY));
        newYear.setText(Integer.toString(maxY));
        aveY.setText(Integer.toString(yearA));}

        else{ // if there is no saved movie
           final String message = "No movie";
            maxValue.setText("No movie");
            minValue.setText( "No movie");
            avgValue.setText("No movie");

            oldYear.setText("No movie");
            newYear.setText("No movie");
            aveY.setText("No movie");}

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });// Click cancel button to cancel
        return dialog;
    }
}
