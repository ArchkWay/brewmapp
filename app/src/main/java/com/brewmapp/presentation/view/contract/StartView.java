package com.brewmapp.presentation.view.contract;

import android.app.Activity;
import android.content.Context;

import com.facebook.login.widget.LoginButton;

import ru.frosteye.ovsa.presentation.view.BasicView;

public interface StartView extends BasicView {
    void proceed();
    LoginButton getLoginButton();
    Activity getActivity();
}
