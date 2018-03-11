package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Review;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.task.AddReviewsApprovalTask;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;

/**
 * Created by Kras on 11.03.2018.
 */

public class ReviewYesNoButton extends android.support.v7.widget.AppCompatButton implements View.OnClickListener{
    private Review review;

    @Inject AddReviewsApprovalTask addReviewsApprovalTask;
    private int val;
    private int POSITIVE_REVIEW = 1;
    private int NEGATIVE_REVIEW = 0;

    public ReviewYesNoButton(Context context) {
        super(context);
    }

    public ReviewYesNoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReviewYesNoButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
        wrapperParams.addParam(Keys.TYPE,val);
        BeerMap.getAppComponent().plus(new PresenterModule(this)).inject(this);
        addReviewsApprovalTask.execute(wrapperParams,new SimpleSubscriber<String>(){
            @Override
            public void onNext(String s) {
                super.onNext(s);
                if(getContext() instanceof BaseActivity){
                    BaseActivity baseActivity= (BaseActivity) getContext();
                    baseActivity.showSnackbar("OK");
                    ReviewsApprovalAdded();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if(getContext() instanceof BaseActivity){
                    BaseActivity baseActivity= (BaseActivity) getContext();
                    baseActivity.showSnackbarRed("Err");
                }
            }
        });
    }

    private void ReviewsApprovalAdded() {
        if(review!=null){
            if(val== POSITIVE_REVIEW){
                review.setLike(String.valueOf(Integer.valueOf(review.getLike())+1));
            }else if(val== NEGATIVE_REVIEW){
                review.setDis_like(String.valueOf(Integer.valueOf(review.getDis_like())+1));
            }
            setTextButtom();
        }

    }

    public void setVal(int val) {
        this.val = val;
        setTextButtom();
    }

    private void setTextButtom() {
        if(review!=null){
            if(val== POSITIVE_REVIEW){
                setText(getContext().getString(R.string.yes) + " "+String.valueOf(review.getLike()));
            }else if(val== NEGATIVE_REVIEW){
                setText(getContext().getString(R.string.no) + " "+String.valueOf(review.getDis_like()));
            }
        }
    }
}
