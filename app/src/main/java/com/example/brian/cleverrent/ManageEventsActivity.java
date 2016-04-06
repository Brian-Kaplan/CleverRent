package com.example.brian.cleverrent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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
    String userName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Events");

        events = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

    private void updateList() {
        SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        userName = sharedPref.getString("USER_NAME", null);

        Firebase ref = new Firebase(MainActivity.getFirebaseRootRef() + "events/");
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

                if (events.size() == 0){
                    TextView noListingLabel = (TextView) findViewById(R.id.noListingsToShowLabel);
                    noListingLabel.setVisibility(View.VISIBLE);
                }

                ListView manageEventsListView = (ListView) findViewById(R.id.manageEventsListView);
                ManageEventsListAdapter listAdapter = new ManageEventsListAdapter(ManageEventsActivity.this, events);
                manageEventsListView.setAdapter(listAdapter);

                manageEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        Intent intent = new Intent(ManageEventsActivity.this, ManageListingsActivity.class);
                        intent.putExtra("LISTING_TYPE", "events");
                        intent.putExtra("LISTING_TITLE", events.get(position).getEventTitle());
                        intent.putExtra("INTEREST_LIST_SIZE", String.valueOf(events.get(position).getInterestedList().size()));
                        intent.putExtra("RSVP_LIST_SIZE", String.valueOf(events.get(position).getRsvpList().size()));
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }
}
