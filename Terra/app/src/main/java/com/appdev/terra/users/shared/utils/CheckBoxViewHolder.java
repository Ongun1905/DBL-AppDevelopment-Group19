package com.appdev.terra.users.shared.utils;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.R;

public class CheckBoxViewHolder extends RecyclerView.ViewHolder {
    // Visual fields associated with a `qualification_checkbox` GUI element
    public CheckBox checkBox;
    public TextView qualificationName;

    // Create a new CheckBoxViewHolder and have it store references to all its GUI fields
    public CheckBoxViewHolder(View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.qualification_check_box);
        qualificationName = itemView.findViewById(R.id.qualification_text);
    }

    // Bind a specific qualification to a CheckBoxViewHolder and initialize the GUI field values
    public CheckBox bind(
            final String qualification,
            final CheckBoxVHAdapter.OnCheckBoxClickListener listener
    ) {
        qualificationName.setText(qualification);

        // Specify on click behavior for the entire `qualification_checkbox` element
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v.findViewById(R.id.qualification_check_box));
            }
        });

        return checkBox;
    }
}
