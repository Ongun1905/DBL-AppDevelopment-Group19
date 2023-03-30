package com.appdev.terra;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView location;


    public MyViewHolder(View itemView) {
        super(itemView);

        location = itemView.findViewById(R.id.location_text);

    }
}

