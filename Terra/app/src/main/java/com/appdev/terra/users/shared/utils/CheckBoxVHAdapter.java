package com.appdev.terra.users.shared.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.R;
import com.appdev.terra.enums.QualificationsEnum;

import java.util.HashMap;

public class CheckBoxVHAdapter extends RecyclerView.Adapter<CheckBoxViewHolder> {
    private HashMap<QualificationsEnum, CheckBox> checkBoxes = new HashMap<>();
    private OnCheckBoxClickListener listener;
    private final QualificationsEnum[] qualifications = QualificationsEnum.values();

    public CheckBoxVHAdapter(Context context, OnCheckBoxClickListener listener) {
        this.listener = listener;

        for (QualificationsEnum qualification : QualificationsEnum.values()) {
            checkBoxes.put(qualification, new CheckBox(context));
        }
    }

    @Override
    @NonNull
    public CheckBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.qualification_checkbox, parent, false);

        return new CheckBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CheckBoxViewHolder holder, int position) {
        // Bind a checkbox and use it instead of the same checkbox in `qualifications`
        CheckBox newCB = holder.bind(qualifications[position].toString(), listener);
        newCB.setChecked(checkBoxes.get(qualifications[position]).isChecked());
        checkBoxes.put(qualifications[position], newCB);
    }

    @Override
    public int getItemCount() {
        return checkBoxes.size();
    }

    public void setQualificationBoolean(QualificationsEnum qualification, Boolean bool) {
        checkBoxes.get(qualification).setChecked(bool);
    }

    public HashMap<QualificationsEnum, Boolean> getQualifications() {
        HashMap<QualificationsEnum, Boolean> result = new HashMap<>();
        checkBoxes.forEach((qualification, selected) -> {
            result.put(qualification, selected.isChecked());
        });
        return result;
    }

    public interface OnCheckBoxClickListener {
        void onItemClick(CheckBox checkBox);
    }
}
