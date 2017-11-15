package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.User;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 11/15/2017.
 */

public interface AddReviewBeerView extends BasicView {
    void commonError(String... strings);

    void setUser(User user);

    void reviewSent();
}
