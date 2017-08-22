package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.pojo.RegisterPackageWithPhone;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.UserResponse;
import com.brewmapp.execution.task.RegisterTask;
import com.brewmapp.presentation.view.contract.RegisterView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.RegisterPresenter;

public class RegisterPresenterImpl extends BasePresenter<RegisterView> implements RegisterPresenter {

    private RegisterTask registerTask;


    @Inject
    public RegisterPresenterImpl(RegisterTask registerTask) {
        this.registerTask = registerTask;
    }

    @Override
    public void onDestroy() {
        registerTask.cancel();
    }

    @Override
    public void onRegisterPackageReady(RegisterPackageWithPhone registerPackage) {
        enableControls(false);
        WrapperParams params = new WrapperParams(Wrappers.USER);
        params.addParam(Keys.LOGIN, registerPackage.getPhone());
        params.addParam(Keys.PHONE, registerPackage.getPhone());
        params.addParam(Keys.GENDER, registerPackage.getGender());
        params.addParam(Keys.FIRSTNAME, registerPackage.getFirstName());
        params.addParam(Keys.LASTNAME, registerPackage.getLastName());
        registerTask.execute(params, new SimpleSubscriber<UserResponse>() {
            @Override
            public void onError(Throwable e) {
                //FIXME request error codes
                if(e.getMessage().contains("Пользователь с таким")) {
                    view.proceed();
                } else {
                    enableControls(true);
                    showMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(UserResponse responseBody) {
                view.proceed();
            }
        });
    }
}
