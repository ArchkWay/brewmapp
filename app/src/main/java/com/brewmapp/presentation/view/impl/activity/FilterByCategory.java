package com.brewmapp.presentation.view.impl.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.PriceRange;
import com.brewmapp.data.entity.PriceRangeTypes;
import com.brewmapp.data.entity.wrapper.PriceRangeInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.FilterByCategoryPresenter;
import com.brewmapp.presentation.view.contract.FilterByCategoryView;
import com.brewmapp.presentation.view.impl.widget.FinderView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

/**
 * Created by nixus on 01.11.2017.
 */

public class FilterByCategory extends BaseActivity implements FilterByCategoryView, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.filter_toolbar) Toolbar toolbar;
    @BindView(R.id.filter_toolbar_title) TextView toolbarTitle;
    @BindView(R.id.filter_toolbar_subtitle) TextView okButton;
    @BindView(R.id.filter_toolbar_cancel) TextView cancel;
    @BindView(R.id.categoryList) RecyclerView list;
    @BindView(R.id.activity_search_search) FinderView finder;

    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage fullSearchPackage;

    @Inject FilterByCategoryPresenter presenter;
    private List<IFlexible> original;
    private PriceRangeTypes priceRangeTypes;


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
        fullSearchPackage = new FullSearchPackage();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        list.setLayoutManager(manager);
        list.setNestedScrollingEnabled(false);
        adapter = new FlexibleModelAdapter<>(new ArrayList<>(), this);
        list.setAdapter(adapter);
        initFilterByCategory(getIntent().getIntExtra(Keys.FILTER_CATEGORY, 0));
    }

    private void initFilterByCategory(int filterId) {
        switch (filterId) {
            case 0:
                toolbarTitle.setText(R.string.search_resto_name);
                okButton.setVisibility(View.GONE);
                fullSearchPackage.setType(Keys.TYPE_RESTO);
                break;
            case 1:
                toolbarTitle.setText(R.string.search_resto_type);
                presenter.loadRestoTypes();
                break;
            case 2:
                toolbarTitle.setText(R.string.search_resto_beer);
                break;
            case 3:
                toolbarTitle.setText(R.string.search_resto_kitchen);
                presenter.loadKitchenTypes();
                break;
            case 4:
                toolbarTitle.setText(R.string.search_resto_price);
                presenter.loadPriceRangeTypes();
                break;
            case 5:
                toolbarTitle.setText(R.string.select_country);
                okButton.setText(R.string.next);
                break;
            case 6:
                toolbarTitle.setText(R.string.select_metro);
                break;
            case 7:
                toolbarTitle.setText(R.string.search_resto_other);
                presenter.loadFeatureTypes();
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
        this.original = list;
        adapter.clear();
        adapter.updateDataSet(list);
        initFilter();
    }

    private void initFilter() {
        finder.setListener(string -> {
            adapter.setSearchText(string);
            adapter.filterItems(original);
        });
    }

    @Override
    public boolean onItemClick(int position) {
        Log.i("dsf", "price");
        Log.i("priceRange", priceRangeTypes.getModels().get(position).getModel().getName());
        return false;
    }

    @OnClick(R.id.filter_toolbar_subtitle)
    public void okFilterClicked() {
    }

    @OnClick(R.id.filter_toolbar_cancel)
    public void cancelFilterClicked() {
        onBackPressed();
    }
}
