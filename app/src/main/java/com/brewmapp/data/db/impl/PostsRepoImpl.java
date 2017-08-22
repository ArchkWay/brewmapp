package com.brewmapp.data.db.impl;

import java.util.ArrayList;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.PostsRepo;
import com.brewmapp.data.entity.container.Posts;
import ru.frosteye.ovsa.data.storage.BaseRepo;
import ru.frosteye.ovsa.data.storage.Storage;

/**
 * Created by oleg on 16.08.17.
 */

public class PostsRepoImpl extends BaseRepo<Posts> implements PostsRepo {

    @Inject
    public PostsRepoImpl(Storage storage) {
        super(storage);
    }

    @Override
    protected Class<Posts> provideClass() {
        return Posts.class;
    }

    @Override
    protected Posts handleNull() {
        return new Posts(new ArrayList<>());
    }
}
