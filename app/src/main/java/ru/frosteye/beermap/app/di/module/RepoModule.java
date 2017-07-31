package ru.frosteye.beermap.app.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.data.db.impl.UserRepoImpl;

@Module
public class RepoModule {

    @Provides @Singleton
    UserRepo provideUserRepo(UserRepoImpl userRepo) {
        return userRepo;
    }
}
