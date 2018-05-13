package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.StartView;
import com.facebook.CallbackManager;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface StartPresenter extends LivePresenter<StartView> {
    CallbackManager requestCallbackManager();
    void onFacebookLogin();
}
