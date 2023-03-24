package com.appdev.terra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    Context context;
    ArrayList<ContactList> contactLists;

    public ContactListAdapter (Context context, ArrayList<ContactList> contactLists) {
        this.context = context;
        this.contactLists = contactLists;
    }

    @NonNull
    @Override
    public ContactListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.individual_contact, parent, false);
        return new ContactListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.MyViewHolder holder, int position) {
       holder.textViewName.setText(contactLists.get(position).getContactName());
       holder.textViewStatus.setText(contactLists.get(position).getStatus());
       holder.imageView.setImageResource(contactLists.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return contactLists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewName;
        TextView textViewStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView3);
            textViewName = itemView.findViewById(R.id.contact_list_name);
            textViewStatus = itemView.findViewById(R.id.status);

        }
    }
}
