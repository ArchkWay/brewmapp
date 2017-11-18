package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.brewmapp.app.environment.FilterActions;
import com.brewmapp.data.entity.FilterField;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.pojo.FilterRestoPackage;
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

    private Context context;
    private FilterRestoTask filterRestoTask;

    @Inject
    public FilerMapPresenterImpl(Context context, FilterRestoTask filterRestoTask) {
        this.context = context;
        this.filterRestoTask = filterRestoTask;
    }

    @Override
    public void onAttach(FilterMapView filterMapView) {
        super.onAttach(filterMapView);
    }

    @Override
    public void onDestroy() {
        filterRestoTask.cancel();
    }

    @Override
    public void loadFilterResult(List<FilterField> filterFields, int specialOffer) {
        filterRestoTask.cancel();
        FilterRestoPackage filterRestoPackage = new FilterRestoPackage();
        filterRestoPackage.setRestoCity(filterFields.get(FilterActions.RESTO_NAME).getSelectedItemId());
        filterRestoPackage.setRestoTypes(filterFields.get(FilterActions.RESTO_TYPE).getSelectedItemId());
        filterRestoPackage.setMenuBeer(filterFields.get(FilterActions.BEER).getSelectedItemId());
        filterRestoPackage.setRestoKitchens(filterFields.get(FilterActions.KITCHEN).getSelectedItemId());
        filterRestoPackage.setRestoAveragepriceRange(filterFields.get(FilterActions.PRICE_RANGE).getSelectedItemId());
        filterRestoPackage.setRestoFeatures(filterFields.get(FilterActions.FEATURES).getSelectedItemId());
        filterRestoPackage.setResto_discount(specialOffer);
        filterRestoTask.execute(filterRestoPackage, new SimpleSubscriber<List<FilterRestoLocation>>() {
            @Override
            public void onError(Throwable e) {
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<FilterRestoLocation> restoLocations) {
                for (FilterRestoLocation filterRestoLocation : restoLocations) {
                    Log.i("id", filterRestoLocation.getLocationId());
                }
            }
        });
    }
}
