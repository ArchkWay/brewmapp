package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.container.Reviews;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import com.brewmapp.presentation.view.contract.EventsView;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by Kras on 04.11.2017.
 */

public class LoadReviewsListTask extends BaseNetworkTask<LoadNewsPackage, Reviews> {

    private UserRepo userRepo;
    private int step = ResourceHelper.getInteger(R.integer.config_pack_size_1);

    @Inject
    public LoadReviewsListTask(MainThread mainThread, Executor executor, Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<Reviews> prepareObservable(LoadNewsPackage request) {
        return Observable.create(subscriber -> {
            try {

                int start = request.getPage() * step;
                int end = request.getPage() * step + step;

                WrapperParams params = createRequestParams(request);
//                if(reviewPackage.getRelated_model()!=null)
//                    params.addParam(Keys.RELATED_MODEL, reviewPackage.getRelated_model());
//                if(reviewPackage.getRelated_id()!=null)
//                    params.addParam(Keys.RELATED_ID, reviewPackage.getRelated_id());
//                if(reviewPackage.getUser_id()!=null)
//                    params.addParam(Keys.USER_ID, reviewPackage.getUser_id());

                Reviews reviews = executeCall(getApi().loadReviews(start, end, params));
                subscriber.onNext(reviews);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private WrapperParams createRequestParams(LoadNewsPackage request) {
        WrapperParams params = new WrapperParams(Wrappers.REVIEW);
        switch (request.getMode()) {
            case EventsFragment.TAB_REVIEWS: {
                switch (request.getFilter()) {
                    case 0:

                        break;
                    case 1:
                        params.addParam(Keys.USER_ID, userRepo.load().getId());
                        break;
                    case 2:
                        params.addParam(Keys.RELATED_MODEL, request.getRelated_model());
                        break;
                    case 3:
                        params.addParam(Keys.CITY_ID, request.getCity_id());
                        break;
                }
                break;
            }
        }
        return params;
    }
}
