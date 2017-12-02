package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.pojo.FilterBeerPackage;
import com.brewmapp.data.pojo.FilterRestoPackage;
import com.brewmapp.execution.task.FilterBeerTask;
import com.brewmapp.execution.task.FilterRestoTask;
import com.brewmapp.presentation.presenter.contract.FilterMapPresenter;
import com.brewmapp.presentation.view.contract.FilterMapView;

import org.greenrobot.eventbus.EventBus;

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
    private FilterBeerTask filterBeerTask;

    @Inject
    public FilerMapPresenterImpl(Context context, UiSettingRepo uiSettingRepo,
                                 FilterRestoTask filterRestoTask,
                                 FilterBeerTask filterBeerTask) {
        this.uiSettingRepo = uiSettingRepo;
        this.context = context;
        this.filterRestoTask = filterRestoTask;
        this.filterBeerTask = filterBeerTask;
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
        EventBus.getDefault().unregister(this);
        filterRestoTask.cancel();
        filterBeerTask.cancel();
    }

    @Override
    public void loadRestoCoordinates(List<FilterRestoField> filterRestoFields, int specialOffer) {
        filterRestoTask.cancel();
        FilterRestoPackage filterRestoPackage = new FilterRestoPackage();
        filterRestoPackage.setRestoCity(filterRestoFields.get(FilterRestoField.CITY).getSelectedItemId());
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
                view.showProgressBar(false);
            }

            @Override
            public void onNext(List<FilterRestoLocation> restoLocations) {
                if (restoLocations.size() == 0) {
                    view.showMessage("Не найдено ни одного заведения");
                } else {
                    view.goToMapByRestoFilter(restoLocations);
                }
            }

            @Override
            public void onComplete() {
                super.onComplete();
                view.showProgressBar(false);
            }
        });
    }

    @Override
    public void loadBeerCoordinates(List<FilterBeerField> fieldList, int craftBeer) {
        filterBeerTask.cancel();
        FilterBeerPackage filterBeerPackage = new FilterBeerPackage();
        filterBeerPackage.setBeerCountries(fieldList.get(FilterBeerField.COUNTRY).getSelectedItemId());
        filterBeerPackage.setBeerTypes(fieldList.get(FilterBeerField.TYPE).getSelectedItemId());
        filterBeerPackage.setBeerStrengthes(fieldList.get(FilterBeerField.POWER).getSelectedItemId());
        filterBeerPackage.setBeerPacks(fieldList.get(FilterBeerField.BEER_PACK).getSelectedItemId());
        filterBeerPackage.setBeerBreweries(fieldList.get(FilterBeerField.BREWERY).getSelectedItemId());
        filterBeerPackage.setCraft(craftBeer);
        filterBeerPackage.setBeerDensity(fieldList.get(FilterBeerField.DENSITY).getSelectedItemId());
//        filterBeerPackage.setBeerFiltered(fieldList.get(FilterBeerField.COUNTRY).getSelectedItemId());

        filterBeerPackage.setBeerAveragepriceRange(fieldList.get(FilterBeerField.PRICE_BEER).getSelectedItemId());
        filterBeerPackage.setBeerColors(fieldList.get(FilterBeerField.COLOR).getSelectedItemId());
        filterBeerPackage.setBeerFragrances(fieldList.get(FilterBeerField.SMELL).getSelectedItemId());
        filterBeerPackage.setBeerTastes(fieldList.get(FilterBeerField.TASTE).getSelectedItemId());
        filterBeerPackage.setBeerAftertastes(fieldList.get(FilterBeerField.AFTER_TASTE).getSelectedItemId());
        filterBeerPackage.setBeerIBU(fieldList.get(FilterBeerField.IBU).getSelectedItemId());
        filterBeerTask.execute(filterBeerPackage, new SimpleSubscriber<List<FilterRestoLocation>>() {
            @Override
            public void onError(Throwable e) {
                showError(e.getMessage());
                view.showProgressBar(false);
            }

            @Override
            public void onNext(List<FilterRestoLocation> restoLocations) {
                if (restoLocations.size() == 0) {
                    view.showMessage("Не найдено ни одного заведения");
                } else {
                    view.goToMapByRestoFilter(restoLocations);
                }
            }

            @Override
            public void onComplete() {
                super.onComplete();
                view.showProgressBar(false);
            }
        });
    }

    @Override
    public void storeTabActive(int position) {
        uiSettingRepo.setnActiveTabEventFragment(position);
    }
}
