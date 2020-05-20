package com.example.my_parking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.my_parking.storage.FirebaseRepo;
import com.google.android.gms.maps.model.LatLng;

import java.util.NavigableMap;

public class CreateParkingSpotActivity extends AppCompatActivity {

    EditText editTextTitle;
    EditText editTextDescription;

    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parking_spot);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
    }

    public void createParkingSpot(View view) {

        lat = MapsActivity.currentLocation.latitude;
        lng = MapsActivity.currentLocation.longitude;

        String latitude = String.valueOf(lat);
        String longitude = String.valueOf(lng);
        FirebaseRepo.saveNewNote(view,
                editTextTitle.getText().toString(),
                editTextDescription.getText().toString(),
                latitude, longitude);

    }
}
