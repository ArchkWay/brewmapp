package ru.frosteye.beermap.presentation.view.contract;

import android.app.Activity;

import com.facebook.login.widget.LoginButton;

import ru.frosteye.ovsa.presentation.view.BasicView;

public interface LoginView extends BasicView {
    void proceed();
    Activity getActivity();
    LoginButton getLoginButton();
}
