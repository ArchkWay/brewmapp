package com.brewmapp.data.pojo;

/**
 * Created by oleg on 16.08.17.
 */

public class NewAlbumPackage {
    private String description;
    private String name;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean validate() {
        return name != null && !name.isEmpty();
    }
}
