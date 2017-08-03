package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.task.ConfirmCodeTask;
import ru.frosteye.beermap.execution.task.RequestCodeTask;
import ru.frosteye.beermap.presentation.view.contract.ConfirmPhoneView;

import ru.frosteye.ovsa.execution.network.request.RequestParams;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.ConfirmPhonePresenter;

public class ConfirmPhonePresenterImpl extends BasePresenter<ConfirmPhoneView> implements ConfirmPhonePresenter {

    private RequestCodeTask requestCodeTask;
    private ConfirmCodeTask confirmCodeTask;

    @Inject
    public ConfirmPhonePresenterImpl(RequestCodeTask requestCodeTask,
                                     ConfirmCodeTask confirmCodeTask) {
        this.requestCodeTask = requestCodeTask;
        this.confirmCodeTask = confirmCodeTask;
    }

    @Override
    public void onAttach(ConfirmPhoneView confirmPhoneView) {
        super.onAttach(confirmPhoneView);
    }

    @Override
    public void onDestroy() {
        requestCodeTask.cancel();
        confirmCodeTask.cancel();
    }

    @Override
    public void onPhoneReady(String phone) {
        enableControls(false);
        RequestParams params = new RequestParams();
        params.addParam(Keys.PHONE, phone);
        requestCodeTask.execute(params, new SimpleSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
                view.die();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                enableControls(true);
                view.startCounter();
            }
        });
    }

    @Override
    public void onCodeReady(String code) {

    }
}
