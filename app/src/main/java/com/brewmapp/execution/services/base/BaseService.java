package com.brewmapp.execution.services.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;

/**
 * Created by Kras on 20.12.2017.
 */

public class BaseService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
