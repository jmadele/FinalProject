package com.example.minjia.finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/*
This class is used to set the movie detail show.
 */
public class Movie_fragment extends Fragment {
    private TextView title,actor,year,rating,runtime,plot,url;// Views to show selected movie's information
    private Button cancelB,deleteB; // buttons
    private Bundle bundle; // the Bundle contain the movie information
    private View view;// the layout for this fragment.
    public static final int DELETE_MOVIE_RESULT = 6;

    public Movie_fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_movie_fragment, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // The place to show select movie's information
        title=view.findViewById(R.id.movie_detail_title_value);// TextView to show movie title
        actor=view.findViewById(R.id.movie_detail_actor_value); // TextView to show movie actor
        actor.setMovementMethod(new ScrollingMovementMethod());
        year=view.findViewById(R.id.movie_detail_year_value); // TextView to show movie year
        rating=view.findViewById(R.id.movie_detail_rating_value); //TextView to show movie rating
        runtime=view.findViewById(R.id.movie_detail_runtime_value);// TextView to show movie runtime
        plot=view.findViewById(R.id.movie_detail_plot_value);// TextView to show movie plot
        plot.setMovementMethod(new ScrollingMovementMethod());
        url=view.findViewById(R.id.movie_detail_url_value);// TextView to show movie url
        url.setMovementMethod(new ScrollingMovementMethod());

        cancelB=view.findViewById(R.id.movie_return);//Button to go back
        deleteB=view.findViewById(R.id.movie_delete); //  Button to delete the movie from table
        bundle=getArguments(); // get the bundle with select movie's information

        // Show select movie's information on the layout.
        title.setText(bundle.getString(MovieDatabaseHelper.KEY_TITLE));
        actor.setText(bundle.getString(MovieDatabaseHelper.KEY_ACTOR));
        year.setText(bundle.getString(MovieDatabaseHelper.KEY_YEAR));
        rating.setText(bundle.getString(MovieDatabaseHelper.KEY_RATING));
        runtime.setText(bundle.getString(MovieDatabaseHelper.KEY_RUNTIME));
        plot.setText(bundle.getString(MovieDatabaseHelper.KEY_PLOT));
        url.setText(bundle.getString(MovieDatabaseHelper.KEY_URL));

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(bundle.getBoolean("isTablet")){ // if tablet, remove the fragment if cancelled.
                  getFragmentManager().beginTransaction().remove(Movie_fragment.this).commit();
              }else{
                  getActivity().finish();//close this class if cancelled in the phone.
              }
            }
        });

      /*
      Delete the record the this movie.
       */
        deleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bundle.getBoolean("isTablet")){
                    ((Movie)getActivity()).deleteItem(bundle.getInt(MovieDatabaseHelper.KEY_ID));//delete the movie in the tablet
                    getFragmentManager().beginTransaction().remove(Movie_fragment.this).commit();
                    ((Movie)getActivity()).showMovie();
                }
            else{
                Intent intent = new Intent();
                intent.putExtra(MovieDatabaseHelper.KEY_ID,bundle.getInt(MovieDatabaseHelper.KEY_ID));
                getActivity().setResult(DELETE_MOVIE_RESULT,intent ); // set delete result back when in the phone.
                getActivity().finish();
            }}
        });

}}
