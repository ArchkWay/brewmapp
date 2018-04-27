package com.brewmapp.presentation.view.impl.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.presenter.contract.ProfileFragmentFull_presenter;
import com.brewmapp.presentation.view.contract.ProfileFragmentFull_view;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;


public class ProfileFragmentFull extends BaseFragment implements ProfileFragmentFull_view {

    private OnFragmentInteractionListener mListener;

    @Inject    ProfileFragmentFull_presenter presenter;


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile_view_full;
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        if(presenter.parseIntent(mListener.getIntent()))
            presenter.loadProfile();
        else
            mListener.commonError();
    }

    @Override
    protected void initView(View view) {
        mListener.setVisibleChildActivity();

    }

    @Override
    public void showProfile(ArrayList<User> users) {
        if(users.size()!=1){
            commonError();
            return;
        }



    }







    @Override
    public int getMenuToInflate() {
        return 0;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return null;
    }

    @Override
    public void enableControls(boolean b, int i) {

    }


    @Override
    protected LivePresenter<?> getPresenter() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void commonError(String... message) {
        mListener.commonError(message);
    }



    //***********************************
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void setVisibleChildActivity();

        void finish();

        void sendActionParentActivity(int actionRefresh);

        void showSnackbar(String string);

        Intent getIntent();

        void commonError(String... strings);
    }

}
