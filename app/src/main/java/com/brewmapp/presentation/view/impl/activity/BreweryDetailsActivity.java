package com.brewmapp.presentation.view.impl.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Brewery;
import com.brewmapp.presentation.presenter.contract.BreweryDetailsActivityPresenter;
import com.brewmapp.presentation.view.contract.BreweryDetailsActivityView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

public class BreweryDetailsActivity extends  BaseActivity implements BreweryDetailsActivityView {
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_brewery_detail_swipe)    RefreshableSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.activity_brewery_details_slider)    SliderLayout sliderLayout;
    @BindView(R.id.activity_brewery_details_name)    TextView name;
    @BindView(R.id.activity_brewery_detail_text_view_description)    TextView description;


    @Inject
    BreweryDetailsActivityPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brewery_details);
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);
        enableBackButton();
        sliderLayout.stopAutoCycle();

    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.parseIntent(getIntent());
    }

    @Override
    public void setContent(Brewery brewery) {
        setTitle(brewery.getFormatedTitle());
        name.setText(brewery.getFormatedTitle());
        if(brewery.getText()!=null)
            description.setText(Html.fromHtml(brewery.getText()).toString());
        if(brewery.getGetThumbFormated()==null)
            sliderLayout.addSlider(new DefaultSliderView(this)
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .image(R.drawable.ic_default_beer));
        else
            sliderLayout.addSlider(new DefaultSliderView(this)
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .image(brewery.getGetThumbFormated()));


    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void commonError(String... strings) {
        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();
    }


    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        toolbarTitle.setText(title);
    }
}
