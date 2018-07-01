package com.m1kes.expressscript.adapters.recyclerview.quoteitems.objects;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.m1kes.expressscript.R;
import com.m1kes.expressscript.adapters.recyclerview.quoteitems.SelectableQuoteItem;

public class SelectableQuoteItemViewHolder extends RecyclerView.ViewHolder {

    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    public CheckedTextView textView;
    public TextView txtItemDesc,txtItemPrice;
    public SelectableQuoteItem mItem;
    public OnItemSelectedListener itemSelectedListener;


    public SelectableQuoteItemViewHolder(View view, OnItemSelectedListener listener) {
        super(view);
        itemSelectedListener = listener;
        textView = view.findViewById(R.id.quoteSelection);
        txtItemDesc = view.findViewById(R.id.txtItemDesc);
        txtItemPrice = view.findViewById(R.id.txtItemPrice);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItem.isSelected() && getItemViewType() == MULTI_SELECTION) {
                    setChecked(false);
                } else {
                    setChecked(true);
                }
                itemSelectedListener.onItemSelected(mItem);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItem.isSelected() && getItemViewType() == MULTI_SELECTION) {
                    setChecked(false);
                } else {
                    setChecked(true);
                }
                itemSelectedListener.onItemSelected(mItem);
            }
        });
    }

    public void setChecked(boolean value) {
        if (value) {
            textView.setBackgroundColor(Color.LTGRAY);
        } else {
            textView.setBackground(null);
        }
        mItem.setSelected(value);
        textView.setChecked(value);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(SelectableQuoteItem item);
    }

}

