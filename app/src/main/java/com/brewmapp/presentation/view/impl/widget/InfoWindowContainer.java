package com.brewmapp.presentation.view.impl.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;

import com.brewmapp.R;
import com.brewmapp.presentation.view.contract.InfoWindowMap_view;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowContainer extends RelativeLayout {

    private final String infoWindowMapTAG="infoWindowMapTAG";

    private View progressBar;
    private InfoWindowMap_view infoWindowMapView;
    private int finishWitdh;
    private int finishHight;
    private float ratio;

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

    public InfoWindowContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void processVisibleAnimation(){
        if(infoWindowMapView !=null) {

            //region ResetView
            if (progressBar == null)
                progressBar =  findViewById(R.id.InfoWindowMap_progressBar);

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
                        infoWindowMapView.setListenerFinishLoadData(new Handler.Callback(){
                            @Override
                            public boolean handleMessage(Message msg) {
                                //region Calculate new Size
                                AbsoluteLayout absoluteLayout = (AbsoluteLayout) getParent();
                                absoluteLayout.addView(infoWindowMapView.getView());
                                infoWindowMapView.setVisibility(INVISIBLE);
                                infoWindowMapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        //region Animation New Size
                                        infoWindowMapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                        setBackgroundColor(Color.WHITE);

                                        finishWitdh= infoWindowMapView.getWidth();
                                        finishHight= infoWindowMapView.getHeight();
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
                                        absoluteLayout.removeView(infoWindowMapView.getView());
                                        vaWidth.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                infoWindowMapView.setVisibility(VISIBLE);
                                                addView(infoWindowMapView.getView());
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
                        infoWindowMapView.requestData();
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
                    if(infoWindowMapView.getVisibility()!=VISIBLE)
                        progressBar.setVisibility(VISIBLE);
                }
            },100);
            //endregion

        }
    }


    public void clearContainer() {
        setVisibility(INVISIBLE);
        if(infoWindowMapView !=null)
            removeView(infoWindowMapView.getView());
        infoWindowMapView =null;
    }


    public void setInfoWindowLayoutListener(ViewTreeObserver.OnGlobalLayoutListener infoWindowLayoutListener) {
        this.infoWindowLayoutListener = infoWindowLayoutListener;
    }

    public void setInfoWindowMapView(InfoWindowMap_view infoWindowMapView) {
        this.infoWindowMapView = infoWindowMapView;
        this.infoWindowMapView.setVisibility(INVISIBLE);
    }

    public void setMarker(Marker marker) {
        infoWindowMapView.setMarker(marker);
    }
}
