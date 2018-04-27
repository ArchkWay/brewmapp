package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;

import com.brewmapp.presentation.view.contract.ProfileFragmentFull_view;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface ProfileFragmentFull_presenter extends LivePresenter<ProfileFragmentFull_view> {
    boolean parseIntent(Intent intent);

    void loadProfile();

}
