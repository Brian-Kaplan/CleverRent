package com.example.brian.cleverrent;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by brian on 1/31/16.
 */
// In this case, the fragment displays simple text based on the page
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AccountPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static AccountPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AccountPageFragment fragment = new AccountPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        mPage = getArguments().getInt(ARG_PAGE);
        switch (mPage){
            case 0:
                view  = inflater.inflate(R.layout.user_fragment_page, container, false);
                loadUserAccountDetails(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.billing_fragment_page, container, false);
                setBillingListAdapter(view);
                setBillingActionButton(view);
                break;
            default:
                view  = inflater.inflate(R.layout.user_fragment_page, container, false);
                break;
        }
        return view;
    }



    private void loadUserAccountDetails(View view){

        SharedPreferences sharedPref  = getActivity().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        final String fireBaseUID = sharedPref.getString("FIRE_BASE_UID", null);
        if (fireBaseUID !=  null){

            TextView userAccountNumberLabel = (TextView) view.findViewById(R.id.userAccountNumberLabel);
            final TextView userNameLabel = (TextView) view.findViewById(R.id.userNameLabel);
            final TextView userEmailLabel = (TextView) view.findViewById(R.id.userEmailLabel);

            // Get a reference to our user
            Firebase ref = new Firebase("https://cleverrent.firebaseio.com/users/"+fireBaseUID);
            // Attach an listener to read the data at our posts reference
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String username = snapshot.child("displayName").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    userNameLabel.setText(username);
                    userEmailLabel.setText(email);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });

            userAccountNumberLabel.setText("Account #: " + fireBaseUID.split("-")[0]);
        }


    }

    private void setBillingListAdapter(View view){
        String[] ammounts = {"$2.50", "$3.00", "$0.00", "$2.50", "$3.00", "$0.00", "$2.50", "$3.00", "$0.00", "$2.50", "$3.00", "$0.00"};
        final String[] status = {"pending", "failed", "complete", "pending", "pending", "failed", "complete", "pending", "pending", "failed", "complete", "pending"};

        ListView lv = (ListView) view.findViewById(R.id.billingListView);
        BillingListAdapter adapter = new BillingListAdapter(getActivity(), ammounts, status);
        lv.setAdapter(adapter);
    }

    private void setBillingActionButton(View view){
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.billingNewButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), BillingActivity.class);
                startActivity(intent);
            }
        });
    }
}
