package com.appdev.terra;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;



public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<ThreadPost> items;

    public MyAdapter(List<ThreadPost> items) {
        this.items = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_thread, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.location.setText(items.get(position).getLocation());

        CardView postCardView = holder.itemView.findViewById(R.id.postCardView);
        String loc = items.get(position).getLocation();
        postCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the new UI page and pass necessary data
                Intent intent = new Intent(holder.itemView.getContext(), PostThreadActivity.class);
                intent.putExtra("post_id", loc);

                // Start the new activity
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ThreadPost> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}
