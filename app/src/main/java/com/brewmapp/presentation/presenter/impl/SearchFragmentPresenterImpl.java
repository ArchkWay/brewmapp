package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.brewmapp.app.environment.Starter;
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
    public void setTabActive(int newTabActive) {
        ActiveTab=newTabActive;
        switch (ActiveTab) {
            case SearchFragment.TAB_RESTO:
                if(!Paper.book().contains(SearchFragment.CATEGORY_LIST_RESTO))
                    Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO,FilterRestoField.createDefault());
                view.showRestoFilters(Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO));
            break;
            case SearchFragment.TAB_BEER:
                if(!Paper.book().contains(SearchFragment.CATEGORY_LIST_BEER))
                    Paper.book().write(SearchFragment.CATEGORY_LIST_BEER,FilterBeerField.createDefault());
                view.showBeerFilters(Paper.book().read(SearchFragment.CATEGORY_LIST_BEER));
            break;
            case SearchFragment.TAB_BREWERY:
                if(!Paper.book().contains(SearchFragment.CATEGORY_LIST_BREWERY))
                    Paper.book().write(SearchFragment.CATEGORY_LIST_BREWERY,FilterBreweryField.createDefault());
                view.showBreweryFilters(Paper.book().read(SearchFragment.CATEGORY_LIST_BREWERY));
            break;
        }
    }

    @Override
    public void setUserLocation(Location location) {
        if(location==null)
            location=MapUtils.getDefaultLocation(context);

        if(location!=null) {
            Locale ru=MapUtils.getLocaleRu();
            if(ru!=null) {
                Geocoder geocoder = new Geocoder(context, ru);
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
                                    if(cities.size()==1) {
                                        List<FilterRestoField> list = Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO);
                                        list.get(FilterRestoField.CITY).setSelectedItemId(String.valueOf(cities.get(0).getId()));
                                        list.get(FilterRestoField.CITY).setSelectedFilter(String.valueOf(cities.get(0).getName()));
                                        Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, list);
                                        view.refreshItemRestoFilters(FilterRestoField.CITY, list);
                                    }else {
                                        Starter.InfoAboutCrashSendToServer("size of list not eq 1 (List<City>)", getClass().getCanonicalName());
                                    }
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
}
