package com.appdev.terra;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.appdev.terra.services.helpers.PostCollection;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<PostCollection> items;
    private OnItemClickListener listener;

    public MyAdapter(List<PostCollection> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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
