package ru.frosteye.beermap.app.di.component;

import ru.frosteye.beermap.app.di.module.AppModule;
import ru.frosteye.beermap.app.di.module.PresenterModule;
import ru.frosteye.beermap.app.di.module.RepoModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, RepoModule.class})
@Singleton
public interface AppComponent {
    PresenterComponent plus(PresenterModule module);
}