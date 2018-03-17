package com.brewmapp.presentation.view.impl.activity;


import android.animation.ObjectAnimator;
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
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.AverageEvaluation;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.Review;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.view.contract.EventsView;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.contract.ProfileEditView;
import com.brewmapp.presentation.view.contract.RestoDetailView;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;
import com.brewmapp.presentation.view.impl.widget.AddPhotoSliderView;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_EDIT_BEER;


public class RestoDetailActivity extends BaseActivity implements RestoDetailView {

    //region BindView
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
    @BindView(R.id.layout_view_call)  View layout_view_call;
    @BindView(R.id.layout_view_call_2)  View layout_view_call_2;
    @BindView(R.id.activity_resto_detail_constraintLayout)    ConstraintLayout place;
    @BindView(R.id.activity_resto_detail_text_view_place)    TextView text_view_place;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.activity_resto_detail_button_review)    Button button_revew;
    @BindView(R.id.activity_resto_detail_button_review_layout)    View button_review_layout;
    @BindView(R.id.activity_restoDetails_recycler_reviews)    RecyclerView recycler_reviews;
    @BindView(R.id.activity_restoDetails_recycler_added_favorites)    RecyclerView added_favorites;
    @BindView(R.id.activity_resto_detail_layout_news)    ViewGroup layout_news;
    @BindView(R.id.activity_resto_detail_layout_event)    ViewGroup layout_event;
    @BindView(R.id.activity_resto_detail_layout_sale)    ViewGroup layout_sale;
    @BindView(R.id.activity_resto_detail_layout_photo)    ViewGroup layout_photo;
    @BindView(R.id.activity_resto_detail_layout_menu)    ViewGroup layout_menu;
    @BindView(R.id.layout_like)    ViewGroup layout_like;
    @BindView(R.id.layout_dislike)    ViewGroup layout_dislike;
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
    @BindView(R.id.activity_resto_details_container_added_to_favorite)    View container_added_to_favorite;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    @BindView(R.id.activity_resto_details_root)    View root;

    @BindViews({
            R.id.activity_resto_detail_constraintLayout,
            R.id.activity_resto_detail_button_call2,
            R.id.activity_resto_detail_button_call,
            R.id.activity_resto_detail_button_subscribe,
            R.id.activity_resto_detail_button_private_message,
            R.id.activity_resto_panel_reviews,
            //R.id.activity_resto_panel_i_here,
            R.id.activity_resto_panel_favorite,
            R.id.activity_resto_panel_i_owner,
            R.id.activity_resto_detail_text_view_description_button,
            R.id.activity_resto_detail_layout_news,
            R.id.activity_resto_detail_layout_event,
            R.id.activity_resto_detail_layout_sale,
            R.id.activity_resto_detail_layout_photo,
            R.id.activity_resto_detail_layout_menu,
            R.id.layout_like,
            R.id.layout_dislike,
            R.id.activity_resto_details_container_added_to_favorite
    })    List<View> viewList;

    //endregion

    //region Inject
    @Inject RestoDetailPresenter presenter;
    //endregion

    //region Private
    private final int ALL_CONTROL =0;
    private Resto resto;
    private ArrayList<String> photoArrayListPreview =new ArrayList<>();
    private FlexibleAdapter adapter_favorites;
    private FlexibleAdapter adapter_reviews;
    private  ArrayList<Photo> photoArrayList;

    //endregion

    //region Impl RestoDetailActivity
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
        button_revew.setOnClickListener(view -> Starter.AddReviewRestoActivityForResult(RestoDetailActivity.this,presenter.getRestoDetails(),RequestCodes.REQUEST_CODE_REVIEW));
        adapter_reviews =new FlexibleModelAdapter<>(new ArrayList<>(), this::processItemClickAction);
        adapter_favorites=new FlexibleModelAdapter<>(new ArrayList<>(), this::processItemClickAction);
        recycler_reviews.setLayoutManager(new LinearLayoutManager(this));
        layout_news.setOnClickListener(v -> presenter.startShowEventFragment(RestoDetailActivity.this, EventsFragment.TAB_POST));
        layout_sale.setOnClickListener(v -> presenter.startShowEventFragment(RestoDetailActivity.this, EventsFragment.TAB_SALE));
        layout_event.setOnClickListener(v -> presenter.startShowEventFragment(RestoDetailActivity.this, EventsFragment.TAB_EVENT));
        layout_menu.setOnClickListener(v -> presenter.startShowMenu(RestoDetailActivity.this));
        layout_photo.setOnClickListener(v -> PhotoSliderActivity.startPhotoSliderActivity(photoArrayList,this));
        layout_like.setOnClickListener(v -> presenter.clickLikeDislike(LikeDislikePackage.TYPE_LIKE));
        layout_dislike.setOnClickListener(v -> presenter.clickLikeDislike(LikeDislikePackage.TYPE_DISLIKE));
        private_message.setOnClickListener(v -> presenter.startChat(this,resto.getUser_id()));
        call.setOnClickListener(v -> callPhone(number_call.getText().toString()));
        call1.setOnClickListener(v -> callPhone(number_cal2.getText().toString()));
        button_more_description.setOnClickListener(v->setTitleToButtonOfMoreDescription(true));
        panel_i_here.setAlpha(0.5f);
        //panel_i_here.setOnClickListener(v->showMessage(getString(R.string.message_develop)));

        panel_favorite.setOnClickListener(v->{presenter.clickFav();setResult(RESULT_OK);});
        panel_i_owner.setOnClickListener(v->Starter.MultiFragmentActivity_MODE_FORM_I_OWNER(this));
        panel_reviews.setOnClickListener(v->Starter.MultiListActivity_MODE_SHOW_REVIEWS_RESTO(RestoDetailActivity.this,String.valueOf(resto.getId())));
        added_favorites.setLayoutManager(new LinearLayoutManager(this));
        added_favorites.setAdapter(adapter_favorites);
        layout_view_call.setVisibility(View.GONE);
        layout_view_call_2.setVisibility(View.GONE);

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
//        ButterKnife.apply(viewList, (ButterKnife.Action<View>) (view, index) -> {
//                view.setEnabled(enabled);
//                view.setAlpha(enabled?1.0f:0.5f);
//        });
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
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

    @Override
    protected void onResume() {
        super.onResume();

        if((getIntent().getFlags()^Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)==0)
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO your magic code to be run
                    presenter.sendResultReceiver(Actions.ACTION_STOP_PROGRESS_BAR);
                }

            },500);


    }

    //endregion

    //region Imp RestoDetailView
    @Override
    public void setModel(RestoDetail restoDetail, int mode) {

        switch (mode){
            case Actions.MODE_REFRESH_ALL:

                //region fill Texts
                resto=restoDetail.getResto();
                try {text_view_place.setText(restoDetail.getResto().getAdressFormat());}catch (Exception e){}
                setTitle(restoDetail.getResto().getName());
                name.setText(restoDetail.getResto().getName());
                //region Phone
                try {
                    JSONObject jsonObject=new JSONObject(restoDetail.getResto().getAdditional_data());
                    JSONArray jsonArray=jsonObject.getJSONArray("phones");
                    for (int i=0;i<2;i++)
                        switch (i){
                            case 0:
                                number_call.setText(jsonArray.getString(i));
                                layout_view_call.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                number_cal2.setText(jsonArray.getString(i));
                                layout_view_call_2.setVisibility(View.VISIBLE);
                                break;
                        }

                }catch (Exception e){};
                //endregion
                site.setText(restoDetail.getResto().getSite());
                cost.setText(String.valueOf(restoDetail.getResto().getAvgCost()));
                //region Description
                String strDescription=Html.fromHtml(restoDetail.getResto().getText()).toString();
                if(strDescription.length()>0)  description.setText(strDescription);
                setTitleToButtonOfMoreDescription(false);
                button_more_description.setVisibility(description.getLineCount()>description.getMaxLines()?View.VISIBLE:View.GONE);
                //endregion
                //endregion

                //region fill Photo
                fillSlider(restoDetail);

                presenter.loadAllPhoto(new SimpleSubscriber<List<Photo>>(){

                    @Override
                    public void onNext(List<Photo> photos) {
                        super.onNext(photos);
                        photoArrayList=new ArrayList<>(photos);
                        Iterator<Photo> iterator=photos.iterator();
                        while (iterator.hasNext()) try {photoArrayListPreview.add(iterator.next().getThumb().getThumbUrl());}catch (Exception e){}
                        cnt_photo.setText(String.valueOf(photoArrayListPreview.size()));
                    }
                });
                //endregion

                try {scrollTo(Integer.valueOf(getIntent().getAction()));}catch (Exception e){}

                activityReorderToTop();


            case Actions.MODE_REFRESH_ONLY_LIKE:

                try {like_counter.setText(restoDetail.getResto().getLike());}catch (Exception e){}
                try {dislike_counter.setText(restoDetail.getResto().getDis_like());}catch (Exception e){}

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
    public void addItemsAddedToFavorite(List<IFlexible> iFlexibles) {
        container_added_to_favorite.setVisibility(iFlexibles.size()==0?View.GONE:View.VISIBLE);
        adapter_favorites.clear();
        adapter_favorites.addItems(0,iFlexibles);
        adapter_favorites.notifyItemRangeInserted(0,iFlexibles.size());

    }

    //endregion

    //region Functions
    private void fillSlider(RestoDetail restoDetail) {

        if(restoDetail.getResto().getThumb()!=null) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(RestoDetailActivity.this);
            defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            defaultSliderView.setPicasso(Picasso.with(slider.getContext()));
            defaultSliderView.image(restoDetail.getResto().getThumb());
            defaultSliderView.setOnSliderClickListener(slider1 -> PhotoSliderActivity.startPhotoSliderActivity(photoArrayList, RestoDetailActivity.this));
            slider.addSlider(defaultSliderView);
        }else {
            slider.addSlider(new DefaultSliderView(RestoDetailActivity.this)
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .image(R.drawable.ic_default_resto)
            );
        }
        slider.addSlider(new AddPhotoSliderView(RestoDetailActivity.this, v -> {addPhoto();}));

        int mountSliders=2;
        photosCounter.setText(String.format(Locale.getDefault(),"%d/%d", 1, mountSliders));
        slider.addOnPageChangeListener(new ViewPagerEx.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                photosCounter.setText(String.format(Locale.getDefault(),"%d/%d", position + 1, mountSliders));
            }
        });

    }

    private void addPhoto() {
        showSelect(this, R.array.avatar_options_mini, (text, position) -> {
            switch (position) {
                case 0:takeFromGallery(); break;
                case 1:takePhoto(); break;
            }
        });

    }

    private void takePhoto() {
        RxPaparazzo.single(this)
                .usingCamera()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.resultCode() != RESULT_OK) {
                        return;
                    }
                    response.targetUI().onImageReady(response.data().getFile());
                });
    }

    private void takeFromGallery() {
        RxPaparazzo.single(this)
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.resultCode() != RESULT_OK) {
                        return;
                    }
                    response.targetUI().onImageReady(response.data().getFile());
                });

    }

    void onImageReady(File file){
        presenter.uploadPhoto(file, new Callback<Integer>() {
            @Override
            public void onResult(Integer result) {
                switch (result){
                    case RESULT_OK:
                        showMessage(getString(android.R.string.ok));
                        break;
                    case RESULT_CANCELED:
                        showMessage(getString(android.R.string.cancel));
                        break;
                }
            }
        });
    }

    private void setTitleToButtonOfMoreDescription(boolean click) {
        if(button_more_description.getVisibility()==View.VISIBLE) {
            if(click) {
                if (description.getMaxLines() > 0) {
                    int k = description.getLineCount() / description.getMaxLines() + 1;
                    ObjectAnimator.ofInt(description, "height", description.getHeight() * k).setDuration(1000).start();
                    button_more_description.setVisibility(View.GONE);
                }
            }
            button_more_description.setText("Читать полностью");
        }else {
            button_more_description.setOnClickListener(null);
        }
    }

    private void processItemClickAction(int action, Object payload){
        switch (action){
            case Actions.ACTION_CLICK_ON_ITEM_USER: {
                Starter.ProfileEditActivityForResult(
                        this,
                        String.valueOf(ProfileEditView.SHOW_FRAGMENT_VIEW),
                        String.valueOf(((Interest) payload).getUser_info().getId()),
                        RequestCodes.REQUEST_CODE_REFRESH_ITEMS
                );
            }
                break;
            case Actions.ACTION_CLICK_ON_ITEM_REVIEW_ON_USER: {
                Starter.ProfileEditActivityForResult(
                        this,
                        String.valueOf(ProfileEditView.SHOW_FRAGMENT_VIEW),
                        ((Review) payload).getUser_id(),
                        RequestCodes.REQUEST_CODE_REFRESH_ITEMS
                );
            }
                break;
        }
    }

    private void scrollTo(Integer integer) {
        switch (integer){
            case ACTION_SCROLL_TO_NEWS:
                scroll.postDelayed(() ->
                                ObjectAnimator.ofInt(scroll, "scrollY",  layout_news.getTop()).setDuration(1000).start()
                        ,300);
                getIntent().setAction(null);
                break;
            case ACTION_SCROLL_TO_ADD_REVIEWS:
                scroll.postDelayed(() ->
                                ObjectAnimator.ofInt(scroll, "scrollY",  button_review_layout.getBottom()).setDuration(1000).start()
                        ,300);
                getIntent().setAction(null);
                break;
        }
    }


    //endregion


}
