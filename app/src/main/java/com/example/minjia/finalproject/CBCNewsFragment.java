package com.example.minjia.finalproject;

import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * fragment, showing detailed description of CBC news
 * author: Min Jia
 */
public class CBCNewsFragment extends Fragment {
    View view;
    EditText newsView;
    String newsDescription, title, pubDate, author;
    Bundle bundle;
    boolean isTablet;
    int position;
    long id;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        //if bundle is not null, put news article text into the bundle
        if(bundle!=null){
            //newsLink=bundle.getString("link");
            title=bundle.getString("title");
            newsDescription = bundle.getString("desc");
            pubDate=bundle.getString("pubDate");
            author=bundle.getString("author");
            position = bundle.getInt("position");
            id = bundle.getLong("id");
        }
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view of layout CBC_stats, showing the text and number of words inside
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cbcnews, container, false);
        newsView = view.findViewById(R.id.CBC_stats);
        newsView.setShowSoftInputOnFocus(false);
        //convert the news description from HTML to text, removing the images and tabs
        String txt = String.valueOf(Html.fromHtml(newsDescription));
        StringBuilder sBuilder= new StringBuilder();
        //put the news information and number of words,  text of news description into the view
        sBuilder.append("Title: "+ title+"\n")
                .append(pubDate+"\n")
                .append(author+"\n")
                .append("number of words in this article: " + countWords())
                .append(txt);
        newsView.setText(sBuilder);

        Log.i("news description","testing in fragment",null);
        return view;

    }

    /**
     * count the number of words in the news description excluding the images
     * @return int - number of words
     * source:https://stackoverflow.com/questions/225337/how-do-i-split-a-string-with-any-whitespace-chars-as-delimiters
     * \s removes white space and tabs in the text
     */
    public int countWords(){
        String[] count = String.valueOf(Html.fromHtml(newsDescription)).split("\\s+");
        return count.length;
    }
}
