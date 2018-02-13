package com.brewmapp.execution.task;

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
import io.paperdb.Paper;
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
        this.step = ResourceHelper.getInteger(R.integer.filter_pack_size);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FullSearchPackage fullSearchPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("");
                String key=getClass().getSimpleName();
                List<IFlexible> flexibleList= Paper.book().read(key);
                if(flexibleList==null) {
                    BeerBrandTypes response = executeCall(getApi().loadBeerBrands(params));
                    flexibleList=new ArrayList<>(response.getModels());
                    Paper.book().write(key,flexibleList);
                }
                subscriber.onNext(flexibleList);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
