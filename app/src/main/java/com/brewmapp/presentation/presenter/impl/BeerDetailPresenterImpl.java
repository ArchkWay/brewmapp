package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Averagevalue;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerDetail;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.entity.wrapper.InterestInfoByUsers;
import com.brewmapp.data.pojo.LoadAverageValuePackage;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.data.pojo.LoadProductPackage;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.data.pojo.SearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.containers.contract.ContainerTasks;
import com.brewmapp.execution.task.LoadProductTask;
import com.brewmapp.presentation.presenter.contract.BeerDetailPresenter;
import com.brewmapp.presentation.view.contract.BeerDetailView;
import com.brewmapp.presentation.view.impl.activity.AddReviewBeerActivity;
import com.brewmapp.presentation.view.impl.activity.BeerDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 30.10.2017.
 */

public class BeerDetailPresenterImpl extends BasePresenter<BeerDetailView> implements BeerDetailPresenter{


    private LoadProductTask loadProductTask;
    private ContainerTasks containerTasks;
    private BeerDetail beerDetail;
    private Context context;
    private TempDataHolder tempDataHolder =new TempDataHolder();
    private Interest interest;
    private UserRepo userRepo;


    @Inject
    public BeerDetailPresenterImpl(LoadProductTask loadProductTask, ContainerTasks containerTasks, Context context,UserRepo userRepo){
        this.loadProductTask= loadProductTask;
        this.containerTasks = containerTasks;
        this.context = context;
        this.userRepo = userRepo;

    }


    @Override
    public void clickLike(int like_dislike) {
        containerTasks.clickLikeDislike(Keys.CAP_BEER,Integer.valueOf(beerDetail.getBeer().getId()),like_dislike,new SimpleSubscriber<MessageResponse>(){
            @Override
            public void onNext(MessageResponse messageResponse) {
                super.onNext(messageResponse);
                loadData(Actions.MODE_REFRESH_ONLY_LIKE);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showMessage(context.getString(R.string.assessment_has_already_been_taken),0);
            }
        });
    }

    @Override
    public void parseIntent(Intent intent) {
        try {
            interest=(Interest)intent.getSerializableExtra(context.getString(R.string.key_serializable_extra));
            Beer beer=new Beer(); beer.setId(interest.getInterest_info().getId());
            beerDetail=new BeerDetail(beer);
            refreshContent(Actions.MODE_REFRESH_ALL);
        }catch (Exception e){
            view.commonError(e.getMessage());
        }
    }

    @Override
    public void refreshContent(int mode) {
        loadData(mode);
    }

    @Override
    public void clickFav() {
        if(tempDataHolder.isFavBeer()){
            containerTasks.interestOFF(tempDataHolder.getId_interest(),new SimpleSubscriber<String>(){
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    view.setFavorite(false);
                    tempDataHolder.setFavBeer(false);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    view.commonError(e.getMessage());
                }
            });
        }else {
            containerTasks.interestON(Keys.CAP_BEER,beerDetail.getBeer().getId(),new SimpleSubscriber<String>(){
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    view.setFavorite(true);
                    tempDataHolder.setFavBeer(true);
                    tempDataHolder.setId_interest(s);
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
    public BeerDetail getBeerDetail() {
        return beerDetail;
    }

    private void loadData(int mode) {

        class LoadersAttributes{
            public LoadersAttributes(int mode){
                loadBeer(mode);
            }

            private void loadBeer(int mode) {
                switch (mode){
                    case Actions.MODE_REFRESH_ALL:
                    case Actions.MODE_REFRESH_ONLY_LIKE:
                        LoadProductPackage loadProductPackage=new LoadProductPackage();
                        loadProductPackage.setId(beerDetail.getBeer().getId());

                        loadProductTask.execute(loadProductPackage,new SimpleSubscriber<List<IFlexible>>(){
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                view.commonError(e.getMessage());
                            }

                            @Override
                            public void onNext(List<IFlexible> iFlexibles) {
                                super.onNext(iFlexibles);
                                if(iFlexibles.size()==1){
                                    beerDetail=new BeerDetail(((BeerInfo)iFlexibles.get(0)).getModel());
                                    Paper.book().write("beerTest",beerDetail);
                                    view.setModel(beerDetail, mode);
                                }else {
                                    view.commonError();
                                }
                                loadFav(mode);
                            }
                        });
                        break;
                    default:{
                        loadFav(mode);
                    }
                }
            }
            private void loadFav(int mode) {
                switch (mode){
                    case Actions.MODE_REFRESH_ALL:
                        LoadInterestPackage loadInterestPackage =new LoadInterestPackage();
                        loadInterestPackage.setRelated_model(Keys.CAP_BEER);
                        loadInterestPackage.setRelated_id(String.valueOf(Integer.valueOf(beerDetail.getBeer().getId())));
                        loadInterestPackage.setUser_id(String.valueOf(userRepo.load().getId()));
                        containerTasks.loadInteres(loadInterestPackage,new SimpleSubscriber<List<IFlexible>>(){
                            @Override
                            public void onNext(List<IFlexible> iFlexibles) {
                                super.onNext(iFlexibles);
                                if(iFlexibles.size()==1){
                                    try {
                                        tempDataHolder.setId_interest(((InterestInfo) iFlexibles.get(0)).getModel().getId());
                                        view.setFavorite(true);
                                        tempDataHolder.setFavBeer(true);
                                    }catch (Exception e){
                                        view.commonError(e.getMessage());
                                    }

                                }else if(iFlexibles.size()==0){
                                    view.setFavorite(false);
                                    tempDataHolder.setFavBeer(false);
                                }else {
                                    view.commonError();
                                }
                                loadReviews(mode);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                view.commonError(e.getMessage());
                            }
                        });
                        break;
                    default:
                        loadReviews(mode);

                }
            }
            private void loadReviews(int mode){
                switch (mode){
                    case Actions.MODE_REFRESH_ALL:
                        ReviewPackage reviewPackage=new ReviewPackage();
                        reviewPackage.setRelated_model(Keys.CAP_BEER);
                        reviewPackage.setRelated_id(String.valueOf(beerDetail.getBeer().getId()));
                        containerTasks.loadReviewsTask(reviewPackage,new SimpleSubscriber<List<IFlexible>>(){
                            @Override public void onNext(List<IFlexible> iFlexibles ) {
                                super.onNext(iFlexibles);view.addItemsReviews(iFlexibles);loadResto(mode);
                            }
                            @Override public void onError(Throwable e) {
                                super.onError(e);view.commonError(e.getMessage());
                            }
                        });
                        break;
                        default:
                            loadResto(mode);
                }
            }
            private void loadResto(int mode){
                switch (mode){
                    case Actions.MODE_REFRESH_ALL:
                        SearchPackage searchPackage = new SearchPackage("");
                        searchPackage.getAdditionalFields().put(Keys.menuBeer, beerDetail.getBeer().getId());
                        searchPackage.setStartLimit(0);
                        searchPackage.setEndLimit(3);

                        containerTasks.loadRestoByBeer(searchPackage,new SimpleSubscriber<ListResponse<Resto>>(){
                            @Override
                            public void onNext(ListResponse<Resto> listResponse) {
                                super.onNext(listResponse);
                                ArrayList<InterestInfo> interestInfos=new ArrayList<>();
                                for (Resto resto:listResponse.getModels())
                                    interestInfos.add(new InterestInfo(new Interest(resto)));
                                view.addItemsWhereTheyPour(new ArrayList<>(interestInfos));
                                loadWhoIsInterested(mode);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                        });
                        break;
                        default:
                            loadWhoIsInterested(mode);
                }

            }
            private void loadWhoIsInterested(int mode) {
                switch (mode) {
                    case Actions.MODE_REFRESH_ALL:
                        LoadInterestPackage loadInterestPackage =new LoadInterestPackage();
                        loadInterestPackage.setRelated_model(Keys.CAP_BEER);
                        loadInterestPackage.setRelated_id(String.valueOf(Integer.valueOf(beerDetail.getBeer().getId())));
                        loadInterestPackage.setUser_id(null);
                        containerTasks.loadInteres(loadInterestPackage,new SimpleSubscriber<List<IFlexible>>(){
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
                                    loadProductAverageValue(mode);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                view.commonError(e.getMessage());
                            }
                        });
                        break;
                        default:
                            loadProductAverageValue(mode);
                }
            }
            private void loadProductAverageValue(int mode){//productaveragevalue
                switch (mode){
                    case Actions.MODE_REFRESH_ALL:
                        LoadAverageValuePackage loadAverageValuePackage=new LoadAverageValuePackage();
                        loadAverageValuePackage.setBeer_id(beerDetail.getBeer().getId());
                        containerTasks.loadProductAverageValue(loadAverageValuePackage,new SimpleSubscriber<ListResponse<Averagevalue>>(){
                            @Override
                            public void onNext(ListResponse<Averagevalue> averagevalueListResponse) {
                                super.onNext(averagevalueListResponse);
                                view.setProductAverageValue(averagevalueListResponse.getModels());
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                view.commonError(e.getMessage());
                            }
                        });
                        break;
                        default:
                            //THE END
                }
            }

        }

        new LoadersAttributes(mode);
    }

    @Override
    public void onDestroy() {

    }

    class TempDataHolder {
        private boolean favBeer;

        private String id_interest;

        public String getId_interest() {
            return id_interest;
        }

        public void setId_interest(String id_interest) {
            this.id_interest = id_interest;
        }

        public boolean isFavBeer() {
            return favBeer;
        }

        public void setFavBeer(boolean favBeer) {
            this.favBeer = favBeer;
        }

    }
}
