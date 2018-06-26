package com.m1kes.expressscript.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.m1kes.expressscript.R;
import com.m1kes.expressscript.adapters.menu.MapInfoWindowAdapter;
import com.m1kes.expressscript.utils.extra.MapLocation;

import java.util.ArrayList;
import java.util.List;



public class LocationMapFragment extends Fragment implements OnMapReadyCallback {


    private static final String LOCATIONS = "locations";
    private static final String TITLE = "title";
    private static final String SHOW_SELF = "show.self";

    private GoogleMap googleMap;
    private View rootView;
    private String title;
    private MapView mapView;
    private boolean showSelf;

    private List<MapLocation> locations;

    public static LocationMapFragment newInstance(ArrayList<MapLocation> locations, String title, boolean showSelf) {
        LocationMapFragment frag = new LocationMapFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LOCATIONS, locations);
        args.putString(TITLE, title);
        args.putBoolean(SHOW_SELF, showSelf);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            locations = getArguments().getParcelableArrayList(LOCATIONS);
            title = getArguments().getString(TITLE);
            showSelf = getArguments().getBoolean(SHOW_SELF);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location_map, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {

            MapsInitializer.initialize(getActivity());

        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Open native google maps to show direction to the start location
     */
    public void showDirections(final String location) {


        if (location != null) {
            String uri = "http://maps.google.com/maps?saddr=&daddr=" + location;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                    .parse(uri));
            intent.setClassName("com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }

    }

    /**
     * Shows locations on map
     *
     * @param locations
     */
    private void showLocations(final List<MapLocation> locations) {

        if (googleMap == null)
            return;

        if (locations == null)
            return;

        if (googleMap != null)
            googleMap.clear();

        LatLngBounds.Builder builderOfBounds = new LatLngBounds.Builder();
        LatLng latlng = new LatLng(0,0);
        for (int i = 0; i < locations.size(); i++) {

            double latitude = locations.get(i).getLatitude();
            double longitude = locations.get(i).getLongitude();

            if ((latitude != 0 && longitude != 0)
                    && (!Double.isNaN(latitude) && !Double.isNaN(longitude))) {


                 latlng = new LatLng(latitude, longitude);
                String address = locations.get(i).getAddress();

                String name = locations.get(i).getName();
                String markerData = name + "#" + address;

				/* Marker marker = */

                googleMap.addMarker(new MarkerOptions()
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                        .anchor(1.0f / 2.0f, 1f).title(markerData)
                        .snippet(Double.toString(latitude) + "," + Double.toString(longitude))
                        .position(latlng));
                builderOfBounds.include(latlng);
            }

            try {
                if (locations != null && locations.size() > 0) {
                    final LatLngBounds bounds = builderOfBounds.build();
                    final LatLng finalLatlng = latlng;
                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            if(locations.size() > 1) {
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,
                                        (int) getResources().getDimension(R.dimen.marker_pading)));
                            }else{

                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(finalLatlng, 15f));

                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        if (googleMap == null)
            return;

        //TODO need to issue runtime permissions for newer Android phones
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(showSelf);
        googleMap.setInfoWindowAdapter(new MapInfoWindowAdapter(this));
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                String location = marker.getSnippet();
                showDirections(location);
            }
        });
        showLocations(locations);
    }
}
