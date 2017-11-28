package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.brewmapp.app.environment.FilterActions;
import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.UserRepo;
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
        if (uiSettingRepo.getnActiveFragment() == 0 || uiSettingRepo.getnActiveFragment() == -1) {
            if (Paper.book().read("restoCategoryList") == null) {
                Paper.book().write("restoCategoryList", FilterRestoField.createDefault(context));
            }
            view.showRestoFilters(Paper.book().read("restoCategoryList"));

        } else {
            if (Paper.book().read("beerCategoryList") == null) {
                Paper.book().write("beerCategoryList", FilterBeerField.createDefault(context));
            }
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
        filterRestoPackage.setRestoCity(filterRestoFields.get(FilterActions.RESTO_NAME).getSelectedItemId());
        filterRestoPackage.setRestoTypes(filterRestoFields.get(FilterActions.RESTO_TYPE).getSelectedItemId());
        filterRestoPackage.setMenuBeer(filterRestoFields.get(FilterActions.BEER).getSelectedItemId());
        filterRestoPackage.setRestoKitchens(filterRestoFields.get(FilterActions.KITCHEN).getSelectedItemId());
        filterRestoPackage.setRestoAveragepriceRange(filterRestoFields.get(FilterActions.PRICE_RANGE).getSelectedItemId());
        filterRestoPackage.setRestoFeatures(filterRestoFields.get(FilterActions.FEATURES).getSelectedItemId());
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
