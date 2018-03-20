package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.FilterAdapter;
import com.brewmapp.data.entity.Brewery;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.entity.FilteredTitle;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.data.entity.SearchBeer;
import com.brewmapp.data.entity.wrapper.SearchRestoInfo;
import com.brewmapp.data.entity.wrapper.SearchBeerInfo;
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
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ResultSearchActivity extends BaseActivity implements
        ResultSearchActivityView,
        View.OnClickListener,
        AdapterView.OnItemClickListener
{

    //region BindView
    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_search_list) RecyclerView list;
    @BindView(R.id.activity_search_search) FinderView finder;
    @BindView(R.id.activity_search_more) Button more;
    @BindView(R.id.common_toolbar_title)    TextView titleToolbar;
    @BindView(R.id.activity_search_tv_not_found)    TextView tv_not_found;
    @BindView(R.id.activity_search_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.activity_search_progress)    RelativeLayout progress;
    @BindView(R.id.progressToolbar)    ProgressBar progressBar;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbar_subtitle;
    @BindView(R.id.filter_list)    ListView filter_list;
    @BindView(R.id.container_filter_list)    LinearLayout container_filter_list;


    //endregion

    //region Private
    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage searchPackage = new FullSearchPackage();
    private int selectedTab;
    private EndlessRecyclerOnScrollListener scrollListener;
    private ProgressDialog dialog;
    private String[] titleContent = ResourceHelper.getResources().getStringArray(R.array.full_search);
    private List<IFlexible> listAdapter=new ArrayList<>();
    private boolean clickGoMap=false;
    //endregion

    //region Inject
    @Inject
    ResultSearchActivityPresenter presenter;
    //endregion

    //region Impl ResultSearchActivity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(menu!=null) menu.clear();
        getMenuInflater().inflate(R.menu.map,menu);
        MenuItem menuItem=menu.findItem(R.id.action_map);
        switch (selectedTab){
            case SearchFragment.TAB_RESTO:
            case SearchFragment.TAB_BEER:
                menuItem.setVisible(listAdapter.size()!=0&&!clickGoMap);
                break;
            case SearchFragment.TAB_BREWERY:
                menuItem.setVisible(false);
                break;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_map:
                progressBar.setVisibility(View.VISIBLE);
                clickGoMap=true;
                invalidateOptionsMenu();
                switch (selectedTab) {
                    case SearchFragment.TAB_RESTO: {
                        filterListVisible(false);
                        presenter.getLocationsResto(searchPackage);
                    }
                    break;
                    case SearchFragment.TAB_BEER: {
                        presenter.getLocationsBeer(searchPackage);
                    }
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
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
                                searchPackage.setCityName(filterRestoField.getSelectedFilter());
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
                            case FilterBeerField.CITY:
                                searchPackage.setCity(filterBeerField.getSelectedItemId());break;
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
        searchPackage.setOrder(Keys.ORDER_SORT_RATING_DESC);
        toolbar_subtitle.setOnClickListener(this);
        filter_list.setOnItemClickListener(this);
        //endregion

    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        //region StartLoadResults
        searchPackage.setPage(0);
        loadResult(searchPackage);
        //endregion
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
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    //endregion

    //region Impl ResultSearchActivityView
    @Override
    public void commonError(String... strings) {
        if(strings!=null && strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();
    }

    @Override
    public void setLocations(List<FilterRestoLocation> filterRestoLocations) {
        ArrayList<RestoLocation> arrayListResult=new ArrayList<>();

        switch (selectedTab){
            case SearchFragment.TAB_RESTO:{
                //region Resto to Map

                HashMap<String,FilterRestoLocation> hashMap=new HashMap<>();
                for (FilterRestoLocation filterRestoLocation:filterRestoLocations)
                    hashMap.put(filterRestoLocation.getRestoId(),filterRestoLocation);

                for (IFlexible iFlexible:listAdapter){
                    SearchRestoInfo searchRestoInfo = (SearchRestoInfo) iFlexible;
                    FilterRestoLocation  filterRestoLocation=hashMap.get(String.valueOf(searchRestoInfo.getModel().getId()));
                    if(filterRestoLocation!=null)
                        arrayListResult.add(
                                new RestoLocation(
                                        filterRestoLocation.getRestoId(),
                                        Integer.valueOf(filterRestoLocation.getLocationId()),
                                        filterRestoLocation.getmName(),
                                        filterRestoLocation.getLocationLat(),
                                        filterRestoLocation.getLocationLon()
                                )
                        );

                }
                Starter.MainActivity(this,MainActivity.MODE_MAP_FRAGMENT,arrayListResult);
                //endregion
            }break;
            case SearchFragment.TAB_BEER:{
                //region Beer to Map
                HashMap<String,RestoLocation> hmListResult=new HashMap<>();
                HashMap<String,ArrayList<FilterRestoLocation>> hmBeerPourResto=new HashMap<>();
                for (FilterRestoLocation filterRestoLocation:filterRestoLocations){
                    String beerId=filterRestoLocation.getmBeerId();

                    ArrayList<FilterRestoLocation> arrayList=hmBeerPourResto.get(beerId);
                    if(arrayList==null) arrayList=new ArrayList<>();

                    arrayList.add(filterRestoLocation);
                    hmBeerPourResto.put(beerId,arrayList);
                }

                for (IFlexible iFlexible:listAdapter) {
                    SearchBeerInfo beerInfo= (SearchBeerInfo) iFlexible;
                    String beerId=beerInfo.getModel().getId();
                    ArrayList<FilterRestoLocation> arrayListRestoLocation = hmBeerPourResto.get(beerId);
                    if(arrayListRestoLocation !=null){
                        for (FilterRestoLocation  filterRestoLocation:arrayListRestoLocation ){
                            String keyResto=filterRestoLocation.getRestoId();
                            RestoLocation restoLocation=hmListResult.get(keyResto);
                            if(restoLocation==null) {
                                restoLocation = new RestoLocation(
                                        filterRestoLocation.getRestoId(),
                                        0,
                                        filterRestoLocation.getmName(),
                                        filterRestoLocation.getLocationLat(),
                                        filterRestoLocation.getLocationLon()
                                );
                            }
                            restoLocation.getBeersId().put(beerId,beerId);
                            hmListResult.put(keyResto,restoLocation );
                        }
                    }
                }
                arrayListResult.addAll(hmListResult.values());
                Starter.MainActivity(this,MainActivity.MODE_MAP_FRAGMENT,arrayListResult);
                //endregion
            }
        }
        progressBar.setVisibility(View.GONE);
        clickGoMap=false;
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

        invalidateOptionsMenu();
    }
    @Override
    public void enableControls(boolean enabled, int code) {
    }
    //endregion

    //region User Events
    private void processAction(int action, Object payload) {

        switch (selectedTab) {
            case SearchFragment.TAB_RESTO:
                filterListVisible(false);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_toolbar_subtitle:
                ArrayList<FilteredTitle> arrayList=new ArrayList<>();
                String[] orders = getResources().getStringArray(R.array.order_search_resto);
                for (int i=0;i<orders.length;i++)
                    arrayList.add(new FilteredTitle(orders[i], toolbar_subtitle.getText().toString().toLowerCase().contains(orders[i].toLowerCase())));

                filter_list.setAdapter(new FilterAdapter(this,arrayList));
                filterListVisible(container_filter_list.getVisibility()==View.GONE);

                break;
        }
    }

    private void filterListVisible(boolean visible) {
        if(visible){
            container_filter_list.setVisibility(View.VISIBLE);
            filter_list.setAlpha(0);
            ObjectAnimator.ofFloat(filter_list,"alpha",1).setDuration(500).start();
        }else {
            ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(filter_list,"alpha",0).setDuration(500);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    container_filter_list.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        filterListVisible(false);
        switch (position){
            case 0:
                searchPackage.setOrder(Keys.ORDER_SORT_RATING_DESC);
                break;
            case 1:
                searchPackage.setOrder(Keys.ORDER_SORT_DISTANCE_ASC);
                break;
        }
        setSubtitle(getTextSubtitle(), true);
        searchPackage.setPage(0);
        loadResult(searchPackage);
        progress.setVisibility(View.VISIBLE);
    }


    //endregion

    //region Functions

    private void loadResult(FullSearchPackage searchPackage) {
        if(searchPackage.getPage()==0) {
            progress.setVisibility(View.VISIBLE);
            swipe.setVisibility(View.GONE);
        }else {
            swipe.setVisibility(View.VISIBLE);
            swipe.setEnabled(true);
            swipe.setRefreshing(true);
        }
        setSubtitle(getTextSubtitle(), true);
        switch (selectedTab) {
            case SearchFragment.TAB_RESTO:
                requestLastLocation(location -> {
                    if(location==null)
                        location=getDefaultLocation();
                    searchPackage.setLat(location.getLatitude());
                    searchPackage.setLon(location.getLongitude());
                    presenter.loadRestoList(0, searchPackage);
                });
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

    private void setSubtitle(String subtitle, boolean visible) {
        if(subtitle!=null) {
            toolbar_subtitle.setVisibility(visible ? View.VISIBLE : View.GONE);
            toolbar_subtitle.setText(subtitle);
        }else {
            toolbar_subtitle.setVisibility(View.GONE);
        }
    }

    private String getTextSubtitle() {
        String textSubtitle=null;
        switch (selectedTab) {
            case SearchFragment.TAB_RESTO:
                String[] orders = getResources().getStringArray(R.array.order_search_resto);
                switch (searchPackage.getOrder()){
                    case Keys.ORDER_SORT_RATING_DESC:
                        textSubtitle=orders[0] +", по городу "+searchPackage.getCityName();
                        break;
                    case Keys.ORDER_SORT_DISTANCE_ASC:
                        textSubtitle=orders[1]+ ", по городу "+searchPackage.getCityName();
                        break;

                }
                break;
            case SearchFragment.TAB_BEER:
                break;
            case SearchFragment.TAB_BREWERY:
                break;
            default:
                break;
        }
        if(textSubtitle!=null&&textSubtitle.length()>0)
            textSubtitle=textSubtitle.substring(0, 1).toUpperCase() + textSubtitle.substring(1);

        return textSubtitle;
    }



    //endregion


}
