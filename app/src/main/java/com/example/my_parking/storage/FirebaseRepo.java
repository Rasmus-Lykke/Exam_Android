package com.example.my_parking.storage;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_parking.MainActivity;
import com.example.my_parking.model.ParkingSpots;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseRepo {

    public static List<ParkingSpots> parkingSpots = new ArrayList<>();

    private final static String favoritesPath = "parking_spots";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static RecyclerView.Adapter adapter;

    static { // Make sure the listener starts as soon as possible
        startFavoriteListener();
    }

    private static void startFavoriteListener() {
        FirebaseRepo.db.collection(favoritesPath).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e) {
                FirebaseRepo.parkingSpots.clear();
                for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                    FirebaseRepo.parkingSpots.add(new ParkingSpots(
                            snap.getId(),
                            snap.get("title").toString(),
                            snap.get("description").toString(),
                            snap.get("latitude").toString(),
                            snap.get("longitude").toString())
                    );
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public static void deleteFavorite(int index) {
        String key = parkingSpots.get(index).getId();
        DocumentReference documentReference = FirebaseRepo.db.collection(favoritesPath).document(key);
        documentReference.delete();
        adapter.notifyDataSetChanged();
    }

    public static void editNote(int index, String title, String body) {
        String id = parkingSpots.get(index).getId();
        // Get a Firebase ref. to the current note object.
        DocumentReference documentReference = FirebaseRepo.db.collection(favoritesPath).document(id);
        Map<String, String> map = new HashMap<>();

        map.put("headline", title);
        map.put("body", body);

        documentReference.set(map);
    }

    public static void saveNewNote(View v, String title, String body) {
        DocumentReference documentReference = FirebaseRepo.db.collection(favoritesPath).document();

        Map<String, String> map = new HashMap<>();
        map.put("headline", title);
        map.put("body", body);
        documentReference.set(map);

        // Create new intent. Get the context from the view passed as a param
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        // Again use the view to get the context in order to start activity
        v.getContext().startActivity(intent);
    }

}
