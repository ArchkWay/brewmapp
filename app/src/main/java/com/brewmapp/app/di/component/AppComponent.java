package com.brewmapp.app.di.component;

import java.util.concurrent.Executor;

import com.brewmapp.app.di.module.AppModule;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.di.module.RepoModule;

import javax.inject.Singleton;

import dagger.Component;

import com.brewmapp.app.di.module.ShareModule;
import com.brewmapp.execution.exchange.common.Api;

import io.socket.client.Socket;
import ru.frosteye.ovsa.data.storage.Storage;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.serialization.Serializer;

@Component(modules = {AppModule.class, RepoModule.class, ShareModule.class})
@Singleton
public interface AppComponent {
    PresenterComponent plus(PresenterModule module);

    MainThread mainThread();
    Executor executor();
    Api api();
    Storage storage();

}