package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.R;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.data.pojo.ClaimPackage;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.ClaimTask;
import com.brewmapp.execution.task.DisLikeTask;
import com.brewmapp.execution.task.LikeTask;
import com.brewmapp.execution.task.LoadClaimTypesTask;
import com.brewmapp.presentation.view.contract.EventDetailsView;

import ru.frosteye.ovsa.data.storage.ActiveBox;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.EventDetailsPresenter;

import static ru.frosteye.ovsa.data.storage.ResourceHelper.getString;

public class EventDetailsPresenterImpl extends BasePresenter<EventDetailsView> implements EventDetailsPresenter {

    private ActiveBox activeBox;
    private LoadClaimTypesTask claimTypesTask;
    private ClaimTask claimTask;
    private LikeTask likeTask;
    private DisLikeTask disLikeTask;
    @Inject
    public EventDetailsPresenterImpl(ActiveBox activeBox, LoadClaimTypesTask claimTypesTask,
                                     ClaimTask claimTask,LikeTask likeTask,DisLikeTask disLikeTask) {
        this.activeBox = activeBox;
        this.claimTypesTask = claimTypesTask;
        this.claimTask = claimTask;
        this.likeTask =likeTask;
        this.disLikeTask =disLikeTask;
    }

    @Override
    public void onAttach(EventDetailsView eventDetailsView) {
        super.onAttach(eventDetailsView);
        view.refreshContent(activeBox.getActive(Event.class));
        onLoacEventDetails(activeBox.getActive(Event.class));
    }

    @Override
    public void onDestroy() {
        claimTypesTask.cancel();
    }

    @Override
    public void onLoacEventDetails(Event event) {

    }

    @Override
    public void onRequestAlertVariants() {
        enableControls(false);
        claimTypesTask.execute(null, new SimpleSubscriber<String[]>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(String[] strings) {
                enableControls(true);
                view.showAlertDialog(strings);
            }
        });
    }

    @Override
    public void onClaim(ClaimPackage claimPackage) {
        enableControls(false);
        claimTask.execute(claimPackage, new SimpleSubscriber<Boolean>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                enableControls(true);
                showMessage(getString(R.string.claimed));
            }
        });
    }

    @Override
    public void onDisLakeEvent(Event event) {
        LikeDislikePackage likeDislikePackage = new LikeDislikePackage(LikeDislikePackage.TYPE_LIKE);
        likeDislikePackage.setModel(Keys.CAP_EVENT, event.getId());
        disLikeTask.execute(likeDislikePackage, new DisLikeSubscriber(event));

    }

    class DisLikeSubscriber extends SimpleSubscriber<MessageResponse> {

        private ILikeable iLikeable;

        DisLikeSubscriber(ILikeable iLikeable) {
            this.iLikeable = iLikeable;
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(MessageResponse messageResponse) {
            iLikeable.increaseDisLikes();
            view.refreshState();
        }
    }

}
