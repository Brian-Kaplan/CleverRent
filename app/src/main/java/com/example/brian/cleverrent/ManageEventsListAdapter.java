package com.example.brian.cleverrent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.brian.cleverrent.EventsListAdapter.Event;

import java.util.ArrayList;

/**
 * Created by brian on 2/2/16.
 */
public class ManageEventsListAdapter extends ArrayAdapter<Event> {

    ArrayList<Event> events;
    Context c;
    LayoutInflater inflater;

    public ManageEventsListAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.manage_events_cell_model, events);
        this.c = context;
        this.events = events;
    }

    //Inner Class to hold views for each row
    private class ViewHolder
    {
        TextView title;
        TextView interestCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.manage_events_cell_model, null);
        }

        //Make a ViewHolder object
        final ViewHolder holder = new ViewHolder();

        //Initialize the view
        holder.title = (TextView) convertView.findViewById(R.id.manageEventTitleLabel);
        holder.interestCount = (TextView) convertView.findViewById(R.id.manageEventCountLabel);

        //Assign the data
        holder.title.setText(events.get(position).getEventTitle());
        holder.interestCount.setText(String.valueOf(events.get(position).getInterestCount()));


        convertView.setMinimumHeight(300);
        return convertView;
    }
}
