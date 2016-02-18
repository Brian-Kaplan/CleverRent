package com.example.brian.cleverrent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ClassifiedsCreateNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifieds_create_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Classified");

        Button createClassifiedButton = (Button) findViewById(R.id.classifiedSubmitButton);
        createClassifiedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            ImageView imageView = (ImageView) findViewById(R.id.classifiedUploadImageThumb);
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
