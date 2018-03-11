package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.EvaluationResto;
import com.brewmapp.data.entity.Review;
import com.brewmapp.data.pojo.EvaluationPackage;
import com.brewmapp.execution.task.LoadRestoEvaluationTask;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 04.11.2017.
 */

public class ReviewView extends BaseLinearLayout implements InteractiveModelView<Review> {

    @BindView(R.id.view_review_author)    TextView author;
    @BindView(R.id.view_review_avatar)    ImageView avatar;
    @BindView(R.id.view_review_date) TextView date;
    @BindView(R.id.view_review_text) TextView text;
    @BindView(R.id.widget_review_interior) TextView interior;
    @BindView(R.id.widget_review_quality_beer) TextView quality_beer;
    @BindView(R.id.widget_review_common_effect) TextView common_effect;
    @BindView(R.id.widget_review_service) TextView service;
    @BindView(R.id.widget_review_beer_linear_layout)    View linear_layout;
    @BindView(R.id.view_review_more) ReviewMoreImageButton more;
    @BindView(R.id.view_review_plus_minus)    TextView plus_minus;
    @BindView(R.id.view_review_yes)    ReviewYesNoButton yes;
    @BindView(R.id.view_review_no)    ReviewYesNoButton no;

    @Inject
    LoadRestoEvaluationTask loadRestoEvaluationTask;

    private Review model;

    public ReviewView(Context context) {
        super(context);
    }

    public ReviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ReviewView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setModel(Review model) {
        this.model = model;
        try {author.setText(model.getUser_info().getFormattedName());}catch (Exception e){}

        try {
            if(model.getType().equals("1")){
                plus_minus.setText("+");
                plus_minus.setBackground(getResources().getDrawable(R.drawable.bg_circle,getContext().getTheme()));
            }else {
                plus_minus.setText("-");
                plus_minus.setBackground(getResources().getDrawable(R.drawable.bg_circle_red, getContext().getTheme()));
            }
        }catch (Exception e){
            plus_minus.setVisibility(INVISIBLE);
        }
        more.setReview(model);
        yes.setReview(model);        yes.setVal(1);
        no.setReview(model);         no.setVal(0);

        String imgUrl=null;
        try {imgUrl=model.getUser_getThumb();Picasso.with(getContext()).load(imgUrl).fit().centerCrop().into(avatar);}catch (Exception e){}
        try {date.setText(MapUtils.FormatDate(model.getTimestamp()));}catch (Exception e){}
        try {text.setText(model.getText());}catch (Exception e){}
        if(TextUtils.isEmpty(imgUrl))  try {Picasso.with(getContext()).load(model.getUser_info().getGender().equals("1")?R.drawable.ic_user_man:R.drawable.ic_user_woman).fit().centerCrop().into(avatar);}catch (Exception e){}

        //region Evaluation
        EvaluationPackage evaluationPackage=new EvaluationPackage();
        evaluationPackage.setModel_id(model.getRelated_id());
        evaluationPackage.setToken(model.getUser_info().getToken());
        loadRestoEvaluationTask.execute(evaluationPackage,new SimpleSubscriber<List<EvaluationResto>>(){
            @Override
            public void onNext(List<EvaluationResto> evaluationRestos) {
                super.onNext(evaluationRestos);
                if(evaluationRestos.size()==0){
                    linear_layout.setVisibility(GONE);
                }else {
                    linear_layout.setVisibility(VISIBLE);
                    for (EvaluationResto evaluationResto : evaluationRestos)
                        switch (evaluationResto.getEvaluation_type()) {
                            case "1":
                                interior.setText(evaluationResto.getEvaluation_value());
                                break;
                            case "2":
                                service.setText(evaluationResto.getEvaluation_value());
                                break;
                            case "3":
                                quality_beer.setText(evaluationResto.getEvaluation_value());
                                break;
                            case "4":
                                common_effect.setText(evaluationResto.getEvaluation_value());
                                break;
                        }
                }
            }
        });
        //endregion

    }

    @Override
    public Review getModel() {
        return model;
    }

    @Override
    public void setListener(Listener listener) {
        setOnClickListener(v->listener.onModelAction(Actions.ACTION_CLICK_ON_ITEM_REVIEW_ON_USER,model));
    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);
        BeerMap.getAppComponent().plus(new PresenterModule(this)).inject(this);
    }
}
