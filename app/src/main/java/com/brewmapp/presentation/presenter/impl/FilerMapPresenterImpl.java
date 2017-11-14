package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import com.brewmapp.data.entity.FilterField;
import com.brewmapp.presentation.presenter.contract.FilterMapPresenter;
import com.brewmapp.presentation.view.contract.FilterMapView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by nlbochas on 28/10/2017.
 */

public class FilerMapPresenterImpl extends BasePresenter<FilterMapView> implements FilterMapPresenter {

    private Context context;

    @Inject
    public FilerMapPresenterImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onAttach(FilterMapView filterMapView) {
        super.onAttach(filterMapView);
        EventBus.getDefault().register(this);
        view.showFilters(FilterField.createDefault(context));
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}
