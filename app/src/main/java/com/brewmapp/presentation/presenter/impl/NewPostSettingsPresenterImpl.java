package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.presentation.view.contract.NewPostSettingsView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.NewPostSettingsPresenter;

public class NewPostSettingsPresenterImpl extends BasePresenter<NewPostSettingsView> implements NewPostSettingsPresenter {

    @Inject
    public NewPostSettingsPresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }
}
