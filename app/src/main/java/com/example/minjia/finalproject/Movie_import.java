package com.example.minjia.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    private TextView titleT,actorT,yearT,ratingT,runtimeT,plotT,urlT,searchT;
    private Button cancelT,downloadT,searchB;
    private ProgressBar progressBarT;
    private Bundle bundle;
    public static final int DOWNLOAD_MOVIE_RESULT = 8;
    private String t;
    private ImageView imageView;
    private LinearLayout layout1, layout2, layout3, layout4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_import);

        searchT=(EditText)findViewById(R.id.movie_searchTitle) ;
        searchB=(Button)findViewById(R.id.movie_searchButton);
        titleT=(TextView)findViewById(R.id.movie_detail_title_value);
        actorT=(TextView)findViewById(R.id.movie_detail_actor_value);
        actorT.setMovementMethod(new ScrollingMovementMethod());
        yearT=(TextView)findViewById(R.id.movie_detail_year_value);
        ratingT=(TextView)findViewById(R.id.movie_detail_rating_value);
        runtimeT=(TextView)findViewById(R.id.movie_detail_runtime_value);
        plotT=(TextView)findViewById(R.id.movie_detail_plot_value);
        plotT.setMovementMethod(new ScrollingMovementMethod());
        urlT=(TextView)findViewById(R.id.movie_detail_url_value);
        urlT.setMovementMethod(new ScrollingMovementMethod());
        progressBarT=(ProgressBar)findViewById(R.id.movie_progressBar);
        progressBarT.setVisibility(View.VISIBLE);
        cancelT= (Button)findViewById(R.id.movie_import_cancel);
        downloadT=(Button)findViewById(R.id.movie_import_download);
        imageView=(ImageView)findViewById(R.id.movie_image);
        layout1 = (LinearLayout)findViewById(R.id.movie_impot_layout1);
        layout2 = (LinearLayout)findViewById(R.id.movie_import_layout2);
        layout3 = (LinearLayout)findViewById(R.id.movie_import_layout3);
        layout4 = (LinearLayout)findViewById(R.id.movie_import_layout4);

        searchB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                t=searchT.getText().toString();
                if(t.equals("")) {
                    Toast toast=Toast.makeText(getApplicationContext(), "Please Enter Title", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();}
                else{
                final MovieQuery movieQuery = new MovieQuery();
                movieQuery.execute();
                downloadT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bundle = movieQuery.getBundle();
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
              link= new URL("http://www.omdbapi.com/?t="+t+"&r=xml&apikey=beb34815");
                HttpURLConnection connection = (HttpURLConnection)link.openConnection();
                connection.setReadTimeout(8000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                connection.connect();
                InputStream inputStream = connection.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
                parser.setInput(inputStream,null);

                while(parser.next()!=XmlPullParser.END_DOCUMENT){
                    if(parser.getEventType()!= XmlPullParser.START_TAG){
                        continue;
                    }
                    if(parser.getName().equals("movie")){
                        title=parser.getAttributeValue(null,"title");
                        year =parser.getAttributeValue(null,"year");
                        publishProgress(25);
                        android.os.SystemClock.sleep(500);
                        rating=parser.getAttributeValue(null,"rated");
                        actor=parser.getAttributeValue(null,"actors");
                        publishProgress(50);
                        android.os.SystemClock.sleep(500);
                        runtime=parser.getAttributeValue(null,"runtime");
                        plot=parser.getAttributeValue(null,"plot");
                        url=parser.getAttributeValue(null,"poster");
                        publishProgress(75);
                        android.os.SystemClock.sleep(500);
                    }
                  }
            connection.disconnect();

            URL imageUrl = new URL(url);
            HttpURLConnection imageConn = (HttpURLConnection)imageUrl.openConnection();
            imageConn.connect();
            inputStream = imageConn.getInputStream();
            movieImage= BitmapFactory.decodeStream(inputStream);
            os = new ByteArrayOutputStream();
            movieImage.compress(Bitmap.CompressFormat.PNG,100,os);
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
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                downloadT.setVisibility(View.VISIBLE);
                titleT.setText(title);
                actorT.setText(actor);
                yearT.setText(year);
                publishProgress(30);
                android.os.SystemClock.sleep(400);

                runtimeT.setText(runtime);
                ratingT.setText(rating);
                plotT.setText(plot);
                urlT.setText(url);
                imageView.setImageBitmap(movieImage);
                publishProgress(80);
                android.os.SystemClock.sleep(400);
            }
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
