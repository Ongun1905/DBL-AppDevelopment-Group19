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

public class MyViewHolderGov extends RecyclerView.ViewHolder {
    private TextView header;
    private TextView nrPosts;
    private TextView status;
    private TextView reqQualifications;

    public MyViewHolderGov(View itemView) {
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

    public void bind(final PostCollection item, final MyAdapterGov.OnItemClickListener listener) {
        header.setText(item.getLocation().toString());

        Geocoder geocoder = new Geocoder(this.itemView.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(item.getLatitude(), item.getLongitude(),1);

            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            header.setText(add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        nrPosts.setText(item.getNrPosts() + " post(s) for this accident");

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }
}

