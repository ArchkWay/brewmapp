package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.entity.container.FilterRestoLocationTypes;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 04.12.2017.
 */

public class SearchOnMapTask extends BaseNetworkTask<FullSearchPackage, List<IFlexible>> {

    private int step;

    @Inject
    public SearchOnMapTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
        this.step = ResourceHelper.getInteger(R.integer.config_posts_pack_size);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FullSearchPackage fullSearchPackage) {
        return Observable.create(subscriber -> {
            try {
                int start = fullSearchPackage.getPage() * step;
                int end = fullSearchPackage.getPage() * step + step;

                FilterRestoLocationTypes response = executeCall(getApi().searchOnMap(fullSearchPackage.getStringSearch(), start, end));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();

                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}