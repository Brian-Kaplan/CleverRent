package com.example.brian.cleverrent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by brian on 2/15/16.
 */
public class EventsListAdapter extends ArrayAdapter<EventsListAdapter.Event> {

    Event[] events = {};
    Context c;
    LayoutInflater inflater;

    public EventsListAdapter(Context context, Event[] events) {
        super(context, R.layout.event_cell_model, events);
        this.events = events;
        this.c = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_cell_model, null);
        }

        //Make a ViewHolder object
        final EventViewHolder holder = new EventViewHolder();
        final Event myEvent = events[position];

        //Initialize the view
        holder.eventLocationLabel = (TextView) convertView.findViewById(R.id.eventLocationLabel);
        holder.eventAgeLabel = (TextView) convertView.findViewById(R.id.eventAgeLabel);
        holder.imageThumb = (ImageView) convertView.findViewById(R.id.eventImageThumb);
        holder.eventDateLabel = (TextView) convertView.findViewById(R.id.eventDateLabel);
        holder.eventTitleLabel = (TextView) convertView.findViewById(R.id.eventTitleLabel);

        //Assign the data
        holder.eventLocationLabel.setText("("+myEvent.eventlocation+")");
        holder.eventAgeLabel.setText(myEvent.eventAge);
        Picasso.with(c).load(myEvent.imgeUrl).into(holder.imageThumb);
        holder.eventDateLabel.setText(myEvent.eventdate);
        holder.eventTitleLabel.setText(myEvent.eventTitle);

        return convertView;
    }

    private class EventViewHolder
    {
        TextView eventLocationLabel;
        TextView eventAgeLabel;
        ImageView imageThumb;
        TextView eventDateLabel;
        TextView eventTitleLabel;
    }

    public static class Event{
        String eventlocation;
        String eventAge;
        String imgeUrl;
        String eventdate;
        String eventTitle;
        String hostName;
        String identifier;
        int interestCount;

        public Event() {}

        public Event(String location, String eventAge, String imgeUrl, String eventdate, String eventTitle, String hostName, String identifier) {
            this.eventlocation = location;
            this.eventAge = eventAge;
            this.imgeUrl = imgeUrl;
            this.eventdate = eventdate;
            this.eventTitle = eventTitle;
            this.hostName = hostName;
            this.identifier = identifier;
            this.interestCount = 0;
        }

        public String getEventlocation() { return eventlocation; }

        public String getEventAge() { return eventAge; }

        public String getImgeUrl() { return imgeUrl; }

        public String getEventdate() {
            return eventdate;
        }

        public String getEventTitle() {
            return eventTitle;
        }

        public String getHostName() { return hostName; }

        public String getIdentifier() { return identifier; }

        public int getInterestCount() { return interestCount; }
    }
}


