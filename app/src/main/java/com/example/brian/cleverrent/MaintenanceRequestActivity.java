package com.example.brian.cleverrent;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
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
        request.setTimeOfSubmission(getTimeStamp());

        // Spinner element
        final Spinner requestTypeSpinner = (Spinner) findViewById(R.id.requestTypeSpinner);
        final Spinner timeFrameSpinner = (Spinner) findViewById(R.id.timeFrameSpinner);
        
        
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

                request.setDescription(editTextDescription.getText().toString());
                request.setTimeForService(timeFrameSpinner.getSelectedItem().toString());
                request.setTenantName(displayName);
                request.setStatus("pending");
                request.setRequestType(requestTypeSpinner.getSelectedItem().toString());


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
        List<String> typeOptions = new ArrayList<String>();
        typeOptions.add("Plumbing");
        typeOptions.add("Electric");
        typeOptions.add("Appliances");
        typeOptions.add("Other");

        List<String> timeOptions = new ArrayList<>();
        timeOptions.add("Morning");
        timeOptions.add("Afternoon");
        timeOptions.add("Evening");

        // Creating adapter for spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeOptions);
        ArrayAdapter<String> timeFrameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeOptions);
        // Drop down layout style - list view with radio button
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeFrameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        requestTypeSpinner.setAdapter(typeAdapter);
        timeFrameSpinner.setAdapter(timeFrameAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        request.setRequestType(item);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private String getTimeStamp() {
        String time = null;
        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("d-M-y H:mm");
        time = dateFormatter.format(now);
        return time;
    }

}
