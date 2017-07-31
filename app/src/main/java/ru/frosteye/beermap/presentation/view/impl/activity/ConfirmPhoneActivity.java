package ru.frosteye.beermap.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.presentation.presenter.contract.ConfirmPhonePresenter;
import ru.frosteye.beermap.presentation.view.contract.ConfirmPhoneView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;
import ru.frosteye.beermap.R;

public class ConfirmPhoneActivity extends BaseActivity implements ConfirmPhoneView {

    @Inject ConfirmPhonePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_phone);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }
}
