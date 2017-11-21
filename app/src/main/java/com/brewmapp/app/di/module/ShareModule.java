package com.brewmapp.app.di.module;

import com.brewmapp.execution.task.containers.contract.ContainerTasks;
import com.brewmapp.execution.task.containers.impl.ContainerTasksImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by xpusher on 11/14/2017.
 */
@Module
public class ShareModule {
    @Provides
    @Singleton
    ContainerTasks provideUserRepo(ContainerTasksImpl likeDislike) {
        return likeDislike;
    }

}
