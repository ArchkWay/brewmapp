package com.brewmapp.presentation.view.impl.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.MultiFragmentActivityPresenter;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class MultiFragmentActivity extends BaseActivity implements MultiFragmentActivityView {
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.multiactivity_root)    ViewGroup root;

    @Inject    MultiFragmentActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_view);
    }

    @Override
    protected void initView() {
        enableBackButton();
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.parseIntent(getIntent());
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
    public void commonError(String... strings) {
        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();
    }

    @Override
    public void setContent(String modeContent) {
        switch (modeContent){
            case MODE_ABOUT:
                getLayoutInflater().inflate(R.layout.about_view,root);
                break;
        }

    }


    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }
}
