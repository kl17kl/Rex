package com.example.rex;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the MainActivity class that gets called upon start-up. Here, the user is presented with
 * the main screen, in which they can select a category (Music, Movies, or Shows) and search for
 * recommended items based on their search. API parameters and calls are managed here.
 *
 * @author Jesse Masciarelli
 * @author Katie Lee (6351696)
 */
public class MainActivity extends AppCompatActivity {

    static String queryType = "music";   // Default type
    String typeTitle = "Artists";
    String typeText = "musical artist";
    int queryLimit = 10;         // Max results returned
    int queryInfo = 1;          // 1 = Display additional info

    // Returned JSON results from API call
    JSONArray infoArray, resultsArray;

    // Categorical favourites lists (currentWatchLater = current favourites list view)
    List<String> watchLaterMusic, watchLaterMovies, watchLaterShows, currentWatchLater;

    // The widgets on the activity_main layout
    EditText searchBox;
    TextView exploreTitle, exploreCopy, listViewTitle, listViewCopy;
    ListView resultsListView;
    Button musicButton, moviesButton, showsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize staved instances, layout, and widgets
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();

        // Onclick listener for listView items (when a user selects a recommendation)
        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Perform new API call on selected item
                Result selected = (Result) adapterView.getItemAtPosition(i);
                searchBox.setText(selected.getName());
                doSearch(searchBox);
                searchBox.setText("");
            }
        });
    }


    /**
     * This method sets up the ResultsAdapter to populate the listView with all the
     * returned recommendations (results) for the given search result.
     */
    private void setResultsAdapter() {
        ArrayList<Result> returnedResults = Result.resultsList;
        ResultsAdapter resultsAdapter = new ResultsAdapter(this, returnedResults);
        resultsListView.setAdapter(resultsAdapter);
        justifyListViewHeight(resultsListView);
        System.out.println("Adapter has been set");
    }

    /**
     * This method initializes the widgets on the main activity screen.
     */
    private void initWidgets() {
        searchBox = findViewById(R.id.searchBox);
        musicButton = findViewById(R.id.musicButton);
        moviesButton = findViewById(R.id.moviesButton);
        showsButton = findViewById(R.id.showsButton);
        exploreTitle = findViewById(R.id.exploreTitle);
        exploreCopy = findViewById(R.id.exploreCopy);
        listViewTitle = findViewById(R.id.listViewTitle);
        listViewCopy = findViewById(R.id.listViewCopy);
        resultsListView = findViewById(R.id.resultsListView);
    }

    /**
     * This method determines what to do once a user selects a tab (Music, Movies, or Shows).
     * All tab buttons change their display to be inactive, and depending on what tab the user
     * selects, setCategory() gets called to display the appropriate data to the screen.
     *
     * @param view the View
     */
    @SuppressLint("NonConstantResourceId")
    public void onclickTab(View view) {
        refreshScreen();

        switch(view.getId()) {
            case R.id.musicButton:
                setCategory(getString(R.string.music), watchLaterMusic, musicButton,
                        getString(R.string.artists_upper), getString(R.string.musical_artist));
                break;
            case R.id.moviesButton:
                setCategory(getString(R.string.movies), watchLaterMovies, moviesButton,
                        getString(R.string.movies_upper), getString(R.string.movie));
                break;
            case R.id.showsButton:
                setCategory(getString(R.string.shows), watchLaterShows, showsButton,
                        getString(R.string.shows_upper), getString(R.string.show));
                break;
            default:
                throw new RuntimeException("_ERROR: Unknown category button ID");
        }
    }

    /**
     * This method refreshes the screen by clearing it's recommendation list section.
     */
    private void refreshScreen() {
        // Reset tab buttons
        setButtonsInactive();
        // Clear recommendations section
        listViewTitle.setText("");
        listViewCopy.setText("");
        Result.resultsList.clear();
        setResultsAdapter();
    }

    /**
     * This method resets all tab buttons to be inactive. The button backgrounds are set to
     * the default medium grey tone and the button text is set to black.
     */
    private void setButtonsInactive() {
        musicButton.setBackgroundColor(getColor(R.color.purple_inactive));
        moviesButton.setBackgroundColor(getColor(R.color.purple_inactive));
        showsButton.setBackgroundColor(getColor(R.color.purple_inactive));
        musicButton.setTextColor(getColor(R.color.black));
        moviesButton.setTextColor(getColor(R.color.black));
        showsButton.setTextColor(getColor(R.color.black));
    }

    /**
     * This method displays the appropriate data (i.e., text, titles, API parameter, etc.) on the
     * screen to correspond with the user's selected tab category (Music, Movies, or Shows).
     *
     * @param category          Music, Movie, or Show
     * @param favouritesList    favourite music/movie/show lists
     * @param button            selected tab button
     * @param title             title: Explore + Artists, Movies, or Shows
     * @param text              copy under title: musical artist, movie, or TV show
     */
    @SuppressLint("SetTextI18n")
    private void setCategory(String category, List<String> favouritesList, Button button,
                             String title, String text) {
        queryType = category;                 // Set API "Type" parameter
        typeTitle = title;
        typeText = text;
        currentWatchLater = favouritesList;   // Set the current favourites list
        button.setBackgroundColor(getColor(R.color.purple_active)); // Set the tab to active
        button.setTextColor(getColor(R.color.white));

        // Display the appropriate data to the screen
        exploreTitle.setText("Explore "+title);
        exploreCopy.setText(getString(R.string.explore_copy1) + " " + text + " " + getString(R.string.explore_copy2));
        searchBox.setText("");
        searchBox.setHint(getString(R.string.search_copy) + " " + text + "…");
    }

    /**
     * This method executes an API call with the "Query" parameter set as the input from the search
     * bar. The results from the call get returned in the form of a JSONArray.
     *
     * @param view the View
     */
    @SuppressLint("SetTextI18n")
    public void doSearch(View view) {
        // Close/hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // Get the search query
        String input = searchBox.getText().toString();

        // Start a thread and wait until it's complete to pull the API data
        APIThread thread = new APIThread(input, queryType, queryLimit, queryInfo);
        thread.start();
        while (!thread.isComplete()) {}

        // Save the API data into JSONArrays and update resultsList
        infoArray = thread.getInfo();
        resultsArray = thread.getResults();
        JSONToArrayList();
        System.out.println("results array: "+resultsArray.toString());

        // Update the screen
        setResultsAdapter();
        listViewTitle.setText("Similar "+ typeTitle + " to\n" + input.substring(0,1).toUpperCase() + input.substring(1));
        listViewCopy.setText("Top " + typeText + "s " + getString(R.string.results_copy));

        // If returned results is empty (i.e. if API doesn't recognize search query)
        ResultsAdapter adapter = new ResultsAdapter(this, Result.resultsList);
        if (adapter.getCount() == 0)
            listViewCopy.setText("Sorry, we couldn't find what you were looking for!");
    }

    /**
     * This method updates and saves the returned results from the API call to the resultList in
     * the Result class. To do so, it first converts the JSON object to an ArrayList object.
     */
    private void JSONToArrayList() {
        ArrayList<Result> returnList = new ArrayList<>();

        if (resultsArray != null) {
            for (int i=0;i<resultsArray.length();i++){
                returnList.add(new Result(getKeyValue(resultsArray, "Name", i),
                        getKeyValue(resultsArray, "Type", i),
                        getKeyValue(resultsArray, "wTeaser", i),
                        getKeyValue(resultsArray, "wUrl", i),
                        getKeyValue(resultsArray, "yUrl", i),
                        getKeyValue(resultsArray, "yID", i)));
            }
        }
        Result.resultsList = returnList;
    }

    /**
     * This method returns the value at a given key within a JSON array.
     * @param array the JSONArray
     * @param key   the key
     * @param position  the position in the array to search within
     * @return  the value at the specified key and position
     */
    private String getKeyValue(JSONArray array, String key, Integer position) {
        String value = null;
        try {
            for(int i=0; i<array.length(); i++) {
                if (i == position) {
                    JSONObject obj = array.getJSONObject(i);
                    value = obj.optString(key);
                    break;
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * This method calculates the height that the listview should be after a list of recommended
     * results has been returned by an API call. The total height is given by the sum of each
     * listview item's individual height.
     * @param listView the results (recommendations) listview
     */
    public static void justifyListViewHeight (ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) return;
        int totalHeight = 0;
        int buffer = 40;
        for (int i = 0; i < adapter.getCount(); i++) {
            View mView = adapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight() + buffer;
        }
        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }


    /**
     * This method opens a popup with the user's watchlist of a given type (Music, Movies, Shows).
     * @param view the View
     */
    public void openWatchLater(View view){
        // Start a new popup activity
        startActivity(new Intent(MainActivity.this, Popup.class));
    }

}