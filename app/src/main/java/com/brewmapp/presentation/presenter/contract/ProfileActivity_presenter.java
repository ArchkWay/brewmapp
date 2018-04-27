package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;

import com.brewmapp.presentation.view.contract.ProfileActivity_view;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 08.11.2017.
 */

public interface ProfileActivity_presenter extends LivePresenter<ProfileActivity_view> {

    void handlePhoto(BaseFragment baseFragment, int position);

    int parseIntent(Intent intent);

//    void sendResultReceiver(int actionStopProgressBar);
}
