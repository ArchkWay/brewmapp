package com.brewmapp.presentation.view.impl.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.presentation.presenter.contract.SearchFragmentPresenter;
import com.brewmapp.presentation.view.contract.MultiListView;
import com.brewmapp.presentation.view.contract.OnLocationInteractionListener;
import com.brewmapp.presentation.view.contract.SearchAllView;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.brewmapp.presentation.view.impl.activity.SelectCategoryActivity;
import com.brewmapp.presentation.view.impl.widget.TabsView;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.paperdb.Paper;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
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

    //region Public static
    public static final String CATEGORY_LIST_RESTO = "restoCategoryList";
    public static final String CATEGORY_LIST_BEER = "beerCategoryList";
    public static final String CATEGORY_LIST_BREWERY = "breweryCategoryList";
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
    private String[] searchContent = ResourceHelper.getResources().getStringArray(R.array.full_search);
    private SimpleTabSelectListener simpleTabSelectListener;
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
        simpleTabSelectListener=new SimpleTabSelectListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                presenter.setContentTab((String) tab.getTag(), mLocationListener, mListener);
                offer.setVisibility(tab.getTag() == CATEGORY_LIST_RESTO ? View.VISIBLE : View.GONE);
                craft.setVisibility(tab.getTag() == CATEGORY_LIST_BEER ? View.VISIBLE : View.GONE);
                filterBeer.setVisibility(tab.getTag() == CATEGORY_LIST_BEER ? View.VISIBLE : View.GONE);

            }
        };
        tabsView.setItems(Arrays.asList(searchContent), simpleTabSelectListener);

        tabsView.getTabs().getTabAt(0).setTag(CATEGORY_LIST_BEER);
        tabsView.getTabs().getTabAt(1).setTag(CATEGORY_LIST_RESTO);
        tabsView.getTabs().getTabAt(2).setTag(CATEGORY_LIST_BREWERY);

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
        simpleTabSelectListener.onTabSelected(tabsView.getTabs().getTabAt(0));
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public int getMenuToInflate() {
        return R.menu.add_string;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
            return "";
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

    @Override
    public void onBarAction(int id) {
        switch (id){
            case R.id.action_add:
                switch (presenter.getActiveTab()){
                    case CATEGORY_LIST_RESTO:
                        startActivity(new Intent(MultiListView.MODE_SHOW_AND_CREATE_RESTO,null,getActivity(), MultiListActivity.class));
                        return;
                    case CATEGORY_LIST_BEER:
                        startActivity(new Intent(MultiListView.MODE_SHOW_AND_CREATE_BEER,null,getActivity(), MultiListActivity.class));
                        return;
                    case CATEGORY_LIST_BREWERY:
                        showMessage(getString(R.string.message_develop));
                        return;
                }

                return;
        }
        super.onBarAction(id);
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

    //endregion

    //region User Events
    @OnClick(R.id.accept_filter_layout)
    public void acceptFilter() {

        switch (presenter.getActiveTab()){
            case CATEGORY_LIST_RESTO: {
                String filtrCity = restoFilterList.get(FilterRestoField.CITY).getSelectedItemId();
                if (filtrCity == null) {
                    mListener.showSnackbarRed(getString(R.string.text_need_select_city));
                    return;
                }
            }break;
            case CATEGORY_LIST_BEER: {
                String filtrCity = beerFilterList.get(FilterBeerField.CITY).getSelectedItemId();
                if (filtrCity == null) {
                    mListener.showSnackbarRed(getString(R.string.text_need_select_city));
                    return;
                }
            }break;
        }

        Starter.ResultSearchActivity((BaseActivity) getActivity(),presenter.getActiveTab());
    }

    private void processAction(int code, Object o) {



        if(code==FilterRestoField.CODE_CLICK_FILTER_START_SELECTION ||code==FilterBeerField.CODE_CLICK_FILTER_START_SELECTION ||code==FilterBreweryField.CODE_CLICK_FILTER_START_SELECTION){
            //region Selection category
            boolean result=false;
            int itemId=0;
            String filterTxt=null;
            String filterId=null;
            String city_id=null;
            switch (presenter.getActiveTab()){
                //region RESTO
                case CATEGORY_LIST_RESTO:{
                    FilterRestoField f=((FilterRestoField)o);
                    itemId=f.getId();
                    filterTxt=f.getSelectedFilter();
                    filterId=f.getSelectedItemId();
                    switch (itemId){
                        case FilterRestoField.NAME:
                            List<FilterRestoField> listResto = Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO);
                            city_id=listResto.get(FilterRestoField.CITY).getSelectedItemId();
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
                case CATEGORY_LIST_BEER:{
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
                case CATEGORY_LIST_BREWERY:{
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
                intent.putExtra(Actions.PARAM1,presenter.getActiveTab());
                intent.putExtra(Actions.PARAM2,itemId);
                intent.putExtra(Actions.PARAM3,new StringBuilder().append(filterId).toString());
                intent.putExtra(Actions.PARAM4,new StringBuilder().append(filterTxt).toString());
                Location userLocation=presenter.getUsetLocation();
                if(userLocation!=null){
                    intent.putExtra(Actions.PARAM5,userLocation.getLatitude());
                    intent.putExtra(Actions.PARAM6,userLocation.getLongitude());
                }
                if(city_id!=null)
                    intent.putExtra(Actions.PARAM7,city_id);

                startActivityForResult(intent, RequestCodes.REQUEST_SEARCH_CODE);
            }else {
                Toast.makeText(getContext(), "В разработке...", Toast.LENGTH_SHORT).show();
            }
            //endregion

            //endregion
        }
        else if(code==FilterRestoField.CODE_CLICK_FILTER_CLEAR||code==FilterBeerField.CODE_CLICK_FILTER_CLEAR||code==FilterBreweryField.CODE_CLICK_FILTER_CLEAR){
            //region Clear category

            switch (presenter.getActiveTab()){
                case CATEGORY_LIST_RESTO:
                    ((FilterRestoField)o).clearFilter();
                    restoAdapter.notifyDataSetChanged();
                    Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, restoFilterList );
                    break;
                case CATEGORY_LIST_BEER:
                    ((FilterBeerField)o).clearFilter();
                    beerAdapter.notifyDataSetChanged();
                    Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, beerFilterList);
                    break;
                case CATEGORY_LIST_BREWERY:
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
        String numberTab=data.getStringExtra(Actions.PARAM1);
        int numberMenuItem=data.getIntExtra(Actions.PARAM2,Integer.MAX_VALUE);
        String filterID=data.getStringExtra(Actions.PARAM3);
        String filterTXT=data.getStringExtra(Actions.PARAM4);
        //endregion

        //region Refresh filter list
        switch (numberTab) {
            //region RESTO
            case CATEGORY_LIST_RESTO:
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
            case CATEGORY_LIST_BEER:
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
            case CATEGORY_LIST_BREWERY:
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
