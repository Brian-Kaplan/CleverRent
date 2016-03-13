package com.example.brian.cleverrent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HubActivity extends AppCompatActivity {

    ArrayList<HubNotification> hubNotificationsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hub");

        TextView eventsTextView = (TextView) findViewById(R.id.eventsHubLabel);
        TextView classifiedsTextView = (TextView) findViewById(R.id.classifiedsHubLabel);

        eventsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HubActivity.this, ManageEventsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
        });
        classifiedsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HubActivity.this, ManageClassifiedsActivity.class);
                startActivity(intent);
//                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
            }
        });


        hubNotificationsList = new ArrayList<>();
        hubNotificationsList.clear();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(HubActivity.this);
        final String userName = sharedPref.getString("USER_NAME", null);
        Firebase firebaseRef = new Firebase("https://cleverrent.firebaseio.com/notifications/"+userName);

        //Pull all the data, It will be in the form on NotifcationObjects
        //Each object will have a date. Sort them into Hub Notifications and then build the lists
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    HubNotification temp = new HubNotification(dateSnapshot.getKey());
                    for (DataSnapshot notifSnapshot : dateSnapshot.getChildren()) {
                        NotificationObject notification = notifSnapshot.getValue(NotificationObject.class);
                        temp.addNotificationObject(notification);
                    }
                    hubNotificationsList.add(0, temp);
                }

                ListView hubListView = (ListView) findViewById(R.id.hubListView);
                HubListAdapter adapter = new HubListAdapter(HubActivity.this, hubNotificationsList);
                hubListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }
}
