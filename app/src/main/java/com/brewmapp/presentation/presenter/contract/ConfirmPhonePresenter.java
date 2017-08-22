package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.ConfirmPhoneView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface ConfirmPhonePresenter extends LivePresenter<ConfirmPhoneView> {
    void onPhoneReady(String phone);
    void onCodeReady(String code);
}
