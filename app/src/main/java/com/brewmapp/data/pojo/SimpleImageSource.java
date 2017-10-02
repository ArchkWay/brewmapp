package com.brewmapp.data.pojo;

import com.brewmapp.data.model.ICommonItem;

import java.io.File;

/**
 * Created by oleg on 30.09.17.
 */

public class SimpleImageSource implements ICommonItem.ImageSource {

    private String url;
    private File file;
    private int resource;
    private Source source;

    public SimpleImageSource(String url) {
        this.url = url;
        source = Source.URL;
    }

    public SimpleImageSource(File file) {
        this.file = file;
        source = Source.FILE;
    }

    public SimpleImageSource(int resource) {
        this.resource = resource;
        source = Source.RESOURCE;
    }

    @Override
    public Source source() {
        return source;
    }

    @Override
    public File file() {
        return file;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public int resource() {
        return resource;
    }
}
