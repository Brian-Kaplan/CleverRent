package com.example.brian.cleverrent;

/**
 * Created by brian on 3/12/16.
 */
public class NotificationObject {

    String notifDate;
    String title;
    String description;
    String timeStamp;
    String type; //Notication Object Type

    public NotificationObject() {}

    public NotificationObject(String notifDate, String title, String description, String timeStamp, String type) {
        this.notifDate = notifDate;
        this.title = title;
        this.description = description;
        this.timeStamp = timeStamp;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getType() {
        return type;
    }

    public String getNotifDate() {
        return notifDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NotificationObject){
            NotificationObject temp = (NotificationObject) o;
            if (this.getTitle().equals(temp.getTitle())){
                return true;
            }
        }
        return false;
    }
}
