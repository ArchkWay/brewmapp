package com.brewmapp.presentation.presenter.contract;

import android.widget.RadioGroup;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.impl.fragment.ProfileEditFragment;

import java.io.File;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 09.11.2017.
 */

public interface ProfileEditFragmentPresenter extends LivePresenter<ProfileEditFragmentView> {
    CharSequence getTitle();

    RadioGroup.OnCheckedChangeListener getOnCheckedChangeListener();

    void save(ProfileEditFragment.OnFragmentInteractionListener mListener);


    void setPhoto(File file);

    User getUserWithNewData();

    boolean checkNewDataUser();
}
