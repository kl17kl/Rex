package com.example.rex;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * This class represents the Popup activity for the user's watchlist...
 */

public class Popup extends Activity {

    String queryType = MainActivity.queryType;
    TextView popupTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set-up saved instances, layout, and widgets
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_view);
        initWidgets();

        // Define pop-up screen dimensions
        initDimensions();

        popupTitle.setText(queryType);

        // Populate the listView with watchlist items of the given type
        setWatchListAdapter();
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
    }

    /**
     * This method sets up the MeetingAdapter to populate the listView with all the meetings for
     * the selected date.
     */
    private void setWatchListAdapter() {
    }

}
