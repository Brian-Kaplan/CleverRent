package com.example.brian.cleverrent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ManageListingsActivity extends AppCompatActivity {

    String listingType = null;
    String listingTitle = null;
    String listingIdentifier = null;
    String interestNumber = null;
    String rsvpNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_listings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        final String userName = sharedPref.getString("USER_NAME", null);

        if(bundle != null)
        {
            listingTitle = (String) bundle.get("LISTING_TITLE");
            listingType = (String) bundle.get("LISTING_TYPE");
            interestNumber = (String) bundle.get("INTEREST_LIST_SIZE");
            rsvpNumber = (String) bundle.get("RSVP_LIST_SIZE");

            listingIdentifier = listingTitle+"-"+userName;
            getSupportActionBar().setTitle("Manage: " + listingTitle);

            TextView editListingLabel = (TextView) findViewById(R.id.manageListingEditLabel);
            TextView manageListingInterestedCountLabel = (TextView) findViewById(R.id.manageListingInterestedCountLabel);
            TextView manageListingRSVPCountLabel = (TextView) findViewById(R.id.manageListingRSVPCountLabel);
            manageListingInterestedCountLabel.setText(interestNumber);
            manageListingRSVPCountLabel.setText(rsvpNumber);
            editListingLabel.setText("Edit " + listingType);

        }

        View editListingView = findViewById(R.id.manageListingEditView);
        editListingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listingType.equals("events")){
                    Intent intent = new Intent(ManageListingsActivity.this, EventCreateNewActivity.class);
                    intent.putExtra("LISTING_IDENTIFIER", listingIdentifier);
                    startActivityForResult(intent, 1);
                }
                if (listingType.equals("classifieds")){
                    Intent intent = new Intent(ManageListingsActivity.this, ClassifiedsCreateNewActivity.class);
                    intent.putExtra("LISTING_IDENTIFIER", listingIdentifier);
                    startActivityForResult(intent, 1);
                }
            }
        });

        View interestView = findViewById(R.id.manageListingInterestedView);
        interestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageListingsActivity.this, ManageListingsRsvpActivity.class);
                intent.putExtra("TYPE", "interest");
                intent.putExtra("LISTING_TYPE", listingType);
                intent.putExtra("LISTING_IDENTIFIER", listingIdentifier);
                startActivity(intent);
            }
        });

        View rsvpView = findViewById(R.id.manageListingRSVPView);
        rsvpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageListingsActivity.this, ManageListingsRsvpActivity.class);
                intent.putExtra("TYPE", "rsvp");
                intent.putExtra("LISTING_TYPE", listingType);
                intent.putExtra("LISTING_IDENTIFIER", listingIdentifier);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }
}
