package com.brewmapp.presentation.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.AverageEvaluation;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.data.entity.Sales;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.container.Events;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.entity.wrapper.InterestInfoByUsers;
import com.brewmapp.data.pojo.AddInterestPackage;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.data.pojo.LoadPhotoPackage;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.data.pojo.NewPhotoPackage;
import com.brewmapp.data.pojo.RestoAverageEvaluationPackage;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.data.pojo.SubscriptionPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.LoadUsersByInterestTask;
import com.brewmapp.execution.task.LoadUsersTask;
import com.brewmapp.execution.task.UploadPhotoTask;
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
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

/**
 * Created by Kras on 26.10.2017.
 */

public class RestoDetailPresenterImpl extends BasePresenter<RestoDetailView>
        implements
        RestoDetailPresenter
{

    //region Private
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
    private LoadUsersTask loadUsersTask;
    private LoadUsersByInterestTask loadUsersByInterestTask;
    private Interest interest;
    private UserRepo userRepo;
    private UploadPhotoTask uploadPhotoTask;
    private ResultReceiver resultReceiver;
    //endregion

    //region Inject
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
            ContainerTasks containerTasks,
            LoadUsersTask loadUsersTask,
            LoadUsersByInterestTask loadUsersByInterestTask,
            UserRepo userRepo,
            UploadPhotoTask uploadPhotoTask){
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
        this.loadUsersTask = loadUsersTask;
        this.loadUsersByInterestTask = loadUsersByInterestTask;
        this.userRepo = userRepo;
        this.uploadPhotoTask = uploadPhotoTask;
    }
    //endregion

    //region Impl BasePresenter
    @Override
    public void onDestroy() {
        loadRestoDetailTask.cancel();
        containerTasks.cancelTasks();
        loadSubscriptionsListTask.cancel();
        loadSalesTask.cancel();
        loadNewsTask.cancel();
        loadEventsTask.cancel();
        loadInterestTask.cancel();
        loadRestoAverageEvaluationTask.cancel();
        loadInterestTask.cancel();
        sendResultReceiver(Actions.ACTION_ACTIVITY_DESTROY);
    }

    @Override
    public void onAttach(RestoDetailView restoDetailView) {
        super.onAttach(restoDetailView);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //endregion

    //region Impl RestoDetailPresenter
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
    public void parseIntent(Intent intent) {
        //stub
        restoDetail=new RestoDetail();
        restoDetail.setResto(new Resto());
        try {
            interest=(Interest) intent.getSerializableExtra(RESTO_ID);
            restoDetail.getResto().setId(Integer.valueOf(interest.getInterest_info().getId()));
        } catch (Exception e){
            view.commonError(e.getMessage());return;
        }
        resultReceiver=intent.getParcelableExtra(context.getString(R.string.key_blur));
        if(resultReceiver!=null)
            view.activityMoveToBack(true);


        refreshContent(Actions.MODE_REFRESH_ALL);

    }

    @Override
    public void startShowEventFragment(RestoDetailActivity restoDetailActivity, int tab) {
        tempDataHolder.storeUiSetting();
        Starter.MainActivity(
                restoDetailActivity,
                MainActivity.MODE_EVENT_FRAGMENT_WITHOUT_TABS,
                tab,
                String.valueOf(restoDetail.getResto().getId()),
                Keys.CAP_RESTO
        );

//        Intent intent=new Intent(
//                MainActivity.MODE_EVENT_FRAGMENT_WITHOUT_TABS,
//                null,
//                restoDetailActivity,
//                MainActivity.class);
//        intent.putExtra(RequestCodes.INTENT_EXTRAS,tab);
//
//        intent.putExtra(Keys.RELATED_ID,String.valueOf(restoDetail.getResto().getId()));
//        intent.putExtra(Keys.RELATED_MODEL,Keys.CAP_RESTO);
//
//        restoDetailActivity.startActivityForResult(intent,RequestCodes.REQUEST_SHOW_EVENT_FRAGMENT);
    }

    @Override
    public void restoreSetting() {
        tempDataHolder.restoreUiSetting();
    }

    @Override
    public void startShowMenu(RestoDetailActivity restoDetailActivity) {
        view.showMessage(context.getString(R.string.message_develop),0);
    }

    @Override
    public void refreshContent(int mode) {
        new LoadersAttributes(mode);
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
                String.valueOf(restoDetail.getResto().getId()),
                0,
                restoDetail.getResto().getName()==null?"":restoDetail.getResto().getName(),
                restoDetail.getResto().getLocation().getLocation().getLat(),
                restoDetail.getResto().getLocation().getLocation().getLon()
        );

        tempDataHolder.storeUiSetting();
        ArrayList<RestoLocation> arrayList=new ArrayList<>();
        arrayList.add(restoLocation);
        Starter.MainActivity(restoDetailActivity,MainActivity.MODE_MAP_FRAGMENT,arrayList);

    }

    @Override
    public void loadAllPhoto(SimpleSubscriber<List<Photo>> simpleSubscriber) {


        class LoadPhotoResto extends BaseNetworkTask<LoadPhotoPackage, List<Photo>> {

            public LoadPhotoResto(MainThread mainThread, Executor executor, Api api) {
                super(mainThread, executor, api);
            }

            @Override
            protected Observable<List<Photo>> prepareObservable(LoadPhotoPackage loadPhotoPackage) {
                return Observable.create(subscriber -> {
                    try {
                        WrapperParams wrapperParams=new WrapperParams(Wrappers.PHOTO);
                        if(loadPhotoPackage.getRelated_id()!=null)
                            wrapperParams.addParam(Keys.RELATED_ID,loadPhotoPackage.getRelated_id());
                        if(loadPhotoPackage.getRelated_model()!=null)
                            wrapperParams.addParam(Keys.RELATED_MODEL,loadPhotoPackage.getRelated_model());

                        ListResponse<Photo> listResponse= executeCall(getApi().loadPhotosResto(wrapperParams));
                        subscriber.onNext(listResponse.getModels());
                        subscriber.onComplete();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        }

        LoadPhotoPackage loadPhotoPackage=new LoadPhotoPackage();
        loadPhotoPackage.setRelated_id(String.valueOf(restoDetail.getResto().getId()));
        loadPhotoPackage.setRelated_model(Keys.CAP_RESTO);

        new LoadPhotoResto(
                BeerMap.getAppComponent().mainThread(),
                BeerMap.getAppComponent().executor(),
                BeerMap.getAppComponent().api()
        ).execute(loadPhotoPackage, simpleSubscriber);

    }

    @Override
    public void startChat(Activity activity, String user_id) {

        loadUsersTask.execute(Integer.valueOf(user_id),new SimpleSubscriber<ArrayList<User>>(){
            @Override
            public void onNext(ArrayList<User> users) {
                super.onNext(users);
                if(users.size()==0) return;
                User friend=new User();
                friend.setId(users.get(0).getId());
                friend.setFirstname(users.get(0).getFirstname());
                friend.setLastname(users.get(0).getLastname());
                Starter.MultiFragmentActivity_MODE_CHAT(activity,friend);

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });
    }

    @Override
    public RestoDetail getRestoDetails() {
        return restoDetail;
    }

    @Override
    public void uploadPhoto(File file, Callback<Integer> callback) {
        NewPhotoPackage newPhotoPackage=new NewPhotoPackage(file);
        newPhotoPackage.setRelatedModel(Keys.CAP_RESTO);
        newPhotoPackage.setRelatedId(restoDetail.getResto().id());
        uploadPhotoTask.execute(newPhotoPackage,new SimpleSubscriber<SingleResponse<UploadPhotoResponse>>(){
            @Override
            public void onNext(SingleResponse<UploadPhotoResponse> uploadPhotoResponseSingleResponse) {
                super.onNext(uploadPhotoResponseSingleResponse);
                if(callback!=null) callback.onResult(Activity.RESULT_OK);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if(callback!=null) callback.onResult(Activity.RESULT_CANCELED);
            }
        });
    }
    //endregion

    //region Inner Classes
    class LoadersAttributes {
        public LoadersAttributes(int mode) {
            loadRestoDetails(mode);
        }
        private void loadRestoDetails(int mode){
            if(mode== Actions.MODE_REFRESH_ALL ||mode== Actions.MODE_REFRESH_ONLY_LIKE) {
                LoadRestoDetailPackage loadRestoDetailPackage =new LoadRestoDetailPackage();
                loadRestoDetailPackage.setId(String.valueOf(restoDetail.getResto().getId()));
                loadRestoDetailTask.execute(loadRestoDetailPackage, new SimpleSubscriber<RestoDetail>() {
                    @Override public void onNext(RestoDetail _restoDetail) {
                        super.onNext(_restoDetail);
                        restoDetail=_restoDetail;
                        view.setModel(_restoDetail,mode);
                        loadReviews(mode);
                    }
                    @Override public void onError(Throwable e) {
                        super.onError(e); view.commonError(e.getMessage());
                    }
                });
            }else {
                loadReviews(mode);
            }
        }
        private void loadReviews(int mode) {
            if(mode== Actions.MODE_REFRESH_ALL) {
                Log.i("SpeedLoad","loadReviews");
                ReviewPackage reviewPackage=new ReviewPackage();
                reviewPackage.setRelated_model(Keys.CAP_RESTO);
                reviewPackage.setRelated_id(String.valueOf(restoDetail.getResto().getId()));
                containerTasks.loadReviewsTask(reviewPackage, new SimpleSubscriber<List<IFlexible>>() {
                    @Override
                    public void onNext(List<IFlexible> iFlexibles) {
                        super.onNext(iFlexibles);
                        view.setReviews(iFlexibles);
                        loadSubscriptions(mode);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(e.getMessage());
                    }
                });
            }else
                loadSubscriptions(mode);
        }
        private void loadSubscriptions(int mode) {
            if(mode== Actions.MODE_REFRESH_ALL) {
                Log.i("SpeedLoad","loadSubscriptions");
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
                Log.i("SpeedLoad","loadCntSales");
                LoadNewsPackage loadNewsPackage=new LoadNewsPackage();
                loadNewsPackage.setMode(EventsView.MODE_SALES);
                loadNewsPackage.setRelated_model(Keys.CAP_RESTO);
                loadNewsPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                loadNewsPackage.setOnlyMount(true);
                loadSalesTask.execute(loadNewsPackage,new SimpleSubscriber<Sales>(){
                    @Override
                    public void onNext(Sales sales) {
                        super.onNext(sales);
                        view.setCnt(sales.getTotal(),EventsView.MODE_SALES);
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
                Log.i("SpeedLoad","loadCntNews");
                LoadNewsPackage loadNewsPackage=new LoadNewsPackage();
                loadNewsPackage.setMode(EventsView.MODE_NEWS);
                loadNewsPackage.setRelated_model(Keys.CAP_RESTO);
                loadNewsPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                loadNewsPackage.setOnlyMount(true);
                loadNewsTask.execute(loadNewsPackage,new SimpleSubscriber<Posts>(){
                    @Override
                    public void onNext(Posts posts) {
                        super.onNext(posts);
                        view.setCnt(posts.getTotal(), EventsView.MODE_NEWS);
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
                Log.i("SpeedLoad","loadCntEvent");
                LoadNewsPackage loadNewsPackage=new LoadNewsPackage();
                loadNewsPackage.setMode(EventsView.MODE_EVENTS);
                loadNewsPackage.setRelated_model(Keys.CAP_RESTO);
                loadNewsPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                loadNewsPackage.setOnlyMount(true);
                loadEventsTask.execute(loadNewsPackage,new SimpleSubscriber<Events>(){
                    @Override
                    public void onNext(Events events) {
                        super.onNext(events);
                        view.setCnt(events.getTotal(), EventsView.MODE_EVENTS);
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
                Log.i("SpeedLoad","loadFav");
                LoadInterestPackage loadInterestPackage =new LoadInterestPackage();
                loadInterestPackage.setRelated_model(Keys.CAP_RESTO);
                loadInterestPackage.setRelated_id(String.valueOf(restoDetail.getResto().getId()));
                loadInterestPackage.setUser_id(String.valueOf(userRepo.load().getId()));
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
                Log.i("SpeedLoad","loadAvegagEvaluation");
                RestoAverageEvaluationPackage restoAverageEvaluationPackage=new RestoAverageEvaluationPackage();
                restoAverageEvaluationPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                restoAverageEvaluationPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
                loadRestoAverageEvaluationTask.execute(restoAverageEvaluationPackage,new SimpleSubscriber<List<AverageEvaluation>>(){
                    @Override
                    public void onNext(List<AverageEvaluation> evaluations) {
                        super.onNext(evaluations);
                        view.AverageEvaluation(evaluations);
                        loadInterests(mode);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(e.getMessage());
                    }
                });
            }else {
                loadInterests(mode);
            }
        }
        private void loadInterests(int mode) {
            Log.i("SpeedLoad","loadInterests");
            LoadInterestPackage loadInterestPackage =new LoadInterestPackage();
            loadInterestPackage.setRelated_model(Keys.CAP_RESTO);
            loadInterestPackage.setRelated_id(String.valueOf(restoDetail.getResto().getId()));
            loadInterestTask.execute(loadInterestPackage ,new SimpleSubscriber<List<IFlexible>>() {
                @Override
                public void onNext(List<IFlexible> iFlexibles) {
                    super.onNext(iFlexibles);
                    ArrayList<IFlexible> arrayList=new ArrayList<>();
                    for (IFlexible iFlexible:iFlexibles){
                        InterestInfoByUsers interestInfoByUsers=new InterestInfoByUsers();
                        interestInfoByUsers.setModel(((InterestInfo)iFlexible).getModel());
                        arrayList.add(interestInfoByUsers);
                    }
                    view.addItemsAddedToFavorite(arrayList);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    view.commonError(e.getMessage());
                }
            });

        }
    }

    @Override
    public void sendResultReceiver(int actionResultReceiver) {
        if(resultReceiver!=null) {
            resultReceiver.send(actionResultReceiver, null);
        }

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
    //endregion

}
