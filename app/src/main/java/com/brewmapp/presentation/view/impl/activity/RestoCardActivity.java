package com.brewmapp.presentation.view.impl.activity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.presentation.presenter.contract.RestoCardPresenter;
import com.brewmapp.presentation.view.contract.RestoCardView;
import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class RestoCardActivity extends BaseActivity implements RestoCardView {

    @BindView(R.id.common_toolbar)    Toolbar toolbar;

    @Inject
    RestoCardPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto_card);
    }

    @Override
    protected void initView() {
        enableBackButton();
        String resto_id=((Interest)getIntent().getSerializableExtra(getString(R.string.key_serializable_extra))).getInterest_info().getId();
        presenter.requestResto(resto_id);
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
    protected Toolbar findActionBar() {
        return toolbar;
    }

}
