package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.presentation.presenter.contract.SettingsPresenter;
import com.brewmapp.presentation.view.contract.SettingsView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SettingsPresenterImpl extends BasePresenter<SettingsView> implements SettingsPresenter {

    @Inject
    public SettingsPresenterImpl() {
    }

    @Override
    public void onDestroy() {

    }
}
