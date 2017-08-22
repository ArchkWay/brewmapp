package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.entity.User;
import com.brewmapp.data.pojo.RegisterPackageWithPhoneAndPassword;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.CreatePasswordTask;
import com.brewmapp.presentation.presenter.contract.EnterPasswordPresenter;
import com.brewmapp.presentation.view.contract.EnterPasswordView;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by oleg on 07.08.17.
 */

public class EnterPasswordPresenterImpl extends BasePresenter<EnterPasswordView> implements EnterPasswordPresenter {

    private CreatePasswordTask createPasswordTask;

    @Inject
    public EnterPasswordPresenterImpl(CreatePasswordTask createPasswordTask) {
        this.createPasswordTask = createPasswordTask;
    }

    @Override
    public void onDestroy() {
        createPasswordTask.cancel();
    }

    @Override
    public void onRegisterPackageReady(RegisterPackageWithPhoneAndPassword pack) {
        enableControls(false);
        createPasswordTask.execute(pack, new SimpleSubscriber<ListResponse<User>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(ListResponse<User> userResponse) {
                enableControls(true);
                view.proceed();
            }
        });
    }
}
