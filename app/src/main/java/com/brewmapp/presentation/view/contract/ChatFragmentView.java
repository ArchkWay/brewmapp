package com.brewmapp.presentation.view.contract;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 12/13/2017.
 */

public interface ChatFragmentView extends BasicView {
    void commonError(String... message);

    FragmentActivity getActivity();
}
