package com.m1kes.expressscript.adapters.recyclerview.quoteitems;

import com.m1kes.expressscript.objects.MedicalAid;
import com.m1kes.expressscript.objects.QuoteItem;

public class SelectableQuoteItem extends QuoteItem{

    private boolean isSelected = false;

    public SelectableQuoteItem(QuoteItem item, boolean isSelected) {
        super(item.getDesc(), item.getPrice());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
