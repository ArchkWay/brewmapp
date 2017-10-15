package com.brewmapp.presentation.view.contract;

/**
 * Created by Kras on 15.10.2017.
 */

public interface ResultTask {
    void onError(Throwable e);
    void onComplete();
}
