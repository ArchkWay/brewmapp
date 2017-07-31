package ru.frosteye.beermap.app.di.component;

import ru.frosteye.beermap.app.di.module.PresenterModule;
import ru.frosteye.beermap.app.di.scope.PresenterScope;
import ru.frosteye.beermap.presentation.view.impl.activity.BaseActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.ConfirmPhoneActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.LoginActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.RegisterActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.StartActivity;
import ru.frosteye.beermap.presentation.view.impl.fragment.BaseFragment;

import dagger.Subcomponent;

@PresenterScope
@Subcomponent(modules = PresenterModule.class)
public interface PresenterComponent {
    void inject(BaseFragment baseFragment);

    void inject(BaseActivity baseActivity);
    void inject(StartActivity baseActivity);
    void inject(LoginActivity activity);
    void inject(RegisterActivity activity);
    void inject(ConfirmPhoneActivity activity);
}
