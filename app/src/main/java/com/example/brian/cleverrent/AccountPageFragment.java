package com.example.brian.cleverrent;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    private void setBillingListAdapter(View view){
        String[] ammounts = {"$2.50", "$3.00", "$0.00", "$2.50", "$3.00", "$0.00", "$2.50", "$3.00", "$0.00", "$2.50", "$3.00", "$0.00"};
        final String[] status = {"pending", "failed", "complete", "pending", "pending", "failed", "complete", "pending", "pending", "failed", "complete", "pending"};

        ListView lv = (ListView) view.findViewById(R.id.billingListView);
        MaintenanceListAdapter adapter = new MaintenanceListAdapter(getActivity(), ammounts, status);
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
