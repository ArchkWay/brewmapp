package com.brewmapp.presentation.view.impl.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.AddFavoriteBeerPresenter;
import com.brewmapp.presentation.view.contract.AddFavoriteBeerView;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class AddFavoriteBeerActivity extends BaseActivity implements AddFavoriteBeerView {
    @BindView(R.id.common_toolbar)    Toolbar toolbar;

    @Inject
    AddFavoriteBeerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite_beer);
    }

    @Override
    protected void initView() {
        enableBackButton();

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

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }
}
