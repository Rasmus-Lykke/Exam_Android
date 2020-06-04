package com.example.my_parking;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_parking.auth.FirebaseManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.my_parking.storage.FirebaseRepo;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationListener listener; // Listens for location updates
    LocationManager manager;
    public static LatLng currentLocation;
    public LatLng parkingLocation;

    public static int index;

    EditText editTextTitle;
    EditText editTextDescription;
    TextView textViewAddress;
    TextView textViewCity;
    TextView textViewCountry;
    Button buttonChangeMap;

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

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextTitle.setText(FirebaseRepo.parkingSpots.get(index).getTitle());
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDescription.setText(FirebaseRepo.parkingSpots.get(index).getDescription());


        textViewCity = findViewById(R.id.textViewCity);
        textViewAddress =  findViewById(R.id.textViewAddress);
        textViewCountry = findViewById(R.id.textViewCountry);


        buttonChangeMap = findViewById(R.id.buttonChangeMap);
        buttonChangeMap.setText("Satellite");


        handlePermissionUpdate();
        createListener();
    }

    private void handlePermissionUpdate() {
        System.out.println("=======>> Checking permission");
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            System.out.println("=======>> Requesting location update");
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 10, listener);
        }
    }

    private void createListener() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("all", "=======>> New location" + location);
                currentLocation = (new LatLng(location.getLatitude(), location.getLongitude()));
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(
                            FirebaseRepo.parkingSpots.get(index).getLatitude(),
                            FirebaseRepo.parkingSpots.get(index).getLongitude(), 1);

                    if (listAddresses != null && listAddresses.size() > 0) {
                        String address = "";
                        String city = "";
                        String country = "";

                        if (listAddresses.get(0).getThoroughfare() != null) {
                            address += listAddresses.get(0).getThoroughfare() + " ";
                        }

                        if (listAddresses.get(0).getLocality() != null) {
                            city += listAddresses.get(0).getLocality() + ", ";
                            System.out.println("Locality: " + listAddresses.get(0).getLocality());
                        }

                        if (listAddresses.get(0).getPostalCode() != null) {
                            city += listAddresses.get(0).getPostalCode() + " ";
                            System.out.println("Postal code: " + listAddresses.get(0).getPostalCode());
                        }

                        if (listAddresses.get(0).getCountryName() != null) {
                            country += listAddresses.get(0).getCountryName();
                            System.out.println("Admin area: " + listAddresses.get(0).getAdminArea());
                        }

                        textViewAddress.setText(address);
                        textViewCity.setText(city);
                        textViewCountry.setText(country);

                        updateMarkers();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) { }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handlePermissionUpdate();
        }
    }

    public void updateMarkers() {
        System.out.println("=======>> Updating markers");
        mMap.clear();
        parkingLocation = (new LatLng(
                FirebaseRepo.parkingSpots.get(index).getLatitude(),
                FirebaseRepo.parkingSpots.get(index).getLongitude()));
        mMap.addMarker(new MarkerOptions().position(parkingLocation).
                title(FirebaseRepo.parkingSpots.get(index).getTitle()));

        if (currentLocation != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .title("My location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            Toast.makeText(this, "Success, found your location!", Toast.LENGTH_SHORT).show();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
        }
    }

    public void changeMapType(View view) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            buttonChangeMap.setText("Normal");
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            buttonChangeMap.setText("Satellite");
        }
    }

    public void editParkingSpot(View view){
        FirebaseRepo.editParkingSpot(view, index,
                editTextTitle.getText().toString(),
                editTextDescription.getText().toString(),
                FirebaseManager.getUser().getUid());
    }

    public void openInMaps(View view){ // Not necessary because it is implemented in the maps layout if you click on a marker.
        if (parkingLocation != null) {
            startActivity(
                    new Intent(
                            android.content.Intent.ACTION_VIEW,
                            Uri.parse("geo:" +
                                    parkingLocation.latitude + "," +
                                    parkingLocation.longitude + "?q=" +
                                    parkingLocation.latitude + "," +
                                    parkingLocation.longitude )));
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(56.2928882, 10.4692549), 6));
        Toast.makeText(this, "Searching for location, please wait", Toast.LENGTH_SHORT).show();
        updateMarkers();
    }
}
