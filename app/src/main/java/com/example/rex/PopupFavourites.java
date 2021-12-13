package com.example.rex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class represents the Popup activity for the user's watchlist...
 */

public class PopupFavourites extends Activity {

    String queryType = MainActivity.queryType;
    TextView popupTitle, popupCopy;
    ListView favouritesListView;
    ArrayList<Result> currentFavourites = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set-up saved instances, layout, and widgets
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_favourites);
        initWidgets();

        // Define pop-up screen dimensions
        initDimensions();

        // Define the type of favourites list and display to screen
        getFavouritesListType();
        popupTitle.setText("Favourite " + MainActivity.typeTitle);
        popupCopy.setText("Here are your favourite "+MainActivity.typeText+"s!");

        // Populate the listView with watchlist items of the given type
        setFavouritesAdapter();
    }

    /**
     * This method saves the currentFavourites list with results of the desired type.
     */
    private void getFavouritesListType() {
        int type = 0; //default we assume queryType is "music"
        if (MainActivity.queryType.equals("movies")) type = 1;
        else if (MainActivity.queryType.equals("shows")) type = 2;

        for (Result r : MainActivity.allFavourites) {
            if (type == 0 && r.getType().equals("music")) currentFavourites.add(r);
            else if (type == 1 && r.getType().equals("movie")) currentFavourites.add(r);
            else if (type == 2 && r.getType().equals("show")) currentFavourites.add(r);
        }
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
        popupCopy = findViewById(R.id.popupCopy);
        favouritesListView = findViewById(R.id.favouritesListView);
    }

    /**
     * This method sets up the FavouritesAdapter to populate the listView with all the
     * user's favourite artists/movies/shows from their watchlist of a specified type.
     */
    private void setFavouritesAdapter() {
        FavouritesAdapter favouritesAdapter = new FavouritesAdapter(this, currentFavourites);
        favouritesListView.setAdapter(favouritesAdapter);
    }

}
