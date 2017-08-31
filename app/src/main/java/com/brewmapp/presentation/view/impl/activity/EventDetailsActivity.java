package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Event;
import com.brewmapp.presentation.presenter.contract.EventDetailsPresenter;
import com.brewmapp.presentation.view.contract.EventDetailsView;

import butterknife.BindView;
import ru.frosteye.ovsa.data.storage.ActiveBox;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.AddPhotoSliderView;
import com.brewmapp.presentation.view.impl.widget.InfoCounter;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;

public class EventDetailsActivity extends BaseActivity implements EventDetailsView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_eventDetails_indicator) PagerIndicator indicator;
    @BindView(R.id.activity_eventDetails_options) RecyclerView options;
    @BindView(R.id.activity_eventDetails_photosCounter) TextView photosCounter;
    @BindView(R.id.activity_eventDetails_rating) TextView rating;
    @BindView(R.id.activity_eventDetails_slider) SliderLayout slider;
    @BindView(R.id.activity_eventDetails_title) TextView title;
    @BindView(R.id.activity_eventDetails_dislikes) TextView dislikes;
    @BindView(R.id.activity_eventDetails_likes) TextView likes;
    @BindView(R.id.activity_eventDetails_willGo) InfoCounter willGo;
    @BindView(R.id.activity_eventDetails_invited) InfoCounter invited;
    @BindView(R.id.activity_eventDetails_interested) InfoCounter interested;
    @BindView(R.id.activity_eventDetails_text) TextView text;

    @Inject EventDetailsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void enableControls(boolean enabled, int code) {
        showTopBarLoading(!enabled);
    }

    @Override
    protected void initView() {
        enableBackButton();
        slider.stopAutoCycle();
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
    public void refreshState() {

    }

    @Override
    public void showEventsDetails(Event event) {
        setTitle(event.getName());
        title.setText(event.getName());
        likes.setText(String.valueOf(event.getLike()));
        dislikes.setText(String.valueOf(event.getDislike()));
        rating.setText(String.valueOf(event.getBall().getRating()));
        slider.addSlider(new AddPhotoSliderView(this));
        photosCounter.setText("0");
        willGo.setCount(event.getiWillGo());
        interested.setCount(event.getInterested());
        invited.setCount(event.getInvited());
        text.setText(event.getText() != null ? Html.fromHtml(event.getText()) : null);
    }
}
