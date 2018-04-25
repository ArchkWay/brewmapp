package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.brewmapp.R;
import com.brewmapp.presentation.presenter.contract.ProfileEditPresenter;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.contract.ProfileEditView;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
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

public class ProfileEditPresenterImpl extends BasePresenter<ProfileEditView> implements ProfileEditPresenter {

    private Context context;
    private ResultReceiver resultReceiver;

    @Inject
    public ProfileEditPresenterImpl(Context context){
        this.context = context;
    }

    @Override
    public void onAttach(ProfileEditView profileEditView) {
        super.onAttach(profileEditView);

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
                            if (response.resultCode() != RESULT_OK) return;
                            ((ProfileEditFragmentView)baseFragment).selectedPhoto(response.data().getFile());
                        });

                break;
        }
    }

    @Override
    public int parseIntent(Intent intent) {
        try {
//            resultReceiver=intent.getParcelableExtra(context.getString(R.string.key_blur));
//            if(resultReceiver!=null)
//                view.activityMoveToBack(true);

            return Integer.valueOf(intent.getAction());
        }catch (Exception e){
            return 0;
        }


    }

//    @Override
//    public void sendResultReceiver(int actionResultReceiver) {
//        if(resultReceiver!=null) {
//            resultReceiver.send(actionResultReceiver, null);
//        }
//    }

}
