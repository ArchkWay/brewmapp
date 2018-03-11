package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.widget.RatingBar;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.EvaluationResto;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.pojo.EvaluationPackage;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.AddReviewTask;
import com.brewmapp.execution.task.LoadRestoEvaluationTask;
import com.brewmapp.execution.task.SetRestoEvaluationTask;
import com.brewmapp.execution.task.containers.contract.ContainerTasks;
import com.brewmapp.presentation.presenter.contract.AddReviewRestoPresenter;
import com.brewmapp.presentation.view.contract.AddReviewRestoView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 03.11.2017.
 */

public class AddReviewRestoPresenterImpl extends BasePresenter<AddReviewRestoView> implements AddReviewRestoPresenter {

    private RestoDetail restoDetail;
    private List<EvaluationResto> evaluationRestoList =new ArrayList<>();
    private LoadRestoEvaluationTask loadRestoEvaluationTask;
    private SetRestoEvaluationTask setRestoEvaluationTask;
    private AddReviewTask addReviewTask;
    private UserRepo userRepo;
    private ContainerTasks containerTasks;

    private EvaluationPackage evaluationPackage =new EvaluationPackage(null);
    private EvaluationPackage evaluationPackageInterior =new EvaluationPackage(Keys.EVLUATION_TYPE_INTERIOR);
    private EvaluationPackage evaluationPackageService =new EvaluationPackage(Keys.EVLUATION_TYPE_SERVICE);
    private EvaluationPackage evaluationPackageCommonEffect =new EvaluationPackage(Keys.EVLUATION_TYPE_EFFECT);
    private EvaluationPackage evaluationPackageBeer =new EvaluationPackage(Keys.EVLUATION_TYPE_BEER);

    @Inject
    public AddReviewRestoPresenterImpl(LoadRestoEvaluationTask loadRestoEvaluationTask,SetRestoEvaluationTask setRestoEvaluationTask,UserRepo userRepo,AddReviewTask addReviewTask,ContainerTasks containerTasks){
        this.loadRestoEvaluationTask = loadRestoEvaluationTask;
        this.setRestoEvaluationTask = setRestoEvaluationTask;
        this.userRepo = userRepo;
        this.addReviewTask = addReviewTask;
        this.containerTasks = containerTasks;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(AddReviewRestoView addReviewRestoView) {
        super.onAttach(addReviewRestoView);
    }

    @Override
    public void parseIntent(Intent intent) {
        try {
            restoDetail= (RestoDetail) intent.getSerializableExtra(Keys.RESTO_ID);
            if(restoDetail==null)view.commonError();else loadEverything();
        }catch (Exception e){
            view.commonError(e.getMessage());
        }
    }

    private void loadEverything() {
        //prepare post
        view.setUser(userRepo.load());
        //prepare load
        evaluationPackage.setModel_id(String.valueOf(restoDetail.getResto().getId()));
        evaluationPackageInterior.setModel_id(String.valueOf(restoDetail.getResto().getId()));
        evaluationPackageService.setModel_id(String.valueOf(restoDetail.getResto().getId()));
        evaluationPackageCommonEffect.setModel_id(String.valueOf(restoDetail.getResto().getId()));
        evaluationPackageBeer.setModel_id(String.valueOf(restoDetail.getResto().getId()));

        //load
        loadRestoEvaluationTask.execute(evaluationPackage,new SimpleSubscriber<List<EvaluationResto>>(){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }

            @Override
            public void onNext(List<EvaluationResto> evaluationRestos) {
                evaluationRestoList.clear();
                evaluationRestoList.addAll(evaluationRestos);
                view.setEvaluation(evaluationRestoList);
            }
        });
    }

    @Override
    public RatingBar.OnRatingBarChangeListener getRatingListener() {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                switch (ratingBar.getId()){
                    case R.id.addreviewactivity_rating_interior:
                        evaluationPackageInterior.setEaluation_value(String.valueOf(v));
                        break;
                    case R.id.addreviewactivity_rating_common_effect:
                        evaluationPackageCommonEffect.setEaluation_value(String.valueOf(v));
                        break;
                    case R.id.addreviewactivity_rating_quality_beer:
                        evaluationPackageBeer.setEaluation_value(String.valueOf(v));
                        break;
                    case R.id.addreviewactivity_rating_service:
                        evaluationPackageService.setEaluation_value(String.valueOf(v));
                        break;
                    default:
                            return;
                }

            }
        };
    }

    @Override
    public void sendReview(Post post) {
        //EvaluationSend
            setRestoEvaluationTask.execute(evaluationPackageInterior,new SimpleSubscriber<String>(){
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    setRestoEvaluationTask.execute(evaluationPackageCommonEffect,new SimpleSubscriber<String>(){
                        @Override
                        public void onNext(String s) {
                            super.onNext(s);
                            setRestoEvaluationTask.execute(evaluationPackageBeer,new SimpleSubscriber<String>(){
                                @Override
                                public void onNext(String s) {
                                    super.onNext(s);
                                    setRestoEvaluationTask.execute(evaluationPackageService, new SimpleSubscriber<String>(){
                                        @Override
                                        public void onNext(String s) {
                                            super.onNext(s);
                                            //Review
                                            containerTasks.addReviewTask(
                                                    Keys.CAP_RESTO,
                                                    String.valueOf(restoDetail.getResto().getId()),
                                                    post.getText(),
                                                    post.getType(),
                                                    new SimpleSubscriber<String>(){
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
}
