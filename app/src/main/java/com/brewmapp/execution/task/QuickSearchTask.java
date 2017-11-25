package com.brewmapp.execution.task;

import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Models;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.wrapper.EventInfo;
import com.brewmapp.data.entity.wrapper.PostInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.QuickSearchResponse;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by xpusher on 11/24/2017.
 */

public class QuickSearchTask extends BaseNetworkTask<FullSearchPackage,List<IFlexible>> {


    @Inject
    public QuickSearchTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FullSearchPackage fullSearchPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.SEARCH_TYPE);
                params.addParam(Keys.TYPE, fullSearchPackage.getType());
                switch (fullSearchPackage.getType()){
                    case Keys.HASHTAG:

                        QuickSearchResponse listResponse = executeCall(getApi().quickSearch(fullSearchPackage.getStringSearch(),1));

                        ArrayList<IFlexible> iFlexibleArrayList=new ArrayList<>();

                        Iterator<Post> iteratorPost=listResponse.getModels().getPosts().iterator();
                        while (iteratorPost.hasNext()){
                            PostInfo postInfo=new PostInfo();
                            postInfo.setModel(iteratorPost.next());
                            iFlexibleArrayList.add(postInfo);
                        }
                        Iterator<Event> iteratorEvent=listResponse.getModels().getEvents().iterator();
                        while (iteratorEvent.hasNext()){
                            EventInfo eventInfo=new EventInfo();
                            eventInfo.setModel(iteratorEvent.next());
                            iFlexibleArrayList.add(eventInfo);
                        }


                        subscriber.onNext(iFlexibleArrayList);
                        subscriber.onComplete();
                        break;
                }
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
