package com.appdev.terra;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.UpdatePostScreen;

public class MyViewHolderThread extends RecyclerView.ViewHolder {
    public TextView username;
    public TextView location;
    public TextView level;
    public TextView verifiedText;
    public ImageView imageView;
    public PostModel item;
    public TextView descriptionText;
    public TextView requirementsText;

    public MyViewHolderThread(View itemView, Context context) {
        super(itemView);
        username = itemView.findViewById(R.id.username_text);
        location = itemView.findViewById(R.id.location_text);
        level = itemView.findViewById(R.id.emergency_text);
        verifiedText = itemView.findViewById(R.id.verified_text);
        imageView = itemView.findViewById(R.id.image_view);
        descriptionText = itemView.findViewById(R.id.description_text);
        requirementsText = itemView.findViewById(R.id.requirements_text);
    }

    public void bind(PostModel post) {
        this.item = post;
        if (true) { // TODO: Check with user qualifications
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        username.setText("J U I N");
        location.setText(item.geoId);
        level.setText(item.status.toString());
        verifiedText.setText("Verified: " + item.verified);
        descriptionText.setText("Description: " + item.description);
        requirementsText.setText(item.getSelectedQuealifications().toString());
        // bind other views here
    }
}
