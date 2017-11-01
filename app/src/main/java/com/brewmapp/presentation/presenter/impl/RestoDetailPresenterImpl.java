package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.data.pojo.SubscriptionPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.brewmapp.execution.task.LoadSubscriptionsListTask;
import com.brewmapp.execution.task.SubscriptionOffTask;
import com.brewmapp.execution.task.SubscriptionOnTask;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.view.contract.RestoDetailView;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 26.10.2017.
 */

public class RestoDetailPresenterImpl extends BasePresenter<RestoDetailView> implements RestoDetailPresenter {

    private LoadRestoDetailTask loadRestoDetailTask;
    private RestoDetail restoDetail;
    private SubscriptionOnTask subscriptionOnTask;
    private SubscriptionOffTask subscriptionOffTask;
    private LoadSubscriptionsListTask loadSubscriptionsListTask;
    private String IdSubscription=null;

    public void setRestoDetail(RestoDetail restoDetail) {
        this.restoDetail = restoDetail;
    }

    @Inject
    public RestoDetailPresenterImpl(LoadRestoDetailTask loadRestoDetailTask, SubscriptionOnTask subscriptionOnTask, LoadSubscriptionsListTask loadSubscriptionsListTask, SubscriptionOffTask subscriptionOffTask){
        this.loadRestoDetailTask = loadRestoDetailTask;
        this.subscriptionOnTask = subscriptionOnTask;
        this.loadSubscriptionsListTask = loadSubscriptionsListTask;
        this.subscriptionOffTask=subscriptionOffTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(RestoDetailView restoDetailView) {
        super.onAttach(restoDetailView);
        setRestoDetail(null);

    }


    @Override
    public void changeSubscription() {

        if(IdSubscription==null) {
            String id = String.valueOf(restoDetail.getResto().getId());
            SubscriptionPackage subscriptionPackage = new SubscriptionPackage();
            subscriptionPackage.setRelated_id(id);
            subscriptionPackage.setRelated_model(Keys.CAP_RESTO);
            subscriptionOnTask.execute(subscriptionPackage, new SimpleSubscriber<String>() {
                @Override
                public void onNext(String idSubscription) {
                    super.onNext(idSubscription); view.SubscriptionExist(true); IdSubscription = idSubscription;
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e); view.showMessage(e.getMessage(),0); view.commonError();
                }
            });
        }else {
            SubscriptionPackage subscriptionPackage = new SubscriptionPackage();
            subscriptionPackage.setId(IdSubscription);
            subscriptionOffTask.execute(subscriptionPackage,new SimpleSubscriber<String>(){
                @Override
                public void onNext(String s) {
                    super.onNext(s); IdSubscription=null; view.SubscriptionExist(false);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e); view.showMessage(e.getMessage(),0);
                }
            });

        }
    }

    @Override
    public void onLoadEverything(String id) {
        if(id==null || id.length()==0) {view.commonError();return;}

        LoadRestoDetailPackage loadRestoDetailPackage =new LoadRestoDetailPackage();
        loadRestoDetailPackage.setId(id);
        loadRestoDetailTask.execute(loadRestoDetailPackage,new SimpleSubscriber<RestoDetail>(){
            @Override
            public void onNext(RestoDetail restoDetail) {
                super.onNext(restoDetail);
                setRestoDetail(restoDetail);
                view.setModel(restoDetail);
                requestExistSubscriptions();
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }

            private void requestExistSubscriptions() {
                loadSubscriptionsListTask.execute(0,new SimpleSubscriber<ListResponse<Subscription>>(){
                    @Override
                    public void onNext(ListResponse<Subscription> subscriptionListResponse) {
                        super.onNext(subscriptionListResponse);
                        for (Subscription s:subscriptionListResponse.getModels())
                            if(s.getInformation().getId().equals(String.valueOf(restoDetail.getResto().getId()))) {
                                view.SubscriptionExist(true);
                                IdSubscription=s.getId();
                                return;
                            }
                        view.SubscriptionExist(false);
                        IdSubscription=null;
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);view.commonError();
                    }
                });
            }
        });

    }
}
