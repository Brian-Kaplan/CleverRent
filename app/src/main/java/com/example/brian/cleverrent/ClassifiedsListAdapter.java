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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by brian on 2/15/16.
 */
public class ClassifiedsListAdapter extends ArrayAdapter<ClassifiedsListAdapter.ClassifiedPost> {

    ArrayList<ClassifiedPost> posts = null;
    Context c;
    LayoutInflater inflater;

    public ClassifiedsListAdapter(Context context, ArrayList<ClassifiedPost> posts) {
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
        final ClassifiedPost myPost = posts.get(position);

        //Initialize the view
        holder.postLocation = (TextView) convertView.findViewById(R.id.classifiedPostLocationLabel);
        holder.postPrice = (TextView) convertView.findViewById(R.id.classifiedPostPriceLabel);
        holder.imageThumb = (ImageView) convertView.findViewById(R.id.classifiedImageThumb);
        holder.postDate = (TextView) convertView.findViewById(R.id.classifiedPostDateLabel);
        holder.postTitle = (TextView) convertView.findViewById(R.id.classifiedPostTitleLabel);

        //Assign the data
        holder.postLocation.setText("("+myPost.postType+")");
        holder.postPrice.setText(myPost.postPrice);
        String encodedImage = myPost.getImgeUrl();
        byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        holder.imageThumb.setImageBitmap(bitmap);
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
        String postType;
        String postTitle;
        String postDescription;
        String postCondition;
        String postPrice;
        String imgeUrl;
        String fullName;
        String postPhone;
        String postEmail;
        String identifier;
        String postDate;
        int interestCount;

        public ClassifiedPost() {}

        public ClassifiedPost(String postType, String postTitle,
                              String postDescription, String postCondition,
                              String postPrice, String imgeUrl,
                              String fullName, String postPhone,
                              String postEmail , String identifier) {

            this.postType = postType;
            this.postTitle = postTitle;
            this.postDescription = postDescription;
            this.postCondition = postCondition;
            this.postPrice = postPrice;
            this.imgeUrl = imgeUrl;
            this.fullName = fullName;
            this.postPhone = postPhone;
            this.postEmail = postEmail;
            this.identifier = identifier;
            Calendar now = Calendar.getInstance();
            Locale locale = Locale.getDefault();
            String month = now.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
            int day = now.get(Calendar.DAY_OF_MONTH);
            this.postDate = month + " " + Integer.toString(day); //TODO Generate the date somehow
            this.interestCount = 0;
        }

        public String getPostDate() { return postDate; }

        public String getPostType() {
            return postType;
        }

        public String getPostTitle() {return postTitle;}

        public String getImgeUrl() {return imgeUrl;}

        public String getPostPrice() {
            return postPrice;
        }

        public String getIdentifier() {return identifier;}

        public int getInterestCount() {return interestCount;}

        public String getPostDescription() {return postDescription;}

        public String getPostCondition() {return postCondition;}

        public String getPostPhone() {return postPhone;}

        public String getFullName() {return fullName;}

        public String getPostEmail() {return postEmail;}

        @Override
        public boolean equals(Object o) {
            if (o instanceof ClassifiedPost){
                ClassifiedPost temp = (ClassifiedPost) o;
                if (temp.getPostTitle().equals(this.getPostTitle())){
                    return true;
                }
            }
            return false;
        }
    }
}

