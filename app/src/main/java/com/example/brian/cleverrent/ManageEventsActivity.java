package com.example.brian.cleverrent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brian.cleverrent.EventsListAdapter.Event;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class ManageEventsActivity extends AppCompatActivity {

    ArrayList<Event> events = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        events = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateList();

        if (events.size() == 0){
            TextView noListingLabel = (TextView) findViewById(R.id.noListingsToShowLabel);
            noListingLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

    private void updateList() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ManageEventsActivity.this);
        final String userName = sharedPref.getString("USER_NAME", null);

        Firebase ref = new Firebase("https://cleverrent.firebaseio.com/events/");
        // Attach an listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Event event = postSnapshot.getValue(Event.class);
                    if (event.getIdentifier().equals(event.getEventTitle() + "-" + userName))
                        if (!events.contains(event)){
                            events.add(event);
                        }
                }
                ListView manageEventsListView = (ListView) findViewById(R.id.manageEventsListView);
                ManageEventsListAdapter listAdapter = new ManageEventsListAdapter(ManageEventsActivity.this, events);
                manageEventsListView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }
}
