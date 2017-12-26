package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.MenuField;
import com.brewmapp.data.pojo.ProfileInfoPackage;
import com.brewmapp.execution.services.ChatService;
import com.brewmapp.execution.task.LoadProfileAndPostsTask;
import com.brewmapp.execution.tool.NetworkChangeReceiver;
import com.brewmapp.presentation.view.contract.StartView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.StartPresenter;

public class StartPresenterImpl extends BasePresenter<StartView> implements StartPresenter {

    private UserRepo userRepo;
    public UiSettingRepo uiSettingRepo;
    private Context context;
    private LoadProfileAndPostsTask loadProfilePostsTask;
    @Inject
    public StartPresenterImpl(UserRepo userRepo,UiSettingRepo uiSettingRepo,Context context,LoadProfileAndPostsTask loadProfilePostsTask) {
        this.userRepo = userRepo;
        this.uiSettingRepo = uiSettingRepo;
        this.context = context;
        this.loadProfilePostsTask = loadProfilePostsTask;
    }

    @Override
    public void onAttach(StartView startView) {
        super.onAttach(startView);

        uiSettingRepo.setIsOnLine(new NetworkChangeReceiver().isOnline(context));

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
    }

    @Override
    public void onDestroy() {

    }
}
