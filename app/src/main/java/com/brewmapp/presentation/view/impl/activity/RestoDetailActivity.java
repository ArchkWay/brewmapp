package com.brewmapp.presentation.view.impl.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.view.contract.EventsView;
import com.brewmapp.presentation.view.contract.RestoDetailView;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class RestoDetailActivity extends BaseActivity implements RestoDetailView {

    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_resto_detail_name)    TextView name;
    @BindView(R.id.activity_restoDetails_slider)    SliderLayout slider;
    @BindView(R.id.activity_restoDetails_indicator)    PagerIndicator pagerIndicator;
    @BindView(R.id.activity_restoDetails_slider_photosCounter)    TextView photosCounter;
    @BindView(R.id.activity_resto_detail_text_view_site)    TextView site;
    @BindView(R.id.activity_resto_detail_text_view_description)    TextView description;
    //@BindView(R.id.activity_resto_detail_text_view_interior)    TextView interior;
    //@BindView(R.id.activity_resto_detail_text_view_service)    TextView service;
    //@BindView(R.id.activity_resto_detail_text_view_quality_beer)    TextView beer;
    //@BindView(R.id.activity_resto_detail_text_view_common_effect)    TextView effect;
    @BindView(R.id.activity_resto_detail_text_view_avg_cost)    TextView cost;
    @BindView(R.id.activity_resto_detail_button_private_message)    Button private_message;
    @BindView(R.id.activity_resto_detail_button_subscribe)    Button subscribe;
    @BindView(R.id.activity_resto_detail_button_call)    TextView call;
    @BindView(R.id.activity_resto_detail_button_call2)    TextView call1;
    @BindView(R.id.text_view_call_1)    TextView number_call;
    @BindView(R.id.text_view_call_2)    TextView number_cal2;
    @BindView(R.id.activity_resto_detail_constraintLayout)    ConstraintLayout place;
    @BindView(R.id.activity_resto_detail_button_review)    Button button_revew;
    @BindView(R.id.activity_restoDetails_recycler_reviews)    RecyclerView recycler_reviews;
    @BindView(R.id.activity_resto_detail_layout_news)    ViewGroup layout_news;
    @BindView(R.id.activity_resto_detail_layout_event)    ViewGroup layout_event;
    @BindView(R.id.activity_resto_detail_layout_sale)    ViewGroup layout_sale;
    @BindView(R.id.activity_resto_detail_layout_photo)    ViewGroup layout_photo;
    @BindView(R.id.activity_resto_detail_layout_menu)    ViewGroup layout_menu;
    @BindView(R.id.layout_like)    ViewGroup layout_like;
    @BindView(R.id.layout_dislike)    ViewGroup layout_dislike;
    @BindView(R.id.layout_fav)    ViewGroup layout_fav;
    @BindView(R.id.view_like_counter)    TextView like_counter;
    @BindView(R.id.view_dislike_counter)    TextView dislike_counter;
    @BindView(R.id.activity_restoDetails_empty_text_reviews)    TextView empty_text_reviews;
    @BindView(R.id.activity_resto_detail_text_view_none8)    TextView cnt_sales;
    @BindView(R.id.activity_resto_detail_text_view_none3)    TextView cnt_news;
    @BindView(R.id.activity_resto_detail_text_view_none13)    TextView cnt_events;

    @BindViews({
            R.id.activity_resto_detail_constraintLayout,
            R.id.activity_resto_detail_button_call2,
            R.id.activity_resto_detail_button_call,
            R.id.activity_resto_detail_button_subscribe,
            R.id.activity_resto_detail_button_private_message,
            R.id.activity_restoDetails_slider
    })    List<View> viewList;

    @Inject RestoDetailPresenter presenter;

    private final int ALL_CONTROL =0;

    private ArrayList<String> photosResto=new ArrayList<>();
    private FlexibleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resto);
    }

    @Override
    protected void initView() {
        enableBackButton();
        enableControls(false, ALL_CONTROL);
        slider.stopAutoCycle();
        place.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        subscribe.setOnClickListener(view -> presenter.changeSubscription());
        button_revew.setOnClickListener(view -> {presenter.startAddReviewRestoActivity(RestoDetailActivity.this);});
        adapter=new FlexibleAdapter<>(new ArrayList<>());
        recycler_reviews.setLayoutManager(new LinearLayoutManager(this));
        layout_news.setOnClickListener(v -> presenter.startShowEventFragment(RestoDetailActivity.this, EventsFragment.TAB_POST));
        layout_sale.setOnClickListener(v -> presenter.startShowEventFragment(RestoDetailActivity.this, EventsFragment.TAB_SALE));
        layout_event.setOnClickListener(v -> presenter.startShowEventFragment(RestoDetailActivity.this, EventsFragment.TAB_EVENT));
        layout_menu.setOnClickListener(v -> presenter.startShowMenu(RestoDetailActivity.this));
        layout_photo.setOnClickListener(v -> presenter.startShowPhoto(RestoDetailActivity.this));
        layout_like.setOnClickListener(v -> presenter.clickLike());
        layout_dislike.setOnClickListener(v -> presenter.clickDisLike());
        layout_fav.setOnClickListener(v -> presenter.clickFav());
        private_message.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        call.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number_call.getText()))));
        call1.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number_cal2.getText()))));

    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.parseIntent(getIntent());
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
            if(code == ALL_CONTROL) {
                view.setEnabled(enabled);
                view.setClickable(enabled);
            }
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
        photosResto.clear();
        if(restoDetail.getResto().getThumb()==null) {
            slider.addSlider(new DefaultSliderView(this)
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .image(R.drawable.ic_default_resto)
            );
        }else {
            photosResto.add(restoDetail.getResto().getThumb());
        }
        for (Kitchen kitchen:restoDetail.getResto_kitchen())
            if(kitchen.getGetThumb()!=null)
                photosResto.add(kitchen.getGetThumb());

        for(String imgUrl:photosResto){
            if(imgUrl!=null)
                slider.addSlider(new DefaultSliderView(this)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .image(imgUrl)
                        .setOnSliderClickListener(slider1 -> {
                            Intent intent = new Intent(this, PhotoSliderActivity.class);
                            String[] urls = {imgUrl};
                            intent.putExtra(Keys.PHOTOS, urls);
                            startActivity(intent);
                        }));
        }
        if(photosResto.size()>0) {
            photosCounter.setText(String.format("%d/%d", 1, photosResto.size()));
            slider.addOnPageChangeListener(new ViewPagerEx.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    photosCounter.setText(String.format("%d/%d", position + 1, photosResto.size()));
                }
            });
        }else {
            pagerIndicator.setVisibility(View.GONE);
            photosCounter.setText("0/0");
        }

        site.setText(restoDetail.getResto().getSite());
        description.setText(Html.fromHtml(restoDetail.getResto().getText()));
        cost.setText(String.valueOf(restoDetail.getResto().getAvgCost()));

        try {like_counter.setText(restoDetail.getResto().getLike());}catch (Exception e){};
        try {dislike_counter.setText(restoDetail.getResto().getDis_like());}catch (Exception e){};

        try {
            JSONObject jsonObject=new JSONObject(restoDetail.getResto().getAdditional_data());
            JSONArray jsonArray=jsonObject.getJSONArray("phones");
            for (int i=0;i<2;i++)
                switch (i){
                    case 0:
                        number_call.setText(jsonArray.getString(i));
                        break;
                    case 1:
                        number_cal2.setText(jsonArray.getString(i));
                        break;
                }

        }catch (Exception e){};

        enableControls(true,ALL_CONTROL);



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
    public void SubscriptionExist(boolean b) {
        subscribe.setText(b?getString(R.string.button_text_unsubscribe):getString(R.string.button_text_subscribe));
        setResult(RESULT_OK);
    }

    @Override
    public void setReviews(List<IFlexible> iFlexibles) {

        empty_text_reviews.setVisibility(iFlexibles.size()==0?View.VISIBLE:View.GONE);
        adapter.clear();
        adapter.addItems(0,iFlexibles);
        adapter.notifyDataSetChanged();
        recycler_reviews.setAdapter(adapter);

    }

    @Override
    public void setCnt(int size, int mode) {

        switch (mode){
            case EventsView.MODE_EVENTS:
                cnt_events.setText(String.valueOf(size));
                break;
            case EventsView.MODE_NEWS:
                cnt_news.setText(String.valueOf(size));
                break;
            case EventsView.MODE_SALES:
                cnt_sales.setText(String.valueOf(size));
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RequestCodes.REQUEST_SHOW_EVENT_FRAGMENT:
                if(resultCode==RESULT_OK)
                    presenter.restoreSetting();
                return;
            case RequestCodes.REQUEST_CODE_REVIEW_RESTO:
                if(resultCode==RESULT_OK) {
                    enableControls(false,ALL_CONTROL);
                    presenter.refreshContent();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
