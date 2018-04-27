package com.brewmapp.presentation.presenter.contract;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.view.contract.ProfileFragmentEdit_view;
import com.brewmapp.presentation.view.impl.fragment.ProfileFragmentEdit;

import java.io.File;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 09.11.2017.
 */

public interface ProfileFragmentEdit_presenter extends LivePresenter<ProfileFragmentEdit_view> {
    CharSequence getTitle();

    void save(ProfileFragmentEdit.OnFragmentInteractionListener mListener);



    User getUserWithNewData();

    boolean isNeedSaveUser(String... checkListCustom);

    void setNewPhoto(File file);

    View.OnClickListener getOnClickBirthday(FragmentActivity activity, User user, TextView text_birthday);
}
