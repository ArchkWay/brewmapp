package ru.frosteye.beermap.data.pojo;

import java.io.File;

import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.ovsa.execution.network.request.MultipartRequestParams;
import ru.frosteye.ovsa.execution.network.request.Requestable;

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
