package com.example.minjia.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.minjia.finalproject.Movie.ACTIVITY_NAME;

public class Movie_import extends Activity {
    private TextView titleT,actorT,yearT,ratingT,runtimeT,plotT,urlT,searchT; // TextViews in the layout to show the new movie's information.
    private Button cancelT,downloadT,searchB;//Buttons
    private ProgressBar progressBarT;
    private Bundle bundle;
    public static final int DOWNLOAD_MOVIE_RESULT = 8;
    private String t; // to define the title the user input and want to search.
    private ImageView imageView; // show movie image
    private LinearLayout layout1, layout2, layout3, layout4;// layour1,layout2, layout3,layout4 are used to contain the views which will show the movie information.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_import);

        searchT=(EditText)findViewById(R.id.movie_searchTitle) ;// The EditText where user input the title of the movie
        searchB=(Button)findViewById(R.id.movie_searchButton); // The button of search.
        titleT=(TextView)findViewById(R.id.movie_detail_title_value); // place to show movie title
        actorT=(TextView)findViewById(R.id.movie_detail_actor_value); // place to show movie actors.
        actorT.setMovementMethod(new ScrollingMovementMethod());
        yearT=(TextView)findViewById(R.id.movie_detail_year_value); //place to show movie year
        ratingT=(TextView)findViewById(R.id.movie_detail_rating_value);//place to show movie rate
        runtimeT=(TextView)findViewById(R.id.movie_detail_runtime_value); //place to diaplay movie run time
        plotT=(TextView)findViewById(R.id.movie_detail_plot_value); //place to display movie plot
        plotT.setMovementMethod(new ScrollingMovementMethod());
        urlT=(TextView)findViewById(R.id.movie_detail_url_value); //place to display movie poster
        urlT.setMovementMethod(new ScrollingMovementMethod());
        progressBarT=(ProgressBar)findViewById(R.id.movie_progressBar);// place to display progressBar
        progressBarT.setVisibility(View.VISIBLE);
        cancelT= (Button)findViewById(R.id.movie_import_cancel); // button to cancel
        downloadT=(Button)findViewById(R.id.movie_import_download); // button to download the search result
        imageView=(ImageView)findViewById(R.id.movie_image);  // place to set poster picture.
        layout1 = (LinearLayout)findViewById(R.id.movie_impot_layout1); // layout include image, year, runtime, title and rating.
        layout2 = (LinearLayout)findViewById(R.id.movie_import_layout2); // layout for the information of actor
        layout3 = (LinearLayout)findViewById(R.id.movie_import_layout3); // layout for the information of plot
        layout4 = (LinearLayout)findViewById(R.id.movie_import_layout4); // layout for the information of url.

        /*
        Set click listener of the search button.
         */
        searchB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                t= Uri.encode(searchT.getText().toString());// the title which the user input.
                if(t.equals("")) {
                    Toast toast=Toast.makeText(getApplicationContext(), "Please Enter Title", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();} // if no information input, make a toast let user known.
                else{
                final MovieQuery movieQuery = new MovieQuery(); // new MovieQuery (inner class)
                movieQuery.execute(); // if there is input information, execute movieQuery
                downloadT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bundle = movieQuery.getBundle();//get the bundle which contains the information of new movie
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(DOWNLOAD_MOVIE_RESULT, intent);
                        finish();
                    }
                });}}
            });


        cancelT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    /**
     * Used to search new movie.
     */
    public class MovieQuery extends AsyncTask<String,Integer,String> {
        private String title,plot,actor,year,runtime,rating,url;
        private URL link;
        private  Bitmap movieImage;
        private ByteArrayOutputStream os;

        @Override
        protected String doInBackground(String... strings) {

            //checking network connectivity
            ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()) {
                Log.i(ACTIVITY_NAME, "Device is connecting to the network");
            } else {
                Log.i(ACTIVITY_NAME, "Device is not connecting to the network");
            }

            try{
              link= new URL("http://www.omdbapi.com/?t="+t+"&r=xml&apikey=beb34815");//URL
                HttpURLConnection connection = (HttpURLConnection)link.openConnection();
                connection.setReadTimeout(8000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                connection.connect(); // connect.
                InputStream inputStream = connection.getInputStream();
                XmlPullParser parser = Xml.newPullParser(); // use XML pull parser.
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
                parser.setInput(inputStream,null);

                while(parser.next()!=XmlPullParser.END_DOCUMENT){
                    if(parser.getEventType()!= XmlPullParser.START_TAG){
                        continue;
                    }
                    if(parser.getName().equals("movie")){ // If get name"movie", start to get movie information
                        title=parser.getAttributeValue(null,"title"); // Get movie title
                        year =parser.getAttributeValue(null,"year");// Get movie year
                        publishProgress(25);
                        android.os.SystemClock.sleep(500);
                        rating=parser.getAttributeValue(null,"rated");  // Get movie rate
                        actor=parser.getAttributeValue(null,"actors"); // Get movie actor
                        publishProgress(50);
                        android.os.SystemClock.sleep(500);
                        runtime=parser.getAttributeValue(null,"runtime"); // Get movie runtime
                        plot=parser.getAttributeValue(null,"plot"); // get movie plot
                        url=parser.getAttributeValue(null,"poster"); // get movie url
                        publishProgress(75);
                        android.os.SystemClock.sleep(500);
                    }
                  }
            connection.disconnect();

            URL imageUrl = new URL(url);// Get the link to movie picture.
            HttpURLConnection imageConn = (HttpURLConnection)imageUrl.openConnection();
            imageConn.connect();
            inputStream = imageConn.getInputStream();
            movieImage= BitmapFactory.decodeStream(inputStream); // get the movie picture
            os = new ByteArrayOutputStream();
            movieImage.compress(Bitmap.CompressFormat.PNG,100,os); //compress the picture to ByteArray
            publishProgress(100);
            os.flush();
            os.close();
            imageConn.disconnect();
          }
            catch (XmlPullParserException e) {e.printStackTrace();}
            catch (MalformedURLException e) { e.printStackTrace(); }
            catch (Exception e) {e.printStackTrace(); }
            return null;
            }

            @Override
            protected void onProgressUpdate(Integer...value){
                progressBarT.setVisibility(View.VISIBLE);
                progressBarT.setProgress(value[0]);
            }

            @Override
            protected void onPostExecute(String result){
                progressBarT.setVisibility(View.INVISIBLE);

                // layour1,layout2, layout3,layout4 are used to contain the views which will show the movie information.
                // Invisible before click search button. showing after click search button.
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);

                downloadT.setVisibility(View.VISIBLE);// After showing the search result, set download button visible and allow the user to click

                // Set and show the search result on the layout.
                titleT.setText(title);
                actorT.setText(actor);
                yearT.setText(year);
                runtimeT.setText(runtime);
                ratingT.setText(rating);
                plotT.setText(plot);
                urlT.setText(url);
                imageView.setImageBitmap(movieImage);
            }

        /**
         * Return the Bundle contain the information of the new searched movie
         * @return Bundle with the movie information.
         */
        public Bundle getBundle(){
                bundle = new Bundle();
                bundle.putByteArray(MovieDatabaseHelper.KEY_IMAGE,os.toByteArray());
                bundle.putString(MovieDatabaseHelper.KEY_TITLE,title);
                bundle.putString(MovieDatabaseHelper.KEY_ACTOR,actor);
                bundle.putString(MovieDatabaseHelper.KEY_YEAR,year);
                bundle.putString(MovieDatabaseHelper.KEY_RUNTIME,runtime);
                bundle.putString(MovieDatabaseHelper.KEY_RATING,rating);
                bundle.putString(MovieDatabaseHelper.KEY_PLOT,plot);
                bundle.putString(MovieDatabaseHelper.KEY_URL,url);
                return bundle;
            }
            }
}
