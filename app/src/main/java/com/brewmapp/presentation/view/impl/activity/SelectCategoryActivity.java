package com.brewmapp.presentation.view.impl.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerAftertaste;
import com.brewmapp.data.entity.BeerBrand;
import com.brewmapp.data.entity.BeerColor;
import com.brewmapp.data.entity.BeerDensity;
import com.brewmapp.data.entity.BeerPack;
import com.brewmapp.data.entity.BeerPower;
import com.brewmapp.data.entity.BeerSmell;
import com.brewmapp.data.entity.BeerTaste;
import com.brewmapp.data.entity.BeerType;
import com.brewmapp.data.entity.Brewery;
import com.brewmapp.data.entity.BreweryShort;
import com.brewmapp.data.entity.BreweryTypes;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.Country;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.PriceRange;
import com.brewmapp.data.entity.PropertyFilterBeer;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoType;
import com.brewmapp.data.entity.wrapper.BeerAftertasteInfo;
import com.brewmapp.data.entity.wrapper.BeerBrandInfo;
import com.brewmapp.data.entity.wrapper.BeerColorInfo;
import com.brewmapp.data.entity.wrapper.BeerDensityInfo;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.entity.wrapper.BeerPackInfo;
import com.brewmapp.data.entity.wrapper.BeerPowerInfo;
import com.brewmapp.data.entity.wrapper.BeerSmellInfo;
import com.brewmapp.data.entity.wrapper.BeerTasteInfo;
import com.brewmapp.data.entity.wrapper.BeerTypeInfo;
import com.brewmapp.data.entity.wrapper.BreweryInfo;
import com.brewmapp.data.entity.wrapper.BreweryInfoSelect;
import com.brewmapp.data.entity.wrapper.CountryInfo;
import com.brewmapp.data.entity.wrapper.FilterBeerInfo;
import com.brewmapp.data.entity.wrapper.KitchenInfo;
import com.brewmapp.data.entity.wrapper.PropertyFilterBeerInfo;
import com.brewmapp.data.entity.wrapper.RestoTypeInfo;
import com.brewmapp.data.pojo.BeerTypes;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.SelectCategoryActivityPresenter;
import com.brewmapp.presentation.view.contract.SelectCategoryActivityView;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;
import com.brewmapp.presentation.view.impl.widget.FinderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;

/**
 * Created by nixus on 01.11.2017.
 */

public class SelectCategoryActivity extends BaseActivity implements SelectCategoryActivityView {

    @BindView(R.id.filter_toolbar)
    Toolbar toolbar;
    @BindView(R.id.filter_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.filter_toolbar_subtitle)
    TextView okButton;
    @BindView(R.id.filter_toolbar_cancel)
    TextView cancel;
    @BindView(R.id.categoryList)
    RecyclerView filterList;
    @BindView(R.id.activity_search_search)
    FinderView finder;
    @BindView(R.id.lyt_empty_view)
    LinearLayout emptyView;
    @BindView(R.id.empty_title)
    TextView emptyTitle;
    @BindView(R.id.view_finder_input)
    AutoCompleteTextView input;
    @BindView(R.id.lytProgressBar)
    LinearLayout lytProgressBar;

    private FlexibleModelAdapter<IFlexible> adapter;
    private EndlessRecyclerOnScrollListener scrollListener;
    private FullSearchPackage fullSearchPackage=new FullSearchPackage();

    private String filterTxt;
    private String filterId;
    private List<IFlexible> original=new ArrayList<>();
    private int filterCategory;
    private String selectedFilter = null;
    private int numberTab;
    private int numberMenuItem;
    private HashMap<String,String> hashMap=new HashMap<>();
    private StringBuilder sb=new StringBuilder();
    @Inject
    SelectCategoryActivityPresenter presenter;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {

        //region setup view
        LinearLayoutManager manager = new LinearLayoutManager(this);
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                fullSearchPackage.setPage(currentPage-1);
                if(getTypeFilter()==1) sendQuery();
            }
        };

        filterList.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        filterList.setLayoutManager(manager);
        adapter = new FlexibleModelAdapter<>(original, this::processAction);
        filterList.setAdapter(adapter);
        //endregion

        //region parse intent
        numberTab =getIntent().getIntExtra(Actions.PARAM1,Integer.MAX_VALUE);
        numberMenuItem =getIntent().getIntExtra(Actions.PARAM2,Integer.MAX_VALUE);
        filterId=getIntent().getStringExtra(Actions.PARAM3);
        filterTxt=getIntent().getStringExtra(Actions.PARAM4);
        //endregion

        initFinder();
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        //region Load data for selection
        switch (numberTab){
            case SearchFragment.TAB_RESTO:
                initRestoFilterByCategory(numberMenuItem);
                break;
            case SearchFragment.TAB_BEER:
                initBeerFilterByCategory(numberMenuItem);
                break;
            case SearchFragment.TAB_BREWERY:
                initBreweryFilterByCategory(numberMenuItem);
                break;
            default:
                commonError(getString(R.string.not_valid_param));

        }
        //endregion
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

        switch (numberTab){
            case SearchFragment.TAB_BEER:
                //region Prepare append items for TAB_BEER
                switch (numberMenuItem) {
                    case FilterBeerField.NAME: {
                        if (fullSearchPackage.getPage() == 0)
                            this.original.clear();
                        for (IFlexible iFlexible : list)
                            ((FilterBeerInfo) iFlexible).getModel().setSelectable(false);
                    }break;
                     case FilterBeerField.COUNTRY: {
                         for (IFlexible iFlexible : list) {
                             CountryInfo countryInfo=(CountryInfo) iFlexible;
                             Country model=countryInfo.getModel();
                             String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                             model.setSelectable(true);
                             model.setSelected(hashMap.containsKey(key));
                         }
                     }break;
                    case FilterBeerField.TYPE: {
                        for (IFlexible iFlexible : list) {
                            BeerTypeInfo beerTypeInfo=(BeerTypeInfo) iFlexible;
                            BeerTypes model=beerTypeInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                    case FilterBeerField.BEER_FILTER: {
                        for (IFlexible iFlexible : list) {
                            PropertyFilterBeerInfo propertyFilterBeerInfo=(PropertyFilterBeerInfo) iFlexible;
                            PropertyFilterBeer model=propertyFilterBeerInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                    case FilterBeerField.BEER_PACK: {
                        for (IFlexible iFlexible : list) {
                            BeerPackInfo beerPack=(BeerPackInfo) iFlexible;
                            BeerPack model=beerPack.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                    case FilterBeerField.COLOR: {
                        for (IFlexible iFlexible : list) {
                            BeerColorInfo colorInfo=(BeerColorInfo) iFlexible;
                            BeerColor model=colorInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                    case FilterBeerField.SMELL: {
                        for (IFlexible iFlexible : list) {
                            BeerSmellInfo beerSmellInfo=(BeerSmellInfo) iFlexible;
                            BeerSmell model=beerSmellInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                    case FilterBeerField.TASTE: {
                        for (IFlexible iFlexible : list) {
                            BeerTasteInfo beerTasteInfo=(BeerTasteInfo) iFlexible;
                            BeerTaste model=beerTasteInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                    case FilterBeerField.AFTER_TASTE: {
                        for (IFlexible iFlexible : list) {
                            BeerAftertasteInfo aftertasteInfo=(BeerAftertasteInfo) iFlexible;
                            BeerAftertaste model=aftertasteInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                    case FilterBeerField.BREWERY: {
                        for (IFlexible iFlexible : list) {
                            BreweryInfo breweryInfo=(BreweryInfo) iFlexible;
                            Brewery model=breweryInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                            model.setSelectable(true);
                        }
                    }break;
                    case FilterBeerField.DENSITY: {
                        for (IFlexible iFlexible : list) {
                            BeerDensityInfo beerDensityInfo=(BeerDensityInfo) iFlexible;
                            BeerDensity model=beerDensityInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                    case FilterBeerField.BRAND: {
                        for (IFlexible iFlexible : list) {
                            BeerBrandInfo beerBrandInfo=(BeerBrandInfo) iFlexible;
                            BeerBrand model=beerBrandInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                    case FilterBeerField.POWER: {
                        for (IFlexible iFlexible : list) {
                            BeerPowerInfo beerPowerInfo=(BeerPowerInfo) iFlexible;
                            BeerPower model=beerPowerInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                }
                //endregion
                break;
            case SearchFragment.TAB_BREWERY:
                switch (numberMenuItem){
                    case FilterBreweryField.COUNTRY: {
                        for (IFlexible iFlexible : list) {
                            CountryInfo countryInfo=(CountryInfo) iFlexible;
                            Country model=countryInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelectable(true);
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                    case FilterBreweryField.BRAND: {
                        for (IFlexible iFlexible : list) {
                            BeerBrandInfo beerBrandInfo=(BeerBrandInfo) iFlexible;
                            BeerBrand model=beerBrandInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;
                    case FilterBreweryField.TYPE_BEER: {
                        for (IFlexible iFlexible : list) {
                            BeerTypeInfo beerTypeInfo=(BeerTypeInfo) iFlexible;
                            BeerTypes model=beerTypeInfo.getModel();
                            String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            model.setSelected(hashMap.containsKey(key));
                        }
                    }break;

                }
                break;
            case SearchFragment.TAB_RESTO:
                //region Prepare append items for TAB_RESTO
                switch (numberMenuItem){
                    case FilterRestoField.BEER:
                        for (IFlexible iFlexible:list)
                            ((FilterBeerInfo)iFlexible).getModel().setSelectable(true);
                    case FilterRestoField.NAME:
                    case FilterRestoField.CITY:
                        if(fullSearchPackage.getPage()==0)
                            this.original.clear();
                        break;
                    case FilterRestoField.TYPE:
                        this.original.clear();
                        for(IFlexible iFlexible:list){
                            RestoType model= ((RestoTypeInfo) iFlexible).getModel();
                            model.setSelected(hashMap.containsKey(sb.delete(0,sb.length()).append(model.getId()).toString()));
                        }
                        break;
                    case FilterRestoField.KITCHEN:
                        this.original.clear();
                        for(IFlexible iFlexible:list){
                            Kitchen model= ((KitchenInfo) iFlexible).getModel();
                            model.setSelected(hashMap.containsKey(sb.delete(0,sb.length()).append(model.getId()).toString()));
                        }
                        break;
                    case FilterRestoField.PRICE:
                        this.original.clear();
                        break;
                }
                //endregion
                break;
        }

        //region Process append
        adapter.notifyDataSetChanged();
        int numberStartNotificationInsert=this.original.size();
        this.original.addAll(list);
        adapter.notifyItemRangeInserted(numberStartNotificationInsert,list.size());
        //endregion
        //region Visible control
        showProgressBar(false);
        emptyView.setVisibility(original.size()==0?View.VISIBLE:View.GONE);
        filterList.setVisibility(original.size()==0?View.GONE:View.VISIBLE);
        //endregion
    }

    @Override
    public void showProgressBar(boolean show) {
        lytProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void commonError(String... strings) {
        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();
    }

    @OnClick(R.id.filter_toolbar_subtitle)
    public void okFilterClicked() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Actions.PARAM1, numberTab);
        returnIntent.putExtra(Actions.PARAM2, numberMenuItem);
        returnIntent.putExtra(Actions.PARAM3, strJoin( hashMap.keySet().toArray(),","));
        returnIntent.putExtra(Actions.PARAM4, strJoin( hashMap.values().toArray(),","));

        setResult(RESULT_OK, returnIntent);
        finish();

    }

    @OnClick(R.id.filter_toolbar_cancel)
    public void cancelFilterClicked() {
        onBackPressed();
    }

//*********************************************

    private void filterStringToHashMap() {
        try {
            String[] ids = filterId.split(",");
            String[] txts = filterTxt.split(",");
            int i = 0;
            for (String s : ids)
                if(!"null".equals(s))
                    hashMap.put(
                            new StringBuilder().append(s).toString(),
                            new StringBuilder().append(txts[i++]).toString()
                    );
        } catch (Exception e) {
        }
        invalidateMenu();


    }

    private void invalidateMenu() {
        okButton.setVisibility(hashMap.size()==0?View.GONE:View.VISIBLE);
    }

    private void initBreweryFilterByCategory(int filterId) {
        this.filterCategory = filterId;
        switch (filterId) {
            case FilterBreweryField.NAME:
                toolbarTitle.setText(R.string.search_beer_factory);
                filterList.addOnScrollListener(scrollListener);
                fullSearchPackage.setType(Keys.TYPE_BREWERY);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_brewery));
                toolbarTitle.setText(R.string.search_brewery_name);
                break;
            case FilterBreweryField.COUNTRY:
                showProgressBar(true);
                toolbarTitle.setText(R.string.select_country);
                presenter.loadCountries();
                finder.clearFocus();
                break;
            case FilterBreweryField.BRAND:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_brand);
                filterList.addOnScrollListener(scrollListener);
                fullSearchPackage.setType(Keys.TYPE_BEERBRAND);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_brand));
                filterStringToHashMap();
                finder.clearFocus();
                break;
            case FilterBreweryField.TYPE_BEER:
                toolbarTitle.setText(R.string.search_beer_type);
                presenter.loadBeerTypes(fullSearchPackage);
                finder.clearFocus();
                finder.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void initBeerFilterByCategory(int filterId) {
        this.filterCategory = filterId;
        switch (filterId) {
            case FilterBeerField.NAME:
                filterList.addOnScrollListener(scrollListener);
                fullSearchPackage.setType(Keys.TYPE_BEER);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_beer));
                toolbarTitle.setText(R.string.search_beer_name);
                hashMap.clear();
                invalidateMenu();
                break;
            case FilterBeerField.COUNTRY:
                showProgressBar(true);
                toolbarTitle.setText(R.string.select_country);
                filterStringToHashMap();
                presenter.loadCountries();
                finder.clearFocus();
                break;
            case FilterBeerField.TYPE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_type);
                filterStringToHashMap();
                presenter.loadBeerTypes(fullSearchPackage);
                finder.clearFocus();
                break;
            case FilterBeerField.PRICE_BEER:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_price);
                filterStringToHashMap();
                presenter.loadPriceRangeTypes("beer");
                finder.clearFocus();
                break;
            case FilterBeerField.BEER_PACK:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_bootle);
                filterStringToHashMap();
                finder.clearFocus();
                presenter.loadBeerPack();

                break;
            case FilterBeerField.BRAND:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_brand);
                filterList.addOnScrollListener(scrollListener);
                fullSearchPackage.setType(Keys.TYPE_BEERBRAND);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_brand));
                filterStringToHashMap();

                break;
            case FilterBeerField.COLOR:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_color);
                filterStringToHashMap();
                finder.clearFocus();
                presenter.loadBeerColor();
                break;
            case FilterBeerField.TASTE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_taste);
                filterStringToHashMap();
                finder.clearFocus();
                presenter.loadBeerTaste();
                break;
            case FilterBeerField.SMELL:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_smell);
                filterStringToHashMap();
                finder.clearFocus();
                presenter.loadBeerSmell();
                break;
            case FilterBeerField.AFTER_TASTE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_after_taste);
                filterStringToHashMap();
                finder.clearFocus();
                presenter.loadBeerAfterTaste();
                break;
            case FilterBeerField.POWER:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_power);
                filterStringToHashMap();
                presenter.loadBeerPower();
                finder.clearFocus();
                break;
            case FilterBeerField.DENSITY:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_type_broj);
                filterStringToHashMap();
                presenter.loadBeerDensity();
                finder.clearFocus();
                break;
            case FilterBeerField.IBU:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_ibu);
                    presenter.loadBeerIbu();
                break;
            case FilterBeerField.BREWERY:
                toolbarTitle.setText(R.string.search_beer_factory);
                filterList.addOnScrollListener(scrollListener);
                fullSearchPackage.setType(Keys.TYPE_BREWERY);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_brewery));
                filterStringToHashMap();
                break;
            case FilterBeerField.BEER_FILTER:
                toolbarTitle.setText(R.string.search_beer_filter);
                finder.clearFocus();
                finder.setVisibility(View.GONE);
                filterStringToHashMap();
                presenter.loadFilter();
                break;

            default:commonError(getString(R.string.not_valid_param));
        }
    }

    private void initRestoFilterByCategory(int filterId) {
        this.filterCategory = filterId;
        switch (filterId) {
            case FilterRestoField.NAME:
                filterList.addOnScrollListener(scrollListener);
                fullSearchPackage.setType(Keys.TYPE_RESTO);
                emptyView.setVisibility(View.GONE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_resto));
                toolbarTitle.setText(R.string.search_resto_name);
                hashMap.clear();
                invalidateMenu();
                break;
            case FilterRestoField.TYPE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_resto_type);
                filterStringToHashMap();
                presenter.loadRestoTypes();
                finder.clearFocus();
                break;
            case FilterRestoField.BEER:
                filterList.addOnScrollListener(scrollListener);
                fullSearchPackage.setType(Keys.TYPE_BEER);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_beer));
                toolbarTitle.setText(R.string.search_beer_name);
                hashMap.clear();
                invalidateMenu();
                break;
            case FilterRestoField.KITCHEN:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_resto_kitchen);
                filterStringToHashMap();
                presenter.loadKitchenTypes();
                finder.clearFocus();
                break;
            case FilterRestoField.PRICE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_resto_price);
                presenter.loadPriceRangeTypes("resto");
                finder.clearFocus();
                break;
            case FilterRestoField.CITY:
                break;
            case FilterRestoField.METRO:
                toolbarTitle.setText(R.string.select_metro);
                break;
            case FilterRestoField.FEATURES:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_resto_other);
                    presenter.loadFeatureTypes();
                break;
            default:
                commonError(getString(R.string.not_valid_param));
        }
    }

    private void initFinder() {

        if(getTypeFilter()==0) {
            finder.setListener(string -> {
                if (original != null) {
                    adapter.setSearchText(string);
                    adapter.filterItems(original);
                }
            });
        }else {
            finder.setListener(string -> prepareQuery(string));
        }

    }

    private int getTypeFilter() {
        //0-local filter
        //1-remote filter
        int type_filter=0;

        switch (numberTab){
            case SearchFragment.TAB_BEER:
                if(FilterBeerField.NAME == numberMenuItem)
                    type_filter=1;
                else if(FilterBeerField.BREWERY == numberMenuItem)
                    type_filter=1;
                else if(FilterBeerField.BRAND == numberMenuItem)
                    type_filter=1;
                break;
            case SearchFragment.TAB_BREWERY:
                if(FilterBreweryField.NAME == numberMenuItem)
                    type_filter=1;
                else if(FilterBreweryField.BRAND == numberMenuItem)
                    type_filter=1;
                break;
            case SearchFragment.TAB_RESTO:
                if(FilterRestoField.NAME == numberMenuItem)
                    type_filter=1;
                else if(FilterRestoField.BEER == numberMenuItem)
                    type_filter=1;
                else if(FilterRestoField.CITY == numberMenuItem)
                    type_filter=1;
                break;
        }

        return type_filter;
    }

    private void prepareQuery(String stringSearch) {
        fullSearchPackage.setPage(0);
        fullSearchPackage.setStringSearch(stringSearch);
        scrollListener.reset();
        this.original.clear();
        emptyView.setVisibility(View.GONE);
        filterList.setVisibility(View.GONE);
        sendQuery();
    }

    private void sendQuery() {
        if (fullSearchPackage.getStringSearch().length() > 0){
            if(numberMenuItem ==FilterRestoField.CITY){
                presenter.sendQueryCitySearch(fullSearchPackage);
            }else{
                presenter.sendQueryFullSearch(fullSearchPackage);
            }
        }
    }

    private void processAction(int action, Object payload) {
        String key=null;
        String name=null;
        boolean selected=false;


        switch (numberTab){
            case SearchFragment.TAB_RESTO:
                //region select item for TAB_RESTO
                switch (numberMenuItem){
                    case FilterRestoField.NAME:
                        Starter.RestoDetailActivity(this,String.valueOf(((Resto)payload).getId()));
                        break;
                    case FilterRestoField.TYPE: {
                        RestoType model = (RestoType) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterRestoField.BEER: {
                        Beer model = (Beer) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getTitle()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterRestoField.KITCHEN: {
                        Kitchen model = (Kitchen) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterRestoField.PRICE: {
                        PriceRange model = (PriceRange) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterRestoField.CITY: {
                        City model = (City) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    default: {
                        commonError(getString(R.string.not_valid_param));
                        return;
                    }
                }
                //endregion
                break;
            case SearchFragment.TAB_BEER:
                //region select item for TAB_BEER
                switch (numberMenuItem) {
                    case FilterBeerField.NAME:
                        Starter.BeerDetailActivity(this, String.valueOf(((Beer) payload).getId()));
                        break;
                    case FilterBeerField.COUNTRY: {
                        Country model = (Country) payload;
                        key = sb.delete(0, sb.length()).append(model.getId()).toString();
                        name = sb.delete(0, sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.TYPE:{
                        BeerTypes model = (BeerTypes) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.BEER_FILTER:{
                        PropertyFilterBeer model = (PropertyFilterBeer) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.BEER_PACK:{
                        BeerPack model = (BeerPack) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.COLOR:{
                        BeerColor model = (BeerColor) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.SMELL:{
                        BeerSmell model = (BeerSmell) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.TASTE:{
                        BeerTaste model = (BeerTaste) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.AFTER_TASTE:{
                        BeerAftertaste model = (BeerAftertaste) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.BREWERY:{
                        Brewery model = (Brewery) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.DENSITY:{
                        BeerDensity model = (BeerDensity) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.BRAND:{
                        BeerBrand model = (BeerBrand) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.POWER:{
                        BeerPower model = (BeerPower) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBeerField.PRICE_BEER:{
                        PriceRange model = (PriceRange) payload;
                        key = sb.delete(0,sb.length()).append(model.getId()).toString();
                        name = sb.delete(0,sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    default: {
                        commonError(getString(R.string.not_valid_param));
                        return;
                    }
                }
                //endregion
                break;
            case SearchFragment.TAB_BREWERY:
                //region select item for TAB_BREWERY
                switch (numberMenuItem) {
                    case FilterBeerField.NAME:
                        Starter.BreweryDetailsActivity(this, String.valueOf(((Brewery) payload).getId()));
                        break;
                    case FilterBreweryField.COUNTRY: {
                        Country model = (Country) payload;
                        key = sb.delete(0, sb.length()).append(model.getId()).toString();
                        name = sb.delete(0, sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBreweryField.BRAND: {
                        BeerBrand model = (BeerBrand) payload;
                        key = sb.delete(0, sb.length()).append(model.getId()).toString();
                        name = sb.delete(0, sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterBreweryField.TYPE_BEER: {
                        BeerTypes model = (BeerTypes) payload;
                        key = sb.delete(0, sb.length()).append(model.getId()).toString();
                        name = sb.delete(0, sb.length()).append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                }
                //endregion
                    break;
            default:
                commonError(getString(R.string.not_valid_param));

        }
        if(selected) hashMap.put(key,name);else hashMap.remove(key);
        invalidateMenu();
    }

    public String strJoin(Object[] aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.length; i < il; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr[i]);
        }
        return sbStr.toString();
    }
}
