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
public class ClassifiedsListAdapter extends ArrayAdapter<ClassifiedsListAdapter.ClassifiedPost> {

    ClassifiedPost[] posts = {};
    Context c;
    LayoutInflater inflater;

    public ClassifiedsListAdapter(Context context, ClassifiedPost[] posts) {
        super(context, R.layout.classifieds_cell_model, posts);
        this.posts = posts;
        this.c = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.classifieds_cell_model, null);
        }

        //Make a ViewHolder object
        final ClassifiedsViewHolder holder = new ClassifiedsViewHolder();
        final ClassifiedPost myPost = posts[position];

        //Initialize the view
        holder.postLocation = (TextView) convertView.findViewById(R.id.classifiedPostLocationLabel);
        holder.postPrice = (TextView) convertView.findViewById(R.id.classifiedPostPriceLabel);
        holder.imageThumb = (ImageView) convertView.findViewById(R.id.classifiedImageThumb);
        holder.postDate = (TextView) convertView.findViewById(R.id.classifiedPostDateLabel);
        holder.postTitle = (TextView) convertView.findViewById(R.id.classifiedPostTitleLabel);

        //Assign the data
        holder.postLocation.setText("("+myPost.postLocation+")");
        holder.postPrice.setText(myPost.postPrice);
        Picasso.with(c).load(myPost.imgeUrl).into(holder.imageThumb);
        holder.postDate.setText(myPost.postDate);
        holder.postTitle.setText(myPost.postTitle);

        return convertView;
    }

    private class ClassifiedsViewHolder
    {
        TextView postLocation;
        TextView postPrice;
        ImageView imageThumb;
        TextView postDate;
        TextView postTitle;
    }

    public static class ClassifiedPost{
        String postLocation;
        String postTitle;
        String imgeUrl;
        String postDate;
        String postPrice;

        public ClassifiedPost(String postLocation, String postPrice, String imgeUrl, String postDate, String postTitle) {
            this.postLocation = postLocation;
            this.postPrice = postPrice;
            this.imgeUrl = imgeUrl;
            this.postDate = postDate;
            this.postTitle = postTitle;
        }
    }
}


