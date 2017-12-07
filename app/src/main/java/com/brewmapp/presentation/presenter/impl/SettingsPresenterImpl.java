package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.pojo.RegisterPackage;
import com.brewmapp.data.pojo.RegisterPackageWithPhone;
import com.brewmapp.presentation.presenter.contract.SettingsPresenter;
import com.brewmapp.presentation.view.contract.SettingsView;
import com.brewmapp.presentation.view.impl.activity.EnterPasswordActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogCheckOldPassword;

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
    public void setPassword(final FragmentActivity activity) {
        new DialogCheckOldPassword(activity.getSupportFragmentManager(), new DialogCheckOldPassword.OnValidConfirmPassword() {
            @Override
            public void onValidPass() {
                Intent intent = new Intent(activity, EnterPasswordActivity.class);
                RegisterPackageWithPhone registerPackage=new RegisterPackageWithPhone(userRepo.load());
                intent.putExtra(RegisterPackage.KEY, registerPackage);
                activity.startActivity(intent);

            }

            @Override
            public void onErrorPass() {
                view.showMessage(activity.getString(R.string.error),0);
            }
        });

    }

}
