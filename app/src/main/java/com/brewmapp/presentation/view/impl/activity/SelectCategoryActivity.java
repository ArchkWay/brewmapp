package com.brewmapp.presentation.view.impl.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.brewmapp.data.entity.Brewery;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.Country;
import com.brewmapp.data.entity.Feature;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.PriceRange;
import com.brewmapp.data.entity.PropertyFilterBeer;
import com.brewmapp.data.entity.Region;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoType;
import com.brewmapp.data.entity.wrapper.BeerAftertasteInfo;
import com.brewmapp.data.entity.wrapper.BeerBrandInfo;
import com.brewmapp.data.entity.wrapper.BeerColorInfo;
import com.brewmapp.data.entity.wrapper.BeerDensityInfo;
import com.brewmapp.data.entity.wrapper.BeerPackInfo;
import com.brewmapp.data.entity.wrapper.BeerPowerInfo;
import com.brewmapp.data.entity.wrapper.BeerSmellInfo;
import com.brewmapp.data.entity.wrapper.BeerTasteInfo;
import com.brewmapp.data.entity.wrapper.BeerTypeInfo;
import com.brewmapp.data.entity.wrapper.BreweryInfo;
import com.brewmapp.data.entity.wrapper.CityInfo;
import com.brewmapp.data.entity.wrapper.CountryInfo;
import com.brewmapp.data.entity.wrapper.FeatureInfo;
import com.brewmapp.data.entity.wrapper.FilterBeerInfo;
import com.brewmapp.data.entity.wrapper.KitchenInfo;
import com.brewmapp.data.entity.wrapper.PropertyFilterBeerInfo;
import com.brewmapp.data.entity.wrapper.RegionInfo;
import com.brewmapp.data.entity.wrapper.RestoTypeInfo;
import com.brewmapp.data.pojo.BeerTypes;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.SelectCategoryActivityPresenter;
import com.brewmapp.presentation.view.contract.SelectCategoryActivityView;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;
import com.brewmapp.presentation.view.impl.widget.FinderView;
import com.brewmapp.utils.events.markerCluster.MapUtils;

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

public class SelectCategoryActivity extends BaseActivity implements SelectCategoryActivityView
{
    public static final String CHOOSE_COUNTRY = "CHOOSE_COUNTRY_SESSION";
    public static final String CHOOSE_CITY = "CHOOSE_CITY_SESSION";

    public static final String CHOOSE_REVIEWS_RELATED_MODELS = "CHOOSE_REVIEWS_RELATED_MODELS";
    public static final String CHOOSE_NEWS_RELATED_MODELS = "CHOOSE_NEWS_RELATED_MODELS";

    //region BindView
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
    //endregion

    //region Private
    private FlexibleModelAdapter<IFlexible> adapter;
    private EndlessRecyclerOnScrollListener scrollListener;
    private FullSearchPackage fullSearchPackage=new FullSearchPackage();
    private String filterTxt;
    private String filterId;
    private List<IFlexible> original=new ArrayList<>();
    private String numberTab;
    private int numberMenuItem;
    private HashMap<String,String> hashMap=new HashMap<>();
    private StringBuilder sb=new StringBuilder();
    private String mCountryId = null;
    //endregion

    //region Inject
    @Inject
    SelectCategoryActivityPresenter presenter;
    //endregion

    //region Impl SelectCategoryActivity
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
        emptyView.setVisibility(View.VISIBLE);
        //endregion

        //region parse intent
        Intent intent=getIntent();
        numberTab =intent.getStringExtra(Actions.PARAM1);
        numberMenuItem =intent.getIntExtra(Actions.PARAM2,Integer.MAX_VALUE);
        filterId=intent.getStringExtra(Actions.PARAM3);
        filterTxt=intent.getStringExtra(Actions.PARAM4);
        fullSearchPackage.setLat(intent.getDoubleExtra(Actions.PARAM5,0));
        fullSearchPackage.setLon(intent.getDoubleExtra(Actions.PARAM6,0));
        fullSearchPackage.setCity(intent.getStringExtra(Actions.PARAM7));
        //endregion

        initFinder();
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        //region Load data for selection
        switch (numberTab){
            case SearchFragment.CATEGORY_LIST_RESTO:
                initRestoFilterByCategory(numberMenuItem);
                break;
            case SearchFragment.CATEGORY_LIST_BEER:
                initBeerFilterByCategory(numberMenuItem);
                break;
            case SearchFragment.CATEGORY_LIST_BREWERY:
                initBreweryFilterByCategory(numberMenuItem);
                break;
            case CHOOSE_COUNTRY:
                initCountryList(numberMenuItem);
                break;
            case CHOOSE_CITY:
                initCityList(mCountryId);
                break;
            case CHOOSE_REVIEWS_RELATED_MODELS:
                initReviewRelModelsList();
                break;
            case CHOOSE_NEWS_RELATED_MODELS:
                initNewsRelModelsList();
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
    //endregion

    //region Impl SelectCategoryActivityView
    @Override
    public void enableControls(boolean enabled, int code) {
    }

    @Override
    public void appendItems(List<IFlexible> list) {

        Log.v("xzxz", "---DBG appendItems size="+ list.size());

        switch (numberTab){
            case SearchFragment.CATEGORY_LIST_BEER:
                //region Prepare append items for TAB_BEER
                switch (numberMenuItem) {
                    case FilterBeerField.NAME:{
                        if (fullSearchPackage.getPage() == 0)
                            this.original.clear();
                        for (IFlexible iFlexible : list)
                            ((FilterBeerInfo) iFlexible).getModel().setSelectable(false);
                    }break;
                    case FilterBeerField.CITY:{
                        if (fullSearchPackage.getPage() == 0)
                            this.original.clear();
                    }break;
                     case FilterBeerField.COUNTRY: {
                         for (IFlexible iFlexible : list) {
                             CountryInfo countryInfo=(CountryInfo) iFlexible;
                             Country model=countryInfo.getModel();
                             String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                            // model.setSelectable(true);
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
            case SearchFragment.CATEGORY_LIST_BREWERY:
                //region Prepare append items for TAB_BREWERY
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
                //endregion
                break;
            case SearchFragment.CATEGORY_LIST_RESTO:
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
                    case FilterRestoField.FEATURES:

                        break;
                }
                //endregion
                break;
            case CHOOSE_COUNTRY:{

                    for (IFlexible iFlexible : list) {
                        CountryInfo countryInfo=(CountryInfo) iFlexible;
                        Country model=countryInfo.getModel();
                        String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                        // model.setSelectable(true);
                        model.setSelected(hashMap.containsKey(key));
                    }

                break;
            }
            case CHOOSE_CITY:{

//                for (IFlexible iFlexible : list) {
//                    RegionInfo item=(RegionInfo) iFlexible;
//                    Region model = item.getModel();
//                    String key=sb.delete(0,sb.length()).append(model.getId()).toString();
//
//                    //model.setSelected(hashMap.containsKey(key));
//                }

                for (IFlexible iFlexible : list) {
                    CityInfo item=(CityInfo) iFlexible;
                    City model = item.getModel();
                    String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                    // model.setSelectable(true);
                    model.setSelected(hashMap.containsKey(key));
                }

                break;
            }
            case CHOOSE_REVIEWS_RELATED_MODELS:{

                for (IFlexible iFlexible : list) {
                    CountryInfo item=(CountryInfo) iFlexible;
                    Country model = item.getModel();
                    String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                    // model.setSelectable(true);
                    model.setSelected(hashMap.containsKey(key));
                }

                break;
            }
            case CHOOSE_NEWS_RELATED_MODELS:{

                for (IFlexible iFlexible : list) {
                    CountryInfo item=(CountryInfo) iFlexible;
                    Country model = item.getModel();
                    String key=sb.delete(0,sb.length()).append(model.getId()).toString();
                    // model.setSelectable(true);
                    model.setSelected(hashMap.containsKey(key));
                }

                break;
            }
        }

        //region Process append
        adapter.notifyDataSetChanged();
        int numberStartNotificationInsert=this.original.size();
        this.original.addAll(list);
        Log.v("xzxz", "---DBG before notifyItemRangeInserted list.size="+ list.size());
        adapter.notifyItemRangeInserted(numberStartNotificationInsert,list.size());
        //endregion

        //region Visible control
        showProgressBar(false);
        emptyView.setVisibility(original.size()==0?View.VISIBLE:View.GONE);
        filterList.setVisibility(original.size()==0?View.GONE:View.VISIBLE);
//        if(getTypeFilter()==0)
//            finder.setVisibility(original.size()==0?View.GONE:View.VISIBLE);
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
    //endregion

    //region User Evens
    @OnClick(R.id.filter_toolbar_subtitle)
    public void okFilterClicked() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Actions.PARAM1, numberTab);
        returnIntent.putExtra(Actions.PARAM2, numberMenuItem);
        returnIntent.putExtra(Actions.PARAM3, MapUtils.strJoin(hashMap.keySet().toArray(),","));
        returnIntent.putExtra(Actions.PARAM4, MapUtils.strJoin(hashMap.values().toArray(),","));

        setResult(RESULT_OK, returnIntent);
        finish();

    }

//    @OnClick(R.id.filter_toolbar_next)
//    public void okNextClicked() {
//
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra(Actions.PARAM1, numberTab);
//        returnIntent.putExtra(Actions.PARAM2, numberMenuItem);
//        returnIntent.putExtra(Actions.PARAM3, MapUtils.strJoin(hashMap.keySet().toArray(),","));
//        returnIntent.putExtra(Actions.PARAM4, MapUtils.strJoin(hashMap.values().toArray(),","));
//
//        setResult(RESULT_OK, returnIntent);
//        finish();
//
//    }

    @OnClick(R.id.filter_toolbar_cancel)
    public void cancelFilterClicked() {
        onBackPressed();
    }

    private void processAction(int action, Object payload) {
        String key=null;
        String name=null;
        boolean selected=false;


        switch (numberTab){
            case SearchFragment.CATEGORY_LIST_RESTO:
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
                    case FilterRestoField.FEATURES: {
//                        FeatureInfo
                        Feature model = (Feature) payload;
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
            case SearchFragment.CATEGORY_LIST_BEER:
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
                    case FilterBeerField.CITY: {
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
            case SearchFragment.CATEGORY_LIST_BREWERY:
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
            case CHOOSE_COUNTRY:{
                numberTab = CHOOSE_CITY;

                adapter.notifyItemRangeRemoved(0, this.original.size());
                original.clear();

                Country model = (Country) payload;
                initCityList(model.getId());

                break;
            }
            case CHOOSE_CITY:{
                City model = (City) payload;
                showMessage(model.getName());

                key = sb.delete(0,sb.length()).append(model.getId()).toString();
                name = sb.delete(0,sb.length()).append(model.getName()).toString();
                selected = model.isSelected();
                if(selected) hashMap.put(key,name);else hashMap.remove(key);
                okFilterClicked();

                break;
            }
            case CHOOSE_REVIEWS_RELATED_MODELS:{
                Country model = (Country) payload;
                key = sb.delete(0,sb.length()).append(model.getId()).toString();
                name = sb.delete(0,sb.length()).append(model.getName()).toString();
                selected = model.isSelected();
                if(selected) hashMap.put(key,name);else hashMap.remove(key);
                okFilterClicked();
                break;
            }
            case CHOOSE_NEWS_RELATED_MODELS:{
                Country model = (Country) payload;
                key = sb.delete(0,sb.length()).append(model.getId()).toString();
                name = sb.delete(0,sb.length()).append(model.getName()).toString();
                selected = model.isSelected();
                if(selected) hashMap.put(key,name);else hashMap.remove(key);
                okFilterClicked();

                break;
            }
            default:
                commonError(getString(R.string.not_valid_param));

        }
        if(selected) hashMap.put(key,name);else hashMap.remove(key);
        invalidateMenu();
    }
    //endregion

    //region Functions

    private void filterStringToHashMap() {
        try {
            String[] ids = filterId.split(",");
            String[] txts = filterTxt.split(",");
            int i = 0;
            for (String s : ids)
                if(!"null".equals(s)) {
                    String key = new StringBuilder().append(s).toString();
                    String value=new StringBuilder().append(txts[i++]).toString();
                    if(!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value))
                        hashMap.put(key,value);
                }
        } catch (Exception e) {
        }
        invalidateMenu();


    }

    private void invalidateMenu() {
        okButton.setVisibility(hashMap.size()==0?View.GONE:View.VISIBLE);
    }

    private void initCountryList(int filterId) {

//        switch (filterId) {
//            case FilterBreweryField.COUNTRY:
                toolbarTitle.setText(R.string.select_country);
                presenter.loadCountries();
                finder.clearFocus();
                emptyView.setVisibility(View.GONE);
                emptyTitle.setText(getString(R.string.search_country));
                showProgressBar(true);

                okButton.setVisibility(View.GONE);

//                break;
//            default:
//                break;
//        }
    }

    private void initCityList(String countryId) {

//        switch (filterId) {
//            case FilterBreweryField.COUNTRY:
        toolbarTitle.setText(R.string.select_city);
        presenter.loadCity(new GeoPackage(countryId, null));
//        presenter.loadRegions(new GeoPackage(countryId, null));
        finder.clearFocus();

        emptyView.setVisibility(View.GONE);
        showProgressBar(true);

        okButton.setVisibility(View.GONE);
//                break;
//            default:
//                break;
//        }
    }
    private void initReviewRelModelsList() {

//        switch (filterId) {
//            case FilterBreweryField.COUNTRY:
        toolbarTitle.setText(R.string.select_review_type);
        presenter.loadReviewsRelatedModels();
//        presenter.loadRegions(new GeoPackage(countryId, null));
        finder.clearFocus();
        emptyView.setVisibility(View.GONE);
        showProgressBar(true);
//                break;
//            default:
//                break;
//        }
    }
    private void initNewsRelModelsList() {

//        switch (filterId) {
//            case FilterBreweryField.COUNTRY:
        toolbarTitle.setText(R.string.select_news_type);
        presenter.loadNewsRelatedModels();
//        presenter.loadRegions(new GeoPackage(countryId, null));
        finder.clearFocus();
        emptyView.setVisibility(View.GONE);
        showProgressBar(true);
//                break;
//            default:
//                break;
//        }
    }


    private void initBreweryFilterByCategory(int filterId) {

        switch (filterId) {
            case FilterBreweryField.NAME:
                toolbarTitle.setText(R.string.search_beer_factory);
                filterList.addOnScrollListener(scrollListener);
                fullSearchPackage.setType(Keys.TYPE_BREWERY);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_brewery));
                toolbarTitle.setText(R.string.search_brewery_name);
                hashMap.clear();
                invalidateMenu();
                break;
            case FilterBreweryField.COUNTRY:
                toolbarTitle.setText(R.string.select_country);
                presenter.loadCountries();
                finder.clearFocus();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            case FilterBreweryField.BRAND:
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
                filterStringToHashMap();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            default:
                break;
        }
    }

    private void initBeerFilterByCategory(int filterId) {

        switch (filterId) {
            case FilterBeerField.CITY:
                filterStringToHashMap();
                emptyTitle.setText(getString(R.string.filter_search_city));
                toolbarTitle.setText(R.string.filter_search_city_title);
                break;

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
                toolbarTitle.setText(R.string.select_country);
                filterStringToHashMap();
                presenter.loadCountries();
                finder.clearFocus();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            case FilterBeerField.TYPE:
                toolbarTitle.setText(R.string.search_beer_type);
                filterStringToHashMap();
                presenter.loadBeerTypes(fullSearchPackage);
                finder.clearFocus();
                break;
            case FilterBeerField.PRICE_BEER:
                toolbarTitle.setText(R.string.search_beer_price);
                filterStringToHashMap();
                presenter.loadPriceRangeTypes("beer");
                finder.clearFocus();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);

                break;
            case FilterBeerField.BEER_PACK:
                toolbarTitle.setText(R.string.search_beer_bootle);
                filterStringToHashMap();
                finder.clearFocus();
                presenter.loadBeerPack();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            case FilterBeerField.BRAND:
                toolbarTitle.setText(R.string.search_beer_brand);
                filterList.addOnScrollListener(scrollListener);
                fullSearchPackage.setType(Keys.TYPE_BEERBRAND);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_brand));
                filterStringToHashMap();
                break;
            case FilterBeerField.COLOR:
                toolbarTitle.setText(R.string.search_beer_color);
                filterStringToHashMap();
                finder.clearFocus();
                presenter.loadBeerColor();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            case FilterBeerField.TASTE:
                toolbarTitle.setText(R.string.search_beer_taste);
                filterStringToHashMap();
                finder.clearFocus();
                presenter.loadBeerTaste();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            case FilterBeerField.SMELL:
                toolbarTitle.setText(R.string.search_beer_smell);
                filterStringToHashMap();
                finder.clearFocus();
                presenter.loadBeerSmell();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            case FilterBeerField.AFTER_TASTE:
                toolbarTitle.setText(R.string.search_beer_after_taste);
                filterStringToHashMap();
                finder.clearFocus();
                presenter.loadBeerAfterTaste();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            case FilterBeerField.POWER:
                toolbarTitle.setText(R.string.search_beer_power);
                filterStringToHashMap();
                presenter.loadBeerPower();
                finder.clearFocus();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            case FilterBeerField.DENSITY:
                toolbarTitle.setText(R.string.search_beer_type_broj);
                filterStringToHashMap();
                presenter.loadBeerDensity();
                finder.clearFocus();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            case FilterBeerField.IBU:
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

        switch (filterId) {
            case FilterRestoField.NAME:
                filterList.addOnScrollListener(scrollListener);
                fullSearchPackage.setType(Keys.TYPE_RESTO);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_resto));
                toolbarTitle.setText(R.string.search_resto_name);
                hashMap.clear();
                invalidateMenu();
                break;
            case FilterRestoField.TYPE:
                toolbarTitle.setText(R.string.text_resto_type);
                filterStringToHashMap();
                presenter.loadRestoTypes();
                finder.clearFocus();
                finder.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);

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
                toolbarTitle.setText(R.string.search_resto_kitchen);
                filterStringToHashMap();
                presenter.loadKitchenTypes();
                finder.clearFocus();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                finder.setVisibility(View.GONE);
                break;
            case FilterRestoField.PRICE:
                toolbarTitle.setText(R.string.search_resto_price);
                presenter.loadPriceRangeTypes("resto");
                finder.clearFocus();
                emptyView.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            case FilterRestoField.CITY:
                filterStringToHashMap();
                emptyTitle.setText(getString(R.string.filter_search_city));
                toolbarTitle.setText(R.string.filter_search_city_title);
                finder.setVisibility(View.GONE);
                break;
            case FilterRestoField.METRO:
                toolbarTitle.setText(R.string.select_metro);
                break;
            case FilterRestoField.FEATURES:
                toolbarTitle.setText(R.string.search_resto_other);
                presenter.loadFeatureTypes();
                emptyView.setVisibility(View.GONE);
                finder.setVisibility(View.GONE);
                showProgressBar(true);
                break;
            default:
                commonError(getString(R.string.not_valid_param));
        }
    }

    private void initFinder() {

//        if(getTypeFilter()==0) {
//            finder.setListener(string -> {
//                if (original != null) {
//                    adapter.setSearchText(string);
//                    adapter.filterItems(original);
//                }
//            });
//        }else {
            finder.setListener(string -> prepareQuery(string));
            finder.setCancelClickListener(v -> {
                attachPresenter();
            });
//        }
        finder.clearFocus();
    }

    private int getTypeFilter() {
        //0-local filter
        //1-remote filter
        int type_filter=0;

        switch (numberTab){
            case SearchFragment.CATEGORY_LIST_BEER:
                if(FilterBeerField.NAME == numberMenuItem)
                    type_filter=1;
                else if(FilterBeerField.BREWERY == numberMenuItem)
                    type_filter=1;
                else if(FilterBeerField.BRAND == numberMenuItem)
                    type_filter=1;
                else if(FilterBeerField.CITY == numberMenuItem)
                    type_filter=1;
                break;
            case SearchFragment.CATEGORY_LIST_BREWERY:
                if(FilterBreweryField.NAME == numberMenuItem)
                    type_filter=1;
                else if(FilterBreweryField.BRAND == numberMenuItem)
                    type_filter=1;
                break;
            case SearchFragment.CATEGORY_LIST_RESTO:
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
        Log.v("xzxz", "---DBG prepareQuery="+ stringSearch);
        fullSearchPackage.setPage(0);
        fullSearchPackage.setStringSearch(stringSearch);
        scrollListener.reset();
        this.original.clear();
        emptyView.setVisibility(View.GONE);
        filterList.setVisibility(View.GONE);
        showProgressBar(true);
        sendQuery();
    }

    private void sendQuery() {
        Log.v("xzxz", "---DBG sendQuery");
        if (fullSearchPackage.getStringSearch().length() > 0){
            if(SearchFragment.CATEGORY_LIST_RESTO.equals(numberTab)&&numberMenuItem ==FilterRestoField.CITY){
                presenter.sendQueryCitySearch(fullSearchPackage);
            }else if(SearchFragment.CATEGORY_LIST_BEER.equals(numberTab)&&numberMenuItem ==FilterBeerField.CITY){
                presenter.sendQueryCitySearch(fullSearchPackage);
            }else if(CHOOSE_COUNTRY.equals(numberTab)){
                presenter.sendQueryCountrySearch(fullSearchPackage);
            }else if(CHOOSE_CITY.equals(numberTab)){
                presenter.sendQueryCitySearch(fullSearchPackage);
            }
            else{
                presenter.sendQueryFullSearch(fullSearchPackage);
            }
        }else {
            showProgressBar(false);
        }
    }

    //endregion
}
