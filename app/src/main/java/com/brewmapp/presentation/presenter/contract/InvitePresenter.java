package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.InviteView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface InvitePresenter extends LivePresenter<InviteView> {
    void onFacebookShare();
}
