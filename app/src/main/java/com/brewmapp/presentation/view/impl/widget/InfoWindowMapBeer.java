package com.brewmapp.presentation.view.impl.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.pojo.LoadProductPackage;
import com.brewmapp.execution.task.LoadProductTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 05.03.2018.
 */

public class InfoWindowMapBeer extends BaseLinearLayout {

    @BindView(R.id.infowindowmapbeer_beer_id)
    TextView textView;
    @BindView(R.id.infowindowmapbeer_price)
    TextView textViewPrice;
    @BindView(R.id.infowindowmapbeer_avatar)
    RoundedImageView imageView;
    @Inject
    LoadProductTask loadProductTask;

    private Target target;
    private String beer_id;

    public InfoWindowMapBeer(Context context) {
        super(context);
    }

    public InfoWindowMapBeer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InfoWindowMapBeer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InfoWindowMapBeer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
        BeerMap.getAppComponent().plus(new PresenterModule(this)).inject(this);

        //region User Evens
        setOnClickListener(v -> Starter.BeerDetailActivity(getContext(),beer_id));
        //endregion


    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //region set height and width

        post(new Runnable() {
            @Override
            public void run() {
                InfoWindowMap infoWindowMap= (InfoWindowMap) getParent();
                getLayoutParams().width = infoWindowMap.getWidth();
                requestLayout();

                //region Request Beer
                LoadProductPackage loadProductPackage=new LoadProductPackage();
                loadProductPackage.setId(beer_id);
                loadProductTask.execute(loadProductPackage,new SimpleSubscriber<List<IFlexible>>(){
                    @Override
                    public void onNext(List<IFlexible> iFlexibles) {
                        super.onNext(iFlexibles);
                        //region Replay Beer
                        if (iFlexibles.size() == 1) {
                            Beer beer = null;
                            try {
                                beer = ((BeerInfo) iFlexibles.get(0)).getModel();
                            } catch (Exception e) {
                                return;
                            }

                            if (beer != null) {
                                //region set Text and Image
                                textView.setText(beer.getFormatedTitle());
                                String imgUrl = beer.getGetThumb();
                                if(imgUrl!=null) {
                                    target = new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            imageView.setImageBitmap(bitmap);
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {

                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                        }
                                    };
                                    Picasso.with(
                                            getContext()).
                                            load(imgUrl).
                                            into(target);
                                }
                                //endregion
                            }
                        }
                        //endregion
                    }
                });
                //endregion

            }
        });
        //endregion
    }

    public void setBeerId(String s) {
        beer_id=s;

    }

    public void showBeer(int height) {

                //region Animation open
                setVisibility(VISIBLE);
                ValueAnimator va = ValueAnimator.ofInt(0, height);
                va.setDuration(500);
                va.addUpdateListener(animation -> {
                    Integer value = (Integer) animation.getAnimatedValue();
                    getLayoutParams().height = value.intValue();
                    requestLayout();
                });
                va.start();
                //endregion

    }
}
