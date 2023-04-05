package com.appdev.terra;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.appdev.terra.services.helpers.PostCollection;


public class MyAdapterGov extends RecyclerView.Adapter<MyViewHolderGov> {
    private List<PostCollection> items;
    private OnItemClickListener listener;

    public MyAdapterGov(List<PostCollection> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public MyViewHolderGov onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_collection_item, parent, false);

        return new MyViewHolderGov(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderGov holder, int position) {
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
