package com.m1kes.expressscript.objects;

import java.io.Serializable;

public class MenuItem implements Serializable {

    private MenuId id;
    private String title;
    private String description;
    private int iconDrawableId;
    private boolean header;


    public MenuItem(String headingTitle) {
        header = true;
        this.title = headingTitle;
    }

    public MenuItem(MenuId id, String title, String description, int iconDrawableId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.iconDrawableId = iconDrawableId;
    }

    public MenuItem() {
    }

    public MenuId getId() {
        return id;
    }

    public void setId(MenuId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconDrawableId() {
        return iconDrawableId;
    }

    public void setIconDrawableId(int iconDrawableId) {
        this.iconDrawableId = iconDrawableId;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }
}
