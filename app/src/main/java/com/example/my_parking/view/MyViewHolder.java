package com.example.my_parking.view;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_parking.MainActivity;
import com.example.my_parking.MapsActivity;
import com.example.my_parking.R;
import com.example.my_parking.storage.FirebaseRepo;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView textViewTitle;
    private int rowNumber = 0;

    public MyViewHolder(@NonNull final View itemView) {
        super(itemView);

        LinearLayout linearLayout = (LinearLayout) itemView;
        textViewTitle = linearLayout.findViewById(R.id.textViewTitle);

        textViewTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
            FirebaseRepo.deleteFavorite(rowNumber);
            Toast.makeText(view.getContext(), "Delete successful!", Toast.LENGTH_SHORT).show();
            return true;
            }
        });

        textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Create new intent. Get the context from the view passed as a param
            Intent intent = new Intent(view.getContext(), MapsActivity.class);
            // Add the row number to the intent, so we can get it back
            // "on the other side" in DetailActivity
            intent.putExtra(MainActivity.INDEX_KEY, rowNumber);
            // Again use the view to get the context in order to start activity
            itemView.getContext().startActivity(intent);
            }
        });

    }

    public void setData(String title) {
        textViewTitle.setText(title);
    }

    public void setPosition(int position) {
        rowNumber = position;
    }
}
