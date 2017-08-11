package ru.frosteye.beermap.data.db.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.data.entity.User;
import ru.frosteye.ovsa.data.storage.BaseRepo;
import ru.frosteye.ovsa.data.storage.Storage;
import ru.frosteye.ovsa.execution.network.client.IdentityProvider;

/**
 * Created by oleg on 25.07.17.
 */

public class UserRepoImpl extends BaseRepo<User> implements UserRepo {

    @Inject
    public UserRepoImpl(Storage storage) {
        super(storage);
    }

    @Override
    protected Class<User> provideClass() {
        return User.class;
    }

    @Override
    public String provideIdentity() {
        User user = load();
        if(user == null) return null;
        return user.getToken();
    }
}
