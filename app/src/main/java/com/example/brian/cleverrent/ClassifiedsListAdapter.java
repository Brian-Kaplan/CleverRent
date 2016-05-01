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
import java.util.Calendar;
import java.util.HashMap;
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
        holder.postDate.setText(myPost.postDate);
        holder.postTitle.setText(myPost.postTitle);
        String encodedImage = myPost.getImageUrl();
        if (encodedImage != null) {
            byte[]b = null;
            try {
                b = Base64.decode(encodedImage, Base64.DEFAULT);
            } catch (Exception e) {
                Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_image_available);
                holder.imageThumb.setImageBitmap(bm);
                return convertView;
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            holder.imageThumb.setImageBitmap(bitmap);
        } else {
            Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_image_available);
            holder.imageThumb.setImageBitmap(bm);
        }
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
        String imageUrl;
        String fullName;
        String postOwner;
        String identifier;
        String postDate;
        HashMap<String, ChatInstance> chatInstances;
        ArrayList<String> interestedList;

        public ClassifiedPost() {}

        public ClassifiedPost(String postType, String postTitle,
                              String postDescription, String postCondition,
                              String postPrice, String imageUrl,
                              String fullName, String postOwner, String identifier) {

            this.postType = postType;
            this.postTitle = postTitle;
            this.postDescription = postDescription;
            this.postCondition = postCondition;
            this.postPrice = postPrice;
            this.imageUrl = imageUrl;
            this.fullName = fullName;
            this.postOwner = postOwner;
            this.identifier = identifier;
            Calendar now = Calendar.getInstance();
            Locale locale = Locale.getDefault();
            String month = now.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
            int day = now.get(Calendar.DAY_OF_MONTH);
            this.chatInstances = new HashMap<>();
            this.postDate = month + " " + Integer.toString(day); //TODO Generate the date somehow
            this.interestedList = new ArrayList<>();
        }

        public HashMap<String, ChatInstance> getChatInstances() {
            if (chatInstances == null) {
                chatInstances = new HashMap<String, ChatInstance>();
            }
            return chatInstances;
        }

        public ArrayList<String> getInterestedList() {
            if (interestedList == null) {
                interestedList = new ArrayList<>();
            }
            return interestedList;
        }

        public String getPostDate() { return postDate; }

        public String getPostType() {
            return postType;
        }

        public String getPostOwner() {return postOwner; }

        public String getPostTitle() {return postTitle;}

        public String getImageUrl() {return imageUrl;}

        public String getPostPrice() {
            return postPrice;
        }

        public String getIdentifier() {return identifier;}

        public String getPostDescription() {return postDescription;}

        public String getPostCondition() {return postCondition;}

        public String getFullName() {return fullName;}

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


