package com.brewmapp.presentation.view.impl.fragment;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.SettingsPresenter;
import com.brewmapp.presentation.view.contract.SettingsView;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SettingsFragment extends BaseFragment implements SettingsView {

    @BindView(R.id.fragment_setting_about) View about;
    @BindView(R.id.fragment_setting_help) View help;
    @BindView(R.id.fragment_setting_write_to_us) View write_to_us;
    @BindView(R.id.fragment_setting_terms_of_use) View terms_of_use;
    @BindView(R.id.fragment_setting_profile) View profile;
    @BindView(R.id.fragment_setting_change_password) View change_password;
    @BindView(R.id.fragment_setting_change_phone) View change_phone;
    @BindView(R.id.fragment_setting_auth_facebook) View auth_facebook;
    @BindView(R.id.fragment_setting_delete_account) View delete_account;

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
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu!=null) menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
}
