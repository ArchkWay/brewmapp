package com.brewmapp.data.db.impl;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import ru.frosteye.ovsa.data.storage.BaseRepo;
import ru.frosteye.ovsa.data.storage.Storage;

/**
 * Created by oleg on 15.07.17.
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
