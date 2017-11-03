package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.widget.RatingBar;

import com.brewmapp.R;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.pojo.RestoEvaluationPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.LoadRestoEvaluationTask;
import com.brewmapp.execution.task.SetRestoEvaluationTask;
import com.brewmapp.presentation.presenter.contract.AddReviewRestoPresenter;
import com.brewmapp.presentation.view.contract.AddReviewRestoView;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 03.11.2017.
 */

public class AddReviewRestoPresenterImpl extends BasePresenter<AddReviewRestoView> implements AddReviewRestoPresenter {

    private RestoDetail restoDetail;
    private LoadRestoEvaluationTask loadRestoEvaluationTask;
    private SetRestoEvaluationTask setRestoEvaluationTask;
    private RestoEvaluationPackage restoEvaluationPackageInterior =new RestoEvaluationPackage(Keys.CAP_RESTO);
    private RestoEvaluationPackage restoEvaluationPackageService =new RestoEvaluationPackage(Keys.CAP_RESTO);
    private RestoEvaluationPackage restoEvaluationPackageCommonEffect =new RestoEvaluationPackage(Keys.CAP_RESTO);
    private RestoEvaluationPackage restoEvaluationPackageBeer =new RestoEvaluationPackage(Keys.CAP_RESTO);

    @Inject
    public AddReviewRestoPresenterImpl(LoadRestoEvaluationTask loadRestoEvaluationTask,SetRestoEvaluationTask setRestoEvaluationTask){
        this.loadRestoEvaluationTask = loadRestoEvaluationTask;
        this.setRestoEvaluationTask = setRestoEvaluationTask;

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
        restoEvaluationPackageInterior.setResto_id(String.valueOf(restoDetail.getResto().getId()));
        loadRestoEvaluationTask.execute(restoEvaluationPackageInterior,new SimpleSubscriber<String>(){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                super.onNext(s);
                view.setModel(restoDetail);
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
                        //setRestoEvaluationTask.execute(restoEvaluationPackageInterior);
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

                }

            }
        };
    }
}
