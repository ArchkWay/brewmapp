package com.brewmapp.app.di.module;

import com.brewmapp.execution.exchange.share.contract.LikeDislike;
import com.brewmapp.execution.exchange.share.impl.LikeDislikeImpl;

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
    LikeDislike provideUserRepo(LikeDislikeImpl likeDislike) {
        return likeDislike;
    }

}
