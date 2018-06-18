package com.m1kes.expressscript.adapters.menu;

import android.os.Bundle;

import com.m1kes.expressscript.objects.MenuId;
import com.m1kes.expressscript.objects.MenuItem;
import com.m1kes.expressscript.utils.extra.AbstractListMenuFragment;

import java.util.ArrayList;



public class DefaultListMenuFragment extends AbstractListMenuFragment {


    private ArrayList<MenuItem> menuItems;

    @Override
    protected ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public DefaultListMenuFragment createMenus() {
        this.menuItems = new ArrayList<>();
        return this;
    }

    public DefaultListMenuFragment createMenus(String title) {
        createMenus();
        return createTitle(title);
    }

    public DefaultListMenuFragment createTitle(String title) {

        if (title != null && !title.trim().isEmpty()) {

            Bundle arguments = getArguments();
            if (arguments == null) {
                arguments = new Bundle();
            }
            arguments.putString(AbstractListMenuFragment.LIST_TITLE, title);
            setArguments(arguments);
        }
        return this;
    }

    public DefaultListMenuFragment showCabsHeading(boolean showHeading) {

        Bundle arguments = getArguments();
        if (arguments == null) {
            arguments = new Bundle();
        }
        arguments.putBoolean(AbstractListMenuFragment.SHOW_CABS_HEADING, showHeading);
        setArguments(arguments);
        return this;
    }

    public DefaultListMenuFragment addMenu(MenuId id, String title, String description, int iconDrawableId) {
        menuItems.add(new MenuItem(id, title, description, iconDrawableId));
        return this;
    }
}
