package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.beermap.execution.exchange.response.base.MessageResponse;
import ru.frosteye.beermap.execution.task.LoginTask;
import ru.frosteye.beermap.presentation.view.contract.LoginView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.LoginPresenter;

public class LoginPresenterImpl extends BasePresenter<LoginView> implements LoginPresenter {

    private LoginTask loginTask;

    @Inject
    public LoginPresenterImpl(LoginTask loginTask) {
        this.loginTask = loginTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onLoginPassReady(String login, String pass) {
        enableControls(false);
        WrapperParams params = new WrapperParams(Wrappers.USER);
        params.addParam(Keys.LOGIN, login);
        params.addParam(Keys.PASSWORD, pass);
        loginTask.execute(params, new SimpleSubscriber<MessageResponse>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(MessageResponse responseBody) {
                super.onNext(responseBody);
            }
        });
    }

    @Override
    public void onFacebookLogin() {
        enableControls(false);
    }
}
