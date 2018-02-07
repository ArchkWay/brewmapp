package com.brewmapp.presentation.view.impl.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.SearchFragmentPackage;
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
import com.brewmapp.presentation.presenter.contract.SearchFragmentPresenter;
import com.brewmapp.presentation.view.contract.SearchAllView;
import com.brewmapp.presentation.view.impl.activity.SelectCategoryActivity;
import com.brewmapp.presentation.view.impl.activity.ResultSearchActivity;
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
 * Created by ovcst on 24.08.2017.
 */

public class SearchFragment extends BaseFragment implements SearchAllView, FlexibleAdapter.OnItemClickListener  {

    @BindView(R.id.accept_filter)
    Button search;
    @BindView(R.id.filter_list)
    RecyclerView list;
    @BindView(R.id.offer)
    CheckBox offer;
    @BindView(R.id.craft)
    CheckBox craft;
    @BindView(R.id.filter)
    CheckBox filterBeer;
    @BindView(R.id.fragment_events_tabs)
    TabsView tabsView;

    @Inject    SearchFragmentPresenter presenter;

    private FlexibleAdapter<FilterRestoField> restoAdapter;
    private FlexibleAdapter<FilterBeerField> beerAdapter;
    private FlexibleAdapter<FilterBreweryField> breweryAdapter;
    private List<FilterRestoField> restoFilterList;
    private List<FilterBeerField> beerFilterList;
    private List<FilterBreweryField> breweryList;

    private SearchFragmentPackage searchFragmentPackage = new SearchFragmentPackage();

    public static final int TAB_RESTO = 0;
    public static final int TAB_BEER = 1;
    public static final int TAB_BREWERY = 2;
    public static final String CATEGORY_LIST_RESTO = "restoCategoryList";
    public static final String CATEGORY_LIST_BEER = "beerCategoryList";
    public static final String CATEGORY_LIST_BREWERY = "breweryCategoryList";



    private String[] searchContent = ResourceHelper.getResources().getStringArray(R.array.full_search);
    private String[] titleContent = ResourceHelper.getResources().getStringArray(R.array.search_title);

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_search;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Keys.SEARCH, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        interractor().processSetActionBar(0);
        tabsView.setItems(Arrays.asList(searchContent), new SimpleTabSelectListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                 searchFragmentPackage.setMode(tab.getPosition());
                presenter.storeTabActive(tab.getPosition());
                offer.setVisibility(tabsView.getTabs().getSelectedTabPosition() == 0 ? View.VISIBLE : View.GONE);
                craft.setVisibility(tabsView.getTabs().getSelectedTabPosition() == 1 ? View.VISIBLE : View.GONE);
                filterBeer.setVisibility(tabsView.getTabs().getSelectedTabPosition() == 1 ? View.VISIBLE : View.GONE);
                interractor().processSetFilterFragmentActionBar(SearchFragment.this);
            }
        });
    }

    @Override
    public boolean onItemClick(int position) {
        if ((tabsView.getTabs().getSelectedTabPosition() == 0) && position == 6){
            Toast.makeText(getContext(), "В разработке...", Toast.LENGTH_SHORT).show();
        } else {
            String filterTxt=null;
            String filterId=null;
            switch (tabsView.getTabs().getSelectedTabPosition()){
                case TAB_RESTO:
                    filterTxt=restoAdapter.getItem(position).getSelectedFilter();
                    filterId=restoAdapter.getItem(position).getSelectedItemId();
                    break;
            }

            Intent intent = new Intent(getContext(), SelectCategoryActivity.class);
            intent.putExtra(Actions.PARAM1,tabsView.getTabs().getSelectedTabPosition());
            intent.putExtra(Actions.PARAM2,position);
            intent.putExtra(Actions.PARAM3,new StringBuilder().append(filterId).toString());
            intent.putExtra(Actions.PARAM4,new StringBuilder().append(filterTxt).toString());

            startActivityForResult(intent, RequestCodes.REQUEST_SEARCH_CODE);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            if(requestCode == RequestCodes.REQUEST_SEARCH_CODE)
                showResult(data);
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
    public int getMenuToInflate() {
        return 0;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return titleContent[searchFragmentPackage.getMode()];
    }


    @Override
    public void enableControls(boolean enabled, int code) {
    }

    @Override
    protected void prepareView(View view) {
        super.prepareView(view);
    }

    @Override
    public void showRestoFilters(List<FilterRestoField> fieldList) {
        this.restoFilterList = fieldList;
        restoAdapter = new FlexibleAdapter<>(fieldList, this);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.addItemDecoration(new ListDivider(getContext(), ListDivider.VERTICAL_LIST));
        list.setNestedScrollingEnabled(true);
        list.setAdapter(restoAdapter);
    }

    @Override
    public void showBeerFilters(List<FilterBeerField> fieldList) {
        this.beerFilterList = fieldList;
        beerAdapter = new FlexibleAdapter<>(fieldList, this);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.addItemDecoration(new ListDivider(getContext(), ListDivider.VERTICAL_LIST));
        list.setNestedScrollingEnabled(true);
        list.setAdapter(beerAdapter);
    }

    @Override
    public void showBreweryFilters(List<FilterBreweryField> fieldList) {
        this.breweryList = fieldList;
        breweryAdapter = new FlexibleAdapter<>(fieldList, this);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.addItemDecoration(new ListDivider(getContext(), ListDivider.VERTICAL_LIST));
        list.setNestedScrollingEnabled(true);
        list.setAdapter(breweryAdapter);
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

    private void setBrewerySelectedFilter(String filterCategory, int category, String cityName, String cityId) {
        StringBuilder filter = new StringBuilder();
        StringBuilder filterId = new StringBuilder();
        boolean notEmpty = false;
        List<IFlexible> tempList;
        if (filterCategory != null) {
            if (filterCategory.equalsIgnoreCase(FilterKeys.BREWERY_BEER_TYPE)) {
                List<BeerTypeInfo> beerTypeInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BREWERY_BEER_TYPE);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerTypeInfos.add((BeerTypeInfo) o);
                    }
                    for (int i = 0; i < beerTypeInfos.size(); i++) {
                        if (beerTypeInfos.get(i).getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(beerTypeInfos.get(i).getModel().getName()).append(", ");
                            filterId.append(beerTypeInfos.get(i).getModel().getId()).append(",");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerTypeInfos.get(0).getModel().getName());
                    }
                }
            }else if (filterCategory.equalsIgnoreCase(FilterKeys.BREWERY_BEER_BRAND)) {
                List<BeerBrandInfo> beerBrandInfos = new ArrayList<>();
                tempList = Paper.book().read(FilterKeys.BREWERY_BEER_BRAND);
                if (tempList != null) {
                    for (Object o : tempList) {
                        beerBrandInfos.add((BeerBrandInfo) o);
                    }
                    for (BeerBrandInfo brandInfo : beerBrandInfos) {
                        if (brandInfo.getModel().isSelected()) {
                            notEmpty = true;
                            filter.append(brandInfo.getModel().getName()).append(", ");
                            filterId.append(brandInfo.getModel().getId()).append(",");
                        }
                    }
                    if (!notEmpty) {
                        filter.append(beerBrandInfos.get(0).getModel().getName());
                    }
                }
            }
            if (!notEmpty) {
                filterId.append("!");
            }
        }
        if (cityName != null) {
            breweryAdapter.getItem(category).setSelectedFilter(cityName);
            breweryAdapter.getItem(category).setSelectedItemId(cityId);
        } else if (!filterId.toString().isEmpty()) {
            breweryAdapter.getItem(category).setSelectedFilter(filter.deleteCharAt(filter.length() - 2).toString());
            breweryAdapter.getItem(category).setSelectedItemId(filterId.deleteCharAt(filterId.length() - 1).toString());
        }
        String selectedItemId=breweryAdapter.getItem(category).getSelectedItemId();
        if("null".equals(selectedItemId))
            breweryAdapter.getItem(category).setSelectedItemId(null);
        breweryAdapter.notifyDataSetChanged();
        presenter.saveBreweryFilterChanges(breweryList);
    }

    @OnClick(R.id.accept_filter)
    public void acceptFilter() {
        Intent intent = new Intent(getActivity(), ResultSearchActivity.class);
        switch (tabsView.getTabs().getSelectedTabPosition()) {
            case TAB_RESTO:
                intent.putExtra("craft", craft.isChecked() ? 1 : 0);
                intent.putExtra("filterBeer", filterBeer.isChecked() ? 1 : 0);
                intent.putExtra(Keys.SEARCH_RESULT, 0);
                startActivity(intent);
                break;
            case TAB_BEER:
                intent.putExtra("offer", craft.isChecked() ? 1 : 0);
                intent.putExtra(Keys.SEARCH_RESULT, 1);
                startActivity(intent);
                break;
            case TAB_BREWERY:
                intent.putExtra(Keys.SEARCH_RESULT, 2);
                startActivity(intent);
                break;
            default :break;
        }
    }

    @Override
    public void setTabActive(int i) {
        tabsView.getTabs().getTabAt(i);
    }

    @Override
    public void showResult(Intent data) {
        int numberTab=data.getIntExtra(Actions.PARAM1,Integer.MAX_VALUE);
        int numberMenuItem=data.getIntExtra(Actions.PARAM2,Integer.MAX_VALUE);
        String filterID=data.getStringExtra(Actions.PARAM3);
        String filterTXT=data.getStringExtra(Actions.PARAM4);

        switch (numberTab) {
            case TAB_RESTO:
                switch (numberMenuItem){
                    case FilterRestoField.TYPE:
                        restoAdapter.getItem(numberMenuItem).setSelectedItemId(filterID);
                        restoAdapter.getItem(numberMenuItem).setSelectedFilter(filterTXT);
                        restoAdapter.notifyDataSetChanged();
                        Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, restoFilterList );
                        break;

                }

//                setRestoSelectedFilter(data.getStringExtra("filter"),
//                        data.getIntExtra("category", -999), data.getStringExtra("selectedItem"),
//                        data.getStringExtra("selectedItemId"));
                break;
            case TAB_BEER:
                setBeerSelectedFilter(data.getStringExtra("filter"),
                        data.getIntExtra("category", -999), data.getStringExtra("selectedItem"),
                        data.getStringExtra("selectedItemId"));
                break;
            case TAB_BREWERY:
                setBrewerySelectedFilter(data.getStringExtra("filter"),
                        data.getIntExtra("category", -999), data.getStringExtra("selectedItem"),
                        data.getStringExtra("selectedItemId"));
                break;
        }
    }
}
