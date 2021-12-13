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
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

/**
 * This class represents the Popup activity for the selected result (recommendation) that the user
 * selected from the MainActivity. Here, the user will be able to see the result's name, a brief
 * description pulled from Wikipedia, the Wikipedia source, and relevant Youtube video (if
 * applicable), and two options: to save to their "Save for later" list or to "See more like this".
 * If the user selects, "See more like this", this activity will close and an API call on the
 * selected result item will get called.
 *
 * @see MainActivity
 * @see Result
 * @author Jesse Masciarelli (6243109)
 * @author Katie Lee (6351696)
 */

public class PopupResult extends Activity {

    String queryType = MainActivity.queryType;
    String result;
    Result selectedResult;
    TextView popupTitle, popupDescription;
    WebView popupYoutube;
    Button popupFavourite, popupSearch;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set-up saved instances, layout, and widgets
        MainActivity.popUpResult = true;
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
        popupTitle.setText(selectedResult.getName()+"\n");
        try {
            Spanned link = Html.fromHtml(selectedResult.getWikiURL());
            popupDescription.setText(selectedResult.getDescription().substring(0, 150) + "..." + "\n" + link);
        } catch(Exception e) {popupDescription.setText("Description not available.");}
        try {
            popupDescription.setMovementMethod(LinkMovementMethod.getInstance());
        } catch(Exception e) {}
        try {
            showVid(selectedResult.getYoutubeID());
        } catch(Exception e) {
            popupYoutube.setBackgroundColor(getColor(R.color.black));
            popupDescription.setText("Description and video not available.");
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
        popupDescription = findViewById(R.id.popupDescription);
        popupYoutube = findViewById(R.id.popupYoutube);
        popupFavourite = findViewById(R.id.popupFavourite);
        popupSearch = findViewById(R.id.popupSearch);
    }

    /**
     * This method embeds a Youtube video in the webView.
     * @param videoID the ID of the youtube video to embed
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void showVid(String videoID) {
        try {
            WebView youtubeWebView = (WebView) findViewById(R.id.popupYoutube);
            youtubeWebView.setWebViewClient(new
                                                    WebViewClient() {
                                                        @Override
                                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                            return false;
                                                        }
                                                    });
            WebSettings webSettings = youtubeWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setNeedInitialFocus(false);
            webSettings.setUseWideViewPort(true);
            youtubeWebView.loadUrl("https://www.youtube.com/embed/" + videoID);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method adds the displayed result to the user's appropriate "Save for later" list. The
     * user is unable to add duplicates to their list.
     * @param view the View
     */
    public void addToFavourites(View view) {
        List<Result> favList = MainActivity.allFavourites;
        Boolean exists = false;
        for(Result r: favList) {
            if (r.getName().equals(selectedResult.getName()) && r.getType().equals(selectedResult.getType())) {
                exists = true;
            }
        }
        if(exists){
            Toast.makeText(getApplicationContext(),"Already saved to list.",Toast.LENGTH_SHORT).show();
        }else{
                MainActivity.allFavourites.add(selectedResult);
                MainActivity.saveToInternalStorage(this, "favList","favKey");
                Toast.makeText(getApplicationContext(),"Saved to list!",Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * This method performs a new API call on the selected result - setting the current result as
     * the search query of the new call.
     * @param view the View
     */
    public void doSearch(View view) {
        MainActivity.searchBox.setText(result);
        MainActivity.newSearch = true;
        finish();
    }

    /**
     * This method sets the popUpResult flag from the MainActivity to false, indicating that the
     * user can now open another PopupResult activity window if they'd like to. The Youtube video
     * also gets destroyed and its cache gets cleared - making it so that the Youtube video will
     * not continue to play even after this activity has finished.
     */
    @Override
    public void onDestroy() {
        MainActivity.popUpResult = false;
        // destroy video and clear cache
        popupYoutube.onPause();
        popupYoutube.loadUrl("");
        popupYoutube.clearCache(true);
        super.onDestroy();
    }

}
