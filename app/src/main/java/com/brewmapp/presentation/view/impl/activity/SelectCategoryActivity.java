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
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.PriceRange;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoType;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.entity.wrapper.FilterBeerInfo;
import com.brewmapp.data.entity.wrapper.KitchenInfo;
import com.brewmapp.data.entity.wrapper.RestoTypeInfo;
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

        //region Load data and custom View
        switch (numberTab){
            case SearchFragment.TAB_BEER:
                initBeerFilterByCategory(numberMenuItem);
                break;
            case SearchFragment.TAB_BREWERY:
                initBreweryFilterByCategory(numberMenuItem);
                break;
            case SearchFragment.TAB_RESTO:
                initRestoFilterByCategory(numberMenuItem);
                break;
            default:
                commonError(getString(R.string.not_valid_param));

        }
        //endregion

        initFinder();
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

        switch (numberTab){
            case SearchFragment.TAB_BEER:
                //region Prepare append items for TAB_BEER
                switch (numberMenuItem) {
                    case FilterBeerField.NAME:
                        if (fullSearchPackage.getPage() == 0)
                            this.original.clear();
                        for (IFlexible iFlexible:list)
                            ((FilterBeerInfo)iFlexible).getModel().setSelectable(false);
                        break;
                }
                //endregion
                break;
            case SearchFragment.TAB_BREWERY:
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
                            model.setSelected(hashMap.containsKey(new StringBuilder().append(model.getId()).toString()));
                        }
                        break;
                    case FilterRestoField.KITCHEN:
                        this.original.clear();
                        for(IFlexible iFlexible:list){
                            Kitchen model= ((KitchenInfo) iFlexible).getModel();
                            model.setSelected(hashMap.containsKey(new StringBuilder().append(model.getId()).toString()));
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
        this.original.addAll(list);
        int numberStartNotificationInsert=this.original.size();
        adapter.notifyItemRangeInserted(numberStartNotificationInsert,list.size());
        //endregion
        //region Visible control
        showProgressBar(false);
        emptyView.setVisibility(numberStartNotificationInsert==0?View.VISIBLE:View.GONE);
        filterList.setVisibility(numberStartNotificationInsert==0?View.GONE:View.VISIBLE);
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

        returnIntent.putExtra("filter", selectedFilter);
        returnIntent.putExtra("category", filterCategory);
        returnIntent.putExtra("selectedItem", filterTxt);
        returnIntent.putExtra("selectedItemId", filterId);
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

                fullSearchPackage.setType(Keys.TYPE_BREWERY);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_brewery));
                filterList.setVisibility(View.GONE);
                toolbarTitle.setText(R.string.search_brewery_name);
                break;
            case FilterBreweryField.COUNTRY:
                showProgressBar(true);

                toolbarTitle.setText(R.string.select_country);
                    presenter.loadCountries();
                break;
            case FilterBreweryField.BRAND:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_brand);
                    presenter.loadBeerBrand(fullSearchPackage);
                break;
            case FilterBreweryField.TYPE_BEER:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_type);
                    presenter.loadBeerTypes(fullSearchPackage);
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
                    presenter.loadCountries();
                break;
            case FilterBeerField.TYPE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_type);
                    presenter.loadBeerTypes(fullSearchPackage);
                break;
            case FilterBeerField.PRICE_BEER:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_price);
                    presenter.loadPriceRangeTypes("beer");
                break;
            case FilterBeerField.BEER_PACK:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_bootle);
                    presenter.loadBeerPack();
                break;
            case FilterBeerField.BRAND:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_brand);
                    presenter.loadBeerBrand(fullSearchPackage);
                break;
            case FilterBeerField.COLOR:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_color);
                    presenter.loadBeerColor();
                break;
            case FilterBeerField.TASTE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_taste);
                    presenter.loadBeerTaste();
                break;
            case FilterBeerField.SMELL:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_smell);
                    presenter.loadBeerSmell();
                break;
            case FilterBeerField.AFTER_TASTE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_after_taste);
                    presenter.loadBeerAfterTaste();
                break;
            case FilterBeerField.POWER:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_power);
                    presenter.loadBeerPower();
                break;
            case FilterBeerField.DENSITY:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_type_broj);
                    presenter.loadBeerDensity();
                break;
            case FilterBeerField.IBU:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_ibu);
                    presenter.loadBeerIbu();
                break;
            case FilterBeerField.BREWERY:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_factory);
                    presenter.loadBrewery();
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
                break;
            case SearchFragment.TAB_BREWERY:
                if(FilterBreweryField.NAME == numberMenuItem)
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

        switch (numberTab){
            case SearchFragment.TAB_BEER:
                //region select item for TAB_BEER
                switch (numberMenuItem) {
                    case FilterRestoField.NAME:
                        Starter.BeerDetailActivity(this, String.valueOf(((Beer) payload).getId()));
                        break;
                    default: {
                        commonError(getString(R.string.not_valid_param));
                        return;
                    }
                }
                //endregion
                break;
            case SearchFragment.TAB_BREWERY:
                break;
            case SearchFragment.TAB_RESTO:
                boolean selected=false;
                //region select item for TAB_RESTO
                switch (numberMenuItem){
                    case FilterRestoField.NAME:
                        Starter.RestoDetailActivity(this,String.valueOf(((Resto)payload).getId()));
                        break;
                    case FilterRestoField.TYPE: {
                        RestoType model = (RestoType) payload;
                        key = new StringBuilder().append(model.getId()).toString();
                        name = new StringBuilder().append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterRestoField.BEER: {
                        Beer model = (Beer) payload;
                        key = new StringBuilder().append(model.getId()).toString();
                        name = new StringBuilder().append(model.getTitle()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterRestoField.KITCHEN: {
                        Kitchen model = (Kitchen) payload;
                        key = new StringBuilder().append(model.getId()).toString();
                        name = new StringBuilder().append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterRestoField.PRICE: {
                        PriceRange model = (PriceRange) payload;
                        key = new StringBuilder().append(model.getId()).toString();
                        name = new StringBuilder().append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                    case FilterRestoField.CITY: {
                        City model = (City) payload;
                        key = new StringBuilder().append(model.getId()).toString();
                        name = new StringBuilder().append(model.getName()).toString();
                        selected = model.isSelected();
                    }break;
                        default: {
                            commonError(getString(R.string.not_valid_param));
                            return;
                        }
                }
                if(selected) hashMap.put(key,name);else hashMap.remove(key);
                //endregion
                break;
            default:
                commonError(getString(R.string.not_valid_param));

        }
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
