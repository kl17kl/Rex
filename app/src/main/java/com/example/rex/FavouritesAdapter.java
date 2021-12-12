package com.example.rex;

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
 * This class acts as an Adapter for the Favourites listView display. When the user adds an item
 * (artist/movie/show) to their favourites list, and when the user opens up their favourites list,
 * this adapter displays each item with its name, type, and description.
 */

public class FavouritesAdapter extends ArrayAdapter<Result>{

    public FavouritesAdapter(@NonNull Context context, List<Result> results) {
        super(context, 0, results);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Result result = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favourite_item, parent, false);

        // Initialize widgets
        TextView favouriteItemTitle = convertView.findViewById(R.id.favouriteItemTitle);
        TextView favouriteItemDesc = convertView.findViewById(R.id.favouriteItemDesc);

        // Display the item's name and description
        favouriteItemTitle.setText(result.getName());
        favouriteItemDesc.setText(result.getDescription());

        return convertView;
    }

}

