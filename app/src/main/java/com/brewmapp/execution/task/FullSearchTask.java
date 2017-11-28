package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.entity.container.Beers;
import com.brewmapp.data.entity.container.Restos;
import com.brewmapp.data.entity.container.Users;
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
 * Created by Kras on 25.10.2017.
 */

public class FullSearchTask extends BaseNetworkTask<FullSearchPackage,List<IFlexible>> {

    private int step;

    @Inject
    public FullSearchTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
        this.step = ResourceHelper.getInteger(R.integer.config_posts_pack_size);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FullSearchPackage fullSearchPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.SEARCH_TYPE);
                params.addParam(Keys.TYPE, fullSearchPackage.getType());
                int start = fullSearchPackage.getPage() * step;
                int end = fullSearchPackage.getPage() * step + step;
                switch (fullSearchPackage.getType()){
                    case Keys.TYPE_BEER:
                        Beers beers = executeCall(getApi().fullSearchBeer(fullSearchPackage.getStringSearch(), start, end, params));
                        subscriber.onNext(new ArrayList<>(beers.getModels()));
                        subscriber.onComplete();
                        break;
                    case Keys.TYPE_RESTO:
                        Restos restos=executeCall(getApi().fullSearchResto(fullSearchPackage.getStringSearch(), start, end, params));
                        subscriber.onNext(new ArrayList<>(restos.getModels()));
                        subscriber.onComplete();
                        break;
                    case Keys.TYPE_USER:
                        Users users=executeCall(getApi().fullSearchUser(fullSearchPackage.getStringSearch(), start, end, params));
                        subscriber.onNext(new ArrayList<>(users.getModels()));
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
