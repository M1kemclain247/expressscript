package com.m1kes.expressscript.adapters.menu;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.m1kes.expressscript.R;
import com.m1kes.expressscript.objects.Branch;
import com.m1kes.expressscript.objects.BranchDistance;
import com.m1kes.expressscript.utils.extra.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.m1kes.expressscript.utils.extra.Utils.findImageView;
import static com.m1kes.expressscript.utils.extra.Utils.findTextView;


public class BranchAgentListAdapter<T extends Branch> extends ArrayAdapter<T> {


    private LayoutInflater inflater;

    public BranchAgentListAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
        inflater = ((Activity) context).getLayoutInflater();
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_location, parent, false);
            holder = new ViewHolder();
            holder.locationNameTextView = findTextView(convertView, R.id.location_name_textview);
            holder.locationAddressTextView = findTextView(convertView, R.id.location_address_textview);
            holder.locationImageView = findImageView(convertView, R.id.location_imageview);
            holder.locationSeperator = convertView.findViewById(R.id.dash_seperator);
            holder.distanceTextView = findTextView(convertView, R.id.location_distance_textview);
            holder.distanceContainer = (LinearLayout) convertView.findViewById(R.id.location_distance_container);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Branch branch = getItem(position);
        holder.locationNameTextView.setText(branch.getName());
        holder.locationAddressTextView.setText(branch.getAddress());
        Picasso.with(convertView.getContext()).load(R.drawable.icon_locator).fit().into(holder.locationImageView);
        //Fix the seperator layout

        if (branch instanceof BranchDistance) {
            double distance = ((BranchDistance) branch).getDistance();
            holder.distanceTextView.setText(Utils.formatMetersToKm(distance));
        } else {
            holder.distanceContainer.setVisibility(View.GONE);
        }

        holder.locationSeperator.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        return convertView;
    }


    static class ViewHolder {

        private TextView locationNameTextView;
        private TextView locationAddressTextView;
        private ImageView locationImageView;
        private View locationSeperator;
        private TextView distanceTextView;
        private LinearLayout distanceContainer;
    }
}
