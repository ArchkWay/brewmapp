package com.brewmapp.app.environment;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.brewmapp.app.di.component.AppComponent;
import com.brewmapp.app.di.component.DaggerAppComponent;
import com.brewmapp.app.di.module.AppModule;
import com.crashlytics.android.Crashlytics;

import java.net.URISyntaxException;

import io.fabric.sdk.android.Fabric;
import io.socket.client.IO;
import io.socket.client.Socket;


public class BeerMap extends Application {

    public static final String OLD_API_ACTION = "brewmap.OLD_API";
    public static final String CHAT_SERVER_URL = "https://chat.brewmapp.com:8443";

    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
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

    public Socket getSocket() {
        return mSocket;
    }

}
