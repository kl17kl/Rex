package com.example.rex;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONArray;

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

    String queryType = "music"; // Default API call values
    int queryLimit = 20;        // Max results returned
    int queryInfo = 1;          // 1 = Display additional info

    // Categorical favourites lists (currentWatchLater = current favourites list view)
    List<String> watchLaterMusic, watchLaterMovies, watchLaterShows, currentWatchLater;

    // The widgets on the activity_main layout
    EditText searchBox;
    TextView exploreTitle, exploreCopy, listViewTitle, listViewCopy;
    Button musicButton, moviesButton, showsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
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
        // Refresh buttons (set inactive)
        setButtonsInactive();

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
    public void doSearch(View view) {
        // Get the search query
        String input = searchBox.getText().toString();

        // Start a thread and wait until it's complete to pull the API data
        APIThread thread = new APIThread(input, queryType, queryLimit, queryInfo);
        thread.start();
        while (!thread.isComplete()) {}

        // Save the API data into JSONArrays
        JSONArray infoArray = thread.getInfo();
        JSONArray resultsArray = thread.getResults();
    }

    public void openWatchLater(View view){
        //popup
    }

}