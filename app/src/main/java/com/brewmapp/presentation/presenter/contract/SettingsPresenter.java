package com.brewmapp.presentation.presenter.contract;

import android.support.v4.app.FragmentActivity;

import com.brewmapp.presentation.view.contract.SettingsView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public interface SettingsPresenter extends LivePresenter<SettingsView> {
    void setPassword(FragmentActivity activity);

    void tmpLocation();
}
