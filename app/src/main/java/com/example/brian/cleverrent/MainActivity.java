package com.example.brian.cleverrent;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new AccountFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));

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

            setContentToTabLayout();
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(new AccountFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));

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

            ListView lv = (ListView) findViewById(R.id.maintenanceListView);
            String[] dates = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
            final String[] status = {"pending", "failed", "complete", "pending", "pending", "failed", "complete", "pending", "pending", "failed", "complete", "pending"};
//
            MaintenanceListAdapter adapter = new MaintenanceListAdapter(this, dates, status);
            lv.setAdapter(adapter);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.maintenanceRequestButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("TEST");
                    Intent intent = new Intent(MainActivity.this, MaintenanceRequestActivity.class);
                    startActivity(intent);
                }
            });

            //Click Listener for Maintenance Requests
//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(getApplicationContext(), status[position], Toast.LENGTH_SHORT).show();
//                }
//            });



        } else if (id == R.id.nav_community) {

            setContentToTabLayout();

            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(new CommunityFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);


        } else if (id == R.id.nav_local) {

            setContentToTabLayout();

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
}
