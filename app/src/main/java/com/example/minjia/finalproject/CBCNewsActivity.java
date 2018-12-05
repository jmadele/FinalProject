package com.example.minjia.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * activity: CBC News RSS Reader
 * author: Min Jia
 * Date: Dec 02, 2018
 */

public class CBCNewsActivity extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "CBC News Reader";
    private ProgressBar progressBar;
    private Button btnSearch;
    protected ListView newsListView;
    private NewsAdapter newsAdapter;
    private TextView titleText;
    private EditText editText;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private ArrayList<CBCNewsData> newsList;
    private String tempTitle, tempLink, tempDescription, tempPubDate, tempAuthor;

    /**
     * onCreate()method is to create the activity
     * instantiate variables
     * populate data into the listView
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcnews);

        //instantiate newsList as an ArrayList
        newsList = new ArrayList<>();
        //instantiate the progress bar
        progressBar = findViewById(R.id.CBC_progressBar);

        titleText = findViewById(R.id.News_title);
        editText = findViewById(R.id.CBC_editText);
        editText.setShowSoftInputOnFocus(false);
        //linkText=findViewById(R.id.News_link);

        //get a reference to the ListView
        newsListView = findViewById(R.id.CBC_listView);
        newsAdapter = new NewsAdapter(CBCNewsActivity.this);
        //to populate the listView with data
        newsListView.setAdapter(newsAdapter);

        //By clicking on a certain title in the listView, you can get the news details
        newsListView.setOnItemClickListener((parent, view, position, id) -> {
            progressBar.setProgress(50);
            Toast.makeText(getBaseContext(), "Going to the details of the news..." , Toast.LENGTH_SHORT).show();

            CBCNewsData newsData = newsAdapter.getItem(position);
            Intent intent = new Intent(CBCNewsActivity.this, CBCNewsContent.class);
            intent.putExtra("title", newsData.getNewsTitle());
            intent.putExtra("link", newsData.getNewsLink());
            intent.putExtra("desc", newsData.getNewsDescription());
            intent.putExtra("pubDate", newsData.getPubDate());
            intent.putExtra("author", newsData.getAuthor());
            startActivity(intent);

        });

        btnSearch = findViewById(R.id.CBC_SearchNews);
        //click search for news titles that you want
        // this will show the news title that you want
        btnSearch.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "searching.... ", Toast.LENGTH_SHORT).show();
            for(int i=0;i<newsList.size();i++) {
                if ((newsList.get(i).getNewsTitle()).contains(editText.getText().toString())) {
                    Snackbar.make(findViewById(R.id.CBC_SearchNews), newsList.get(i).getNewsTitle(), Snackbar.LENGTH_LONG).show();
                    Spannable spannable = new SpannableString(newsList.get(i).getNewsTitle());
                    titleText.setText(spannable);
                }
            }
        });
        Toolbar cbc_toolbar = findViewById(R.id.cbc_toolbar);
        setSupportActionBar(cbc_toolbar);
        //call AsynTask
        new NewsQuery().execute();
    }

    /**
     * onCreateOptionsMenu (menu) is to display the menu and menuitem from layout cbc_toolbarmenu
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cbc_toolbarmenu, menu);
        return true;
    }

    /**
     * this function responds to selected item
     * @param mi menu item
     * @return boolean true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();
        switch (id) {
            //clicking Bus icon to access OCTranspo activity
            case R.id.CBC_BusItem:
                Toast.makeText(getApplicationContext(), "switching to OCTranspo APP", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CBCNewsActivity.this, OCTranspo.class);
                startActivity(intent);
                break;
            //clicking Food icon to access Food activity
            case R.id.CBC_FoodItem:
                Toast.makeText(getApplicationContext(), "switching to Food Nutrition APP", Toast.LENGTH_SHORT).show();
                Intent intentFood = new Intent(CBCNewsActivity.this, FoodNutrition.class);
                startActivity(intentFood);
                break;
            //clicking Movie icon to access Movie activity
            case R.id.CBC_MovieItem:
                Toast.makeText(getApplicationContext(), "switching to Movie APP", Toast.LENGTH_SHORT).show();
                Intent intentMovie = new Intent(CBCNewsActivity.this, Movie.class);
                startActivity(intentMovie);
                break;

            /**
             * when clicking on Instructions, a dialog will show
             */
            case R.id.CBC_InstructionItem: {
                builder = new AlertDialog.Builder(CBCNewsActivity.this);
                builder.setTitle("Instructions");
                builder.setMessage("Click news title for more details, then click the link to view the website. " +
                        "To go to another activity, please click one icon in the toolbar" );
                builder.setNegativeButton("cancel", (dialog, id12) -> dialog.dismiss());
                dialog = builder.create();
                dialog.show();
                break;
            }
            /**
             * when clicking on AboutItem, a message about the app and author will show
             */
            case R.id.CBC_AboutItem:
                builder = new AlertDialog.Builder(CBCNewsActivity.this);
                builder.setTitle("About")
                        .setMessage("CBC News RSS Reader APP V1.0 \n Author: Min Jia")
                        .setNegativeButton("cancel", (dialog, id12) -> dialog.dismiss());
                dialog = builder.create();
                dialog.show();
                break;
        }
        return true;
    }

    /**
     * Adpater class, to pass data to the listView
     */
    public class NewsAdapter extends ArrayAdapter<CBCNewsData> {
        protected NewsAdapter(Context ctx) {
            super(ctx, 0);
        }

        /**
         * @return number of items in the list
         */
        public int getCount() {
            return newsList.size();
        }
        /**
         * @param position: integer
         * @return item at the specific row position
         */
        public CBCNewsData getItem(int position) {
            return newsList.get(position);
        }

        /**
         * @param position
         * @param view
         * @param parent
         * @return news title as a View object at the row position
         */
        public View getView(int position, View view, ViewGroup parent) {
            //for each row, use a LayoutInflator object
            LayoutInflater inflater = CBCNewsActivity.this.getLayoutInflater();
            //resultView is a containter and has news titles
            View resultView = inflater.inflate(R.layout.list_view_cbcnews, null);
            //retrieve the object from the resultView
            titleText = resultView.findViewById(R.id.News_title);
            titleText.setText(newsList.get(position).getNewsTitle());
            return resultView;
        }

        //return the database id of the item at the position
        public long getItemId(int position) {
            return position;
        }
    }

    /**
     * class NewsQuery is used to get data from the news web site
     */
    private class NewsQuery extends AsyncTask<String, String, String> {
        InputStream stream;
        XmlPullParser parser;
        XmlPullParserFactory xmlPullParserFactory;
        private String title, link, description;
        //run in UI before calling background method
              @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            newsAdapter.notifyDataSetChanged();
        }

        /**
         * call readText() and parse the text between "title" tags to String title
         * @param parser
         * @return text between opening tag and closing tag of "title"
         * @throws IOException
         * @throws XmlPullParserException
         * source: https://android.googlesource.com/platform/frameworks/base.git/+/master/samples/training/network-usage/src/com/example/android/networkusage/StackOverflowXmlParser.java
         */
        private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "title");
            String title = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "title");
            return title;
        }

        /**
         * call readText() and parse the text between "link" tags to String link
         * @param parser
         * @return text between opening tag and closing tag of "link"
         * @throws IOException
         * @throws XmlPullParserException
         * source: https://android.googlesource.com/platform/frameworks/base.git/+/master/samples/training/network-usage/src/com/example/android/networkusage/StackOverflowXmlParser.java
         */
        private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "link");
            String link= readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "link");
            return link;
        }

        private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "pubDate");
            String pubDate = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "pubDate");
            return pubDate;
        }
        private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "author");
            String author = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "author");
            return author;
        }
        /**
         * call readText and parse the text between "description" tags to String description
         * @param parser
         * @return text between opening tag and closing tag of "description"
         * @throws IOException
         * @throws XmlPullParserException
         * source: https://android.googlesource.com/platform/frameworks/base.git/+/master/samples/training/network-usage/src/com/example/android/networkusage/StackOverflowXmlParser.java
         */
        private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {

            parser.require(XmlPullParser.START_TAG, null, "description");

            String description = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "description");
            return description;
        }

        /**
         * parse the content between matching tags to a String
         * @param parser
         * @return text between opening and closing tags
         * @throws IOException
         * @throws XmlPullParserException
         * source:https://stackoverflow.com/questions/21081480/best-way-to-parse-this-xml-file
         */
        private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String result = null;
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }

        @Override
        protected String doInBackground(String... args) {
            URL url = null;
            //newsList = new ArrayList<>();
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.i(ACTIVITY_NAME, "Device is connecting to the network");
            } else {
                Log.i(ACTIVITY_NAME, "Device is not connecting to network");
            }
            try {
                //set up the connection and get input stream
                url = new URL("https://www.cbc.ca/cmlink/rss-world");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                Log.d(ACTIVITY_NAME, "Connecting with URL...");
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("debug", "the response is: " + response);
                stream = conn.getInputStream();
                Log.d(ACTIVITY_NAME, "Reading rss. Stream is: " + stream);
                //progressBar.setProgress(50);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                //first create XmlPullParserFatory object, then create XmlPullParser object
                xmlPullParserFactory = XmlPullParserFactory.newInstance();
                xmlPullParserFactory.setNamespaceAware(true);
                parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                //specify the file for XmlPullParser that contains XML
                parser.setInput(stream, null);
                parser.nextTag();
                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    if (name.equals("title")) {
                        tempTitle = readTitle(parser);
                    }
                    if (name.equals("link")) {
                        tempLink = readLink(parser);
                    }
                    if (name.equals("pubDate")) {
                        tempPubDate = readPubDate(parser);
                    }
                    if (name.equals("author")) {
                        tempAuthor = readAuthor(parser);
                    }
                    if (name.equals("description")) {
                        tempDescription = readDescription(parser);
                        //newsList.add(new CBCNewsData(tempTitle, tempLink, tempDescription,));
                        newsList.add(new CBCNewsData(tempTitle, tempLink, tempDescription,tempPubDate,tempAuthor));
                        progressBar.setProgress(75);
                    }

                }
                stream.close();
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return title;
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
            progressBar.setVisibility(View.GONE);
          //  newsAdapter.notifyDataSetChanged();
        }
 }
    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }
}

