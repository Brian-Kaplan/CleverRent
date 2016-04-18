package com.example.brian.cleverrent;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.brian.cleverrent.LocalDealsListAdapter.Deal;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static AccountFragmentPagerAdapter accountFragmentPagerAdapter = null;
    static CommunityFragmentPagerAdapter communityFragmentPagerAdapter = null;
    static final int LOGIN_REQUEST = 1; //Request code for the LoginActivity
    static String firebaseUID = null;
    static User authUser = null;
    private ArrayList<HubNotification> hubNotificationsList = null;
    private Firebase firebaseAuthRef = null;
    private SharedPreferences sharedPref = null;
    private static String fireBaseRootRef = "https://clever-rent.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        firebaseAuthRef = new Firebase(fireBaseRootRef);
        firebaseAuthRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    System.out.println("User Is Logged In");
                    //Save the users UID into shared preferences
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("FIRE_BASE_UID", authData.getUid());
                    editor.commit();
                    //Get the users information from the UID
                    Firebase ref = new Firebase(MainActivity.fireBaseRootRef + "tenant/" + authData.getUid());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            authUser = dataSnapshot.getValue(User.class);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("USER_NAME", authUser.getUserName());
                            editor.putString("USER_EMAIL", authUser.getEmail());
                            editor.putString("DISPLAY_NAME", authUser.getDisplayName());
                            TextView navHeaderViewNameLabel = (TextView) findViewById(R.id.navHeaderViewNameLabel);
                            navHeaderViewNameLabel.setText(authUser.getDisplayName());
                            editor.commit();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_REQUEST);
                    finish();
                }
            }
        });

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");

        setContentToTabLayout();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Set the Account and Billing nav item to checked by default
        Menu m = navigationView.getMenu();
        MenuItem mi = m.getItem(0);
        mi.setCheckable(true);
        mi.setChecked(true);


        //Load the Account Page By Default
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        accountFragmentPagerAdapter = new AccountFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        communityFragmentPagerAdapter = new CommunityFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(accountFragmentPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {

            //Set title of toolbar
            getSupportActionBar().setTitle("Account");

            RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);
            View child = getLayoutInflater().inflate(R.layout.content_main, null);
            contentView.removeAllViews();
            contentView.addView(child);

            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            accountFragmentPagerAdapter.notifyChangeInPosition(1);
            accountFragmentPagerAdapter.notifyDataSetChanged();
            viewPager.setAdapter(accountFragmentPagerAdapter);

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);


        } else if (id == R.id.nav_maintenance) {

            //Set title of toolbar
            getSupportActionBar().setTitle("Maintenance");

            //Set the content to the maintenance_page
            RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);
            View child = getLayoutInflater().inflate(R.layout.maintenance_page, null);
            contentView.removeAllViews();
            contentView.addView(child);


//            String[] dates = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
//            final String[] status = {"pending", "failed", "complete", "pending", "pending", "failed", "complete", "pending", "pending", "failed", "complete", "pending"};

            final ArrayList<MaintenanceRequest> maintenanceRequests = new ArrayList<>();
            SharedPreferences sharedPref  = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
            String userName = sharedPref.getString("USER_NAME", null);
            Firebase firebaseRef = new Firebase(MainActivity.fireBaseRootRef + "maintenance/"+userName);
            firebaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    maintenanceRequests.clear();
                    for (DataSnapshot request : dataSnapshot.getChildren()) {
                        MaintenanceRequest temp = request.getValue(MaintenanceRequest.class);
                        if (!maintenanceRequests.contains(temp))
                            maintenanceRequests.add(0, temp);
                    }
                    if (MainActivity.this != null && maintenanceRequests != null) {
                        ListView lv = (ListView) findViewById(R.id.maintenanceListView);
                        MaintenanceListAdapter adapter = new MaintenanceListAdapter(MainActivity.this, maintenanceRequests);
                        lv.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.maintenanceRequestButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, MaintenanceRequestActivity.class);
                    startActivity(intent);
                }
            });

        } else if (id == R.id.nav_community) {

            //Set title of toolbar
            getSupportActionBar().setTitle("Community");

            RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);
            View child = getLayoutInflater().inflate(R.layout.content_main, null);
            contentView.removeAllViews();
            contentView.addView(child);

            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            //The fragmentsPagerAdapters mess with each other. Offset this by 2 and the other by 1
            communityFragmentPagerAdapter.notifyChangeInPosition(-1);
            communityFragmentPagerAdapter.notifyDataSetChanged();
            viewPager.setAdapter(communityFragmentPagerAdapter);

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);


        } else if (id == R.id.nav_local) {

            //Set title of toolbar
            getSupportActionBar().setTitle("Local Deals");

            //Set the content to the maintenance_page
            RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);
            View child = getLayoutInflater().inflate(R.layout.local_deals_page, null);
            contentView.removeAllViews();
            contentView.addView(child);

            Firebase ref = new Firebase(MainActivity.getFirebaseRootRef() + "deals/");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Deal> deals = new ArrayList<Deal>();
                    for (DataSnapshot deal : dataSnapshot.getChildren()) {
                        Deal temp = deal.getValue(Deal.class);
                        deals.add(temp);
                        ListView lv = (ListView) findViewById(R.id.localDealsListView);
                        LocalDealsListAdapter adapter = new LocalDealsListAdapter(MainActivity.this, deals);
                        lv.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        } else if (id == R.id.nav_notification) {

            //Set title of toolbar
            getSupportActionBar().setTitle("Notifications");

            //Set the content to the notification_page
            RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);
            View child = getLayoutInflater().inflate(R.layout.notification_page, null);
            contentView.removeAllViews();
            contentView.addView(child);


        } else if (id == R.id.nav_contact) {

            //Set title of toolbar
            getSupportActionBar().setTitle("Contact Us");

            //Set the content to the contact_page
            RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);
            View child = getLayoutInflater().inflate(R.layout.contact_page, null);
            contentView.removeAllViews();
            contentView.addView(child);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.contactUsChatButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, OfficeChatActivity.class);
                    startActivity(intent);
                }
            });


        } else if(id == R.id.nav_logout) {
            firebaseAuthRef.unauth();


        } else if(id == R.id.nav_hub) {

            getSupportActionBar().setTitle("Hub");

            RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);
            View child = getLayoutInflater().inflate(R.layout.content_hub, null);
            contentView.removeAllViews();
            contentView.addView(child);


            TextView eventsTextView = (TextView) findViewById(R.id.eventsHubLabel);
            TextView classifiedsTextView = (TextView) findViewById(R.id.classifiedsHubLabel);

            eventsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ManageEventsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                }
            });
            classifiedsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ManageClassifiedsActivity.class);
                    startActivity(intent);
                    overridePendingTransition (R.anim.right_slide_in, R.anim.right_slide_out);
                }
            });



            hubNotificationsList = new ArrayList<>();
            hubNotificationsList.clear();

            final String userName = sharedPref.getString("USER_NAME", null);
            Firebase firebaseRef = new Firebase(MainActivity.fireBaseRootRef + "notifications/"+userName);

            //Pull all the data, It will be in the form on NotifcationObjects
            //Each object will have a date. Sort them into Hub Notifications and then build the lists
            firebaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                        HubNotification temp = new HubNotification(dateSnapshot.getKey());
                        for (DataSnapshot notifSnapshot : dateSnapshot.getChildren()) {
                            NotificationObject notification = notifSnapshot.getValue(NotificationObject.class);
                            temp.addNotificationObject(notification);
                        }
                        hubNotificationsList.add(0, temp);
                    }

                    ListView hubListView = (ListView) findViewById(R.id.hubListView);
                    HubListAdapter adapter = new HubListAdapter(MainActivity.this, hubNotificationsList);
                    hubListView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            if (!(mi.getItemId() == item.getItemId())) {
                mi.setCheckable(false);
            }
        }
        item.setCheckable(true);
        item.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Sets the main content view to the TablLayout view
    public void setContentToTabLayout(){
        RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);
        View child = getLayoutInflater().inflate(R.layout.content_main, null);
        contentView.removeAllViews();
        contentView.addView(child);
    }

    public static String getTodaysDate(){
        String date = null;
        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("M-d-y");
        date = dateFormatter.format(now);
        return date;
    }

    public static String getTimeStamp() {
        String time = null;
        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm a");
        time = dateFormatter.format(now);
        return time;
    }

    public static String getFirebaseRootRef() {
        return fireBaseRootRef;
    }
}
