package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.AverageEvaluation;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.pojo.AddInterestPackage;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.data.pojo.RestoAverageEvaluationPackage;
import com.brewmapp.data.pojo.SubscriptionPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import com.brewmapp.execution.task.containers.contract.ContainerTasks;
import com.brewmapp.execution.task.AddInterestTask;
import com.brewmapp.execution.task.LoadRestoAverageEvaluationTask;
import com.brewmapp.execution.task.LoadEventsTask;
import com.brewmapp.execution.task.LoadInterestTask;
import com.brewmapp.execution.task.LoadNewsTask;
import com.brewmapp.execution.task.LoadRestoDetailTask;
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
import com.brewmapp.presentation.view.impl.activity.PhotoSliderActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;
import com.brewmapp.presentation.view.impl.widget.AddPhotoSliderView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

/**
 * Created by Kras on 26.10.2017.
 */

public class RestoDetailPresenterImpl extends BasePresenter<RestoDetailView> implements RestoDetailPresenter{


    private ContainerTasks containerTasks;
    private Context context;
    private LoadRestoAverageEvaluationTask loadRestoAverageEvaluationTask;
    private LoadInterestTask loadInterestTask;
    private AddInterestTask addInterestTask;
    private RemoveInterestTask removeInterestTask;
    private LoadSalesTask loadSalesTask;
    private LoadNewsTask loadNewsTask;
    private LoadEventsTask loadEventsTask;
    private UiSettingRepo uiSettingRepo;
    private LoadRestoDetailTask loadRestoDetailTask;
    private RestoDetail restoDetail;
    private SubscriptionOnTask subscriptionOnTask;
    private SubscriptionOffTask subscriptionOffTask;
    private LoadSubscriptionsListTask loadSubscriptionsListTask;
    private String IdSubscription=null;
    private TempDataHolder tempDataHolder =new TempDataHolder();

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
            UiSettingRepo uiSettingRepo,
            LoadSalesTask loadSalesTask,
            LoadNewsTask loadNewsTask,
            LoadEventsTask loadEventsTask,
            AddInterestTask addInterestTask,
            LoadInterestTask loadInterestTask,
            RemoveInterestTask removeInterestTask,
            LoadRestoAverageEvaluationTask loadRestoAverageEvaluationTask,
            ContainerTasks containerTasks){
        this.context=context;
        this.loadRestoDetailTask = loadRestoDetailTask;
        this.subscriptionOnTask = subscriptionOnTask;
        this.loadSubscriptionsListTask = loadSubscriptionsListTask;
        this.subscriptionOffTask=subscriptionOffTask;
        this.uiSettingRepo = uiSettingRepo;
        this.loadSalesTask= loadSalesTask;
        this.loadEventsTask= loadEventsTask;
        this.loadNewsTask= loadNewsTask;
        this.addInterestTask= addInterestTask;
        this.loadInterestTask= loadInterestTask;
        this.removeInterestTask=removeInterestTask;
        this.loadRestoAverageEvaluationTask=loadRestoAverageEvaluationTask;
        this.containerTasks = containerTasks;
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
        restoDetailActivity.startActivityForResult(intent, RequestCodes.REQUEST_CODE_REVIEW);
    }

    @Override
    public void parseIntent(Intent intent) {
        //stub
        restoDetail=new RestoDetail();
        restoDetail.setResto(new Resto());
        try {
            String id=((Interest) intent.getSerializableExtra(RESTO_ID)).getInterest_info().getId();
            if(id==null || id.length()==0) {view.commonError();return;}
            restoDetail.getResto().setId(Integer.valueOf(id));
        } catch (Exception e){
            view.commonError(e.getMessage());return;
        }

//        try {
//            view.scrollTo(Integer.valueOf(intent.getAction()));
//        }catch (Exception e){}

        refreshContent(Actions.MODE_REFRESH_ALL);

    }

    @Override
    public void startShowEventFragment(RestoDetailActivity restoDetailActivity, int tab) {
        tempDataHolder.storeUiSetting();
        //set mode EventsFragment
        Intent intent=new Intent(
                MainActivity.MODE_ONLY_EVENT_FRAGMENT,
                null,
                restoDetailActivity,
                MainActivity.class);
        intent.putExtra(RequestCodes.INTENT_EXTRAS,tab);

        //set arguments EventsFragment
        intent.putExtra(Keys.RELATED_ID,String.valueOf(restoDetail.getResto().getId()));
        intent.putExtra(Keys.RELATED_MODEL,Keys.CAP_RESTO);

        restoDetailActivity.startActivityForResult(intent,RequestCodes.REQUEST_SHOW_EVENT_FRAGMENT);
    }

    @Override
    public void restoreSetting() {
        tempDataHolder.restoreUiSetting();
    }

    private void loadData(int mode) {

        class LoadersAttributes {
            public LoadersAttributes(int mode) {
                loadRestoDetails(mode);
            }
            private void loadRestoDetails(int mode){
                if(mode== Actions.MODE_REFRESH_ALL ||mode== Actions.MODE_REFRESH_ONLY_LIKE) {
                    LoadRestoDetailPackage loadRestoDetailPackage =new LoadRestoDetailPackage();
                    loadRestoDetailPackage.setId(String.valueOf(restoDetail.getResto().getId()));
                    loadRestoDetailTask.execute(loadRestoDetailPackage, new SimpleSubscriber<RestoDetail>() {
                        @Override public void onNext(RestoDetail restoDetail) {super.onNext(restoDetail);setRestoDetail(restoDetail);view.setModel(restoDetail,mode); loadReviews(mode);}
                        @Override public void onError(Throwable e) {super.onError(e); view.commonError(e.getMessage()); }
                    });
                }else {
                    loadReviews(mode);
                }
            }
            private void loadReviews(int mode) {
                if(mode== Actions.MODE_REFRESH_ALL)
                    containerTasks.loadReviewsTask(Keys.CAP_RESTO,restoDetail.getResto().getId(),new SimpleSubscriber<List<IFlexible>>(){
                        @Override public void onNext(List<IFlexible> iFlexibles ) {super.onNext(iFlexibles);view.setReviews(iFlexibles);loadSubscriptions(mode);}
                        @Override public void onError(Throwable e) {super.onError(e);view.commonError(e.getMessage());}
                    });
                else
                    loadSubscriptions(mode);
            }
            private void loadSubscriptions(int mode) {
                if(mode== Actions.MODE_REFRESH_ALL) {
                    SubscriptionPackage subscriptionPackage=new SubscriptionPackage();
                    subscriptionPackage.setRelated_model(Keys.CAP_RESTO);
                    subscriptionPackage.setRelated_id(String.valueOf(restoDetail.getResto().getId()));
                    loadSubscriptionsListTask.execute(subscriptionPackage,new SimpleSubscriber<ListResponse<Subscription>>(){
                        @Override
                        public void onNext(ListResponse<Subscription> subscriptionListResponse) {
                            super.onNext(subscriptionListResponse);
                            loadCntSales(mode);
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
                    }else {
                        loadCntSales(mode);

                    }
            }
            private void loadCntSales(int mode) {
                if(mode== Actions.MODE_REFRESH_ALL) {
                    LoadNewsPackage loadNewsPackage=new LoadNewsPackage();
                    loadNewsPackage.setMode(EventsView.MODE_SALES);
                    loadNewsPackage.setRelated_model(Keys.CAP_RESTO);
                    loadNewsPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                    loadSalesTask.execute(loadNewsPackage,new SimpleSubscriber<List<IFlexible>>(){
                        @Override
                        public void onNext(List<IFlexible> flexibleList) {
                            super.onNext(flexibleList);
                            view.setCnt(flexibleList.size(),EventsView.MODE_SALES);
                            loadCntNews(mode);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            view.commonError(e.getMessage());
                        }
                    });
                }else {
                    loadCntNews(mode);

                }
            }
            private void loadCntNews(int mode) {
                if(mode== Actions.MODE_REFRESH_ALL) {
                    LoadNewsPackage loadNewsPackage=new LoadNewsPackage();
                    loadNewsPackage.setMode(EventsView.MODE_NEWS);
                    loadNewsPackage.setRelated_model(Keys.CAP_RESTO);
                    loadNewsPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                    loadNewsTask.execute(loadNewsPackage,new SimpleSubscriber<List<IFlexible>>(){
                        @Override
                        public void onNext(List<IFlexible> flexibleList) {
                            super.onNext(flexibleList);
                            view.setCnt(flexibleList.size(), EventsView.MODE_NEWS);
                            loadCntEvent(mode);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            view.commonError(e.getMessage());
                        }
                    });
                }else {
                    loadCntEvent(mode);

                }

            }
            private void loadCntEvent(int mode) {
                if(mode== Actions.MODE_REFRESH_ALL) {
                    LoadNewsPackage loadNewsPackage=new LoadNewsPackage();
                    loadNewsPackage.setMode(EventsView.MODE_EVENTS);
                    loadNewsPackage.setRelated_model(Keys.CAP_RESTO);
                    loadNewsPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                    loadEventsTask.execute(loadNewsPackage,new SimpleSubscriber<List<IFlexible>>(){
                        @Override
                        public void onNext(List<IFlexible> flexibleList) {
                            super.onNext(flexibleList);
                            view.setCnt(flexibleList.size(), EventsView.MODE_EVENTS);
                            loadFav(mode);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            view.commonError(e.getMessage());
                        }
                    });
                }else {
                    loadFav(mode);

                }
            }
            private void loadFav(int mode) {
                if(mode== Actions.MODE_REFRESH_ALL) {
                    LoadInterestPackage loadInterestPackage =new LoadInterestPackage();
                    loadInterestPackage.setRelated_model(Keys.CAP_RESTO);
                    loadInterestPackage.setRelated_id(String.valueOf(restoDetail.getResto().getId()));
                    loadInterestTask.execute(loadInterestPackage ,new SimpleSubscriber<List<IFlexible>>(){
                        @Override
                        public void onNext(List<IFlexible> iFlexibles) {
                            super.onNext(iFlexibles);
                            if(iFlexibles.size()==1){
                                try {
                                    tempDataHolder.setId_interest(((InterestInfo) iFlexibles.get(0)).getModel().getId());
                                    tempDataHolder.setFavResto(true);
                                    view.setFav(true);
                                }catch (Exception e){
                                    view.commonError(e.getMessage());
                                }
                            }else if(iFlexibles.size()==0){
                                tempDataHolder.setFavResto(false);
                                view.setFav(false);
                            }else {
                                view.commonError();
                            }

                            loadAvegagEvaluation(mode);
                        }
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            view.commonError(e.getMessage());
                        }
                    });
                }else {
                    loadAvegagEvaluation(mode);

                }

            }
            private void loadAvegagEvaluation(int mode) {
                if(mode== Actions.MODE_REFRESH_ALL) {
                RestoAverageEvaluationPackage restoAverageEvaluationPackage=new RestoAverageEvaluationPackage();
                restoAverageEvaluationPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                restoAverageEvaluationPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                loadRestoAverageEvaluationTask.execute(restoAverageEvaluationPackage,new SimpleSubscriber<List<AverageEvaluation>>(){
                    @Override
                    public void onNext(List<AverageEvaluation> evaluations) {
                        super.onNext(evaluations);
                        view.AverageEvaluation(evaluations);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(e.getMessage());
                    }
                });
                }else {
                    //THE END
                }
            }
        }

        new LoadersAttributes(mode);

    }

    @Override
    public void startShowMenu(RestoDetailActivity restoDetailActivity) {
        view.showMessage(context.getString(R.string.message_develop),0);
    }

    @Override
    public void startShowPhoto(RestoDetailActivity restoDetailActivity, ArrayList<String> photosResto) {

        Intent intent = new Intent(restoDetailActivity, PhotoSliderActivity.class);
        String[] urls = new String[photosResto.size()];
        photosResto.toArray(urls);
        intent.putExtra(Keys.PHOTOS, urls);
        restoDetailActivity.startActivity(intent);
    }

    @Override
    public void refreshContent(int mode) {
        loadData(mode);
    }

    @Override
    public void clickLikeDislike(int type_like) {
        containerTasks.clickLikeDislike(Keys.CAP_RESTO,restoDetail.getResto().id(),type_like,new SimpleSubscriber<MessageResponse>(){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showMessage(context.getString(R.string.assessment_has_already_been_taken),0);
            }
            @Override
            public void onNext(MessageResponse messageResponse) {
                super.onNext(messageResponse);
                refreshContent(Actions.MODE_REFRESH_ONLY_LIKE);
            }
        });
    }

    @Override
    public void clickFav() {
        //view.showMessage(context.getString(R.string.message_develop),0);
        if(tempDataHolder.isFavResto()){
            removeInterestTask.execute(String.valueOf(tempDataHolder.getId_interest()),new SimpleSubscriber<String>(){
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    tempDataHolder.setFavResto(false);
                    tempDataHolder.setId_interest(null);
                    view.setFav(tempDataHolder.isFavResto());
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
                    tempDataHolder.setId_interest(s);
                    tempDataHolder.setFavResto(true);
                    view.setFav(tempDataHolder.isFavResto());
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    view.showMessage(e.getMessage(), 0);
                }
            });
        }
    }

    @Override
    public void startMapFragment(RestoDetailActivity restoDetailActivity) {
        RestoLocation restoLocation=new RestoLocation(
                restoDetail.getResto().getId(),
                restoDetail.getResto().getName()==null?"":restoDetail.getResto().getName(),
                restoDetail.getResto().getLocation().getLocation().getLon(),
                restoDetail.getResto().getLocation().getLocation().getLat()
        );

        tempDataHolder.storeUiSetting();
        Intent intent=new Intent(MainActivity.MODE_ONLY_MAP_FRAGMENT,null,restoDetailActivity,MainActivity.class);
        intent.putExtra(Keys.LOCATION,restoLocation);
        restoDetailActivity.startActivityForResult(intent,RequestCodes.REQUEST_MAP_FRAGMENT);

    }

    @Override
    public void loadAllPhoto(SliderLayout sliderLayout) {
        class LoadPhotoResto extends BaseNetworkTask<String, List<Photo>> {

            public LoadPhotoResto(MainThread mainThread, Executor executor, Api api) {
                super(mainThread, executor, api);
            }

            @Override
            protected Observable<List<Photo>> prepareObservable(String resto_id) {
                return Observable.create(subscriber -> {
                    try {
                        WrapperParams wrapperParams=new WrapperParams(Wrappers.PHOTO);
                        wrapperParams.addParam(Keys.RELATED_MODEL,Keys.CAP_RESTO);
                        wrapperParams.addParam(Keys.RELATED_ID,resto_id);
                        ListResponse<Photo> listResponse= executeCall(getApi().loadPhotosResto(wrapperParams));
                        subscriber.onNext(listResponse.getModels());
                        subscriber.onComplete();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        }
        new LoadPhotoResto(
                BeerMap.getAppComponent().mainThread(),
                BeerMap.getAppComponent().executor(),
                BeerMap.getAppComponent().api()
        ).execute(
                String.valueOf(restoDetail.getResto().getId()),
                new SimpleSubscriber<List<Photo>>(){
                    @Override
                    public void onNext(List<Photo> photos) {
                        super.onNext(photos);
                        Iterator<Photo> iterator=photos.iterator();
                        while (iterator.hasNext())
                            try {
                                Photo photo=iterator.next();
                                String strThumbUrl  =photo.getThumb().getThumbUrl();
                                String strUrl       =photo.getThumb().getUrl();
                                sliderLayout.addSlider(
                                        new DefaultSliderView(sliderLayout.getContext())
                                                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                                .image(strThumbUrl)
                                                .setOnSliderClickListener(slider1 -> {
                                                    //showMessage(sliderLayout.getContext().getString(R.string.message_develop));

                                                    Intent intent = new Intent(sliderLayout.getContext(), PhotoSliderActivity.class);
                                                    String[] urls = {strUrl};
                                                    intent.putExtra(Keys.PHOTOS, urls);
                                                    sliderLayout.getContext().startActivity(intent);

                                                })
                                );
                            }catch (Exception e){};

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }
        );

    }

    class TempDataHolder {
        private boolean favResto;
        private String id_interest;
        private int activeFragment;
        private int activeTabFragment;

        public String getId_interest() {
            return id_interest;
        }

        public void setId_interest(String id_interest) {
            this.id_interest = id_interest;
        }

        public boolean isFavResto() {
            return favResto;
        }

        public void setFavResto(boolean favResto) {
            this.favResto = favResto;
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
