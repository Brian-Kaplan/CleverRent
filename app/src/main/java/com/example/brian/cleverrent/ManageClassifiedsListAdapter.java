package com.example.brian.cleverrent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.brian.cleverrent.ClassifiedsListAdapter.ClassifiedPost;

import java.util.ArrayList;

/**
 * Created by brian on 2/2/16.
 */
public class ManageClassifiedsListAdapter extends ArrayAdapter<ClassifiedPost> {

    ArrayList<ClassifiedPost> posts;
    Context c;
    LayoutInflater inflater;

    public ManageClassifiedsListAdapter(Context context, ArrayList<ClassifiedPost> posts) {
        super(context, R.layout.manage_classifieds_cell_model, posts);
        this.c = context;
        this.posts = posts;
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
            convertView = inflater.inflate(R.layout.manage_classifieds_cell_model, null);
        }

        //Make a ViewHolder object
        final ViewHolder holder = new ViewHolder();

        //Initialize the view
        holder.title = (TextView) convertView.findViewById(R.id.manageClassifiedsTitleLabel);
        holder.interestCount = (TextView) convertView.findViewById(R.id.manageClassifiedsCountLabel);

        //Assign the data
        holder.title.setText(posts.get(position).getPostTitle());
        holder.interestCount.setText(String.valueOf(posts.get(position).getInterestCount()));


        convertView.setMinimumHeight(200);
        return convertView;
    }
}
