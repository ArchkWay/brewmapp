package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
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
import com.brewmapp.app.environment.FilterActions;
import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Feature;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.PriceRange;
import com.brewmapp.data.entity.PriceRangeTypes;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoType;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.entity.wrapper.FeatureInfo;
import com.brewmapp.data.entity.wrapper.KitchenInfo;
import com.brewmapp.data.entity.wrapper.PriceRangeInfo;
import com.brewmapp.data.entity.wrapper.RestoInfo;
import com.brewmapp.data.entity.wrapper.RestoTypeInfo;
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
import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

/**
 * Created by nixus on 01.11.2017.
 */

public class FilterByCategory extends BaseActivity implements FilterByCategoryView {

    @BindView(R.id.filter_toolbar)
    Toolbar toolbar;
    @BindView(R.id.filter_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.filter_toolbar_subtitle)
    TextView okButton;
    @BindView(R.id.filter_toolbar_cancel)
    TextView cancel;
    @BindView(R.id.categoryList)
    RecyclerView list;
    @BindView(R.id.activity_search_search)
    FinderView finder;

    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage fullSearchPackage;
    private String selectedItem;
    private String selectedItemId;

    @Inject
    FilterByCategoryPresenter presenter;
    private List<IFlexible> original;
    private int filterCategory;
    private String selectedFilter = null;

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
        Paper.init(this);
        fullSearchPackage = new FullSearchPackage();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        list.setLayoutManager(manager);
        adapter = new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        list.setAdapter(adapter);
        initFilterByCategory(getIntent().getIntExtra(Keys.FILTER_CATEGORY, 0));
        initFilter();
    }

    private void initFilterByCategory(int filterId) {
        this.filterCategory = filterId;
        switch (filterId) {
            case FilterActions.RESTO_NAME:
                okButton.setVisibility(View.GONE);
                fullSearchPackage.setType(Keys.TYPE_RESTO);
                toolbarTitle.setText(R.string.search_resto_name);
                break;
            case FilterActions.RESTO_TYPE:
                toolbarTitle.setText(R.string.search_resto_type);
                if (getStoredFilterList(FilterKeys.RESTO_TYPE) != null) {
                    appendItems(getStoredFilterList(FilterKeys.RESTO_TYPE));
                } else {
                    presenter.loadRestoTypes();
                }
                break;
            case FilterActions.BEER:
                fullSearchPackage.setType(Keys.TYPE_BEER);
                toolbarTitle.setText(R.string.search_resto_beer);
                break;
            case FilterActions.KITCHEN:
                toolbarTitle.setText(R.string.search_resto_kitchen);
                if (getStoredFilterList(FilterKeys.KITCHEN) != null) {
                    appendItems(getStoredFilterList(FilterKeys.KITCHEN));
                } else {
                    presenter.loadKitchenTypes();
                }
                break;
            case FilterActions.PRICE_RANGE:
                toolbarTitle.setText(R.string.search_resto_price);
                if (getStoredFilterList(FilterKeys.PRICE_RANGE) != null) {
                    appendItems(getStoredFilterList(FilterKeys.PRICE_RANGE));
                } else {
                    presenter.loadPriceRangeTypes();
                }
                break;
            case FilterActions.COUNTRY:
                toolbarTitle.setText(R.string.select_country);
                okButton.setText(R.string.next);
                break;
            case FilterActions.METRO:
                toolbarTitle.setText(R.string.select_metro);
                break;
            case FilterActions.FEATURES:
                toolbarTitle.setText(R.string.search_resto_other);
                if (getStoredFilterList(FilterKeys.FEATURES) != null) {
                    appendItems(getStoredFilterList(FilterKeys.FEATURES));
                } else {
                    presenter.loadFeatureTypes();
                }
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
    }

    private void initFilter() {
        if (fullSearchPackage.getType() != null) {
            finder.setListener(string -> prepareQuery(string));
        } else {
            finder.setListener(string -> {
                adapter.setSearchText(string);
                adapter.filterItems(original);
            });
        }
    }

    private void prepareQuery(String stringSearch) {
        fullSearchPackage.setPage(0);
        fullSearchPackage.setStringSearch(stringSearch);
        if (stringSearch.length() > 3) {
            sendQuery();
        } else if (stringSearch.length() == 0) {
            adapter.clear();
        }
    }

    private void sendQuery() {
        if (fullSearchPackage.getStringSearch().length() == 0){
            fullSearchPackage.setPage(0);
            appendItems(new ArrayList<>());
        } else {
            presenter.sendQueryFullSearch(fullSearchPackage);
        }
    }

    private void processAction(int action, Object payload) {
        switch (action){
            case FilterActions.RESTO_NAME:
                Resto resto = (Resto) payload;
                selectedItemId = String.valueOf(resto.getId());
                selectedItem = resto.getName();
                goToFilterMap();
                break;
            case FilterActions.RESTO_TYPE:
                RestoType restoType = (RestoType) payload;
                if (!restoType.isSelected()) {
                    restoType.setSelected(true);
                } else {
                    restoType.setSelected(false);
                }
                break;
            case FilterActions.BEER:
                Beer beer = (Beer) payload;
                if (!beer.isSelected()) {
                    beer.setSelected(true);
                } else {
                    beer.setSelected(false);
                }
                break;
            case FilterActions.KITCHEN:
                Kitchen kitchen = (Kitchen) payload;
                if (!kitchen.isSelected()) {
                    kitchen.setSelected(true);
                } else {
                    kitchen.setSelected(false);
                }
                break;
            case FilterActions.PRICE_RANGE:
                PriceRange priceRange = (PriceRange) payload;
                if (!priceRange.isSelected()) {
                    priceRange.setSelected(true);
                } else {
                    priceRange.setSelected(false);
                }
                break;
            case FilterActions.COUNTRY:
                //TO DO
                break;
            case FilterActions.METRO:
                //TO DO
                break;
            case FilterActions.FEATURES:
                Feature feature = (Feature) payload;
                if (!feature.isSelected()) {
                    feature.setSelected(true);
                } else {
                    feature.setSelected(false);
                }
                break;
            default:break;
        }
        adapter.updateDataSet(original);
    }

    @OnClick(R.id.filter_toolbar_subtitle)
    public void okFilterClicked() {
        if (filterCategory == FilterActions.RESTO_TYPE) {
            selectedFilter = FilterKeys.RESTO_TYPE;
            saveStoredFilter(FilterKeys.RESTO_TYPE);
        } else if (filterCategory == FilterActions.KITCHEN) {
            selectedFilter = FilterKeys.KITCHEN;
            saveStoredFilter(FilterKeys.KITCHEN);
        } else if (filterCategory == FilterActions.BEER) {
            selectedFilter = FilterKeys.BEER;
            saveStoredFilter(FilterKeys.BEER);
        } else if (filterCategory == FilterActions.PRICE_RANGE) {
            selectedFilter = FilterKeys.PRICE_RANGE;
            saveStoredFilter(FilterKeys.PRICE_RANGE);
        } else if (filterCategory == FilterActions.COUNTRY) {
            selectedFilter = FilterKeys.COUNTRY;
            saveStoredFilter(FilterKeys.COUNTRY);
        } else if (filterCategory == FilterActions.METRO) {
            selectedFilter = FilterKeys.METRO;
            saveStoredFilter(FilterKeys.METRO);
        } else if (filterCategory == FilterActions.FEATURES) {
            selectedFilter = FilterKeys.FEATURES;
            saveStoredFilter(FilterKeys.FEATURES);
        }
        goToFilterMap();
    }

    private void goToFilterMap() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("filter", selectedFilter);
        returnIntent.putExtra("category", filterCategory);
        returnIntent.putExtra("selectedItem", selectedItem);
        returnIntent.putExtra("selectedItemId", selectedItemId);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @OnClick(R.id.filter_toolbar_cancel)
    public void cancelFilterClicked() {
        onBackPressed();
    }

    private List<IFlexible> getStoredFilterList(String filterKey) {
        return Paper.book().read(filterKey);
    }

    private void saveStoredFilter(String filterKey) {
        Paper.book().write(filterKey, original);
    }
}
