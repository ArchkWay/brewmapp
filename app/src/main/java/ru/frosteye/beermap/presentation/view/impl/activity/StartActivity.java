package ru.frosteye.beermap.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.BindView;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.presentation.presenter.contract.StartPresenter;
import ru.frosteye.beermap.presentation.view.contract.StartView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;
import ru.frosteye.beermap.R;

public class StartActivity extends BaseActivity implements StartView {


    @BindView(R.id.activity_start_enter) View enter;
    @BindView(R.id.activity_start_register) View register;

    @Inject StartPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView() {
        enter.setOnClickListener(v -> startActivity(LoginActivity.class));
        register.setOnClickListener(v -> startActivity(RegisterActivity.class));
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

    @Override
    public void proceed() {
        startActivity(MainActivity.class);
        finish();
    }
}
