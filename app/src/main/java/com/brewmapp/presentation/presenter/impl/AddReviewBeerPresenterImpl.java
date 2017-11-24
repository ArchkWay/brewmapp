package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Post;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.containers.contract.ContainerTasks;
import com.brewmapp.presentation.presenter.contract.AddReviewBeerPresenter;
import com.brewmapp.presentation.view.contract.AddReviewBeerView;
import com.brewmapp.presentation.view.contract.UiCustomControl;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 11/15/2017.
 */

public class AddReviewBeerPresenterImpl extends BasePresenter<AddReviewBeerView> implements AddReviewBeerPresenter,UiCustomControl {

    Beer beer=new Beer();
    private UserRepo userRepo;
    private ContainerTasks containerTasks;

    @Inject
    public AddReviewBeerPresenterImpl (UserRepo userRepo,ContainerTasks containerTasks){
        this.userRepo = userRepo;
        this.containerTasks = containerTasks;
    }


    @Override
    public void onAttach(AddReviewBeerView addReviewBeerView) {
        super.onAttach(addReviewBeerView);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void sendReview(Post post) {
        containerTasks.addReviewTask(Keys.CAP_BEER,beer.getId(),post.getText(),new SimpleSubscriber<String>(){
            @Override
            public void onNext(String s) {
                super.onNext(s);
                view.reviewSent();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });
    }

    @Override
    public void parseIntent(Intent intent) {
        try {
            beer.setId(intent.getStringExtra(Keys.CAP_BEER));
            if(beer.getId()==null)view.commonError();else loadBeer(REFRESH_ALL);
        }catch (Exception e){
            view.commonError(e.getMessage());
        }

    }

    private void loadBeer(int mode) {
        if(mode== REFRESH_ALL){
            view.setUser(userRepo.load());
        }
    }
}
