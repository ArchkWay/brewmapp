package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.model.ILikeable;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.presentation.view.contract.EventsView;
import com.brewmapp.presentation.view.contract.RefreshableView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface EventsPresenter extends LivePresenter<EventsView>{
    void onLoadItems(LoadNewsPackage request);
    void storeTabActive(int position);
    void onLike(ILikeable likeable, RefreshableView refreshableView);
    int getStoredActiveTab();
}
