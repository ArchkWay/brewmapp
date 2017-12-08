package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.pojo.AddInterestPackage;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.data.pojo.LoadProductPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.AddInterestTask;
import com.brewmapp.execution.task.LoadInterestTask;
import com.brewmapp.execution.task.LoadProductTask;
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
    private LoadProductTask loadProductTask;

    @Inject
    public InterestListPresenterImpl(LoadInterestTask loadInterestTask, AddInterestTask addInterestTask, UserRepo userRepo, RemoveInterestTask removeInterestTask,LoadProductTask loadProductTask){
        this.loadInterestTask =loadInterestTask;
        this.addInterestTask = addInterestTask;
        this.userRepo = userRepo;
        this.removeInterestTask = removeInterestTask;
        this.loadProductTask = loadProductTask;
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
                    if(isBeer(interestInfo)||isResto(interestInfo)){
                        i++;
                    }else {
                        iFlexibles.remove(i);
                    }
                }

                view.appendItems(iFlexibles);
            }

            private boolean isResto(InterestInfo interestInfo) {
                return interestInfo!=null
                        &&interestInfo.getModel()!=null
                        &&interestInfo.getModel().getRelated_model().equals(Keys.CAP_RESTO)
                        &&interestInfo.getModel().getInterest_info()!=null
                        &&interestInfo.getModel().getInterest_info().getName()!=null
                        &&interestInfo.getModel().getRelated_model().equals(loadInterestPackage.getFilterInterest());
            }

            private boolean isBeer(InterestInfo interestInfo) {

                return interestInfo!=null
                        &&interestInfo.getModel()!=null
                        &&interestInfo.getModel().getRelated_model().equals(Keys.CAP_BEER)
                        &&interestInfo.getModel().getInterest_info()!=null
                        &&interestInfo.getModel().getInterest_info().getTitle()!=null
                        &&interestInfo.getModel().getRelated_model().equals(loadInterestPackage.getFilterInterest());
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
    public void storeInterest(HashMap<Serializable,Serializable> hmAdd, HashMap<Interest,Interest> hmRemove) {
                storeAddedInterests(new ArrayList<>(hmAdd.keySet()),new ArrayList<>(hmRemove.keySet()));
    }

    @Override
    public void requestBeer(String id) {
        LoadProductPackage loadProductPackage=new LoadProductPackage();
        loadProductPackage.setId(id);
        loadProductTask.execute(loadProductPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                try {
                    view.addOneItem(((Beer)((BeerInfo)iFlexibles.iterator().next()).getModel()));
                }catch (Exception e){}

            }
        });
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

    public void storeAddedInterests(ArrayList<Serializable> serializableArrayList,ArrayList<Interest> removeArrayList) {

        if(serializableArrayList.size()>0) {
            Serializable serializableExtra=serializableArrayList.get(0);
            AddInterestPackage addInterestPackage = new AddInterestPackage();
            addInterestPackage.setId(String.valueOf(userRepo.load().getId()));
            String related_id;
            String related_model;
            if (serializableExtra instanceof Beer) {
                related_id = ((Beer) serializableExtra).getId();
                related_model = Keys.CAP_BEER;
            }else if(serializableExtra instanceof Resto) {
                related_id = String.valueOf(((Resto) serializableExtra).getId());
                related_model = Keys.CAP_RESTO;
            }else
                return;
            addInterestPackage.setRelated_id(related_id);
            addInterestPackage.setRelated_model(related_model);
            addInterestPackage.setToken(userRepo.load().getToken());

            addInterestTask.execute(addInterestPackage, new SimpleSubscriber<String>() {
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    serializableArrayList.remove(0);
                    storeAddedInterests(serializableArrayList, removeArrayList);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    view.showMessage(e.getMessage(),0);
                    view.onError();
                }
            });
        }else {
            storeRemovedInterest(removeArrayList);
        }
    }

}
