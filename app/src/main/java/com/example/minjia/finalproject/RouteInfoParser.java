package com.example.minjia.finalproject;

import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

/**
 * This class uses the bus station number or bus route number supplied from user,
 * to connect to the OC Transpo website
 * and retrieve data that is then parsed through to display needed information to user on bus routes.
 * @author Xinji Zhu (040446578) on 2018-11-19
 */
public class RouteInfoParser extends AsyncTask<String, Integer, String> {

    // create a StringBuilder of route
    private StringBuilder route = new StringBuilder(4);

    // create a StringBuilder of details
    private StringBuilder details = new StringBuilder(6);

    // create a LinkedList of fullRouteList
    private LinkedList<String> fullRouteList = new LinkedList<>();

    // create a LinkedList of fullDetailsList
    private LinkedList<String> fullDetailsList = new LinkedList<>();

    // create String of busStationNum
    private String busStationNum;

    // create a String of routeNumber
    private String routeNumber;

    // create an Object of creator
    private Object creator;

    // for searching purpose of bus route info, or bus info
    // rout is set 0; bus is set 1
    private int searchType;
    /**
     * this is a constructor that is used to set up a line to the creator to initiate the bus info
     * searching
     * @param creator
     * @param searchType
     */
    RouteInfoParser(Object creator, int searchType){
        this.creator = creator;
        this.searchType = searchType;
    }

    /**
     * set the BustStation number
     * @param num
     */
    void setBusStationNum(String num){
        busStationNum = num;
    }

    /**
     * set the Route number
     * @param num
     */
    void setRouteNum(String num){
        routeNumber = num;
    }

    /**
     * take actions after the tasks execution in the background
     * returns the route info to the class that initiates the searching
     * @param s
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (searchType == 0) {
            OCTranspo transpo = (OCTranspo) creator;
            transpo.setRouteData(fullRouteList);
        }else if (searchType == 1){
            RoutesFragment fragment = (RoutesFragment) creator;
            fragment.updateDetails(fullDetailsList);
        }
        onProgressUpdate(100);

    }

    /**
     * this is to update the creator on the progress on the background tasks
     * @param value
     */
    @Override
    protected void onProgressUpdate(Integer... value){
        if (searchType == 0) {
            OCTranspo transpo = (OCTranspo) creator;
            transpo.updateProgressBar(value[0]);
        }
    }

    /**
     *  This is the default abstruct method of class AsyncTask,
     *  Take the URL of OCTranspo website and add the needed station number or combination of station
     *  number and bus number to setup the HTTP connection.
     * @param strings
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {
        URL url;
        // Given a string representation of a URL, sets up a connection and gets an input stream.
        try {
            if (searchType == 0) {
                url = new URL("https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="+busStationNum);
                Log.i("Search for route", url.toString());
            }else {
                url = new URL("https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="+busStationNum+"&routeNo="+routeNumber);
                Log.d("Search for bus", url.toString());
            }

            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000 /* milliseconds */);
                conn.setConnectTimeout(30000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                try {
                    if (searchType == 0) {
                        parseForRoutes(conn.getInputStream());
                    }else if (searchType == 1){
                        parseForDetails(conn.getInputStream());
                    }
                } catch (XmlPullParserException pullException) {

                }
            } catch (IOException ioException) {

            }
        } catch (MalformedURLException badURL) {

        }
        return null;
    }

    /**
     * accept the input stream, parse the XML to the designed tags and save the complete
     * bus route the list
     * @param in InputStream of data from http connection
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void parseForRoutes(InputStream in) throws XmlPullParserException, IOException {
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(in, null);

            xpp.nextTag();

            //used while looping through route tags to only return full route as a line of text
            int counter = 0;

            int progress = 0;

            // Returns the type of current event: START_TAG, END_TAG, etc..
            int eventType = xpp.getEventType();

            if (eventType != XmlResourceParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlResourceParser.START_TAG:

                        int eventType2 = xpp.next();
                        while (eventType2 != XmlResourceParser.END_DOCUMENT) {
                            String tagName2 = xpp.getName();

                            switch (eventType2) {
                                case XmlResourceParser.START_TAG:
                                    // get the route number
                                    if (tagName2.equalsIgnoreCase("routeNo")) {
                                        route.delete(0, route.length());
                                        counter = 1;
                                    }
                                    // get the route direction id
                                    if (tagName2.equalsIgnoreCase("directionId")) {
                                        counter = 2;
                                    }
                                    // get the route direction
                                    if (tagName2.equalsIgnoreCase("direction")) {
                                        counter = 3;
                                    }
                                    // get the route heading info
                                    if (tagName2.equalsIgnoreCase("routeHeading")) {
                                        counter = 4;
                                    }

                                    break;
                                case XmlResourceParser.TEXT:
                                    if (counter == 1){
                                        route.append(xpp.getText());
                                        progress += 2;
                                    }else {
                                        route.append(xpp.getText() + " ");
                                    }

                                    break;
                                case XmlPullParser.END_TAG:
                                    if (counter == 4) {
                                        fullRouteList.push(route.toString());
                                        Log.i("FULL ROUTE", route.toString());
                                        counter = 0;
                                    }
                                    break;
                            }
                            eventType2 = xpp.next();
                            onProgressUpdate(progress);
                        }
                        break;
                    case XmlResourceParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
            }

        } finally {
            in.close();
        }
    }

    /**
     * accepts the input stream, parse the XML to the designed tags and save the bus route info
     * for the specific bus query
     * @param in InputStream of data from http connection
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void parseForDetails(InputStream in) throws XmlPullParserException, IOException {
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(in, null);

            xpp.nextTag();

            //used while looping through route tags to only return full route as a line of text
            int counter = 0;
            int counter2 = 0;

            int progress = 0;

            // Returns the type of current event: START_TAG, END_TAG, etc..
            int eventType = xpp.getEventType();

            if (eventType != XmlResourceParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlResourceParser.START_TAG:

                        int eventType2 = xpp.next();
                        while (eventType2 != XmlResourceParser.END_DOCUMENT) {
                            String tagName2 = xpp.getName();

                            switch (eventType2) {
                                case XmlResourceParser.START_TAG:

                                    if (xpp.getName().equalsIgnoreCase("Error")) {

                                        int eventType3 = xpp.getEventType();
                                        while (eventType3 != XmlResourceParser.END_DOCUMENT) {
                                            String tagName3 = xpp.getName();

                                            switch (eventType3) {
                                                case XmlResourceParser.START_TAG:
                                                    Log.i("INEER TAG!!!", xpp.getName() + " "+tagName3) ;
                                                    // case of trip Destination info
                                                    if (tagName3.equalsIgnoreCase("tripDestination")){
                                                        details.delete(0, details.length());
                                                        counter = 1;
                                                        counter2 += 1;
                                                        progress += 2;
                                                    }
                                                    // case of trip Start Time info
                                                    else if (tagName3.equalsIgnoreCase("tripStartTime")){
                                                        counter = 2;
                                                        counter2 += 1;
                                                        progress += 2;
                                                    }
                                                    // case of adjusted Schedule Time info
                                                    else if (tagName3.equalsIgnoreCase("adjustedScheduleTime")){
                                                        counter = 3;
                                                        counter2 += 1;
                                                        progress += 2;
                                                    }
                                                    // case of latitude info of the selected bus route
                                                    else if (tagName3.equalsIgnoreCase("latitude")){
                                                        counter = 4;
                                                        counter2 += 1;
                                                        progress += 2;
                                                    }
                                                    // case of lognitude info of the selected bus route
                                                    else if (tagName3.equalsIgnoreCase("longitude")){
                                                        counter = 5;
                                                        counter2 += 1;
                                                        progress += 2;
                                                    }
                                                    // case of GPS Speed info of the selected bus route
                                                    else if (tagName3.equalsIgnoreCase("gpsspeed")){
                                                        counter = 6;
                                                        counter2 += 1;
                                                        progress += 2;
                                                    }
                                                    break;
                                                case XmlResourceParser.TEXT:
                                                    Log.i("WHATS!!?", " "+xpp.getText());
                                                    if (counter == 1){
                                                        // case to display bus route destination
                                                        details.append("DESTINATION: "+ xpp.getText());
                                                        counter = 0;
                                                    }
                                                    else if (counter == 2){
                                                        // case to display bus route trip start time
                                                        details.append("\nTRIP START TIME: "+xpp.getText());
                                                        counter = 0;
                                                    }
                                                    else if (counter == 3){
                                                        // case to display bus route adjust schedule
                                                        details.append("\nADJ SCHEDULE TIME: "+xpp.getText());
                                                        counter = 0;
                                                    }
                                                    else if (counter == 4){
                                                        // case to display bus route latitude
                                                        details.append("\nLATITUDE: "+xpp.getText());
                                                        counter = 0;
                                                    }
                                                    else if (counter == 5){
                                                        // case to display bus route longitude
                                                        details.append("\nLONGITUDE: "+xpp.getText());
                                                        counter = 0;
                                                    }
                                                    else if (counter == 6){
                                                        // case to display bus route GPS speed
                                                        details.append("\nGPS SPEED: "+xpp.getText());
                                                        counter = 0;
                                                    }
                                                    break;
                                                case XmlPullParser.END_TAG:
                                                    if (counter2 == 6) {
                                                        fullDetailsList.push(details.toString());
                                                        counter2 = 0;
                                                    }
                                                    break;
                                            }
                                            eventType3 = xpp.next();
                                            onProgressUpdate(progress);
                                        }
                                    }
                                    break;
                                case XmlResourceParser.TEXT:
                                    break;
                                case XmlPullParser.END_TAG:
                                    break;
                            }
                            eventType2 = xpp.next();
                            //onProgressUpdate(progress);
                        }
                        break;
                    case XmlResourceParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
            }
        } finally {
            in.close();
        }
    }

}
