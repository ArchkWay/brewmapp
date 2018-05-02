package com.brewmapp.presentation.view.impl.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import com.brewmapp.data.entity.Menu;
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
    private List<Menu> menu;
    private Handler.Callback listenerFinishLoadData;

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
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showPrice();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
                va.start();
                //endregion

    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
        showPrice();
    }

    private void showPrice() {
        if(textViewPrice.getHeight()==0&&menu!=null) {
            try {
                for (Menu menuItem : menu) {
                    if (menuItem.getBeer_id().equals(beer_id)) {
                        textViewPrice.setText(menuItem.getPrice());
                        String[] capacity= getContext().getResources().getStringArray(R.array.resto_menu_capacity);
                        try {
                            textViewPrice.setText(textViewPrice.getText()+ Html.fromHtml(" &#x20bd").toString() +" / "+capacity[Integer.valueOf(menuItem.getResto_menu_capacity_id())]);
                        }catch (Exception e){}
                        ValueAnimator va = ValueAnimator.ofInt(0, textView.getHeight());
                        va.setDuration(500);
                        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                textViewPrice.setVisibility(VISIBLE);
                                textViewPrice.setHeight((Integer) animation.getAnimatedValue());
                            }
                        });
                        va.start();
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void requstData() {


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
                if(listenerFinishLoadData!=null)
                    listenerFinishLoadData.handleMessage(new Message());
            }
        });


    }

    public void setListenerFinishLoadData(Handler.Callback listenerFinishLoadData) {
        this.listenerFinishLoadData = listenerFinishLoadData;
    }

    public Handler.Callback getListenerFinishLoadData() {
        return listenerFinishLoadData;
    }
}
