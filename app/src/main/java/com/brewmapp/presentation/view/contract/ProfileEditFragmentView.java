package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.User;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 09.11.2017.
 */

public interface ProfileEditFragmentView extends BasicView {
    void refreshProfile(User user);
    void commonError(String... strings);
}
