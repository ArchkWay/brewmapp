package com.brewmapp.execution.exchange.response;

import java.io.File;

/**
 * Created by oleg on 16.08.17.
 */

public class UploadPhotoResponse {
    private int id;
    private transient File file;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UploadPhotoResponse() {
    }

    public UploadPhotoResponse(File file) {
        this.file = file;
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
