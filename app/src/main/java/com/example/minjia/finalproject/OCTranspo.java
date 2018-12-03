package com.example.minjia.finalproject;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Movie;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minjia.finalproject.R;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Main Activity for the OCTranspo bus route information App.
 * User can enter a bus station number (4 digits) which is saved to the database and displayed on
 * screen in a list.
 * User can then select a route from the list and get the routes available from that station/stop.
 * @author xinji Zhu(040446578) on 2018-11-12
 */


public class OCTranspo extends AppCompatActivity {

    // create a ListView of stationsList
    private ListView stationsList;

    // create a ProgressBar of progressBsr
    private ProgressBar progressBar;

    // create a RelativeLayout mainLayout
    private RelativeLayout mainLayout;

    // create an Indten of intent to start activity
    private Intent intent;

    // create a Cussor of cursonr to represent a set of results from an SQL query
    private Cursor cursor;

    // create a FrameLayout of routesFrame
    private FrameLayout routesFrame;

    // create an EditText of stationInput for user to input the station
    private EditText stationInput;

    // for launching bus station query
    private FloatingActionButton submitButton;

    // create a helper to connenct to database
    private final OCTranspoDAO helper = new OCTranspoDAO(this);

    // create an ArrayList of routes to display the routes info
    private ArrayList<String> routes = new ArrayList<>();

    // create a SQL query
    public SQLiteDatabase db;

    // create a StationAdapter of stationsAdapter
    private StationsAdapter stationsAdapter;

    private static String version;
    private static final String versionNum = " 1.0";
    private static String author;

    public int numOfStations;

    // create three Dialog of stationDialog, helpDialog, statisticsDialog
    private Dialog stationDialog;
    private Dialog helpDialog;
    private Dialog statisticsDialog;

    //for dismissing the route list currently being viewed
    private FloatingActionButton dismissButton;

    //current bus station selected by user
    private String currentStation = "";

    private boolean routeListIsOpen;

    final RoutesFragment fragment = new RoutesFragment();

    private View oldView;

    private RouteInfoParser routeInfoParser;

    private TextView stationStats;


    /**
     * Called on activity start. Provides content view, loads resources and sets button functions.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo);

        mainLayout = findViewById(R.id.transpoMainLayout);

        // to retrieve and display info of version number and author's name
        version = getResources().getString(R.string.version) + versionNum;
        author = getResources().getString(R.string.myName);

        // a toolBar to display the about/help/statistics for each OCTronspo, Movie, etc.
        Toolbar myToolBar = findViewById(R.id.transpo_toolbar);
        setSupportActionBar(myToolBar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }catch(NullPointerException except){
            Log.d("Null Pointer exception", "Show title bar throwing null");
        }

        // open and write data to database
        db = helper.getWritableDatabase();

        stationsAdapter = new StationsAdapter(this);

        // object of stationList and set the Adapter
        stationsList = findViewById(R.id.stationsList);
        stationsList.setAdapter(stationsAdapter);

        // oject of submitButton
        submitButton = findViewById(R.id.submitButton);

        // object of stationInput
        stationInput = findViewById(R.id.stationInput);

        // object of routesFrame
        routesFrame = findViewById(R.id.routesFrame);

        // object of progressBar
        progressBar = findViewById(R.id.progress);

        // method of addSubmitButton
        addSubmitButton();

        numOfStations = addStationsToList();

        // method of addStatioinList
        addStationList();

        stationDialog = createStationDialog();

        // object of dismissButton
        dismissButton = findViewById(R.id.dismissButton);

        helpDialog = createHelpDialog();
        statisticsDialog = createStatisticsDialog(numOfStations);

        // create alert dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("A dialog")
//                .setPositiveButton("Hello", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "You clicked on ...", Toast.LENGTH_SHORT).show();
//                    }
//                }).create().show();
//
//        Toast.makeText(this,"searching", Toast.LENGTH_SHORT).show();
//
//        submitButton.setOnClickListener(e ->{
//            Snackbar.make(e, "submitting", Snackbar.LENGTH_LONG).show();
//        });

    }

    /**
     * Build dialog for displaying options for station number selection; view, delete, cancel.
     */
    private Dialog createStationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.custom_station_dialog, null));

        return builder.create();
    }

    /**
     * Pass the currently selected station number to the {@link RoutesFragment} and adds the
     * fragment of routes to the screen.
     * @param fullRouteList
     */
    public void setRouteData(LinkedList<String> fullRouteList){

        progressBar.setVisibility(View.INVISIBLE);

        Bundle b = new Bundle();
        b.putString("stationNum", currentStation);

        fragment.setArguments(b);

        //display routes fragment
        getFragmentManager().beginTransaction()
                .add(routesFrame.getId(), fragment)
                .commit();


        //load bus routes into fragment list
        for (String item : fullRouteList) {
            fragment.addRoutes(item);
        }
    }

    /**
     * Update the progress bar, updated from the RouteInfoParser doInBackground, during and after
     * XML parsing.
     * @param value Value set to the progress bar out of 100.
     */
    public void updateProgressBar(int value){
        progressBar.setProgress(value);
    }

    /**
     * Add the button click functions for the listView of stations. When an item is clicked the
     * station dialog is displayed. Here the functionality of the buttons in the station dialog is
     * also set. Including the "view" button which initiates the RouteInfoParser to dynamically get
     * the route info from the OCTranspo website.
     */
    private void addStationList(){

        final OCTranspo creator = this;

        stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                if (!routeListIsOpen) {

                    final int index = position;

                    oldView = view;


                    String station = stationsAdapter.getItem(position);
                    Log.i("STATION", station);
                    currentStation = station.substring(2, station.length());


                    stationDialog.show();
                    routeListIsOpen = true;

                    stationsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    view.setBackgroundColor(Color.LTGRAY);       
                    TextView stationNumber = stationDialog.findViewById(R.id.currentStation);
                    String stationInfo = getResources().getString(R.string.station);
                    stationNumber.setText(stationInfo + station);

                    Button cancelButton = stationDialog.findViewById(R.id.cancelStationDialogButton);
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            stationDialog.dismiss();
                            oldView.setBackgroundColor(Color.WHITE);
                        }
                    });

                    Button viewRoutesButton = stationDialog.findViewById(R.id.viewRoutesButton);
                    viewRoutesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            progressBar.setProgress(0);
                            progressBar.setVisibility(View.VISIBLE);
                            routeInfoParser = new RouteInfoParser(creator, 0);
                            routeInfoParser.setBusStationNum(currentStation);
//                            Log.i("MSG","it is here now" );
                            routeInfoParser.execute();
//                            FragmentManager fm = getSupportFragmentManager();
//                            FragmentTransaction ft = fm.beginTransaction();
//                            Log.i("MSG","it is now here" );
                            //ft.attach();
                            dismissButton.show();
                            stationDialog.dismiss();

                        }
                    });

                    Button deleteStationButton = stationDialog.findViewById(R.id.deleteStationButton);
                    deleteStationButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            removeStation(index);
                        }
                    });

                    dismissButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view2) {
                            //remove the routes fragment from screen
                            dismissRoutesList(fragment);
                            view.setBackgroundColor(Color.WHITE);
                        }
                    });

                }else{
                    dismissRoutesList(fragment);
                    stationsList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                    oldView.setBackgroundColor(Color.WHITE);
                }
            }
        });
    }

    /**
     * Remove the list of routes(Fragment : RouteFragment) from the screen.
     * @param fragment Fragment containing the list of bus routes
     */
    private void dismissRoutesList(Fragment fragment){
        getFragmentManager().beginTransaction().remove(fragment).commit();
        dismissButton.hide();
        routeListIsOpen = false;
    }

    /**
     * Delete a station number from the database. Also removes the station number from the list on-screen
     * dynamically by notifying the stationsAdapter(ArrayAdapter) of change.
     * @param index
     */
    private void removeStation(int index){
        //remove message from Database
        db.delete(OCTranspoDAO.TABLE_NAME, OCTranspoDAO.KEY_STATION+"="+currentStation, null);

        routes.remove(index);
        stationsAdapter.notifyDataSetChanged();
        stationDialog.dismiss();

        numOfStations--;
        stationStats.setText(String.valueOf(numOfStations));

        CharSequence text = "STATION #"+currentStation+" deleted ";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();

    }

    /**
     * Retrieve the stations currently in database and add them to the station listview. This is done on application
     * startup (onCreate).
     */
    private int addStationsToList(){

        cursor = db.query(false, OCTranspoDAO.TABLE_NAME, new String[]{OCTranspoDAO.KEY_STATION}, null, null, null, null, null, null);

        int colIndex = cursor.getColumnIndex(OCTranspoDAO.KEY_STATION);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()){

            routes.add("# "+cursor.getString(colIndex));
            numOfStations++;
            cursor.moveToNext();

        }
        cursor.close();
        return numOfStations;
    }

    /**
     * Station entered is saved to database and added to the listView when user clicks the submit button.
     * Input validation is done to ensure station is not already in the database/list, and to ensure a
     * four digit number is entered.
     */
    private void addSubmitButton(){

        final ContentValues cValues = new ContentValues();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean numInList = false;
                //check if number is in the station list already
                for(String route : routes){
                    if (stationInput.getText().toString().equals(route.substring(2, route.length()))){
                        Log.i("MSG","it is here now" );

                        Log.i("STATION1", stationInput.getText().toString());
                        Log.i("COMPARE TO: ", route.substring(2,route.length()));
                        CharSequence text = "Station number already in list ";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
//                        submitButton.setOnClickListener(e ->{
//                            Snackbar.make(e, "Station number already in list ", Snackbar.LENGTH_LONG).show();
//                        });

                        numInList = true;
                    }
                }

                if (validNumber(stationInput.getText().toString()) && !numInList) {

                    routes.add("# " + stationInput.getText().toString());
                    stationsAdapter.notifyDataSetChanged(); //restarts the process of getCount() & getView()
                    numOfStations++;
                    stationStats.setText(String.valueOf(numOfStations));

                    //add messages to database
                    cValues.put(OCTranspoDAO.KEY_STATION, stationInput.getText().toString());
                    db.insert(OCTranspoDAO.TABLE_NAME, OCTranspoDAO.KEY_ID, cValues);
                    //clear text after display/saved
                    stationInput.setText("");

                }else{
                    CharSequence text = "Number entered needs to be 4 digits ";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

//                    submitButton.setOnClickListener(e ->{
//                        Snackbar.make(e, "Number entered needs to be 4 digits", Snackbar.LENGTH_LONG).show();
//                    });

                }
            }
        });
    }


    /**
     * Validate user input is a four digit number for the bust station.
     * @param numEntered Number entered by user
     * @return True if number is 4 digits.
     */
    private boolean validNumber(String numEntered){
        return numEntered.length() == 4;
    }

    /**
     * Add the menu items to the toobar.
     * @param m
     * @return true if number is valid(4 digits).
     */
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.transpo_toolbar_menu, m);
//        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;

    }
    /**
     * Set the actions for the toolbar menu items by the item id.
     * @param mi Item from the menu layout.
     * @return the that item was clicked.
     */
    public boolean onOptionsItemSelected(MenuItem mi){

        switch(mi.getItemId()){
            case R.id.home : finish();
                break;
            case R.id.activity_two : intent = new Intent(OCTranspo.this, Movie.class);
                startActivity(intent);
                break;
            case R.id.activity_three : intent = new Intent(OCTranspo.this, CBCNewsActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_four: intent = new Intent(OCTranspo.this, FoodNutrition.class);
                startActivity(intent);
                break;
            case R.id.aboutTranspo : Snackbar.make(mainLayout, version+" "+author, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
                break;
            case R.id.helpTranspo :  helpDialog.show();
                break;
            case R.id.statistics : statisticsDialog.show();
                break;

        }

        return true;
    }

    /**
     * Build the Help Dialog popUp and add the custom cancel button function.
     */
    private Dialog createHelpDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = (inflater.inflate(R.layout.custom_help_dialog, null));
        builder.setView(view);
        FloatingActionButton cancelHelp = view.findViewById(R.id.infoBackButton);
        cancelHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpDialog.dismiss();
            }
        });

        return builder.create();
    }

    private Dialog createStatisticsDialog(int stations){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = (inflater.inflate(R.layout.custom_statistics_dialog, null));
        stationStats = view.findViewById(R.id.stationsStat);
        stationStats.setText(String.valueOf(stations));

        builder.setView(view);
        FloatingActionButton cancelHelp = view.findViewById(R.id.infoBackButton);
        cancelHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statisticsDialog.dismiss();
            }
        });

        return builder.create();
    }

    /**
     * On closing activity. Close Database.
     */
    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }

    /**
     * Adapter for the station numbers, converts the arrayList of numbers into view items.
     * Provides layout for the listView items and tracks
     * the size of the list and the item at that position
     */
    private class StationsAdapter extends ArrayAdapter<String> {

        StationsAdapter(Context ctx){
            super(ctx,0);

        }

        /**
         * Get the number of items in the list.
         * @return Number of items
         */
        public int getCount(){
            return routes.size();
        }

        public String getItem(int position){
            return routes.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = OCTranspo.this.getLayoutInflater();

            convertView = inflater.inflate(R.layout.stations_line_layout, null);

            TextView station = convertView.findViewById(R.id.stationsText);
            station.setTextSize(18);
            station.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            station.setText( getItem(position) ); //get the string at position
            return convertView;
        }

        /**
         * Get the id of the item clicked on by user.
         * @param position Item clicked (from position in list)
         * @return the item id.
         */
        public long getItemId(int position){
            cursor = db.query(false, OCTranspoDAO.TABLE_NAME, new String[]{"_id"}, null, null, null, null, null, null);
            cursor.moveToPosition(position);
            Long id = cursor.getLong(cursor.getColumnIndex("_id"));
            cursor.close();
            return id;
        }

    }
}
