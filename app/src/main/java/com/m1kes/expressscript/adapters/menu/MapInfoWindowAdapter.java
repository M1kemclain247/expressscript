package com.m1kes.expressscript.adapters.menu;

import android.annotation.SuppressLint;

import android.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.m1kes.expressscript.R;


public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View contentsView;
    private TextView name;
    private TextView address;

    @SuppressLint("InflateParams")
    public MapInfoWindowAdapter(Fragment fragment) {
        contentsView = fragment.getActivity().getLayoutInflater()
                .inflate(R.layout.map_info_window, null);
        initUI();
    }


    /**
     * Initializes UI
     */
    private void initUI() {
        name = (TextView) contentsView.findViewById(R.id.name);
        address = (TextView) contentsView.findViewById(R.id.address);
    }

    /**
     * Sets fonts for text views on the screen
     */
    @SuppressWarnings("unused")
    private void setFonts() {
    }

    @Override
    public View getInfoContents(Marker arg0) {
        try {
            String[] splits = arg0.getTitle().split("#");

            // Name
            name.setText(splits[0]);

            // Address
            address.setText(splits[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentsView;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }
}
