package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;

import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.data.pojo.SubscriptionPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.brewmapp.execution.task.LoadReviewsTask;
import com.brewmapp.execution.task.LoadSubscriptionsListTask;
import com.brewmapp.execution.task.SubscriptionOffTask;
import com.brewmapp.execution.task.SubscriptionOnTask;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.view.contract.RestoDetailView;
import com.brewmapp.presentation.view.impl.activity.AddReviewRestoActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

/**
 * Created by Kras on 26.10.2017.
 */

public class RestoDetailPresenterImpl extends BasePresenter<RestoDetailView> implements RestoDetailPresenter {

    private LoadRestoDetailTask loadRestoDetailTask;
    private RestoDetail restoDetail;
    private SubscriptionOnTask subscriptionOnTask;
    private SubscriptionOffTask subscriptionOffTask;
    private LoadSubscriptionsListTask loadSubscriptionsListTask;
    private LoadReviewsTask loadReviewsTask;
    private String IdSubscription=null;

    public void setRestoDetail(RestoDetail restoDetail) {
        this.restoDetail = restoDetail;
    }

    @Inject
    public RestoDetailPresenterImpl(LoadRestoDetailTask loadRestoDetailTask, SubscriptionOnTask subscriptionOnTask, LoadSubscriptionsListTask loadSubscriptionsListTask, SubscriptionOffTask subscriptionOffTask,LoadReviewsTask loadReviewsTask){
        this.loadRestoDetailTask = loadRestoDetailTask;
        this.subscriptionOnTask = subscriptionOnTask;
        this.loadSubscriptionsListTask = loadSubscriptionsListTask;
        this.subscriptionOffTask=subscriptionOffTask;
        this.loadReviewsTask = loadReviewsTask;
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
    public void startAddReviewRestoActivity(RestoDetailActivity restoDetailActivity) {
        Intent intent=new Intent(restoDetailActivity, AddReviewRestoActivity.class);
        intent.putExtra(Keys.RESTO_ID,restoDetail);
        restoDetailActivity.startActivityForResult(intent, RequestCodes.REQUEST_CODE_REVIEW_RESTO);
    }

    @Override
    public void parseIntent(Intent intent) {

        String id;
        try {
            id=((Interest) intent.getSerializableExtra(RESTO_ID)).getInterest_info().getId();
            if(id==null || id.length()==0) {view.commonError();return;}
        } catch (Exception e){
            view.commonError(e.getMessage());return;
        }

        loadEverything(id);

    }

    private void loadEverything(String id) {
        //load RestoDetail
        LoadRestoDetailPackage loadRestoDetailPackage =new LoadRestoDetailPackage();
        loadRestoDetailPackage.setId(id);
        loadRestoDetailTask.execute(loadRestoDetailPackage,new SimpleSubscriber<RestoDetail>(){
            @Override
            public void onNext(RestoDetail restoDetail) {
                super.onNext(restoDetail);
                setRestoDetail(restoDetail);
                view.setModel(restoDetail);
                loadReviews();
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }

            private void loadReviews() {
                ReviewPackage reviewPackage=new ReviewPackage();
                reviewPackage.setRelated_model(Keys.CAP_RESTO);
                reviewPackage.setRelated_id(String.valueOf(restoDetail.getResto().getId()));
                loadReviewsTask.execute(reviewPackage,new SimpleSubscriber<List<IFlexible>>(){
                    @Override
                    public void onNext(List<IFlexible> iFlexibles ) {
                        super.onNext(iFlexibles);
                        view.setReviews(iFlexibles);
                        loadSubscriptions();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(e.getMessage());
                    }
                });

            }
            private void loadSubscriptions() {
                SubscriptionPackage subscriptionPackage=new SubscriptionPackage();
                subscriptionPackage.setRelated_model(Keys.CAP_RESTO);
                subscriptionPackage.setRelated_id(String.valueOf(restoDetail.getResto().getId()));
                loadSubscriptionsListTask.execute(subscriptionPackage,new SimpleSubscriber<ListResponse<Subscription>>(){
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
