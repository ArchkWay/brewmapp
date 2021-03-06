package com.brewmapp.execution.task;

import android.util.Log;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.container.Events;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import com.brewmapp.presentation.view.contract.EventsView;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;

import java.text.SimpleDateFormat;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadEventsTask extends BaseNetworkTask<LoadNewsPackage, Events> {

    private UserRepo userRepo;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int step;

    @Inject
    public LoadEventsTask(MainThread mainThread,
                          Executor executor,
                          Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
        this.step = ResourceHelper.getInteger(R.integer.config_pack_size_1);
    }

    @Override
    protected Observable<Events> prepareObservable(LoadNewsPackage request) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = createRequestParams(request);
                int start = request.getPage() * step;
                int end = request.getPage() * step + step;

//                if(request.getRelated_model()!=null && request.getResto_id()!=null){
//                    params.addParam(Keys.RELATED_MODEL,request.getRelated_model());
//                    params.addParam(Keys.RELATED_ID,request.getResto_id());
//                }
//                if(request.isOnlyMount()) {
//                    start=0; end=1;
//                }
//                if(request.getCity_id()!=null)
//                    params.addParam(Keys.CITY_ID,request.getCity_id());

                Events posts = executeCall(getApi().loadEvents(start, end, params));
                Log.v("xzxz", "---DBG LoadEventsTask onNext size="+ posts.getModels().size());
                subscriber.onNext(posts);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private WrapperParams createRequestParams(LoadNewsPackage request) {
        WrapperParams params = new WrapperParams(Wrappers.EVENT);
        switch (request.getMode()) {
            case EventsFragment.TAB_EVENT:
                switch (request.getFilter()) {
                    case 0:
//               Event[user_go]"] != nil;
//               Event[user_invitations]"] != nil;
//               Event[user_subscription]"] != nil;
                        break;
                    case 1:
                        params.addParam(Keys.USER_GO, userRepo.load().getId());
                        break;
                    case 2:
                        params.addParam(Keys.USER_INVITATIONS, userRepo.load().getId());
                        break;
                    case 3:
                        params.addParam(Keys.USER_SUBSCRIPTION, userRepo.load().getId());
                        break;
                    case 4:
                        params.addParam(Keys.DATE_EVENTS, format.format(request.getDateFrom()) + "|" + format.format(request.getDateTo()));
//                        params.addParam(Keys.RELATED_MODEL, Keys.CAP_USER);
//                        params.addParam(Keys.RELATED_ID, userRepo.load().getId());
                        break;
                }
                break;
        }
        return params;
    }
}
