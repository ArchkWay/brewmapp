package com.brewmapp.data.entity;

/**
 * Created by nlbochas on 18/10/2017.
 */

public class FilteredTitle {

    private String title;
    private boolean selected;

    public FilteredTitle(String title, boolean selected) {
        this.title = title;
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
