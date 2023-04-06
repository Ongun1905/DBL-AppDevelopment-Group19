package com.appdev.terra.services;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.R;

public class CheckBoxViewHolder extends RecyclerView.ViewHolder {
    public CheckBox checkBox;
    public TextView qualificationName;

    public CheckBoxViewHolder(View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.qualification_check_box);
        qualificationName = itemView.findViewById(R.id.qualification_text);
    }

    public CheckBox bind(final String qualification, final CheckBoxAdapter.OnCheckBoxClickListener listener) {
        qualificationName.setText(qualification);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v.findViewById(R.id.qualification_check_box));
            }
        });

        return checkBox;
    }
}
