package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.PhotoDetails;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.LoadPhotoTask;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.security.Key;
import java.util.List;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.tool.UITools;


/**
 * Created by ovcst on 21.08.2017.
 */

public class CustomSliderView extends BaseSliderView {

    private String url;
    private com.github.chrisbanes.photoview.PhotoView imageView;
    private Photo photo;


    public CustomSliderView(Context context, String url) {
        super(context);
        this.url = url;
        photo=null;
    }

    public CustomSliderView(Context context, String url, Photo photo) {
        super(context);
        this.url = url;
        this.photo= photo;

    }

    @Override
    public View getView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_zoomable_photo, null);
        imageView = ((com.github.chrisbanes.photoview.PhotoView) view.findViewById(R.id.view_custom_slider_image));
        final TextView date= (TextView) view.findViewById(R.id.view_custom_slider_date);
        final TextView avtor= (TextView) view.findViewById(R.id.view_custom_slider_avtor);

        Picasso.with(getContext()).load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });

        try {
            WrapperParams wrapperParams=new WrapperParams(Wrappers.PHOTO);
            if(photo.getInfo()!=null)
                wrapperParams.addParam(Keys.ID,photo.getInfo().getId());
            else
                wrapperParams.addParam(Keys.ID,photo.getId());
            new LoadPhotoTask(
                    BeerMap.getAppComponent().mainThread(),
                    BeerMap.getAppComponent().executor(),
                    BeerMap.getAppComponent().api()
            ).execute(wrapperParams,new SimpleSubscriber<List<PhotoDetails>>() {
                @Override
                public void onNext(List<PhotoDetails> list) {
                    super.onNext(list);
                    try {
                        PhotoDetails photoDetails = list.get(0);
                        date.setText(photoDetails.getTimestampFormated());
                        avtor.setText(photoDetails.getUser_info().getFormattedName());
                    }catch (Exception e){}
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
        }catch (Exception e){};
        return view ;
    }
}
