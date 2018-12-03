package com.example.minjia.finalproject;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * fragment, showing detailed description of CBC news
 */
public class CBCNewsFragment extends Fragment {
    View view;
    TextView newsView;
    String newsDescription;
    Bundle bundle;
    boolean isTablet;
    int position;
    long id;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        if(bundle!=null){
            //newsLink=bundle.getString("link");
            newsDescription = bundle.getString("desc");
            position = bundle.getInt("position");
            id = bundle.getLong("id");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cbcnews, container, false);
        newsView = view.findViewById(R.id.CBC_stats);
        newsView.setText(newsDescription);
        Log.i("news description","testing in fragment",null);
        return view;
    }
}
