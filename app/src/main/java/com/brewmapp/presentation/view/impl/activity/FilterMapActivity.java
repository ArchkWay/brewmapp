package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.FilterField;
import com.brewmapp.presentation.presenter.contract.FilterMapPresenter;
import com.brewmapp.presentation.view.contract.FilterMapView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by nlbochas on 28/10/2017.
 */

public class FilterMapActivity extends BaseActivity implements FilterMapView, View.OnClickListener, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.accept_filter) Button search;
    @BindView(R.id.filter_list) RecyclerView list;

    @Inject FilterMapPresenter presenter;
    private FlexibleAdapter<FilterField> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_map);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    protected void inject(PresenterComponent component) {
    }

    @Override
    protected void initView() {
        enableBackButton();
        search.setOnClickListener(this);
        showFilters(FilterField.createDefault(this));

    }

    @Override
    protected void attachPresenter() {
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        finish();
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void showFilters(List<FilterField> fieldList) {
        adapter = new FlexibleAdapter<>(fieldList, this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    @Override
    public boolean onItemClick(int position) {
        Log.i("sdfdsfds", "1!!!!!");
        return false;
    }
}
