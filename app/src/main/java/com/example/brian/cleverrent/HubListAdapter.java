package com.example.brian.cleverrent;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by brian on 3/12/16.
 */
public class HubListAdapter extends ArrayAdapter<HubNotification> {


    ArrayList<HubNotification> notifications;
    Context c;
    LayoutInflater inflater;

    public HubListAdapter(Context context, ArrayList<HubNotification> notifications) {
        super(context, R.layout.notification_hub_cell_model, notifications);
        this.c = context;
        this.notifications = notifications;
    }

    private class ViewHolder
    {
        LinearLayout notificationObjectLayout;
        TextView dateLabel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.notification_hub_cell_model, null);
        }

        //Make a ViewHolder object
        final ViewHolder holder = new ViewHolder();

        //Initialize the view
        holder.notificationObjectLayout = (LinearLayout) convertView.findViewById(R.id.notificationObjectLayout);
        holder.notificationObjectLayout.removeAllViews();
        holder.dateLabel = (TextView) convertView.findViewById(R.id.hubListDateTimeLabel);
        //Assign the data

        ArrayList<NotificationObject> list = notifications.get(position).getNotificationObjectList();

        for (NotificationObject notificationObject : list){
            final View notificationView = inflater.inflate(R.layout.notifcation_object_cell_model, null);

            notificationView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        notificationView.setBackgroundColor(Color.parseColor("#838383"));
                    else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        notificationView.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    return true;
                }
            });

            TextView title = (TextView) notificationView.findViewById(R.id.notificationObjectTitleLabel);
            TextView description = (TextView) notificationView.findViewById(R.id.notificationObjecDescriptionLabel);
            TextView time = (TextView) notificationView.findViewById(R.id.notificationObjecTimeLabel);
            ImageView icon = (ImageView) notificationView.findViewById(R.id.notificationObjectImageView);


            if (notificationObject.getType().equals("CHAT"))
                icon.setImageResource(R.mipmap.ic_iconmonstr_speech_bubble_20_240);
            if (notificationObject.getType().equals("REMINDER"))
                icon.setImageResource(R.mipmap.ic_iconmonstr_bell_8_240);
            title.setText(notificationObject.getTitle());
            description.setText(notificationObject.getDescription());
            time.setText(notificationObject.getTimeStamp());
            holder.notificationObjectLayout.addView(notificationView);
        }


        holder.dateLabel.setText(notifications.get(position).getDate());

        return convertView;
    }
}
