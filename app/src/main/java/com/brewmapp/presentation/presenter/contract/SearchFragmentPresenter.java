package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.OnLocationInteractionListener;
import com.brewmapp.presentation.view.contract.SearchAllView;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public interface SearchFragmentPresenter extends LivePresenter<SearchAllView> {
    void setContentTab(String position, OnLocationInteractionListener mLocationListener, SearchFragment.OnFragmentInteractionListener mListener);

    String getActiveTab();

}
