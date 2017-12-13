package com.brewmapp.presentation.view.impl.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.MultiFragmentActivityPresenter;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.impl.fragment.BeerEditFragment;
import com.brewmapp.presentation.view.impl.fragment.SimpleFragment.ChatFragment.ChatFragment;
import com.brewmapp.presentation.view.impl.fragment.RestoEditFragment;
import com.brewmapp.presentation.view.impl.fragment.SimpleFragment.AboutFragment;
import com.brewmapp.presentation.view.impl.fragment.SimpleFragment.WebViewFragment;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class MultiFragmentActivity extends BaseActivity implements MultiFragmentActivityView,
        AboutFragment.OnFragmentInteractionListener,
        WebViewFragment.OnFragmentInteractionListener ,
        BeerEditFragment.OnFragmentInteractionListener,
        RestoEditFragment.OnFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener

{
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
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.multiactivity_сontainer, fragment)
                .commit();

    }


    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setTitleActionBar(int title) {
        setTitle(title);
    }
}
