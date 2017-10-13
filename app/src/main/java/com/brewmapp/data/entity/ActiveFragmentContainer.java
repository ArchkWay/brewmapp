package com.brewmapp.data.entity;

/**
 * Created by xpusher on 10/13/2017.
 */

public class ActiveFragmentContainer {

    public int nActiveFragment;

    public ActiveFragmentContainer(int nActiveFragment) {
        this.nActiveFragment=nActiveFragment;
    }


    public int getnActiveFragment() {
        return nActiveFragment;
    }

    public void setnActiveFragment(int nActiveFragment) {
        this.nActiveFragment = nActiveFragment;
    }

}
