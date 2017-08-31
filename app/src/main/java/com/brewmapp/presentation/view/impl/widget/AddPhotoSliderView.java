package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.brewmapp.R;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

/**
 * Created by oleg on 31.08.17.
 */

public class AddPhotoSliderView extends BaseSliderView {
    public AddPhotoSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.view_add_event_photo, null);
    }
}
