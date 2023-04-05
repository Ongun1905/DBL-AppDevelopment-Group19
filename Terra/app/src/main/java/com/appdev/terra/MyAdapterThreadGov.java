package com.appdev.terra;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.UpdatePostScreen;


public class MyAdapterThreadGov extends RecyclerView.Adapter<MyViewHolderThreadGov> {
    private List<PostModel> items;
    private Context context;

    public MyAdapterThreadGov(Context context, List<PostModel> items) {
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
        PostModel item = items.get(position);

        holder.bind(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the UpdatePostScreen activity with the post data
                Intent intent = new Intent(holder.itemView.getContext(), UpdatePostScreen.class);
                // TODO: add relevant fields to the intent
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<PostModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
