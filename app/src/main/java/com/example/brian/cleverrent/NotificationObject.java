package com.example.brian.cleverrent;

/**
 * Created by brian on 3/12/16.
 * A Single Notification Representation
 */
public class NotificationObject {

    private String notifDate;
    private String title;
    private String description;
    private String timeStamp;
    private String type; //Notication Object Type
    private String owner;
    private ChatInstance chatInstance;
    private String listingType;
    private String listingIdentifier;

    public NotificationObject() {}

    public NotificationObject(String title, String description, String type, String owner, ChatInstance chatInstance, String listingType, String listingIdentifier) {
        this.notifDate = MainActivity.getTodaysDate();
        this.title = title;
        this.description = description;
        this.timeStamp = MainActivity.getTimeStamp();
        this.type = type;
        this.owner = owner;
        this.chatInstance = chatInstance;
        this.listingType = listingType;
        this.listingIdentifier = listingIdentifier;
    }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getTimeStamp() { return timeStamp; }

    public String getType() {
        return type;
    }

    public String getNotifDate() {
        return notifDate;
    }

    public String getOwner() { return owner; }

    public String getListingIdentifier() { return listingIdentifier; }

    public String getListingType() { return listingType; }

    public ChatInstance getChatInstance() { return chatInstance; }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NotificationObject){
            NotificationObject temp = (NotificationObject) o;
            if (this.getTitle().equals(temp.getTitle()) && this.getTimeStamp().equals(temp.getTimeStamp())){
                return true;
            }
        }
        return false;
    }
}
