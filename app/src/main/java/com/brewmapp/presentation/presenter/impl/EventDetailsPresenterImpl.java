package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.R;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.pojo.ClaimPackage;
import com.brewmapp.execution.task.ClaimTask;
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

    @Inject
    public EventDetailsPresenterImpl(ActiveBox activeBox, LoadClaimTypesTask claimTypesTask,
                                     ClaimTask claimTask) {
        this.activeBox = activeBox;
        this.claimTypesTask = claimTypesTask;
        this.claimTask = claimTask;
    }

    @Override
    public void onAttach(EventDetailsView eventDetailsView) {
        super.onAttach(eventDetailsView);
        view.showEventsDetails(activeBox.getActive(Event.class));
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
}
