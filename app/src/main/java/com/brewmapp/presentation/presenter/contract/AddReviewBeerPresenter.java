package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;

import com.brewmapp.data.entity.Post;
import com.brewmapp.presentation.view.contract.AddReviewBeerView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by xpusher on 11/15/2017.
 */

public interface AddReviewBeerPresenter extends LivePresenter<AddReviewBeerView> {
    void sendReview(Post post);

    void parseIntent(Intent intent);
}
