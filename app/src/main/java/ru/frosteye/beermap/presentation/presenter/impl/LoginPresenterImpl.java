package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.beermap.execution.exchange.response.UserResponse;
import ru.frosteye.beermap.execution.exchange.response.base.MessageResponse;
import ru.frosteye.beermap.execution.task.LoginTask;
import ru.frosteye.beermap.presentation.view.contract.LoginView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.LoginPresenter;

public class LoginPresenterImpl extends BasePresenter<LoginView> implements LoginPresenter {

    private LoginTask loginTask;
    private UserRepo userRepo;

    @Inject
    public LoginPresenterImpl(LoginTask loginTask, UserRepo userRepo) {
        this.loginTask = loginTask;
        this.userRepo = userRepo;
    }

    @Override
    public void onDestroy() {
        loginTask.cancel();
    }

    @Override
    public void onLoginPassReady(String login, String pass) {
        enableControls(false);
        WrapperParams params = new WrapperParams(Wrappers.USER);
        params.addParam(Keys.LOGIN, login);
        params.addParam(Keys.PASSWORD, pass);
        loginTask.execute(params, new SimpleSubscriber<UserResponse>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(UserResponse responseBody) {
                view.proceed();
            }
        });
    }

    @Override
    public void onFacebookLogin() {
        enableControls(false);
    }
}
