package com.example.brian.cleverrent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class EventCreateNewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText eventDescriptionEditText = null;
    EditText eventNameEditText = null;
    EditText eventLocationEditText = null;
    Spinner ageSpinner = null;
    Spinner timeSpinner = null;
    EditText eventDateEditText = null;
    EditText eventCostEditText = null;
    ImageView imageView = null;

    Bitmap thumbnail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Event");

        eventDescriptionEditText = (EditText) findViewById(R.id.eventDescription);
        eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
        eventLocationEditText = (EditText) findViewById(R.id.eventLocationEditText);
        ageSpinner = (Spinner) findViewById(R.id.eventAgeTypeSpinner);
        timeSpinner = (Spinner) findViewById(R.id.eventTimeSpinner);
        eventDateEditText = (EditText) findViewById(R.id.eventDateEditText);
        eventCostEditText = (EditText) findViewById(R.id.eventCostEditText);
        imageView = (ImageView) findViewById(R.id.eventUploadThumbImageView);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            String callee = (String ) getCallingActivity().getClassName();
            //If the activity was started with the intent to edit an event
            //Fill in all of the fields
            if (callee.equals("com.example.brian.cleverrent.ManageListingsActivity")){
                String eventIdentifier = (String) bundle.get("LISTING_IDENTIFIER");
                Firebase firebaseRef = new Firebase(MainActivity.getFirebaseRootRef() + "events/"+eventIdentifier);
                firebaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        EventsListAdapter.Event event = dataSnapshot.getValue(EventsListAdapter.Event.class);
                        eventDescriptionEditText.setText(event.getEventDescription());
                        eventNameEditText.setFocusable(false);
                        eventNameEditText.setText(event.getEventTitle());
                        eventLocationEditText.setText(event.getEventLocation());
                        switch (event.getEventAge()){
                            case "Adult":
                                ageSpinner.setSelection(0);
                                break;
                            case "Teenage":
                                ageSpinner.setSelection(1);
                                break;
                            case "Children":
                                ageSpinner.setSelection(2);
                                break;
                            case "Toddlers":
                                ageSpinner.setSelection(3);
                                break;
                            case "Other":
                                ageSpinner.setSelection(4);
                                break;
                        }
                        switch (event.getEventTime()){
                            case "Early Morning":
                                timeSpinner.setSelection(0);
                                break;
                            case "Afternoon":
                                timeSpinner.setSelection(1);
                                break;
                            case "After Work":
                                timeSpinner.setSelection(2);
                                break;
                            case "Happy Hour":
                                timeSpinner.setSelection(3);
                                break;
                            case "Late Night":
                                timeSpinner.setSelection(4);
                                break;
                        }
                        eventDateEditText.setText(event.getEventDate());
                        eventCostEditText.setText(event.getEventCost());
                        String encodedImage = event.getImageUrl();
                        byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                        imageView.setImageBitmap(bitmap);
                        thumbnail = bitmap;
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
            System.out.println();
        }



        Button createEventButton = (Button) findViewById(R.id.eventCreateSubmitButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                final String userName = sharedPref.getString("USER_NAME", null);
                final String displayName = sharedPref.getString("DISPLAY_NAME", null);
                String eventIdentifier = eventNameEditText.getText().toString() + "-" + userName;
                String encodedImage = "image url";

                if (thumbnail != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] byteArray = baos.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }

                EventsListAdapter.Event newEvent = new EventsListAdapter.Event(
                        eventDescriptionEditText.getText().toString(),
                        eventLocationEditText.getText().toString(),
                        ageSpinner.getSelectedItem().toString(),
                        timeSpinner.getSelectedItem().toString(),
                        eventCostEditText.getText().toString(),
                        encodedImage,
                        eventDateEditText.getText().toString(),
                        eventNameEditText.getText().toString(),
                        displayName,
                        userName,
                        eventIdentifier
                );

                Firebase firebaseRef = new Firebase(MainActivity.getFirebaseRootRef() + "events/"+eventIdentifier);
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
                        try {
                            thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            imageView.setImageBitmap(thumbnail);
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
