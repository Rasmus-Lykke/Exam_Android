package com.example.my_parking.storage;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_parking.MainActivity;
import com.example.my_parking.model.Favorite;
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

    public static List<Favorite> favorites = new ArrayList<>();

    private final static String favoritesPath = "favorites";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static RecyclerView.Adapter adapter;

    static { // Make sure the listener starts as soon as possible
        startNoteListener();
    }

    private static void startNoteListener() {
        FirebaseRepo.db.collection(favoritesPath).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e) {
                FirebaseRepo.favorites.clear();
                for (DocumentSnapshot snap: queryDocumentSnapshots.getDocuments()){
                    FirebaseRepo.favorites.add(new Favorite(
                            snap.get("title").toString(),
                            snap.get("comment").toString(),
                            snap.getId(),
                            snap.get("longitude").toString(),
                            snap.get("latitude").toString()));
                    System.out.println("--> Notes size: " + FirebaseRepo.favorites.size());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public static Favorite getFavorite(int index){
        if(index >= favorites.size()) return new Favorite("", "", "", "", "");
        return favorites.get(index);
    }

    public static void deleteFavorite(int index) {
        String key = favorites.get(index).getId();
        DocumentReference documentReference = FirebaseRepo.db.collection(favoritesPath).document(key);
        documentReference.delete();
        adapter.notifyDataSetChanged();
    }

    public static void editNote(int index, String title, String body) {
        String id = favorites.get(index).getId();
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
