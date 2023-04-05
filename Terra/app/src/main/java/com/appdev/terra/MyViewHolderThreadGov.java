package com.appdev.terra;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.Post;
import com.appdev.terra.services.UpdatePostScreen;

public class MyViewHolderThreadGov extends RecyclerView.ViewHolder {
    public TextView postText;
    public TextView username;
    public TextView location;
    public TextView level;
    public TextView verifiedText;
    public ImageView imageView;
    public Post item;

    public MyViewHolderThreadGov(View itemView, Context context) {
        super(itemView);
        username = itemView.findViewById(R.id.username_text);
        location = itemView.findViewById(R.id.location_text);
        level = itemView.findViewById(R.id.emergency_text);
        verifiedText = itemView.findViewById(R.id.verified_text);
        imageView = itemView.findViewById(R.id.image_view);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the UpdatePostScreen activity with the post data
                Intent intent = new Intent(context, UpdatePostScreen.class);
                intent.putExtra("post", item.getUsername());
                context.startActivity(intent);
            }
        });
    }

    public void bind(Post item) {
        this.item = item;
        if (item.requirementsMet() == true) { // replace with your own condition
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        username.setText(item.getUsername());
        location.setText(item.getLocation());
        level.setText(item.getLevel());
        // bind other views here
    }
}

