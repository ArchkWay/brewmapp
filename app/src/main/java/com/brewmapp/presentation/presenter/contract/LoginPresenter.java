package com.brewmapp.presentation.presenter.contract;

import com.facebook.CallbackManager;

import com.brewmapp.presentation.view.contract.LoginView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface LoginPresenter extends LivePresenter<LoginView> {
    void onLoginPassReady(String login, String pass);


}
