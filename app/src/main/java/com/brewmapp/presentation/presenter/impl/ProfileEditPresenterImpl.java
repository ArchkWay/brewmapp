package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.execution.task.LoadProfileTask;
import com.brewmapp.presentation.presenter.contract.ProfileEditPresenter;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.contract.ProfileEditView;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Kras on 08.11.2017.
 */

public class ProfileEditPresenterImpl extends BasePresenter<ProfileEditView> implements ProfileEditPresenter {


    @Inject
    public ProfileEditPresenterImpl(){
    }

    @Override
    public void onAttach(ProfileEditView profileEditView) {
        super.onAttach(profileEditView);
        view.showFragment(ProfileEditActivity.FRAGMENT_EDIT);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void handlePhoto(BaseFragment baseFragment, int position) {
        if(baseFragment instanceof ProfileEditFragmentView)
        switch (position) {
            case 0:
                RxPaparazzo.single(baseFragment.getActivity())
                        .usingGallery()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.resultCode() != RESULT_OK) return;
                            ((ProfileEditFragmentView)baseFragment).selectedPhoto(response.data().getFile());
                        });

                break;
            case 1:
                RxPaparazzo.single(baseFragment.getActivity())
                        .usingCamera()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.resultCode() != RESULT_OK) {
                                return;
                            }
                            ((ProfileEditFragmentView)baseFragment).selectedPhoto(response.data().getFile());
                        });

                break;
            case 2:
                ((ProfileEditFragmentView)baseFragment).selectedPhoto(null);
                break;
        }
    }

}
