package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.Averagevalue;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerDetail;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Review;
import com.brewmapp.data.entity.wrapper.ItemShowAllResto;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.BeerDetailPresenter;
import com.brewmapp.presentation.view.contract.BeerDetailView;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.contract.ProfileEditView;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_EDIT_BEER;

public class BeerDetailActivity extends  BaseActivity implements BeerDetailView{

    //region BindView
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_beer_detail_name)    TextView name;
    @BindView(R.id.activity_beer_detail_text_view_avg_cost)    TextView avg_cost;
    @BindView(R.id.activity_beer_detail_text_view_brand)    TextView brand;
    @BindView(R.id.activity_beer_detail_text_view_brew)    TextView brew;
    @BindView(R.id.activity_beer_detail_text_view_country)    TextView country;
    @BindView(R.id.activity_beer_detail_text_view_type)    TextView type;
    @BindView(R.id.activity_beer_detail_text_view_strength)    TextView strength;
    @BindView(R.id.activity_beer_detail_text_view_density)    TextView density;
    @BindView(R.id.activity_beer_detail_text_view_ubi)    TextView ubi;
    @BindView(R.id.activity_beer_detail_text_view_filter_beer)    TextView filter_beer;
    @BindView(R.id.activity_beer_detail_text_view_taste)    TextView taste;
    @BindView(R.id.activity_beer_detail_text_view_after_taste)    TextView after_taste;
    @BindView(R.id.activity_beer_detail_text_view_description)    TextView description;
    @BindView(R.id.activity_beerDetails_slider)    SliderLayout slider;
    @BindView(R.id.layout_like)    ViewGroup layout_like;
    @BindView(R.id.layout_dislike)    ViewGroup layout_dislike;
    //@BindView(R.id.layout_fav)    ViewGroup layout_fav;
    @BindView(R.id.view_like_counter)    TextView like_counter;
    @BindView(R.id.view_dislike_counter)    TextView dislike_counter;
    @BindView(R.id.activity_beer_panel_favorite_icon)    ImageView fav_icon;
    @BindView(R.id.activity_beer_panel_map)    View panel_map;
    @BindView(R.id.activity_beer_panel_reviews)    View panel_reviews;
    @BindView(R.id.activity_beer_details_recycler_reviews)    RecyclerView recycler_reviews;
    @BindView(R.id.activity_beer_detail_button_review)    Button button_review;
    @BindView(R.id.activity_beer_details_empty_text_reviews)    TextView empty_text_reviews;
    @BindView(R.id.activity_beer_detaild_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.activity_beer_details_recycler_where_they_pour)    RecyclerView recycler_where_they_pour;
    @BindView(R.id.activity_beer_details_recycler_added_to_favorite)    RecyclerView recycler_added_to_favorite;
    @BindView(R.id.activity_beer_details_container_recycler_resto)    View container_where_they_pour;
    @BindView(R.id.activity_beer_details_container_added_to_favorite)    LinearLayout container_added_to_favorite;
    @BindView(R.id.activity_beer_details_container_reviews)    View container_reviews;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    @BindView(R.id.activity_beer_panel2_aroma)    TextView panel2_aroma;
    @BindView(R.id.activity_beer_panel2_aftertaste)    TextView panel2_aftertaste;
    @BindView(R.id.activity_beer_panel2_color)    TextView panel2_color;
    @BindView(R.id.activity_beer_panel2_taste)    TextView panel2_taste;

    @BindViews({
            R.id.layout_like,
            R.id.layout_dislike,
            R.id.layout_fav
    })    List<View> viewList;

    //endregion

    //region Inject
    @Inject BeerDetailPresenter presenter;
    //endregion

    //region Private
    private FlexibleAdapter adapter_review ;
    private FlexibleAdapter adapter_where_they_pour;
    private FlexibleAdapter adapter_added_to_favorite;
    private Beer beer;
    //endregion

    //region Impl BeerDetailActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);
    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);
        enableBackButton();

        enableControls(false,0);
        slider.stopAutoCycle();
        layout_like.setOnClickListener(v -> presenter.clickLike(LikeDislikePackage.TYPE_LIKE));
        layout_dislike.setOnClickListener(v -> presenter.clickLike(LikeDislikePackage.TYPE_DISLIKE));
        fav_icon.setOnClickListener(v -> {presenter.clickFav();setResult(RESULT_OK);enableControls(false,0);});
        button_review.setOnClickListener(view -> Starter.AddReviewBeerActivityForResult(BeerDetailActivity.this,presenter.getBeerDetail().getBeer().getId(),RequestCodes.REQUEST_CODE_REVIEW));
        panel_map.setOnClickListener(v->Starter.MainActivity(this,MainActivity.MODE_MAP_FRAGMENT,null));
        panel_reviews.setOnClickListener(v-> Starter.MultiListActivity_MODE_SHOW_REVIEWS_BEER(this,beer.getId()));

        recycler_reviews.setLayoutManager(new LinearLayoutManager(this));
        recycler_where_they_pour.setLayoutManager(new LinearLayoutManager(this));
        recycler_added_to_favorite.setLayoutManager(new LinearLayoutManager(this));

        adapter_where_they_pour =new FlexibleModelAdapter<>(new ArrayList<>(),this::processItemClickAction);
        adapter_added_to_favorite =new FlexibleModelAdapter<>(new ArrayList<>(),this::processItemClickAction);
        adapter_review=new FlexibleModelAdapter<>(new ArrayList<>(),this::processItemClickAction);

        recycler_where_they_pour.setAdapter(adapter_where_they_pour);
        recycler_reviews.setAdapter(adapter_review);
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

        swipe.setRefreshing(!enabled);
        swipe.setEnabled(!enabled);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit,menu);
        menu.findItem(R.id.action_edit).getActionView().setOnClickListener(v -> onOptionsItemSelected(menu.findItem(R.id.action_edit)));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                startActivityForResult(new Intent(MultiFragmentActivityView.MODE_BEER_EDIT,Uri.parse(beer.getId()),this,MultiFragmentActivity.class),REQUEST_EDIT_BEER);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Impl BeerDetailView
    @Override
    public void setModel(BeerDetail beerDetail,int mode) {
        switch (mode){
            case Actions.MODE_REFRESH_ALL:
                beer=beerDetail.getBeer();

                String tmpStr;

                try {tmpStr=beer.getTitle();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) setTitle(beer.getFormatedTitle());
                try {tmpStr=beer.getAvg_cost();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) avg_cost.setText(tmpStr);
                try {tmpStr=beer.getTitle_ru();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) name.setText(tmpStr);
                try {tmpStr=beer.getRelations().getBeerTaste().iterator().next().getName();}catch (Exception e){tmpStr=null;}if(!TextUtils.isEmpty(tmpStr)) taste.setText(tmpStr);
                try {tmpStr=beer.getAvg_cost();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) avg_cost.setText(tmpStr);
                try {tmpStr=beer.getRelations().getBeerBrand().getName();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) brand.setText(tmpStr);
                try {tmpStr=beer.getRelations().getBrewery().getName();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) brew.setText(tmpStr);
                try {tmpStr=beer.getRelations().getCountry().getName();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) country.setText(tmpStr);
                try {tmpStr=beer.getRelations().getBeerType().getName();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) type.setText(tmpStr);
                try {tmpStr=beer.getRelations().getBeerStrength().getName();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) strength.setText(tmpStr);
                try {tmpStr=beer.getRelations().getProductDensity().getName();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) density.setText(tmpStr);
                try {tmpStr=beer.getFiltered();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) filter_beer.setText(getString(tmpStr.equals("1")?R.string.yes:R.string.no));
                try {tmpStr=beer.getRelations().getBeerAftertaste().iterator().next().getName();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) after_taste.setText(tmpStr);
                try {tmpStr=Html.fromHtml(beer.getText()).toString();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) description.setText(tmpStr);else description.setLines(1);



                ArrayList<String> photos=new ArrayList<>();
                if(beer.getGetThumb()!=null&&beer.getGetThumb().length()>0)
                    photos.add(beer.getGetThumb());
                if(photos.size()==0){
                    slider.addSlider(new DefaultSliderView(this)
                            .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                            .image(R.drawable.ic_default_beer));
                }else {
                    for(String s:photos){
                        slider.addSlider(new DefaultSliderView(this)
                                .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                .image(s)
                                .setOnSliderClickListener(slider1 -> {
                                    Intent intent = new Intent(this, PhotoSliderActivity.class);
                                    String[] urls = {s};
                                    intent.putExtra(Keys.PHOTOS, urls);
                                    startActivity(intent);
                                }));

                    }
                }
            case Actions.MODE_REFRESH_ONLY_LIKE:
                try {tmpStr=beerDetail.getBeer().getLike();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) like_counter.setText(tmpStr);
                try {tmpStr=beerDetail.getBeer().getDis_like();}catch (Exception e){tmpStr=null;} if(!TextUtils.isEmpty(tmpStr)) dislike_counter.setText(tmpStr);
        }
        enableControls(true,0);
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
    public void setFavorite(boolean b) {
        fav_icon.setImageResource(b?R.drawable.ic_love_icon_green:R.drawable.ic_dislove);
        enableControls(true,0);
    }

    @Override
    public void addItemsReviews(List<IFlexible> iFlexibles) {
            empty_text_reviews.setVisibility(iFlexibles.size() == 0 ? View.VISIBLE : View.GONE);
            adapter_review.clear();
            adapter_review.addItems(0, iFlexibles);
            adapter_review.notifyDataSetChanged();
    }

    @Override
    public void addItemsWhereTheyPour(ArrayList iFlexibles) {
        container_where_they_pour.setVisibility(iFlexibles.size()>0?View.VISIBLE:View.GONE);
        adapter_where_they_pour.clear();
        adapter_where_they_pour.addItems(0, iFlexibles);
        if(iFlexibles.size()==3)
            adapter_where_they_pour.addItem(adapter_where_they_pour.getItemCount(),new ItemShowAllResto());
        adapter_where_they_pour.notifyDataSetChanged();

    }

    @Override
    public void addItemsAddedToFavorite(List iFlexibles) {
        container_added_to_favorite.setVisibility(iFlexibles.size()>0?View.VISIBLE:View.GONE);
        adapter_added_to_favorite.clear();
        adapter_added_to_favorite.addItems(0,iFlexibles);
        adapter_added_to_favorite.notifyDataSetChanged();
        recycler_added_to_favorite.setAdapter(adapter_added_to_favorite);
    }

    @Override
    public void setProductAverageValue(List<Averagevalue> models) {
        double value=0;
        try {
            value=models.get(0).getAverage_value();
            panel2_aroma.setText(String.valueOf(value));
            panel2_aroma.setTextColor(getValueColor(value));
        }catch (Exception e){}
        try {
            value=models.get(3).getAverage_value();
            panel2_aftertaste.setText(String.valueOf(value));
            panel2_aftertaste.setTextColor(getValueColor(value));
        }catch (Exception e){}
        try {
            value=models.get(2).getAverage_value();
            panel2_color.setText(String.valueOf(value));
            panel2_color.setTextColor(getValueColor(value));
        }catch (Exception e){}
        try {
            value=models.get(1).getAverage_value();
            panel2_taste.setText(String.valueOf(value));
            panel2_taste.setTextColor(getValueColor(value));
        }catch (Exception e){}
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        toolbarTitle.setText(title);

    }
    //endregion

    //region Functions
    private void processItemClickAction(int action, Object payload){


        switch (action){
            case Actions.ACTION_SHOW_ALL_RESTO_BY_BEER:{
                Starter.ResultSearchActivity(this, SearchFragment.CATEGORY_LIST_RESTO,presenter.getBeerDetail().getBeer().getId());
            }break;
            case Actions.ACTION_CLICK_ON_ITEM_INTEREST_RESTO:{
                Starter.RestoDetailActivityForResult(this,(Interest)payload,REQUEST_CODE_REFRESH_ITEMS);
            }break;
            case Actions.ACTION_CLICK_ON_ITEM_USER: {
                startActivityForResult(
                        new Intent(
                                String.valueOf(ProfileEditView.SHOW_PROFILE_FRAGMENT_VIEW_SHOT),
                                Uri.parse(String.valueOf(((Interest) payload).getUser_info().getId())),
                                this,
                                ProfileEditActivity.class
                        ),
                        RequestCodes.REQUEST_CODE_REFRESH_ITEMS
                );
            }break;
            case Actions.ACTION_CLICK_ON_ITEM_REVIEW_ON_USER:{
                startActivityForResult(
                        new Intent(
                                String.valueOf(ProfileEditView.SHOW_PROFILE_FRAGMENT_VIEW_SHOT),
                                Uri.parse(String.valueOf(((Review) payload).getUser_id())),
                                this,
                                ProfileEditActivity.class
                        ),
                        RequestCodes.REQUEST_CODE_REFRESH_ITEMS
                );

            }break;
        }

    }
    private int getValueColor(double value) {
        int index=(int)Math.round(value);
        int[] mValueColor=getResources().getIntArray(R.array.product_average_value);
        return mValueColor[index>4?4:index];
    }
    //endregion

}
