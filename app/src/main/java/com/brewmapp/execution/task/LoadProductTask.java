package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.entity.container.Beers;
import com.brewmapp.data.pojo.LoadProductPackage;
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

public class LoadProductTask extends BaseNetworkTask<LoadProductPackage,List<IFlexible>> {

    private int step;

    @Inject
    public LoadProductTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
        this.step = ResourceHelper.getInteger(R.integer.config_posts_pack_size);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(LoadProductPackage loadProductPackage) {
        return Observable.create(subscriber -> {
            try {
                int start=0;
                int end=1;
                WrapperParams params = new WrapperParams(Wrappers.BEER);
                if(loadProductPackage.getmTitle()!=null)
                    params.addParam(Keys.TITLE, loadProductPackage.getmTitle());
                if(loadProductPackage.getId()!=null)
                    params.addParam(Keys.ID, loadProductPackage.getId());
                if(loadProductPackage.getPage()!=0) {
                    start = loadProductPackage.getPage() * step;
                    end = loadProductPackage.getPage() * step + step;
                }
                Beers beers = executeCall(getApi().loadProduct(start , end, params));
                subscriber.onNext(new ArrayList<>(beers.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
