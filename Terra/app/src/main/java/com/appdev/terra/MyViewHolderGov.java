package com.appdev.terra;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.services.helpers.PostCollection;

public class MyViewHolderGov extends RecyclerView.ViewHolder {
    private TextView header;
    private TextView nrPosts;
    private TextView status;
    private TextView reqQualifications;

    public MyViewHolderGov(View itemView) {
        super(itemView);
        header = itemView.findViewById(R.id.post_collection_header);
        nrPosts = itemView.findViewById(R.id.post_collection_nr_posts);
        status = itemView.findViewById(R.id.post_collection_status);
        reqQualifications = itemView.findViewById(R.id.post_collection_req_qualifications);
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

    public void bind(final PostCollection item, final MyAdapterGov.OnItemClickListener listener) {
        header.setText(item.getLocation().toString());
        nrPosts.setText("" + item.getNrPosts());
        status.setText(item.getAccidentStatus().toString());
        reqQualifications.setText(item.getRequestedQualifications().toString());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }
}

