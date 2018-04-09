package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.execution.task.LoadCityTask;
import com.brewmapp.presentation.presenter.contract.SearchFragmentPresenter;
import com.brewmapp.presentation.view.contract.OnLocationInteractionListener;
import com.brewmapp.presentation.view.contract.SearchAllView;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;

import java.util.List;

import javax.inject.Inject;

import io.paperdb.Paper;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import static com.brewmapp.presentation.view.impl.fragment.SearchFragment.CATEGORY_LIST_BREWERY;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SearchFragmentPresenterImpl extends BasePresenter<SearchAllView> implements SearchFragmentPresenter {

    private UiSettingRepo uiSettingRepo;
    private Context context;
    private LoadCityTask loadCityTask;
    private String ActiveTab;
    private Location userLocation=null;

    @Inject
    public SearchFragmentPresenterImpl(Context context, UiSettingRepo uiSettingRepo,LoadCityTask loadCityTask) {
        this.uiSettingRepo = uiSettingRepo;
        this.context = context;
        this.loadCityTask = loadCityTask;
    }

    @Override
    public void onAttach(SearchAllView searchAllView) {
        super.onAttach(searchAllView);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void setContentTab(String newTabActive, OnLocationInteractionListener mLocationListener, SearchFragment.OnFragmentInteractionListener mListener) {
        ActiveTab=newTabActive;
        String[] titleContent = ResourceHelper.getResources().getStringArray(R.array.search_title);
        switch (ActiveTab){
            case SearchFragment.CATEGORY_LIST_RESTO:
                if(!Paper.book().contains(ActiveTab))
                    Paper.book().write(ActiveTab,FilterRestoField.createDefault());
                List<FilterRestoField> listResto = Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO);
                mLocationListener.requestCity(result -> {
                    if(result!=null) {
                        String city_id=listResto.get(FilterRestoField.CITY).getSelectedItemId();
                        if(TextUtils.isEmpty(city_id)) {
                            listResto.get(FilterRestoField.CITY).setSelectedItemId(String.valueOf(result.getId()));
                            listResto.get(FilterRestoField.CITY).setSelectedFilter(String.valueOf(result.getName()));
                            Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, listResto);
                            view.showRestoFilters(listResto);
                        }
                    }
                    mLocationListener.requestLastLocation(new Callback<Location>() {
                        @Override
                        public void onResult(Location location) {
                            userLocation=location;
                            if(userLocation==null)
                                userLocation=mLocationListener.getDefaultLocation();
                        }
                    });
                });
                view.showRestoFilters(listResto);
                mListener.setTitle(titleContent[0]);

                break;
            case SearchFragment.CATEGORY_LIST_BEER:
                if(!Paper.book().contains(ActiveTab))
                    Paper.book().write(ActiveTab,FilterBeerField.createDefault());
                List<FilterBeerField> listBeer = Paper.book().read(ActiveTab);
                mLocationListener.requestCity(result -> {
                    if(result!=null) {
                        String city_id=listBeer.get(FilterBeerField.CITY).getSelectedItemId();
                        if(TextUtils.isEmpty(city_id)) {
                            listBeer.get(FilterBeerField.CITY).setSelectedItemId(String.valueOf(result.getId()));
                            listBeer.get(FilterBeerField.CITY).setSelectedFilter(String.valueOf(result.getName()));
                            Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, listBeer);
                            view.showBeerFilters(listBeer);
                        }
                    }
                });
                view.showBeerFilters(listBeer);
                mListener.setTitle(titleContent[1]);
                break;
            case CATEGORY_LIST_BREWERY:
                if(!Paper.book().contains(ActiveTab))
                    Paper.book().write(ActiveTab,FilterBreweryField.createDefault());
                view.showBreweryFilters(Paper.book().read(ActiveTab));
                mListener.setTitle(titleContent[2]);
                break;
        }

    }

    @Override
    public String getActiveTab() {
        return ActiveTab;
    }

    @Override
    public Location getUsetLocation() {
        return userLocation;
    }

}
