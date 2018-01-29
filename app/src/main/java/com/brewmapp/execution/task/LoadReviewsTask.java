package com.brewmapp.execution.task;

import android.util.Log;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Review;
import com.brewmapp.data.entity.container.Interests;
import com.brewmapp.data.entity.container.Reviews;
import com.brewmapp.data.entity.wrapper.ReviewInfo;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by Kras on 04.11.2017.
 */

public class LoadReviewsTask extends BaseNetworkTask<ReviewPackage, List<IFlexible>> {

    private UserRepo userRepo;

    @Inject
    public LoadReviewsTask(MainThread mainThread, Executor executor, Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(ReviewPackage reviewPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.REVIEW);
                if(reviewPackage.getRelated_model()!=null)
                    params.addParam(Keys.RELATED_MODEL, reviewPackage.getRelated_model());
                if(reviewPackage.getRelated_id()!=null)
                    params.addParam(Keys.RELATED_ID, reviewPackage.getRelated_id());
                if(reviewPackage.getUser_id()!=null)
                    params.addParam(Keys.USER_ID, reviewPackage.getUser_id());

                String key=new StringBuilder()
                        .append(getClass().toString())
                        .append(reviewPackage.getUser_id())
                        .append(reviewPackage.getRelated_id())
                        .append(reviewPackage.getUser_id())
                        .toString();
                Reviews  o= Paper.book().read(key);
                if(o!=null) {
                    subscriber.onNext(new ArrayList<>(o.getModels()));
                    Log.i("NetworkTask","LoadReviewsTask - cache-read");
                }


                Reviews reviews = executeCall(getApi().loadReviews(params));
                Paper.book().write(key,reviews);
                Log.i("NetworkTask","LoadReviewsTask - cache-write");
                if(o==null)
                    subscriber.onNext(new ArrayList<>(reviews.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
