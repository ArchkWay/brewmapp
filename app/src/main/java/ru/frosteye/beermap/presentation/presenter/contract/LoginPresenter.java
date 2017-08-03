package ru.frosteye.beermap.presentation.presenter.contract;

import com.facebook.CallbackManager;

import ru.frosteye.beermap.presentation.view.contract.LoginView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface LoginPresenter extends LivePresenter<LoginView> {
    void onLoginPassReady(String login, String pass);
    void onFacebookLogin();
    CallbackManager requestCallbackManager();
}
