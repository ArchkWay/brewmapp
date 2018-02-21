package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.task.LoadCityTask;
import com.brewmapp.presentation.presenter.contract.SearchFragmentPresenter;
import com.brewmapp.presentation.view.contract.SearchAllView;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;
import com.brewmapp.utils.events.markerCluster.MapUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.paperdb.Paper;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SearchFragmentPresenterImpl extends BasePresenter<SearchAllView> implements SearchFragmentPresenter {

    private UiSettingRepo uiSettingRepo;
    private Context context;
    private LoadCityTask loadCityTask;
    private int ActiveTab;

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
    public void setTabActive(int position) {
        ActiveTab=position;
        switch (position) {
            case SearchFragment.TAB_RESTO: {
                List<FilterRestoField> list=Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO);
                if(list==null)
                {
                    list = FilterRestoField.createDefault();
                    Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, list);
                }
                view.showRestoFilters(list);
            }break;
            case SearchFragment.TAB_BEER: {
                List<FilterBeerField> list=Paper.book().read(SearchFragment.CATEGORY_LIST_BEER);
                if(list==null)
                {
                    list = FilterBeerField.createDefault(context);
                    Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, list);
                }
                view.showBeerFilters(list);
            }break;
            case SearchFragment.TAB_BREWERY:
                List<FilterBreweryField> list=Paper.book().read(SearchFragment.CATEGORY_LIST_BREWERY);
                if(list==null)
                {
                    list = FilterBreweryField.createDefault(context);
                    Paper.book().write(SearchFragment.CATEGORY_LIST_BREWERY, list);
                }
                view.showBreweryFilters(list);
                break;
        }
    }


    @Override
    public void saveRestoFilterChanges(List<FilterRestoField> fields) {
        new Thread(() -> Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, fields)).start();
    }

    @Override
    public void saveBeerFilterChanges(List<FilterBeerField> fields) {
        new Thread(() -> Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, fields)).start();
    }

    @Override
    public void saveBreweryFilterChanges(List<FilterBreweryField> fields) {
        new Thread(() -> Paper.book().write(SearchFragment.CATEGORY_LIST_BREWERY, fields)).start();
    }

    @Override
    public void setUserLocation(Location location) {
        if(location==null)
            location=MapUtils.getDefaultLocation(location, context);

        if(location!=null) {
            Geocoder geocoder = new Geocoder(context, new Locale("RU", "ru"));
            try {
                List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                loadCityTask.cancel();
                GeoPackage geoPackage = new GeoPackage();
                geoPackage.setCityName(list.get(0).getLocality());
                loadCityTask.execute(geoPackage, new SimpleSubscriber<List<City>>() {
                    @Override
                    public void onNext(List<City> cities) {
                        super.onNext(cities);
                        switch (ActiveTab) {
                            case SearchFragment.TAB_RESTO: {
                                List<FilterRestoField> list = Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO);
                                list.get(FilterRestoField.CITY).setSelectedItemId(String.valueOf(cities.get(0).getId()));
                                list.get(FilterRestoField.CITY).setSelectedFilter(String.valueOf(cities.get(0).getName()));
                                Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, list);
                                view.refreshItemRestoFilters(FilterRestoField.CITY, list);
                            }
                            break;
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
