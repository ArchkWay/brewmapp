package com.brewmapp.presentation.view.impl.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;

import com.brewmapp.R;
import com.brewmapp.presentation.view.contract.InfoWindowMapContent_view;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowContainer extends CardView {

    private final String infoWindowMapTAG="infoWindowMapTAG";

    private View progressBar;
    private InfoWindowMapContent_view infoWindowMapContent_view;
    private int finishWitdh;
    private int finishHight;
    private float ratio;
    private AbsoluteLayout absoluteLayout;

    private ViewTreeObserver.OnGlobalLayoutListener infoWindowLayoutListener;


    public InfoWindowContainer(Context context) {
        super(context);
    }

    public InfoWindowContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InfoWindowContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public InfoWindowContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    public void processVisibleAnimation(){

        if(infoWindowMapContent_view !=null&&getVisibility()!=VISIBLE) {

            //region ResetView
            if (progressBar == null)
                progressBar =  findViewById(R.id.InfoWindowMap_progressBar);
            if(absoluteLayout==null)
                absoluteLayout = (AbsoluteLayout) getParent();


            absoluteLayout.setClickable(true);
            absoluteLayout.setFocusable(true);
            absoluteLayout.setFocusableInTouchMode(true);


            progressBar.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            progressBar.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            progressBar.setVisibility(INVISIBLE);
            getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            setBackgroundColor(Color.TRANSPARENT);
            //endregion

            //region ShowContent
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        //region Request Data
                        infoWindowMapContent_view.setListenerFinishLoadData(new Handler.Callback(){
                            @Override
                            public boolean handleMessage(Message msg) {
                                //region Calculate new Size

                                absoluteLayout.addView(infoWindowMapContent_view.getView());
                                infoWindowMapContent_view.setVisibility(INVISIBLE);
                                infoWindowMapContent_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        //region Animation New Size
                                        infoWindowMapContent_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                        setBackgroundColor(Color.WHITE);

                                        finishWitdh= infoWindowMapContent_view.getWidth();
                                        finishHight= infoWindowMapContent_view.getHeight();
                                        int startHight = getHeight();
                                        int startWidth=getWidth();

                                        ratio = (float) finishHight/ (float) finishWitdh;

                                        ValueAnimator vaWidth = ValueAnimator.ofInt(startWidth,finishWitdh);
                                        vaWidth.setDuration(500);
                                        vaWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator animation) {
                                                int AnimatedValue=(int) animation.getAnimatedValue();
                                                getLayoutParams().width=AnimatedValue;
                                                getLayoutParams().height= (int) Math.max(AnimatedValue*ratio,startHight);
                                                requestLayout();
                                                infoWindowLayoutListener.onGlobalLayout();
                                            }
                                        });
                                        absoluteLayout.removeView(infoWindowMapContent_view.getView());
                                        vaWidth.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                infoWindowMapContent_view.setVisibility(VISIBLE);
                                                addView(infoWindowMapContent_view.getView());
                                                absoluteLayout.setClickable(false);
                                                absoluteLayout.setFocusable(false);
                                                absoluteLayout.setFocusableInTouchMode(false);
                                            }

                                            @Override
                                            public void onAnimationStart(Animator animation) {
                                                super.onAnimationStart(animation);
                                                progressBar.setVisibility(INVISIBLE);
                                            }
                                        });
                                        vaWidth.start();
                                        //endregion

                                    }
                                });
                                //endregion
                                return false;
                            }
                        });
                        infoWindowMapContent_view.requestData();
                        //endregion
                }
            });
            requestLayout();
            //endregion

            //region ShowProgressBar
            setVisibility(VISIBLE);
            progressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(infoWindowMapContent_view.getVisibility()!=VISIBLE)
                        progressBar.setVisibility(VISIBLE);
                }
            },100);
            //endregion

        }
    }


    public void clearContainer() {
        setVisibility(INVISIBLE);
        if(infoWindowMapContent_view !=null)
            removeView(infoWindowMapContent_view.getView());
        infoWindowMapContent_view =null;
    }


    public void setInfoWindowLayoutListener(ViewTreeObserver.OnGlobalLayoutListener infoWindowLayoutListener) {
        this.infoWindowLayoutListener = infoWindowLayoutListener;
    }

    public void setInfoWindowMapContent_view(InfoWindowMapContent_view infoWindowMapContent_view) {
        this.infoWindowMapContent_view = infoWindowMapContent_view;
        this.infoWindowMapContent_view.setVisibility(INVISIBLE);
    }

    public void setMarker(Marker marker) {
        infoWindowMapContent_view.setMarker(marker);
    }
}
