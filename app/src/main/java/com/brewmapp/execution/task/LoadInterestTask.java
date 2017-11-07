package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.container.Interests;
import com.brewmapp.data.pojo.LoadInterestPackage;
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
 * Created by Kras on 22.10.2017.
 */

public class LoadInterestTask extends BaseNetworkTask<LoadInterestPackage,List<IFlexible>> {

    private UserRepo userRepo;
    private int step;

    @Inject
    public LoadInterestTask(MainThread mainThread, Executor executor, Api api,UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
        this.step = ResourceHelper.getInteger(R.integer.config_posts_pack_size);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(LoadInterestPackage loadInterestPackage) {
        return Observable.create(subscriber -> {
          try {
              WrapperParams params = new WrapperParams(Wrappers.USER_INTEREST);
              params.addParam(Keys.USER_ID, userRepo.load().getId());
              int start = loadInterestPackage.getPage() * step;
              int end = loadInterestPackage.getPage() * step + step;
              if(loadInterestPackage.getRelated_model()!=null&&loadInterestPackage.getRelated_id()!=null) {
                  params.addParam(Keys.RELATED_MODEL,loadInterestPackage.getRelated_model());
                  params.addParam(Keys.RELATED_ID,loadInterestPackage.getRelated_id());
              }
              Interests interests=executeCall(getApi().loadInterest(start , end, params));
              subscriber.onNext(new ArrayList<>(interests.getModels()));
              subscriber.onComplete();
          }catch (Exception e){
              subscriber.onError(e);
          }
        });
    }
}
