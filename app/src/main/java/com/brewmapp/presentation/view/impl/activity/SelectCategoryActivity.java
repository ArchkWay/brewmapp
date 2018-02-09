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
import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoType;
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
import io.paperdb.Paper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;
import ru.frosteye.ovsa.tool.UITools;

import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

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
    private FullSearchPackage fullSearchPackage;

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
        numberTab =getIntent().getIntExtra(Actions.PARAM1,Integer.MAX_VALUE);
        numberMenuItem =getIntent().getIntExtra(Actions.PARAM2,Integer.MAX_VALUE);
        filterId=getIntent().getStringExtra(Actions.PARAM3);
        filterTxt=getIntent().getStringExtra(Actions.PARAM4);
        if(numberTab ==SearchFragment.TAB_RESTO&&(numberMenuItem==FilterRestoField.BEER||numberMenuItem==FilterRestoField.NAME)) {
            hashMap.clear();
        }else {
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
        }

        okButton.setVisibility(hashMap.size()==0?View.GONE:View.VISIBLE);

        fullSearchPackage = new FullSearchPackage();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                fullSearchPackage.setPage(currentPage-1);
                if(getTypeFiltr()==1) sendQuery();
            }
        };
        filterList.addOnScrollListener(scrollListener);
        filterList.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        filterList.setLayoutManager(manager);

        adapter = new FlexibleModelAdapter<>(original, this::processAction);
        filterList.setAdapter(adapter);

        filterList.setOnTouchListener((view, motionEvent) -> {
            UITools.hideKeyboard(this);
            return false;
        });

        initFilterCategory();
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
                break;
            case SearchFragment.TAB_BREWERY:
                break;
            case SearchFragment.TAB_RESTO:
                switch (numberMenuItem){
                    case FilterRestoField.NAME:
                        break;
                    case FilterRestoField.TYPE:
                        for(IFlexible iFlexible:list){
                            RestoType model= ((RestoTypeInfo) iFlexible).getModel();
                            model.setSelected(hashMap.containsKey(new StringBuilder().append(model.getId()).toString()));
                        }
                        break;
                    case FilterRestoField.KITCHEN:
                        for(IFlexible iFlexible:list){
                            Kitchen model= ((KitchenInfo) iFlexible).getModel();
                            model.setSelected(hashMap.containsKey(new StringBuilder().append(model.getId()).toString()));
                        }
                        break;

                }
                break;
        }



        showProgressBar(false);

        this.original.addAll(list);
        int numberStartNotificationInsert=this.original.size();
        adapter.notifyItemRangeInserted(numberStartNotificationInsert,list.size());
        emptyView.setVisibility(numberStartNotificationInsert==0?View.VISIBLE:View.GONE);
        filterList.setVisibility(numberStartNotificationInsert==0?View.GONE:View.VISIBLE);

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
        switch (numberTab){
            case SearchFragment.TAB_BEER:
                beerFilterLogic();
                break;
            case SearchFragment.TAB_BREWERY:
                breweryLogic();
                break;
            case SearchFragment.TAB_RESTO:
                restoFilterLogic();
                break;
        }

        goToFilterMap();
    }

    @OnClick(R.id.filter_toolbar_cancel)
    public void cancelFilterClicked() {
        onBackPressed();
    }

//*********************************************

    private List<IFlexible> getStoredFilterList(String filterKey) {
        return Paper.book().read(filterKey);
    }

    private void saveStoredFilter(String filterKey) {
        if (original != null) {
            Paper.book().write(filterKey, original);
        }
    }

    private void initFilterCategory() {

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
                if (getStoredFilterList(FilterKeys.COUNTRY) != null) {
                    appendItems(getStoredFilterList(FilterKeys.COUNTRY));
                } else {
                    presenter.loadCountries();
                }
                break;
            case FilterBreweryField.BRAND:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_brand);
                if (getStoredFilterList(FilterKeys.BEER_BRAND) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_BRAND));
                } else {
                    presenter.loadBeerBrand(fullSearchPackage);
                }
                break;
            case FilterBreweryField.TYPE_BEER:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_type);
                if (getStoredFilterList(FilterKeys.BEER_TYPES) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_TYPES));
                } else {
                    presenter.loadBeerTypes(fullSearchPackage);
                }
                break;
            default:
                break;
        }
    }

    private void initBeerFilterByCategory(int filterId) {
        this.filterCategory = filterId;
        switch (filterId) {
            case FilterBeerField.NAME:
                showProgressBar(true);
                fullSearchPackage.setType(Keys.TYPE_BEER);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_beer));
                filterList.setVisibility(View.GONE);
                toolbarTitle.setText(R.string.search_beer_name);
                break;
            case FilterBeerField.COUNTRY:
                showProgressBar(true);
                toolbarTitle.setText(R.string.select_country);
                if (getStoredFilterList(FilterKeys.COUNTRY) != null) {
                    appendItems(getStoredFilterList(FilterKeys.COUNTRY));
                } else {
                    presenter.loadCountries();
                }
                break;
            case FilterBeerField.TYPE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_type);
                if (getStoredFilterList(FilterKeys.BEER_TYPES) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_TYPES));
                } else {
                    presenter.loadBeerTypes(fullSearchPackage);
                }
                break;
            case FilterBeerField.PRICE_BEER:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_price);
                if (getStoredFilterList(FilterKeys.PRICE_BEER) != null) {
                    appendItems(getStoredFilterList(FilterKeys.PRICE_BEER));
                } else {
                    presenter.loadPriceRangeTypes("beer");
                }
                break;
            case FilterBeerField.BEER_PACK:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_bootle);
                if (getStoredFilterList(FilterKeys.BEER_PACK) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_PACK));
                } else {
                    presenter.loadBeerPack();
                }
                break;
            case FilterBeerField.BRAND:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_brand);
                if (getStoredFilterList(FilterKeys.BEER_BRAND) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_BRAND));
                } else {
                    presenter.loadBeerBrand(fullSearchPackage);
                }
                break;
            case FilterBeerField.COLOR:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_color);
                if (getStoredFilterList(FilterKeys.BEER_COLOR) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_COLOR));
                } else {
                    presenter.loadBeerColor();
                }
                break;
            case FilterBeerField.TASTE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_taste);
                if (getStoredFilterList(FilterKeys.BEER_TASTE) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_TASTE));
                } else {
                    presenter.loadBeerTaste();
                }
                break;
            case FilterBeerField.SMELL:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_smell);
                if (getStoredFilterList(FilterKeys.BEER_SMELL) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_SMELL));
                } else {
                    presenter.loadBeerSmell();
                }
                break;
            case FilterBeerField.AFTER_TASTE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_after_taste);
                if (getStoredFilterList(FilterKeys.BEER_AFTER_TASTE) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_AFTER_TASTE));
                } else {
                    presenter.loadBeerAfterTaste();
                }
                break;
            case FilterBeerField.POWER:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_power);
                if (getStoredFilterList(FilterKeys.BEER_POWER) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_POWER));
                } else {
                    presenter.loadBeerPower();
                }
                break;
            case FilterBeerField.DENSITY:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_type_broj);
                if (getStoredFilterList(FilterKeys.BEER_DENSITY) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_DENSITY));
                } else {
                    presenter.loadBeerDensity();
                }
                break;
            case FilterBeerField.IBU:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_ibu);
                if (getStoredFilterList(FilterKeys.BEER_IBU) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_IBU));
                } else {
                    presenter.loadBeerIbu();
                }
                break;
            case FilterBeerField.BREWERY:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_beer_factory);
                if (getStoredFilterList(FilterKeys.BEER_BREWERIES) != null) {
                    appendItems(getStoredFilterList(FilterKeys.BEER_BREWERIES));
                } else {
                    presenter.loadBrewery();
                }
                break;
            default:commonError(getString(R.string.not_valid_param));
        }
    }

    private void initRestoFilterByCategory(int filterId) {
        this.filterCategory = filterId;
        switch (filterId) {
            case FilterRestoField.NAME:
                fullSearchPackage.setType(Keys.TYPE_RESTO);
                emptyView.setVisibility(View.GONE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_resto));
                toolbarTitle.setText(R.string.search_resto_name);
                break;
            case FilterRestoField.TYPE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_resto_type);
                presenter.loadRestoTypes();
                finder.clearFocus();
                break;
            case FilterRestoField.BEER:
                fullSearchPackage.setType(Keys.TYPE_BEER);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_beer));
                toolbarTitle.setText(R.string.search_beer_name);
                break;
            case FilterRestoField.KITCHEN:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_resto_kitchen);
                presenter.loadKitchenTypes();
                finder.clearFocus();
                break;
            case FilterRestoField.PRICE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_resto_price);
                if (getStoredFilterList(FilterKeys.PRICE_RANGE) != null) {
                    appendItems(getStoredFilterList(FilterKeys.PRICE_RANGE));
                } else {
                    presenter.loadPriceRangeTypes("resto");
                }
                break;
            case FilterRestoField.CITY:
                break;
            case FilterRestoField.METRO:
                toolbarTitle.setText(R.string.select_metro);
                break;
            case FilterRestoField.FEATURES:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_resto_other);
                if (getStoredFilterList(FilterKeys.FEATURES) != null) {
                    appendItems(getStoredFilterList(FilterKeys.FEATURES));
                } else {
                    presenter.loadFeatureTypes();
                }
                break;
            default:
                commonError(getString(R.string.not_valid_param));
        }
    }

    private void breweryLogic() {
        if (filterCategory == FilterBreweryField.TYPE_BEER) {
            selectedFilter = FilterKeys.BREWERY_BEER_TYPE;
            saveStoredFilter(FilterKeys.BREWERY_BEER_TYPE);
        } else if (filterCategory == FilterBreweryField.BRAND) {
            selectedFilter = FilterKeys.BREWERY_BEER_BRAND;
            saveStoredFilter(FilterKeys.BREWERY_BEER_BRAND);
        } else if (filterCategory == FilterBreweryField.COUNTRY) {
            selectedFilter = FilterKeys.BREWERY_BEER_COUNTRY;
            saveStoredFilter(FilterKeys.BREWERY_BEER_COUNTRY);
        }
    }

    private void beerFilterLogic() {
        selectedFilter=null;
        if (filterCategory == FilterBeerField.PRICE_BEER) {
            selectedFilter = FilterKeys.PRICE_BEER;
        } else if (filterCategory == FilterBeerField.BEER_PACK) {
            selectedFilter = FilterKeys.BEER_PACK;
        } else if (filterCategory == FilterBeerField.TYPE) {
            selectedFilter = FilterKeys.BEER_TYPES;
        } else if (filterCategory == FilterBeerField.BRAND) {
            selectedFilter = FilterKeys.BEER_BRAND;
        } else if (filterCategory == FilterBeerField.COLOR) {
            selectedFilter = FilterKeys.BEER_COLOR;
        } else if (filterCategory == FilterBeerField.TASTE) {
            selectedFilter = FilterKeys.BEER_TASTE;
        } else if (filterCategory == FilterBeerField.SMELL) {
            selectedFilter = FilterKeys.BEER_SMELL;
        } else if (filterCategory == FilterBeerField.AFTER_TASTE) {
            selectedFilter = FilterKeys.BEER_AFTER_TASTE;
        } else if (filterCategory == FilterBeerField.POWER) {
            selectedFilter = FilterKeys.BEER_POWER;
        } else if (filterCategory == FilterBeerField.DENSITY) {
            selectedFilter = FilterKeys.BEER_DENSITY;
        }  else if (filterCategory == FilterBeerField.IBU) {
            selectedFilter = FilterKeys.BEER_IBU;
        } else if (filterCategory == FilterBeerField.COUNTRY) {
            selectedFilter = FilterKeys.COUNTRY;
        } else if (filterCategory == FilterBeerField.BREWERY) {
            selectedFilter = FilterKeys.BEER_BREWERIES;
        }
        if(selectedFilter!=null)
            saveStoredFilter(selectedFilter);
    }

    private void restoFilterLogic() {
        if (filterCategory == FilterRestoField.TYPE) {
            selectedFilter = FilterKeys.RESTO_TYPE;
            saveStoredFilter(FilterKeys.RESTO_TYPE);
        } else if (filterCategory == FilterRestoField.KITCHEN) {
            selectedFilter = FilterKeys.KITCHEN;
            saveStoredFilter(FilterKeys.KITCHEN);
        } else if (filterCategory == FilterRestoField.BEER) {
            selectedFilter = FilterKeys.BEER;
            saveStoredFilter(FilterKeys.BEER);
        } else if (filterCategory == FilterRestoField.PRICE) {
            selectedFilter = FilterKeys.PRICE_RANGE;
            saveStoredFilter(FilterKeys.PRICE_RANGE);
        } else if (filterCategory == FilterRestoField.CITY) {
            selectedFilter = FilterKeys.COUNTRY;
            saveStoredFilter(FilterKeys.COUNTRY);
        } else if (filterCategory == FilterRestoField.METRO) {
            selectedFilter = FilterKeys.METRO;
            saveStoredFilter(FilterKeys.METRO);
        } else if (filterCategory == FilterRestoField.FEATURES) {
            selectedFilter = FilterKeys.FEATURES;
            saveStoredFilter(FilterKeys.FEATURES);
        }
    }

    private void goToFilterMap() {
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

    private void initFinder() {

        if(getTypeFiltr()==0) {
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

    private int getTypeFiltr() {
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
        this.original.clear();
        adapter.notifyDataSetChanged();
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
        switch (numberTab){
            case SearchFragment.TAB_BEER:
                break;
            case SearchFragment.TAB_BREWERY:
                break;
            case SearchFragment.TAB_RESTO:
                String key=null;
                String name=null;
                boolean selected=false;
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
                        default: {
                            commonError(getString(R.string.not_valid_param));
                            return;
                        }
                }
                if(selected) hashMap.put(key,name);else hashMap.remove(key);
                break;
            default:
                commonError(getString(R.string.not_valid_param));

        }
        okButton.setVisibility(hashMap.size()==0?View.GONE:View.VISIBLE);

//        switch (fullSearchPackage.getType()) {
//            case Keys.TYPE_BREWERY:{
//                Brewery brewery= (Brewery) payload;
//                Starter.BreweryDetailsActivity(this,brewery.getId());
//            }break;
//            default: {
//                switch (action){
//                    case FilterRestoField.NAME:
//                        Resto resto = (Resto) payload;
//                        goToRestoDetails(String.valueOf(resto.getId()));
//                        break;
//                    case FilterRestoField.BEER:
//                        Beer beer = (Beer) payload;
//                        goToBeerDetails(beer.getId());
//                        break;
//                    case FilterRestoField.CITY:
//                        Country country = (Country) payload;
//                        if (country.getId() == null) {
//                            filterTxt = country.getName();
//                            filterId = country.getId();
//                            goToFilterMap();
//                            return;
//                        }
//                        toolbarTitle.setText(R.string.select_region);
//                        if (getStoredFilterList(FilterKeys.REGION) != null) {
//                            appendItems(getStoredFilterList(FilterKeys.REGION));
//                        } else {
//                            showProgressBar(true);
//                            presenter.loadRegions(new GeoPackage(country.getId(), null));
//                        }
//                        break;
//                    case FilterRestoField.REGION:
//                        if (getStoredFilterList(FilterKeys.REGION) == null) {
//                            saveStoredFilter(FilterKeys.REGION);
//                        }
//                        Region region = (Region) payload;
//                        if (region.getId() == 1 || region.getId() == 2) {
//                            filterTxt = region.getName();
//                            filterId = String.valueOf(region.getId());
//                            goToFilterMap();
//                            return;
//                        }
//                        toolbarTitle.setText(R.string.select_city);
//                        if (getStoredFilterList(FilterKeys.CITY) != null) {
//                            appendItems(getStoredFilterList(FilterKeys.CITY));
//                        } else {
//                            showProgressBar(true);
//                            presenter.loadCity(new GeoPackage(region.getCountryId(), String.valueOf(region.getId())));
//                        }
//                        break;
//                    case FilterRestoField.SELECTED_CITY:
//                        City city = (City) payload;
//                        filterTxt = city.getName();
//                        filterId = String.valueOf(city.getId());
//                        goToFilterMap();
//                        break;
//                    case FilterRestoField.METRO:
//                        //TO DO
//                        break;
//                    default:break;
//                }
//            }break;
//        }
    }

    private void goToBeerDetails(String beerId) {
        Beer beer = new Beer();
        beer.setId(beerId);
        Intent intent = new Intent(this, BeerDetailActivity.class);
        intent.putExtra(getString(R.string.key_serializable_extra), new Interest(beer));
        startActivity(intent);
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
