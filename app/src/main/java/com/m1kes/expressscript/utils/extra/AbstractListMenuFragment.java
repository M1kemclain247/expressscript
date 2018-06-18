package com.m1kes.expressscript.utils.extra;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.m1kes.expressscript.R;
import com.m1kes.expressscript.adapters.menu.ListMenuAdapter;
import com.m1kes.expressscript.objects.MenuItem;

import java.util.ArrayList;

import static com.m1kes.expressscript.utils.extra.Utils.findTextView;


public abstract class AbstractListMenuFragment extends ListFragment {


    public static String LIST_TITLE = "fragment_listmenu_title";
    public static String SHOW_CABS_HEADING = "fragment_listmenu_show_heading";

    private OnListMenuListener onListMenuListener;
    private ListMenuAdapter listMenuAdapter;
    private ArrayList<MenuItem> menuItems;

    private TextView titleView;
    private String toolbarTile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //If we have a saved instance, we most likely are recovering from a screen orientation change
        //so we need to recreate the menu structure that was saved before the change, otherwise if it
        //is a fresh Frgament, the menu items will be created from the getMenuItems()
        if (savedInstanceState != null) {
            menuItems = (ArrayList<MenuItem>) savedInstanceState.getSerializable("current_menu_structure");
        } else {
            menuItems = getMenuItems();
        }

        if (menuItems == null) {
            throw new RuntimeException("must return non null List<MenuItem>");
        }
        listMenuAdapter = new ListMenuAdapter(getActivity(), menuItems);
        setListAdapter(listMenuAdapter);
        //By default disable the CABS heading
        checkForHeading();
    }

    protected void toggleCabsHeading(boolean showHeading) {

        Activity baseActivity = getActivity();
        if (baseActivity != null) {
            View menuHeading = baseActivity.findViewById(R.id.menu_heading);
            if (menuHeading != null) {
                if (showHeading) {
                    menuHeading.setVisibility(View.VISIBLE);
                } else {
                    menuHeading.setVisibility(View.GONE);
                }
            }

        }

    }

    protected void setToolbarTile(String toolbarTile) {

        Activity baseActivity = getActivity();
        if (baseActivity != null) {
            TextView toolbarTextView = (TextView) baseActivity.findViewById(R.id.toolbar_title_textview);
            toolbarTextView.setText(toolbarTile);

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Store our current menu structure so it is not lost on orientation change
        outState.putSerializable("current_menu_structure", menuItems);


    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_menu, container, false);
        titleView =  findTextView(view, R.id.fragment_listmenu_title_textview);
        checkForTitle();
        return view;
    }

    private void checkForTitle() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String listTile = arguments.getString(LIST_TITLE);
            if (listTile != null) {
                titleView.setText(listTile);
                setToolbarTile(listTile);
            } else {
                titleView.setVisibility(View.GONE);
            }
        } else {
            titleView.setVisibility(View.GONE);
        }
    }

    private void checkForHeading() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            boolean showHeading = arguments.getBoolean(SHOW_CABS_HEADING);
            toggleCabsHeading(showHeading);
        } else {
            //By default hide the menu
            toggleCabsHeading(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onListMenuListener = (OnListMenuListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    //Support for older android versions
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onListMenuListener = (OnListMenuListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {


        MenuItem item = listMenuAdapter.getItem(position);
        if (item != null) {
            onListMenuListener.onMenuItemClicked(item);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onListMenuListener = null;
        menuItems = null;
    }

    protected abstract ArrayList<MenuItem> getMenuItems();


    public interface OnListMenuListener {
        void onMenuItemClicked(MenuItem menuItem);
    }

}
