package com.example.minjia.finalproject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.RequiresApi;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.minjia.finalproject.R;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * a fragemnet in OCTranspo GUI to display the list of routes available for the users to enter
 * the bus station number. Bus routes will be displayed in the fragement listView, while Details
 * of the bus route (such as start time, logtitude,GPS speed) will be displayed in a popup listView
 * @author xinji Zhu
 */
public class RoutesFragment extends Fragment {

    // crreate an ArrayList of routes to use for the fragment listView (Routes)
    private ArrayList<String> routes = new ArrayList<>();

    // create an ArrayList of routeDetails to use for the dialog popup listView (Route Details)
    private ArrayList<String> routeDetails = new ArrayList<>();

    // creata a ListView of routeList to use for displaying the route lists
    private ListView routeList;

    // create a ListView of detailsList to sue for displaying the route details
    private ListView detailsList;

    // createa an Adapter of routeList
    private RouteListAdapter routeListAdapter;

    // create an Adapter of detailsList
    private DetailsListAdapter detailsListAdapter;

    private TextView routeDetailsTitle;

    private Dialog detailsDialog;

    private RouteInfoParser routeInfoParser;

    private String currentStation;


    /**
     * Required empty public constructor
     */
    public RoutesFragment() {

    }

//    @TargetApi(Build.VERSION_CODES.M)
//    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        detailsDialog = createRouteDetails();
    }

    /**
     * Called from OCTranspo to add routes to the route list.
     * @param route Route name to add to list.
     */
    public void addRoutes(String route){
        routes.add(route);
    }

    /**
     * When fragment is attached to the main screen. Station number is transported from main activity.
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle b = getArguments();
        currentStation = b.getString("stationNum");

    }

    /**
     * When fragment is detached from the main screen. route information is cleared.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        routes.clear();
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View newView = inflater.inflate(R.layout.fragment_routes, container, false);
        routeListAdapter = new RouteListAdapter(this.getContext());

        routeList = newView.findViewById(R.id.routeList);

        routeList.setAdapter(routeListAdapter);

        addRoutesList();

        return newView;

    }

    /**
     * Provides the layout for the Bus Routes ListView. Updates the listView when
     * data has been changed in the list.
     */
    private class RouteListAdapter extends ArrayAdapter<String> {
        RouteListAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return routes.size();
        }

        public String getItem(int position) {
            return routes.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            // use getActivity() here to avoid the warning msg of API lever 26
            LayoutInflater inflater = RoutesFragment.this.getActivity().getLayoutInflater();
//            LayoutInflater inflater = RoutesFragment.this.getLayoutInflater();
            convertView = inflater.inflate(R.layout.routes_line_layout, null);

            TextView station = convertView.findViewById(R.id.routesText);
            station.setTextSize(18);
            station.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            station.setText(getItem(position)); //get the string at position
            return convertView;
        }
    }

    /**
     * Called from RouteInfoParse after it has finished parsing the XML route details from web.
     * Then the details dialog is displayed for user with all the current details loaded.
     * @param fullDetailsList
     */
    public void updateDetails(LinkedList<String> fullDetailsList){
        detailsDialog.show();

        for (String item : fullDetailsList) {
            routeDetails.add(item);
            detailsListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Dialog builder for the Bus Route specific details popUp.
     * @return the created Dialog for display
     */
//    @TargetApi(Build.VERSION_CODES.M)
//    @RequiresApi(api = Build.VERSION_CODES.M)
    private Dialog createRouteDetails() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // use getActivity() instead of getContext() to avoid the warning mes of API leve 26
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

//        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.route_details_layout, null);
        routeDetailsTitle = view.findViewById(R.id.routeDetailTitle);
        FloatingActionButton closeDetailsButton = view.findViewById(R.id.detailsBackButton);

//        detailsListAdapter = new DetailsListAdapter(this.getContext());
        // use getActivity() instead of getContext() to avoid the warning mes of API leve 26
        detailsListAdapter = new DetailsListAdapter(this.getActivity());

        detailsList = view.findViewById(R.id.detailsList);

        detailsList.setAdapter(detailsListAdapter);

        detailsListAdapter.notifyDataSetChanged();

        closeDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailsDialog.dismiss();
            }
        });

        builder.setView(view);

        return builder.create();

    }

    /**
     * Add the button click functions for the listView of bus routes. When an item is clicked the
     * selected route is passed to RoutesInfoParser which parses the XML from the websit in background.
     */
    private void addRoutesList(){

        final RoutesFragment thisFrag = this;

        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                String routeNumber = routeListAdapter.getItem(position);

                routeDetailsTitle.setText(routeNumber);

                routeInfoParser = new RouteInfoParser(thisFrag, 1);

                routeDetails.clear();

                String num = routeNumber;
                if (num.contains(" ")){
                    num = num.substring(0, num.indexOf(" "));
                    num = num.substring(0, num.length()-1);
                }

                routeInfoParser.setBusStationNum(currentStation);

                routeInfoParser.setRouteNum(num);

                routeInfoParser.execute();

            }
        });
    }

    /**
     * Provides the layout for the Bus Route Details ListView. Updates the listView when
     * data has been changed in the list.
     */
    private class DetailsListAdapter extends ArrayAdapter<String> {

        DetailsListAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return routeDetails.size();
        }

        public String getItem(int position) {
            return routeDetails.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {


            // use getActivity() here to avoid the warning msg of API lever 26
            LayoutInflater inflater = RoutesFragment.this.getActivity().getLayoutInflater();

            convertView = inflater.inflate(R.layout.details_line_layout, null);

            TextView details = convertView.findViewById(R.id.detailsText);
            details.setTextSize(18);
            details.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            details.setText(getItem(position)); //get the string at position
            return convertView;
        }
    }

}
