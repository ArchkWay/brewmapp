package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.execution.task.LoadInterestTask;
import com.brewmapp.presentation.presenter.contract.InterestListPresenter;

import com.brewmapp.presentation.view.contract.InterestListView;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 20.10.2017.
 */

public class InterestListPresenterImpl extends BasePresenter<InterestListView> implements InterestListPresenter {

    private LoadInterestTask loadInterestTask;

    @Inject
    public InterestListPresenterImpl(LoadInterestTask loadInterestTask){
        this.loadInterestTask =loadInterestTask;
    }

    @Override
    public void onAttach(InterestListView interestListView) {
        super.onAttach(interestListView);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void sendQuery(LoadInterestPackage loadInterestPackage) {
        loadInterestTask.execute(loadInterestPackage, new SimpleSubscriber<List<IFlexible>>(){
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
