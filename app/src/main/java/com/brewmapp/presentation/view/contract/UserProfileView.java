package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.User;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 11/20/2017.
 */

public interface UserProfileView extends BasicView{
    void commonError(String... strings);

    void refreshContent(User user);
}
