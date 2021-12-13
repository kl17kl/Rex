package com.example.rex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
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
 * and type (musical artist, movie, TV show).
 *
 * @see Result
 * @see MainActivity
 * @author Jesse Masciarelli (6243109)
 * @author Katie Lee (6351696)
 */

public class ResultsAdapter extends ArrayAdapter<Result>{

    public ResultsAdapter(@NonNull Context context, List<Result> results) {
        super(context, 0, results);
    }

    @SuppressLint("SetTextI18n")
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
        resultItemTitle.setPaintFlags(resultItemTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String type = result.getType();
        if (type.equals("music"))
            resultItemType.setText("Musical Artist");
        else if (type.equals("movie"))
            resultItemType.setText("Movie");
        else if (type.equals("show"))
            resultItemType.setText("TV show");

        return convertView;
    }

}


