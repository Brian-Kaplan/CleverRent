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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ClassifiedsCreateNewActivity extends AppCompatActivity {

    Spinner type = null;
    EditText title =  null;
    EditText desc =  null;
    Spinner condition =  null;
    EditText price =  null;
    EditText name =  null;
    EditText phone =  null;
    EditText email =  null;

    Bitmap thumbnail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifieds_create_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Classified");

        type = (Spinner) findViewById(R.id.classifiedTypeSpinner);
        title = (EditText) findViewById(R.id.classifiedShortTitleEditText);
        desc = (EditText) findViewById(R.id.classifiedDescEditText);
        condition = (Spinner) findViewById(R.id.classifiedConditionSpinner);
        price = (EditText) findViewById(R.id.classifiedPriceEditText);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            String callee = (String) getCallingActivity().getClassName();
            //If the activity was started with the intent to edit a classified
            //Fill in all of the fields
            if (callee.equals("com.example.brian.cleverrent.ManageListingsActivity")) {
                String classifiedIdentifier = (String) bundle.get("LISTING_IDENTIFIER");
                Firebase firebaseRef = new Firebase(MainActivity.getFirebaseRootRef() + "classifieds/" + classifiedIdentifier);
                firebaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ClassifiedsListAdapter.ClassifiedPost post = dataSnapshot.getValue(ClassifiedsListAdapter.ClassifiedPost.class);
                        switch (post.getPostType()){
                            case "Kitchen":
                                type.setSelection(0);
                                break;
                            case "Bathroom":
                                type.setSelection(1);
                                break;
                            case "Bedroom":
                                type.setSelection(2);
                                break;
                            case "Other":
                                type.setSelection(3);
                                break;
                        }
                        title.setFocusable(false);
                        title.setText(post.getPostTitle());
                        desc.setText(post.getPostDescription());
                        switch (post.getPostCondition()){
                            case "Unopened":
                                condition.setSelection(0);
                                break;
                            case "Like New":
                                condition.setSelection(1);
                                break;
                            case "Lightly Used":
                                condition.setSelection(2);
                                break;
                            case "Used":
                                condition.setSelection(3);
                                break;
                        }
                        price.setText(post.getPostPrice());
                        name.setText(post.getFullName());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

        }

        Button createClassifiedButton = (Button) findViewById(R.id.classifiedSubmitButton);
        createClassifiedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                final String userName = sharedPref.getString("USER_NAME", null);
                final String displayName = sharedPref.getString("DISPLAY_NAME", null);
                final String fireBaseUID = sharedPref.getString("FIRE_BASE_UID", null);
                String postIdentifier = title.getText().toString() + "-" + userName;
                String encodedImage = "image url";

                if (thumbnail != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] byteArray = baos.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }

                ClassifiedsListAdapter.ClassifiedPost post = new ClassifiedsListAdapter.ClassifiedPost(
                        type.getSelectedItem().toString(), title.getText().toString(),
                        desc.getText().toString(), condition.getSelectedItem().toString(),
                        price.getText().toString(), encodedImage,
                        displayName, postIdentifier
                );

                Firebase firebaseRef = new Firebase(MainActivity.getFirebaseRootRef() + "classifieds/"+postIdentifier);
                firebaseRef.setValue(post);

                ClassifiedsCreateNewActivity.this.finish();
            }
        });

        Button uploadImageButton = (Button) findViewById(R.id.classifiedUploadImageButton);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 8);
            }
        });

        Spinner typeSpinner = (Spinner) findViewById(R.id.classifiedTypeSpinner);
        Spinner conditionSpinner = (Spinner) findViewById(R.id.classifiedConditionSpinner);

        // Spinner Drop down elements
        List<String> typeCategories = new ArrayList<String>();
        typeCategories.add("Kitchen");
        typeCategories.add("Bathroom");
        typeCategories.add("Bedroom");
        typeCategories.add("Other");

        List<String> conditionCategories = new ArrayList<String>();
        conditionCategories.add("Unopened");
        conditionCategories.add("Like New");
        conditionCategories.add("Lightly Used");
        conditionCategories.add("Used");

        // Creating adapter for spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeCategories);
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conditionCategories);

        // Drop down layout style - list view with radio button
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        typeSpinner.setAdapter(typeAdapter);
        conditionSpinner.setAdapter(conditionAdapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 8:
                    if (resultCode == Activity.RESULT_OK) {
                        //data gives you the image uri. Try to convert that to bitmap
                        Uri selectedImage = data.getData();
                        try {
                            thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            ImageView imageView = (ImageView) findViewById(R.id.classifiedUploadImageThumb);
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
