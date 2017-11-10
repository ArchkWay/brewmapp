package com.brewmapp.presentation.view.contract;

import android.widget.RadioGroup;

import com.brewmapp.data.pojo.ProfileChangePackage;
import com.brewmapp.presentation.view.impl.fragment.ProfileEditFragment;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 09.11.2017.
 */

public interface ProfileEditFragmentPresenter extends LivePresenter<ProfileEditFragmentView> {
    CharSequence getTitle();

    RadioGroup.OnCheckedChangeListener getOnCheckedChangeListener();

    void save(ProfileChangePackage profileChangePackage, ProfileEditFragment.OnFragmentInteractionListener mListener);
}
