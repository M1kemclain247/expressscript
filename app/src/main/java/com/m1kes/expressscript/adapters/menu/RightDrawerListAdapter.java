package com.m1kes.expressscript.adapters.menu;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.m1kes.expressscript.R;
import com.m1kes.expressscript.objects.MenuItem;
import com.squareup.picasso.Picasso;

import java.util.List;



public class RightDrawerListAdapter extends ArrayAdapter<MenuItem> {


    public static final int TYPE_MENU = 0;
    public static final int TYPE_HEADING = 1;
    private LayoutInflater inflater;

    public RightDrawerListAdapter(Context context, List<MenuItem> menuItems) {
        super(context, 0, menuItems);
        inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getItemViewType(int position) {
        MenuItem item = getItem(position);
        if (item.isHeader()) {
            return TYPE_HEADING;
        }
        return TYPE_MENU;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Heading and menu
    }

    @Override
    public boolean isEnabled(int position) {
        MenuItem item = getItem(position);
        if (item.isHeader()) {
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int viewType = getItemViewType(position);
        if (convertView == null) {
            switch (viewType) {
                case TYPE_HEADING: {
                    convertView = inflater.inflate(R.layout.right_menu_heading, parent, false);
                    break;
                }
                //Menu item
                default: {
                    convertView = inflater.inflate(R.layout.list_item_right_drawer, parent, false);
                    break;
                }
            }
        }

        MenuItem menuItem = getItem(position);
        //Populate the respective views
        switch (viewType) {
            case TYPE_HEADING: {
                ((TextView) convertView).setText(menuItem.getTitle());
                break;
            }
            //Onlu other is a menu
            default: {
                ImageView menuImgage = (ImageView) convertView.findViewById(R.id.menu_item_icon);
                TextView menuTitle = (TextView) convertView.findViewById(R.id.menu_item_title_textview);

                Picasso.with(convertView.getContext()).load(menuItem.getIconDrawableId()).fit().into(menuImgage);
                menuTitle.setText(menuItem.getTitle());

            }
        }

        return convertView;
    }


}
