package com.example.brian.cleverrent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class CommunityListingViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_listing_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        final ImageView imageView = (ImageView) findViewById(R.id.communityListingViewImageView);
        final TextView descriptionLabel = (TextView) findViewById(R.id.communityListingViewDescriptionLabel);
        final TextView ageLabel = (TextView) findViewById(R.id.communityLisitingViewAgeLabel);
        final TextView locationLabel = (TextView) findViewById(R.id.communityLisitingViewLocationLabel);
        final TextView dateLabel = (TextView) findViewById(R.id.communityLisitingViewTimeLabel);
        final TextView priceLabel = (TextView) findViewById(R.id.communityLisitingViewPriceLabel);
        final TextView sponsorLabel = (TextView) findViewById(R.id.communityLisitingViewSponsorLabel);

        if(bundle != null)
        {
            final String listingType = (String) bundle.get("LISTING_TYPE");
            final String listingIdentifier = (String) bundle.get("LISTING_IDENTIFIER");
            Firebase ref = new Firebase("https://cleverrent.firebaseio.com/"+listingType+"/"+listingIdentifier);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    switch (listingType) {
                        case "events":
                            final EventsListAdapter.Event event = dataSnapshot.getValue(EventsListAdapter.Event.class);
                            getSupportActionBar().setTitle(event.getEventTitle());
                            String encodedImage = event.getImgeUrl();
                            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            imageView.setImageBitmap(bitmap);
                            descriptionLabel.setText(event.getEventDescription());
                            ageLabel.setText(event.getEventAge());
                            locationLabel.setText(event.getEventLocation());
                            dateLabel.setText(event.getEventDate());
                            priceLabel.setText(event.getEventCost());
                            sponsorLabel.setText(event.getHostName());
                            SharedPreferences sharedPref  = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                            final String userName = sharedPref.getString("USER_NAME", null);
                            final String displayName = sharedPref.getString("DISPLAY_NAME", null);
                            final Button rsvp = (Button) findViewById(R.id.communityListingRSVPButton);
                            final Button chat = (Button) findViewById(R.id.communityListingChatButton);
                            final Button showInterest = (Button) findViewById(R.id.communityListingInterestButton);
                            //If the user owns this event disable all the buttons
                            if (event.getHostName().equals(userName)){
                                rsvp.setEnabled(false);
                                chat.setEnabled(false);
                                showInterest.setEnabled(false);
                            }

                            showInterest.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onShowInterest(displayName, listingType, listingIdentifier, userName, event);
                                }
                            });

                            rsvp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onRSVP(displayName, listingType, listingIdentifier, userName, event);
                                }
                            });

                            chat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Make a new chat instance if it doesn't exist. Give the chat identifier to the ChatActivity
                                    Firebase ref = new Firebase("https://cleverrent.firebaseio.com/"+listingType+"/"+listingIdentifier+"/chatInstances/"+userName);
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            onShowInterest(displayName, listingType, listingIdentifier, userName, event);
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });

                                    Intent intent = new Intent(CommunityListingViewActivity.this, ChatActivity.class);
                                    intent.putExtra("LISTING_TYPE", listingType);
                                    intent.putExtra("LISTING_IDENTIFIER", listingIdentifier);
                                    intent.putExtra("CHAT_IDENTIFIER", userName);
                                    startActivity(intent);
                                }
                            });

                        case "classifieds":
                            break;
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

    }

    private void onShowInterest (final String displayName, final String listingType, final String listingIdentifier, final String userName, final EventsListAdapter.Event event) {
        Firebase ref = new Firebase("https://cleverrent.firebaseio.com/"+listingType+"/"+listingIdentifier+"/chatInstances/"+userName);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    ChatInstance chatInstance = new ChatInstance(event.getHostName(), displayName, userName);
                    ChatEvent expressInterest = new ChatEvent("Friday, March 25", displayName.split(" ")[0] + " Expressed Interest");
                    chatInstance.addChatEvent(expressInterest);
                    Firebase ref = new Firebase("https://cleverrent.firebaseio.com/" + listingType + "/" + listingIdentifier +
                            "/chatInstances" + "/" + chatInstance.getIdentifier());
                    ref.setValue(chatInstance);
                    NotificationObject notificationObject = new NotificationObject(displayName, "Events: " + event.getEventTitle(), "CHAT", event.getHostName(), chatInstance, listingType, listingIdentifier);
                    postChatNotification(notificationObject);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void onRSVP(final String displayName, final String listingType, final String listingIdentifier, final String userName, final EventsListAdapter.Event event) {
        Firebase ref = new Firebase("https://cleverrent.firebaseio.com/"+listingType+"/"+listingIdentifier+"/chatInstances/"+userName);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChatInstance chatInstance;
                ChatEvent rsvp = new ChatEvent("Friday, March 25", displayName.split(" ")[0] + " RSVP'd");
                if (dataSnapshot.getValue() == null) {
                    chatInstance = new ChatInstance(event.getHostName(), displayName, userName);
                    chatInstance.addChatEvent(rsvp);
                } else {
                    chatInstance = dataSnapshot.getValue(ChatInstance.class);
                    ArrayList<ChatEvent> chatEventTimeline = chatInstance.getChatEventTimeline();
                    if (!chatEventTimeline.contains(rsvp)) {
                        chatEventTimeline.add(rsvp);
                    }
                }
                Firebase ref = new Firebase("https://cleverrent.firebaseio.com/" + listingType + "/" + listingIdentifier +
                        "/chatInstances" + "/" + chatInstance.getIdentifier());
                ref.setValue(chatInstance);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void postChatNotification(final NotificationObject notificationObject){
        String date = notificationObject.getNotifDate();
        String owner = notificationObject.getOwner();
        final Firebase ref = new Firebase("https://cleverrent.firebaseio.com/notifications/" + owner + "/" + date);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<NotificationObject> notificationObjectArrayList = new ArrayList<>();
                for (DataSnapshot notification : dataSnapshot.getChildren()){
                    notificationObjectArrayList.add(notification.getValue(NotificationObject.class));
                }
                notificationObjectArrayList.add(notificationObject);
                ref.setValue(notificationObjectArrayList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}