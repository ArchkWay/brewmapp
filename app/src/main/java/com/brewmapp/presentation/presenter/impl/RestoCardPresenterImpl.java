package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.pojo.LoadRestoPackage;
import com.brewmapp.execution.task.LoadRestoTask;
import com.brewmapp.presentation.presenter.contract.RestoCardPresenter;
import com.brewmapp.presentation.view.contract.RestoCardView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 26.10.2017.
 */

public class RestoCardPresenterImpl extends BasePresenter<RestoCardView> implements RestoCardPresenter {

    LoadRestoTask loadRestoTask;

    @Inject
    public RestoCardPresenterImpl(LoadRestoTask loadRestoTask){
        this.loadRestoTask = loadRestoTask;

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(RestoCardView restoCardView) {
        super.onAttach(restoCardView);
    }

    @Override
    public void requestResto(String idResto) {
        LoadRestoPackage loadRestoPackage=new LoadRestoPackage();
        loadRestoPackage.setId(idResto);
        loadRestoTask.execute(loadRestoPackage);

    }
}
