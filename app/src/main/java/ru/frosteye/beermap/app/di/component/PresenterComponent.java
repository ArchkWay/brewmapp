package ru.frosteye.beermap.app.di.component;

import ru.frosteye.beermap.app.di.module.PresenterModule;
import ru.frosteye.beermap.app.di.scope.PresenterScope;
import ru.frosteye.beermap.presentation.view.impl.activity.BaseActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.ConfirmCodeActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.EnterPasswordActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.EnterPhoneActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.InviteActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.LoginActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.MainActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.RegisterActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.StartActivity;
import ru.frosteye.beermap.presentation.view.impl.fragment.BaseFragment;

import dagger.Subcomponent;
import ru.frosteye.beermap.presentation.view.impl.fragment.FriendsFragment;
import ru.frosteye.beermap.presentation.view.impl.fragment.ProfileFragment;

@PresenterScope
@Subcomponent(modules = PresenterModule.class)
public interface PresenterComponent {
    void inject(BaseFragment baseFragment);
    void inject(ProfileFragment fragment);
    void inject(FriendsFragment fragment);

    void inject(BaseActivity activity);
    void inject(StartActivity activity);
    void inject(EnterPasswordActivity activity);
    void inject(LoginActivity activity);
    void inject(RegisterActivity activity);
    void inject(EnterPhoneActivity activity);
    void inject(MainActivity activity);
    void inject(ConfirmCodeActivity activity);
    void inject(InviteActivity activity);
}
