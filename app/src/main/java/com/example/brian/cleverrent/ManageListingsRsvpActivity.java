package com.example.brian.cleverrent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ManageListingsRsvpActivity extends AppCompatActivity {

    String type = null;
    String listingType = null;
    String listingIdentifier = null;
    ArrayList<ChatInstance> chatInstances = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_listings_rsvp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage RSVP");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            type = (String) bundle.get("TYPE");
            listingIdentifier = (String) bundle.get("LISTING_IDENTIFIER");
            listingType = (String) bundle.get("LISTING_TYPE");
            chatInstances = new ArrayList<>();
            String typePath = null;
            if (type.equals("interest")){
                typePath = "interestedList";
            } else
                typePath = "rsvpList";

            Firebase ref = new Firebase(MainActivity.getFirebaseRootRef() + listingType + "/" + listingIdentifier + "/" + typePath);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    LinearLayout listLayout = (LinearLayout) findViewById(R.id.manageListingRSVPLayout);
                    for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
                        final String person = personSnapshot.getValue(String.class);

                        View chatCellModel = getLayoutInflater().inflate(R.layout.manage_listings_chat_cell_model, null);
                        TextView chatCellName = (TextView) chatCellModel.findViewById(R.id.manageListingChatNameLabel);
                        chatCellName.setText(person);
                        listLayout.addView(chatCellModel);
                        chatCellModel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ManageListingsRsvpActivity.this, ChatActivity.class);
                                intent.putExtra("LISTING_TYPE", listingType);
                                intent.putExtra("LISTING_IDENTIFIER", listingIdentifier);
                                intent.putExtra("CHAT_IDENTIFIER", person);
                                startActivity(intent);
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }


    }

}
