package com.example.ankush.activity1.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ankush.activity1.R;
import com.example.ankush.activity1.models.PointOfInterest;

import java.util.ArrayList;
import java.util.List;

public class POIAdapter extends ArrayAdapter<PointOfInterest> {

    private ArrayList<PointOfInterest> m_List;

    private Activity activity;

    private static LayoutInflater inflater = null;

    public POIAdapter(Activity activity, int resource, List<PointOfInterest> objects) {
        super(activity, resource, objects);
        this.m_List = (ArrayList<PointOfInterest>) objects;
        this.activity = activity;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View ConvertView, ViewGroup parent) {

        //Get the item to be displayed
        PointOfInterest poi = getItem(position);

        // Object to hold the view of the item layout
        ViewHolder viewHolder;

        // The final view returned for the list
        final View result;

        // If the view is not created for the list item, create a new one from the layout specified
        if(ConvertView == null) {
            viewHolder = new ViewHolder();
            ConvertView = inflater.inflate(R.layout.content_list_item, parent, false);
            viewHolder.titleView = ConvertView.findViewById(R.id.titleView);
            viewHolder.descView = ConvertView.findViewById(R.id.descriptionView);

            viewHolder.titleView.setText(poi.getTitle());
            viewHolder.descView.setText(poi.getDescription());

            result = ConvertView;

            ConvertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) ConvertView.getTag();
            result = ConvertView;
        }

        // Set the text for the views in the list item
        viewHolder.titleView.setText(poi.getTitle());
        viewHolder.descView.setText(poi.getDescription());

        return result;
    }

    // To hold the views of the list item
    private static class ViewHolder {
        public TextView titleView;
        public TextView descView;
    }

}
