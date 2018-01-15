package com.brewmapp.presentation.view.impl.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.AverageEvaluation;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.view.contract.EventsView;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.contract.RestoDetailView;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

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
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_EDIT_BEER;


public class RestoDetailActivity extends BaseActivity implements RestoDetailView {

    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_resto_detail_name)    TextView name;
    @BindView(R.id.activity_restoDetails_slider)    SliderLayout slider;
    @BindView(R.id.activity_restoDetails_container_slider)    View container_slider;
    @BindView(R.id.activity_restoDetails_indicator)    PagerIndicator pagerIndicator;
    @BindView(R.id.activity_restoDetails_slider_photosCounter)    TextView photosCounter;
    @BindView(R.id.activity_resto_detail_text_view_site)    TextView site;
    @BindView(R.id.activity_resto_detail_text_view_description)    TextView description;
    @BindView(R.id.activity_resto_detail_text_view_interior)    TextView interior;
    @BindView(R.id.activity_resto_detail_text_view_service)    TextView service;
    @BindView(R.id.activity_resto_detail_text_view_quality_beer)    TextView beer;
    @BindView(R.id.activity_resto_detail_text_view_common_effect)    TextView effect;
    @BindView(R.id.activity_resto_detail_text_view_avg_cost)    TextView cost;
    @BindView(R.id.activity_resto_detail_button_private_message)    Button private_message;
    @BindView(R.id.activity_resto_detail_button_subscribe)    Button subscribe;
    @BindView(R.id.activity_resto_detail_button_call)    TextView call;
    @BindView(R.id.activity_resto_detail_button_call2)    TextView call1;
    @BindView(R.id.text_view_call_1)    TextView number_call;
    @BindView(R.id.text_view_call_2)    TextView number_cal2;
    @BindView(R.id.activity_resto_detail_constraintLayout)    ConstraintLayout place;
    @BindView(R.id.activity_resto_detail_text_view_place)    TextView text_view_place;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.activity_resto_detail_button_review)    Button button_revew;
    @BindView(R.id.activity_restoDetails_recycler_reviews)    RecyclerView recycler_reviews;
    @BindView(R.id.activity_restoDetails_recycler_added_favorites)    RecyclerView added_favorites;
    @BindView(R.id.activity_resto_detail_layout_news)    ViewGroup layout_news;
    @BindView(R.id.activity_resto_detail_layout_event)    ViewGroup layout_event;
    @BindView(R.id.activity_resto_detail_layout_sale)    ViewGroup layout_sale;
    @BindView(R.id.activity_resto_detail_layout_photo)    ViewGroup layout_photo;
    @BindView(R.id.activity_resto_detail_layout_menu)    ViewGroup layout_menu;
    @BindView(R.id.layout_like)    ViewGroup layout_like;
    @BindView(R.id.layout_dislike)    ViewGroup layout_dislike;
//    @BindView(R.id.layout_fav)    ViewGroup layout_fav;
    @BindView(R.id.view_like_counter)    TextView like_counter;
    @BindView(R.id.view_dislike_counter)    TextView dislike_counter;
    @BindView(R.id.activity_restoDetails_empty_text_reviews)    TextView empty_text_reviews;
    @BindView(R.id.activity_resto_detail_text_view_none8)    TextView cnt_sales;
    @BindView(R.id.activity_resto_detail_text_view_none3)    TextView cnt_news;
    @BindView(R.id.activity_resto_detail_text_view_none13)    TextView cnt_events;
    @BindView(R.id.activity_resto_detail_text_view_none23)    TextView cnt_photo;
    @BindView(R.id.activity_resto_panel_i_here)    View panel_i_here;
    @BindView(R.id.activity_resto_panel_reviews)    View panel_reviews;
    @BindView(R.id.activity_resto_panel_favorite)    View panel_favorite;
    @BindView(R.id.activity_resto_panel_i_owner)    View panel_i_owner;
    @BindView(R.id.activity_resto_panel_favorite_icon)    ImageView panel_favorite_icon;
    @BindView(R.id.activity_resto_detail_text_view_description_button)    Button button_more_description;
    @BindView(R.id.activity_resto_details_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.activity_resto_details_scroll)    ScrollView scroll;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;

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

    private Resto resto;
    private ArrayList<String> photosResto=new ArrayList<>();
    private ArrayList<String> photosRestoPreview=new ArrayList<>();
    private FlexibleAdapter adapter_reviews;
    private  swipeDelayed swipeDelayed=new swipeDelayed();
    private  ArrayList<Photo> photoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resto);
    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);
        enableBackButton();
        enableControls(false, ALL_CONTROL);
        slider.stopAutoCycle();
        place.setOnClickListener(v -> presenter.startMapFragment(RestoDetailActivity.this));
        subscribe.setOnClickListener(view -> presenter.changeSubscription());
        button_revew.setOnClickListener(view -> presenter.startAddReviewRestoActivity(RestoDetailActivity.this));
        adapter_reviews =new FlexibleAdapter<>(new ArrayList<>());
        recycler_reviews.setLayoutManager(new LinearLayoutManager(this));
        layout_news.setOnClickListener(v -> presenter.startShowEventFragment(RestoDetailActivity.this, EventsFragment.TAB_POST));
        layout_sale.setOnClickListener(v -> presenter.startShowEventFragment(RestoDetailActivity.this, EventsFragment.TAB_SALE));
        layout_event.setOnClickListener(v -> presenter.startShowEventFragment(RestoDetailActivity.this, EventsFragment.TAB_EVENT));
        layout_menu.setOnClickListener(v -> presenter.startShowMenu(RestoDetailActivity.this));
        layout_photo.setOnClickListener(v -> PhotoSliderActivity.startPhotoSliderActivity(photoArrayList,this));
        layout_like.setOnClickListener(v -> presenter.clickLikeDislike(LikeDislikePackage.TYPE_LIKE));
        layout_dislike.setOnClickListener(v -> presenter.clickLikeDislike(LikeDislikePackage.TYPE_DISLIKE));
        //layout_fav.setOnClickListener(v -> {presenter.clickFav();setResult(RESULT_OK);});
        private_message.setOnClickListener(v -> presenter.startChat(resto.getUser_id()));
        call.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number_call.getText()))));
        call1.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number_cal2.getText()))));
        button_more_description.setOnClickListener(v->setTitleToButtonOfMoreDescription(true));
        slider.setVisibility(View.INVISIBLE);
        panel_i_here.setOnClickListener(v->showMessage(getString(R.string.message_develop)));
        panel_favorite.setOnClickListener(v->{presenter.clickFav();setResult(RESULT_OK);});
        panel_i_owner.setOnClickListener(v->showMessage(getString(R.string.message_develop)));
        panel_reviews.setOnClickListener(v->showMessage(getString(R.string.message_develop)));
        added_favorites.setOnClickListener(v->showMessage(getString(R.string.message_develop)));


    }

    private void setTitleToButtonOfMoreDescription(boolean click) {
        if(button_more_description.getVisibility()==View.VISIBLE) {
            if(click) {
                if (description.getLineCount() > description.getMaxLines())
                    description.setMaxLines(description.getMaxLines() * 4);
                else
                    description.setMaxLines(getResources().getInteger(R.integer.init_max_lites_text_view));
            }

            if (getResources().getInteger(R.integer.init_max_lites_text_view) == description.getMaxLines())
                button_more_description.setText(description.getLineCount() > description.getMaxLines() ? "Читать подробнее" : "Свернуть");
            else
                button_more_description.setText(description.getLineCount() > description.getMaxLines() ? "Читать полностью" : "Свернуть");
        }else {
            button_more_description.setOnClickListener(null);
        }
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
        if(enabled){
            swipeDelayed.cancel();
            swipe.setRefreshing(false);
            swipe.setEnabled(false);
        }else{
            swipe.postDelayed(swipeDelayed,2000);
        }

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
    public void setModel(RestoDetail restoDetail, int mode) {

        switch (mode){
            case Actions.MODE_REFRESH_ALL:
                resto=restoDetail.getResto();
                try {text_view_place.setText(restoDetail.getResto().getAdressFormat());}catch (Exception e){}
                setTitle(restoDetail.getResto().getName());
                name.setText(restoDetail.getResto().getName());
                //fill photos
                presenter.loadAllPhoto(new SimpleSubscriber<List<Photo>>(){

                    @Override
                    public void onNext(List<Photo> photos) {
                        super.onNext(photos);
                        photoArrayList=new ArrayList<>(photos);

                        Iterator<Photo> iterator=photos.iterator();
                        while (iterator.hasNext()) {
                            Photo photo=iterator.next();
                            try {
                                photosResto.add(photo.getThumb().getUrl());
                                photosRestoPreview.add(photo.getThumb().getThumbUrl());
                            }catch (Exception e){}
                        }
                        fillSlider();
                    }

                    private void fillSlider() {
                        if(restoDetail.getResto().getThumb()==null&&photosRestoPreview.size()==0) {
                            slider.addSlider(new DefaultSliderView(RestoDetailActivity.this)
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .image(R.drawable.ic_default_resto)
                            );
                        }else {

                            photosRestoPreview.add(restoDetail.getResto().getThumb());
                            photosResto.add(restoDetail.getResto().getThumb());

                            for (Kitchen kitchen:restoDetail.getResto_kitchen())
                                if(kitchen.getGetThumb()!=null) {
                                    photosRestoPreview.add(kitchen.getGetThumb());
                                    photosResto.add(kitchen.getGetThumb());
                                }

                            for(String imgUrl:photosRestoPreview){
                                if(imgUrl!=null) {
                                    DefaultSliderView defaultSliderView=new DefaultSliderView(RestoDetailActivity.this);
                                    defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
                                    defaultSliderView.setPicasso(Picasso.with(slider.getContext()));
                                    defaultSliderView.image(imgUrl);
                                    defaultSliderView.setOnSliderClickListener(slider1 -> PhotoSliderActivity.startPhotoSliderActivity(photoArrayList,RestoDetailActivity.this));
                                    slider.addSlider(defaultSliderView);
                                }
                            }
                        }

                        if(photosRestoPreview.size()>0) {
                            photosCounter.setText(String.format("%d/%d", 1, photosRestoPreview.size()));
                            slider.addOnPageChangeListener(new ViewPagerEx.SimpleOnPageChangeListener() {
                                @Override
                                public void onPageSelected(int position) {
                                    photosCounter.setText(String.format("%d/%d", position + 1, photosRestoPreview.size()));
                                }
                            });
                            cnt_photo.setText(String.valueOf(photosRestoPreview.size()));
                            slider.setVisibility(View.VISIBLE);
                        }else {
                            container_slider.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        fillSlider();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        fillSlider();
                    }
                });

                site.setText(restoDetail.getResto().getSite());
                String strDescription=Html.fromHtml(restoDetail.getResto().getText()).toString();
                if(strDescription.length()>0)  description.setText(strDescription);
                cost.setText(String.valueOf(restoDetail.getResto().getAvgCost()));

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

                button_more_description.setVisibility(description.getLineCount()>description.getMaxLines()?View.VISIBLE:View.GONE);
                setTitleToButtonOfMoreDescription(false);
                try {scrollTo(Integer.valueOf(getIntent().getAction()));}catch (Exception e){}
            case Actions.MODE_REFRESH_ONLY_LIKE:
                try {like_counter.setText(restoDetail.getResto().getLike());}catch (Exception e){};
                try {dislike_counter.setText(restoDetail.getResto().getDis_like());}catch (Exception e){};

        }

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
        adapter_reviews.clear();
        adapter_reviews.addItems(0,iFlexibles);
        adapter_reviews.notifyDataSetChanged();
        recycler_reviews.setAdapter(adapter_reviews);

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
    public void setFav(boolean b) {
        panel_favorite_icon.setImageResource(b?R.drawable.ic_love_icon_green:R.drawable.ic_dislove);
    }

    @Override
    public void AverageEvaluation(List<AverageEvaluation> evaluations) {
        for (AverageEvaluation averageEvaluation:evaluations)
            switch (averageEvaluation.getEvaluation_type_id()){
                case "1":
                    interior.setText(averageEvaluation.getAverage_value());
                    break;
                case "2":
                    service.setText(averageEvaluation.getAverage_value());
                    break;
                case "3":
                    beer.setText(averageEvaluation.getAverage_value());
                    break;
                case "4":
                    effect.setText(averageEvaluation.getAverage_value());
                    break;
            }

        enableControls(true,ALL_CONTROL);
    }

    @Override
    public void scrollTo(Integer integer) {
        switch (integer){
            case Actions.ACTION_SCROLL_TO_NEWS:
                scroll.post(() -> scroll.scrollTo(0,layout_news.getTop()));
                getIntent().setAction(null);
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RequestCodes.REQUEST_SHOW_EVENT_FRAGMENT:
            case RequestCodes.REQUEST_MAP_FRAGMENT:
                if(resultCode==RESULT_OK)
                    presenter.restoreSetting();
                return;
            case RequestCodes.REQUEST_CODE_REVIEW:
                if(resultCode==RESULT_OK) {
                    enableControls(false,ALL_CONTROL);
                    presenter.refreshContent(Actions.MODE_REFRESH_ALL);
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit,menu);
        menu.findItem(R.id.action_edit).getActionView().setOnClickListener(v -> onOptionsItemSelected(menu.findItem(R.id.action_edit)));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                startActivityForResult(new Intent(MultiFragmentActivityView.MODE_RESTO_EDIT,Uri.parse(String.valueOf(resto.getId())),this,MultiFragmentActivity.class),REQUEST_EDIT_BEER);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        toolbarTitle.setText(title);
    }

    class swipeDelayed implements Runnable {
        private boolean cancel=false;
        public void cancel(){
            cancel=true;
        }
        @Override
        public void run() {
            if(cancel) return;
            swipe.setEnabled(true);
            swipe.setRefreshing(true);
        }

    }
}
