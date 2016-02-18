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
import android.widget.ListView;
import com.example.brian.cleverrent.FacilitiesListAdapter.Facility;
import com.example.brian.cleverrent.EventsListAdapter.Event;

/**
 * Created by brian on 1/31/16.
 */
// In this case, the fragment displays simple text based on the page
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CommunityPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

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
        mPage = getArguments().getInt(ARG_PAGE);
        switch (mPage){
            case 0:
                view  = inflater.inflate(R.layout.facilities_fragment_page, container, false);
                setFacilitesListAdapter(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.events_fragment_page, container, false);
                setEventsListAdapter(view);
                setEventCreateActionButton(view);
                break;
            case 2:
                view  = inflater.inflate(R.layout.classifieds_fragment_page, container, false);
                break;
            default:
                view = null;
                break;
        }
        return view;
    }

    private void setFacilitesListAdapter(View view){
        Facility facilityOne = new Facility("Hot Dogs", "Must Reserve", "http://i.imgur.com/8vdAgMR.jpg", "Miami");
        Facility facilityTwo = new Facility("More Dogs", "10AM - 9PM", "http://i.imgur.com/8vdAgMR.jpg", "Roanoke");
        Facility facilityThree = new Facility("HOLY HOT Dogs", "Must Reserve", "http://i.imgur.com/8vdAgMR.jpg", "My House");
        Facility[] facilities = {facilityOne, facilityTwo, facilityThree};

        ListView lv = (ListView) view.findViewById(R.id.facilitiesListView);
        FacilitiesListAdapter adapter = new FacilitiesListAdapter(getActivity(), facilities);
        lv.setAdapter(adapter);
    }

    private void setEventsListAdapter(View view){
        Event eventOne = new Event("Lazer Crazies", "Infants", "http://equinoxlasertag.com/wp-content/uploads/2015/09/Screen-Shot-2014-06-23-at-8.35.12-PM.png", "May 10", "Lazer Tag");
        Event eventTwo = new Event("Black Pool", "Adults", "http://equinoxlasertag.com/wp-content/uploads/2015/09/Screen-Shot-2014-06-23-at-8.35.12-PM.png", "May 11", "Pool Party");
        Event eventThree = new Event("Back Alley", "Middle Aged Women", "http://equinoxlasertag.com/wp-content/uploads/2015/09/Screen-Shot-2014-06-23-at-8.35.12-PM.png", "May 15", "CRZY ORGY");
        Event[] events = {eventOne, eventTwo, eventThree};

        ListView lv = (ListView) view.findViewById(R.id.eventsListView);
        EventsListAdapter adapter = new EventsListAdapter(getActivity(), events);
        lv.setAdapter(adapter);
    }

    private void setEventCreateActionButton(View view){
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.eventCreateButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EventCreateNewActivity.class);
                startActivity(intent);
            }
        });
    }
}
