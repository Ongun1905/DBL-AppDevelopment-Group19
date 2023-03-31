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
import java.util.List;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxViewHolder> {
    private List<CheckBox> checkBoxes = new ArrayList<>();
    private OnCheckBoxClickListener listener;
    private final ArrayList<String> qualifications = Arrays.stream(QualificationsEnum.values())
            .map(QualificationsEnum::toString)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

    public CheckBoxAdapter(Context context, OnCheckBoxClickListener listener) {
        this.listener = listener;
        for (int i = 0; i < QualificationsEnum.values().length; i++) {
            checkBoxes.add(new CheckBox(context));
        }
//        notifyDataSetChanged();
    }

    public CheckBoxAdapter(Context context, ArrayList<Boolean> checksList, OnCheckBoxClickListener listener) {
        this.listener = listener;
        for (int i = 0; i < QualificationsEnum.values().length; i++) {
            CheckBox newCB = new CheckBox(context);
            newCB.setChecked(checksList.get(i));
            checkBoxes.add(newCB);
        }
        notifyDataSetChanged();
    }

    public CheckBoxAdapter(Context context, String encodedQualifications, OnCheckBoxClickListener listener) {
        this.listener = listener;
        for (int i = 0; i < QualificationsEnum.values().length; i++) {
            CheckBox newCB = new CheckBox(context);
            newCB.setChecked(encodedQualifications.charAt(i) == 'T');
            checkBoxes.add(newCB);
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
        checkBoxes.set(position, holder.bind(qualifications.get(position), listener));
    }

    @Override
    public int getItemCount() {
        return checkBoxes.size();
    }

    public void setItems(List<CheckBox> checkBoxes) {
        this.checkBoxes = checkBoxes;
        notifyDataSetChanged();
    }

    public void printBools() {
        for (CheckBox cb : checkBoxes) {
            System.out.print(cb.isChecked() + ", ");
        }
        System.out.println();
    }

    public interface OnCheckBoxClickListener {
        void onItemClick(CheckBox checkBox);
    }
}
