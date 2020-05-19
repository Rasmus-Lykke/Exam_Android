package com.example.my_parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.my_parking.adapter.MyAdapter;
import com.example.my_parking.auth.FirebaseManager;
import com.example.my_parking.storage.FirebaseRepo;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(new FirebaseRepo());
        recyclerView.setAdapter(myAdapter); // Assign the adapter to recyclerView
    }

    public void signOut(View view) {
        FirebaseManager.getInstance().signOut();
        finish();
    }
}
