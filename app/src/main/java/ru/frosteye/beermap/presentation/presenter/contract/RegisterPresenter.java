package ru.frosteye.beermap.presentation.presenter.contract;

import ru.frosteye.beermap.data.pojo.RegisterPackageWithPhone;
import ru.frosteye.beermap.presentation.view.contract.RegisterView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface RegisterPresenter extends LivePresenter<RegisterView> {
    void onRegisterPackageReady(RegisterPackageWithPhone registerPackage);
}
