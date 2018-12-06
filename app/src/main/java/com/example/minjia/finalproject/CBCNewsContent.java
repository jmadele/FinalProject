package com.example.minjia.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * CBCNewsContent activity shows details of the news
 * author: Min Jia
 */

public class CBCNewsContent extends Activity {
    private static final String ACTIVITY_NAME = "News Content Activity";
    Button btnSave;
    String title, link,description, pubDate, author;
    private CBCDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private boolean isTablet;
    Cursor cursor;
    ArrayList <String> dbList;
    int position;
    String dbColTitle;
    long id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcnews_content);

        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        pubDate = intent.getExtras().getString("pubDate");
        author = intent.getExtras().getString("author");
        link = intent.getExtras().getString("link");
        description = intent.getExtras().getString("desc");

        EditText titleView = findViewById(R.id.CBC_NewsContent);
        TextView linkView = findViewById(R.id.CBC_NewsLink);
        WebView descView = findViewById(R.id.CBC_NewsDescription);

        titleView.setText(title);
        linkView.setText(link);
        //source: https://stackoverflow.com/questions/20910405/android-webview-isnt-scrollable
        //source: https://stackoverflow.com/questions/14374553/android-webview-loaddatawithbaseurl-how-load-images-from-assets
        //load the webview with news description and make it scrollable
        descView.setVerticalScrollBarEnabled(true);
        descView.setHorizontalScrollBarEnabled(true);
        descView.loadDataWithBaseURL(null, description, "text/html", "utf-8", null);

        //source:https://stackoverflow.com/questions/24261224/android-open-url-onclick-certain-button
        //clicking on the link will open the website of the news
        linkView.setOnClickListener(view->{
            Intent intentLink = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intentLink);
        });
        //create an ArrayList dbList to hold data going to database
        dbList = new ArrayList<String>();
        dbHelper = new CBCDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        cursor =  db.rawQuery( "select * from " + CBCDatabaseHelper.TABLE_NAME, null );

        if(cursor.getCount()>0){
            cursor.moveToFirst();
        }
        while(!cursor.isAfterLast()){
            dbColTitle = cursor.getString(cursor.getColumnIndexOrThrow(CBCDatabaseHelper.COLUMN_NEWS));
           // long id = cursor.getLong(cursor.getColumnIndex(CBCDatabaseHelper.KEY_ID));
            Log.i(ACTIVITY_NAME, "SQL ID:" + id);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + dbColTitle);
            //dbList.add(text);

            System.out.println("database testing: "+dbList);
            cursor.moveToNext();
        }
        if(cursor.getCount()>0){
            cursor.moveToFirst();

        }
        for(int i=0;i<cursor.getColumnCount();i++){
            cursor.getColumnCount();
        }

        FrameLayout frameLayout = findViewById(R.id.cbc_framelayout);
        if(frameLayout == null){
            Log.i(ACTIVITY_NAME, "framelayout not loaded:" +frameLayout);
            Toast.makeText(this,"framelayout not loaded",Toast.LENGTH_SHORT).show();
            isTablet = false;
        } else{
            Toast.makeText(this,"framelayout loaded",Toast.LENGTH_SHORT).show();
            isTablet = true;
        }
        btnSave = findViewById(R.id.CBC_saveButton);
        //click the button and go to the fragment showing the news content
        btnSave.setOnClickListener((View view) -> {
            //insert the current news into database
            ContentValues contentValues = new ContentValues();
            contentValues.put(CBCDatabaseHelper.COLUMN_NEWS,title);
            db.insert(CBCDatabaseHelper.TABLE_NAME,null, contentValues);

          //  dbList.add(title);
            //convert the dbList from ArrayList to a StringBuilder
//            StringBuilder sb = new StringBuilder();
//            for (String s : dbList) {
//                sb.append(s);
//                sb.append("\n");
//            }
            /**
             * if using tablet, switch to the fragment
             * else if using phone, call the CBCNewsStat activity
             * source: https://stackoverflow.com/questions/14460109/android-fragmenttransaction-addtobackstack-confusion
             */
            if (isTablet) {
                        Bundle bundle = new Bundle();
                        bundle.putString("title",title);
                        bundle.putString("pubDate",pubDate);
                        bundle.putString("author",author);
                        bundle.putString("desc", description);
                        //bundle.putString("link", link);
                        bundle.putString("TITLE", dbColTitle);

                        bundle.putInt("position",position);
                        bundle.putLong("id",id);
                        bundle.putBoolean("isTablet", true);

                        CBCNewsFragment fragment = new CBCNewsFragment();
                        fragment.setArguments(bundle);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.cbc_framelayout, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }else {
                            Intent intent2 = new Intent(CBCNewsContent.this, CBCNewsStat.class);
//                            intent2.putExtra("desc",description);
//                            intent2.putExtra("pubDate",pubDate);
//                            intent2.putExtra("author",author);
//                            intent2.putExtra("title",title);
                            intent2.putExtra("position",position);
                            intent2.putExtra("id",id);
                           // intent2.putExtra("TITLE", (CharSequence) sb);
                            intent2.putExtra("TITLE",dbColTitle);
                            startActivity(intent2);
                            }
                }
        );
    }


    public void deleteNews(int position){
        try {
            dbList.remove(position);
           //messageAdapter.notifyDataSetChanged();
        }catch(SQLException e){

        }
    }
    public void onActivityResult(int requestCode, int responseCode, Intent data){
        if(requestCode == 10  && responseCode == -1) {
            Bundle bundle = data.getExtras();
            int position = bundle.getInt("position");
            long id = bundle.getLong("id");
            deleteNews(position);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
