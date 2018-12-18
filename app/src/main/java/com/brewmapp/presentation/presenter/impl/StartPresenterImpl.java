package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import com.brewmapp.R;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.LocalizedStrings;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.pojo.ProfileInfoPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.UserResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.services.ChatService;
import com.brewmapp.execution.task.FbLoginTask;
import com.brewmapp.execution.task.LoadProfileAndPostsTask;
import com.brewmapp.execution.task.ReviewsRelModelsTask;
import com.brewmapp.execution.tool.NetworkChangeReceiver;
import com.brewmapp.presentation.view.contract.StartView;

import ru.frosteye.ovsa.execution.network.request.RequestParams;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.StartPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class StartPresenterImpl extends BasePresenter<StartView> implements StartPresenter {

    private UserRepo userRepo;
    public UiSettingRepo uiSettingRepo;
    private Context context;
    private LoadProfileAndPostsTask loadProfilePostsTask;
    private CallbackManager callbackManager;
    private FbLoginTask fbLoginTask;

    @Inject
    public StartPresenterImpl(UserRepo userRepo,UiSettingRepo uiSettingRepo,Context context,LoadProfileAndPostsTask loadProfilePostsTask,FbLoginTask fbLoginTask) {
        this.userRepo = userRepo;
        this.uiSettingRepo = uiSettingRepo;
        this.context = context;
        this.loadProfilePostsTask = loadProfilePostsTask;
        this.fbLoginTask = fbLoginTask;
    }

    @Override
    public void onAttach(StartView startView) {
        super.onAttach(startView);
        this.callbackManager = CallbackManager.Factory.create();

        uiSettingRepo.setIsOnLine(new NetworkChangeReceiver().isOnline(context));

        startView.getLoginButton().setReadPermissions(Arrays.asList(view.getActivity().getResources()
                .getStringArray(R.array.config_facebook_permissions)));
        startView.getLoginButton().registerCallback(callbackManager, facebookCallback);


        if(userRepo.load() != null) {
            view.enableControls(false,0);
            if(uiSettingRepo.isOnLine()) {
                context.startService(new Intent(ChatService.ACTION_SET_ONLINE, null, context, ChatService.class));
                loadProfilePostsTask.execute(null, new SimpleSubscriber<ProfileInfoPackage>() {
                    @Override
                    public void onNext(ProfileInfoPackage pack) {
                        userRepo.save(pack.getUserProfile().getUser());
                        view.proceed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.proceed();
                    }
                });

            }else {
                context.startService(new Intent(ChatService.ACTION_SET_OFFLINE, null, context, ChatService.class));
                view.proceed();
            }
        }else {
            view.enableControls(true,0);
        }
        ReviewsRelModelsTask relatedModels = new ReviewsRelModelsTask(BeerMap.getAppComponent().mainThread(), BeerMap.getAppComponent().executor(), BeerMap.getAppComponent().api());
        relatedModels.execute(null, new SimpleSubscriber<SingleResponse<LocalizedStrings>>());
    }

    @Override
    public void onDestroy() {

    }


    private void onFacebookLoginInner(LoginResult result) {
        RequestParams params = new RequestParams();
        params.addParam(Keys.ID, result.getAccessToken().getUserId());
        params.addParam(Keys.TOKEN, result.getAccessToken().getToken());
        fbLoginTask.execute(params, new StartPresenterImpl.LoginSubscriber());
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

}
