package com.appdev.terra.users.shared.utils;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.appdev.terra.R;
import com.appdev.terra.services.helpers.PostCollection;


public class PostCollectionVHAdapter extends RecyclerView.Adapter<PostCollectionViewHolder> {
    private List<PostCollection> items;
    private OnItemClickListener listener;

    public PostCollectionVHAdapter(List<PostCollection> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public PostCollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.post_collection_item, parent, false);

        return new PostCollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostCollectionViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<PostCollection> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(PostCollection item);
    }
}
