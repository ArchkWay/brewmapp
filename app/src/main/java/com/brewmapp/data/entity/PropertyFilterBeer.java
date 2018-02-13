package com.brewmapp.data.entity;

/**
 * Created by Kras on 13.02.2018.
 */

public class PropertyFilterBeer {
    private String id;
    private String name;
    private boolean selected;
    public PropertyFilterBeer(String name,String id) {
        this.name=name;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
