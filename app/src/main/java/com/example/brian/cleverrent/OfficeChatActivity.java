package com.example.brian.cleverrent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class OfficeChatActivity extends AppCompatActivity {

    static String userName = null;
    static String displayName = null;
    static Firebase ref = null;
    LinearLayout chatTimeLineLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Office Chat");


        chatTimeLineLayout = (LinearLayout) findViewById(R.id.chatTimeLineLayout);


        SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        userName = sharedPref.getString("USER_NAME", null);
        displayName = sharedPref.getString("DISPLAY_NAME", null);
        ref = new Firebase(MainActivity.getFirebaseRootRef() + "officeChat/" + userName);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    ChatInstance chatInstance = new ChatInstance(displayName, "Office Staff", userName);
                    ref.setValue(chatInstance);
                } else {
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
                public void onCancelled (FirebaseError firebaseError){

                }
            }

            );

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
                          }

                          @Override
                          public void onCancelled(FirebaseError firebaseError) {

                          }
                      });
                  }
              }

            );

        }

    }
