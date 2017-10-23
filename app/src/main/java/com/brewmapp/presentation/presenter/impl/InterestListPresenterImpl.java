package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Product;
import com.brewmapp.data.pojo.AddInterestPackage;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.AddInterestTask;
import com.brewmapp.execution.task.LoadInterestTask;
import com.brewmapp.presentation.presenter.contract.InterestListPresenter;

import com.brewmapp.presentation.view.contract.InterestListView;

import java.io.Serializable;
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
    private AddInterestTask addInterestTask;
    private UserRepo userRepo;

    @Inject
    public InterestListPresenterImpl(LoadInterestTask loadInterestTask, AddInterestTask addInterestTask, UserRepo userRepo){
        this.loadInterestTask =loadInterestTask;
        this.addInterestTask = addInterestTask;
        this.userRepo = userRepo;
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

    @Override
    public void addInterest(Serializable serializableExtra) {
        AddInterestPackage addInterestPackage=new AddInterestPackage();
        addInterestPackage.setId(String.valueOf(userRepo.load().getId()));
        String related_id;
        String related_model;
        if(serializableExtra instanceof Product) {
            related_id = ((Product) serializableExtra).getId();
            related_model = Keys.CAP_BEER;
        }else
            return;

        addInterestPackage.setRelated_id(related_id);
        addInterestPackage.setRelated_model(related_model);
        addInterestPackage.setToken(userRepo.load().getToken());
        addInterestTask.execute(addInterestPackage,new SimpleSubscriber<String>(){

            @Override
            public void onNext(String s) {
                super.onNext(s);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
