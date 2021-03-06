package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;

import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by xpusher on 11/20/2017.
 */

public interface MultiFragmentActivityPresenter extends LivePresenter<MultiFragmentActivityView> {

    void parseIntent(Intent intent);
}
