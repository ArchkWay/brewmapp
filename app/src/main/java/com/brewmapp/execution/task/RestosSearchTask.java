package com.brewmapp.execution.task;

import android.content.Context;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.container.ResponseSearchResto;
import com.brewmapp.data.pojo.FilterRestoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by nixus on 07.12.2017.
 */

public class RestosSearchTask extends BaseNetworkTask<FilterRestoPackage, List<IFlexible>> {

    private UserRepo userRepo;
    private int step;

    @Inject
    public RestosSearchTask(MainThread mainThread,
                            Executor executor,
                            Api api, UserRepo userRepo, Context context) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
        this.step = context.getResources().getInteger(R.integer.step_items_load);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FilterRestoPackage restoPackage) {
        return Observable.create(subscriber -> {
            try {

                int start = restoPackage.getPage() * step;
                int end = start + step;
                RequestParams params = new RequestParams();
                RequestParams queryParams = new RequestParams();
                params.addParam(Keys.RESTO_CITY, restoPackage.getRestoCity() != null ? restoPackage.getRestoCity() : "");
                params.addParam(Keys.MENU_BEER, restoPackage.getRestoBeer() != null ? restoPackage.getRestoBeer() : "");
                params.addParam(Keys.RESTO_TYPE, restoPackage.getRestoTypes() != null ? restoPackage.getRestoTypes() : "");
                params.addParam(Keys.RESTO_FEATURES, restoPackage.getRestoFeatures() != null ? restoPackage.getRestoFeatures() : "");
                params.addParam(Keys.RESTO_AVERAGE, restoPackage.getRestoPrices() != null ? restoPackage.getRestoPrices() : "");
                params.addParam(Keys.RESTO_KITCHEN, restoPackage.getRestoKitchens() != null ? restoPackage.getRestoKitchens() : "");
                //params.addParam(Keys.RESTO_DISCOUNT, restoPackage.getResto_discount());

                params.addParam(Keys.COORD_START , restoPackage.getCoordStart() != null ? restoPackage.getCoordStart() : "");
                params.addParam(Keys.COORD_END , restoPackage.getCoordEnd() != null ? restoPackage.getCoordEnd() : "");

                if(restoPackage.getOrder()!=null)
                    params.addParam(Keys.ORDER_SORT_RESTO,restoPackage.getOrder());

                queryParams.addParam(Keys.USER_ID, userRepo.load().getId());
                ResponseSearchResto response = executeCall(getApi().searchResto(queryParams,restoPackage.getLat(),restoPackage.getLon(), start, end, params));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
