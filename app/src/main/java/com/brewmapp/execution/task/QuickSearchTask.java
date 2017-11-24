package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.entity.container.Beers;
import com.brewmapp.data.entity.container.Restos;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
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
 * Created by xpusher on 11/24/2017.
 */

public class QuickSearchTask extends BaseNetworkTask<FullSearchPackage,List<IFlexible>> {


    @Inject
    public QuickSearchTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FullSearchPackage fullSearchPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.SEARCH_TYPE);
                params.addParam(Keys.TYPE, fullSearchPackage.getType());
                switch (fullSearchPackage.getType()){
                    case Keys.HASHTAG:

                        Object o = executeCall(getApi().quickSearch(fullSearchPackage.getStringSearch()+"?hashtagonly=1"));
                        subscriber.onNext(new ArrayList<>());
                        subscriber.onComplete();
                        break;
                }
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
