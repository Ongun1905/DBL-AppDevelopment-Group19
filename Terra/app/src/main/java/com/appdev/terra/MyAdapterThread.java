package com.appdev.terra;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;



public class MyAdapterThread extends RecyclerView.Adapter<MyViewHolderThread> {
    private List<Post> items;

    public MyAdapterThread(List<Post> items) {
        this.items = items;
    }

    @Override
    public MyViewHolderThread onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_thread, parent, false);

        return new MyViewHolderThread(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderThread holder, int position) {
        holder.postText.setText(items.get(position).getPostText());
        holder.username.setText(items.get(position).getUsername());
        holder.location.setText(items.get(position).getLocation());
        holder.level.setText(items.get(position).getLevel());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Post> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}
