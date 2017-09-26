package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.CardMenuField;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.pojo.ClaimPackage;
import com.brewmapp.data.pojo.SimpleLocation;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.EventDetailsPresenter;
import com.brewmapp.presentation.view.contract.EventDetailsView;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import ru.frosteye.ovsa.data.storage.ActiveBox;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.IPrompt;
import ru.frosteye.ovsa.presentation.view.SimplePrompt;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.listener.SelectListener;
import ru.frosteye.ovsa.tool.UITools;

import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.AddPhotoSliderView;
import com.brewmapp.presentation.view.impl.widget.InfoCounter;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends BaseActivity implements EventDetailsView, FlexibleAdapter.OnItemClickListener {

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
    @Inject ActiveBox activeBox;

    private FlexibleAdapter<CardMenuField> optionsAdapter;
    private Event event;

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
        optionsAdapter = new FlexibleAdapter<>(new ArrayList<>(), this);
        options.setLayoutManager(new LinearLayoutManager(this));
        options.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        options.setAdapter(optionsAdapter);
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
        this.event = event;
        optionsAdapter.updateDataSet(CardMenuField.createEventItems(event, this));
        setTitle(event.getName());
        title.setText(event.getName());
        likes.setText(String.valueOf(event.getLike()));
        dislikes.setText(String.valueOf(event.getDislike()));
        rating.setText(String.valueOf(event.getBall().getRating()));
        if(event.getThumb() != null) {
            slider.addSlider(new DefaultSliderView(this)
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .image(event.getThumb())
                    .setOnSliderClickListener(slider1 -> {
                Intent intent = new Intent(this, PhotoSliderActivity.class);
                String[] urls = { event.getThumb() };
                intent.putExtra(Keys.PHOTOS, urls);
                startActivity(intent);
            }));
        }
        slider.addOnPageChangeListener(new ViewPagerEx.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                photosCounter.setText(String.format("%d/%d", position + 1, event.getThumb() != null ? 2 : 1));
            }
        });
        slider.addSlider(new AddPhotoSliderView(this));

        willGo.setCount(event.getiWillGo());
        interested.setCount(event.getInterested());
        invited.setCount(event.getInvited());
        text.setText(event.getText() != null ? Html.fromHtml(event.getText()) : null);
    }

    @Override
    public void showAlertDialog(String[] variants) {
        showSelect(this, variants, (text1, position) -> {
            UITools.showPrompt(this, new SimplePrompt()
                    .setTitle(getString(R.string.alert))
                    .setHint(getString(R.string.enter_message))
                    .setPositiveButton(getString(R.string.send)), new IPrompt.Listener() {
                @Override
                public void onResult(String text) {
                    ClaimPackage claimPackage = new ClaimPackage(position, event.getId(), Keys.CAP_EVENT);
                    claimPackage.setText(text);
                    presenter.onClaim(claimPackage);
                }

                @Override
                public void onCancel() {

                }
            });
        }, false);
    }

    @Override
    public boolean onItemClick(int position) {
        switch (position) {
            case 1:
                SimpleLocation location = new SimpleLocation(event.getLocation().getInfo().getLat(),
                        event.getLocation().getInfo().getLat(), event.getLocation().getFormattedAddress());
                activeBox.setActive(location);
                startActivity(UniversalMapActivity.class);
                break;
            case 2:
                share();
                break;
            case 3:
                alert();
                break;
        }
        return true;
    }

    private void alert() {
        presenter.onRequestAlertVariants();
    }

    private void share() {

    }
}
