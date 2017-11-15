package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.brewmapp.R;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerDetail;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.pojo.LoadProductPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.containers.contract.ContainerTasks;
import com.brewmapp.execution.task.LoadProductTask;
import com.brewmapp.presentation.presenter.contract.BeerDetailPresenter;
import com.brewmapp.presentation.view.contract.BeerDetailView;
import com.brewmapp.presentation.view.impl.activity.AddReviewBeerActivity;
import com.brewmapp.presentation.view.impl.activity.BeerDetailActivity;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import static com.brewmapp.app.environment.RequestCodes.MODE_LOAD_ALL;

/**
 * Created by Kras on 30.10.2017.
 */

public class BeerDetailPresenterImpl extends BasePresenter<BeerDetailView> implements BeerDetailPresenter {


    private LoadProductTask loadProductTask;
    private ContainerTasks containerTasks;
    private BeerDetail beerDetail;
    private Context context;
    private TempDataHolder tempDataHolder =new TempDataHolder();


    @Inject
    public BeerDetailPresenterImpl(LoadProductTask loadProductTask, ContainerTasks containerTasks, Context context){
        this.loadProductTask= loadProductTask;
        this.containerTasks = containerTasks;
        this.context = context;

    }


    @Override
    public void clickLike(int like_dislike) {
        containerTasks.clickLikeDislike(Keys.CAP_BEER,Integer.valueOf(beerDetail.getBeer().getId()),like_dislike,new SimpleSubscriber<MessageResponse>(){
            @Override
            public void onNext(MessageResponse messageResponse) {
                super.onNext(messageResponse);
                loadData(RequestCodes.MODE_LOAD_ONLY_LIKE);
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
            Beer beer=new Beer(); beer.setId(((Interest)intent.getSerializableExtra(context.getString(R.string.key_serializable_extra))).getInterest_info().getId());
            beerDetail=new BeerDetail(beer);
            refreshContent(MODE_LOAD_ALL);
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
    public void startAddReviewRestoActivity(BeerDetailActivity beerDetailActivity) {
        Intent intent=new Intent(beerDetailActivity, AddReviewBeerActivity.class);
        intent.putExtra(Keys.CAP_BEER,beerDetail.getBeer().getId());
        beerDetailActivity.startActivityForResult(intent, RequestCodes.REQUEST_CODE_REVIEW);

    }

    private void loadData(int mode) {
        class LoadersAttributes{
            public LoadersAttributes(int mode){
                loadBeer(mode);
            }

            private void loadBeer(int mode) {
                switch (mode){
                    case MODE_LOAD_ALL:
                    case RequestCodes.MODE_LOAD_ONLY_LIKE:
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
                    case MODE_LOAD_ALL:
                        containerTasks.loadInteres(Keys.CAP_BEER,Integer.valueOf(beerDetail.getBeer().getId()),new SimpleSubscriber<List<IFlexible>>(){
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
                    case MODE_LOAD_ALL:
                        containerTasks.loadReviewsTask(Keys.CAP_BEER,Integer.valueOf(beerDetail.getBeer().getId()),new SimpleSubscriber<List<IFlexible>>(){
                            @Override public void onNext(List<IFlexible> iFlexibles ) {
                                super.onNext(iFlexibles);view.setReviews(iFlexibles);
                            }
                            @Override public void onError(Throwable e) {
                                super.onError(e);view.commonError(e.getMessage());
                            }
                        });
                        break;
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
