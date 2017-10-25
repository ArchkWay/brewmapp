package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.pojo.FindBeerPackage;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.task.FullSearchTask;
import com.brewmapp.execution.task.LoadProductTask;
import com.brewmapp.presentation.presenter.contract.AddInterestPresenter;
import com.brewmapp.presentation.view.contract.AddInterestView;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 10/20/2017.
 */

public class AddInterestPresenterImpl extends BasePresenter<AddInterestView> implements AddInterestPresenter {


    private FullSearchTask fullSearchTask;

    @Inject
    public AddInterestPresenterImpl(FullSearchTask fullSearchTask){
        this.fullSearchTask = fullSearchTask;
    }



    @Override
    public void onAttach(AddInterestView addInterestView) {
        super.onAttach(addInterestView);
    }

    @Override
    public void onDestroy() {

    }



    @Override
    public void sendQueryFullSearch(FullSearchPackage fullSearchPackage) {
        fullSearchTask.cancel();
        fullSearchTask.execute(fullSearchPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                view.appendItems(iFlexibles);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.onError();
                view.showMessage(e.getMessage(),0);
            }
        });

    }

}
