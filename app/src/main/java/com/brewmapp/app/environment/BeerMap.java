package com.brewmapp.app.environment;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.brewmapp.app.di.component.AppComponent;
import com.brewmapp.app.di.component.DaggerAppComponent;
import com.brewmapp.app.di.module.AppModule;
import com.brewmapp.execution.services.ChatService;
import com.brewmapp.presentation.view.impl.activity.SplashActivity;
import com.crashlytics.android.Crashlytics;

import java.net.URISyntaxException;

import io.fabric.sdk.android.Fabric;
import io.socket.client.IO;
import io.socket.client.Socket;


public class BeerMap extends Application {

    public static final String OLD_API_ACTION = "brewmap.OLD_API";

    private static AppComponent appComponent;

    private static Context appContext;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext=getApplicationContext();
        Fabric.with(this, new Crashlytics());
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        registerReceiver(oldApiReceiver, new IntentFilter(OLD_API_ACTION));


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                RestartApp();
            }
        });

    }

    private BroadcastReceiver oldApiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    public static void RestartApp(){

        appContext.stopService(new Intent(appContext, ChatService.class));

        Intent mStartActivity = new Intent(appContext, SplashActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(appContext, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)appContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
        //System.exit(2);
    }


}
