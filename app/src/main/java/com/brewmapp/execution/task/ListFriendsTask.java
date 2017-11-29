package com.brewmapp.execution.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.data.entity.wrapper.FriendsTitleInfo;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import ru.frosteye.ovsa.execution.executor.MainThread;

import static ru.frosteye.ovsa.data.storage.ResourceHelper.getString;

/**
 * Created by oleg on 26.07.17.
 */

public class ListFriendsTask extends BaseNetworkTask<Void, List<IFlexible>> {

    private UserRepo userRepo;

    @Inject
    public ListFriendsTask(MainThread mainThread,
                           Executor executor,
                           Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(Void v) {
        return Observable.create(subscriber -> {
            try {
                List<IFlexible> out = new ArrayList<IFlexible>();
                WrapperParams params = createParamsForType(2);
                ListResponse<ContactInfo>  response = executeCall(getApi().listFriends(params));
                boolean needFriendsHeader = false;
                if(response.getModels().size() > 0) {
                    FriendsTitleInfo incomingRequestInfo = new FriendsTitleInfo(getString(R.string.incoming_requests));
                    out.add(incomingRequestInfo);
                    Iterator<ContactInfo> iterator=response.getModels().iterator();while (iterator.hasNext()) iterator.next().getModel().setStatus(2);
                    out.addAll(response.getModels());
                    needFriendsHeader = true;
                }

                params = createParamsForType(0);
                response = executeCall(getApi().listFriends(params));
                if(response.getModels().size() > 0) {
                    FriendsTitleInfo outgoingRequetInfo = new FriendsTitleInfo(getString(R.string.outgoing_requests));
                    out.add(outgoingRequetInfo);
                    Iterator<ContactInfo> iterator=response.getModels().iterator();while (iterator.hasNext()) iterator.next().getModel().setStatus(0);
                    out.addAll(response.getModels());
                    needFriendsHeader = true;
                }


                params = createParamsForType(1);
                response = executeCall(getApi().listFriends(params));
                if(needFriendsHeader) {
                    FriendsTitleInfo friends = new FriendsTitleInfo(getString(R.string.friends));
                    Iterator<ContactInfo> iterator=response.getModels().iterator();while (iterator.hasNext()) iterator.next().getModel().setStatus(1);
                    out.add(friends);
                }
                out.addAll(response.getModels());


                subscriber.onNext(out);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private WrapperParams createParamsForType(int type) {
        WrapperParams wrapperParams = new WrapperParams(Wrappers.USER_FRIENDS);
        wrapperParams.addParam(Keys.USER_ID, userRepo.load().getId());
        wrapperParams.addParam(Keys.STATUS, type);
        return wrapperParams;
    }
}
