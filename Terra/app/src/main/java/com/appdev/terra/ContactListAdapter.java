package com.appdev.terra;

import android.content.Context;
import android.location.Location;
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

import com.appdev.terra.models.PostModel;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.UserService;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    static Context context;
    static ArrayList<ContactList> contactLists;

    private static ContactListAdapter adapter;

    public ContactListAdapter (Context context, ArrayList<ContactList> contactLists ) {
        this.context = context;
        this.contactLists = contactLists;
        this.adapter = this;
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

        Button contactButton;

        PostService postService= new PostService();

        Boolean isClickable;

        PostModel postModel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.user_icon);
            textViewName = itemView.findViewById(R.id.contact_list_name);
            textViewStatus = itemView.findViewById(R.id.status_variable_text);
            editButton = itemView.findViewById(R.id.edit_button);
            contactButton = itemView.findViewById(R.id.button);
            isClickable = false;


            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        showPopupWindow(v, contactLists.get(position), adapter);
                    }

                }
            });

            postService.getAllPosts(new IFirestoreCallback<PostModel>() {
                @Override
                public void onCallback(ArrayList<PostModel> models) {
                    for (PostModel model : models) {
                       System.out.println( model.userId+" is compare too"+ contactLists.get(getAdapterPosition()).id);
                        if (model.userId == contactLists.get(getAdapterPosition()).id) {
                            isClickable = true;
                            model = postModel;
                            contactButton.getBackground().setAlpha(76);

                        }
                    }

                }
            });


            contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickable) {
                        showPopupWindowActivity(v,contactLists.get(getAdapterPosition()), adapter, postModel);
                    }

                }
            });
        }
    }

    private static void showPopupWindowActivity(View view, ContactList user, ContactListAdapter adapter, PostModel model) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_status_popup, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TextView userText = popupView.findViewById(R.id.UserText);
        userText.setText(user.contactName);

        TextView locationText = popupView.findViewById(R.id.LocationText);
        locationText.setText((CharSequence) model.location);




    }

        private static void showPopupWindow(View view, ContactList user, ContactListAdapter adapter) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.contact_details_popup, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        EditText editText = popupView.findViewById(R.id.EditUserName);
        editText.setText(user.getContactName());

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button submitButton = popupView.findViewById(R.id.SubmitEdit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ContactList contact: contactLists ) {
                    if (contact.phoneNumber.equals(user.phoneNumber)) {
                        System.out.println("ss");
                        contact.setContactName(String.valueOf(editText.getText()));
                    }
                }
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });
    }


}

