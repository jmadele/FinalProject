package com.example.minjia.finalproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CBCNewsActivity extends Activity {
    private static final String ACTIVITY_NAME = "CBCNews Reader Activity";
    private ProgressBar progressBar;
    private ImageView CBCNewsImage;
    private TextView newsTitle;
    private EditText editText;
    private Button btn;
    private Button progressBtn;
    private TextView typeText;

    private ListView myList;
    //variable newsList: stores search results
    private ArrayList<String> newsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcnews);
        //instantiate the progress bar and set it as visible
        // set the progressBar as Indeterminate, only for testing
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        progressBar.setBackgroundColor(Color.RED);
        progressBar.setIndeterminate(true);
        progressBar.incrementProgressBy(5);

        //editText is used to store data
        editText = findViewById(R.id.searchEditText);
        newsList = new ArrayList<>();
        newsList.add(0,"movie");
        newsList.add(1,"news");
        newsList.add(2,"sports");
        newsList.add(3,"travel");

        //final NewsAdapter newsAdapter = new NewsAdapter(this);
        //initiate btn and perform click event on Search button
        btn = findViewById(R.id.Search);
        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
              Context context = getApplicationContext();
               Toast.makeText(context,"In searching...message",Toast.LENGTH_SHORT).show();
//              String text = editText.getText().toString();
//              newsList.add(text);
//              newsAdapter.notifyDataSetChanged();
//              editText.setText("");
                                   }

         });
        myList = findViewById(R.id.foodListView);

        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_2, android.R.id.text1,newsList );
        myList.setAdapter(newsAdapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(CBCNewsActivity.this, "item clicked: "+i +" "+ newsList.get(i),Toast.LENGTH_LONG).show();
            }
        });
       // CBCNewsImage = findViewById(R.id.CBCNewsImage);
        new NewsQuery().execute("String parameters");
    }

    private class NewsAdapter extends ArrayAdapter<String> {
        protected NewsAdapter(Context ctx) {
            super(ctx, 0);
        }
        public int getCount() {
            return newsList.size();
        }
        public String getItem(int position){
            return newsList.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = CBCNewsActivity.this.getLayoutInflater();
            View resultView = null;
            resultView = inflater.inflate(R.layout.activity_news_title, null);
            TextView txt = resultView.findViewById(R.id.foodListView);
            txt.setText(getItem(position));
            return resultView;
        }
        public long getItemId(int position){
            return position;
        }

    }
    //inner class
    private class NewsQuery extends AsyncTask <String, Integer, String> {
            private String type;
            //bitmap used to store the picture of current news
            private Bitmap bitmap;
            private String iconName;
            //run in UI before calling background method
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String ...args){
                InputStream stream;
                //check network connection
                URL url = null;
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
                if(networkInfo != null&& networkInfo.isConnected()){
                    Log.i(ACTIVITY_NAME, "Device is connecting to the network");
                }else {
                    Log.i(ACTIVITY_NAME,"Device is not connecting to network");
                }
                try {
                    //set up the connection and get input stream
                    url = new URL("https://www.cbc.ca/cmlink/rss-world");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    Log.d(ACTIVITY_NAME,"Connecting with URL...");

                    conn.connect();
                    stream = conn.getInputStream();
                    Log.d(ACTIVITY_NAME,"Reading rss. Stream is: " + stream);

                    //instantiate the XmlPullParser
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(stream,null);
                    parser.nextTag();

                    while(parser.next()!=XmlPullParser.END_DOCUMENT){
                        //if the current event isn't a start_tag, it throws an exception
                        if(parser.getEventType()!= XmlPullParser.START_TAG){
                            throw new IllegalStateException();
                        }

                        if(parser.getName().equals("channel")) {
                            iconName = parser.getAttributeValue(null, "icon");
                        }
                        if(parser.getName().equals("title")){
                            type = parser.getAttributeValue(null,"story");
                            publishProgress(50);
                            SystemClock.sleep(500);
                        }

                    }
                    conn.disconnect();
                    String imageFile = iconName + ".png";
                    if(fileExistence(imageFile)){
                        FileInputStream fileInput = null;
                        try{
                            fileInput = new FileInputStream(getBaseContext().getFileStreamPath(imageFile));
                            // fileInput = openFileInput(imageFile);
                        }catch(FileNotFoundException e){
                            e.printStackTrace();
                        }
                        bitmap = BitmapFactory.decodeStream(fileInput);
                        Log.i(ACTIVITY_NAME,"image exists, read from file");
                    }
//                    else {
//                        URL imageUrl =  new URL("http://openweathermap.org/img/w/" + iconName + ".png");
//                        conn = (HttpURLConnection) imageUrl.openConnection();
//                        conn.connect();
//                        stream = conn.getInputStream();
//                        bitmap = BitmapFactory.decodeStream(stream);
//                        FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
//                        outputStream.flush();
//                        outputStream.close();
//                    }
                    Log.i(ACTIVITY_NAME,"File Name = " + imageFile );
                    publishProgress(100);

                } catch (MalformedURLException e) {
                    Log.e(ACTIVITY_NAME,e.getMessage());
                } catch (IOException e) {
                    Log.e(ACTIVITY_NAME,e.getLocalizedMessage());
                } catch (XmlPullParserException parseException) {
                    Log.e(ACTIVITY_NAME,parseException.getLocalizedMessage());
                }
            return "string passed to onPostExecute()";
        }
        //set the visibility of progressBar to visible and pass value to the progress
        public void onProgressUpdate(Integer... value) {
            progressBar.setProgress(value[0]);
            progressBar.setVisibility(View.VISIBLE);
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");
        }
        //update the GUI components with the data read from XML
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            typeText.setText("type of the news is" + type);
            Log.i(ACTIVITY_NAME,result);
            progressBar.setVisibility(View.INVISIBLE);

        }
        public boolean fileExistence(String fileName) {
            File file = getBaseContext().getFileStreamPath(fileName);
            return file.exists();
        }
    }

}
