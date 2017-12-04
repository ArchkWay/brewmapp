package com.brewmapp.data.pojo;

/**
 * Created by nixus on 02.12.2017.
 */

public class FilterPackage {
    private int mode;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void dropAll() {
        mode = 0;
    }
}
