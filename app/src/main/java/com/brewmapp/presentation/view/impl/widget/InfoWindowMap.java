package com.brewmapp.presentation.view.impl.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 24.02.2018.
 */

public class InfoWindowMap extends BaseLinearLayout {

    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.metro)
    TextView metro;
    @BindView(R.id.img_resto_logo)
    ImageView img_resto_logo;
    @BindView(R.id.layout_metro)
    View layout_metro;
    @BindView(R.id.layout_city)
    View layout_city;

    @Inject
    LoadRestoDetailTask loadRestoDetailTask;

    private Marker marker;
    private Target target;
    private String resto_id;

    public InfoWindowMap(Context context) {
        super(context);
    }

    public InfoWindowMap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InfoWindowMap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InfoWindowMap(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
        BeerMap.getAppComponent().plus(new PresenterModule(this)).inject(this);

    }


    public void setResto(String restoId, double locationLat, double locationLon){
        this.resto_id=restoId;
        post(new Runnable() {
            @Override
            public void run() {
                //region User Events
                setOnClickListener(v -> Starter.RestoDetailActivity((BaseActivity) getContext(), resto_id));
                //endregion

                //region Request Resto
                getRestoDetail(restoId, locationLat, locationLon, new Callback<RestoDetail>() {
                    @Override
                    public void onResult(RestoDetail restoDetail) {
                        setDistance(restoDetail);
                        setLogo(restoDetail);
                    }
                });
                //endregion

            }
        });

    }

    private void setLogo(RestoDetail restoDetail) {
        try {
            String pathImage=restoDetail.getResto().getThumb();
            if(pathImage!=null){
                target=new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        img_resto_logo.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                Picasso.with(
                        getContext()).
                        load(pathImage).
                        into(target);
            }
        }catch (Exception e){}

    }

    private void setDistance(RestoDetail restoDetail) {
        try {
            city.setText(restoDetail.getResto().getLocation().getCity_id());
        }catch (Exception e){}

        try {
            city.setText(city.getText()+" "+restoDetail.getDistance().getFormatedDistance());
        }catch (Exception e){}

        try {
            metro.setText(restoDetail.getResto().getLocation().getMetro().getName());

            layout_metro.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout_metro.setVisibility(VISIBLE);
                    layout_metro.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ValueAnimator va = ValueAnimator.ofInt(0, layout_city.getHeight());
                    va.setDuration(500);
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            Integer value = (Integer) animation.getAnimatedValue();
                            layout_metro.getLayoutParams().height = value.intValue();
                            layout_metro.requestLayout();

                        }
                    });
                    va.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            for(int i=0;i<getChildCount();i++){
                                View view=getChildAt(i);
                                if(view instanceof InfoWindowMapBeer){
                                    ((InfoWindowMapBeer) view).setMenu(restoDetail.getMenu());

                                }
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    va.start();
                }
            });
        }catch (Exception e){
            layout_metro.setVisibility(GONE);
        }


    }

    private void getRestoDetail(String restoId, double locationLat, double locationLon, Callback<RestoDetail> callback) {
        LoadRestoDetailPackage loadRestoDetailPackage=new LoadRestoDetailPackage();
        loadRestoDetailPackage.setId(String.valueOf(restoId));
        loadRestoDetailPackage.setLat(locationLat);
        loadRestoDetailPackage.setLon(locationLon);
        loadRestoDetailTask.execute(loadRestoDetailPackage,new SimpleSubscriber<RestoDetail>(){
            @Override
            public void onNext(RestoDetail restoDetail) {
                super.onNext(restoDetail);
                callback.onResult(restoDetail);
            }
        });
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void countDistanceCluster(String restoId, double locationLat, double locationLon) {
        getRestoDetail(restoId, locationLat, locationLon, new Callback<RestoDetail>() {
            @Override
            public void onResult(RestoDetail restoDetail) {
                setDistance(restoDetail);
            }
        });
    }

}
