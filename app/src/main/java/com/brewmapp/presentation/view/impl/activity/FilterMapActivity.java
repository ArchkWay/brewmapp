package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.FilterField;
import com.brewmapp.data.entity.wrapper.FeatureInfo;
import com.brewmapp.data.entity.wrapper.FilterBeerInfo;
import com.brewmapp.data.entity.wrapper.KitchenInfo;
import com.brewmapp.data.entity.wrapper.PriceRangeInfo;
import com.brewmapp.data.entity.wrapper.RestoTypeInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.FilterMapPresenter;
import com.brewmapp.presentation.view.contract.FilterMapView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

/**
 * Created by nlbochas on 28/10/2017.
 */

public class FilterMapActivity extends BaseActivity implements FilterMapView, View.OnClickListener, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.accept_filter) Button search;
    @BindView(R.id.filter_list) RecyclerView list;

    @Inject FilterMapPresenter presenter;
    private FlexibleAdapter<FilterField> adapter;
    private List<FilterField> categoryList;

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
        component.inject(this);
    }

    @Override
    protected void initView() {
        enableBackButton();
        search.setOnClickListener(this);
        Paper.init(this);
        if (Paper.book().read("categoryList") == null) {
            Paper.book().write("categoryList", FilterField.createDefault(this));
        }
        showFilters(Paper.book().read("categoryList"));
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
    public void onClick(View v) {
        Intent intent = new Intent();
        finish();
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void showFilters(List<FilterField> fieldList) {
        this.categoryList = fieldList;
        adapter = new FlexibleAdapter<>(fieldList, this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        list.setNestedScrollingEnabled(true);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onItemClick(int position) {
        if (position == 5 || position == 6) {
            Toast.makeText(this, "В разработке...", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, FilterByCategory.class);
            intent.putExtra(Keys.FILTER_CATEGORY, position);
            startActivityForResult(intent, RequestCodes.REQUEST_FILTER_CATEGORY);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setSelectedFilter(data.getStringExtra("filter"),
                    data.getIntExtra("category", -999), data.getStringExtra("selectedItem"));
        }
    }

    private void setSelectedFilter(String filterCategory, int category, String selectedItem) {
        StringBuilder filter = new StringBuilder();
        List<IFlexible> tempList;
        if (filterCategory != null) {
            if (filterCategory.equalsIgnoreCase(FilterKeys.RESTO_TYPE)) {
                List<RestoTypeInfo> restoTypeInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.RESTO_TYPE);
                for (Object o : tempList) {
                    restoTypeInfos.add((RestoTypeInfo) o);
                }
                for (int i = 0; i < restoTypeInfos.size(); i++) {
                    if (restoTypeInfos.get(i).getModel().isSelected()) {
                        filter.append(restoTypeInfos.get(i).getModel().getName() + ", ");
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.KITCHEN)) {
                List<KitchenInfo> kitchenInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.KITCHEN);
                for (Object o : tempList) {
                    kitchenInfos.add((KitchenInfo) o);
                }
                for (KitchenInfo kitchenInfo : kitchenInfos) {
                    if (kitchenInfo.getModel().isSelected()) {
                        filter.append(kitchenInfo.getModel().getName() + ", ");
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER)) {
                    List<FilterBeerInfo> filterBeerInfos = new ArrayList<>();
                    tempList = Paper.book().read(FilterKeys.BEER);
                    for (Object o : tempList) {
                        filterBeerInfos.add((FilterBeerInfo) o);
                    }
                    for (FilterBeerInfo filterBeerInfo : filterBeerInfos) {
                        if (filterBeerInfo.getModel().isSelected()) {
                            filter.append(filterBeerInfo.getModel().getTitle_ru() + ", ");
                        }
                    }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.PRICE_RANGE)) {
                List<PriceRangeInfo> priceRangeInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.PRICE_RANGE);
                for (Object o : tempList) {
                    priceRangeInfos.add((PriceRangeInfo) o);
                }
                for (PriceRangeInfo priceRangeInfo : priceRangeInfos) {
                    if (priceRangeInfo.getModel().isSelected()) {
                        filter.append(priceRangeInfo.getModel().getName() + ", ");
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.FEATURES)) {
                List<FeatureInfo> featureInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.FEATURES);
                for (Object o : tempList) {
                    featureInfos.add((FeatureInfo) o);
                }
                for (FeatureInfo featureInfo : featureInfos) {
                    if (featureInfo.getModel().isSelected()) {
                        filter.append(featureInfo.getModel().getName() + ", ");
                    }
                }
            }
        }
        if (selectedItem != null) {
            adapter.getItem(category).setSelectedFilter(selectedItem);
        } else {
            adapter.getItem(category).setSelectedFilter(filter.deleteCharAt(filter.length() - 2).toString());
        }
        adapter.notifyDataSetChanged();
        Paper.book().write("categoryList", categoryList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Paper.book().destroy();
    }
}
