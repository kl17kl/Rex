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

    private String name, description;

    public Result(String name, String description) {
        this.name = name;
        this.description = description;
        // wiki url and youtube url?
    }

    /** Getter and setter methods for Result data. */

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

}

