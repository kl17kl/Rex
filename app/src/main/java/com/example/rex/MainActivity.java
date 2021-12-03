package com.example.rex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    String queryType = "music"; //defaults
    int queryLimit = 20;
    int queryInfo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        System.out.println("kt peanut");
                        queryType = "music";
                        break;
                    case 1:
                        System.out.println("tab 2");
                        queryType = "movie";
                        break;
                    case 2:
                        System.out.println("tab 3");
                        queryType = "show";
                        break;
                    default:
                        System.out.println("Defaulted");
                }
            }
                @Override
                public void onTabUnselected (TabLayout.Tab tab){

                }

                @Override
                public void onTabReselected (TabLayout.Tab tab){

                }

        });
    }

    /** doSearch executes when the search button is pressed. It takes the user input and uses
     * the global variables to make an api call, and then handles the return.
     * */

    public void doSearch(View view) {
        // Get the search query
//        EditText textArea = (EditText)findViewById(R.id.searchEditText);
//        String input = textArea.getText().toString();
//
//        // Start a thread to pull the API data
//        APIThread thread = new APIThread(input,queryType,queryLimit,queryInfo);
//        thread.start();
//        while (!thread.isComplete()) {}
//
//        // Save the API data into JSONArrays
//        JSONArray infoArray = thread.getInfo();
//        JSONArray resultsArray = thread.getResults();
//
//        // Start new ExploreMMS activity with JSON data
//        Intent intent = new Intent(ExploreRandoms.this, ExploreMMS.class);
//        intent.putExtra("info", String.valueOf(infoArray));
//        intent.putExtra("results", String.valueOf(resultsArray));
//        startActivity(intent);
    }

}