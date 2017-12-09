package com.brewmapp.presentation.view.impl.fragment;

import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.RestoEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.RestoEditFragmentView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 10.12.2017.
 */

public class RestoEditFragment extends BaseFragment  implements RestoEditFragmentView {

    @Inject RestoEditFragmentPresenter presenter;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_resto_edit;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView(View view) {

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
    public int getMenuToInflate() {
        return 0;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return null;
    }
}
