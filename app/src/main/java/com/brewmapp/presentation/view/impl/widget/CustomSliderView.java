package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.brewmapp.R;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import ru.frosteye.ovsa.tool.UITools;


/**
 * Created by ovcst on 21.08.2017.
 */

public class CustomSliderView extends BaseSliderView {

    private String url;
    private com.github.chrisbanes.photoview.PhotoView imageView;

    public CustomSliderView(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public View getView() {
        imageView = ((com.github.chrisbanes.photoview.PhotoView) LayoutInflater
                .from(getContext()).inflate(R.layout.view_zoomable_photo, null));
        Picasso.with(getContext()).load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });

        return imageView;
    }
}
