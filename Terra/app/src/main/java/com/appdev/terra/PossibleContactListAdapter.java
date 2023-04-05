package com.appdev.terra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PossibleContactListAdapter extends RecyclerView.Adapter<PossibleContactListAdapter.MyViewHolder> {

    Context context;
    static ArrayList<PossibleContactList> PossibleContactLists;

    static ArrayList<ContactList> AddedContactList = new ArrayList<ContactList>();

    static ArrayList<PossibleContactList> DeletedContactList = new ArrayList<PossibleContactList>();;

    private OnItemClickListener listener;

    public PossibleContactListAdapter(Context context, ArrayList<PossibleContactList> contactLists) {
        this.context = context;
        this.PossibleContactLists = contactLists;
        this.listener = listener;

    }

    @NonNull
    @Override
    public PossibleContactListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.individual_possible_contact, parent, false);
        return new PossibleContactListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PossibleContactListAdapter.MyViewHolder holder, int position) {
       holder.textViewName.setText(PossibleContactLists.get(position).getContactName());
       holder.textViewStatus.setText(PossibleContactLists.get(position).getStatus());
       holder.imageView.setImageResource(PossibleContactLists.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return PossibleContactLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textViewName;
        TextView textViewStatus;
        Button addButton;
        Button deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.user_icon);
            textViewName = itemView.findViewById(R.id.contact_list_name);
            textViewStatus = itemView.findViewById(R.id.status_variable_text);
            addButton = itemView.findViewById(R.id.button3);
            deleteButton = itemView.findViewById(R.id.button4);

            addButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            switch (v.getId()) {
                case R.id.button3: // Add button
                    System.out.println("I have been pressed");

                    ContactList addedContact = new ContactList(PossibleContactLists.get(position).contactName, PossibleContactLists.get(position).status, PossibleContactLists.get(position).image,PossibleContactLists.get(position).phoneNumber);
                    AddedContactList.add(addedContact);
                    PossibleContactLists.remove(position);
                    notifyDataSetChanged();
                    break;
                case R.id.button4: // Delete button

                    PossibleContactList deletedContact = new PossibleContactList(PossibleContactLists.get(position).contactName, PossibleContactLists.get(position).status, PossibleContactLists.get(position).image, PossibleContactLists.get(position).phoneNumber);
                    DeletedContactList.add(deletedContact);
                    PossibleContactLists.remove(position);
                    notifyDataSetChanged();
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        void onAddClick(int position);
    }
}
