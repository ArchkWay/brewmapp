package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.entity.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import javax.inject.Inject;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.UserResponse;
import com.brewmapp.execution.task.FbLoginTask;
import com.brewmapp.execution.task.LoginTask;
import com.brewmapp.presentation.view.contract.LoginView;

import ru.frosteye.ovsa.execution.network.request.RequestParams;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.LoginPresenter;

public class LoginPresenterImpl extends BasePresenter<LoginView> implements LoginPresenter {

    private LoginTask loginTask;

    private UserRepo userRepo;


    @Inject
    public LoginPresenterImpl(LoginTask loginTask, UserRepo userRepo) {
        this.loginTask = loginTask;
        this.userRepo = userRepo;
    }

    @Override
    public void onAttach(LoginView loginView) {
        super.onAttach(loginView);

    }

    @Override
    public void onDestroy() {
        loginTask.cancel();
    }

    @Override
    public void onLoginPassReady(String login, String pass) {
        login = login.replace("+","");
        enableControls(false);
        WrapperParams params = new WrapperParams(Wrappers.USER);
        params.addParam(Keys.LOGIN, login);
        params.addParam(Keys.PASSWORD, pass);
        loginTask.execute(params, new LoginSubscriber());
    }

    private class LoginSubscriber extends SimpleSubscriber<UserResponse> {
        @Override
        public void onError(Throwable e) {
            enableControls(true);
            showMessage(e.getMessage());
        }

        @Override
        public void onNext(UserResponse userResponse) {
            userResponse.getUser().setCounts(new User.Counts());
            userRepo.save(userResponse.getUser());
            view.proceed();
        }
    }



}
