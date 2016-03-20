package com.example.brian.cleverrent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {

    static String listingType = null;
    static String listingIdentifier = null;
    static String chatIdentifier = null;
    static String userName = null;
    static String displayName = null;
    static Firebase ref = null;
    EventsListAdapter.Event event = null;
    LinearLayout chatTimeLineLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

       chatTimeLineLayout = (LinearLayout) findViewById(R.id.chatTimeLineLayout);

        if (bundle != null){
            SharedPreferences sharedPref  = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
            listingType = (String) bundle.get("LISTING_TYPE");
            listingIdentifier = (String) bundle.get("LISTING_IDENTIFIER");
            chatIdentifier = (String) bundle.get("CHAT_IDENTIFIER");
            userName = sharedPref.getString("USER_NAME", null);
            displayName = sharedPref.getString("DISPLAY_NAME", null);

            //Get the event information
            ref = new Firebase("https://cleverrent.firebaseio.com/" + listingType + "/" + listingIdentifier);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    event = dataSnapshot.getValue(EventsListAdapter.Event.class);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


            ref = new Firebase("https://cleverrent.firebaseio.com/" + listingType + "/" + listingIdentifier +
                    "/chatInstances" + "/" + chatIdentifier);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot event : dataSnapshot.child("chatEventTimeline").getChildren()) {
                            ChatEvent chatEvent = event.getValue(ChatEvent.class);
                            View chatEventFragment = getLayoutInflater().inflate(R.layout.chat_event_fragment, null);
                            TextView chatEventDateLabel = (TextView) chatEventFragment.findViewById(R.id.chatEventDateLabel);
                            TextView chatEventTextLabel = (TextView) chatEventFragment.findViewById(R.id.chatEventTextLabel);
                            chatEventDateLabel.setText(chatEvent.getDate());
                            chatEventTextLabel.setText(chatEvent.getText());
                            chatTimeLineLayout.addView(chatEventFragment);
                        }
                        for (DataSnapshot message : dataSnapshot.child("chatMessageTimeline").getChildren()) {
                            ChatMessage chatMessage = message.getValue(ChatMessage.class);
                            View messageView;
                            if (chatMessage.getFrom().equals(userName)) {
                                messageView = getLayoutInflater().inflate(R.layout.chat_message_fragment_right, null);
                            } else {
                                messageView = getLayoutInflater().inflate(R.layout.chat_message_fragment_left, null);
                            }
                            TextView textLabel = (TextView) messageView.findViewById(R.id.chatMessageTextLabel);
                            textLabel.setText(chatMessage.getMessage());
                            chatTimeLineLayout.addView(messageView);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            final EditText chatEditText = (EditText) findViewById(R.id.chatEditText);
            Button chatSendButton = (Button) findViewById(R.id.chatSendButton);
            chatSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String message = chatEditText.getText().toString();
                    chatEditText.getText().clear();
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Calendar c = Calendar.getInstance();
                            int minute = c.get(Calendar.MINUTE);
                            int hour = c.get(Calendar.HOUR);
                            ChatInstance chatInstance = dataSnapshot.getValue(ChatInstance.class);
                            ChatMessage chatMessage = new ChatMessage(Integer.toString(hour) + ":" + Integer.toString(minute), message, userName);
                            if (chatInstance.getChatMessageTimeline() == null) {
                                ArrayList<ChatMessage> chatMessageArrayList = new ArrayList<>();
                                chatInstance.setChatMessageTimeLine(chatMessageArrayList);
                            }
                            chatInstance.addChatMessage(chatMessage);
                            ref.setValue(chatInstance);

                            View messageView = getLayoutInflater().inflate(R.layout.chat_message_fragment_right, null);
                            TextView textLabel = (TextView) messageView.findViewById(R.id.chatMessageTextLabel);
                            textLabel.setText(chatMessage.getMessage());
                            chatTimeLineLayout.addView(messageView);

                            NotificationObject notificationObject = new NotificationObject(displayName, "Events: " + event.getEventTitle(), "CHAT", event.getHostName(), chatInstance, listingType, listingIdentifier);
                            CommunityListingViewActivity.postChatNotification(notificationObject);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            });

        }
    }

}
