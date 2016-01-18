package com.example.deepak.simpletodo;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomItemListAdapter extends ArrayAdapter<Item> {

    public CustomItemListAdapter(Context context, ArrayList<Item> listViewItems) {
        super(context, 0, listViewItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_list_view, parent, false);
        }
        TextView todoLabel = (TextView) convertView.findViewById(R.id.todoLabel);
        TextView priorityLevel = (TextView) convertView.findViewById(R.id.priorityLevel);

        // Populate the data into the template view using the data object
        todoLabel.setText(item.label);
        priorityLevel.setText(item.priorityLevel);

        if(item.priorityLevel.equalsIgnoreCase("low")){
            priorityLevel.setTextColor(Color.parseColor("#26cba3"));
        } else if(item.priorityLevel.equalsIgnoreCase("medium")) {
            priorityLevel.setTextColor(Color.parseColor("#eaa456"));
        } else {
            priorityLevel.setTextColor(Color.parseColor("#c85475"));
        }

        todoLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        priorityLevel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        // Return the completed view to render on screen
        return convertView;
    }
}