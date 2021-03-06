package com.brewmapp.execution.task;

import android.util.Log;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerTypesModel;
import com.brewmapp.data.entity.wrapper.BeerTypeInfo;
import com.brewmapp.data.pojo.BeerTypes;
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
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 27.11.2017.
 */

public class BeerTypesTask extends BaseNetworkTask<FullSearchPackage, List<IFlexible>> {

    private int step;

    @Inject
    public BeerTypesTask(MainThread mainThread,
                         Executor executor,
                         Api api) {
        super(mainThread, executor, api);
        this.step = 500;
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FullSearchPackage fullSearchPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("");
                int end = fullSearchPackage.getPage() + step;
                String key=getClass().getSimpleName();
                List<IFlexible> flexibleList= Paper.book().read(key);
                if(flexibleList==null) {
                    BeerTypesModel response = executeCall(getApi().loadBeerTypes(0, end, params));
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
