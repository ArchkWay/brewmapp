package com.brewmapp.execution.task;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import com.brewmapp.data.db.contract.AlbumsRepo;
import com.brewmapp.data.entity.container.Albums;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.task.base.LoaderTask;
import ru.frosteye.ovsa.data.storage.Repo;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadAlbumsTask extends LoaderTask<WrapperParams, Albums> {

    private AlbumsRepo albumsRepo;

    @Inject
    public LoadAlbumsTask(MainThread mainThread,
                          Executor executor,
                          Api api, AlbumsRepo albumsRepo) {
        super(mainThread, executor, api);
        this.albumsRepo = albumsRepo;
    }

    @NonNull
    @Override
    protected Repo<Albums> getRepo() {
        return albumsRepo;
    }

    @Override
    protected boolean cachedFirst() {
        return true;
    }

    @NonNull
    @Override
    protected Call<Albums> getCall(WrapperParams params) {
        return getApi().loadUserAlbums(params);
    }
}
