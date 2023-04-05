package com.appdev.terra;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.appdev.terra.services.UpdatePostScreen;


public class MyAdapterThreadGov extends RecyclerView.Adapter<MyViewHolderThreadGov> {
    private List<Post> items;
    private Context context;

    public MyAdapterThreadGov(Context context, List<Post> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public MyViewHolderThreadGov onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new MyViewHolderThreadGov(view, context);
    }

    @Override
    public void onBindViewHolder(MyViewHolderThreadGov holder, int position) {
        Post item = items.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the UpdatePostScreen activity with the post data
                Intent intent = new Intent(holder.itemView.getContext(), UpdatePostScreen.class);
                intent.putExtra("post", item.getUsername()); //id can be implemented here later
                holder.itemView.getContext().startActivity(intent);
            }
        });
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
