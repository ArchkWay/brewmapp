package com.brewmapp.app.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.AlbumsRepo;
import com.brewmapp.data.db.contract.PostsRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.db.impl.UiSettingImpl;
import com.brewmapp.data.db.impl.AlbumsRepoImpl;
import com.brewmapp.data.db.impl.PostsRepoImpl;
import com.brewmapp.data.db.impl.UserRepoImpl;

@Module
public class RepoModule {

    @Provides @Singleton
    UserRepo provideUserRepo(UserRepoImpl userRepo) {
        return userRepo;
    }

    @Provides @Singleton
    UiSettingRepo provideActiveFragmentRepo(UiSettingImpl activeFragmentRepo) {
        return activeFragmentRepo;
    }

    @Provides @Singleton
    AlbumsRepo provideAlbumsRepo(AlbumsRepoImpl repo) {
        return repo;
    }

    @Provides @Singleton
    PostsRepo providePostsRepo(PostsRepoImpl repo) {
        return repo;
    }
}
