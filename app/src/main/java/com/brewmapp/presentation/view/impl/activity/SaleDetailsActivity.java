package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.presentation.presenter.contract.SaleDetailsPresenter;
import com.brewmapp.presentation.view.contract.SaleDetailsView;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;

import com.brewmapp.R;

public class SaleDetailsActivity extends BaseActivity implements SaleDetailsView {
    @BindView(R.id.activity_sale_details_avatar)    ImageView avatar;
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_sale_details_title)    TextView titile;
    @BindView(R.id.activity_sale_details_resto_name)    TextView resto_name;

    @Inject SaleDetailsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_details);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView() {
        enableBackButton();

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
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
    public void showSaleDetails(Sale active) {
        setTitle(active.getName());
        titile.setText(active.getName());
        resto_name.setText(active.getParent().getName());
    }
}
