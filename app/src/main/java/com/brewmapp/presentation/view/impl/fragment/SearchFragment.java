package com.brewmapp.presentation.view.impl.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.MenuField;
import com.brewmapp.data.entity.SearchFragmentPackage;
import com.brewmapp.data.entity.container.FilterBeer;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.SearchFragmentPresenter;
import com.brewmapp.presentation.view.contract.OnLocationInteractionListener;
import com.brewmapp.presentation.view.contract.SearchAllView;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.activity.SelectCategoryActivity;
import com.brewmapp.presentation.view.impl.activity.ResultSearchActivity;
import com.brewmapp.presentation.view.impl.widget.TabsView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.paperdb.Paper;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.impl.SimpleTabSelectListener;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SearchFragment extends BaseFragment implements SearchAllView
{

    //region BindView
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
    //endregion

    //region Inject
    @Inject    SearchFragmentPresenter presenter;
    //endregion

    //region Private
    private OnFragmentInteractionListener mListener;
    private OnLocationInteractionListener mLocationListener;
    private FlexibleModelAdapter<FilterRestoField> restoAdapter;
    private FlexibleModelAdapter<FilterBeerField> beerAdapter;
    private FlexibleModelAdapter<FilterBreweryField> breweryAdapter;
    private List<FilterRestoField> restoFilterList;
    private List<FilterBeerField> beerFilterList;
    private List<FilterBreweryField> breweryList;
    private SearchFragmentPackage searchFragmentPackage = new SearchFragmentPackage();
    private String[] searchContent = ResourceHelper.getResources().getStringArray(R.array.full_search);
    private String[] titleContent = ResourceHelper.getResources().getStringArray(R.array.search_title);
    //endregion

    //region Public static
    public static final int TAB_RESTO = 0;
    public static final int TAB_BEER = 1;
    public static final int TAB_BREWERY = 2;
    public static final String CATEGORY_LIST_RESTO = "restoCategoryList";
    public static final String CATEGORY_LIST_BEER = "beerCategoryList";
    public static final String CATEGORY_LIST_BREWERY = "breweryCategoryList";
    //endregion

    //region Impl SearchFragment
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_search;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View view) {

        tabsView.setItems(Arrays.asList(searchContent), new SimpleTabSelectListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchFragmentPackage.setMode(tab.getPosition());
                presenter.setTabActive(tab.getPosition());
                offer.setVisibility(tabsView.getTabs().getSelectedTabPosition() == 0 ? View.VISIBLE : View.GONE);
                craft.setVisibility(tabsView.getTabs().getSelectedTabPosition() == 1 ? View.VISIBLE : View.GONE);
                filterBeer.setVisibility(tabsView.getTabs().getSelectedTabPosition() == 1 ? View.VISIBLE : View.GONE);
                interractor().processSetFilterFragmentActionBar(SearchFragment.this);
            }
        });
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.addItemDecoration(new ListDivider(getContext(), ListDivider.VERTICAL_LIST));
        list.setNestedScrollingEnabled(true);
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
        presenter.setTabActive(TAB_RESTO);
        mLocationListener.requestCity(result -> {
            if(result!=null) {
                List<FilterRestoField> listResto = Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO);
                listResto.get(FilterRestoField.CITY).setSelectedItemId(String.valueOf(result.getId()));
                listResto.get(FilterRestoField.CITY).setSelectedFilter(String.valueOf(result.getName()));
                Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, listResto);
                refreshItemRestoFilters(FilterRestoField.CITY, listResto);

                List<FilterBeerField> listBeer = Paper.book().read(SearchFragment.CATEGORY_LIST_BEER);
                listBeer.get(FilterBeerField.CITY).setSelectedItemId(String.valueOf(result.getId()));
                listBeer.get(FilterBeerField.CITY).setSelectedFilter(String.valueOf(result.getName()));
                Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, listBeer);

            }
        });

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
    protected void prepareView(View view) {
        super.prepareView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mLocationListener = mListener.getLocationListener();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    //endregion

    //region Impl SearchAllView
    @Override
    public void enableControls(boolean enabled, int code) {
    }

    @Override
    public void showRestoFilters(List<FilterRestoField> fieldList) {
        this.restoFilterList = fieldList;
        restoAdapter = new FlexibleModelAdapter<>(restoFilterList, this::processAction);
        list.setAdapter(restoAdapter);
        restoAdapter.notifyItemRangeChanged(0,restoFilterList.size());
    }

    @Override
    public void showBeerFilters(List<FilterBeerField> fieldList) {
        this.beerFilterList = fieldList;
        beerAdapter = new FlexibleModelAdapter<>(fieldList, this::processAction);
        list.setAdapter(beerAdapter);

    }

    @Override
    public void showBreweryFilters(List<FilterBreweryField> fieldList) {
        this.breweryList = fieldList;
        breweryAdapter = new FlexibleModelAdapter<>(fieldList, this::processAction);
        list.setAdapter(breweryAdapter);
    }

    @Override
    public void commonError(String... strings) {
        mListener.commonError(strings);

    }

    @Override
    public void refreshItemRestoFilters(int position, List<FilterRestoField> list) {
        this.restoFilterList.clear();
        this.restoFilterList.addAll(list);
        restoAdapter.notifyItemRangeChanged(position,1);
    }
    //endregion

    //region User Events
    @OnClick(R.id.accept_filter_layout)
    public void acceptFilter() {
        int selectedTab=tabsView.getTabs().getSelectedTabPosition();
        switch (selectedTab){
            case TAB_RESTO: {
                String filtrCity = restoFilterList.get(FilterRestoField.CITY).getSelectedItemId();
                if (filtrCity == null) {
                    mListener.showSnackbarRed(getString(R.string.text_need_select_city));
                    return;
                }
            }break;
            case TAB_BEER: {
                String filtrCity = beerFilterList.get(FilterBeerField.CITY).getSelectedItemId();
                if (filtrCity == null) {
                    mListener.showSnackbarRed(getString(R.string.text_need_select_city));
                    return;
                }
            }break;
        }

        Starter.ResultSearchActivity((BaseActivity) getActivity(),selectedTab);
    }

    private void processAction(int code, Object o) {
        if(code==FilterRestoField.CODE_CLICK_FILTER_START_SELECTION ||code==FilterBeerField.CODE_CLICK_FILTER_START_SELECTION ||code==FilterBreweryField.CODE_CLICK_FILTER_START_SELECTION){
            //region Selection category
            boolean result=false;
            int itemId=0;
            String filterTxt=null;
            String filterId=null;
            switch (tabsView.getTabs().getSelectedTabPosition()){
                //region RESTO
                case TAB_RESTO:{
                    FilterRestoField f=((FilterRestoField)o);
                    itemId=f.getId();
                    filterTxt=f.getSelectedFilter();
                    filterId=f.getSelectedItemId();
                    switch (itemId){
                        case FilterRestoField.NAME:
                        case FilterRestoField.BEER:
                        case FilterRestoField.CITY:
                        case FilterRestoField.PRICE:
                        case FilterRestoField.TYPE:
                        case FilterRestoField.KITCHEN:
                            result=true;
                            break;
                    }
                    Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, restoFilterList );
                }
                //endregion
                break;
                //region BEER
                case TAB_BEER:{
                    FilterBeerField f=((FilterBeerField)o);
                    itemId=f.getId();
                    filterTxt=f.getSelectedFilter();
                    filterId=f.getSelectedItemId();
                    switch (itemId){
                        case FilterBeerField.NAME:
                            result=true;
                            break;
                        case FilterBeerField.COUNTRY:
                            result=true;
                            break;
                        case FilterBeerField.TYPE:
                            result=true;
                            break;
                        case FilterBeerField.BRAND:
                            result=true;
                            break;
                        case FilterBeerField.POWER:
                            result=true;
                            break;
                        case FilterBeerField.BEER_FILTER:
                            result=true;
                            break;
                        case FilterBeerField.BEER_PACK:
                            result=true;
                            break;
                        case FilterBeerField.COLOR:
                            result=true;
                            break;
                        case FilterBeerField.SMELL:
                            result=true;
                            break;
                        case FilterBeerField.TASTE:
                            result=true;
                            break;
                        case FilterBeerField.AFTER_TASTE:
                            result=true;
                            break;
                        case FilterBeerField.BREWERY:
                            result=true;
                            break;
                        case FilterBeerField.PRICE_BEER:
                            result=true;
                            break;
                        case FilterBeerField.DENSITY:
                            result=true;
                            break;
                        case FilterBeerField.CITY:
                            result=true;
                            break;
                    }
                    Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                }
                //endregion
                break;
                //region BREWERY
                case TAB_BREWERY:{
                    FilterBreweryField f=((FilterBreweryField)o);
                    itemId=f.getId();
                    filterTxt=f.getSelectedFilter();
                    filterId=f.getSelectedItemId();
                    switch (itemId) {
                        case FilterBreweryField.NAME:
                            result = true;
                            break;
                        case FilterBreweryField.COUNTRY:
                            result = true;
                            break;
                        case FilterBreweryField.BRAND:
                            result = true;
                            break;
                        case FilterBreweryField.TYPE_BEER:
                            result = true;
                            break;
                    }
                    Paper.book().write(SearchFragment.CATEGORY_LIST_BREWERY, breweryList);
                }
                //endregion
                break;
            }

            //region Go to SelectCategoryActivity
            if(result){
                Intent intent = new Intent(getContext(), SelectCategoryActivity.class);
                intent.putExtra(Actions.PARAM1,tabsView.getTabs().getSelectedTabPosition());
                intent.putExtra(Actions.PARAM2,itemId);
                intent.putExtra(Actions.PARAM3,new StringBuilder().append(filterId).toString());
                intent.putExtra(Actions.PARAM4,new StringBuilder().append(filterTxt).toString());
                startActivityForResult(intent, RequestCodes.REQUEST_SEARCH_CODE);
            }else {
                Toast.makeText(getContext(), "В разработке...", Toast.LENGTH_SHORT).show();
            }
            //endregion

            //endregion
        }
        else if(code==FilterRestoField.CODE_CLICK_FILTER_CLEAR||code==FilterBeerField.CODE_CLICK_FILTER_CLEAR||code==FilterBreweryField.CODE_CLICK_FILTER_CLEAR){
            //region Clear category
            switch (tabsView.getTabs().getSelectedTabPosition()){
                case TAB_RESTO:
                    ((FilterRestoField)o).clearFilter();
                    restoAdapter.notifyDataSetChanged();
                    Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, restoFilterList );
                    break;
                case TAB_BEER:
                    ((FilterBeerField)o).clearFilter();
                    beerAdapter.notifyDataSetChanged();
                    Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                    break;
                case TAB_BREWERY:
                    ((FilterBreweryField)o).clearFilter();
                    breweryAdapter.notifyDataSetChanged();
                    Paper.book().write(SearchFragment.CATEGORY_LIST_BREWERY, breweryList);
                    break;
                default:{
                    commonError(getString(R.string.not_valid_param));
                    return;
                }
            }
            //endregion
        }
        else if(code==FilterRestoField.CODE_CLICK_FILTER_ERROR||code==FilterBeerField.CODE_CLICK_FILTER_ERROR||code==FilterBreweryField.CODE_CLICK_FILTER_ERROR){
            Toast.makeText(getContext(), "В разработке...", Toast.LENGTH_SHORT).show();
        }

    }
    //endregion

    //region Functions
    public void showResult(Intent data) {

        //region Parse intent
        int numberTab=data.getIntExtra(Actions.PARAM1,Integer.MAX_VALUE);
        int numberMenuItem=data.getIntExtra(Actions.PARAM2,Integer.MAX_VALUE);
        String filterID=data.getStringExtra(Actions.PARAM3);
        String filterTXT=data.getStringExtra(Actions.PARAM4);
        //endregion

        //region Refresh filter list
        switch (numberTab) {
            //region RESTO
            case TAB_RESTO:
                switch (numberMenuItem){
                    case FilterRestoField.TYPE:
                    case FilterRestoField.BEER:
                    case FilterRestoField.KITCHEN:
                    case FilterRestoField.PRICE:
                    case FilterRestoField.CITY:
                        if("null".equals(filterID)){
                            restoFilterList.get(numberMenuItem).clearFilter();
                        }else {
                            restoFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                            restoFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        }
                        restoAdapter.notifyItemChanged(numberMenuItem);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, restoFilterList );
                        break;
                    default:
                        commonError(getString(R.string.not_valid_param));
                }
                break;
            //endregion
            //region BEER
            case TAB_BEER:
                switch (numberMenuItem){
                    //region COUNTRY
                    case FilterBeerField.COUNTRY: {
                        if ("null".equals(filterID)) {
                            beerFilterList.get(numberMenuItem).clearFilter();
                        } else {
                            beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                            beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        }
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region TYPE
                    case FilterBeerField.TYPE: {
                        if ("null".equals(filterID)) {
                            beerFilterList.get(numberMenuItem).clearFilter();
                        } else {
                            beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                            beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        }
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region BEER_FILTER
                    case FilterBeerField.BEER_FILTER: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        if(filterID.split(",").length==2)
                            beerFilterList.get(numberMenuItem).clearFilter();
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region BEER_PACK
                    case FilterBeerField.BEER_PACK: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region COLOR
                    case FilterBeerField.COLOR: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region SMELL
                    case FilterBeerField.SMELL: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region TASTE
                    case FilterBeerField.TASTE: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region AFTER_TASTE
                    case FilterBeerField.AFTER_TASTE: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region BREWERY
                    case FilterBeerField.BREWERY: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region DENSITY
                    case FilterBeerField.DENSITY: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region BRAND
                    case FilterBeerField.BRAND: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region BRAND
                    case FilterBeerField.POWER: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    //region BRAND
                    case FilterBeerField.PRICE_BEER: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    case FilterBeerField.CITY: {
                        beerFilterList.get(numberMenuItem).setSelectedItemId(filterID);
                        beerFilterList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                        beerAdapter.notifyItemChanged(numberMenuItem);
                    }break;
                    //endregion
                    default:
                        commonError(getString(R.string.not_valid_param));
                }
                break;
            //endregion
            //region BREWERY
            case TAB_BREWERY:
                switch (numberMenuItem){
                    case FilterBreweryField.NAME:
                        break;

                    case FilterBreweryField.COUNTRY:
                    case FilterBreweryField.BRAND:
                    case FilterBreweryField.TYPE_BEER: {
                        if ("null".equals(filterID)) {
                            breweryList.get(numberMenuItem).clearFilter();
                        } else {
                            breweryList.get(numberMenuItem).setSelectedItemId(filterID);
                            breweryList.get(numberMenuItem).setSelectedFilter(filterTXT);
                        }
                        Paper.book().write(SearchFragment.CATEGORY_LIST_BREWERY, breweryList);
                        breweryAdapter.notifyItemChanged(numberMenuItem);
                    }break;

                    default:
                        commonError(getString(R.string.not_valid_param));
                }
                break;
            default:
                commonError(getString(R.string.not_valid_param));
                //endregion
        }
        //endregion
    }
    //endregion

    public interface OnFragmentInteractionListener {
        void commonError(String... message);
        void setTitle(CharSequence name);
        OnLocationInteractionListener getLocationListener();
        void processChangeFragment(int id);
        void showSnackbar(String string);

        void showSnackbarRed(String string);
    }

}
