package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.ProfileInfoView;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 08.11.2017.
 */

public interface ProfileInfoPresenter extends LivePresenter<ProfileInfoView> {

    void handlePhoto(BaseFragment baseFragment, int position);
}
