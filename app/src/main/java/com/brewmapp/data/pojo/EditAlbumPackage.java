package com.brewmapp.data.pojo;

/**
 * Created by oleg on 16.08.17.
 */

public class EditAlbumPackage {
    private String description;
    private String name;
    private int albumId;

    public int getAlbumId() {
        return albumId;
    }

    public EditAlbumPackage setAlbumId(int albumId) {
        this.albumId = albumId;
        return this;
    }

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
