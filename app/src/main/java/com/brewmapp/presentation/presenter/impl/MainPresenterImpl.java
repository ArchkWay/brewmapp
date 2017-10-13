package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.UiSettingContainer;
import com.brewmapp.data.entity.MenuField;
import com.brewmapp.presentation.presenter.contract.MainPresenter;
import com.brewmapp.presentation.view.contract.MainView;
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
}
