package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.brewmapp.presentation.presenter.contract.ProfileActivity_presenter;
import com.brewmapp.presentation.view.contract.ProfileFragmentEdit_view;
import com.brewmapp.presentation.view.contract.ProfileActivity_view;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Kras on 08.11.2017.
 */

public class ProfileActivity_presenter_Impl extends BasePresenter<ProfileActivity_view> implements ProfileActivity_presenter {

    private Context context;
    private ResultReceiver resultReceiver;

    @Inject
    public ProfileActivity_presenter_Impl(Context context){
        this.context = context;
    }

    @Override
    public void onAttach(ProfileActivity_view profileEditView) {
        super.onAttach(profileEditView);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void handlePhoto(BaseFragment baseFragment, int position) {
        if(baseFragment instanceof ProfileFragmentEdit_view)
        switch (position) {
            case 0:
                RxPaparazzo.single(baseFragment.getActivity())
                        .usingGallery()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.resultCode() != RESULT_OK) return;
                            ((ProfileFragmentEdit_view)baseFragment).selectedPhoto(response.data().getFile());
                        });

                break;
            case 1:
                RxPaparazzo.single(baseFragment.getActivity())
                        .usingCamera()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.resultCode() != RESULT_OK) return;
                            ((ProfileFragmentEdit_view)baseFragment).selectedPhoto(response.data().getFile());
                        });

                break;
        }
    }

    @Override
    public int parseIntent(Intent intent) {
        try {
            return Integer.valueOf(intent.getAction());
        }catch (Exception e){
            return 0;
        }


    }

}
