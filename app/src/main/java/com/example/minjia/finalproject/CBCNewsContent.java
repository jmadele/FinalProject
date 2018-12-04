package com.example.minjia.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
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

/**
 * CBCNewsContent activity creates details of the news
 */

public class CBCNewsContent extends Activity {
    private static final String ACTIVITY_NAME = "News Content Activity";
    Button btnSave;
    String title, link,description;
    private CBCDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private boolean isTablet;

    String name;
    int position;
    long id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcnews_content);

        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        link = intent.getExtras().getString("link");
        description = intent.getExtras().getString("desc");
        EditText titleView = findViewById(R.id.CBC_NewsContent);
        TextView linkView = findViewById(R.id.CBC_NewsLink);
        WebView descView = findViewById(R.id.CBC_NewsDescription);

        titleView.setText(title);
        linkView.setText(link);

        descView.setVerticalScrollBarEnabled(true);
        descView.setHorizontalScrollBarEnabled(true);
        descView.loadDataWithBaseURL(null, description, "text/html", "utf-8", null);

        //source:https://stackoverflow.com/questions/24261224/android-open-url-onclick-certain-button
        //clicking on the link will open the website of the news
        linkView.setOnClickListener(view->{
            Intent intentLink = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intentLink);
        });
//
        dbHelper = new CBCDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

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
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(CBCDatabaseHelper.KEY_NEWS,title);
                    db.insert(CBCDatabaseHelper.TABLE_NAME,null, contentValues);
            /**
             * if using tablet, the fragment will be called
             * else if using phone, the CBCNewsStat activity will be called
             */
            if (isTablet) {
                        Bundle bundle = new Bundle();
                        bundle.putString("desc", description);
                        bundle.putInt("position",position);
                        bundle.putLong("id",id);
                        bundle.putBoolean("isTablet", true);

                       // bundle.putString("link", link);
                        CBCNewsFragment fragment = new CBCNewsFragment();
                        fragment.setArguments(bundle);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.cbc_framelayout, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }else {
                            Intent intent2 = new Intent(CBCNewsContent.this, CBCNewsStat.class);
                            intent2.putExtra("desc",description);
                            intent2.putExtra("position",position);
                            intent2.putExtra("id",id);
                        //    intent2.putExtra("link",link);
                            startActivityForResult(intent2, 10);
                            }
                }
        );

    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
