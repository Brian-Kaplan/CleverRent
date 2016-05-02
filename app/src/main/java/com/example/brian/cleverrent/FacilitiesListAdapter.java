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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by brian on 2/15/16.
 */
public class FacilitiesListAdapter extends ArrayAdapter<FacilitiesListAdapter.Facility> {

    ArrayList<Facility> facilities = null;
    Context c;
    LayoutInflater inflater;

    public FacilitiesListAdapter(Context context, ArrayList<Facility> facilities) {
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
        final Facility myFacility = facilities.get(position);

        //Initialize the view
        holder.facility_name = (TextView) convertView.findViewById(R.id.facilityNameLabel);
        holder.facility_hours = (TextView) convertView.findViewById(R.id.facilityReservationLabel);
        holder.imageThumb = (ImageView) convertView.findViewById(R.id.facilityImageThumb);
        holder.facility_status = (TextView) convertView.findViewById(R.id.facilitySubLocationLabel);

        //Assign the data
        holder.facility_hours.setText(myFacility.getFacility_hours());
        holder.facility_status.setText("("+myFacility.getFacility_status()+")");
        holder.facility_name.setText(myFacility.getFacility_name());

        String encodedImage = myFacility.getFacility_image();
        if (encodedImage != null) {
            byte[]b = null;
            Bitmap bitmap = null;
            try {
                bitmap = MainActivity.getBitmap(myFacility.getFacility_id());
                if (bitmap == null) {
                    b = Base64.decode(encodedImage, Base64.DEFAULT);
                    bitmap = decodeSampledBitmapFromByteArray(b, 800, 600);
                    MainActivity.putBitmap(myFacility.getFacility_id(), bitmap);
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

    private class FacilityViewHolder
    {
        TextView facility_hours;
        ImageView imageThumb;
        TextView facility_status;
        TextView facility_name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Facility{
        private String facility_description;
        private String facility_hours;
        private String facility_name;
        private String facility_image;
        private String facility_status;
        private String facility_id;

        public Facility() {}

        public Facility(String facility_description, String facility_hours, String facility_name, String imgeUrl, String facility_status, String facility_id) {
            this.facility_description = facility_description;
            this.facility_hours = facility_hours;
            this.facility_name = facility_name;
            this.facility_image = imgeUrl;
            this.facility_status = facility_status;
            this.facility_id = facility_id;

        }

        public String getFacility_description() {
            return facility_description;
        }

        public String getFacility_hours() {
            return facility_hours;
        }

        public String getFacility_name() {
            return facility_name;
        }

        public String getFacility_image() {
            return facility_image;
        }

        public String getFacility_status() {
            return facility_status;
        }

        public String getFacility_id() { return facility_id; }
    }
}


