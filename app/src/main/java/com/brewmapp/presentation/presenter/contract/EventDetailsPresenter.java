package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.Event;
import com.brewmapp.presentation.view.contract.EventDetailsView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface EventDetailsPresenter extends LivePresenter<EventDetailsView> {
    void loadEventDetails(Event event);
}
