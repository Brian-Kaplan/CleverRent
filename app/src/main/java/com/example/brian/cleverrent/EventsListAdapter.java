package com.example.brian.cleverrent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brian on 2/15/16.
 */
public class EventsListAdapter extends ArrayAdapter<EventsListAdapter.Event> {

    ArrayList<Event> events = null;
    Context c;
    LayoutInflater inflater;

    public EventsListAdapter(Context context, ArrayList<Event> events) {
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
        final Event myEvent = events.get(position);

        //Initialize the view
        holder.eventLocationLabel = (TextView) convertView.findViewById(R.id.eventLocationLabel);
        holder.eventAgeLabel = (TextView) convertView.findViewById(R.id.eventAgeLabel);
        holder.imageThumb = (ImageView) convertView.findViewById(R.id.eventImageThumb);
        holder.eventDateLabel = (TextView) convertView.findViewById(R.id.eventDateLabel);
        holder.eventTitleLabel = (TextView) convertView.findViewById(R.id.eventTitleLabel);

        //Assign the data
        holder.eventLocationLabel.setText("("+myEvent.eventLocation+")");
        holder.eventAgeLabel.setText(myEvent.eventAge);
        holder.eventDateLabel.setText(myEvent.eventDate);
        holder.eventTitleLabel.setText(myEvent.eventTitle);
        String encodedImage = myEvent.getImageUrl();

        if (encodedImage != null) {
            byte[]b = null;
            Bitmap bitmap = null;
            try {
                bitmap = MainActivity.getBitmap(myEvent.getIdentifier());
                if (bitmap == null) {
                    b = Base64.decode(encodedImage, Base64.DEFAULT);
                    bitmap = decodeSampledBitmapFromByteArray(b, 800, 600);
                    MainActivity.putBitmap(myEvent.getIdentifier(), bitmap);
                }
            } catch (Exception e) {
                Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_image_available);
                holder.imageThumb.setImageBitmap(bm);
                return convertView;
            }
            holder.imageThumb.setImageBitmap(bitmap);
        } else {
            Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_image_available);
            holder.imageThumb.setImageBitmap(bm);
        }
        return convertView;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromByteArray(byte[] b, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeByteArray(b, 0, b.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(res, resId, options);
        return BitmapFactory.decodeByteArray(b, 0, b.length, options);
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
        String eventDescription;
        String eventLocation;
        String eventAge;
        String eventTime;
        String imageUrl;
        String eventDate;
        String eventTitle;
        String eventCost;
        String eventOwner;
        String hostName;
        String identifier;
        HashMap<String, ChatInstance> chatInstances;
        ArrayList<String> interestedList;
        ArrayList<String> rsvpList;
        int interestCount;

        public Event() {}

        public Event(String eventDescription, String eventLocation, String eventAge, String eventTime, String eventCost, String imageUrl, String eventDate, String eventTitle, String hostName, String eventOwner, String identifier) {
            this.eventDescription = eventDescription;
            this.eventLocation = eventLocation;
            this.eventAge = eventAge;
            this.eventTime = eventTime;
            this.eventCost = eventCost;
            this.imageUrl = imageUrl;
            this.eventDate = eventDate;
            this.eventTitle = eventTitle;
            this.hostName = hostName;
            this.eventOwner = eventOwner;
            this.identifier = identifier;
            this.interestCount = 0;
            this.chatInstances = new HashMap<>();
            this.interestedList = new ArrayList<>();
            this.rsvpList = new ArrayList<>();
        }

        public String getEventOwner() {
            return eventOwner;
        }

        public ArrayList<String> getInterestedList() {
            if (interestedList == null) {
                interestedList = new ArrayList<>();
            }
            return interestedList;
        }

        public ArrayList<String> getRsvpList() {
            if (rsvpList == null) {
                rsvpList = new ArrayList<>();
            }
            return rsvpList;
        }

        public String getEventCost() {return eventCost;}

        public String getEventTime() {return eventTime;}

        public String getEventDescription() {
            System.out.println();
            return eventDescription;
        }

        public String getEventLocation() { return eventLocation; }

        public String getEventAge() { return eventAge; }

        public String getImageUrl() { return imageUrl; }

        public String getEventDate() { return eventDate; }

        public String getEventTitle() {return eventTitle;}

        public String getHostName() { return hostName; }

        public String getIdentifier() { return identifier; }

        public int getInterestCount() {
            return interestCount;
        }

        public void incrementInterestCount() { interestCount++; }

        public HashMap<String, ChatInstance> getChatInstances() {
            if (chatInstances == null) {
                chatInstances = new HashMap<String, ChatInstance>();
            }
            System.out.println();
            return chatInstances;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Event){
                Event temp = (Event) o;
                if (temp.getEventTitle().equals(this.getEventTitle())){
                    return true;
                }
            }
            return false;
        }
    }
}


