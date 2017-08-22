package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.pojo.RegisterPackageWithPhone;
import com.brewmapp.presentation.view.contract.RegisterView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface RegisterPresenter extends LivePresenter<RegisterView> {
    void onRegisterPackageReady(RegisterPackageWithPhone registerPackage);
}
