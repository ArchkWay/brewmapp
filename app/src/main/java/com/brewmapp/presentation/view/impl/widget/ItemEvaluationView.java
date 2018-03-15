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
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.EvaluationBeer;
import com.brewmapp.data.entity.EvaluationResto;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.entity.wrapper.EvaluationData;
import com.brewmapp.data.pojo.LoadProductPackage;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.LoadProductTask;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.items.IFlexible;
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
    @BindView(R.id.view_evaluation_aftertaste)    TextView aftertaste;
    @BindView(R.id.view_evaluation_color)    TextView color;
    @BindView(R.id.view_evaluation_taste)    TextView taste;
    @BindView(R.id.view_evaluation_flavor)    TextView flavor;


    @BindView(R.id.view_evaluation_container_resto)    View container_resto;
    @BindView(R.id.view_evaluation_container_beer)    View container_beer;

    private EvaluationData evaluationData;
    private Listener listener;
    @Inject
    public LoadRestoDetailTask loadRestoDetailTask;
    @Inject
    public LoadProductTask loadProductTask;


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
        switch (evaluationData.getType_of_object_evaluation()){
            case Keys.CAP_RESTO:
                container_beer.setVisibility(GONE);
                handleResto();
            break;
            case Keys.CAP_BEER:
                container_resto.setVisibility(GONE);
                handleBeer();
                break;

        }
    }
    //***********************************
    private void handleBeer() {
        setOnClickListener(v -> Starter.BeerDetailActivity(getContext(), evaluationData.getId_of_object_evaluation()));
        Iterator<BaseEvaluation> iterator = evaluationData.iterator();
        while (iterator.hasNext()) {
            EvaluationBeer evaluationBeer = (EvaluationBeer) iterator.next();
            switch (evaluationBeer.getEvaluation_type()) {
                case "1":
                    flavor.setText(evaluationBeer.getEvaluation_value());
                    break;
                case "2":
                    taste.setText(evaluationBeer.getEvaluation_value());
                    break;
                case "3":
                    color.setText(evaluationBeer.getEvaluation_value());
                    break;
                case "4":
                    aftertaste.setText(evaluationBeer.getEvaluation_value());
                    break;
            }
        }
        LoadProductPackage loadProductPackage=new LoadProductPackage();
        loadProductPackage.setId(evaluationData.getId_of_object_evaluation());
        loadProductTask.execute(loadProductPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                try {
                    Beer beer=((BeerInfo)iFlexibles.get(0)).getModel();
                    Picasso.with(getContext()).load(beer.getGetThumb()).fit().centerInside().error(R.drawable.ic_default_resto).into(avatar);
                    title.setText(beer.getFormatedTitle());
                }catch (Exception e){}
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void handleResto() {
        setOnClickListener(v -> Starter.RestoDetailActivity((BaseActivity) getContext(), evaluationData.getId_of_object_evaluation()));
        Iterator<BaseEvaluation> iterator = evaluationData.iterator();
        while (iterator.hasNext()) {
            EvaluationResto evaluationResto = (EvaluationResto) iterator.next();
            switch (evaluationResto.getEvaluation_type()) {
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
