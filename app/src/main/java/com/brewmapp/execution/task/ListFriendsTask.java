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
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.data.entity.wrapper.FriendsTitleInfo;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import com.brewmapp.presentation.view.contract.FriendsView;

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
                ListResponse<ContactInfo>  response;

                List<IFlexible> out = new ArrayList<IFlexible>();
                WrapperParams params = createParamsForType(FriendsView.FRIENDS_REQUEST_IN);
                response = executeCall(getApi().listFriends(params));
                if(response.getModels().size() > 0) {
                    //out.add(new FriendsTitleInfo(getString(R.string.incoming_requests),FriendsView.FRIENDS_REQUEST_IN));
                    Iterator<ContactInfo> iterator=response.getModels().iterator();
                    while (iterator.hasNext())
                        iterator.next().getModel().setStatus(FriendsView.FRIENDS_REQUEST_IN);
                    out.addAll(response.getModels());
                }

                params = createParamsForType(FriendsView.FRIENDS_REQUEST_OUT);
                response = executeCall(getApi().listFriends(params));
                if(response.getModels().size() > 0) {
                    //out.add(new FriendsTitleInfo(getString(R.string.outgoing_requests),FriendsView.FRIENDS_REQUEST_OUT));
                    Iterator<ContactInfo> iterator=response.getModels().iterator();
                    while (iterator.hasNext())
                        iterator.next().getModel().setStatus(FriendsView.FRIENDS_REQUEST_OUT);
                    out.addAll(response.getModels());
                }


                params = createParamsForType(FriendsView.FRIENDS_NOW);
                response = executeCall(getApi().listFriends(params));
                if(response.getModels().size()>0) {
                    out.add(new FriendsTitleInfo(getString(R.string.friends),FriendsView.FRIENDS_NOW));
                    Iterator<ContactInfo> iterator=response.getModels().iterator();
                    while (iterator.hasNext())
                        iterator.next().getModel().setStatus(FriendsView.FRIENDS_NOW);
                }
                out.addAll(response.getModels());

                for (IFlexible iFlexible:out){
                    if(iFlexible instanceof  ContactInfo){
                        ContactInfo contactInfo= (ContactInfo) iFlexible;
                        if(contactInfo.getModel().getFriend_info().getId()==userRepo.load().getId()){
                            User tmpUser=contactInfo.getModel().getUser();
                            User tmpFriend_info=contactInfo.getModel().getFriend_info();
                            contactInfo.getModel().setFriend_info(tmpUser);
                            contactInfo.getModel().setUser(tmpFriend_info);
                        }
                    }
                }



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
