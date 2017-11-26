package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.CardMenuField;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.pojo.ClaimPackage;
import com.brewmapp.data.pojo.SimpleLocation;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.tool.HashTagHelper2;
import com.brewmapp.presentation.presenter.contract.EventDetailsPresenter;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.view.contract.EventDetailsView;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import ru.frosteye.ovsa.data.storage.ActiveBox;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.IPrompt;
import ru.frosteye.ovsa.presentation.view.SimplePrompt;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
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
import java.util.Iterator;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;

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
    @BindView(R.id.activity_eventDetails_like)   ImageView like;
    @BindView(R.id.activity_eventDetails_dislike)   ImageView dislike;

    @Inject EventDetailsPresenter presenter;
    @Inject EventsPresenter eventsPresenter;

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        options.setNestedScrollingEnabled(false);
        options.setLayoutManager(linearLayoutManager);
        options.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        options.setAdapter(optionsAdapter);
        like.setOnClickListener(v -> {eventsPresenter.onLike(event,this);});
        dislike.setOnClickListener(v -> {presenter.onDisLakeEvent(event);});
        text.setMovementMethod(LinkMovementMethod.getInstance());
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
        likes.setText(String.valueOf(event.getLike()));
        dislikes.setText(String.valueOf(event.getDislike()));
    }

    @Override
    public void refreshContent(Event event) {
        this.event = event;

        class FillContent{
            public FillContent(){
                optionsAdapter.updateDataSet(CardMenuField.createEventItems(event, EventDetailsActivity.this));
                texts();
                images();
                counts();
            }

            private void counts() {
                willGo.setCount(event.getiWillGo());
                interested.setCount(event.getInterested());
                invited.setCount(event.getInvited());
            }

            private void images() {

                if(event.getPhoto().size()>0) {
                    String[] urls=new String[event.getPhoto().size()];
                    for(int i=0;i<urls.length;i++)
                        urls[i]=event.getPhoto().get(i).getUrl();

                    Iterator<Photo> iterator=event.getPhoto().iterator();
                    while (iterator.hasNext())
                        slider.addSlider(new DefaultSliderView(EventDetailsActivity.this)
                                .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                .image(iterator.next().getUrl())
                                .setOnSliderClickListener(slider1 -> {
                                    Intent intent = new Intent(EventDetailsActivity.this, PhotoSliderActivity.class);
                                    intent.putExtra(Keys.PHOTOS, urls);
                                    startActivity(intent);
                                }));
                }else {
                    slider.addSlider(new DefaultSliderView(EventDetailsActivity.this)
                            .setScaleType(BaseSliderView.ScaleType.CenterInside)
                            .image(R.drawable.ic_default_brewery));

                }
                slider.addOnPageChangeListener(new ViewPagerEx.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        photosCounter.setText(String.format("%d/%d", position + 1, event.getThumb() != null ? 2 : 1));
                    }
                });
                slider.addSlider(new AddPhotoSliderView(EventDetailsActivity.this));
            }

            private void texts() {
                setTitle(event.getName());
                title.setText(event.getName());
                likes.setText(String.valueOf(event.getLike()));
                dislikes.setText(String.valueOf(event.getDislike()));
                rating.setText(String.valueOf(event.getBall().getRating()));
                new HashTagHelper2(text,event.getText());
            }

        }

        new FillContent();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_REFRESH_ITEMS:
                if(resultCode==RESULT_OK){
                    setResult(RESULT_OK);
                    finish();
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
