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
public class FacilitiesListAdapter extends ArrayAdapter<FacilitiesListAdapter.Facility> {

    Facility[] facilities = {};
    Context c;
    LayoutInflater inflater;

    public FacilitiesListAdapter(Context context, Facility[] facilities) {
        super(context, R.layout.facilities_cell_model, facilities);
        this.facilities = facilities;
        this.c = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.facilities_cell_model, null);
        }

        //Make a ViewHolder object
        final FacilityViewHolder holder = new FacilityViewHolder();
        final Facility myFacility = facilities[position];

        //Initialize the view
        holder.location = (TextView) convertView.findViewById(R.id.facilityLocationLabel);
        holder.reservationType = (TextView) convertView.findViewById(R.id.facilityReservationLabel);
        holder.imageThumb = (ImageView) convertView.findViewById(R.id.facilityImageThumb);
        holder.subLocation = (TextView) convertView.findViewById(R.id.facilitySubLocationLabel);

        //Assign the data
        holder.location.setText(myFacility.location);
        holder.reservationType.setText(myFacility.reservationType);
        Picasso.with(c).load(myFacility.imgeUrl).into(holder.imageThumb);
        holder.subLocation.setText("("+myFacility.subLocation+")");

        return convertView;
    }

    private class FacilityViewHolder
    {
        TextView location;
        TextView reservationType;
        ImageView imageThumb;
        TextView subLocation;
    }

    public static class Facility{
        String location;
        String reservationType;
        String imgeUrl;
        String subLocation;

        public Facility(String location, String reservationType, String imgeUrl, String subLocation) {
            this.location = location;
            this.reservationType = reservationType;
            this.imgeUrl = imgeUrl;
            this.subLocation = subLocation;
        }
    }
}


