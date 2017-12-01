package com.brewmapp.presentation.support.navigation;

import android.content.Context;
import android.content.Intent;

import com.brewmapp.presentation.view.impl.fragment.BaseFragment;

/**
 * Created by oleg on 23.08.17.
 */

public interface FragmentInterractor {
    void processTitleDropDown(BaseFragment baseFragment, int selected);
    void processSpinnerTitleSubtitle(String subtitle);
    void processStartActivityWithRefresh(Intent intent,int requestCode);
    void processSetActionBar(int position);

    void processShow(boolean show, boolean smooth);
}
