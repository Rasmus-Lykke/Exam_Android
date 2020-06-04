package com.example.my_parking.storage;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_parking.MainActivity;
import com.example.my_parking.auth.FirebaseManager;
import com.example.my_parking.model.ParkingSpots;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FirebaseRepo {



    private final static String favoritesPath = "parking_spots";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static RecyclerView.Adapter adapter;

    static { // Make sure the listener starts as soon as possible
        startFavoriteListener();
    }

    public static List<ParkingSpots> parkingSpots = new ArrayList<>();

    public static void startFavoriteListener() {
        db.collection(favoritesPath)
                .whereEqualTo("user_id", FirebaseManager.getUser().getUid())                    // Get the parking spots where the current user is equal to the user id in the parking spot
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            parkingSpots.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                FirebaseRepo.parkingSpots.add(new ParkingSpots(
                                        document.getId(),
                                        document.get("title").toString(),
                                        document.get("description").toString(),
                                        document.get("latitude").toString(),
                                        document.get("longitude").toString())
                                );
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public static void deleteFavorite(int index) {
        String key = parkingSpots.get(index).getId();
        DocumentReference documentReference = FirebaseRepo.db.collection(favoritesPath).document(key);
        documentReference.delete();
        startFavoriteListener();
    }


    public static void editParkingSpot(View view, int index, String title, String description, String userId) {
        String id = parkingSpots.get(index).getId();
        // Get a Firebase ref. to the current note object.
        DocumentReference documentReference = db.collection(favoritesPath).document(id);
        Map<String, String> map = new HashMap<>();

        map.put("title", title);
        map.put("description", description);
        map.put("latitude", String.valueOf(parkingSpots.get(index).getLatitude()));
        map.put("longitude", String.valueOf(parkingSpots.get(index).getLongitude()));
        map.put("user_id", userId);

        documentReference.set(map);

        System.out.println("Update successful");
        Toast.makeText(view.getContext(), "Update successful!", Toast.LENGTH_SHORT).show();
        startFavoriteListener();
    }


    public static void saveNewNote(View view, String title, String description,
                                   String lat, String lng, String userId) {
        DocumentReference documentReference = db.collection(favoritesPath).document();

        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("description", description);
        map.put("latitude", lat);
        map.put("longitude", lng);
        map.put("user_id", userId);
        documentReference.set(map);

        // Create new intent. Get the context from the view passed as a param
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        // Again use the view to get the context in order to start activity
        view.getContext().startActivity(intent);
        startFavoriteListener();
    }

}
