package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.container.RestoDetails;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.view.contract.RestoDetailView;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 26.10.2017.
 */

public class RestoDetailPresenterImpl extends BasePresenter<RestoDetailView> implements RestoDetailPresenter {

    LoadRestoDetailTask loadRestoDetailTask;

    @Inject
    public RestoDetailPresenterImpl(LoadRestoDetailTask loadRestoDetailTask){
        this.loadRestoDetailTask = loadRestoDetailTask;

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(RestoDetailView restoDetailView) {
        super.onAttach(restoDetailView);
    }

    @Override
    public void requestRestoDetail(String idResto) {
        LoadRestoDetailPackage loadRestoDetailPackage =new LoadRestoDetailPackage();
        loadRestoDetailPackage.setId(idResto);
        loadRestoDetailTask.execute(loadRestoDetailPackage,new SimpleSubscriber<RestoDetail>(){
            @Override
            public void onNext(RestoDetail restoDetail) {
                super.onNext(restoDetail);
                view.setModel(restoDetail);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showMessage(e.getMessage(),0);
            }
        });

    }
}
