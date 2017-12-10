package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.container.Restos;
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
import ru.frosteye.ovsa.data.storage.ResourceHelper;
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
                       Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
        this.step = ResourceHelper.getInteger(R.integer.config_posts_pack_size);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FilterRestoPackage restoPackage) {
        return Observable.create(subscriber -> {
            try {
                int start = restoPackage.getPage() * step;
                int end = restoPackage.getPage() * step + step;
                RequestParams params = new RequestParams();
                RequestParams queryParams = new RequestParams();
                params.addParam(Keys.RESTO_CITY, restoPackage.getRestoCity() != null ? restoPackage.getRestoCity() : "");
                params.addParam(Keys.MENU_BEER, restoPackage.getMenuBeer() != null ? restoPackage.getMenuBeer() : "");
                params.addParam(Keys.RESTO_TYPE, restoPackage.getRestoTypes() != null ? restoPackage.getRestoTypes() : "");
                params.addParam(Keys.RESTO_FEATURES, restoPackage.getRestoFeatures() != null ? restoPackage.getRestoFeatures() : "");
                params.addParam(Keys.RESTO_AVERAGE, restoPackage.getRestoPrices() != null ? restoPackage.getRestoPrices() : "");
                params.addParam(Keys.RESTO_KITCHEN, restoPackage.getRestoKitchens() != null ? restoPackage.getRestoKitchens() : "");
                params.addParam(Keys.RESTO_DISCOUNT, restoPackage.getResto_discount());

                params.addParam(Keys.COORD_START , restoPackage.getCoordStart() != null ? restoPackage.getCoordStart() : "");
                params.addParam(Keys.COORD_END , restoPackage.getCoordEnd() != null ? restoPackage.getCoordEnd() : "");

                queryParams.addParam(Keys.USER_ID, userRepo.load().getId());
                Restos response = executeCall(getApi().loadRestos(queryParams, params));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
