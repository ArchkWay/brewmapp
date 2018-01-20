package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.BaseEvaluation;
import com.brewmapp.data.entity.EvaluationBeer;
import com.brewmapp.data.entity.EvaluationResto;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.wrapper.EvaluationData;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 20.01.2018.
 */

public class ItemEvaluationView extends BaseLinearLayout implements InteractiveModelView<EvaluationData> {

    @BindView(R.id.view_evaluation_avatar)    ImageView avatar;
    @BindView(R.id.view_evaluation_title)    TextView title;
    @BindView(R.id.view_evaluation_interior)    TextView interior;
    @BindView(R.id.view_evaluation_service)    TextView service;
    @BindView(R.id.view_evaluation_beer)    TextView beer;
    @BindView(R.id.view_evaluation_effect)    TextView effect;

    private EvaluationData evaluationData;
    private Listener listener;
    @Inject
    public LoadRestoDetailTask loadRestoDetailTask;


    public ItemEvaluationView(Context context) {
        super(context);
    }

    public ItemEvaluationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemEvaluationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ItemEvaluationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void setListener(Listener listener) {
        this.listener=listener;
    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);
        BeerMap.getAppComponent().plus(new PresenterModule(this)).inject(this);
    }


    @Override
    public void setModel(EvaluationData model) {
        this.evaluationData=model;

    }

    @Override
    public EvaluationData getModel() {
        return null;
    }

    //**********************************

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        evaluationData=null;
        loadRestoDetailTask.cancel();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(isInEditMode()) return;
        if(evaluationData.getType_of_object_evaluation().equals(Keys.CAP_RESTO)) {
            setOnClickListener(v-> Starter.RestoDetailActivity(getContext(),evaluationData.getId_of_object_evaluation()));
            Iterator<BaseEvaluation> iterator=evaluationData.iterator();
            while (iterator.hasNext()){
                EvaluationResto evaluationResto= (EvaluationResto) iterator.next();
                switch (evaluationResto.getEvaluation_type()){
                    case "1":
                        interior.setText(evaluationResto.getEvaluation_value());
                        break;
                    case "2":
                        service.setText(evaluationResto.getEvaluation_value());
                        break;
                    case "3":
                        beer.setText(evaluationResto.getEvaluation_value());
                        break;
                    case "4":
                        effect.setText(evaluationResto.getEvaluation_value());
                        break;
                }

            }



            LoadRestoDetailPackage loadRestoDetailPackage=new LoadRestoDetailPackage();
            loadRestoDetailPackage.setId(evaluationData.getId_of_object_evaluation());
            loadRestoDetailTask.execute(loadRestoDetailPackage,new SimpleSubscriber<RestoDetail>(){
                @Override
                public void onNext(RestoDetail restoDetail) {
                    super.onNext(restoDetail);
                    Resto resto=restoDetail.getResto();
                    try {
                        Picasso.with(getContext()).load(resto.getThumb()).fit().centerCrop().error(R.drawable.ic_default_resto).into(avatar);
                    }catch (Exception e){}
                    try {
                        title.setText(resto.getName());
                    }catch (Exception e){}
                }
            });
        }
    }
}
