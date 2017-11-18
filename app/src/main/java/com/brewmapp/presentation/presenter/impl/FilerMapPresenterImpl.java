package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.util.Log;

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
    public void loadFilterResult(FilterRestoPackage filterRestoPackage) {
        filterRestoTask.cancel();
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
