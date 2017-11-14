package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.brewmapp.R;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerDetail;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.pojo.LoadProductPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.exchange.share.contract.LikeDislike;
import com.brewmapp.execution.task.LoadProductTask;
import com.brewmapp.presentation.presenter.contract.BeerDetailPresenter;
import com.brewmapp.presentation.view.contract.BeerDetailView;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 30.10.2017.
 */

public class BeerDetailPresenterImpl extends BasePresenter<BeerDetailView> implements BeerDetailPresenter {


    private LoadProductTask loadProductTask;
    private LikeDislike likeDislike;
    private BeerDetail beerDetail;
    private Context context;

    @Inject
    public BeerDetailPresenterImpl(LoadProductTask loadProductTask,LikeDislike likeDislike, Context context){
        this.loadProductTask= loadProductTask;
        this.likeDislike = likeDislike;
        this.context = context;

    }


    @Override
    public void clickLike(int like_dislike) {
        likeDislike.clickLike(Keys.CAP_BEER,Integer.valueOf(beerDetail.getBeer().getId()),like_dislike,new SimpleSubscriber<MessageResponse>(){
            @Override
            public void onNext(MessageResponse messageResponse) {
                super.onNext(messageResponse);
                refreshContent(RequestCodes.MODE_LOAD_ONLY_LIKE);
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
            refreshContent(RequestCodes.MODE_LOAD_ALL);
        }catch (Exception e){
            view.commonError(e.getMessage());
        }
    }

    @Override
    public void refreshContent(int mode) {
        loadData(mode);

    }

    private void loadData(int mode) {
        class LoadersAttributes{

        }

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
            }
        });

    }

    @Override
    public void onDestroy() {

    }
}
