package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.wrapper.BeerAftertasteInfo;
import com.brewmapp.data.entity.wrapper.BeerBrandInfo;
import com.brewmapp.data.entity.wrapper.BeerColorInfo;
import com.brewmapp.data.entity.wrapper.BeerDensityInfo;
import com.brewmapp.data.entity.wrapper.BeerIbuInfo;
import com.brewmapp.data.entity.wrapper.BeerPackInfo;
import com.brewmapp.data.entity.wrapper.BeerPowerInfo;
import com.brewmapp.data.entity.wrapper.BeerSmellInfo;
import com.brewmapp.data.entity.wrapper.BeerTasteInfo;
import com.brewmapp.data.entity.wrapper.BeerTypeInfo;
import com.brewmapp.data.entity.wrapper.BreweryInfoSelect;
import com.brewmapp.data.entity.wrapper.FeatureInfo;
import com.brewmapp.data.entity.wrapper.KitchenInfo;
import com.brewmapp.data.entity.wrapper.PriceRangeInfo;
import com.brewmapp.data.entity.wrapper.RestoTypeInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.FilterMapPresenter;
import com.brewmapp.presentation.view.contract.FilterMapView;
import com.brewmapp.presentation.view.impl.widget.TabsView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.impl.SimpleTabSelectListener;

/**
 * Created by nlbochas on 28/10/2017.
 */

public class FilterMapActivity extends BaseActivity implements FilterMapView, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.filter_category_toolbar)
    Toolbar toolbar;
    @BindView(R.id.title_toolbar)
    TextView titleToolbar;
    @BindView(R.id.accept_filter)
    Button search;
    @BindView(R.id.filter_list)
    RecyclerView list;
    @BindView(R.id.offer)
    CheckBox offer;
    @BindView(R.id.craft)
    CheckBox craft;
    @BindView(R.id.fragment_events_tabs)
    TabsView tabsView;

    private FlexibleAdapter<FilterRestoField> restoAdapter;
    private FlexibleAdapter<FilterBeerField> beerAdapter;
    private List<FilterRestoField> restoFilterList;
    private List<FilterBeerField> beerFilterList;
    private String[] tabContent = ResourceHelper.getResources().getStringArray(R.array.filter_search);
    private String[] titleContent = ResourceHelper.getResources().getStringArray(R.array.filter_title_search);

    @Inject
    FilterMapPresenter presenter;

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

        titleToolbar.setText(titleContent[0]);
        offer.setVisibility(tabsView.getTabs().getSelectedTabPosition() == 0 ? View.VISIBLE : View.GONE);
        craft.setVisibility(tabsView.getTabs().getSelectedTabPosition() != 0 ? View.VISIBLE : View.GONE);
        tabsView.setItems(Arrays.asList(tabContent), new SimpleTabSelectListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                presenter.storeTabActive(tab.getPosition());
                titleToolbar.setText(titleContent[tab.getPosition()]);
                presenter.selectTab(tab.getPosition());
            }
        });
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
    public void showRestoFilters(List<FilterRestoField> fieldList) {
        this.restoFilterList = fieldList;
        restoAdapter = new FlexibleAdapter<>(fieldList, this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        list.setNestedScrollingEnabled(true);
        list.setAdapter(restoAdapter);
    }

    @Override
    public void showBeerFilters(List<FilterBeerField> fieldList) {
        this.beerFilterList = fieldList;
        beerAdapter = new FlexibleAdapter<>(fieldList, this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        list.setNestedScrollingEnabled(true);
        list.setAdapter(beerAdapter);
    }

    @Override
    public void setTabActive(int i) {
        tabsView.getTabs().getTabAt(i);
    }

    @Override
    public boolean onItemClick(int position) {
        if ((tabsView.getTabs().getSelectedTabPosition() == 0) && position == 6){
            Toast.makeText(this, "В разработке...", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, SelectCategoryActivity.class);
            intent.putExtra(Keys.FILTER_CATEGORY, position);
            intent.putExtra(Keys.BEER_TYPES, tabsView.getTabs().getSelectedTabPosition());
            intent.putExtra(Keys.SEARCH, true);
            startActivityForResult(intent, RequestCodes.REQUEST_FILTER_CATEGORY);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (tabsView.getTabs().getSelectedTabPosition() == 0) {
                setRestoSelectedFilter(data.getStringExtra("filter"),
                        data.getIntExtra("category", -999), data.getStringExtra("selectedItem"),
                        data.getStringExtra("selectedItemId"));
            } else {
                setBeerSelectedFilter(data.getStringExtra("filter"),
                        data.getIntExtra("category", -999), data.getStringExtra("selectedItem"),
                        data.getStringExtra("selectedItemId"));
            }
        }
    }

    private void setBeerSelectedFilter(String filterCategory, int category, String selectedItem, String countryId) {
        StringBuilder filter = new StringBuilder();
        StringBuilder filterId = new StringBuilder();
        boolean notEmpty = false;
        List<IFlexible> tempList;
        if (filterCategory != null) {
            if (filterCategory.equalsIgnoreCase(FilterKeys.PRICE_BEER)) {
                List<PriceRangeInfo> priceRangeInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.PRICE_BEER);
                if (tempList != null) {
                    for (Object o : tempList) {
                        priceRangeInfos.add((PriceRangeInfo) o);
                    }
                    for (PriceRangeInfo priceRangeInfo : priceRangeInfos) {
                        if (priceRangeInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(priceRangeInfo.getModel().getName()).append(", ");
                            filterId.append(priceRangeInfo.getModel().getId()).append(",");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(priceRangeInfos.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER_PACK)) {
                List<BeerPackInfo> beerPackInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER_PACK);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerPackInfos.add((BeerPackInfo) o);
                    }
                    for (BeerPackInfo beerPackInfo : beerPackInfos) {
                        if (beerPackInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(beerPackInfo.getModel().getName()).append(", ");
                            filterId.append(beerPackInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerPackInfos.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER_TYPES)) {
                List<BeerTypeInfo> beerTypeInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER_TYPES);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerTypeInfos.add((BeerTypeInfo) o);
                    }
                    for (BeerTypeInfo beerTypeInfo : beerTypeInfos) {
                        if (beerTypeInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(beerTypeInfo.getModel().getName()).append(", ");
                            filterId.append(beerTypeInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerTypeInfos.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER_BRAND)) {
                List<BeerBrandInfo> beerBrandInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER_BRAND);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerBrandInfos.add((BeerBrandInfo) o);
                    }
                    for (BeerBrandInfo beerBrandInfo : beerBrandInfos) {
                        if (beerBrandInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(beerBrandInfo.getModel().getName()).append(", ");
                            filterId.append(beerBrandInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerBrandInfos.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER_COLOR)) {
                List<BeerColorInfo> beerColorInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER_COLOR);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerColorInfos.add((BeerColorInfo) o);
                    }
                    for (BeerColorInfo beerColorInfo : beerColorInfos) {
                        if (beerColorInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(beerColorInfo.getModel().getName()).append(", ");
                            filterId.append(beerColorInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerColorInfos.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER_POWER)) {
                List<BeerPowerInfo> beerPowers = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER_POWER);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerPowers.add((BeerPowerInfo) o);
                    }
                    for (BeerPowerInfo beerPowerInfo : beerPowers) {
                        if (beerPowerInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(beerPowerInfo.getModel().getName()).append(", ");
                            filterId.append(beerPowerInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerPowers.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER_SMELL)) {
                List<BeerSmellInfo> beerSmellInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER_SMELL);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerSmellInfos.add((BeerSmellInfo) o);
                    }
                    for (BeerSmellInfo beerSmellInfo : beerSmellInfos) {
                        if (beerSmellInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(beerSmellInfo.getModel().getName()).append(", ");
                            filterId.append(beerSmellInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerSmellInfos.get(0).getModel().getName());
                    }
                }
            }  else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER_TASTE)) {
                List<BeerTasteInfo> beerTasteInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER_TASTE);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerTasteInfos.add((BeerTasteInfo) o);
                    }
                    for (BeerTasteInfo beerTasteInfo : beerTasteInfos) {
                        if (beerTasteInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(beerTasteInfo.getModel().getName()).append(", ");
                            filterId.append(beerTasteInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerTasteInfos.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER_AFTER_TASTE)) {
                List<BeerAftertasteInfo> beerAftertasteInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER_AFTER_TASTE);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerAftertasteInfos.add((BeerAftertasteInfo) o);
                    }
                    for (BeerAftertasteInfo beerAftertasteInfo : beerAftertasteInfos) {
                        if (beerAftertasteInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(beerAftertasteInfo.getModel().getName()).append(", ");
                            filterId.append(beerAftertasteInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerAftertasteInfos.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER_DENSITY)) {
                List<BeerDensityInfo> beerDensityInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER_DENSITY);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerDensityInfos.add((BeerDensityInfo) o);
                    }
                    for (BeerDensityInfo beerDensityInfo : beerDensityInfos) {
                        if (beerDensityInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(beerDensityInfo.getModel().getName()).append(", ");
                            filterId.append(beerDensityInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerDensityInfos.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER_IBU)) {
                List<BeerIbuInfo> beerIbuInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER_IBU);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerIbuInfos.add((BeerIbuInfo) o);
                    }
                    for (BeerIbuInfo beerIbuInfo : beerIbuInfos) {
                        if (beerIbuInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(beerIbuInfo.getModel().getName()).append(", ");
                            filterId.append(beerIbuInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerIbuInfos.get(0).getModel().getName());
                    }
                }
            }
            if (!notEmpty) {
                filterId.append("!");
            }
        }

        if (selectedItem != null) {
            beerAdapter.getItem(category).setSelectedFilter(selectedItem);
            beerAdapter.getItem(category).setSelectedItemId(countryId);
        } else if (!filterId.toString().isEmpty()) {
            beerAdapter.getItem(category).setSelectedFilter(filter.deleteCharAt(filter.length() - 2).toString());
            beerAdapter.getItem(category).setSelectedItemId(filterId.deleteCharAt(filterId.length() - 1).toString());
        }
        beerAdapter.notifyDataSetChanged();
        presenter.saveBeerFilterChanges(beerFilterList);
    }

    private void setRestoSelectedFilter(String filterCategory, int category, String cityName, String cityId) {
        StringBuilder filter = new StringBuilder();
        StringBuilder filterId = new StringBuilder();
        boolean notEmpty = false;
        List<IFlexible> tempList;
        if (filterCategory != null) {
            if (filterCategory.equalsIgnoreCase(FilterKeys.RESTO_TYPE)) {
                List<RestoTypeInfo> restoTypeInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.RESTO_TYPE);
                if (tempList != null) {
                    for (Object o : tempList) {
                        restoTypeInfos.add((RestoTypeInfo) o);
                    }
                    for (int i = 0; i < restoTypeInfos.size(); i++) {
                        if (restoTypeInfos.get(i).getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(restoTypeInfos.get(i).getModel().getName()).append(", ");
                            filterId.append(restoTypeInfos.get(i).getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(restoTypeInfos.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.KITCHEN)) {
                List<KitchenInfo> kitchenInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.KITCHEN);
                if (tempList != null) {
                    for (Object o : tempList) {
                        kitchenInfos.add((KitchenInfo) o);
                    }
                    for (KitchenInfo kitchenInfo : kitchenInfos) {
                        if (kitchenInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(kitchenInfo.getModel().getName()).append(", ");
                            filterId.append(kitchenInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(kitchenInfos.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.PRICE_RANGE)) {
                List<PriceRangeInfo> priceRangeInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.PRICE_RANGE);
                if (tempList != null) {
                    for (Object o : tempList) {
                        priceRangeInfos.add((PriceRangeInfo) o);
                    }
                    for (PriceRangeInfo priceRangeInfo : priceRangeInfos) {
                        if (priceRangeInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(priceRangeInfo.getModel().getName()).append(", ");
                            filterId.append(priceRangeInfo.getModel().getId()).append(",");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(priceRangeInfos.get(0).getModel().getName());
                    }
                }

            } else if (filterCategory.equalsIgnoreCase(FilterKeys.FEATURES)) {
                List<FeatureInfo> featureInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.FEATURES);
                if (tempList != null) {
                    for (Object o : tempList) {
                        featureInfos.add((FeatureInfo) o);
                    }
                    for (FeatureInfo featureInfo : featureInfos) {
                        if (featureInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(featureInfo.getModel().getName()).append(", ");
                            filterId.append(featureInfo.getModel().getId()).append("|");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(featureInfos.get(0).getModel().getName());
                    }
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER_BREWERIES)) {
                List<BreweryInfoSelect> breweryInfoSelects = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER_BREWERIES);
                if (tempList != null) {
                    for (Object o : tempList) {
                        breweryInfoSelects.add((BreweryInfoSelect) o);
                    }
                    for (BreweryInfoSelect breweryInfoSelect : breweryInfoSelects) {
                        if (breweryInfoSelect.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(breweryInfoSelect.getModel().getName()).append(", ");
                            filterId.append(breweryInfoSelect.getModel().getId()).append(",");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(breweryInfoSelects.get(0).getModel().getName());
                    }
                }
            }
            if (!notEmpty) {
                filterId.append("!");
            }
        }
        if (cityName != null) {
            restoAdapter.getItem(category).setSelectedFilter(cityName);
            restoAdapter.getItem(category).setSelectedItemId(cityId);
        } else if (!filterId.toString().isEmpty()) {
            restoAdapter.getItem(category).setSelectedFilter(filter.deleteCharAt(filter.length() - 2).toString());
            restoAdapter.getItem(category).setSelectedItemId(filterId.deleteCharAt(filterId.length() - 1).toString());
        }
        restoAdapter.notifyDataSetChanged();
        presenter.saveRestoFilterChanges(restoFilterList);
    }

    @OnClick(R.id.accept_filter)
    public void acceptFilter() {
        Intent returnIntent = new Intent();
        if (tabsView.getTabs().getSelectedTabPosition() == 0) {
            returnIntent.putExtra("isBeer", false);
            returnIntent.putExtra("check", offer.isChecked() ? 1 : 0);
        } else {
            returnIntent.putExtra("isBeer", true);
            returnIntent.putExtra("check", craft.isChecked() ? 1 : 0);
        }
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
