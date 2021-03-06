package com.example.rex;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * This is the MainActivity class that gets called upon start-up. Here, the user is presented with
 * the main screen, in which they can select a category (Music, Movies, or Shows) and search for
 * recommended items based on their search. API parameters and calls are managed here.
 *
 * Here, the user can open their "Save for later" list, in which a popup with their "saved" lists
 * will display their saved items for a particular category. For this popup, an instance of the
 * PopupFavourites activity will initiate.
 *
 * After looking up a search query, the user can also select any recommendation from the results
 * list listed on the screen. If the user selects a recommendation result, a popup will display
 * the result's information (Wikipedia description, link to the Wikipedia source, and a relevant
 * Youtube video). From here, the user can add that item to their "Save for later" list or they
 * can perform another search with that item. For this popup, an instance of the PopupResult
 * activity will initiate
 *
 * @see PopupFavourites
 * @see PopupResult
 * @author Jesse Masciarelli (6243109)
 * @author Katie Lee (6351696)
 */

public class MainActivity extends AppCompatActivity {

    static String queryType = "music";          // Default type
    static String typeTitle = "Artists";        // Default type copy for titles
    static String typeText = "musical artist";  // Default type copy for subtitles/text
    int queryLimit = 10;                        // Max results returned
    int queryInfo = 1;                          // 1 = Display additional info
    static boolean newSearch = false;           // Flag: is this a secondary API call
    static boolean popUpResult = false;         // Flag: if popupResult instance already exists
    static boolean popUpFavourites = false;     // Flag: if popupFavourites instance already exists
    JSONArray infoArray, resultsArray;          // Returned JSON results from API call

    static ArrayList<Result> allFavourites = new ArrayList<>(); // The user's "Save for later" list

    // The widgets on the activity_main layout
    static EditText searchBox;
    TextView exploreTitle, exploreCopy, listViewTitle, listViewCopy;
    ListView resultsListView;
    Button musicButton, moviesButton, showsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize staved instances, layout, and widgets
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();

        // Retrieve user data from internal storage
        String internalData = retrieveFromInternalStorage(this, "favList",
                "favKey", "Error: Unable to retrieve data from internal storage");
        allFavourites = convertToArrayList(internalData);

        // Onclick listener for listView items (when a user selects a recommendation/result)
        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected result item
                Result selected = (Result) adapterView.getItemAtPosition(i);
                // Open popup with selected item details - only allow one instance at a time
                if (!popUpResult) {
                    Intent intent = new Intent(MainActivity.this, PopupResult.class);
                    intent.putExtra("selected", selected.getName());
                    startActivity(intent);
                }
            }
        });
    }


    /**
     * This method converts the data retrieved from internal storage (String) to an ArrayList
     * object of type Result.
     * @param internalData String of user's favouritesList from internal storage
     * @return ArrayList: favouritesList
     */
    private ArrayList<Result> convertToArrayList(String internalData) {
        ArrayList<Result> favouritesList = new ArrayList<>();
        String[] favourites = internalData.split("\n");
        for (int i=0; i< favourites.length; i++) {
            String[] values = favourites[i].split("\t");
            if (values.length == 6) {
                Result r = new Result(values[0], values[1], values[2], values[3], values[4], values[5]);
                favouritesList.add(r);
            }
        }
        return favouritesList;
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
    }

    /** This method initializes the widgets on the main activity screen.
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
     * @param view the View
     */
    @SuppressLint("NonConstantResourceId")
    public void onclickTab(View view) {
        refreshScreen();
        switch(view.getId()) {
            case R.id.musicButton:
                setCategory(getString(R.string.music), musicButton, getString(R.string.artists_upper), getString(R.string.musical_artist));
                break;
            case R.id.moviesButton:
                setCategory(getString(R.string.movies), moviesButton, getString(R.string.movies_upper), getString(R.string.movie));
                break;
            case R.id.showsButton:
                setCategory(getString(R.string.shows), showsButton, getString(R.string.shows_upper), getString(R.string.show));
                break;
            default:
                throw new RuntimeException("_ERROR: Unknown category button ID");
        }
    }

    /** This method refreshes the screen by clearing it's recommendation list section.
     */
    private void refreshScreen() {
        setButtonsInactive();
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
     * @param category     Music, Movie, or Show
     * @param button       Selected tab button
     * @param title        Artists, Movies, or Shows
     * @param text         Copy under title: musical artist, movie, or TV show
     */
    @SuppressLint("SetTextI18n")
    private void setCategory(String category, Button button, String title, String text) {
        queryType = category;
        typeTitle = title;
        typeText = text;
        button.setBackgroundColor(getColor(R.color.purple_active)); // Set the tab to active
        button.setTextColor(getColor(R.color.white));

        // Display the appropriate data to the screen
        exploreTitle.setText("Explore "+title);
        exploreCopy.setText(getString(R.string.explore_copy1) + " " + text + " " + getString(R.string.explore_copy2));
        searchBox.setText("");
        searchBox.setHint(getString(R.string.search_copy) + " " + text + "???");
    }

    /**
     * This method executes an API call with the "Query" parameter set as the input from the search
     * bar. The results from the call get returned in the form of a JSONArray.
     * @param view the View
     */
    @SuppressLint("SetTextI18n")
    public void doSearch(View view) {
        // Close/hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // Get the search query
        String input = searchBox.getText().toString();
        if (input.equals("")) {
            Toast.makeText(getApplicationContext(), "Try typing something into the search bar.", Toast.LENGTH_SHORT).show();
        }
        else {
            // Start a thread and wait until it's complete to pull the API data
            APIThread thread = new APIThread(input, queryType, queryLimit, queryInfo);
            thread.start();
            while (!thread.isComplete()) {} // wait for thread to finish

            // Save the API data into JSONArrays and update resultsList
            infoArray = thread.getInfo();
            resultsArray = thread.getResults();
            Result.resultsList = JSONToArrayList(resultsArray);

            // Update the screen
            setResultsAdapter();
            listViewTitle.setText("Similar " + typeTitle + " to\n" + input.substring(0, 1).toUpperCase() + input.substring(1));
            listViewCopy.setText("Top " + typeText + "s " + getString(R.string.results_copy));

            // If returned results is empty (i.e. if API doesn't recognize search query)
            ResultsAdapter adapter = new ResultsAdapter(this, Result.resultsList);
            if (adapter.getCount() == 0)
                listViewCopy.setText("Sorry, we couldn't find what you were looking for!");
        }
    }

    /**
     * This method updates and saves the returned results from the API call to the resultList in
     * the Result class. To do so, it first converts the JSON object to an ArrayList object.
     * @param array the JSONArray to parse through
     */
    public static ArrayList<Result> JSONToArrayList(JSONArray array) {
        ArrayList<Result> returnList = new ArrayList<>();
        if (array != null) {
            for (int i=0;i<array.length();i++){
                returnList.add(new Result(getKeyValue(array, "Name", i),
                        getKeyValue(array, "Type", i),
                        getKeyValue(array, "wTeaser", i),
                        getKeyValue(array, "wUrl", i),
                        getKeyValue(array, "yUrl", i),
                        getKeyValue(array, "yID", i)));
            }
        }
        return returnList;
    }

    /**
     * This method returns the value at a given key within a JSON array.
     * @param array     the JSONArray
     * @param key       the key
     * @param position  the position in the array to search within
     * @return  the value at the specified key and position
     */
    private static String getKeyValue(JSONArray array, String key, Integer position) {
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
        for (int i = 0; i < adapter.getCount(); i++) {
            View mView = adapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    /**
     * This method opens a popup with the user's favourites of a given type (Music, Movies, Shows).
     * @param view the View
     */
    public void openFavourites(View view){
        if (!popUpFavourites)
            startActivity(new Intent(MainActivity.this, PopupFavourites.class));
    }

    /**
     * When the user wants to see recommendations for their selected result, a new search will
     * be performed on that selected result.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (newSearch) {
            doSearch(searchBox);
        }
    }

    /**
     * This method saves all the user's favourites from the favouritesList as a String and saves
     * it to internal storage with a given preference name and data key.
     * @param context the application context (this)
     * @param preferenceName the name to save as to internal storage
     * @param dataKey the key to assign to the saved content
     */
    public static void saveToInternalStorage(Context context, String preferenceName, String dataKey) {
        StringBuilder sb = new StringBuilder();
        for (Result result : allFavourites) {
            sb.append(result.getName()+"\t"+result.getType()+"\t"+result.getDescription()+"\t"
                    +result.getWikiURL()+"\t"+result.getYoutubeURL()+"\t"+result.getYoutubeID()+"\n");
        }
        String userFavouritesList = sb.toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(dataKey, userFavouritesList);
        editor.commit();
    }

    /**
     * This method retrieves a file from internal using a given preference name and data key. If
     * there is an error with retrieving the file given default value is returned instead.
     * @param context the application context (this)
     * @param preferenceName the name to retrieve from internal storage
     * @param dataKey the key to confirm and match
     * @param defaultValue an error message saying the retrieving failed
     * @return the retrieved String from internal storage
     */
    public static String retrieveFromInternalStorage(Context context, String preferenceName, String dataKey, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(dataKey, defaultValue);
    }
}