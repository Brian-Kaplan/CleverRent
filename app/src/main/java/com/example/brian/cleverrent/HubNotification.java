package com.example.brian.cleverrent;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by brian on 3/12/16.
 * A sub region of the hub notifications.
 * They are kind of sorted by date so every instance of this class will have notifications of the same date
 */
public class HubNotification {

    String date;
    ArrayList<NotificationObject> notificationObjectList = null;

    public HubNotification(String date) {
        this.date = date;
        this.notificationObjectList = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }

    public ArrayList<NotificationObject> getNotificationObjectList() {
        return notificationObjectList;
    }

    public void addNotificationObject (NotificationObject object) {

        //If the list is empty add one.
        if (notificationObjectList.size() == 0){
            notificationObjectList.add(object);
        } else {
            //Loop the list and make the most recent chat notification the only chat notification
            for (int i =0; i < notificationObjectList.size(); i++) {
                NotificationObject temp = notificationObjectList.get(i);
                if (temp.equals(object) && object.getType().equals("CHAT")) {
                    if (object.isMoreRecent(temp))
                        notificationObjectList.set(i, object);
                } else {
                    notificationObjectList.add(object);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof HubNotification){
            HubNotification compareTo = (HubNotification) o;
            if (this.getDate().equals(compareTo.getDate()))
                return true;
        }
        return false;
    }
}
