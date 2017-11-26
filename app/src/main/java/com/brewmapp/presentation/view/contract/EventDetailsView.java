package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Event;

import java.util.List;

import ru.frosteye.ovsa.presentation.view.BasicView;

public interface EventDetailsView extends BasicView, RefreshableView {
    void refreshContent(Event event);
    void showAlertDialog(String[] variants);
}
