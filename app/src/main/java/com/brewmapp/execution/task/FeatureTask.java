package com.brewmapp.execution.task;

import com.brewmapp.data.entity.Feature;
import com.brewmapp.data.entity.FeatureTypes;
import com.brewmapp.data.entity.PriceRange;
import com.brewmapp.data.entity.PriceRangeTypes;
import com.brewmapp.data.entity.wrapper.FeatureInfo;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 03.11.2017.
 */

public class FeatureTask extends BaseNetworkTask<Feature, List<IFlexible>> {

    @Inject
    public FeatureTask(MainThread mainThread,
                          Executor executor,
                          Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(Feature feature) {
        return Observable.create(subscriber -> {
            try {
                FeatureTypes response = executeCall(getApi().loadFeature());
                List<FeatureInfo> featureInfos = new ArrayList<>();
                featureInfos.add(0, new FeatureInfo(new Feature("Не имеют значения  ")));
                featureInfos.addAll(response.getModels());
                subscriber.onNext(new ArrayList<>(featureInfos));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
