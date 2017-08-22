package com.brewmapp.data.db.impl;

import java.util.ArrayList;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.AlbumsRepo;
import com.brewmapp.data.entity.container.Albums;
import ru.frosteye.ovsa.data.storage.BaseRepo;
import ru.frosteye.ovsa.data.storage.Storage;

/**
 * Created by oleg on 16.08.17.
 */

public class AlbumsRepoImpl extends BaseRepo<Albums> implements AlbumsRepo {

    @Inject
    public AlbumsRepoImpl(Storage storage) {
        super(storage);
    }

    @Override
    protected Class<Albums> provideClass() {
        return Albums.class;
    }

    @Override
    protected Albums handleNull() {
        return new Albums(new ArrayList<>());
    }
}
