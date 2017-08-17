package ru.frosteye.beermap.data.db.impl;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.frosteye.beermap.data.db.contract.AlbumsRepo;
import ru.frosteye.beermap.data.db.contract.PostsRepo;
import ru.frosteye.beermap.data.entity.Post;
import ru.frosteye.beermap.data.entity.container.Albums;
import ru.frosteye.beermap.data.entity.container.Posts;
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
