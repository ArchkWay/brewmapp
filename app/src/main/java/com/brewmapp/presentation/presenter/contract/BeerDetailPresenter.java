package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;

import com.brewmapp.presentation.view.contract.BeerDetailView;
import com.brewmapp.presentation.view.impl.activity.BeerDetailActivity;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 30.10.2017.
 */

public interface BeerDetailPresenter extends LivePresenter<BeerDetailView> {

    void clickLike(int like_dislike);

    void parseIntent(Intent intent);

    void refreshContent(int mode);

    void clickFav();

    void startAddReviewRestoActivity(BeerDetailActivity beerDetailActivity);
}
