package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.presentation.view.contract.EventsView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface EventsPresenter extends LivePresenter<EventsView> {
    void onLoadItems(LoadNewsPackage request);
    void onLikePost(Post post);
    void onLikeSale(Sale sale);
    void onShareSale(Sale payload);

    void onSharePost(Post payload);

    void storeTabActive(int position);

    void onDeleteNewsTask(Post post);
}
