package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerDetail;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.pojo.LoadProductPackage;
import com.brewmapp.execution.task.LoadProductTask;
import com.brewmapp.presentation.presenter.contract.BeerDetailPresenter;
import com.brewmapp.presentation.view.contract.BeerDetailView;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 30.10.2017.
 */

public class BeerDetailPresenterImpl extends BasePresenter<BeerDetailView> implements BeerDetailPresenter {


    private LoadProductTask loadProductTask;

    @Inject
    public BeerDetailPresenterImpl(LoadProductTask loadProductTask){
        this.loadProductTask= loadProductTask;

    }


    @Override
    public void requestBeerDetail(String idBeer) {
        LoadProductPackage loadProductPackage=new LoadProductPackage();
        loadProductPackage.setId(idBeer);
        loadProductTask.execute(loadProductPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                if(iFlexibles.size()>0){
                    view.setModel(new BeerDetail(((BeerInfo)iFlexibles.get(0)).getModel()));
                }
            }
        });

    }

    @Override
    public void onDestroy() {

    }
}
