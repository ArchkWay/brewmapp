package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Product;
import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.pojo.AddInterestPackage;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.AddInterestTask;
import com.brewmapp.execution.task.LoadInterestTask;
import com.brewmapp.execution.task.RemoveInterestTask;
import com.brewmapp.presentation.presenter.contract.InterestListPresenter;

import com.brewmapp.presentation.view.contract.InterestListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
    private RemoveInterestTask removeInterestTask;
    private UserRepo userRepo;

    @Inject
    public InterestListPresenterImpl(LoadInterestTask loadInterestTask, AddInterestTask addInterestTask, UserRepo userRepo, RemoveInterestTask removeInterestTask){
        this.loadInterestTask =loadInterestTask;
        this.addInterestTask = addInterestTask;
        this.userRepo = userRepo;
        this.removeInterestTask = removeInterestTask;
    }

    @Override
    public void onAttach(InterestListView interestListView) {
        super.onAttach(interestListView);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void requestInterests(LoadInterestPackage loadInterestPackage) {

        loadInterestTask.execute(loadInterestPackage, new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                for(int i=0;i<iFlexibles.size();){
                    InterestInfo interestInfo= (InterestInfo) iFlexibles.get(i);
                    if(interestInfo!=null
                            &&interestInfo.getModel()!=null
                            &&interestInfo.getModel().getInterest_info()!=null
                            &&interestInfo.getModel().getInterest_info().getTitle()!=null
                            &&interestInfo.getModel().getRelated_model().equals(loadInterestPackage.getFilterInterest())
                            ){
                        i++;
                    }else {
                        iFlexibles.remove(i);
                    }
                }

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
    public void storeInterest(HashMap<Product,Product> hmAdd,HashMap<Interest,Interest> hmRemove) {
                storeAddedInterests(new ArrayList<>(hmAdd.keySet()),new ArrayList<>(hmRemove.keySet()));
    }


    private void storeRemovedInterest(ArrayList<Interest> interests) {
        if(interests.size()>0){
            Interest interest=interests.get(0);
            removeInterestTask.execute(interest.getId(),new SimpleSubscriber<String>(){
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    interests.remove(0);
                    storeRemovedInterest(interests);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    view.showMessage(e.getMessage(),0);
                    view.onError();
                }
            });
        }else {
            view.refreshItems();
        }
    }

    public void storeAddedInterests(ArrayList<Serializable> serializableArrayList,ArrayList<Interest> interestArrayList) {

        if(serializableArrayList.size()>0) {
            Serializable serializableExtra=serializableArrayList.get(0);
            AddInterestPackage addInterestPackage = new AddInterestPackage();
            addInterestPackage.setId(String.valueOf(userRepo.load().getId()));
            String related_id;
            String related_model;
            if (serializableExtra instanceof Product) {
                related_id = ((Product) serializableExtra).getId();
                related_model = Keys.CAP_BEER;
            } else
                return;
            addInterestPackage.setRelated_id(related_id);
            addInterestPackage.setRelated_model(related_model);
            addInterestPackage.setToken(userRepo.load().getToken());

            addInterestTask.execute(addInterestPackage, new SimpleSubscriber<String>() {
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    serializableArrayList.remove(serializableExtra);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    view.showMessage(e.getMessage(),0);
                    view.onError();
                }
            });
        }else {
            storeRemovedInterest(interestArrayList);
        }
    }

}
