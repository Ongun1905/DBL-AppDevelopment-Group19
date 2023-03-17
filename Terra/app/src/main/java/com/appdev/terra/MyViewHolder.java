package com.appdev.terra;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView postText;
    public TextView username;
    public TextView location;
    public TextView level;

    public MyViewHolder(View itemView) {
        super(itemView);
        postText = itemView.findViewById(R.id.post_text);
        username = itemView.findViewById(R.id.username_text);
        location = itemView.findViewById(R.id.location_text);
        level = itemView.findViewById(R.id.emergency_text);
    }
}

