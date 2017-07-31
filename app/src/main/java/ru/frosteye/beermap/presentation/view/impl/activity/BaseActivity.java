package ru.frosteye.beermap.presentation.view.impl.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import ru.frosteye.beermap.R;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.app.di.module.PresenterModule;
import ru.frosteye.beermap.app.environment.BeerMap;

import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by ovcst on 01.05.2017.
 */

public abstract class BaseActivity extends PresenterActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PresenterComponent component = BeerMap.getAppComponent().plus(new PresenterModule(this));
        component.inject(this);
        inject(component);
    }

    protected abstract void inject(PresenterComponent component);

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
        Toolbar toolbar = findActionBar();
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setElevation(0);
        }
    }

    protected Toolbar findActionBar() {
        return null;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
