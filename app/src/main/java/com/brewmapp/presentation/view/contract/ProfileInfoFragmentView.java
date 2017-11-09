package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.User;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 11/9/2017.
 */

public interface ProfileInfoFragmentView extends BasicView {
    void refreshProfile(User user);
}
