package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Sales;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import com.brewmapp.presentation.view.contract.EventsView;

import java.text.SimpleDateFormat;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadSalesTask extends BaseNetworkTask<LoadNewsPackage, Sales> {

    private UserRepo userRepo;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int step;

    @Inject
    public LoadSalesTask(MainThread mainThread,
                         Executor executor,
                         Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
        this.step = ResourceHelper.getInteger(R.integer.config_pack_size_1);
    }

    @Override
    protected Observable<Sales> prepareObservable(LoadNewsPackage request) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = createRequestParams(request);
                int start = request.getPage() * step;
                int end = request.getPage() * step + step;
                if(request.getRelated_model()!=null && request.getResto_id()!=null){
                    params.addParam(Keys.RELATED_MODEL,request.getRelated_model());
                    params.addParam(Keys.RELATED_ID,request.getResto_id());
                }
                if(request.isOnlyMount()){
                    start=0;end=1;
                }
                Sales posts = executeCall(getApi().loadSales(start, end, params));
                subscriber.onNext(posts);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private WrapperParams createRequestParams(LoadNewsPackage request) {
        WrapperParams params = new WrapperParams(Wrappers.SHARES);
        switch (request.getMode()) {
            case EventsView.MODE_SALES:
                switch (request.getFilter()) {
                    case 0:

                        break;
                    case 1:
                        params.addParam(Keys.USER_SUBSCRIPTION, userRepo.load().getId());
                        break;
                    case 2:
                        params.addParam(Keys.DATE_SHARES, format.format(request.getDateFrom()) + "|" + format.format(request.getDateTo()));

                        break;
                }
                break;
        }
        return params;
    }
}
