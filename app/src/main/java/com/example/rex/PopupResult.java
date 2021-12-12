package com.example.rex;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class represents the Popup activity for the selected result (recommendation) item...
 */

public class PopupResult extends Activity {

    String queryType = MainActivity.queryType;
    String result;
    Result selectedResult;
    TextView popupTitle, popupDescription, popupYoutube;
    Button popupFavourite, popupSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set-up saved instances, layout, and widgets
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_result);
        initWidgets();

        // Retrieve passed result name from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null) result = extras.getString("selected");


        // Define pop-up screen dimensions
        initDimensions();

        // Perform API call on selected result to get result data
        APIThread thread = new APIThread(result, queryType, 1, 1);
        thread.start();
        while (!thread.isComplete()) {}
        selectedResult = MainActivity.JSONToArrayList(thread.getInfo()).get(0);

        // Display result details
        popupTitle.setText(selectedResult.getName());
        popupDescription.setText(selectedResult.getDescription());
    }

    /**
     * This method assigns the popup screen dimensions.
     */
    private void initDimensions() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)((dm.widthPixels)*0.7),(int)((dm.heightPixels)*0.7));
    }

    /**
     * This method initializes the widgets.
     */
    private void initWidgets() {
        popupTitle = findViewById(R.id.popupTitle);
        popupDescription = findViewById(R.id.popupDescription);
        popupYoutube = findViewById(R.id.popupYoutube);
        popupFavourite = findViewById(R.id.popupFavourite);
        popupSearch = findViewById(R.id.popupSearch);
    }


    /**
     * This method adds the displayed result to the user's appropriate favourites list.
     * @param view the View
     */
    public void addToFavourites(View view) {
        MainActivity.currentFavourites.add(selectedResult);
    }

    /**
     * This method performs a new API call on the selected result - setting the current result as
     * the search query of the new call.
     * @param view the View
     */
    public void doSearch(View view) {
        MainActivity.searchBox.setText(result);
        finish();
    }

}