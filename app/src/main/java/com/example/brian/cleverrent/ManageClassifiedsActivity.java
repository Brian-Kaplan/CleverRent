package com.example.brian.cleverrent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brian.cleverrent.ClassifiedsListAdapter.ClassifiedPost;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class ManageClassifiedsActivity extends AppCompatActivity {

    ArrayList<ClassifiedPost> classifiedPosts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_classifieds);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Classifieds");

        classifiedPosts = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

    private void updateList() {
        SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        final String userName = sharedPref.getString("USER_NAME", null);

        Firebase ref = new Firebase("https://cleverrent.firebaseio.com/classifieds/");
        // Attach an listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ClassifiedPost post = postSnapshot.getValue(ClassifiedPost.class);
                    if (post.getIdentifier().equals(post.getPostTitle() + "-" + userName))
                        if (!classifiedPosts.contains(post)) {
                            classifiedPosts.add(post);
                        }
                }

                if (classifiedPosts.size() == 0){
                    TextView noListingsLabel = (TextView) findViewById(R.id.noListingsToShowLabel);
                    noListingsLabel.setVisibility(View.VISIBLE);
                }

                ListView classifiedsListView = (ListView) findViewById(R.id.manageClassifiedsListView);
                ManageClassifiedsListAdapter listAdapter = new ManageClassifiedsListAdapter(ManageClassifiedsActivity.this, classifiedPosts);
                classifiedsListView.setAdapter(listAdapter);

                classifiedsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        Intent intent = new Intent(ManageClassifiedsActivity.this, ManageListingsActivity.class);
                        intent.putExtra("LISTING_TYPE", "classifiedss");
                        intent.putExtra("LISTING_TITLE", classifiedPosts.get(position).getPostTitle());
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

}
