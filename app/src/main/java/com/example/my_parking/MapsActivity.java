package com.example.my_parking;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.my_parking.storage.FirebaseRepo;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationListener listener; // Listens for location updates
    LocationManager manager;


    public static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        index = getIntent().getIntExtra(MainActivity.INDEX_KEY, 0);

        createListener();

        handlePermissionUpdate();
    }

    private void handlePermissionUpdate() {
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1, 0, listener);
        }
    }

    private void createListener() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("all", "New location" + location);
                updateMarkers(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handlePermissionUpdate();
        }
    }

    public void updateMarkers(double lat, double lng){
        mMap.clear();
        LatLng favoriteLocation = new LatLng(FirebaseRepo.parkingSpots.get(index).getLatitude(), FirebaseRepo.parkingSpots.get(index).getLongitude());
        mMap.addMarker(new MarkerOptions().position(favoriteLocation).title(FirebaseRepo.parkingSpots.get(index).getTitle()));
        LatLng myLocation = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("My location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,5));
    }

    public void changeMapType(View view){

        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateMarkers(0,0);
    }



}
