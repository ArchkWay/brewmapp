package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.data.entity.User;
import ru.frosteye.beermap.data.pojo.RegisterPackageWithPhoneAndPassword;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.beermap.execution.exchange.response.UserResponse;
import ru.frosteye.beermap.execution.exchange.response.base.ListResponse;
import ru.frosteye.beermap.execution.task.CreatePasswordTask;
import ru.frosteye.beermap.presentation.presenter.contract.EnterPasswordPresenter;
import ru.frosteye.beermap.presentation.view.contract.EnterPasswordView;
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
