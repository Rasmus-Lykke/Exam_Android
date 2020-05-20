package com.example.my_parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.my_parking.adapter.MyAdapter;
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
}
