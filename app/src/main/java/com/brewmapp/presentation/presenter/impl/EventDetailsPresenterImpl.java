package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.entity.Event;
import com.brewmapp.presentation.view.contract.EventDetailsView;

import ru.frosteye.ovsa.data.storage.ActiveBox;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.EventDetailsPresenter;

public class EventDetailsPresenterImpl extends BasePresenter<EventDetailsView> implements EventDetailsPresenter {

    private ActiveBox activeBox;

    @Inject
    public EventDetailsPresenterImpl(ActiveBox activeBox) {
        this.activeBox = activeBox;
    }

    @Override
    public void onAttach(EventDetailsView eventDetailsView) {
        super.onAttach(eventDetailsView);
        view.showEventsDetails(activeBox.getActive(Event.class));
        loadEventDetails(activeBox.getActive(Event.class));
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadEventDetails(Event event) {
        enableControls(false);
    }
}
