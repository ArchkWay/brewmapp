package com.brewmapp.presentation.view.impl.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Feature;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.Menu;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.view.contract.RestoDetailView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

public class RestoDetailActivity extends BaseActivity implements RestoDetailView {

    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_resto_detail_name)    TextView name;
    @BindView(R.id.activity_restoDetails_slider)    SliderLayout slider;
    @BindView(R.id.activity_resto_detail_text_view_site)    TextView site;
    @BindView(R.id.activity_resto_detail_text_view_description)    TextView description;
    @BindView(R.id.activity_resto_detail_text_view_interior)    TextView interior;
    @BindView(R.id.activity_resto_detail_text_view_service)    TextView service;
    @BindView(R.id.activity_resto_detail_text_view_quality_beer)    TextView beer;
    @BindView(R.id.activity_resto_detail_text_view_common_effect)    TextView effect;
    @BindView(R.id.activity_resto_detail_text_view_avg_cost)    TextView cost;
    @BindView(R.id.activity_resto_detail_button_private_message)    Button private_message;
    @BindView(R.id.activity_resto_detail_button_subscribe)    Button subscribe;
    @BindView(R.id.activity_resto_detail_button_call)    Button call;
    @BindView(R.id.activity_resto_detail_button_call1)    Button call1;
    @BindView(R.id.activity_resto_detail_constraintLayout)    ConstraintLayout place;

    @BindViews({
            R.id.activity_resto_detail_constraintLayout,
            R.id.activity_resto_detail_button_call1,
            R.id.activity_resto_detail_button_call,
            R.id.activity_resto_detail_button_subscribe,
            R.id.activity_resto_detail_button_private_message,
            R.id.activity_restoDetails_slider


    })    List<View> viewList;

    @Inject RestoDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resto);
    }

    @Override
    protected void initView() {
        enableBackButton();
        enableControls(false, 0);
        String resto_id = ((Interest) getIntent().getSerializableExtra(RESTO_ID)).getInterest_info().getId();
        presenter.requestRestoDetail(resto_id);
        slider.stopAutoCycle();
        place.setOnClickListener(v -> onClickPlace());
    }

    private void onClickPlace() {

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

    @Override
    public void enableControls(boolean enabled, int code) {
        ButterKnife.apply(viewList, (ButterKnife.Action<View>) (view, index) -> {
            view.setEnabled(enabled);
            view.setClickable(enabled);
        });
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void setModel(RestoDetail restoDetail) {
        setTitle(restoDetail.getResto().getName());
        name.setText(restoDetail.getResto().getName());
        if(restoDetail.getResto().getThumb()==null) {
            slider.addSlider(new DefaultSliderView(this)
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .image(R.drawable.ic_default_brewery));
        }else {
            slider.addSlider(new DefaultSliderView(this)
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .image(restoDetail.getResto().getThumb())
                    .setOnSliderClickListener(slider1 -> {
                        Intent intent = new Intent(this, PhotoSliderActivity.class);
                        String[] urls = {restoDetail.getResto().getThumb()};
                        intent.putExtra(Keys.PHOTOS, urls);
                        startActivity(intent);
                    }));
        }
        ArrayList<String> photos=new ArrayList<>();
        for (Kitchen kitchen:restoDetail.getResto_kitchen())
            if(kitchen.getGetThumb()!=null)
                photos.add(kitchen.getGetThumb());
        for (Menu menu:restoDetail.getMenu())
            if(menu.getGetThumb()!=null)
                photos.add(menu.getGetThumb());
        for (Feature feature:restoDetail.getResto_feature())
            if(feature.getGetThumb()!=null)
                photos.add(feature.getGetThumb());


        for(String imgUrl:photos){
            if(imgUrl!=null)
                slider.addSlider(new DefaultSliderView(this)
                        .setScaleType(BaseSliderView.ScaleType.CenterInside)
                        .image(imgUrl)
                        .setOnSliderClickListener(slider1 -> {
                            Intent intent = new Intent(this, PhotoSliderActivity.class);
                            String[] urls = {imgUrl};
                            intent.putExtra(Keys.PHOTOS, urls);
                            startActivity(intent);
                        }));
        }

        site.setText(restoDetail.getResto().getSite());
        description.setText(Html.fromHtml(restoDetail.getResto().getText()));
        cost.setText(String.valueOf(restoDetail.getResto().getAvgCost()));

        enableControls(true,0);
    }
}
