package com.appdev.terra.users.shared.utils;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.R;
import com.appdev.terra.services.helpers.PostCollection;

public class PostCollectionViewHolder extends RecyclerView.ViewHolder {
    // Text fields associated with a `list_item_thread` GUI element
    private final TextView header;
    private final TextView nrPosts;

    // Create a new PostCollectionViewHolder and have it store references to all its GUI fields
    public PostCollectionViewHolder(View itemView) {
        super(itemView);
        header = itemView.findViewById(R.id.post_collection_header);
        nrPosts = itemView.findViewById(R.id.post_collection_nr_posts);
    }

    // Bind a specific post to a PostCollectionViewHolder and initialize the GUI field values
    public void bind(final PostCollection item, final PostCollectionVHAdapter.OnItemClickListener listener) {
        header.setText(item.getLocationName(this.itemView.getContext()));

        nrPosts.setText(String.format("%s post(s) for this accident", item.getNrPosts()));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }
}

