package com.m1kes.expressscript.adapters.recyclerview.medicalaids.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m1kes.expressscript.R;
import com.m1kes.expressscript.adapters.recyclerview.medicalaids.SelectableMedicalAid;
import com.m1kes.expressscript.adapters.recyclerview.medicalaids.objects.SelectableMedicalAidViewHolder;
import com.m1kes.expressscript.objects.MedicalAid;

import java.util.ArrayList;
import java.util.List;

public class SelectableMedicalAidAdapter extends RecyclerView.Adapter implements SelectableMedicalAidViewHolder.OnItemSelectedListener {

    private final List<SelectableMedicalAid> mValues;
    private boolean isMultiSelectionEnabled = false;
    private SelectableMedicalAidViewHolder.OnItemSelectedListener listener;
    private Context context;


    public SelectableMedicalAidAdapter(SelectableMedicalAidViewHolder.OnItemSelectedListener listener,
                                       List<MedicalAid> items, boolean isMultiSelectionEnabled,Context context) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        mValues = new ArrayList<>();
        for (MedicalAid item : items) {
            mValues.add(new SelectableMedicalAid(item, false));
        }
        this.context = context;
    }

    @Override
    public SelectableMedicalAidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selectable_medical_aid_row, parent, false);

        return new SelectableMedicalAidViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        SelectableMedicalAidViewHolder holder = (SelectableMedicalAidViewHolder) viewHolder;
        SelectableMedicalAid item = mValues.get(position);
        String name = item.getName();
        holder.textView.setText(name);

        if(item.getName().equalsIgnoreCase("Cimas")){
            holder.aidLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.cimass));
        }else if(item.getName().equalsIgnoreCase("Hmmas")){
            holder.aidLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.hmmas));
        }

        if (isMultiSelectionEnabled) {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        } else {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        }

        holder.mItem = item;
        holder.setChecked(holder.mItem.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<MedicalAid> getSelectedItems() {

        List<MedicalAid> selectedItems = new ArrayList<>();
        for (SelectableMedicalAid item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return SelectableMedicalAidViewHolder.MULTI_SELECTION;
        }
        else{
            return SelectableMedicalAidViewHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(SelectableMedicalAid item) {
        if (!isMultiSelectionEnabled) {

            for (SelectableMedicalAid selectableItem : mValues) {
                if (!selectableItem.equals(item)
                        && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item)
                        && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);
    }
}
