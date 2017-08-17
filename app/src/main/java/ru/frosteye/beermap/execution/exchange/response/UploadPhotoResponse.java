package ru.frosteye.beermap.execution.exchange.response;

import java.io.File;

/**
 * Created by oleg on 16.08.17.
 */

public class UploadPhotoResponse {
    private int id;
    private transient File file;

    public UploadPhotoResponse() {
    }

    public int getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
