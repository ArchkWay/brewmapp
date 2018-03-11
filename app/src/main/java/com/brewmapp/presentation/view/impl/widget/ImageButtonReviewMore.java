package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Review;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.task.AddReviewsApprovalTask;

import java.security.Key;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;

/**
 * Created by Kras on 11.03.2018.
 */

public class ImageButtonReviewMore extends android.support.v7.widget.AppCompatImageButton implements View.OnClickListener{
    private Review review;

    @Inject AddReviewsApprovalTask addReviewsApprovalTask;

    public ImageButtonReviewMore(Context context) {
        super(context);
    }

    public ImageButtonReviewMore(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageButtonReviewMore(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setReview(Review review) {
        this.review = review;
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        WrapperParams wrapperParams=new WrapperParams(Keys.CAP_REVIEW);
        wrapperParams.addParam(Keys.ID,review.getId());
        wrapperParams.addParam(Keys.TYPE,Keys.CAP_RESTO);
        BeerMap.getAppComponent().plus(new PresenterModule(this)).inject(this);
        addReviewsApprovalTask.execute(wrapperParams,new SimpleSubscriber<String>(){
            @Override
            public void onNext(String s) {
                super.onNext(s);
            }
        });
    }
}
