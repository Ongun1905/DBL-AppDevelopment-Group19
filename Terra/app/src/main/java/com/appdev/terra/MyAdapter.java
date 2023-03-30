package com.appdev.terra;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.appdev.terra.services.helpers.PostCollection;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<PostCollection> items;

    public MyAdapter(List<PostCollection> items) {
        this.items = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // TODO: Please update with correct information
        holder.postText.setText(items.get(position).getLocation().toString());
        holder.location.setText("" + items.get(position).getNrPosts());
        holder.username.setText(items.get(position).getAccidentStatus().toString());
        holder.level.setText(items.get(position).getRequestedQualifications().toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<PostCollection> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}
