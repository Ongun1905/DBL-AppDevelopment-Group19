package com.appdev.terra.users.shared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.R;
import com.appdev.terra.enums.QualificationsEnum;

import java.util.HashMap;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxViewHolder> {
    private HashMap<QualificationsEnum, CheckBox> checkBoxes = new HashMap<>();
    private OnCheckBoxClickListener listener;
    private final QualificationsEnum[] qualifications = QualificationsEnum.values();

    public CheckBoxAdapter(Context context, OnCheckBoxClickListener listener) {
        this.listener = listener;

        for (QualificationsEnum qualification : QualificationsEnum.values()) {
            checkBoxes.put(qualification, new CheckBox(context));
        }
    }

    public CheckBoxAdapter(Context context, HashMap<QualificationsEnum, Boolean> checksList, OnCheckBoxClickListener listener) {
        this.listener = listener;

        for (QualificationsEnum qualification : QualificationsEnum.values()) {
            CheckBox newCB = new CheckBox(context);
            newCB.setChecked(checksList.get(qualification));
            checkBoxes.put(qualification, newCB);
        }

        notifyDataSetChanged();
    }

    @Override
    public CheckBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qualification_checkbox, parent, false);
        return new CheckBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CheckBoxViewHolder holder, int position) {
        CheckBox newCB = holder.bind(qualifications[position].toString(), listener);
        newCB.setChecked(checkBoxes.get(qualifications[position]).isChecked());
        checkBoxes.put(qualifications[position], newCB);
    }

    @Override
    public int getItemCount() {
        return checkBoxes.size();
    }

    public void setCheckBoxValues(HashMap<QualificationsEnum, Boolean> checksList) {
        for (QualificationsEnum qualification : QualificationsEnum.values()) {
            checkBoxes.get(qualification).setChecked(checksList.get(qualification));
        }
        notifyDataSetChanged();
    }

    public void setQualificationBoolean(QualificationsEnum qualification, Boolean bool) {
        checkBoxes.get(qualification).setChecked(bool);
    }

    public HashMap<QualificationsEnum, Boolean> getQualifications() {
        HashMap<QualificationsEnum, Boolean> result = new HashMap<>();
        printBools();
        checkBoxes.forEach((qualification, selected) -> {
            result.put(qualification, selected.isChecked());
        });
        return result;
    }
    
    public void printBools() {
        System.out.println("[");
        checkBoxes.forEach((q, s) -> {
            System.out.println(q + ": " + s.isChecked());
        });
        System.out.println("]");
    }


    public interface OnCheckBoxClickListener {
        void onItemClick(CheckBox checkBox);
    }
}