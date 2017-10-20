package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.AddFavoriteBeerView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by xpusher on 10/20/2017.
 */

public interface AddFavoriteBeerPresenter extends LivePresenter<AddFavoriteBeerView> {

    void sendQuery(String query);
}
