package com.brewmapp.presentation.support.navigation;

import com.brewmapp.presentation.view.impl.fragment.BaseFragment;

/**
 * Created by oleg on 23.08.17.
 */

public interface FragmentInterractor {
    void processTitleDropDown(BaseFragment baseFragment, int selected);
    void processSpinnerTitleSubtitle(String subtitle);
}
