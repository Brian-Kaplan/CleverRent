package com.example.brian.cleverrent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MaintenanceRequestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Context context;
    MaintenanceRequest request = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Maintenance Request");

        this.context = context;
        request = new MaintenanceRequest();
        request.setTimeOfSubmission(MainActivity.getTodaysDate() + "-" + MainActivity.getTimeStamp());

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.typeSpinner);

        Button button = (Button) findViewById(R.id.maintenanceSubmitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MaintenanceRequestActivity.this);
                dialog.setContentView(R.layout.maintenance_submit_confirmation);
                dialog.show();  //<-- See This!

                SharedPreferences sharedPref  = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                String userName = sharedPref.getString("USER_NAME", null);
                String displayName = sharedPref.getString("DISPLAY_NAME", null);
                Firebase firebaseRef = new Firebase(MainActivity.getFirebaseRootRef() + "maintenance/"+userName + "/"  + request.getTimeOfSubmission());

                EditText editTextDescription = (EditText) findViewById(R.id.editTextDescription);
                EditText editTextTimeFrame = (EditText) findViewById(R.id.editTextTimeFrame);

                request.setDescription(editTextDescription.getText().toString());
                request.setTimeForService(editTextTimeFrame.getText().toString());
                request.setTenantName(displayName);
                request.setStatus("pending");

                firebaseRef.setValue(request);

                Button confirmationButton = (Button) dialog.findViewById(R.id.confirmationButton);
                confirmationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Close the activity
                        MaintenanceRequestActivity.this.finish();
                    }
                });

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Plumbing");
        categories.add("Electric");
        categories.add("Appliances");
        categories.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        request.setMaintenanceType(item);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
