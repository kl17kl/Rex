package com.example.rex;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class manages the API threads and calls. This class returns JSONArrays of the returned
 * JSONObject from an API call.
 *
 * @see MainActivity
 * @see PopupResult
 * @author Jesse Masciarelli (6243109)
 * @author Katie Lee (6351696)
 */

public class APIThread extends Thread {

    private String q;   // The input query (Drake, Matilda, etc.) - required
    private String t;   // The query type (music, movie, show) - optional
    private int l;      // The return limit (eg. return only 5 results) - optional
    private int i;      // 1 for more info, 0 otherwise - optional

    private JSONArray info, results;    // The returned data in the form of a JSON
    private Boolean complete = false;   // Flag: if API thread is finished or not

    public APIThread (String query, String type, int limit, int info) {
        q = query;
        t = type;
        l = limit;
        i = info;
    }

    @Override
    public void run() {
        try {
            // Connecting to the API and performing a search with the input key
            String stringUrl = "https://tastedive.com/api/similar?q="+ q;
            if(t!=null) stringUrl+="&type="+t;
            if(l!=20) stringUrl+="&limit="+l;
            if(i!=0) stringUrl+="&info="+i;
            stringUrl += "&k=426198-Recommen-80WFYS8W"; //api key appended to end

            URL url = new URL(stringUrl);
            System.out.println(stringUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Setting up the input stream and buffer readers
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            StringBuffer result = new StringBuffer();

            // Reading in the JSON results from the API
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();

            // Converting results into a JSONObject
            JSONObject json = new JSONObject(result.toString());
            JSONObject similar = json.getJSONObject("Similar");

            // Saving the JSON's Info and Results data
            info = similar.getJSONArray("Info");
            results = similar.getJSONArray("Results");

            // Wait for main process to kill thread
            complete = true;
            while (true) {}

        } catch (Exception e) {
        }
    }

    /**
     * This method returns the JSONArray of the Info data from the main JSON object.
     * This JSONArray contains the search query's data.
     * @return the JSONArray containing info
     */
    public JSONArray getInfo() {
        return this.info;
    }

    /**
     * This method returns the JSONArray of the Results data from the main JSON object.
     * This JSONArray contains the search query's recommendations/results.
     * @return the JSONArray containing results
     */
    public JSONArray getResults() {
        return this.results;
    }

    /**
     * This method returns it's own complete flag.
     * @return true if complete
     */
    public Boolean isComplete() {
        return this.complete;
    }

}

