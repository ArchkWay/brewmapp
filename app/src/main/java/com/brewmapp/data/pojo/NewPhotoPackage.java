package com.brewmapp.data.pojo;

import java.io.File;

import com.brewmapp.execution.exchange.request.base.Keys;

/**
 * Created by oleg on 16.08.17.
 */

public class NewPhotoPackage {
    private File file;
    private int albumId;
    private int relatedId;
    private String relatedModel = Keys.CAP_USER;

    public NewPhotoPackage(File file, int albumId) {
        this.file = file;
        this.albumId = albumId;
    }

    public NewPhotoPackage(File file) {
        this.file = file;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(int relatedId) {
        this.relatedId = relatedId;
    }

    public String getRelatedModel() {
        return relatedModel;
    }

    public void setRelatedModel(String relatedModel) {
        this.relatedModel = relatedModel;
    }

    public File getFile() {
        return file;
    }

    public int getAlbumId() {
        return albumId;
    }
}
