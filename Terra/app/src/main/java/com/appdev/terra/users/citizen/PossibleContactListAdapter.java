package com.appdev.terra.users.citizen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.R;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.UserService;

import java.util.ArrayList;

public class PossibleContactListAdapter extends RecyclerView.Adapter<PossibleContactListAdapter.MyViewHolder> {
    UserService userService = new UserService();


    Context context;
    static ArrayList<PossibleContactList> PossibleContactLists;

    static ArrayList<ContactList> AddedContactList = new ArrayList<ContactList>();



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


            addButton.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            System.out.println(PossibleContactLists.get(position).phoneNumber+"  anan");
            switch (v.getId()) {

                case R.id.button3: // Add button
                    System.out.println("I have been pressed");
                    AddedContactList.add(new ContactList(PossibleContactLists.get(position).contactName,PossibleContactLists.get(position).status, PossibleContactLists.get(position).image, PossibleContactLists.get(position).phoneNumber, PossibleContactLists.get(position).id));
                    userService.get(AccountService.logedInUserModel.id, new IFirestoreCallback<UserModel>() {
                        @Override
                        public void onCallback(UserModel model) {
                            UserModel newModel = model;
                            System.out.println(PossibleContactLists.get(position).phoneNumber + "");
                            userService.get(PossibleContactLists.get(position).id, new IFirestoreCallback<UserModel>() {
                                @Override
                                public void onCallback(UserModel personToBeAdded) {
                                    newModel.contactIds.add(personToBeAdded.id);

                                    userService.update(newModel, new IFirestoreCallback<UserModel>() {
                                        @Override
                                        public void onCallback(UserModel model1) {
                                            for (String i : model1.contactIds) {
                                                System.out.println(i);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                    addButton.setEnabled(false);


                    notifyDataSetChanged();
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        void onAddClick(int position);
    }


}