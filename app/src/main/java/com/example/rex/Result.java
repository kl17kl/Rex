package com.example.rex;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents a Result (recommendation) object. A Result has a name and description.
 * This class has getter and setter methods to retrieve Result data.
 */

public class Result {

    private String name, type, description, wikiURL, youtubeURL, youtubeID;
    public static ArrayList<Result> resultsList = new ArrayList<>();

    public Result(String n, String t, String d, String wURL, String yURL, String yID) {
        this.name = n;
        this.type = t;
        this.description = d;
        this.wikiURL = wURL;
        this.youtubeURL = yURL;
        this.youtubeID = yID;
    }

    /** Getter methods for Result data. */

    public String getName() {
        return this.name;
    }

    public String getType() { return this.type; }

    public String getDescription() {
        return this.description;
    }

    public String getWikiURL() { return this.wikiURL; }

    public String getYoutubeURL() { return this.youtubeURL; }

    public String getYoutubeID() { return this.youtubeID; }

}

