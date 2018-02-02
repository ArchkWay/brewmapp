package com.brewmapp.execution.task;

import android.util.Log;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.container.Beers;
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
import io.paperdb.Paper;
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


              int start = loadInterestPackage.getPage() * step;
              //int end = loadInterestPackage.getPage() * step + step;
              int end = 1000;
              if(loadInterestPackage.getRelated_model()!=null)
                  params.addParam(Keys.RELATED_MODEL,loadInterestPackage.getRelated_model());
              if(loadInterestPackage.getRelated_id()!=null)
                  params.addParam(Keys.RELATED_ID,loadInterestPackage.getRelated_id());
              if(loadInterestPackage.getUser_id()!=null)
                params.addParam(Keys.USER_ID, loadInterestPackage.getUser_id());

              String key=new StringBuilder()
                      .append(getClass().toString())
                      .append(loadInterestPackage.getRelated_model())
                      .append(loadInterestPackage.getRelated_id())
                      .append(loadInterestPackage.getUser_id())
                      .append(start)
                      .append(end).toString();
              Interests o= null;
              if (loadInterestPackage.isCacheOn()){
                  o= Paper.book().read(key);
                  if(o!=null)  {
                      subscriber.onNext(new ArrayList<>(o.getModels()));
                      Log.i("NetworkTask","LoadInterestTask - cache-read");
                  }
              }

              Interests interests=executeCall(getApi().loadInterest(start , end, params));
              Paper.book().write(key,interests);
              Log.i("NetworkTask","LoadInterestTask - cache-write");
              if(o==null)
                subscriber.onNext(new ArrayList<>(interests.getModels()));
              subscriber.onComplete();
          }catch (Exception e){
              subscriber.onError(e);
          }
        });
    }
}
