package com.appdev.terra.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.MainActivity;
import com.appdev.terra.MyViewHolder;
import com.appdev.terra.R;
import com.appdev.terra.enums.QualificationsEnum;
import com.appdev.terra.services.helpers.PostCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
        checkBoxes.put(qualifications[position], holder.bind(qualifications[position].toString(), listener));
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

    public void printBools() {
        System.out.println(checkBoxes);
    }

    public interface OnCheckBoxClickListener {
        void onItemClick(CheckBox checkBox);
    }
}
