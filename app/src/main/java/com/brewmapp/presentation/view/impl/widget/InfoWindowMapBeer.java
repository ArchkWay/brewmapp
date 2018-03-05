package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 05.03.2018.
 */

public class InfoWindowMapBeer extends BaseLinearLayout {

    @BindView(R.id.infowindowmapbeer_beer_id)
    TextView textView;


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

    }

    public void setBeerId(String s) {
        textView.setText(s);
    }
}
