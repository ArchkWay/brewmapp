package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.presentation.view.contract.EventsView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface EventsPresenter extends LivePresenter<EventsView> {
    void onLoadItems(LoadNewsPackage request);
}
