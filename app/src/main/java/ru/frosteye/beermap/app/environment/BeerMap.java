package ru.frosteye.beermap.app.environment;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import ru.frosteye.beermap.app.di.component.AppComponent;
import ru.frosteye.beermap.app.di.component.DaggerAppComponent;
import ru.frosteye.beermap.app.di.module.AppModule;


public class BeerMap extends Application {

    public static final String OLD_API_ACTION = "brewmap.OLD_API";

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

        registerReceiver(oldApiReceiver, new IntentFilter(OLD_API_ACTION));
    }

    private BroadcastReceiver oldApiReceiver = new BroadcastReceiver() {



        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
}
