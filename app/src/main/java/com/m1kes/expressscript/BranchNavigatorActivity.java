package com.m1kes.expressscript;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.m1kes.expressscript.adapters.menu.BranchAgentListAdapter;
import com.m1kes.expressscript.fragments.LocationMapFragment;
import com.m1kes.expressscript.objects.Branch;
import com.m1kes.expressscript.objects.BranchDistance;
import com.m1kes.expressscript.utils.extra.Utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BranchNavigatorActivity extends ListActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = BranchNavigatorActivity.class.getSimpleName();

    private String locationType;
    public static final String EXTRA_LOCATION_TYPE = "LOCATION_TYPE";
    private GoogleApiClient googleApiClient;
    private List<Branch> locations;

    private Location currentLocation;
    private LocationRequest locationRequest;
    private Button showOnMapButton;
    private List<BranchDistance> closestBranches;
    private TextView headingTextView;
    private static int REQUEST_LOCATION_PERMISSIONS = 1;

    private static long MIN_LOCATION_TIME_RESOLUTION = 2 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_navigator);

        showOnMapButton = (Button) findViewById(R.id.location_show_map_button);
        showOnMapButton.setOnClickListener(this);
        headingTextView = (TextView) findViewById(R.id.location_header_textview);

        if (savedInstanceState != null) {
            locationType = savedInstanceState.getString(EXTRA_LOCATION_TYPE);
        }

        locationType = getIntent().getStringExtra(EXTRA_LOCATION_TYPE);
        String headingName = "";
        locations = new ArrayList<>();
        locations.add(new Branch("TEst1","Harare","Harare",14.5,17.9));
        locations.add(new Branch("TEst2","Gweru","Harare",18.5,40.9));
        locations.add(new Branch("TEst3","Bulawyo","Harare",19.5,27.1));
        locations.add(new Branch("TEst4","Bindura","Harare",24.5,27.9));
        locations.add(new Branch("TEst5","Gwanda","Harare",30.5,30.9));

        if (locations == null) {
            //TODO we need to
        }


        //Prepare the client and location request that could be used
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(15000)
                .setFastestInterval(5000);
    }

    private void showDialogGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please enable GPS");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        BranchDistance branchDistance = closestBranches.get(position);
        ArrayList<BranchDistance> singleItem = new ArrayList<>(Arrays.asList(branchDistance));
        showOnMap(singleItem);

    }

    protected void showOnMap(List<BranchDistance> branchDistances) {



        LocationMapFragment fragmentManager = LocationMapFragment.newInstance(
                Utils.convertBranchToMapLocation(branchDistances), "Closest Branches", true);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentManager)
                .addToBackStack(null)
                .commit();

    }

    @Override
    protected void onResume() {

        /**
         * We are adding the .connect here as in the android lifecycle, onResume is called directly
         * after onCreate. By having it here, makes sure we always obtain a connection whether its
         * from a new Intent or if the activity was destroyed because of device orientation
         */
        super.onResume();
        LocationManager mlocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!enabled) {
            showDialogGPS();
        }
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_LOCATION_TYPE, locationType);
    }

    @Override
    public void onClick(View v) {

        if (R.id.location_show_map_button == v.getId()) {
            if (closestBranches != null && !closestBranches.isEmpty()) {
                showOnMap(closestBranches);
                return;
            }

        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Location services connected!");
        //Before we can query for locations we need to get user permission first (newer android)
        if (!Utils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) || !Utils.checkPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {

            Utils.requestPemrission(this, REQUEST_LOCATION_PERMISSIONS, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

        } else {
            handleOnConnected();
        }

    }

    protected boolean isStaleLocation(Location location) {

        long timeDiff = System.currentTimeMillis() - location.getTime();
        return timeDiff > MIN_LOCATION_TIME_RESOLUTION;

    }

    protected void handleOnConnected() {

        try {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (currentLocation == null || isStaleLocation(currentLocation)) {
                requestUpToDateLocation();
            } else {

                long currentTime = System.currentTimeMillis();
                // VLog.d(TAG, "LastLocation time={0,number,#} currentTime={1,number,#}", currentLocation.getTime(), currentTime);
                long timeDif = currentTime - currentLocation.getTime();
                String provider = currentLocation.getProvider();

                Log.d(TAG, MessageFormat.format("LastLocation found: time={0,number,#} timediff={1,number,#} accuracy={2} provider={3}",
                        currentLocation.getTime(), timeDif, currentLocation.getAccuracy(), provider));
                handleNewLocation(currentLocation);
            }
        } catch (SecurityException e) {
            //Tricking IntelliJ
        }

    }

    private void requestUpToDateLocation() {

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException e) {
            //Tricking IntelliJ
            e.printStackTrace();
        }
    }

    private void handleNewLocation(Location location) {

        Log.i(TAG, "Received location, attempting to find closest");

        currentLocation = location;
        List<BranchDistance> branchDistances = new ArrayList<>();
        for (Branch b : locations) {

            if (b.getLatitude() != 0 && b.getLongitude() != 0) {
                float[] results = new float[1];
                Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                        b.getLatitude(), b.getLongitude(), results);
                branchDistances.add(new BranchDistance(b, (double) results[0]));
            }
        }

        Collections.sort(branchDistances, new Comparator<BranchDistance>() {
            @Override
            public int compare(BranchDistance bd1, BranchDistance bd2) {
                return bd1.getDistance().compareTo(bd2.getDistance());
            }
        });
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        //Set the closest locations now
        closestBranches = branchDistances.subList(0, 5);
        setListAdapter(new BranchAgentListAdapter(this, closestBranches));

    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d(TAG, "Location services connection suspended!");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_LOCATION_PERMISSIONS == requestCode && grantResults.length > 0) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                handleOnConnected();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

}
