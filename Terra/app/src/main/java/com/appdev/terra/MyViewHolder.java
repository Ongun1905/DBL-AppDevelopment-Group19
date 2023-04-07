package com.appdev.terra;

import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.services.helpers.PostCollection;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyViewHolder extends RecyclerView.ViewHolder {
    private TextView header;
    private TextView nrPosts;
    private TextView status;
    private TextView reqQualifications;

    public MyViewHolder(View itemView) {
        super(itemView);
        header = itemView.findViewById(R.id.post_collection_header);
        nrPosts = itemView.findViewById(R.id.post_collection_nr_posts);
    }

    public void setHeader(CharSequence text) {
        header.setText(text);
    }

    public void setNrPosts(CharSequence text) {
        nrPosts.setText("Number of posts: " + text);
    }

    public void setStatus(CharSequence text) {
        status.setText("Accident status: " + text);
    }

    public void setReqQualifications(CharSequence text) {
        reqQualifications.setText("Requested qualifications: " + text);
    }

    public void bind(final PostCollection item, final MyAdapter.OnItemClickListener listener) {
        header.setText(item.getLocationName(this.itemView.getContext()));

        nrPosts.setText(item.getNrPosts() + " post(s) for this accident");

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }
}

