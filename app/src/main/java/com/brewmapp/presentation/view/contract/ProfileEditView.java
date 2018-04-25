package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.User;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 08.11.2017.
 */

public interface ProfileEditView extends BasicView {
    int SHOW_FRAGMENT_EDIT =1;
    int INVALIDATE_MENU = 3;
    int ERROR = 4;
    int USER_SAVED = 5;
    int SELECT_PHOTO = 6;
    int SHOW_PROFILE_FRAGMENT_VIEW_SHOT =7;
    int SHOW_PROFILE_FRAGMENT_VIEW_FULL =8;

    void commonError(String... string);
    void showFragment(int fragment);

}
