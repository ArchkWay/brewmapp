package com.brewmapp.presentation.presenter.impl;

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
    private FbLoginTask fbLoginTask;
    private UserRepo userRepo;
    private CallbackManager callbackManager;

    @Inject
    public LoginPresenterImpl(LoginTask loginTask, FbLoginTask fbLoginTask, UserRepo userRepo) {
        this.loginTask = loginTask;
        this.fbLoginTask = fbLoginTask;
        this.userRepo = userRepo;
    }

    @Override
    public void onAttach(LoginView loginView) {
        super.onAttach(loginView);
        this.callbackManager = CallbackManager.Factory.create();
        loginView.getLoginButton().setReadPermissions(Arrays.asList(view.getActivity().getResources()
                .getStringArray(R.array.config_facebook_permissions)));
        loginView.getLoginButton().registerCallback(callbackManager, facebookCallback);
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

    private void onFacebookLoginInner(LoginResult result) {
        RequestParams params = new RequestParams();
        params.addParam(Keys.ID, result.getAccessToken().getUserId());
        params.addParam(Keys.TOKEN, result.getAccessToken().getToken());
        fbLoginTask.execute(params, new LoginSubscriber());
    }

    @Override
    public void onFacebookLogin() {
        enableControls(false);
        LoginManager.getInstance().logOut();
        view.getLoginButton().performClick();
    }

    @Override
    public CallbackManager requestCallbackManager() {
        return callbackManager;
    }

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            onFacebookLoginInner(loginResult);
        }

        @Override
        public void onCancel() {
            enableControls(true);
        }

        @Override
        public void onError(FacebookException error) {
            enableControls(true);
            showMessage(error.getMessage());
        }
    };

    private class LoginSubscriber extends SimpleSubscriber<UserResponse> {
        @Override
        public void onError(Throwable e) {
            enableControls(true);
            showMessage(e.getMessage());
        }

        @Override
        public void onNext(UserResponse userResponse) {
            view.proceed();
        }
    }
}
