package com.appdev.terra.users.shared.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.R;
import com.appdev.terra.models.PostModel;

public class PostViewHolder extends RecyclerView.ViewHolder {
    // Text fields associated with a `list_item` GUI element
    private final TextView location;
    private final TextView level;
    private final TextView verifiedText;
    private final TextView descriptionText;
    private final TextView requirementsText;

    // A red cross to be displayed if the user of the app has usable qualifications
    private final ImageView imageView;

    // Create a new PostViewHolder and have it store references to all its GUI fields
    public PostViewHolder(View itemView) {
        super(itemView);
        location = itemView.findViewById(R.id.location_text);
        level = itemView.findViewById(R.id.emergency_text);
        verifiedText = itemView.findViewById(R.id.verified_text);
        imageView = itemView.findViewById(R.id.image_view);
        descriptionText = itemView.findViewById(R.id.description_text);
        requirementsText = itemView.findViewById(R.id.requirements_text);
    }

    // Bind a specific post to a PostViewHolder and initialize the GUI field values
    public void bind(PostModel post, PostVHAdapter.PostVHUpdate updater) {
        imageView.setVisibility( updater.shouldHaveRedCross(post) ? View.VISIBLE : View.GONE );

        // Set the correct values for the remaining text fields
        location.setText(post.getTitle(this.itemView.getContext()));
        level.setText(post.status.toString());
        verifiedText.setText(String.format("Verified: %b", post.verified));
        requirementsText.setText(post.getSelectedQuealifications().toString());
        descriptionText.setText(String.format("Description: %s", post.description));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updater.onClick(post);
            }
        });
    }
}
