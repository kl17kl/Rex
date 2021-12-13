package com.example.rex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This class represents the Popup activity for removing an item from one's Saved list. From the
 * user's Saved list, if the user chooses to remove an item from their list, they can click on any
 * desired item, and an instance of this activity will initiate. The purpose of this activity is
 * to add friction to the user's action by confirming if they'd sure they'd want to remove a
 * selected item from their list. From here, the user can accept and proceed to remove their item,
 * or they can cancel and in which case nothing will happen. In both cases, the user will get
 * redirected back to their Favourites list (the previous screen).
 *
 * @see PopupFavourites
 * @author Jesse Masciarelli (6243109)
 * @author Katie Lee (6351696)
 */

public class PopupRemove extends Activity {

    String result;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set-up saved instances and layout
        PopupFavourites.popupRemove = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_remove);

        // Retrieve passed result name from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null) result = extras.getString("selected");

        // Define pop-up screen dimensions
        initDimensions();

        // Initialize warning message (to remove an item)
        TextView warningMessage = findViewById(R.id.warningMessage);
        warningMessage.setText("Are you sure you want to remove "+result+"?");
    }

    /** This method assigns the popup screen dimensions.
     */
    private void initDimensions() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)((dm.widthPixels)*0.8),(int)((dm.heightPixels)*0.4));
    }

    /**
     * This method removes the selected item from the user's Saved list.
     * @param view the View
     */
    public void removeFromFavourites(View view) {
        for (Result r : MainActivity.allFavourites) {
            if (r.getName().equals(result)) {
                MainActivity.allFavourites.remove(r);
                break;
            }
        }
        Toast.makeText(getApplicationContext(),"Removed from list!",Toast.LENGTH_SHORT).show();
        MainActivity.saveToInternalStorage(this, "favList","favKey");
        finish();
    }

    /**
     * This method cancels the remove action and ends the popup activity.
     * @param view the View
     */
    public void cancel(View view) {
        finish();
    }

    /**
     * This method sets the popupRemove flag from the MainActivity to false, indicating that this
     * activity will end and the user can now start a new instance of this activity whenever.
     */
    @Override
    public void onDestroy() {
        PopupFavourites.popupRemove = false;
        super.onDestroy();
    }
}
