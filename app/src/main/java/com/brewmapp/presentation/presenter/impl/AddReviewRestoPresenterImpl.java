package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.widget.RatingBar;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Evaluation;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.data.pojo.RestoEvaluationPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.AddReviewTask;
import com.brewmapp.execution.task.LoadRestoEvaluationTask;
import com.brewmapp.execution.task.SetRestoEvaluationTask;
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
    private List<Evaluation> evaluationList=new ArrayList<>();
    private LoadRestoEvaluationTask loadRestoEvaluationTask;
    private SetRestoEvaluationTask setRestoEvaluationTask;
    private AddReviewTask addReviewTask;
    private UserRepo userRepo;

    private RestoEvaluationPackage restoEvaluationPackage =new RestoEvaluationPackage(null);
    private RestoEvaluationPackage restoEvaluationPackageInterior =new RestoEvaluationPackage(Keys.EVLUATION_TYPE_INTERIOR);
    private RestoEvaluationPackage restoEvaluationPackageService =new RestoEvaluationPackage(Keys.EVLUATION_TYPE_SERVICE);
    private RestoEvaluationPackage restoEvaluationPackageCommonEffect =new RestoEvaluationPackage(Keys.EVLUATION_TYPE_EFFECT);
    private RestoEvaluationPackage restoEvaluationPackageBeer =new RestoEvaluationPackage(Keys.EVLUATION_TYPE_BEER);

    @Inject
    public AddReviewRestoPresenterImpl(LoadRestoEvaluationTask loadRestoEvaluationTask,SetRestoEvaluationTask setRestoEvaluationTask,UserRepo userRepo,AddReviewTask addReviewTask){
        this.loadRestoEvaluationTask = loadRestoEvaluationTask;
        this.setRestoEvaluationTask = setRestoEvaluationTask;
        this.userRepo = userRepo;
        this.addReviewTask = addReviewTask;
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
        restoEvaluationPackage.setResto_id(String.valueOf(restoDetail.getResto().getId()));
        restoEvaluationPackageInterior.setResto_id(String.valueOf(restoDetail.getResto().getId()));
        restoEvaluationPackageService.setResto_id(String.valueOf(restoDetail.getResto().getId()));
        restoEvaluationPackageCommonEffect.setResto_id(String.valueOf(restoDetail.getResto().getId()));
        restoEvaluationPackageBeer.setResto_id(String.valueOf(restoDetail.getResto().getId()));

        //load
        loadRestoEvaluationTask.execute(restoEvaluationPackage,new SimpleSubscriber<List<Evaluation>>(){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }

            @Override
            public void onNext(List<Evaluation> evaluations) {
                evaluationList.clear();
                evaluationList.addAll(evaluations);
                view.setEvaluation(evaluationList);
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
                        restoEvaluationPackageInterior.setEaluation_value(String.valueOf(v));
                        break;
                    case R.id.addreviewactivity_rating_common_effect:
                        restoEvaluationPackageCommonEffect.setEaluation_value(String.valueOf(v));
                        break;
                    case R.id.addreviewactivity_rating_quality_beer:
                        restoEvaluationPackageBeer.setEaluation_value(String.valueOf(v));
                        break;
                    case R.id.addreviewactivity_rating_service:
                        restoEvaluationPackageService.setEaluation_value(String.valueOf(v));
                        break;
                    default:
                            return;
                }

            }
        };
    }

    @Override
    public void sendReview(Post post) {
        //Evaluation
            setRestoEvaluationTask.execute(restoEvaluationPackageInterior,new SimpleSubscriber<String>(){
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    setRestoEvaluationTask.execute(restoEvaluationPackageCommonEffect,new SimpleSubscriber<String>(){
                        @Override
                        public void onNext(String s) {
                            super.onNext(s);
                            setRestoEvaluationTask.execute(restoEvaluationPackageBeer,new SimpleSubscriber<String>(){
                                @Override
                                public void onNext(String s) {
                                    super.onNext(s);
                                    setRestoEvaluationTask.execute(restoEvaluationPackageService);
                                }
                            });
                        }
                    });
                }
            });



        //Review
        ReviewPackage reviewPackage =new ReviewPackage();
        reviewPackage.setRelated_model(Keys.CAP_RESTO);
        reviewPackage.setRelated_id(String.valueOf(restoDetail.getResto().getId()));
        reviewPackage.setText(post.getText());
        addReviewTask.execute(reviewPackage,new SimpleSubscriber<String>(){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                super.onNext(s);
                view.reviewSent();
            }
        });
    }
}
