package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.widget.RatingBar;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.pojo.EvaluationPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.SetBeerEvaluationTask;
import com.brewmapp.execution.task.containers.contract.ContainerTasks;
import com.brewmapp.presentation.presenter.contract.AddReviewBeerPresenter;
import com.brewmapp.presentation.view.contract.AddReviewBeerView;


import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 11/15/2017.
 */

public class AddReviewBeerPresenterImpl extends BasePresenter<AddReviewBeerView> implements AddReviewBeerPresenter{

    Beer beer=new Beer();
    private UserRepo userRepo;
    private ContainerTasks containerTasks;
    private SetBeerEvaluationTask setBeerEvaluationTask;


    private RatingBar.OnRatingBarChangeListener ratingBarChangeListener;
    private EvaluationPackage evaluationPackage_aftertaste=new EvaluationPackage("4");
    private EvaluationPackage evaluationPackage_color=new EvaluationPackage("3");
    private EvaluationPackage evaluationPackage_flavor=new EvaluationPackage("1");
    private EvaluationPackage evaluationPackage_taste=new EvaluationPackage("2");

    @Inject
    public AddReviewBeerPresenterImpl (UserRepo userRepo,ContainerTasks containerTasks,SetBeerEvaluationTask setBeerEvaluationTask){
        this.userRepo = userRepo;
        this.containerTasks = containerTasks;
        this.setBeerEvaluationTask = setBeerEvaluationTask;

        ratingBarChangeListener=new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch (ratingBar.getId()){
                    case R.id.addreviewactivitybeer_rating_aftertaste:
                        evaluationPackage_aftertaste.setEaluation_value(String.valueOf(rating));
                        break;
                    case R.id.addreviewactivitybeer_rating_color:
                        evaluationPackage_color.setEaluation_value(String.valueOf(rating));
                        break;
                    case R.id.addreviewactivitybeer_rating_flavor:
                        evaluationPackage_flavor.setEaluation_value(String.valueOf(rating));
                        break;
                    case R.id.addreviewactivitybeer_rating_taste:
                        evaluationPackage_taste.setEaluation_value(String.valueOf(rating));
                        break;
                    default:
                        return;
                }

            }
        };

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
        evaluationPackage_aftertaste.setModel_id(beer.getId());
        evaluationPackage_color.setModel_id(beer.getId());
        evaluationPackage_flavor.setModel_id(beer.getId());
        evaluationPackage_taste.setModel_id(beer.getId());
        setBeerEvaluationTask.execute(evaluationPackage_flavor,new SimpleSubscriber<String>(){
            @Override
            public void onNext(String s) {
                super.onNext(s);
                setBeerEvaluationTask.execute(evaluationPackage_color,new SimpleSubscriber<String>(){
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        setBeerEvaluationTask.execute(evaluationPackage_aftertaste,new SimpleSubscriber<String>(){
                            @Override
                            public void onNext(String s) {
                                super.onNext(s);
                                setBeerEvaluationTask.execute(evaluationPackage_taste,new SimpleSubscriber<String>(){
                                    @Override
                                    public void onNext(String s) {
                                        super.onNext(s);
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
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        view.commonError(e.getMessage());
                                    }
                                });

                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                view.commonError(e.getMessage());
                            }
                        });

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(e.getMessage());
                    }
                });

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
            if(beer.getId()==null)view.commonError();else loadBeer(Actions.MODE_REFRESH_ALL);
        }catch (Exception e){
            view.commonError(e.getMessage());
        }

    }

    @Override
    public RatingBar.OnRatingBarChangeListener getRatingBarChangeListener() {
        return ratingBarChangeListener;
    }

    private void loadBeer(int mode) {
        if(mode== Actions.MODE_REFRESH_ALL){
            view.setUser(userRepo.load());
        }
    }
}
