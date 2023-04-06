package com.appdev.terra;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    static Context context;
    static ArrayList<ContactList> contactLists;

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
        Button editButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.user_icon);
            textViewName = itemView.findViewById(R.id.contact_list_name);
            textViewStatus = itemView.findViewById(R.id.status_variable_text);
            editButton = itemView.findViewById(R.id.edit_button);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String userName = contactLists.get(position).getContactName();
                        showPopupWindow(v, userName);
                    }

                }
            });
        }
    }

    private static void showPopupWindow(View view, String userName) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.contact_details_popup, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        EditText editText = popupView.findViewById(R.id.EditUserName);
        editText.setText(userName);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button submitButton = popupView.findViewById(R.id.SubmitEdit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
    }


}

