package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.contract.ProfileViewFragmentView;
import com.brewmapp.presentation.view.impl.fragment.ProfileEditFragment;
import com.brewmapp.presentation.view.impl.fragment.ProfileViewFragment;

import java.io.File;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 09.11.2017.
 */

public interface ProfileViewFragmentPresenter extends LivePresenter<ProfileViewFragmentView> {
    CharSequence getTitle();


    void loadContent(Intent intent);
    void loadFriends();

    void finish(FragmentActivity activity);
}
