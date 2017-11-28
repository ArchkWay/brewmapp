package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;

import com.brewmapp.presentation.view.contract.ProfileEditView;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 08.11.2017.
 */

public interface ProfileEditPresenter extends LivePresenter<ProfileEditView> {

    void handlePhoto(BaseFragment baseFragment, int position);

    int parseIntent(Intent intent);

}
