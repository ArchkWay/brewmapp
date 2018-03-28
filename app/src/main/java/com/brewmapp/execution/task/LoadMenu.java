package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.entity.MenuResto;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.execution.executor.MainThread;

public class LoadMenu extends BaseNetworkTask<FullSearchPackage, ListResponse<MenuResto>> {
    private int step;

    @Inject
    public LoadMenu(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
        this.step = ResourceHelper.getInteger(R.integer.config_pack_size_0);
    }

    @Override
    protected Observable<ListResponse<MenuResto>> prepareObservable(FullSearchPackage fullSearchPackage) {

        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.RESTO_MENU);
                params.addParam(Keys.RESTO_ID, fullSearchPackage.getId());
                int start = fullSearchPackage.getPage() * step;
                int end = fullSearchPackage.getPage() * step + step;
                ListResponse<MenuResto> menuRetoListResponse = executeCall(getApi().loadMenuResto( start, end, params));
                subscriber.onNext(menuRetoListResponse);
            }catch (Exception e){
                subscriber.onError(e);
            }
            subscriber.onComplete();
        });
    }
}
