package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.model.ILikeable;
import com.brewmapp.presentation.view.contract.RefreshableView;
import com.brewmapp.presentation.view.impl.widget.ShareLikeView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 17.10.2017.
 */

public interface ShareLikeViewPresenter extends LivePresenter<ShareLikeView>{

    void onLike(ILikeable iLikeable,RefreshableView refreshableView);
    void onDislike(ILikeable iLikeable,RefreshableView refreshableView);
}
