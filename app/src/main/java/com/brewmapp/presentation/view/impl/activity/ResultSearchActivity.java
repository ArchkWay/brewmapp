package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Brewery;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.MenuField;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.SearchBeer;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.ResultSearchActivityPresenter;
import com.brewmapp.presentation.view.contract.ResultSearchActivityView;


import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;
import com.brewmapp.presentation.view.impl.widget.FinderView;

import java.util.ArrayList;
import java.util.List;

import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

public class ResultSearchActivity extends BaseActivity implements ResultSearchActivityView {

    @BindView(R.id.filter_category_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_search_list) RecyclerView list;
    @BindView(R.id.activity_search_search) FinderView finder;
    @BindView(R.id.activity_search_more) Button more;
    @BindView(R.id.title_toolbar)    TextView titleToolbar;
    @BindView(R.id.activity_search_tv_not_found)    TextView tv_not_found;
    @BindView(R.id.activity_search_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.activity_search_progress)    RelativeLayout progress;

    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage searchPackage = new FullSearchPackage();
    private int selectedTab;
    private EndlessRecyclerOnScrollListener scrollListener;
    private ProgressDialog dialog;
    private String[] titleContent = ResourceHelper.getResources().getStringArray(R.array.full_search);
    private List<IFlexible> listAdapter=new ArrayList<>();

    @Inject
    ResultSearchActivityPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void enableControls(boolean enabled, int code) {
    }

    @Override
    protected void initView() {
        enableBackButton();

        //region Parse Intent
        selectedTab=getIntent().getIntExtra(Actions.PARAM1,Integer.MAX_VALUE);
        switch (selectedTab) {
            //region TAB_RESTO
            case SearchFragment.TAB_RESTO: {
                List<FilterRestoField> restoFilterList = Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO);
                for (FilterRestoField filterRestoField:restoFilterList){
                    if(filterRestoField.getSelectedItemId()!=null){
                        switch (filterRestoField.getId()){
                            case FilterRestoField.TYPE:{
                                searchPackage.setType(filterRestoField.getSelectedItemId());
                            }break;
                            case FilterRestoField.CITY:{
                                searchPackage.setCity(filterRestoField.getSelectedItemId());
                            }break;
                            case FilterRestoField.BEER:{
                                searchPackage.setBeer(filterRestoField.getSelectedItemId());
                            }break;
                            case FilterRestoField.KITCHEN:{
                                searchPackage.setKitchen(filterRestoField.getSelectedItemId());
                            }break;
                            case FilterRestoField.PRICE:{
                                searchPackage.setPrice(filterRestoField.getSelectedItemId());
                            }break;
                        }
                    }
                }
            }break;
            //endregion

            //region TAB_BEER
            case SearchFragment.TAB_BEER: {
                List<FilterBeerField> filterBeerFieldList = Paper.book().read(SearchFragment.CATEGORY_LIST_BEER);
                for(FilterBeerField filterBeerField:filterBeerFieldList)
                    if(filterBeerField.getSelectedItemId()!=null){
                        switch (filterBeerField.getId()){
                            case FilterBeerField.COUNTRY:
                                searchPackage.setCountry(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.TYPE:
                                searchPackage.setType(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.BRAND:
                                searchPackage.setBeerBrand(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.POWER:
                                searchPackage.setPower(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.BREWERY:
                                searchPackage.setBrewery(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.DENSITY:
                                searchPackage.setDensity(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.BEER_FILTER:
                                searchPackage.setFitredBeer(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.BEER_PACK:
                                searchPackage.setPack(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.COLOR:
                                searchPackage.setColor(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.SMELL:
                                searchPackage.setBeerFragrance(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.TASTE:
                                searchPackage.setTaste(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.AFTER_TASTE:
                                searchPackage.setAfterTaste(filterBeerField.getSelectedItemId());break;
                            case FilterBeerField.PRICE_BEER:
                                searchPackage.setPrice(filterBeerField.getSelectedItemId());break;
                        }
                    }
            }break;
            //endregion

            //region TAB_BREWERY
            case SearchFragment.TAB_BREWERY: {
                List<FilterBreweryField> filterBreweryFields = Paper.book().read(SearchFragment.CATEGORY_LIST_BREWERY);
                for(FilterBreweryField filterBreweryField:filterBreweryFields){
                    switch (filterBreweryField.getId()){
                        case FilterBreweryField.COUNTRY:
                            searchPackage.setCountry(filterBreweryField.getSelectedItemId());
                            break;
                        case FilterBreweryField.BRAND:
                            searchPackage.setBeerBrand(filterBreweryField.getSelectedItemId());
                            break;
                        case FilterBreweryField.TYPE_BEER:
                            searchPackage.setType(filterBreweryField.getSelectedItemId());
                            break;
                    }
                }
            }break;
            //endregion
            //region DEFAULT
            default:
                    commonError(getString(R.string.not_valid_param));
            //endregion
        }
        //endregion

        //region setup View
        titleToolbar.setText(titleContent[selectedTab]);
        more.setOnClickListener(v -> startActivity(ExtendedSearchActivity.class));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                searchPackage.setPage(currentPage-1);
                loadResult(searchPackage);
            }
        };
        list.addOnScrollListener(scrollListener);
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        list.setLayoutManager(manager);
        adapter = new FlexibleModelAdapter<>(listAdapter, this::processAction);
        list.setAdapter(adapter);
        finder.setVisibility(View.GONE);
        //endregion

        //region StartLoad
        searchPackage.setPage(0);
        loadResult(searchPackage);
        //endregion
    }

    private void loadResult(FullSearchPackage searchPackage) {
        if(searchPackage.getPage()==0) {
            progress.setVisibility(View.VISIBLE);
            swipe.setVisibility(View.GONE);
        }else {
            swipe.setVisibility(View.VISIBLE);
            swipe.setEnabled(true);
            swipe.setRefreshing(true);
        }
        switch (selectedTab) {
            case SearchFragment.TAB_RESTO:
                presenter.loadRestoList(0, searchPackage);
                break;
            case SearchFragment.TAB_BEER:
                presenter.loadBeerList( searchPackage);
                break;
            case SearchFragment.TAB_BREWERY:
                presenter.loadBrewery(searchPackage);
                break;
            default: break;
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
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public void showDialogProgressBar(int message) {
        dialog = ProgressDialog.show(this, getString(R.string.loading),
                getString(message), true, false);
    }

    @Override
    public void hideProgressBar() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @Override
    public void commonError(String... strings) {
        if(strings!=null && strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();
    }

    private void processAction(int action, Object payload) {
        switch (selectedTab) {
            case SearchFragment.TAB_RESTO:
                Starter.RestoDetailActivity(this,String.valueOf(((Resto) payload).getId()));
                break;
            case SearchFragment.TAB_BEER:
                Starter.BeerDetailActivity(this,((SearchBeer)payload).getId());
                break;
            case SearchFragment.TAB_BREWERY:
                Starter.BreweryDetailsActivity(this,((Brewery)payload).getId());
                break;
        }
    }

    @Override
    public void appendItems(List<IFlexible> list) {
        progress.setVisibility(View.GONE);

        int startPosition=listAdapter.size();
        listAdapter.addAll(startPosition,list);
        adapter.notifyItemRangeChanged(startPosition,listAdapter.size());
        if(listAdapter.size()==0) {
            tv_not_found.setVisibility(View.VISIBLE);
            finder.setVisibility(View.GONE);
        }



        if(listAdapter.size()==0) {
            tv_not_found.setVisibility(View.VISIBLE);
            swipe.setVisibility(View.GONE);
        }else {
            tv_not_found.setVisibility(View.GONE);
            swipe.setVisibility(View.VISIBLE);
        }

        swipe.setEnabled(false);
        swipe.setRefreshing(false);

    }
}
