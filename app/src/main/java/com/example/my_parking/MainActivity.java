package com.example.my_parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.my_parking.adapter.MyAdapter;
import com.example.my_parking.auth.FirebaseManager;
import com.example.my_parking.storage.FirebaseRepo;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String INDEX_KEY = "INDEX_KEY";

    MyAdapter myAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter); // Assign the adapter to recyclerView
        FirebaseRepo.adapter = myAdapter;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        System.out.println(position);
    }

    public void newParkingSpot(View view) {
        if (MapsActivity.currentLocation == null) {
            Toast.makeText(getApplicationContext(), "Make sure your location shows in one of the favorite parking spots", Toast.LENGTH_SHORT).show();
        } else {
            // Create new intent. Get the context from the view passed as a param
            Intent intent = new Intent(this, CreateParkingSpotActivity.class);
            // Again use the view to get the context in order to start activity
            startActivity(intent);
        }
    }

    public void signOut(View view){
        try {
            FirebaseManager.getInstance().signOut();
            finish();
            // Create new intent. Get the context from the view passed as a param
            Intent intent = new Intent(this, SignInActivity.class);
            // Again use the view to get the context in order to start activity
            startActivity(intent);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }
}
