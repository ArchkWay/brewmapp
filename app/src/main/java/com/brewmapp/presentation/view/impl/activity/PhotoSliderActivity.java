package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Photo;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhotoSliderActivity extends BaseActivity implements PhotoSliderView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.activity_photos_indicator) PagerIndicator indicator;
    @BindView(R.id.activity_photos_slider) SliderLayout slider;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getTitle());
        toolbarSubTitle.setVisibility(View.GONE);

        enableBackButton();
        slider.stopAutoCycle();
        String[] urls = getIntent().getStringArrayExtra(Keys.PHOTOS);

        ArrayList<Photo> photoArrayList = (ArrayList<Photo>) getIntent().getExtras().getSerializable(Keys.PHOTO_COUNT);
        try {
            int i=0;
            for(String url: urls) {
                CustomSliderView customSliderView;
                if(photoArrayList==null)
                    customSliderView=new CustomSliderView(this, url);
                else
                    customSliderView = new CustomSliderView(this, url,photoArrayList.get(i++));

                slider.addSlider(customSliderView);
            }
        }catch (Exception e){
            showMessage(getString(R.string.enter));
            finish();
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

    //******************************
    public static void startPhotoSliderActivity(List<Photo> photos, Context context) {
        try {
            String[] urls=new String[photos.size()];
            for(int i=0;i<photos.size();i++) {
                urls[i] = photos.get(i).getUrl();
                if(urls[i]==null)
                    urls[i]=photos.get(i).getThumb().getUrl();
            }
            if(urls.length>0){
                Intent intent = new Intent(context, PhotoSliderActivity.class);
                intent.putExtra(Keys.PHOTOS, urls);
                intent.putExtra(Keys.PHOTO_COUNT,  new ArrayList<>(photos));
                context.startActivity(intent);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
