package com.example.rex;

import java.util.ArrayList;

/**
 * This class represents a Result (recommendation) object. A Result has a name, a description, the
 * description source, a relevant Youtube video (if applicable), the Youtube's URL, and Youtube ID.
 * This class has getter methods to retrieve Result data.
 *
 * @author Jesse Masciarelli (6243109)
 * @author Katie Lee (6351696)
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

