package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    @BindView(R.id.common_toolbar_title)    TextView common_toolbar_title;
    @BindView(R.id.activity_search_tv_not_found)    TextView tv_not_found;
    @BindView(R.id.activity_search_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.progressToolbar)    ProgressBar progressBar;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout common_toolbar_dropdown;
    @BindView(R.id.common_toolbar_subtitle)    TextView common_toolbar_subtitle;
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
    private String[] orders=new String[0];
    private ResultReceiver resultReceiver;
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
        common_toolbar_dropdown.setVisibility(View.VISIBLE);
        enableBackButton();

        //region Parse Intent
        selectedTab=getIntent().getIntExtra(Actions.PARAM1,Integer.MAX_VALUE);
        switch (selectedTab) {

            //region TAB_RESTO
            case SearchFragment.TAB_RESTO: {

                orders = getResources().getStringArray(R.array.order_search_resto);
                searchPackage.setOrder(Keys.ORDER_SORT_RATING_RESTO_DESC);

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

                orders = getResources().getStringArray(R.array.order_search_beer);
                searchPackage.setOrder(Keys.ORDER_SORT_RATING_BEER_DESC);

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
                                searchPackage.setCity(filterBeerField.getSelectedItemId());
                                searchPackage.setCityName(filterBeerField.getSelectedFilter());
                                break;
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
        common_toolbar_title.setText(titleContent[selectedTab]);
        more.setOnClickListener(v -> startActivity(ExtendedSearchActivity.class));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                searchPackage.setPage(currentPage-1);
                continueLoadResult();
            }
        };
        list.addOnScrollListener(scrollListener);
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        list.setLayoutManager(manager);
        adapter = new FlexibleModelAdapter<>(listAdapter, this::processAction);
        list.setAdapter(adapter);
        finder.setVisibility(View.GONE);
        filter_list.setOnItemClickListener(this);
        common_toolbar_dropdown.setOnClickListener(this);
        container_filter_list.setOnClickListener(this);
        swipe.setEnabled(false);
        //endregion

    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        prepareLoadResult(searchPackage);
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
        }

        invalidateOptionsMenu();
        resultReceiver.send(Actions.ACTION_STOP_PROGRESS_BAR,null);
    }

    @Override
    public void enableControls(boolean enabled, int code) {
    }
    //endregion

    //region User Events
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_toolbar_dropdown:
            case R.id.container_filter_list:
                ToggleVisibleFilterList();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToggleVisibleFilterList();
        switch (position){
            case 0:
                if(selectedTab==SearchFragment.TAB_RESTO)
                    searchPackage.setOrder(Keys.ORDER_SORT_RATING_RESTO_DESC);
                else if(selectedTab==SearchFragment.TAB_BEER)
                    searchPackage.setOrder(Keys.ORDER_SORT_RATING_BEER_DESC);
                break;
            case 1:
                if(selectedTab==SearchFragment.TAB_RESTO)
                    searchPackage.setOrder(Keys.ORDER_SORT_DISTANCE_RESTO_ASC);
                else if(selectedTab==SearchFragment.TAB_BEER)
                    searchPackage.setOrder(Keys.ORDER_SORT_PRICE_BEER_ASC);
                break;
            case 2:
                if(selectedTab==SearchFragment.TAB_BEER)
                    searchPackage.setOrder(Keys.ORDER_SORT_PRICE_BEER_DESC);
                break;
            default:
                searchPackage.setOrder(null);
        }

        prepareLoadResult(searchPackage);
    }


    //endregion

    //region Functions

    private void ToggleVisibleFilterList() {
        boolean show=container_filter_list.getVisibility()==View.GONE;
        ObjectAnimator objectAnimator;
        if(show){
            container_filter_list.setVisibility(View.VISIBLE);
            filter_list.setAdapter(new ResultSearchFilterAdapter (this));
            objectAnimator=ObjectAnimator.ofFloat(container_filter_list,"alpha",1);
        }else {
            objectAnimator=ObjectAnimator.ofFloat(container_filter_list,"alpha",0);
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

        }
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    private void prepareLoadResult(FullSearchPackage searchPackage) {

        //region set Subtitle

        String subtitle=null;

        if(searchPackage.getOrder()!=null)
            switch (searchPackage.getOrder()){
                case Keys.ORDER_SORT_RATING_RESTO_DESC:
                    subtitle=orders[0] +", по городу "+searchPackage.getCityName();
                    break;
                case Keys.ORDER_SORT_DISTANCE_RESTO_ASC:
                    subtitle=orders[1]+ ", по городу "+searchPackage.getCityName();
                    break;
                case Keys.ORDER_SORT_RATING_BEER_DESC:
                    subtitle=orders[0] +", по городу "+searchPackage.getCityName();
                    break;
                case Keys.ORDER_SORT_PRICE_BEER_ASC:
                    subtitle=orders[1] +", по городу "+searchPackage.getCityName();
                    break;
                case Keys.ORDER_SORT_PRICE_BEER_DESC:
                    subtitle=orders[2] +", по городу "+searchPackage.getCityName();
                    break;
                default:
                    subtitle=null;
            }

        common_toolbar_subtitle.setVisibility(View.GONE);
        if(subtitle!=null&&subtitle.length()>0){
            subtitle=subtitle.substring(0, 1).toUpperCase() + subtitle.substring(1);
            common_toolbar_subtitle.setText(subtitle);
            common_toolbar_subtitle.setVisibility(View.VISIBLE);
        }
        //endregion

        searchPackage.setPage(0);
        swipe.setVisibility(View.VISIBLE);
        tv_not_found.setVisibility(View.GONE);
        resultReceiver=ProgressBarOn();
        continueLoadResult();

    }

    private void continueLoadResult() {
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





    //endregion

    class ResultSearchFilterAdapter extends FilterAdapter{

        public ResultSearchFilterAdapter(Context context) {
            super(context, new ArrayList<>());
            for (int i=0;i<orders.length;i++)
                filterList.add(
                        new FilteredTitle(
                                orders[i],
                                common_toolbar_subtitle.getText().toString().toLowerCase().contains(orders[i].toLowerCase())
                        )
                );

        }
    }

}
