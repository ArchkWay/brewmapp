package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;
import android.widget.RatingBar;

import com.brewmapp.data.entity.Post;
import com.brewmapp.presentation.view.contract.AddReviewRestoView;
import com.brewmapp.presentation.view.impl.activity.AddReviewRestoActivity;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 03.11.2017.
 */

public interface AddReviewRestoPresenter  extends LivePresenter<AddReviewRestoView> {
    void parseIntent(Intent intent);

    RatingBar.OnRatingBarChangeListener getRatingListener();

    void sendReview(Post post);
}
