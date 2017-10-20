package com.brewmapp.presentation.view.impl.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.AddFavoriteBeerPresenter;
import com.brewmapp.presentation.view.contract.AddFavoriteBeerView;
import com.brewmapp.presentation.view.impl.widget.FinderView;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class AddFavoriteBeerActivity extends BaseActivity implements AddFavoriteBeerView {
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_search_search)    FinderView finder;
    @Inject    AddFavoriteBeerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite_beer);
    }

    @Override
    protected void initView() {
        finder.setListener(string -> presenter.sendQuery(string));
    }

    @Override
    protected void attachPresenter() {
            presenter.onAttach(this);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return null;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void showResultQuery(String s) {
        Log.i("QQQ",s);
    }
}
