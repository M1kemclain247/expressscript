package com.m1kes.expressscript.adapters.recyclerview.medicalaids;

import com.m1kes.expressscript.objects.MedicalAid;

public class SelectableMedicalAid extends MedicalAid {


    private boolean isSelected = false;

    public SelectableMedicalAid(MedicalAid item, boolean isSelected) {
        super(item.getId(), item.getName());
        this.isSelected = isSelected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
