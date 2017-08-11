package ru.frosteye.beermap.presentation.presenter.contract;

import ru.frosteye.beermap.data.pojo.RegisterPackageWithPhoneAndPassword;
import ru.frosteye.beermap.presentation.view.contract.EnterPasswordView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by oleg on 07.08.17.
 */

public interface EnterPasswordPresenter extends LivePresenter<EnterPasswordView> {
    void onRegisterPackageReady(RegisterPackageWithPhoneAndPassword pack);
}
