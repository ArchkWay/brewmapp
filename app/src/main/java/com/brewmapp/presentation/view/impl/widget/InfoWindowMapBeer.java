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
import com.brewmapp.data.entity.MenuResto;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.LoadProductPackage;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.LoadMenu;
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
    @Inject
    LoadMenu loadMenu;

    private Target target;
    private String beer_id;
    private List<MenuResto> menu;
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


    public void setMenu(List<MenuResto> menu) {
        this.menu = menu;
        showPrice();
    }

    private void showPrice() {
        if(textViewPrice.getHeight()==0&&menu!=null) {
            try {
                for (MenuResto menuItem : menu) {
                    if (menuItem.getBeer_id().equals(beer_id)) {
                        textViewPrice.setText(menuItem.getPrice());
                        String[] capacity= getContext().getResources().getStringArray(R.array.resto_menu_capacity);
                        try {
                            textViewPrice.setText(textViewPrice.getText()+ Html.fromHtml(" &#x20bd").toString() +" / "+capacity[Integer.valueOf(menuItem.getResto_menu_capacity_id())]);
                        }catch (Exception e){}
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void requstData(String resto_id) {


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
                FullSearchPackage fullSearchPackage=new FullSearchPackage();
                fullSearchPackage.setId(resto_id);
                loadMenu.execute(fullSearchPackage,new SimpleSubscriber<ListResponse<MenuResto>>(){
                    @Override
                    public void onNext(ListResponse<MenuResto> menuRestoListResponse) {
                        super.onNext(menuRestoListResponse);
                        setMenu(menuRestoListResponse.getModels());
                        if(listenerFinishLoadData!=null)
                            listenerFinishLoadData.handleMessage(new Message());

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(listenerFinishLoadData!=null)
                            listenerFinishLoadData.handleMessage(new Message());
                    }
                });
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
