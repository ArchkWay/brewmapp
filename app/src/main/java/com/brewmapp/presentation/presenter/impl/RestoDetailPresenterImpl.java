package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.brewmapp.R;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.pojo.AddInterestPackage;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.data.pojo.SubscriptionPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.AddInterestTask;
import com.brewmapp.execution.task.DisLikeTask;
import com.brewmapp.execution.task.LikeTask;
import com.brewmapp.execution.task.LoadEventsTask;
import com.brewmapp.execution.task.LoadInterestTask;
import com.brewmapp.execution.task.LoadNewsTask;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.brewmapp.execution.task.LoadReviewsTask;
import com.brewmapp.execution.task.LoadSalesTask;
import com.brewmapp.execution.task.LoadSubscriptionsListTask;
import com.brewmapp.execution.task.RemoveInterestTask;
import com.brewmapp.execution.task.SubscriptionOffTask;
import com.brewmapp.execution.task.SubscriptionOnTask;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.view.contract.EventsView;
import com.brewmapp.presentation.view.contract.RestoDetailView;
import com.brewmapp.presentation.view.impl.activity.AddReviewRestoActivity;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
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

    private Context context;
    private LoadInterestTask loadInterestTask;
    private AddInterestTask addInterestTask;
    private RemoveInterestTask removeInterestTask;
    private LikeTask likeTask;
    private DisLikeTask disLikeTask;
    private LoadSalesTask loadSalesTask;
    private LoadNewsTask loadNewsTask;
    private LoadEventsTask loadEventsTask;
    private UiSettingRepo uiSettingRepo;
    private LoadRestoDetailTask loadRestoDetailTask;
    private RestoDetail restoDetail;
    private SubscriptionOnTask subscriptionOnTask;
    private SubscriptionOffTask subscriptionOffTask;
    private LoadSubscriptionsListTask loadSubscriptionsListTask;
    private LoadReviewsTask loadReviewsTask;
    private String IdSubscription=null;
    private HolderData holderData =new HolderData();

    public void setRestoDetail(RestoDetail restoDetail) {
        this.restoDetail = restoDetail;
    }

    @Inject
    public RestoDetailPresenterImpl(
            Context context,
            LoadRestoDetailTask loadRestoDetailTask,
            SubscriptionOnTask subscriptionOnTask,
            LoadSubscriptionsListTask loadSubscriptionsListTask,
            SubscriptionOffTask subscriptionOffTask,
            LoadReviewsTask loadReviewsTask,
            UiSettingRepo uiSettingRepo,
            LoadSalesTask loadSalesTask,
            LoadNewsTask loadNewsTask,
            LoadEventsTask loadEventsTask,
            LikeTask likeTask,
            AddInterestTask addInterestTask,
            LoadInterestTask loadInterestTask,
            RemoveInterestTask removeInterestTask){
        this.context=context;
        this.loadRestoDetailTask = loadRestoDetailTask;
        this.subscriptionOnTask = subscriptionOnTask;
        this.loadSubscriptionsListTask = loadSubscriptionsListTask;
        this.subscriptionOffTask=subscriptionOffTask;
        this.loadReviewsTask = loadReviewsTask;
        this.uiSettingRepo = uiSettingRepo;
        this.loadSalesTask= loadSalesTask;
        this.loadEventsTask= loadEventsTask;
        this.loadNewsTask= loadNewsTask;
        this.likeTask= likeTask;
        this.addInterestTask= addInterestTask;
        this.loadInterestTask= loadInterestTask;
        this.removeInterestTask=removeInterestTask;
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

    @Override
    public void startShowEventFragment(RestoDetailActivity restoDetailActivity, int tab) {
        holderData.storeUiSetting();
        Intent intent=new Intent(
                RequestCodes.ACTION_SHOW_EVENT_FRAGMENT,
                null,
                restoDetailActivity,
                MainActivity.class);
        intent.putExtra(Keys.RESTO_ID,restoDetail.getResto().getId());
        intent.putExtra(RequestCodes.INTENT_EXTRAS,tab);
        restoDetailActivity.startActivityForResult(intent,RequestCodes.REQUEST_SHOW_EVENT_FRAGMENT);
    }

    @Override
    public void restoreSetting() {
        holderData.restoreUiSetting();
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
                        loadCntSales();
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
            private void loadCntSales() {
                LoadNewsPackage loadNewsPackage=new LoadNewsPackage();
                loadNewsPackage.setMode(EventsView.MODE_SALES);
                loadNewsPackage.setRelated_model(Keys.CAP_RESTO);
                loadNewsPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                loadSalesTask.execute(loadNewsPackage,new SimpleSubscriber<List<IFlexible>>(){
                    @Override
                    public void onNext(List<IFlexible> flexibleList) {
                        super.onNext(flexibleList);
                        view.setCnt(flexibleList.size(),EventsView.MODE_SALES);
                        loadCntNews();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(e.getMessage());
                    }
                });
            }
            private void loadCntNews() {
                LoadNewsPackage loadNewsPackage=new LoadNewsPackage();
                loadNewsPackage.setMode(EventsView.MODE_NEWS);
                loadNewsPackage.setRelated_model(Keys.CAP_RESTO);
                loadNewsPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                loadNewsTask.execute(loadNewsPackage,new SimpleSubscriber<List<IFlexible>>(){
                    @Override
                    public void onNext(List<IFlexible> flexibleList) {
                        super.onNext(flexibleList);
                        view.setCnt(flexibleList.size(), EventsView.MODE_NEWS);
                        loadCntEvent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(e.getMessage());
                    }
                });

            }
            private void loadCntEvent() {
                LoadNewsPackage loadNewsPackage=new LoadNewsPackage();
                loadNewsPackage.setMode(EventsView.MODE_EVENTS);
                loadNewsPackage.setRelated_model(Keys.CAP_RESTO);
                loadNewsPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                loadEventsTask.execute(loadNewsPackage,new SimpleSubscriber<List<IFlexible>>(){
                    @Override
                    public void onNext(List<IFlexible> flexibleList) {
                        super.onNext(flexibleList);
                        view.setCnt(flexibleList.size(), EventsView.MODE_EVENTS);
                        loadFav();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(e.getMessage());
                    }
                });

            }
            private void loadFav() {
                LoadInterestPackage loadInterestPackage =new LoadInterestPackage();
                loadInterestPackage.setRelated_model(Keys.CAP_RESTO);
                loadInterestPackage.setRelated_id(String.valueOf(restoDetail.getResto().getId()));
                loadInterestTask.execute(loadInterestPackage ,new SimpleSubscriber<List<IFlexible>>(){
                    @Override
                    public void onNext(List<IFlexible> iFlexibles) {
                        super.onNext(iFlexibles);
                        holderData.setFavReso(iFlexibles.size()>0);
                        view.setFav(holderData.isFavReso());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(e.getMessage());
                    }
                });

            }
        });

    }

    @Override
    public void startShowMenu(RestoDetailActivity restoDetailActivity) {
        view.showMessage(context.getString(R.string.message_develop),0);
    }

    @Override
    public void startShowPhoto(RestoDetailActivity restoDetailActivity) {
        view.showMessage(context.getString(R.string.message_develop),0);
    }

    @Override
    public void refreshContent() {
        loadEverything(String.valueOf(restoDetail.getResto().getId()));
    }

    @Override
    public void clickLike() {
        LikeDislikePackage likeDislikePackage=new LikeDislikePackage(LikeDislikePackage.TYPE_LIKE);
        likeDislikePackage.setModel(Keys.CAP_RESTO,restoDetail.getResto().id());
        likeTask.execute(likeDislikePackage,new SimpleSubscriber<MessageResponse>(){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showMessage(context.getString(R.string.assessment_has_already_been_taken),0);
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                super.onNext(messageResponse);
                refreshContent();
            }
        });
    }

    @Override
    public void clickDisLike() {
        LikeDislikePackage likeDislikePackage=new LikeDislikePackage(LikeDislikePackage.TYPE_DISLIKE);
        likeDislikePackage.setModel(Keys.CAP_RESTO,restoDetail.getResto().id());
        likeTask.execute(likeDislikePackage,new SimpleSubscriber<MessageResponse>(){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showMessage(context.getString(R.string.assessment_has_already_been_taken),0);
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                super.onNext(messageResponse);
                refreshContent();
            }
        });

    }

    @Override
    public void clickFav() {
        //view.showMessage(context.getString(R.string.message_develop),0);
        if(holderData.isFavReso()){
            removeInterestTask.execute(String.valueOf(restoDetail.getResto().getId()),new SimpleSubscriber<String>(){
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    view.setFav(false);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    view.showMessage(e.getMessage(), 0);
                }
            });
        }else {
            AddInterestPackage addInterestPackage = new AddInterestPackage();
            addInterestPackage.setRelated_id(String.valueOf(restoDetail.getResto().getId()));
            addInterestPackage.setRelated_model(Keys.CAP_RESTO);
            addInterestTask.execute(addInterestPackage, new SimpleSubscriber<String>() {
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    view.setFav(true);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    view.showMessage(e.getMessage(), 0);
                }
            });
        }
    }


    class HolderData {
        private boolean favReso;
        private int activeFragment;
        private int activeTabFragment;

        public boolean isFavReso() {
            return favReso;
        }

        public void setFavReso(boolean favReso) {
            this.favReso = favReso;
        }

        public int getActiveFragment() {
            return activeFragment;
        }

        public void setActiveFragment(int activeFragment) {
            this.activeFragment = activeFragment;
        }

        public int getActiveTabFragment() {
            return activeTabFragment;
        }

        public void setActiveTabFragment(int activeTabFragment) {
            this.activeTabFragment = activeTabFragment;
        }

        public void storeUiSetting() {
            setActiveFragment(uiSettingRepo.getnActiveFragment());
            setActiveTabFragment(uiSettingRepo.getnActiveTabEventFragment());
        }

        public void restoreUiSetting() {
            uiSettingRepo.setActiveFragment(getActiveFragment());
            uiSettingRepo.setnActiveTabEventFragment(getActiveTabFragment());
        }
    }

}
