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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.BeerPower;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.FilterRestoLocation;
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
import com.brewmapp.data.entity.wrapper.FeatureInfo;
import com.brewmapp.data.entity.wrapper.FilterBeerInfo;
import com.brewmapp.data.entity.wrapper.KitchenInfo;
import com.brewmapp.data.entity.wrapper.PriceRangeInfo;
import com.brewmapp.data.entity.wrapper.RestoTypeInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.FilterMapPresenter;
import com.brewmapp.presentation.view.contract.FilterMapView;
import com.brewmapp.presentation.view.impl.widget.TabsView;
import com.brewmapp.utils.events.ShowRestoOnMapEvent;

import org.greenrobot.eventbus.EventBus;

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
    @BindView(R.id.lytProgressBar)
    LinearLayout lytProgressBar;

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
        Paper.init(this);
        titleToolbar.setText(titleContent[0]);
        offer.setVisibility(tabsView.getTabs().getSelectedTabPosition() == 0 ? View.VISIBLE : View.GONE);
        craft.setVisibility(tabsView.getTabs().getSelectedTabPosition() != 0 ? View.VISIBLE : View.GONE);
        tabsView.setItems(Arrays.asList(tabContent), new SimpleTabSelectListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                presenter.storeTabActive(tab.getPosition());
                titleToolbar.setText(titleContent[tab.getPosition()]);
                if (tab.getPosition() == 0) {
                    showRestoFilters(Paper.book().read("restoCategoryList"));
                } else {
                    showBeerFilters(Paper.book().read("beerCategoryList"));
                }
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
        tabsView.getTabs().getTabAt(i).select();
    }

    @Override
    public void goToMap(List<FilterRestoLocation> restoLocations) {
        EventBus.getDefault().post(new ShowRestoOnMapEvent(restoLocations));
        this.finish();
    }

    @Override
    public boolean onItemClick(int position) {
        if ((tabsView.getTabs().getSelectedTabPosition() == 0) && position == 6){
            Toast.makeText(this, "В разработке...", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, FilterByCategory.class);
            intent.putExtra(Keys.FILTER_CATEGORY, position);
            intent.putExtra(Keys.BEER_TYPES, tabsView.getTabs().getSelectedTabPosition());
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
                        data.getIntExtra("category", -999), data.getStringExtra("selectedItem"));
            } else {
                setBeerSelectedFilter(data.getStringExtra("filter"),
                        data.getIntExtra("category", -999), data.getStringExtra("selectedItem"));
            }
        }
    }
    private void setBeerSelectedFilter(String filterCategory, int category, String selectedItem) {
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
                }
                if (!notEmpty) {
                    filter.append("Не имеет значения  ");
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
                }
                if (!notEmpty) {
                    filter.append("Разливное  ");
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
                }
                if (!notEmpty) {
                    filter.append("Любой  ");
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
                }
                if (!notEmpty) {
                    filter.append("Любой  ");
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
                }
                if (!notEmpty) {
                    filter.append("Любой  ");
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
                }
                if (!notEmpty) {
                    filter.append("Любая  ");
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
                }
                if (!notEmpty) {
                    filter.append("Любой  ");
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
                }
                if (!notEmpty) {
                    filter.append("Любой  ");
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
                }
                if (!notEmpty) {
                    filter.append("Любое  ");
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.DENSITY)) {
                List<BeerDensityInfo> beerDensityInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.DENSITY);
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
                }
                if (!notEmpty) {
                    filter.append("Любое  ");
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.IBU)) {
                List<BeerIbuInfo> beerIbuInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.IBU);
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
                }
                if (!notEmpty) {
                    filter.append("Любой  ");
                }
            }
            if (!notEmpty) {
                filterId.append("!");
            }
        }

        if (selectedItem != null) {
            beerAdapter.getItem(category).setSelectedFilter(selectedItem);
        } else if (!filterId.toString().isEmpty()) {
            beerAdapter.getItem(category).setSelectedFilter(filter.deleteCharAt(filter.length() - 2).toString());
            beerAdapter.getItem(category).setSelectedItemId(filterId.deleteCharAt(filterId.length() - 1).toString());
        }
        beerAdapter.notifyDataSetChanged();
        Paper.book().write("beerCategoryList", beerFilterList);
    }

    private void setRestoSelectedFilter(String filterCategory, int category, String cityId) {
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
                }
                if (!notEmpty) {
                    filter.append("Любой  ");
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
                }
                if (!notEmpty) {
                    filter.append("Любая  ");
                }
            } else if (filterCategory.equalsIgnoreCase(FilterKeys.BEER)) {
                List<FilterBeerInfo> filterBeerInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BEER);
                if (tempList != null) {
                    for (Object o : tempList) {
                        filterBeerInfos.add((FilterBeerInfo) o);
                    }
                    for (FilterBeerInfo filterBeerInfo : filterBeerInfos) {
                        if (filterBeerInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(filterBeerInfo.getModel().getTitle_ru()).append(", ");
                            filterId.append(filterBeerInfo.getModel().getId()).append("|");
                        }
                    }
                }
                if (!notEmpty) {
                    filter.append("Любое  ");
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
                }
                if (!notEmpty) {
                    filter.append("Не имеет значения  ");
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
                }
                if (!notEmpty) {
                    filter.append("Не имеют значения  ");
                }
            }
            if (!notEmpty) {
                filterId.append("!");
            }
        }
        if (cityId != null) {
            restoAdapter.getItem(category).setSelectedFilter(cityId);
        } else if (!filterId.toString().isEmpty()) {
            restoAdapter.getItem(category).setSelectedFilter(filter.deleteCharAt(filter.length() - 2).toString());
            restoAdapter.getItem(category).setSelectedItemId(filterId.deleteCharAt(filterId.length() - 1).toString());
        }
        restoAdapter.notifyDataSetChanged();
        Paper.book().write("restoCategoryList", restoFilterList);
    }

    @OnClick(R.id.accept_filter)
    public void acceptFilter() {
        if (tabsView.getTabs().getSelectedTabPosition() == 0) {
            showProgressBar(true);
            presenter.loadRestoCoordinates(Paper.book().read("restoCategoryList"), offer.isChecked() ? 1 : 0);
        } else {
            Toast.makeText(this, "В разаработке" , Toast.LENGTH_SHORT).show();
//            presenter.loadBeerCoordinates(Paper.book().read("beerCategoryList"), craft.isChecked() ? 1 : 0);
        }
    }

    @Override
    public void showProgressBar(boolean show) {
        lytProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
