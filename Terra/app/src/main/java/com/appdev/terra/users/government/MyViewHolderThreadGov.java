package com.appdev.terra.users.government;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.R;
import com.appdev.terra.models.PostModel;

public class MyViewHolderThreadGov extends RecyclerView.ViewHolder {
    public PostModel item;

    public TextView location;
    public TextView level;
    public TextView verifiedText;
    public ImageView imageView;
    public TextView descriptionText;
    public TextView requirementsText;

    public MyViewHolderThreadGov(View itemView, Context context) {
        super(itemView);
        location = itemView.findViewById(R.id.location_text);
        level = itemView.findViewById(R.id.emergency_text);
        verifiedText = itemView.findViewById(R.id.verified_text);
        imageView = itemView.findViewById(R.id.image_view);
        descriptionText = itemView.findViewById(R.id.description_text);
        requirementsText = itemView.findViewById(R.id.requirements_text);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the UpdatePostScreen activity with the post data
                Intent intent = new Intent(context, UpdatePostScreen.class);
                intent.putExtra("geoPointId", item.geoId);
                intent.putExtra("userId", item.userId);
                context.startActivity(intent);
            }
        });
    }

    public void bind(PostModel post) {
        this.item = post;

        location.setText(post.getTitle(this.itemView.getContext()));
        level.setText(item.status.toString());
        verifiedText.setText("Verified: " + item.verified);
        descriptionText.setText("Description: " + item.description);
        requirementsText.setText(item.getSelectedQuealifications().toString());
        imageView.setVisibility(View.GONE);
    }
}
