package com.brewmapp.execution.task;

import android.util.Log;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerBrand;
import com.brewmapp.data.entity.BeerBrandTypes;
import com.brewmapp.data.entity.wrapper.BeerBrandInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
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
    private List<BeerBrandInfo> beerBrandInfos;

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
                int start;
                if (fullSearchPackage.getPage() == 0) {
                    start = fullSearchPackage.getPage() * step;
                } else {
                    start = (fullSearchPackage.getPage() * step) + 1;
                }
                int end = fullSearchPackage.getPage() * step + step;
                Log.i("fullSearch", String.valueOf(fullSearchPackage.getPage()));
                Log.i("start", String.valueOf(start));
                Log.i("end", String.valueOf(end));
                BeerBrandTypes response = executeCall(getApi().loadBeerBrands(start, end, params));
                if (beerBrandInfos == null) {
                    beerBrandInfos = new ArrayList<>();
                    beerBrandInfos.add(0, new BeerBrandInfo(new BeerBrand("Любой  ")));
                }
                beerBrandInfos.addAll(response.getModels());
                subscriber.onNext(new ArrayList<>(beerBrandInfos));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
