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

    private View.OnClickListener onClickListener;

    public AddPhotoSliderView(Context context) {
        super(context);
    }
    public AddPhotoSliderView(Context context, View.OnClickListener onClickListener) {
        super(context);
        this.onClickListener=onClickListener;
    }

    @Override
    public View getView() {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.view_add_event_photo, null);
        view.setOnClickListener(onClickListener);
        return view;
    }
}
