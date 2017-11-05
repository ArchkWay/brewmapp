package com.brewmapp.data.entity;

import com.brewmapp.presentation.view.impl.fragment.EventsFragment;

/**
 * Created by xpusher on 10/13/2017.
 */

public class UiSettingContainer {

    public int nActiveFragment= MenuField.PROFILE;

    public int nActiveTabEventFragment= EventsFragment.TAB_EVENT;


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
