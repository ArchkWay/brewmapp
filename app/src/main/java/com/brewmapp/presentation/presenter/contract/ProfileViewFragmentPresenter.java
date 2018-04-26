package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.brewmapp.presentation.view.contract.ProfileViewFragmentView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 09.11.2017.
 */

public interface ProfileViewFragmentPresenter extends LivePresenter<ProfileViewFragmentView> {
    CharSequence getTitle();


    void loadContent(Intent intent);
    void loadStatusUser();

    void finish(FragmentActivity activity);

    void deleteFriend(FragmentManager fragmentManager, String string);

    void allowFriens(FragmentManager fragmentManager, String string);

    void sendRequestFriends(FragmentManager fragmentManager, String string);
}
