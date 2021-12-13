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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Popup activity for the selected result (recommendation) item...
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
        Spanned link = Html.fromHtml(selectedResult.getWikiURL());
        popupDescription.setText(selectedResult.getDescription().substring(0,150)+"..."+"\n"+ link); //set length of description text via substring
        popupDescription.setMovementMethod(LinkMovementMethod.getInstance());
        showVid(selectedResult.getYoutubeID());
    }

    /**
     * This method assigns the popup screen dimensions.
     */
    private void initDimensions() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)((dm.widthPixels)*0.8),(int)((dm.heightPixels)*0.8));
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
     * This method embeds a youtube video in the created web view
     *
     * @param videoID the ID of the youtube video to embed
     */

    @SuppressLint("SetJavaScriptEnabled")
    private void showVid(String videoID) {
            WebView youtubeWebView = (WebView) findViewById(R.id.popupYoutube);
            youtubeWebView.setWebViewClient(new
            WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading (WebView view, String url){
                    return false;
                }
            });
            WebSettings webSettings = youtubeWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setNeedInitialFocus(false);
            webSettings.setUseWideViewPort(true);
            youtubeWebView.loadUrl("https://www.youtube.com/embed/"+videoID);
    }

    /**
     * This method adds the displayed result to the user's appropriate favourites list.
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
