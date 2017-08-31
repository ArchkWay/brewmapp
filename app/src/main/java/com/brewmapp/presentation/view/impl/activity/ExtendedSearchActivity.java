package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.ExtendedSearchPresenter;
import com.brewmapp.presentation.view.contract.ExtendedSearchActivityView;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.FinderView;

public class ExtendedSearchActivity extends BaseActivity implements ExtendedSearchActivityView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_extendedSearch_list) RecyclerView list;
    @BindView(R.id.activity_extendedSearch_search) FinderView finder;
    @BindView(R.id.activity_extendedSearch_tabs) TabLayout tabs;


    @Inject ExtendedSearchPresenter presenter;

    private String[] tabContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_search);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView() {
        enableBackButton();
        tabContent = getResources().getStringArray(R.array.tabs_extended_search);
        for(String title: tabContent) {
            tabs.addTab(tabs.newTab().setCustomView(R.layout.view_tab).setText(title));
        }
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
}
