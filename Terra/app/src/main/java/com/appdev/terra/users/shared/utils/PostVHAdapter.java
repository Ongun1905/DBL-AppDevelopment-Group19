package com.appdev.terra.users.shared.utils;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.appdev.terra.R;
import com.appdev.terra.models.PostModel;


public class PostVHAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private List<PostModel> items;
    private final PostVHUpdate updator;

    public PostVHAdapter(List<PostModel> items, PostVHUpdate updater) {
        this.items = items;
        this.updator = updater;
    }

    @Override
    @NonNull
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(items.get(position), updator);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<PostModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface PostVHUpdate {
        void onClick(PostModel post);
        boolean shouldHaveRedCross(PostModel post);
    }
}