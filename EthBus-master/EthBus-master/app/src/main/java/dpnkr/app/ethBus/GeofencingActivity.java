package dpnkr.app.ethBus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GeofencingActivity extends FragmentActivity implements OnMapReadyCallback{

    private static final int LOC_PERM_REQ_CODE = 1;
    private static final int GEOFENCE_RADIUS = 50;

    private GoogleMap mGoogleMap;
    private GeofencingClient geofencingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.setMinZoomPreference(15);

        showCurrentLocationOnMap();
        double latitude = 45.058686;
        double longitude = 7.652868;

        addLocationAlert(latitude, longitude);
        CircleOptions circleOptions = new CircleOptions()
                .center( new LatLng(latitude, longitude) )
                .radius( GEOFENCE_RADIUS )
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2);

        mGoogleMap.addCircle(circleOptions);

        /*mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addLocationAlert(latLng.latitude, latLng.longitude);
                CircleOptions circleOptions = new CircleOptions()
                        .center( new LatLng(latLng.latitude, latLng.longitude) )
                        .radius( GEOFENCE_RADIUS )
                        .fillColor(0x40ff0000)
                        .strokeColor(Color.TRANSPARENT)
                        .strokeWidth(2);

                mGoogleMap.addCircle(circleOptions);

            }
        });*/ //To manually add a geofence
    }
    
    @SuppressLint("MissingPermission")
    private void showCurrentLocationOnMap(){
        if (isLocationAccessPermited()){
            requestLocationAccessPermission();
        }else if (mGoogleMap != null){
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private boolean isLocationAccessPermited(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission
        .ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    private void requestLocationAccessPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOC_PERM_REQ_CODE);
    }

    @SuppressLint("MissingPermission")
    private void addLocationAlert(double lat, double lng){
        if (isLocationAccessPermited()){
            requestLocationAccessPermission();
        }else{
            String key = ""+lat+"-"+lng;
            Geofence geofence = getGeofence(lat, lng, key);
            geofencingClient.addGeofences(getGeofencingRequest(geofence),getGeofencePendingIntent()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(GeofencingActivity.this, "Location alert has added", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(GeofencingActivity.this, "Location alert could not be added", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void removeLocationAlert(){
        if (isLocationAccessPermited()) {
            requestLocationAccessPermission();
        } else {
            geofencingClient.removeGeofences(getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GeofencingActivity.this,
                                        "Location alters have been removed",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(GeofencingActivity.this,
                                        "Location alters could not be removed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOC_PERM_REQ_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCurrentLocationOnMap();
                    Toast.makeText(GeofencingActivity.this,
                            "Location access permission granted, you try " +
                                    "add or remove location allerts",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, LocationAlertIntentService.class);
        return PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofence(geofence);
        return builder.build();
    }


    private Geofence getGeofence(double lat, double lang, String key) {
        return new Geofence.Builder()
                .setRequestId(key)
                .setCircularRegion(lat, lang, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(1000)
                .build();
    }
}
