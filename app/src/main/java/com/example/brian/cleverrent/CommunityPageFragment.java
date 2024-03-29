package com.example.brian.cleverrent;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.brian.cleverrent.FacilitiesListAdapter.Facility;
import com.example.brian.cleverrent.EventsListAdapter.Event;
import com.example.brian.cleverrent.ClassifiedsListAdapter.ClassifiedPost;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 1/31/16.
 */
// In this case, the fragment displays simple text based on the page
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CommunityPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private final ArrayList<Event> eventList = new ArrayList<>();
    private final ArrayList<ClassifiedPost> classifiedsList = new ArrayList<>();
    private final ArrayList<Facility> facilitiesList = new ArrayList<>();
    private int mPage;

    public static CommunityPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        CommunityPageFragment fragment = new CommunityPageFragment();
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
        ListView listView;
        mPage = getArguments().getInt(ARG_PAGE);
        switch (mPage){
            case 0:
                view  = inflater.inflate(R.layout.facilities_fragment_page, container, false);
                listView = (ListView) view.findViewById(R.id.facilitiesListView);
                setFacilitesListAdapter(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.events_fragment_page, container, false);
                listView = (ListView) view.findViewById(R.id.eventsListView);
                setEventsListAdapter(view);
                setEventCreateActionButton(view);
                break;
            case 2:
                view  = inflater.inflate(R.layout.classifieds_fragment_page, container, false);
                listView = (ListView) view.findViewById(R.id.classifiedsListView);
                setClassifiedsListAdapter(view);
                setClassifiedsCreateActionButton(view);
                break;
            default:
                view = null;
                listView = null;
                break;
        }
        setListingViewListener(listView);
        return view;
    }

    private void setListingViewListener(ListView listView){
        if (listView != null){
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), CommunityListingViewActivity.class);
                    if (parent.getAdapter() instanceof EventsListAdapter) {
                        EventsListAdapter adapter = (EventsListAdapter) parent.getAdapter();
                        Event event = adapter.getItem(position);
                        intent.putExtra("LISTING_TYPE", "events");
                        String identifier = event.getIdentifier();
                        intent.putExtra("LISTING_IDENTIFIER", identifier);
                    }
                    else if (parent.getAdapter() instanceof ClassifiedsListAdapter) {
                        ClassifiedsListAdapter adapter = (ClassifiedsListAdapter) parent.getAdapter();
                        ClassifiedPost post = adapter.getItem(position);
                        intent.putExtra("LISTING_TYPE", "classifieds");
                        String identifier = post.getIdentifier();
                        intent.putExtra("LISTING_IDENTIFIER", identifier);
                    } else if (parent.getAdapter() instanceof FacilitiesListAdapter) {
                        FacilitiesListAdapter adapter = (FacilitiesListAdapter) parent.getAdapter();
                        Facility facility = adapter.getItem(position);
                        intent.putExtra("LISTING_TYPE", "facility");
                        String identifier = facility.getFacility_id();
                        intent.putExtra("LISTING_IDENTIFIER", identifier);
                    }
                    getContext().startActivity(intent);

                }
            });
        }
    }

    private void setFacilitesListAdapter(final View view){
        facilitiesList.clear();
        Firebase firebaseRef = new Firebase(MainActivity.getFirebaseRootRef() + "facility/");
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot facility : dataSnapshot.getChildren()) {
                    Facility fc = facility.getValue(Facility.class);
                    facilitiesList.add(fc);
                }
                ListView lv = (ListView) view.findViewById(R.id.facilitiesListView);
                FacilitiesListAdapter adapter = new FacilitiesListAdapter(getActivity(), facilitiesList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setEventsListAdapter(final View view){
        eventList.clear();
        Firebase firebaseRef = new Firebase(MainActivity.getFirebaseRootRef() + "events/");
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (!eventList.contains(event)) {
                        eventList.add(event);
                    }
                }
                ListView lv = (ListView) view.findViewById(R.id.eventsListView);
                if (getActivity() != null) {
                    EventsListAdapter adapter = new EventsListAdapter(getActivity(), eventList);
                    lv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setClassifiedsListAdapter(final View view){
        classifiedsList.clear();
        Firebase firebaseRef = new Firebase(MainActivity.getFirebaseRootRef() + "classifieds/");
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ClassifiedPost post = postSnapshot.getValue(ClassifiedPost.class);
                    if(!classifiedsList.contains(post)) {
                        classifiedsList.add(post);
                    }
                }
                ListView lv = (ListView) view.findViewById(R.id.classifiedsListView);
                if (getActivity() != null ) {
                    ClassifiedsListAdapter adapter = new ClassifiedsListAdapter(getActivity(), classifiedsList);
                    lv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setEventCreateActionButton(View view){
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.eventCreateButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EventCreateNewActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void setClassifiedsCreateActionButton(View view){
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.classifiedsPostCreateNewButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ClassifiedsCreateNewActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }
}
