package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.pojo.FilterRestoPackage;
import com.brewmapp.execution.task.FilterRestoTask;
import com.brewmapp.presentation.presenter.contract.FilterMapPresenter;
import com.brewmapp.presentation.view.contract.FilterMapView;

import java.util.List;

import javax.inject.Inject;

import io.paperdb.Paper;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by nlbochas on 28/10/2017.
 */

public class FilerMapPresenterImpl extends BasePresenter<FilterMapView> implements FilterMapPresenter {

    private UiSettingRepo uiSettingRepo;
    private Context context;
    private FilterRestoTask filterRestoTask;
//    private FilterBeerTask filterBeerTask; to do

    @Inject
    public FilerMapPresenterImpl(UiSettingRepo uiSettingRepo, Context context, FilterRestoTask filterRestoTask) {
        this.uiSettingRepo = uiSettingRepo;
        this.context = context;
        this.filterRestoTask = filterRestoTask;
    }

    @Override
    public void onAttach(FilterMapView filterMapView) {
        super.onAttach(filterMapView);
        Paper.init(context);
        if (Paper.book().read("restoCategoryList") == null) {
            Paper.book().write("restoCategoryList", FilterRestoField.createDefault(context));
        }
        if (Paper.book().read("beerCategoryList") == null) {
            Paper.book().write("beerCategoryList", FilterBeerField.createDefault(context));
        }
        if (uiSettingRepo.getnActiveFragment() == 0 || uiSettingRepo.getnActiveFragment() == -1) {
            view.showRestoFilters(Paper.book().read("restoCategoryList"));
        } else {
            view.showBeerFilters(Paper.book().read("beerCategoryList"));
        }
        filterMapView.setTabActive(uiSettingRepo.getnActiveTabEventFragment());
    }

    @Override
    public void onDestroy() {
        filterRestoTask.cancel();
    }

    @Override
    public void loadFilterResult(List<FilterRestoField> filterRestoFields, int specialOffer) {
        filterRestoTask.cancel();
        FilterRestoPackage filterRestoPackage = new FilterRestoPackage();
        filterRestoPackage.setRestoCity(filterRestoFields.get(FilterRestoField.NAME).getSelectedItemId());
        filterRestoPackage.setRestoTypes(filterRestoFields.get(FilterRestoField.TYPE).getSelectedItemId());
        filterRestoPackage.setMenuBeer(filterRestoFields.get(FilterRestoField.BEER).getSelectedItemId());
        filterRestoPackage.setRestoKitchens(filterRestoFields.get(FilterRestoField.KITCHEN).getSelectedItemId());
        filterRestoPackage.setRestoAveragepriceRange(filterRestoFields.get(FilterRestoField.PRICE).getSelectedItemId());
        filterRestoPackage.setRestoFeatures(filterRestoFields.get(FilterRestoField.FEATURES).getSelectedItemId());
        filterRestoPackage.setResto_discount(specialOffer);
        filterRestoTask.execute(filterRestoPackage, new SimpleSubscriber<List<FilterRestoLocation>>() {
            @Override
            public void onError(Throwable e) {
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<FilterRestoLocation> restoLocations) {
                for (FilterRestoLocation filterRestoLocation : restoLocations) {
                    Log.i("restoID:", filterRestoLocation.getLocationId());
                }
            }
        });
    }

    @Override
    public void storeTabActive(int position) {
        uiSettingRepo.setnActiveTabEventFragment(position);
    }
}
