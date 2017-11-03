package com.brewmapp.presentation.view.impl.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.FilterByCategoryPresenter;
import com.brewmapp.presentation.view.contract.FilterByCategoryView;
import com.brewmapp.presentation.view.impl.widget.FinderView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by nixus on 01.11.2017.
 */

public class FilterByCategory extends BaseActivity implements FilterByCategoryView, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.categoryList) RecyclerView list;
    @BindView(R.id.activity_search_search) FinderView finder;

    private FlexibleModelAdapter<IFlexible> adapter;

    @Inject FilterByCategoryPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filter);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    protected void initView() {
        enableBackButton();
        
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);
        adapter = new FlexibleModelAdapter<>(new ArrayList<>());
        list.setAdapter(adapter);
        initFilterByCategory(getIntent().getIntExtra(Keys.FILTER_CATEGORY, 0));
    }

    private void initFilterByCategory(int filterId) {
        switch (filterId) {
            case 1:
                presenter.loadRestoTypes();
                break;
            case 3:
                presenter.loadKitchenTypes();
                break;
            case 4:
                presenter.loadPriceRangeTypes();
                break;
                default:
                    break;
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
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void appendItems(List<IFlexible> list) {
        adapter.clear();
        adapter.addItems(adapter.getItemCount(), list);
    }

    @Override
    public boolean onItemClick(int position) {
        Log.i("itemClick", String.valueOf(position));
        return false;
    }
}
