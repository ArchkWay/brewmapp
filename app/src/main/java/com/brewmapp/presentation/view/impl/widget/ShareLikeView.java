package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.presentation.presenter.contract.ShareLikeViewPresenter;

import javax.inject.Inject;

/**
 * Created by Kras on 17.10.2017.
 */

public class ShareLikeView extends RelativeLayout {

    @Inject    ShareLikeViewPresenter shareLikeViewPresenter;

    public ShareLikeView(Context context) {
        super(context);
    }

    public ShareLikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareLikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ShareLikeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        PresenterComponent component = BeerMap.getAppComponent().plus(new PresenterModule(this));
        component.inject(this);
    }
}
