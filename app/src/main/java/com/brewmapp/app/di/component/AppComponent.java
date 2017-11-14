package com.brewmapp.app.di.component;

import java.util.concurrent.Executor;

import com.brewmapp.app.di.module.AppModule;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.di.module.RepoModule;

import javax.inject.Singleton;

import dagger.Component;

import com.brewmapp.app.di.module.ShareModule;
import com.brewmapp.execution.exchange.common.Api;
import ru.frosteye.ovsa.execution.executor.MainThread;

@Component(modules = {AppModule.class, RepoModule.class, ShareModule.class})
@Singleton
public interface AppComponent {
    PresenterComponent plus(PresenterModule module);

    MainThread mainThread();
    Executor executor();
    Api api();
}