package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;

import com.brewmapp.presentation.view.contract.BreweryDetailsActivityView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by xpusher on 04.02.2018.
 */

public interface BreweryDetailsActivityPresenter extends LivePresenter<BreweryDetailsActivityView> {
    void parseIntent(Intent intent);
}
