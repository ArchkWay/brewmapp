package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerAftertaste;
import com.brewmapp.data.entity.BeerDetail;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.BeerDetailPresenter;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.view.contract.BeerDetailView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.Iterator;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class BeerDetailActivity extends  BaseActivity implements BeerDetailView {

    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_beer_detail_name)    TextView name;
    @BindView(R.id.activity_beer_detail_text_view_flavor)    TextView flavor;
    @BindView(R.id.activity_beer_detail_text_view__taste)    TextView _taste;
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

    @Inject    BeerDetailPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);
    }


    @Override
    public void setModel(BeerDetail beerDetail) {
        Beer beer=beerDetail.getBeer();
        setTitle(beer.getTitle());
        avg_cost.setText(beer.getAvg_cost());
        name.setText(beer.getTitle_ru());

        Iterator<BeerAftertaste> iterator = beer.getRelations().getBeerTaste().iterator();
        if(iterator.hasNext()) {taste.setText(iterator.next().getName());_taste.setText(iterator.next().getName());}

        avg_cost.setText(beer.getAvg_cost());
        brand.setText(beer.getRelations().getBeerBrand().getName());
        brew.setText(beer.getRelations().getBrewery().getName());
        country.setText(beer.getRelations().getCountry().getName());
        type.setText(beer.getRelations().getBeerType().getName());
        strength.setText(beer.getRelations().getBeerStrength().getName());
        density.setText(beer.getRelations().getProductDensity().getName());
        filter_beer.setText(beer.getFiltered());

        iterator = beer.getRelations().getBeerAftertaste().iterator();
        if(iterator.hasNext()) after_taste.setText(iterator .next().getName());

        description.setText(Html.fromHtml(beer.getText()));

        //ubi.setText(beer.getRelations().);
        //flavor.setText(beer.getRelations().);
        //brand.setText(beer.);

        ArrayList<String> photos=new ArrayList<>();
        if(beer.getGetThumb()!=null&&beer.getGetThumb().length()>0)
            photos.add(beer.getGetThumb());
        if(photos.size()==0){
            slider.addSlider(new DefaultSliderView(this)
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .image(R.drawable.ic_default_brewery));
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

    }


    @Override
    protected void initView() {
        enableBackButton();
        enableControls(false,0);
        String beer_id=((Interest)getIntent().getSerializableExtra(getString(R.string.key_serializable_extra))).getInterest_info().getId();
        presenter.requestBeerDetail(beer_id);

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

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

}
