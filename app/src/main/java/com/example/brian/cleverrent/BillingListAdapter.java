package com.example.brian.cleverrent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by brian on 2/2/16.
 */
public class BillingListAdapter extends ArrayAdapter<String> {

    String[] ammounts = {};
    String[] status = {};
    Context c;
    LayoutInflater inflater;

    public BillingListAdapter(Context context, String[] ammounts, String[] status) {
        super(context, R.layout.billing_cell_model, ammounts);

        this.c = context;
        this.ammounts = ammounts;
        this.status = status;
    }

    //Inner Class to hold views for each row
    private class ViewHolder
    {
        TextView ammount;
        TextView status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.billing_cell_model, null);
        }

        //Make a ViewHolder object
        final ViewHolder holder = new ViewHolder();

        //Initialize the view
        holder.ammount = (TextView) convertView.findViewById(R.id.billingAmmountLabel);
        holder.status = (TextView) convertView.findViewById(R.id.billingStatusLabel);

        //Assign the data
        holder.ammount.setText(ammounts[position]);
        holder.status.setText(status[position]);

        return convertView;
    }
}
