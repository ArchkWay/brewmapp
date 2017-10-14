package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.presentation.view.contract.EventsView;
import com.brewmapp.presentation.view.contract.RefreshableView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface EventsPresenter extends LivePresenter<EventsView> {
    void onLoadItems(LoadNewsPackage request);

    void onShare(ILikeable likeable);

    void storeTabActive(int position);

    void onDeleteNewsTask(Post post);

    void onLike(ILikeable likeable, RefreshableView refreshableView);

    void complaint(Object o);


}
