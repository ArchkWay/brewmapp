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
import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.Country;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.Region;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.data.pojo.ScrollPackage;
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
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;
import ru.frosteye.ovsa.tool.UITools;

import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

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
    @BindView(R.id.lyt_empty_view)
    LinearLayout emptyView;
    @BindView(R.id.empty_title)
    TextView emptyTitle;
    @BindView(R.id.view_finder_input)
    AutoCompleteTextView input;
    @BindView(R.id.lytProgressBar)
    LinearLayout lytProgressBar;

    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage fullSearchPackage;
    private String selectedItem;
    private String selectedItemId;
    private List<IFlexible> original;
    private int filterCategory;
    private String selectedFilter = null;
    private boolean isBeer;

    private EndlessRecyclerOnScrollListener scrollListener;

    @Inject
    FilterByCategoryPresenter presenter;

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
        Paper.init(this);
        fullSearchPackage = new FullSearchPackage();
            LinearLayoutManager manager = new LinearLayoutManager(this);
            scrollListener = new EndlessRecyclerOnScrollListener(manager) {
                @Override
                public void onLoadMore(int currentPage) {
                    fullSearchPackage.setPage(currentPage - 1);
            }
        };
        list.addOnScrollListener(scrollListener);
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        list.setLayoutManager(manager);
        adapter = new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        list.setAdapter(adapter);
        list.setOnTouchListener((view, motionEvent) -> {
            UITools.hideKeyboard(this);
            return false;
        });

        isBeer = getIntent().getIntExtra(Keys.BEER_TYPES, 0) == 1;
        if (!isBeer) {
            initRestoFilterByCategory(getIntent().getIntExtra(Keys.FILTER_CATEGORY, 0));
        } else {
            initBeerFilterByCategory(getIntent().getIntExtra(Keys.FILTER_CATEGORY, 0));
        }
        initFilter();
    }

    private void initBeerFilterByCategory(int filterId) {
        this.filterCategory = filterId;
        switch (filterId) {
            case FilterBeerField.NAME:
                showProgressBar(true);
                okButton.setVisibility(View.GONE);
                fullSearchPackage.setType(Keys.TYPE_BEER);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_beer));
                list.setVisibility(View.GONE);
                toolbarTitle.setText(R.string.search_beer_name);
                break;
            case FilterBeerField.COUNTRY:
                showProgressBar(true);
                okButton.setVisibility(View.GONE);
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
                    presenter.loadBeerTypes();
                }
                break;
            case FilterBeerField.PLACE:
                showProgressBar(true);
                okButton.setVisibility(View.GONE);
                toolbarTitle.setText(R.string.select_country);
                if (getStoredFilterList(FilterKeys.COUNTRY) != null) {
                    appendItems(getStoredFilterList(FilterKeys.COUNTRY));
                } else {
                    presenter.loadCountries();
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
                default:break;
        }
    }

    private void initRestoFilterByCategory(int filterId) {
        this.filterCategory = filterId;
        switch (filterId) {
            case FilterRestoField.NAME:
                okButton.setVisibility(View.GONE);
                fullSearchPackage.setType(Keys.TYPE_RESTO);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_resto));
                list.setVisibility(View.GONE);
                toolbarTitle.setText(R.string.search_resto_name);
                break;
            case FilterRestoField.TYPE:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_resto_type);
                if (getStoredFilterList(FilterKeys.RESTO_TYPE) != null) {
                    appendItems(getStoredFilterList(FilterKeys.RESTO_TYPE));
                } else {
                    presenter.loadRestoTypes();
                }
                break;
            case FilterRestoField.BEER:
                okButton.setVisibility(View.GONE);
                fullSearchPackage.setType(Keys.TYPE_BEER);
                emptyView.setVisibility(View.VISIBLE);
                emptyTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                emptyTitle.setText(getString(R.string.filter_search_beer));
                list.setVisibility(View.GONE);
                toolbarTitle.setText(R.string.search_beer_name);
                break;
            case FilterRestoField.KITCHEN:
                showProgressBar(true);
                toolbarTitle.setText(R.string.search_resto_kitchen);
                if (getStoredFilterList(FilterKeys.KITCHEN) != null) {
                    appendItems(getStoredFilterList(FilterKeys.KITCHEN));
                } else {
                    presenter.loadKitchenTypes();
                }
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
                showProgressBar(true);
                okButton.setVisibility(View.GONE);
                toolbarTitle.setText(R.string.select_country);
                if (getStoredFilterList(FilterKeys.COUNTRY) != null) {
                    appendItems(getStoredFilterList(FilterKeys.COUNTRY));
                } else {
                    presenter.loadCountries();
                }
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
        showProgressBar(false);
        this.original = list;
        adapter.clear();
        adapter.updateDataSet(list);
    }

    @Override
    public void showProgressBar(boolean show) {
        lytProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void initFilter() {
        if (fullSearchPackage.getType() != null) {
            finder.setListener(string -> prepareQuery(string));
        } else {
            finder.setListener(string -> {
                if (original != null) {
                    adapter.setSearchText(string);
                    adapter.filterItems(original);
                }
            });
        }
    }

    private void prepareQuery(String stringSearch) {
        fullSearchPackage.setPage(0);
        fullSearchPackage.setStringSearch(stringSearch);
        if (stringSearch.length() > 3) {
            emptyView.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            sendQuery();
        } else if (stringSearch.length() == 0) {
            adapter.clear();
            emptyView.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }
    }

    private void sendQuery() {
        if (fullSearchPackage.getStringSearch().length() == 0){
            fullSearchPackage.setPage(0);
            appendItems(new ArrayList<>());
        } else {
            lytProgressBar.setVisibility(View.VISIBLE);
            presenter.sendQueryFullSearch(fullSearchPackage);
        }
    }

    private void processAction(int action, Object payload) {
        switch (action){
            case FilterRestoField.NAME:
                Resto resto = (Resto) payload;
                goToRestoDetails(String.valueOf(resto.getId()));
                break;
            case FilterRestoField.BEER:
                Beer beer = (Beer) payload;
                goToBeerDetails(beer.getId());
                break;
            case FilterRestoField.CITY:
                if (getStoredFilterList(FilterKeys.COUNTRY) == null) {
                   saveStoredFilter(FilterKeys.COUNTRY);
                }
                Country country = (Country) payload;
                if (isBeer && filterCategory != FilterBeerField.PLACE) {
                    selectedItem = country.getName();
                    selectedItemId = country.getId();
                    goToFilterMap();
                    return;
                }

                toolbarTitle.setText(R.string.select_region);
                if (getStoredFilterList(FilterKeys.REGION) != null) {
                    appendItems(getStoredFilterList(FilterKeys.REGION));
                } else {
                    showProgressBar(true);
                    presenter.loadRegions(new GeoPackage(country.getId(), null));
                }
                break;
            case FilterRestoField.REGION:
                if (getStoredFilterList(FilterKeys.REGION) == null) {
                    saveStoredFilter(FilterKeys.REGION);
                }
                Region region = (Region) payload;
                if (region.getId() == 1 || region.getId() == 2) {
                    selectedItem = region.getName();
                    selectedItemId = String.valueOf(region.getId());
                    goToFilterMap();
                    return;
                }
                toolbarTitle.setText(R.string.select_city);
                if (getStoredFilterList(FilterKeys.CITY) != null) {
                    appendItems(getStoredFilterList(FilterKeys.CITY));
                } else {
                    showProgressBar(true);
                    presenter.loadCity(new GeoPackage(region.getCountryId(), String.valueOf(region.getId())));
                }
                break;
            case FilterRestoField.SELECTED_CITY:
                City city = (City) payload;
                selectedItem = city.getName();
                selectedItemId = String.valueOf(city.getId());
                goToFilterMap();
                break;
            case FilterRestoField.METRO:
                //TO DO
                break;
            default:break;
        }
    }

    private void goToRestoDetails(String restoId) {
        Interest interest = new Interest();
        Interest_info interest_info = new Interest_info();
        interest_info.setId(restoId);
        interest.setInterest_info(interest_info);
        Intent intent = new Intent(this, RestoDetailActivity.class);
        intent.putExtra(RESTO_ID, interest);
        startActivity(intent);
    }

    private void goToBeerDetails(String beerId) {
        Beer beer = new Beer();
        beer.setId(beerId);
        Intent intent = new Intent(this, BeerDetailActivity.class);
        intent.putExtra(getString(R.string.key_serializable_extra), new Interest(beer));
        startActivity(intent);
    }

    @OnClick(R.id.filter_toolbar_subtitle)
    public void okFilterClicked() {
        if (!isBeer) {
            restoFilterLogic();
        } else {
            beerFilterLogic();
        }
        goToFilterMap();
    }

    private void beerFilterLogic() {
        if (filterCategory == FilterBeerField.PRICE_BEER) {
            selectedFilter = FilterKeys.PRICE_BEER;
            saveStoredFilter(FilterKeys.PRICE_BEER);
        } else if (filterCategory == FilterBeerField.BEER_PACK) {
            selectedFilter = FilterKeys.BEER_PACK;
            saveStoredFilter(FilterKeys.BEER_PACK);
        } else if (filterCategory == FilterBeerField.TYPE) {
            selectedFilter = FilterKeys.BEER_TYPES;
            saveStoredFilter(FilterKeys.BEER_TYPES);
        } else if (filterCategory == FilterBeerField.BRAND) {
            selectedFilter = FilterKeys.BEER_BRAND;
            saveStoredFilter(FilterKeys.BEER_BRAND);
        } else if (filterCategory == FilterBeerField.COLOR) {
            selectedFilter = FilterKeys.BEER_COLOR;
            saveStoredFilter(FilterKeys.BEER_COLOR);
        } else if (filterCategory == FilterBeerField.TASTE) {
            selectedFilter = FilterKeys.BEER_TASTE;
            saveStoredFilter(FilterKeys.BEER_TASTE);
        } else if (filterCategory == FilterBeerField.SMELL) {
            selectedFilter = FilterKeys.BEER_SMELL;
            saveStoredFilter(FilterKeys.BEER_SMELL);
        } else if (filterCategory == FilterBeerField.AFTER_TASTE) {
            selectedFilter = FilterKeys.BEER_AFTER_TASTE;
            saveStoredFilter(FilterKeys.BEER_AFTER_TASTE);
        } else if (filterCategory == FilterBeerField.POWER) {
            selectedFilter = FilterKeys.BEER_POWER;
            saveStoredFilter(FilterKeys.BEER_POWER);
        } else if (filterCategory == FilterBeerField.DENSITY) {
            selectedFilter = FilterKeys.BEER_DENSITY;
            saveStoredFilter(FilterKeys.BEER_DENSITY);
        }  else if (filterCategory == FilterBeerField.IBU) {
            selectedFilter = FilterKeys.BEER_IBU;
            saveStoredFilter(FilterKeys.BEER_IBU);
        } else if (filterCategory == FilterBeerField.PLACE) {
            selectedFilter = FilterKeys.COUNTRY;
            saveStoredFilter(FilterKeys.COUNTRY);
        }
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
        if (original != null) {
            Paper.book().write(filterKey, original);
        }
    }
}
