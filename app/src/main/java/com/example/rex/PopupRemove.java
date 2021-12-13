package com.example.rex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class represents the Popup activity for the selected result (recommendation) item...
 */

public class PopupRemove extends Activity {

    String result;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set-up saved instances, layout, and widgets
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

    /**
     * This method assigns the popup screen dimensions.
     */
    private void initDimensions() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)((dm.widthPixels)*0.8),(int)((dm.heightPixels)*0.4));
    }

    /**
     * This method removes the selected favourite from the user's favourites list.
     * @param view the View
     */
    public void removeFromFavourites(View view) {
        for (Result r : MainActivity.allFavourites) {
            if (r.getName().equals(result)) {
                MainActivity.allFavourites.remove(r);
                break;
            }
        }
        MainActivity.saveToInternalStorage(this, "favList","favKey");
        finish();
    }

    /**
     * This method cancles the remove action and ends the popup activity.
     * @param view the View
     */
    public void cancel(View view) {
        finish();
    }
}
