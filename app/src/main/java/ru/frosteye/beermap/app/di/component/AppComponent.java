package ru.frosteye.beermap.app.di.component;

import java.util.concurrent.Executor;

import ru.frosteye.beermap.app.di.module.AppModule;
import ru.frosteye.beermap.app.di.module.PresenterModule;
import ru.frosteye.beermap.app.di.module.RepoModule;

import javax.inject.Singleton;

import dagger.Component;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.ovsa.execution.executor.MainThread;

@Component(modules = {AppModule.class, RepoModule.class})
@Singleton
public interface AppComponent {
    PresenterComponent plus(PresenterModule module);

    MainThread mainThread();
    Executor executor();
    Api api();
}