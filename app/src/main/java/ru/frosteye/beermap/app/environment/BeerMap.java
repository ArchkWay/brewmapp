package ru.frosteye.beermap.app.environment;

import android.app.Application;

import ru.frosteye.beermap.app.di.component.AppComponent;
import ru.frosteye.beermap.app.di.component.DaggerAppComponent;
import ru.frosteye.beermap.app.di.module.AppModule;


public class BeerMap extends Application {

    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
