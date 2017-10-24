package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.entity.container.Products;
import com.brewmapp.data.pojo.FindInterestPackage;
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
 * Created by Kras on 21.10.2017.
 */

public class LoadProductTask extends BaseNetworkTask<FindInterestPackage,List<IFlexible>> {

    private int step;

    @Inject
    public LoadProductTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
        this.step = ResourceHelper.getInteger(R.integer.config_posts_pack_size);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FindInterestPackage findInterestPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.BEER);
                params.addParam(Keys.TITLE, findInterestPackage.getStringSearch());
                int start = findInterestPackage.getPage() * step;
                int end = findInterestPackage.getPage() * step + step;
                Products products = executeCall(getApi().loadProduct(start , end, params));
                subscriber.onNext(new ArrayList<>(products.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
