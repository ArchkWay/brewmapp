package com.brewmapp.presentation.view.impl.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerDetail;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.presentation.presenter.contract.BeerDetailPresenter;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.view.contract.BeerDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class BeerDetailActivity extends  BaseActivity implements BeerDetailView {
    @BindView(R.id.common_toolbar)    Toolbar toolbar;

    @Inject    BeerDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);
    }

    @Override
    protected void initView() {
        enableBackButton();
        enableControls(false,0);
        String beer_id=((Interest)getIntent().getSerializableExtra(getString(R.string.key_serializable_extra))).getInterest_info().getId();
        presenter.requestBeerDetail(beer_id);

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
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void setModel(BeerDetail beerDetail) {
        setTitle(beerDetail.getBeer().getTitle());

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

}
