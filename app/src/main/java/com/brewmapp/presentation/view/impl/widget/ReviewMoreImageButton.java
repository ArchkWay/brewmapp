package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Review;
import com.brewmapp.data.pojo.ClaimPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.ClaimTask;
import com.brewmapp.execution.task.LoadClaimTypesTask;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.IPrompt;
import ru.frosteye.ovsa.presentation.view.SimplePrompt;
import ru.frosteye.ovsa.tool.UITools;

/**
 * Created by Kras on 11.03.2018.
 */

public class ReviewMoreImageButton extends android.support.v7.widget.AppCompatImageButton implements View.OnClickListener{
    @Inject
    LoadClaimTypesTask loadClaimTypesTask;
    @Inject
    ClaimTask claimTask;
    private Review review;


    public ReviewMoreImageButton(Context context) {
        super(context);
    }

    public ReviewMoreImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReviewMoreImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setReview(Review review) {
        this.review = review;
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        WrapperParams wrapperParams=new WrapperParams(Keys.CAP_REVIEW);
//        wrapperParams.addParam(Keys.ID,review.getId());
//        wrapperParams.addParam(Keys.TYPE,Keys.CAP_RESTO);
        BeerMap.getAppComponent().plus(new PresenterModule(this)).inject(this);
        loadClaimTypesTask.execute(null, new SimpleSubscriber<String[]>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String[] variants) {
                BaseActivity baseActivity=(BaseActivity)getContext();
                baseActivity.showSelect(baseActivity, variants, (text1, position) -> {
                    UITools.showPrompt(baseActivity, new SimplePrompt()
                            .setTitle(baseActivity.getString(R.string.alert))
                            .setHint(baseActivity.getString(R.string.enter_message))
                            .setPositiveButton(baseActivity.getString(R.string.send)), new IPrompt.Listener() {
                        @Override
                        public void onResult(String text) {
                            ClaimPackage claimPackage;
                            claimPackage = new ClaimPackage(position, Integer.valueOf(review.getId()), Keys.CAP_REVIEW);
                            claimPackage.setText(text);
                            claimTask.execute(claimPackage,new SimpleSubscriber<Boolean>(){
                                @Override
                                public void onNext(Boolean aBoolean) {
                                    super.onNext(aBoolean);
                                    baseActivity.showSnackbar(baseActivity.getString(android.R.string.ok));
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    baseActivity.showSnackbarRed(baseActivity.getString(android.R.string.cancel));
                                }
                            });

                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }, false);

            }
        });


    }

    private void showResult() {

    }
}
