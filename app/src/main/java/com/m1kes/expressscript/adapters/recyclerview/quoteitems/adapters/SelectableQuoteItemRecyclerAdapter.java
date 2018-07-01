package com.m1kes.expressscript.adapters.recyclerview.quoteitems.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m1kes.expressscript.R;
import com.m1kes.expressscript.adapters.recyclerview.quoteitems.SelectableQuoteItem;
import com.m1kes.expressscript.adapters.recyclerview.quoteitems.objects.SelectableQuoteItemViewHolder;
import com.m1kes.expressscript.objects.QuoteItem;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SelectableQuoteItemRecyclerAdapter extends RecyclerView.Adapter implements SelectableQuoteItemViewHolder.OnItemSelectedListener {

    private final List<SelectableQuoteItem> mValues;
    private boolean isMultiSelectionEnabled = false;
    private SelectableQuoteItemViewHolder.OnItemSelectedListener listener;
    private Context context;


    public SelectableQuoteItemRecyclerAdapter(SelectableQuoteItemViewHolder.OnItemSelectedListener listener,
                                              List<QuoteItem> items, boolean isMultiSelectionEnabled, Context context) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        mValues = new ArrayList<>();
        for (QuoteItem item : items) {
            mValues.add(new SelectableQuoteItem(item, false));
        }
        this.context = context;
    }

    @Override
    public SelectableQuoteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_quote_item, parent, false);

        return new SelectableQuoteItemViewHolder(itemView, this);
    }

    private DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        SelectableQuoteItemViewHolder holder = (SelectableQuoteItemViewHolder) viewHolder;
        SelectableQuoteItem item = mValues.get(position);

        String desc = item.getDescription();
        double price = item.getTotal();

        holder.txtItemDesc.setText(desc);
        holder.txtItemPrice.setText("$" + df.format(price));

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

    public List<QuoteItem> getSelectedItems() {

        List<QuoteItem> selectedItems = new ArrayList<>();
        for (SelectableQuoteItem item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return SelectableQuoteItemViewHolder.MULTI_SELECTION;
        }
        else{
            return SelectableQuoteItemViewHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(SelectableQuoteItem item) {
        if (!isMultiSelectionEnabled) {

            for (SelectableQuoteItem selectableItem : mValues) {
                if (!selectableItem.equals(item)
                        && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item)
                        && item.isSelected()) {

                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);
    }
}
