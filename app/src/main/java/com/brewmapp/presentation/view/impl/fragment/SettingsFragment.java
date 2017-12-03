package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.brewmapp.BuildConfig;
import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.MainPresenter;
import com.brewmapp.presentation.presenter.contract.SettingsPresenter;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.contract.ProfileEditView;
import com.brewmapp.presentation.view.contract.SettingsView;
import com.brewmapp.presentation.view.impl.activity.MultiFragmentActivity;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.brewmapp.presentation.view.impl.activity.StartActivity;

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
    @BindView(R.id.fragment_setting_simple_exit) View simple_exit;
    @BindView(R.id.fragment_setting_all_devices_exit) View all_devices_exit;

    @Inject SettingsPresenter presenter;
    @Inject    MainPresenter presenterMain;

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
        about.setOnClickListener(view1 -> startActivity(new Intent(MultiFragmentActivityView.MODE_ABOUT,null,getActivity(), MultiFragmentActivity.class)));
        help.setOnClickListener(view1 -> startActivity(new Intent(MultiFragmentActivityView.MODE_WEBVIEW, Uri.parse(BuildConfig.SERVER_ROOT_URL),getActivity(), MultiFragmentActivity.class)));
        write_to_us.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        terms_of_use.setOnClickListener(view1 -> startActivity(new Intent(MultiFragmentActivityView.MODE_WEBVIEW, Uri.parse("https://brewmapp.com/company/terms"),getActivity(), MultiFragmentActivity.class)));
        profile.setOnClickListener(v -> startActivity(new Intent(String.valueOf(ProfileEditView.SHOW_FRAGMENT_EDIT),null,getActivity(), ProfileEditActivity.class)));
        change_password.setOnClickListener(v -> presenter.setPassword(getActivity()));
        change_phone.setOnClickListener(v -> startActivity(new Intent(String.valueOf(ProfileEditView.SHOW_FRAGMENT_EDIT),null,getActivity(), ProfileEditActivity.class)));
        simple_exit.setOnClickListener(v -> {presenterMain.onLogout();startActivity(new Intent(v.getContext(),StartActivity.class));getActivity().finish();});
        all_devices_exit.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        auth_facebook.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        //delete_account.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        delete_account.setOnClickListener(v -> presenter.tmpLocation());

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

    @Override
    protected void prepareView(View view) {
        super.prepareView(view);
        if(interractor()!=null)   view.post(() -> interractor().processShowDrawer(true,true));
    }

    @Override
    public void onBarAction(int id) {
        super.onBarAction(id);
    }


}
