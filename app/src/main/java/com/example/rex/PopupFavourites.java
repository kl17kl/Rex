package com.example.rex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class represents the Popup activity for the user's "Save for later" list. For whichever
 * tab the user is on (Music, Movies, Shows), the saved items in their list for that particular
 * category is displayed in this popup. From here, the user can remove items by selecting on the
 * list item, and from there, an instance of the PopupRemove activity will initiate.
 *
 * @see MainActivity
 * @see Result
 * @see PopupRemove
 * @author Jesse Masciarelli (6243109)
 * @author Katie Lee (6351696)
 */

public class PopupFavourites extends Activity {

    TextView popupTitle, popupCopy;
    ListView favouritesListView;
    ArrayList<Result> currentFavourites = new ArrayList<>();
    static boolean popupRemove = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set-up saved instances, layout, and widgets
        MainActivity.popUpFavourites = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_favourites);
        initWidgets();

        // Define pop-up screen dimensions
        initDimensions();

        // Define the type of favourites list and display to screen
        getFavouritesListType();
        popupTitle.setText("Saved " + MainActivity.typeTitle);
        popupCopy.setText("You've saved these "+MainActivity.typeText+"s for later! Select one to remove it from the list.");

        // Populate the listView with watchlist items of the given type
        setFavouritesAdapter();

        // Onclick listener for listView items (when a user selects a recommendation)
        favouritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected result item
                Result selected = (Result) adapterView.getItemAtPosition(i);
                // Open popup with selected item details - only one instance at a time
                if (!popupRemove) {
                    Intent intent = new Intent(PopupFavourites.this, PopupRemove.class);
                    intent.putExtra("selected", selected.getName());
                    startActivity(intent);
                }
            }
        });
    }


    /** This method saves the currentFavourites list with results of the desired type.
     */
    private void getFavouritesListType() {
        int type = 0; //default we assume queryType is "music"
        if (MainActivity.queryType.equals("movies")) type = 1;
        else if (MainActivity.queryType.equals("shows")) type = 2;

        currentFavourites.clear();
        for (Result r : MainActivity.allFavourites) {
            if (type == 0 && r.getType().equals("music")) currentFavourites.add(r);
            else if (type == 1 && r.getType().equals("movie")) currentFavourites.add(r);
            else if (type == 2 && r.getType().equals("show")) currentFavourites.add(r);
        }
    }

    /** This method assigns the popup screen dimensions.
     */
    private void initDimensions() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)((dm.widthPixels)*0.8),(int)((dm.heightPixels)*0.8));
    }

    /** This method initializes the widgets.
     */
    private void initWidgets() {
        popupTitle = findViewById(R.id.popupTitle);
        popupCopy = findViewById(R.id.popupCopy);
        favouritesListView = findViewById(R.id.favouritesListView);
    }

    /**
     * This method sets up the FavouritesAdapter to populate the listView with all the
     * user's saved artists/movies/shows from their saved list of a specified type.
     */
    private void setFavouritesAdapter() {
        FavouritesAdapter favouritesAdapter = new FavouritesAdapter(this, currentFavourites);
        favouritesListView.setAdapter(favouritesAdapter);
    }

    /**
     * When the user removes an item from their "Save for later" list, the list should get
     * updated on the Saved list popup display.
     */
    @Override
    protected void onResume() {
        super.onResume();
        getFavouritesListType();
        setFavouritesAdapter();
    }

    /**
     * The popUpFavourites flag from the MainActivity is set to false, indicating that this
     * activity is finishing and that the user can open a new popupFavourites window if they wish.
     */
    @Override
    public void onDestroy() {
        MainActivity.popUpFavourites = false;
        super.onDestroy();
    }
}
