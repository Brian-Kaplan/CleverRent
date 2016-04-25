package com.example.brian.cleverrent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.example.brian.cleverrent.EventsListAdapter.Event;
import com.example.brian.cleverrent.ClassifiedsListAdapter.ClassifiedPost;

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

        SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        final String userName = sharedPref.getString("USER_NAME", null);
        final String displayName = sharedPref.getString("DISPLAY_NAME", null);

        if(bundle != null)
        {
            final String listingType = (String) bundle.get("LISTING_TYPE");
            final String listingIdentifier = (String) bundle.get("LISTING_IDENTIFIER");
            Firebase ref = new Firebase(MainActivity.getFirebaseRootRef()+listingType+"/"+listingIdentifier);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    switch (listingType) {

                        case "facility": {
                            RelativeLayout view = (RelativeLayout) findViewById(R.id.facilityView);
                            view.setVisibility(View.VISIBLE);

                            final ImageView imageView = (ImageView) findViewById(R.id.facilityListingViewImageView);
                            final TextView descriptionLabel = (TextView) findViewById(R.id.facilityListingViewDescriptionLabel);
                            final TextView hoursLabel = (TextView) findViewById(R.id.facilityListingViewHoursLabel);
                            final TextView statusLabel = (TextView) findViewById(R.id.facilityListingViewStatusLabel);
                            final FacilitiesListAdapter.Facility facility = dataSnapshot.getValue(FacilitiesListAdapter.Facility.class);
                            getSupportActionBar().setTitle(facility.getFacility_name());
                            String encodedImage = facility.getFacility_image();
                            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            imageView.setImageBitmap(bitmap);
                            descriptionLabel.setText(facility.getFacility_description());
                            hoursLabel.setText(facility.getFacility_hours());
                            statusLabel.setText(facility.getFacility_status());
                            break;
                        }

                        case "events": {
                            //Set the event view to be invisibile
                            RelativeLayout view = (RelativeLayout) findViewById(R.id.eventView);
                            view.setVisibility(View.VISIBLE);

                            final ImageView imageView = (ImageView) findViewById(R.id.eventListingViewImageView);
                            final TextView descriptionLabel = (TextView) findViewById(R.id.eventListingViewDescriptionLabel);
                            final TextView ageLabel = (TextView) findViewById(R.id.eventListingViewAgeLabel);
                            final TextView locationLabel = (TextView) findViewById(R.id.eventListingViewLocationLabel);
                            final TextView dateLabel = (TextView) findViewById(R.id.eventListingViewTimeLabel);
                            final TextView priceLabel = (TextView) findViewById(R.id.eventListingViewPriceLabel);
                            final TextView sponsorLabel = (TextView) findViewById(R.id.eventListingViewSponsorLabel);
                            final Button rsvp = (Button) findViewById(R.id.eventListingRSVPButton);
                            final Button chat = (Button) findViewById(R.id.eventListingChatButton);
                            final Button showInterest = (Button) findViewById(R.id.eventListingInterestButton);

                            final EventsListAdapter.Event event = dataSnapshot.getValue(EventsListAdapter.Event.class);
                            getSupportActionBar().setTitle(event.getEventTitle());
                            String encodedImage = event.getImageUrl();
                            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            imageView.setImageBitmap(bitmap);
                            descriptionLabel.setText(event.getEventDescription());
                            ageLabel.setText(event.getEventAge());
                            locationLabel.setText(event.getEventLocation());
                            dateLabel.setText(event.getEventDate());
                            priceLabel.setText(event.getEventCost());
                            sponsorLabel.setText(event.getHostName());
                            //If the user owns this event disable all the buttons
                            if (event.getEventOwner().equals(userName)) {
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
                                    Firebase ref = new Firebase(MainActivity.getFirebaseRootRef() + listingType + "/" + listingIdentifier + "/chatInstances/" + userName);
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
                            break;
                        }
                        case "classifieds": {
                            //Set the event view to be invisibile
                            RelativeLayout view = (RelativeLayout) findViewById(R.id.classifiedView);
                            view.setVisibility(View.VISIBLE);

                            final ImageView imageView = (ImageView) findViewById(R.id.classifiedsListingViewImageView);
                            final TextView descriptionLabel = (TextView) findViewById(R.id.classifiedsListingViewDescriptionLabel);
                            final TextView conditionLabel = (TextView) findViewById(R.id.classifiedsListingViewConditionLabel);
                            final TextView priceLabel = (TextView) findViewById(R.id.classifiedsListingViewPriceLabel);
                            final TextView ownerLabel = (TextView) findViewById(R.id.classifiedsListingViewOwnerLabel);
                            final TextView dateLabel = (TextView) findViewById(R.id.eventListingViewTimeLabel);
                            final Button chat = (Button) findViewById(R.id.classifiedsListingChatButton);

                            final ClassifiedPost post = dataSnapshot.getValue(ClassifiedPost.class);
                            getSupportActionBar().setTitle(post.getPostTitle());
                            String encodedImage = post.getImageUrl();
                            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            imageView.setImageBitmap(bitmap);
                            descriptionLabel.setText(post.getPostDescription());
                            conditionLabel.setText(post.getPostCondition());
                            priceLabel.setText(post.getPostPrice());
                            ownerLabel.setText(post.getFullName());
                            dateLabel.setText(post.getPostDate());
                            if (post.getIdentifier().split("-")[1].equals(userName)) {
                                chat.setEnabled(false);
                            }

                            chat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Firebase ref = new Firebase(MainActivity.getFirebaseRootRef() + listingType + "/" + listingIdentifier + "/chatInstances/" + userName);
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() == null) {
                                                createClassifiedChat(ref, displayName, userName, post);
                                            }
                                            Intent intent = new Intent(CommunityListingViewActivity.this, ChatActivity.class);
                                            intent.putExtra("LISTING_TYPE", listingType);
                                            intent.putExtra("LISTING_IDENTIFIER", listingIdentifier);
                                            intent.putExtra("CHAT_IDENTIFIER", userName);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }
                            });
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

    }

    private void createClassifiedChat(Firebase ref, String displayName, final String userName, final ClassifiedPost post) {
        ChatInstance chatInstance = new ChatInstance(post.getIdentifier().split("-")[1], userName, userName);
        post.getChatInstances().put(userName, chatInstance);
        ref.getParent().setValue(post.getChatInstances());
    }

    private void onShowInterest (final String displayName, final String listingType, final String listingIdentifier, final String userName, final EventsListAdapter.Event event) {
        final Firebase ref = new Firebase(MainActivity.getFirebaseRootRef() + listingType+"/"+listingIdentifier);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                if (dataSnapshot.child("chatInstances").child(userName).getValue() == null) {
                    ChatInstance chatInstance = new ChatInstance(event.getEventOwner(), userName, userName);
                    ChatEvent expressInterest = new ChatEvent("Friday, March 25", displayName.split(" ")[0] + " Expressed Interest");
                    chatInstance.addChatEvent(expressInterest);
                    event.getChatInstances().put(userName, chatInstance);
                    event.getInterestedList().add(userName);
                    ref.child("chatInstances").setValue(event.getChatInstances());
                    ref.child("interestedList").setValue(event.getInterestedList());
                    NotificationObject notificationObject = new NotificationObject(displayName, "Events: " + event.getEventTitle(), "CHAT", event.getEventOwner(), chatInstance, listingType, listingIdentifier);
                    postChatNotification(notificationObject);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void onRSVP(final String displayName, final String listingType, final String listingIdentifier, final String userName, final EventsListAdapter.Event event) {
        final Firebase ref = new Firebase(MainActivity.getFirebaseRootRef() + listingType+"/"+listingIdentifier);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                ArrayList<String> rsvpList = event.getRsvpList();
                if (!rsvpList.contains(userName)) {
                    ChatInstance chatInstance;
                    if (dataSnapshot.child("chatInstances").child(userName).getValue() == null) {
                        chatInstance = new ChatInstance(event.getEventOwner(), userName, userName);
                    } else {
                        chatInstance = event.getChatInstances().get(userName);
                    }
                    ArrayList<ChatEvent> chatEventTimeline = chatInstance.getChatEventTimeline();
                    ChatEvent rsvp = new ChatEvent("Friday, March 25", displayName.split(" ")[0] + " RSVP'd");
                    chatEventTimeline.add(rsvp);
                    event.getChatInstances().put(userName, chatInstance);
                    event.getRsvpList().add(userName);
                    ref.child("chatInstances").setValue(event.getChatInstances());
                    ref.child("rsvpList").setValue(event.getRsvpList());
                    NotificationObject notificationObject = new NotificationObject(displayName, "Events: " + event.getEventTitle(), "CHAT", event.getEventOwner(), chatInstance, listingType, listingIdentifier);
                    postChatNotification(notificationObject);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void postChatNotification(final NotificationObject notificationObject){
        String date = notificationObject.getNotifDate();
        String owner = notificationObject.getOwner();
        final Firebase ref = new Firebase(MainActivity.getFirebaseRootRef() + "notifications/" + owner + "/" + date);
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
