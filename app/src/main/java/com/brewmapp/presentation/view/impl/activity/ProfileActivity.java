package com.brewmapp.presentation.view.impl.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.ProfileActivity_presenter;
import com.brewmapp.presentation.view.contract.ProfileActivity_view;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;
import com.brewmapp.presentation.view.impl.fragment.ProfileFragmentEdit;
import com.brewmapp.presentation.view.impl.fragment.ProfileFragmentShot;
import com.brewmapp.presentation.view.impl.fragment.ProfileFragmentFull;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class ProfileActivity extends BaseActivity implements ProfileActivity_view,
        ProfileFragmentEdit.OnFragmentInteractionListener,
        ProfileFragmentShot.OnFragmentInteractionListener,
        ProfileFragmentFull.OnFragmentInteractionListener
{
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.profile_info_activity_container)FrameLayout frameLayout;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;

    @Inject    ProfileActivity_presenter presenter;

    private BaseFragment baseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);


        enableBackButton();
        setTitle(R.string.title_activity_edit_profile);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        showFragment(presenter.parseIntent(getIntent()));
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
    public void commonError(String... strings) {

        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();
    }

    @Override
    public void showFragment(int fragment) {
        switch (fragment){
            case SHOW_FRAGMENT_EDIT:
                baseFragment=new ProfileFragmentEdit();
                break;
            case SHOW_PROFILE_FRAGMENT_VIEW_SHOT:
                baseFragment=new ProfileFragmentShot();
                break;
            case SHOW_PROFILE_FRAGMENT_VIEW_FULL:
                baseFragment=new ProfileFragmentFull();
                break;
            default: {commonError();return;}
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
            case SHOW_FRAGMENT_EDIT:
                showFragment(key);
                break;
            case INVALIDATE_MENU:
                invalidateOptionsMenu();
                break;
            case ERROR:
                finish();
                break;
            case USER_SAVED:
                setResult(RESULT_OK);
                finish();
                break;
            case SELECT_PHOTO:
                showSelect(this, R.array.avatar_options, (text, position) -> presenter.handlePhoto(baseFragment,position));
                break;
        }
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        toolbarTitle.setText(getTitle());
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        toolbarTitle.setText(getTitle());

    }



}
