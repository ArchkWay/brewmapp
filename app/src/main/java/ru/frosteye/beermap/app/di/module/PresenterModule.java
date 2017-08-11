package ru.frosteye.beermap.app.di.module;

import android.view.View;

import ru.frosteye.beermap.app.di.scope.PresenterScope;
import ru.frosteye.beermap.presentation.presenter.contract.ConfirmPhonePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.EnterPasswordPresenter;
import ru.frosteye.beermap.presentation.presenter.contract.FriendsPresenter;
import ru.frosteye.beermap.presentation.presenter.contract.InvitePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.LoginPresenter;
import ru.frosteye.beermap.presentation.presenter.contract.MainPresenter;
import ru.frosteye.beermap.presentation.presenter.contract.ProfilePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.RegisterPresenter;
import ru.frosteye.beermap.presentation.presenter.contract.StartPresenter;
import ru.frosteye.beermap.presentation.presenter.impl.ConfirmPhonePresenterImpl;
import ru.frosteye.beermap.presentation.presenter.impl.EnterPasswordPresenterImpl;
import ru.frosteye.beermap.presentation.presenter.impl.FriendsPresenterImpl;
import ru.frosteye.beermap.presentation.presenter.impl.InvitePresenterImpl;
import ru.frosteye.beermap.presentation.presenter.impl.LoginPresenterImpl;
import ru.frosteye.beermap.presentation.presenter.impl.MainPresenterImpl;
import ru.frosteye.beermap.presentation.presenter.impl.ProfilePresenterImpl;
import ru.frosteye.beermap.presentation.presenter.impl.RegisterPresenterImpl;
import ru.frosteye.beermap.presentation.presenter.impl.StartPresenterImpl;
import ru.frosteye.beermap.presentation.view.impl.activity.BaseActivity;
import ru.frosteye.beermap.presentation.view.impl.fragment.BaseFragment;

import dagger.Module;
import dagger.Provides;
import ru.frosteye.ovsa.di.module.BasePresenterModule;

@Module
public class PresenterModule extends BasePresenterModule<BaseActivity, BaseFragment> {
    public PresenterModule(View view) {
        super(view);
    }

    public PresenterModule(BaseActivity activity) {
        super(activity);
    }

    public PresenterModule(BaseFragment fragment) {
        super(fragment);
    }

    @Provides @PresenterScope
    StartPresenter provideStartPresenter(StartPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    LoginPresenter provideLoginPresenter(LoginPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    RegisterPresenter provideRegisterPresenter(RegisterPresenterImpl presenter) {
        return presenter;
    }
    
    @Provides @PresenterScope
    ConfirmPhonePresenter provideConfirmPhonePresenter(ConfirmPhonePresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    MainPresenter provideMainPresenter(MainPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    ProfilePresenter provideProfilePresenter(ProfilePresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    FriendsPresenter provideFriendsPresenter(FriendsPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    EnterPasswordPresenter provideEnterPasswordPresenter(EnterPasswordPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    InvitePresenter provideInvitePresenter(InvitePresenterImpl presenter) {
        return presenter;
    }
}
