package com.brewmapp.data.db.impl;

import com.brewmapp.data.db.contract.ActiveFragmentRepo;

import javax.inject.Inject;

import ru.frosteye.ovsa.data.storage.BaseRepo;
import ru.frosteye.ovsa.data.storage.Storage;

/**
 * Created by Kras on 13.10.2017.
 */

public class ActiveFragmentRepoImpl extends BaseRepo<Integer> implements ActiveFragmentRepo {

    @Inject
    public ActiveFragmentRepoImpl(Storage storage) {
        super(storage);
    }


    @Override
    protected Class<Integer> provideClass() {
        return Integer.class;
    }
}
