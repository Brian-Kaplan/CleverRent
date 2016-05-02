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

/**
 * Created by brian on 2/15/16.
 */
public class LocalDealsListAdapter extends ArrayAdapter<LocalDealsListAdapter.Deal> {

    ArrayList<Deal> deals = null;
    Context c;
    LayoutInflater inflater;

    public LocalDealsListAdapter(Context context, ArrayList<Deal> deals) {
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
        final Deal myDeal = deals.get(position);

        //Initialize the view
        holder.deal_description = (TextView) convertView.findViewById(R.id.dealLocationLabel);
        holder.imageThumb = (ImageView) convertView.findViewById(R.id.dealImageThumb);
        holder.deal_name = (TextView) convertView.findViewById(R.id.dealTitleLabel);
        holder.deal_discount = (TextView) convertView.findViewById(R.id.dealDiscountLabel);

        //Assign the data
        holder.deal_discount.setText(myDeal.getDeal_discount());
        holder.deal_description.setText("(" + myDeal.deal_description + ")");
        holder.deal_name.setText(myDeal.deal_name);
        String encodedImage = myDeal.getImageUrl();
        if (encodedImage != null) {
            byte[]b = null;
            Bitmap bitmap = null;
            try {
                bitmap = MainActivity.getBitmap(myDeal.getDeal_name());
                if (bitmap == null) {
                    b = Base64.decode(encodedImage, Base64.DEFAULT);
                    bitmap = decodeSampledBitmapFromByteArray(b, 800, 600);
                    MainActivity.putBitmap(myDeal.getDeal_name(), bitmap);
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

    private class DealViewHolder
    {
        TextView deal_description;
        TextView deal_name;
        ImageView imageThumb;
        TextView deal_discount;
    }

    public static class Deal{
        private String deal_description;
        private String deal_name;
        private String imageUrl;
        private String deal_discount;

        public Deal() {}

        public String getDeal_description() {
            return deal_description;
        }

        public String getDeal_name() {
            return deal_name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getDeal_discount() { return deal_discount; }
    }
}


