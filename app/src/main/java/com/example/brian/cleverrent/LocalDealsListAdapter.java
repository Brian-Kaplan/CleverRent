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
public class LocalDealsListAdapter extends ArrayAdapter<LocalDealsListAdapter.Deal> {

    Deal[] deals = {};
    Context c;
    LayoutInflater inflater;

    public LocalDealsListAdapter(Context context, Deal[] deals) {
        super(context, R.layout.local_deal_cell_model, deals);
        this.deals = deals;
        this.c = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.local_deal_cell_model, null);
        }

        //Make a ViewHolder object
        final DealViewHolder holder = new DealViewHolder();
        final Deal myDeal = deals[position];

        //Initialize the view
        holder.location = (TextView) convertView.findViewById(R.id.dealLocationLabel);
        holder.percentOff = (TextView) convertView.findViewById(R.id.dealPercentOffLabel);
        holder.imageThumb = (ImageView) convertView.findViewById(R.id.dealImageThumb);
        holder.date = (TextView) convertView.findViewById(R.id.dealDateLabel);
        holder.dealTitle = (TextView) convertView.findViewById(R.id.dealTitleLabel);

        //Assign the data
        holder.location.setText("("+myDeal.dealLocation+")");
        holder.percentOff.setText(myDeal.dealPercentOff + " Off");
        Picasso.with(c).load(myDeal.imgeUrl).into(holder.imageThumb);
        holder.date.setText(myDeal.dealDate);
        holder.dealTitle.setText(myDeal.dealTitle);

        return convertView;
    }

    private class DealViewHolder
    {
        TextView location;
        TextView percentOff;
        ImageView imageThumb;
        TextView date;
        TextView dealTitle;
    }

    public static class Deal{
        String dealLocation;
        String dealPercentOff;
        String imgeUrl;
        String dealDate;
        String dealTitle;

        public Deal(String location, String percentOff, String imgeUrl, String date, String dealTitle) {
            this.dealLocation = location;
            this.dealPercentOff = percentOff;
            this.imgeUrl = imgeUrl;
            this.dealDate = date;
            this.dealTitle = dealTitle;
        }
    }
}


