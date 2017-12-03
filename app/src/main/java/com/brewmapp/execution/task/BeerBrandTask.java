package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerBrand;
import com.brewmapp.data.entity.BeerBrandTypes;
import com.brewmapp.data.entity.wrapper.BeerBrandInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.ScrollPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
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
 * Created by nixus on 28.11.2017.
 */

public class BeerBrandTask extends BaseNetworkTask<FullSearchPackage, List<IFlexible>> {

    private int step;

    @Inject
    public BeerBrandTask(MainThread mainThread,
                         Executor executor,
                         Api api) {
        super(mainThread, executor, api);
        this.step = ResourceHelper.getInteger(R.integer.config_posts_pack_size);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FullSearchPackage fullSearchPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("");
//                int start = fullSearchPackage.getPage() * step;
//                int end = fullSearchPackage.getPage() * step + step;
                BeerBrandTypes response = executeCall(getApi().loadBeerBrands(params));
                List<BeerBrandInfo> beerTypeInfos = new ArrayList<>();
                beerTypeInfos.add(0, new BeerBrandInfo(new BeerBrand("Любой  ")));
                beerTypeInfos.addAll(response.getModels());
                subscriber.onNext(new ArrayList<>(beerTypeInfos));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
