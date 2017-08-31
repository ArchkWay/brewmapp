package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Event;

import ru.frosteye.ovsa.presentation.view.BasicView;

public interface EventDetailsView extends BasicView, RefreshableView {
    void showEventsDetails(Event event);
}
