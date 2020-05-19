package com.example.my_parking.view;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_parking.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView textViewTitle;
    private EditText textViewComment;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        LinearLayout linearLayout = (LinearLayout) itemView;

        textViewTitle = linearLayout.findViewById(R.id.textViewTitle);
        textViewComment = linearLayout.findViewById(R.id.editTextComment);

    }

    public void setData(String title, String comment) {

        textViewTitle.setText(title);
        textViewComment.setText(comment);
    }
}
