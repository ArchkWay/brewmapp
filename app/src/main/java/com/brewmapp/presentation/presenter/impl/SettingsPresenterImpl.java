package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.pojo.RegisterPackage;
import com.brewmapp.data.pojo.RegisterPackageWithPhone;
import com.brewmapp.presentation.presenter.contract.SettingsPresenter;
import com.brewmapp.presentation.view.contract.SettingsView;
import com.brewmapp.presentation.view.impl.activity.EnterPasswordActivity;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SettingsPresenterImpl extends BasePresenter<SettingsView> implements SettingsPresenter {

    private UserRepo userRepo;

    @Inject
    public SettingsPresenterImpl(UserRepo userRepo) {
        this.userRepo=userRepo;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setPassword(FragmentActivity activity) {

        Intent intent = new Intent(activity, EnterPasswordActivity.class);
        RegisterPackageWithPhone registerPackage=new RegisterPackageWithPhone(userRepo.load());
        intent.putExtra(RegisterPackage.KEY, registerPackage);
        activity.startActivity(intent);
    }

    @Override
    public void tmpLocation() {

    }
}
