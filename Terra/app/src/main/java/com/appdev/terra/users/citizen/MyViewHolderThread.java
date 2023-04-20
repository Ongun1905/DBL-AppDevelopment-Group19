package com.appdev.terra.users.citizen;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.R;
import com.appdev.terra.enums.QualificationsEnum;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.AccountService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyViewHolderThread extends RecyclerView.ViewHolder {
    public TextView location;
    public TextView level;
    public TextView verifiedText;
    public ImageView imageView;
    public PostModel item;
    public TextView descriptionText;
    public TextView requirementsText;

    public MyViewHolderThread(View itemView, Context context) {
        super(itemView);
        location = itemView.findViewById(R.id.location_text);
        level = itemView.findViewById(R.id.emergency_text);
        verifiedText = itemView.findViewById(R.id.verified_text);
        imageView = itemView.findViewById(R.id.image_view);
        descriptionText = itemView.findViewById(R.id.description_text);
        requirementsText = itemView.findViewById(R.id.requirements_text);
    }

    public void bind(PostModel post) {
        this.item = post;

        System.out.println(post.qualifications);

        boolean shouldShowCross = false;

        for (QualificationsEnum qualification : QualificationsEnum.values()) {
            if (
                    post.qualifications.get(qualification.toString())
                    && AccountService.logedInUserModel.qualifications.get(qualification)
            ) {
                shouldShowCross = true;
                break;
            }
        }

        if (shouldShowCross) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        location.setText(item.location.toString());

        Geocoder geocoder = new Geocoder(this.itemView.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(item.location.getLatitude(), item.location.getLongitude(),1);

            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            location.setText(add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        level.setText(item.status.toString());
        verifiedText.setText("Verified: " + item.verified);
        requirementsText.setText(item.getSelectedQuealifications().toString());
        descriptionText.setText("Description: " + item.description);
    }
}
