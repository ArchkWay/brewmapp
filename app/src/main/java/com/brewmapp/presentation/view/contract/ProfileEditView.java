package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.User;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 08.11.2017.
 */

public interface ProfileEditView extends BasicView {
    void commonError(String... string);
    void showFragment(int fragment);
}
