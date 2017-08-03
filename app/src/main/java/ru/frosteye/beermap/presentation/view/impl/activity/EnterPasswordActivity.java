package ru.frosteye.beermap.presentation.view.impl.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.frosteye.beermap.R;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class EnterPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void attachPresenter() {

    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return null;
    }

    @Override
    protected void inject(PresenterComponent component) {

    }
}
