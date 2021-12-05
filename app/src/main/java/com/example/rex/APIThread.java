package com.example.rex;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIThread extends Thread {

    private String q; //the input query (Drake, Matilda, etc.) - required
    private String t; //the query type (music, movie, show) - optional
    private int l; //the return limit (eg. return only 5 results) - optional
    private int i; //1 for more info, 0 otherwise - optional

    private JSONArray info, results;
    private Boolean complete = false;
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

            System.out.print(result.toString());

            // Converting results into a JSONObject
            JSONObject json = new JSONObject(result.toString());
            JSONObject similar = json.getJSONObject("Similar");
            System.out.println("RESULTS: " + json);

            // Saving the JSON's Info and Results data
            info = similar.getJSONArray("Info");
            results = similar.getJSONArray("Results");

            /* Code: printing out values from JSONArray
            for (int i=0; i<info.length(); i++) {
                JSONObject rec = info.getJSONObject(i);
                String name = rec.getString("Name");
                String type = rec.getString("Type");
                System.out.println(name+" "+type);
            }*/

            // Wait for main process to kill thread
            complete = true;
            while (true) {}

        } catch (Exception e) {
        }
    }

    public JSONArray getInfo() {
        return this.info;
    }

    public JSONArray getResults() {
        return this.results;
    }

    public Boolean isComplete() {
        return this.complete;
    }

}

