package com.appdev.terra.users.citizen;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.View;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.appdev.terra.R;
import com.appdev.terra.models.PostModel;


public class MyAdapterThread extends RecyclerView.Adapter<MyViewHolderThread> {
    private List<PostModel> items;
    private Context context;

    public MyAdapterThread(Context context, List<PostModel> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public MyViewHolderThread onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new MyViewHolderThread(view, context);
    }

    @Override
    public void onBindViewHolder(MyViewHolderThread holder, int position) {
        holder.bind(items.get(position));
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