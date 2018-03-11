package com.brewmapp.execution.task.containers.impl;

import com.brewmapp.data.entity.Averagevalue;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.pojo.AddInterestPackage;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.data.pojo.LikesByBeerPackage;
import com.brewmapp.data.pojo.LoadAverageValuePackage;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.data.pojo.RestoGeoPackage;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.data.pojo.SearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.AddInterestTask;
import com.brewmapp.execution.task.AddReviewTask;
import com.brewmapp.execution.task.LoadInterestByUsersTask;
import com.brewmapp.execution.task.LoadInterestTask;
import com.brewmapp.execution.task.LoadLikesByBeerTask;
import com.brewmapp.execution.task.LoadProductAverageValue;
import com.brewmapp.execution.task.LoadRestoGeoTask;
import com.brewmapp.execution.task.LoadRestoLocationTask;
import com.brewmapp.execution.task.LoadReviewsTask;
import com.brewmapp.execution.task.LoadUsersByInterestTask;
import com.brewmapp.execution.task.RemoveInterestTask;
import com.brewmapp.execution.task.SearchRestosTask;
import com.brewmapp.execution.task.containers.contract.ContainerTasks;
import com.brewmapp.execution.task.LikeTask;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;

/**
 * Created by xpusher on 11/14/2017.
 */

public class ContainerTasksImpl implements ContainerTasks {

    private LikeTask likeTask;
    private LoadInterestTask loadInterestTask;
    private LoadInterestByUsersTask loadInterestByUsersTask;
    private AddInterestTask addInterestTask;
    private RemoveInterestTask removeInterestTask;
    private LoadReviewsTask loadReviewsTask;
    private AddReviewTask addReviewTask;
    private LoadRestoGeoTask loadRestoGeoTask;
    private LoadLikesByBeerTask loadLikesByBeerTask;
    private SearchRestosTask searchRestosTask;
    private LoadUsersByInterestTask loadUsersByInterestTask;
    private LoadProductAverageValue loadProductAverageValue;

    @Inject
    public ContainerTasksImpl(LikeTask likeTask,LoadInterestTask loadInterestTask,AddInterestTask addInterestTask,RemoveInterestTask removeInterestTask,LoadReviewsTask loadReviewsTask,AddReviewTask addReviewTask,LoadRestoGeoTask loadRestoGeoTask,LoadLikesByBeerTask loadLikesByBeerTask,SearchRestosTask searchRestosTask,LoadInterestByUsersTask loadInterestByUsersTask,LoadUsersByInterestTask loadUsersByInterestTask,LoadProductAverageValue loadProductAverageValue){
        this.likeTask=likeTask;
        this.loadInterestTask=loadInterestTask;
        this.addInterestTask=addInterestTask;
        this.removeInterestTask=removeInterestTask;
        this.loadReviewsTask=loadReviewsTask;
        this.addReviewTask=addReviewTask;
        this.loadRestoGeoTask=loadRestoGeoTask;
        this.loadLikesByBeerTask=loadLikesByBeerTask;
        this.searchRestosTask=searchRestosTask;
        this.loadInterestByUsersTask=loadInterestByUsersTask;
        this.loadUsersByInterestTask=loadUsersByInterestTask;
        this.loadProductAverageValue=loadProductAverageValue;
    }

    @Override
    public void clickLikeDislike(String relatedModel, int relatedId, int type_like, SimpleSubscriber<MessageResponse> simpleSubscriber) {
        LikeDislikePackage likeDislikePackage=new LikeDislikePackage(type_like);
        likeDislikePackage.setModel(relatedModel,relatedId);
        likeTask.execute(likeDislikePackage,simpleSubscriber);
    }

    @Override
    public void loadInteres(LoadInterestPackage loadInterestPackage,SimpleSubscriber<List<IFlexible>> objectSimpleSubscriber) {


        loadInterestTask.execute(loadInterestPackage ,objectSimpleSubscriber);
    }

    @Override
    public void loadInteresByUsers(String related_model, Integer related_id, SimpleSubscriber<List<IFlexible>> objectSimpleSubscriber) {
        LoadInterestPackage loadInterestPackage =new LoadInterestPackage();
        loadInterestPackage.setRelated_model(related_model);
        loadInterestPackage.setRelated_id(String.valueOf(related_id));
        loadInterestByUsersTask.execute(loadInterestPackage ,objectSimpleSubscriber);
    }

    @Override
    public void interestOFF(String id_interest, SimpleSubscriber<String> simpleSubscriber) {
        removeInterestTask.execute(id_interest,simpleSubscriber);
    }

    @Override
    public void interestON(String relatedModel, String relatedId, SimpleSubscriber<String> simpleSubscriber) {
        AddInterestPackage addInterestPackage = new AddInterestPackage();
        addInterestPackage.setRelated_id(relatedId);
        addInterestPackage.setRelated_model(relatedModel);
        addInterestTask.execute(addInterestPackage, simpleSubscriber);
    }

    @Override
    public void loadReviewsTask(ReviewPackage reviewPackage, SimpleSubscriber<List<IFlexible>> simpleSubscriber) {
        loadReviewsTask.execute(reviewPackage,simpleSubscriber);
    }

    @Override
    public void addReviewTask(String relatedModel, String relatedId, String text, int type, SimpleSubscriber<String> simpleSubscriber) {
        ReviewPackage reviewPackage =new ReviewPackage();
        reviewPackage.setRelated_model(relatedModel);
        reviewPackage.setRelated_id(relatedId);
        reviewPackage.setText(text);
        reviewPackage.setType(type);
        addReviewTask.execute(reviewPackage,simpleSubscriber);
    }

    @Override
    public void loadRestoByBeer(SearchPackage searchPackage, SimpleSubscriber<ListResponse<Resto>> simpleSubscriber) {
        searchRestosTask.execute(searchPackage, simpleSubscriber);
    }

    @Override
    public void loadLikesByBeer(String beer_id, SimpleSubscriber<Object> simpleSubscriber) {
        LikesByBeerPackage likesByBeerPackage=new LikesByBeerPackage();
        likesByBeerPackage.setRelated_id(beer_id);
        likesByBeerPackage.setRelated_model(Keys.CAP_BEER);
        loadLikesByBeerTask.execute(likesByBeerPackage,simpleSubscriber);
    }

    @Override
    public void loadUsersByInteres(Integer interest_id, SimpleSubscriber<List<IFlexible>> objectSimpleSubscriber) {
        loadUsersByInterestTask.execute(interest_id,objectSimpleSubscriber);
    }

    @Override
    public void loadProductAverageValue(LoadAverageValuePackage loadAverageValuePackage, SimpleSubscriber<ListResponse<Averagevalue>> listResponseSimpleSubscriber) {
        loadProductAverageValue.execute(loadAverageValuePackage,listResponseSimpleSubscriber);
    }

    @Override
    public void cancelTasks() {
        likeTask.cancel();
        loadInterestTask.cancel();
        loadInterestByUsersTask.cancel();
        addInterestTask.cancel();
        removeInterestTask.cancel();
        loadReviewsTask.cancel();
        addReviewTask.cancel();
        loadRestoGeoTask.cancel();
        loadLikesByBeerTask.cancel();
        searchRestosTask.cancel();
        loadUsersByInterestTask.cancel();
        loadProductAverageValue.cancel();

    }
}
