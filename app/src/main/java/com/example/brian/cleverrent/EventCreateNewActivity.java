package com.example.brian.cleverrent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class EventCreateNewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Event");

        Button createEventButton = (Button) findViewById(R.id.eventCreateSubmitButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eventDescription = (EditText) findViewById(R.id.eventDescription);
                EditText eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
                EditText eventLocationEditText = (EditText) findViewById(R.id.eventLocationEditText);
                Spinner ageSpinner = (Spinner) findViewById(R.id.eventAgeTypeSpinner);
                Spinner timeSpinner = (Spinner) findViewById(R.id.eventTimeSpinner);
                EditText eventDateEditText = (EditText) findViewById(R.id.eventDateEditText);
                EditText eventCostEditText = (EditText) findViewById(R.id.eventCostEditText);

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(EventCreateNewActivity.this);
                final String userName = sharedPref.getString("USER_NAME", null);
                final String fireBaseUID = sharedPref.getString("FIRE_BASE_UID", null);
                String eventIdentifier = eventNameEditText.getText().toString() + "-" + userName;


                EventsListAdapter.Event newEvent = new EventsListAdapter.Event(
                        eventLocationEditText.getText().toString(),
                        ageSpinner.getSelectedItem().toString(),
                        "image url",
                        eventDateEditText.getText().toString(),
                        eventNameEditText.getText().toString(),
                        userName,
                        eventIdentifier
                );

                Firebase firebaseRef = new Firebase("https://cleverrent.firebaseio.com/events/"+eventIdentifier);
                firebaseRef.setValue(newEvent);

                EventCreateNewActivity.this.finish();
            }
        });

        Button uploadImageButton = (Button) findViewById(R.id.eventUploadImageButton);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 7);
            }
        });


        Spinner ageSpinner = (Spinner) findViewById(R.id.eventAgeTypeSpinner);
        Spinner timeSpinner = (Spinner) findViewById(R.id.eventTimeSpinner);

        // Spinner Drop down elements
        List<String> ageCategories = new ArrayList<String>();
        ageCategories.add("Adult");
        ageCategories.add("Teenage");
        ageCategories.add("Children");
        ageCategories.add("Toddlers");
        ageCategories.add("Other");

        List<String> timeCategories = new ArrayList<String>();
        timeCategories.add("Early Morning");
        timeCategories.add("Afternoon");
        timeCategories.add("After Work");
        timeCategories.add("Happy Hour");
        timeCategories.add("Late Night");

        // Creating adapter for spinner
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ageCategories);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeCategories);

        // Drop down layout style - list view with radio button
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        ageSpinner.setAdapter(ageAdapter);
        timeSpinner.setAdapter(timeAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 7:
                    if (resultCode == Activity.RESULT_OK) {
                        //data gives you the image uri. Try to convert that to bitmap
                        Uri selectedImage = data.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            ImageView imageView = (ImageView) findViewById(R.id.eventUploadThumbImageView);
                            imageView.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e("Error", "Selecting picture cancelled");
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e("Error", "Exception in onActivityResult : " + e.getMessage());
        }
    }
}
