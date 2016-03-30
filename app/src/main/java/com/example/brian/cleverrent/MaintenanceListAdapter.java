package com.example.brian.cleverrent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by brian on 2/2/16.
 */
public class MaintenanceListAdapter extends ArrayAdapter<MaintenanceRequest> {

    ArrayList<MaintenanceRequest> maintenanceRequests;
    Context c;
    LayoutInflater inflater;

    public MaintenanceListAdapter(Context context, ArrayList<MaintenanceRequest> maintenanceRequests) {
        super(context, R.layout.maintenance_cell_model, maintenanceRequests);

        this.c = context;
        this.maintenanceRequests = maintenanceRequests;
    }

    //Inner Class to hold views for each row
    private class ViewHolder
    {
        TextView date;
        TextView status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.maintenance_cell_model, null);
        }

        //Make a ViewHolder object
        final ViewHolder holder = new ViewHolder();

        //Initialize the view
        holder.date = (TextView) convertView.findViewById(R.id.maintenanceDateLabel);
        holder.status = (TextView) convertView.findViewById(R.id.maintenanceStatusLabel);

        //Assign the data
        holder.date.setText(maintenanceRequests.get(position).getTimeOfSubmission());
        holder.status.setText(maintenanceRequests.get(position).getStatus());

        return convertView;
    }
}
