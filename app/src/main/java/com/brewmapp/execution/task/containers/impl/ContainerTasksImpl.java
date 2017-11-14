package com.brewmapp.execution.task.containers.impl;

import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.pojo.AddInterestPackage;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.AddInterestTask;
import com.brewmapp.execution.task.LoadInterestTask;
import com.brewmapp.execution.task.RemoveInterestTask;
import com.brewmapp.execution.task.containers.contract.ContainerTasks;
import com.brewmapp.execution.task.LikeTask;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;

/**
 * Created by xpusher on 11/14/2017.
 */

public class ContainerTasksImpl implements ContainerTasks {

    private LikeTask likeTask;
    private LoadInterestTask loadInterestTask;
    private AddInterestTask addInterestTask;
    private RemoveInterestTask removeInterestTask;

    @Inject
    public ContainerTasksImpl(LikeTask likeTask,LoadInterestTask loadInterestTask,AddInterestTask addInterestTask,RemoveInterestTask removeInterestTask){
        this.likeTask=likeTask;
        this.loadInterestTask=loadInterestTask;
        this.addInterestTask=addInterestTask;
        this.removeInterestTask=removeInterestTask;
    }

    @Override
    public void clickLikeDislike(String relatedModel, int relatedId, int type_like, SimpleSubscriber<MessageResponse> simpleSubscriber) {

        LikeDislikePackage likeDislikePackage=new LikeDislikePackage(type_like);
        likeDislikePackage.setModel(relatedModel,relatedId);
        likeTask.execute(likeDislikePackage,new SimpleSubscriber<MessageResponse>(){
            @Override
            public void onError(Throwable e) {
                super.onError(e);if (simpleSubscriber!=null)simpleSubscriber.onError(e);
            }
            @Override
            public void onNext(MessageResponse messageResponse) {
                super.onNext(messageResponse);if (simpleSubscriber!=null)simpleSubscriber.onNext(messageResponse);
            }
        });

    }

    @Override
    public void loadInteres(String related_model, Integer related_id, SimpleSubscriber<List<IFlexible>> objectSimpleSubscriber) {
                    LoadInterestPackage loadInterestPackage =new LoadInterestPackage();
                    loadInterestPackage.setRelated_model(related_model);
                    loadInterestPackage.setRelated_id(String.valueOf(related_id));
                    loadInterestTask.execute(loadInterestPackage ,new SimpleSubscriber<List<IFlexible>>(){
                        @Override
                        public void onNext(List<IFlexible> iFlexibles) {
                            super.onNext(iFlexibles);
                            objectSimpleSubscriber.onNext(iFlexibles);
                        }
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            objectSimpleSubscriber.onError(e);
                        }
                    });

    }

    @Override
    public void interestOFF(String id_interest, SimpleSubscriber<String> simpleSubscriber) {
        removeInterestTask.execute(id_interest,new SimpleSubscriber<String>(){
            @Override
            public void onNext(String s) {
                super.onNext(s);
                simpleSubscriber.onNext(s);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                simpleSubscriber.onError(e);
            }
        });
    }

    @Override
    public void interestON(String relatedModel, String relatedId, SimpleSubscriber<String> simpleSubscriber) {
        AddInterestPackage addInterestPackage = new AddInterestPackage();
        addInterestPackage.setRelated_id(relatedId);
        addInterestPackage.setRelated_model(relatedModel);
        addInterestTask.execute(addInterestPackage, new SimpleSubscriber<String>() {
            @Override
            public void onNext(String s) {
                super.onNext(s);
                simpleSubscriber.onNext(s);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                simpleSubscriber.onError(e);
            }
        });

    }
}
