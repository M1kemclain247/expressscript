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



public class ListMenuAdapter extends ArrayAdapter<MenuItem> {

    public ListMenuAdapter(Context context, List<MenuItem> menuItems) {
        super(context, 0, menuItems);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            view = inflater.inflate(R.layout.list_item_menu, parent, false);
        }

        MenuItem menuItem = getItem(position);

        if (menuItem.getIconDrawableId() > 0) {
            ImageView iconView = (ImageView) view.findViewById(R.id.menu_item_icon);
            Picasso.with(getContext()).load(menuItem.getIconDrawableId()).fit().into(iconView);
        }

        TextView titleView = (TextView) view.findViewById(R.id.menu_item_title_textview);
        titleView.setText(menuItem.getTitle());

        TextView description = (TextView) view.findViewById(R.id.menu_item_description_textview);
        description.setText(menuItem.getDescription());
        return view;
    }


}
