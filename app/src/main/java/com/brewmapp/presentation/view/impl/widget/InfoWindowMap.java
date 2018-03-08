package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.execution.task.LoadRestoDetailTask;
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
    @BindView(R.id.img_resto_logo)
    ImageView img_resto_logo;

    @Inject
    LoadRestoDetailTask loadRestoDetailTask;

    private Marker marker;
    private Target target;

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

    public void countDistanceResto(String restoId, double locationLat, double locationLon){
        getRestoDetail(restoId, locationLat, locationLon, new Callback<RestoDetail>() {
            @Override
            public void onResult(RestoDetail restoDetail) {
                setDistance(restoDetail);
                setLogo(restoDetail);
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
                        refreshInfoWindow();
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
            city.setText(restoDetail.getDistance().getFormatedDistance());
            refreshInfoWindow();
        }catch (Exception e){}
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

    public void refreshInfoWindow() {
        if(marker!=null){
            //marker.hideInfoWindow();
            marker.showInfoWindow();
        }
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
