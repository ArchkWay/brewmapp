package com.brewmapp.presentation.view.impl.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.presenter.contract.ProfileInfoPresenter;
import com.brewmapp.presentation.view.contract.ProfileInfoView;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;
import com.brewmapp.presentation.view.impl.fragment.ProfileEditFragment;
import com.brewmapp.presentation.view.impl.fragment.ProfileInfoFragment;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class ProfileInfoActivity extends BaseActivity implements ProfileInfoView,ProfileInfoFragment.OnFragmentInteractionListener,ProfileEditFragment.OnFragmentInteractionListener {

    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.profile_info_activity_container)FrameLayout frameLayout;

    @Inject    ProfileInfoPresenter presenter;

    public final static int FRAGMENT_INFO=0;
    public final static int FRAGMENT_EDIT=1;
    public static final int FRAGMENT_INVALIDATE_MENU = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
    }

    @Override
    protected void initView() {
        enableBackButton();
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

    @Override
    public void refreshUserProfile(User load) {
        setTitle(load.getFormattedName());
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
    public void showFragment(int fragment) {
        BaseFragment baseFragment;
        switch (fragment){
            case ProfileInfoActivity.FRAGMENT_INFO:
                baseFragment=new ProfileInfoFragment();
                break;
            case ProfileInfoActivity.FRAGMENT_EDIT:
                baseFragment=new ProfileEditFragment();
                break;
                default: {
                    commonError();
                    return;
                }
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.profile_info_activity_container, baseFragment)
                .commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        int key=Integer.valueOf(uri.getPath());
        switch (key){
            case FRAGMENT_EDIT:
            case FRAGMENT_INFO:
                showFragment(key);
                break;
            case FRAGMENT_INVALIDATE_MENU:
                invalidateOptionsMenu();
                break;
        }

    }
}
