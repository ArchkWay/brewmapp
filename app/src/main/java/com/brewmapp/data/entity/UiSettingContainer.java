package com.brewmapp.data.entity;

/**
 * Created by xpusher on 10/13/2017.
 */

public class UiSettingContainer {

    public int nActiveFragment= MenuField.PROFILE;

    public int nActiveTabEventFragment=0;


    public int getnActiveFragment() {
        return nActiveFragment;
    }

    public void setnActiveFragment(int nActiveFragment) {
        this.nActiveFragment = nActiveFragment;
    }

    public int getnActiveTabEventFragment() {
        return nActiveTabEventFragment;
    }

    public void setnActiveTabEventFragment(int nActiveTabEventFragment) {
        this.nActiveTabEventFragment = nActiveTabEventFragment;
    }

}
