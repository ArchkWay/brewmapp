package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

/**
 * Created by Kras on 18.03.2018.
 */

public class PhotoSliderView extends DefaultSliderView {

    public PhotoSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View view=super.getView();
        view.setBackgroundColor(Color.WHITE);
        return view;
    }
}
