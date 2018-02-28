package com.brewmapp.presentation.view.impl.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.MultiFragmentActivityPresenter;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.impl.fragment.BeerEditFragment;
import com.brewmapp.presentation.view.impl.fragment.Chat.ChatFragment;
import com.brewmapp.presentation.view.impl.fragment.RestoEditFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.AboutFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.OwnerFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.WebViewFragment;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.listener.SelectListener;

public class MultiFragmentActivity extends BaseActivity implements
        MultiFragmentActivityView,
        AboutFragment.OnFragmentInteractionListener,
        WebViewFragment.OnFragmentInteractionListener ,
        BeerEditFragment.OnFragmentInteractionListener,
        RestoEditFragment.OnFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener,
        OwnerFragment.OnFragmentInteractionListener

{

    //region BindView
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.multiactivity_root)    ViewGroup root;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle) TextView toolbarSubTitle;
    //endregion

    //region Inject
    @Inject    MultiFragmentActivityPresenter presenter;
    //endregion

    //region Impl MultiFragmentActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_view);
    }

    @Override
    protected void initView() {
        enableBackButton();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);
        toolbarDropdown.setGravity(Gravity.CENTER_HORIZONTAL);
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
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        toolbarTitle.setText(getTitle());
    }

    //endregion

    //region Impl MultiFragmentActivityView
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
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.multiactivity_сontainer, fragment)
                .commit();

    }
    //endregion

    //region Impl other interfaces
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setTitleActionBar(int title) {
        setTitle(title);
        toolbarTitle.setText(getTitle());
    }

    @Override
    public void selectPhoto(SelectListener selectListener) {
        showSelect(this, R.array.avatar_options, selectListener);
    }
    //endregion
}
