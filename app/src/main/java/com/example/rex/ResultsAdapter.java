package com.example.rex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

/**
 * This class acts as an Adapter for the Results listView display. When the user searches for an
 * artist/movie/show, its recommendation results will get returned from an API call. This adapter
 * gets the listView and prints out each Result (recommendation) of the search query with its name
 * and description.
 */

public class ResultsAdapter extends ArrayAdapter<Result>{

    public ResultsAdapter(@NonNull Context context, List<Result> results) {
        super(context, 0, results);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Result result = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.result_item, parent, false);

        // Initialize widgets
        TextView resultItemTitle = convertView.findViewById(R.id.resultItemTitle);
        TextView resultItemType = convertView.findViewById(R.id.resultItemType);

        // Display the item's name and description
        resultItemTitle.setText(result.getName());
        resultItemType.setText(result.getType());

        return convertView;
    }

}


