package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 08.11.2017.
 */

public interface ProfileInfoView extends BasicView {
    void refreshUserProfile(User load);
    void commonError(String... string);
    void showFragment(BaseFragment fragment);
}
