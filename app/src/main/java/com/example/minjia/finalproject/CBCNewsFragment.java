package com.example.minjia.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * fragment, showing detailed description of CBC news
 * author: Min Jia
 */
public class CBCNewsFragment extends Fragment {
    View view;
    TextView newsView;
    String newsDescription, title, pubDate, author, savedTitle;
    Bundle bundle;
    boolean isTablet;
    int position;
    long id;
    Button deleteBtn;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        //if bundle is not null, put news article info into the bundle
        if(bundle!=null){
         //   newsLink=bundle.getString("link");
            title = bundle.getString("title");
            newsDescription = bundle.getString("desc");
            pubDate = bundle.getString("pubDate");
            author = bundle.getString("author");

            position = bundle.getInt("position");
            savedTitle = bundle.getString("TITLE");
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

        //convert the news description from HTML to text, removing the images and tabs
//        String txt = String.valueOf(Html.fromHtml(newsDescription));
//        StringBuilder sBuilder= new StringBuilder();
//        sBuilder.append(" Saved Titles: \n" + savedTitle);
        //newsView.setText(sBuilder);
        newsView.setText(savedTitle);

        deleteBtn = view.findViewById(R.id.DeleteButton);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!isTablet) {
                    Intent intent = new Intent(getActivity(), CBCNewsContent.class);
                    intent.putExtra("position", position);
                    intent.putExtra("id", id);
                    getActivity().setResult(-1, intent);
                    getActivity().finish();
                } else {
                    Log.i("tag", "trying to delete a message: " + position);
                    CBCNewsContent cbc = (CBCNewsContent) getActivity();
                    cbc.deleteNews(position);
                    getFragmentManager().beginTransaction().remove(CBCNewsFragment.this).commit();
                }
            }
        });

        Log.i("news description","testing in fragment",null);
        return view;
    }


}
