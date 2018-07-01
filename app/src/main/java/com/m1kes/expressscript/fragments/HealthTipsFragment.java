package com.m1kes.expressscript.fragments;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.m1kes.expressscript.R;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Created by shelton on 6/14/18.
 */

public class HealthTipsFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private ListView listView;
    private ArrayAdapter adapter;
    private List items;
    private int refresh_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_health_tips, container, false);

        refreshLayout = view.findViewById(R.id.refreshLayout);
        listView = view.findViewById(R.id.healthTipsListView);


        String[] tips = getContext().getResources().getStringArray(R.array.health_tips);
        items = Arrays.asList( new String[]{  tips[new Random().nextInt(tips.length)]});
        adapter = new ArrayAdapter(getContext(),R.layout.health_item_layout,items);
        listView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });



        return view;

    }

    private void refreshItems(){

        String[] tips = getContext().getResources().getStringArray(R.array.health_tips);
        items = Arrays.asList( new String[]{  tips[new Random().nextInt(tips.length)]});
        adapter = new ArrayAdapter(getContext(),R.layout.health_item_layout,items);
        listView.setAdapter(adapter);
        refreshLayout.setRefreshing(false);
    }

}
