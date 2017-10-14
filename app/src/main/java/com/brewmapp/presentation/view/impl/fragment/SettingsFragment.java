package com.brewmapp.presentation.view.impl.fragment;

import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.SettingsPresenter;
import com.brewmapp.presentation.view.contract.SettingsView;

import javax.inject.Inject;

import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SettingsFragment extends BaseFragment implements SettingsView {

    @Inject SettingsPresenter presenter;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_settings;
    }


    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void attachPresenter() {

    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
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
        return ResourceHelper.getString(R.string.settings);
    }
}
