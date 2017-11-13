package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.UiSettingContainer;
import com.brewmapp.data.entity.MenuField;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.MainPresenter;
import com.brewmapp.presentation.view.contract.MainView;
import com.brewmapp.presentation.view.impl.activity.MainActivity;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 02.08.2017.
 */

public class MainPresenterImpl extends BasePresenter<MainView> implements MainPresenter {

    private UserRepo userRepo;
    private Context context;
    private UiSettingRepo uiSettingRepo;

    @Inject
    public MainPresenterImpl(UiSettingRepo uiSettingRepo, UserRepo userRepo, Context context) {
        this.userRepo = userRepo;
        this.context = context;
        this.uiSettingRepo = uiSettingRepo;
    }

    @Override
    public void onAttach(MainView mainView) {
        super.onAttach(mainView);
        view.showUser(userRepo.load());
        view.showMenuItems(MenuField.createDefault(context));
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onLogout() {
        userRepo.save(null);
    }

    @Override
    public int getActiveFragment() {
        return uiSettingRepo.getnActiveFragment();
    }

    @Override
    public String parseMode(Intent intent) {
        if(MainActivity.MODE_ONLY_EVENT_FRAGMENT.equals(intent.getAction()))
            return MainActivity.MODE_ONLY_EVENT_FRAGMENT;
        else if(MainActivity.MODE_ONLY_MAP_FRAGMENT.equals(intent.getAction()))
            return MainActivity.MODE_ONLY_MAP_FRAGMENT;
        else
            return MainActivity.MODE_DEFAULT;

    }

    @Override
    public Bundle parseArguments(Intent intent) {
        Bundle bundle=new Bundle();
        switch (parseMode(intent)){
            case MainActivity.MODE_ONLY_EVENT_FRAGMENT:
                bundle.putString(Keys.RELATED_MODEL,intent.getStringExtra(Keys.RELATED_MODEL));
                bundle.putString(Keys.RELATED_ID,intent.getStringExtra(Keys.RELATED_ID));
                return bundle;
            case MainActivity.MODE_ONLY_MAP_FRAGMENT:
                bundle.putSerializable(Keys.LOCATION,intent.getSerializableExtra(Keys.LOCATION));
                return bundle;
            default:return null;
        }

    }

}
