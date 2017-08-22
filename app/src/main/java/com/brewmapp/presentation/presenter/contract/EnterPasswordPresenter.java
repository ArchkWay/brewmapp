package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.pojo.RegisterPackageWithPhoneAndPassword;
import com.brewmapp.presentation.view.contract.EnterPasswordView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by oleg on 07.08.17.
 */

public interface EnterPasswordPresenter extends LivePresenter<EnterPasswordView> {
    void onRegisterPackageReady(RegisterPackageWithPhoneAndPassword pack);
}
