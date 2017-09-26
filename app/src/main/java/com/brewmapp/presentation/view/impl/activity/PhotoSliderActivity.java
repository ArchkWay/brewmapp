package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.PhotoSliderPresenter;
import com.brewmapp.presentation.view.contract.PhotoSliderView;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;

import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.CustomSliderView;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

public class PhotoSliderActivity extends BaseActivity implements PhotoSliderView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;

    @BindView(R.id.activity_photos_indicator) PagerIndicator indicator;
    @BindView(R.id.activity_photos_slider) SliderLayout slider;

    @Inject PhotoSliderPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_slider);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    protected void initView() {
        enableBackButton();
        slider.stopAutoCycle();
        String[] urls = getIntent().getStringArrayExtra(Keys.PHOTOS);
        for(String url: urls) {
            slider.addSlider(new CustomSliderView(this, url));
        }
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }
}
