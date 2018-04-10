package com.brewmapp.presentation.view.impl.fragment.Simple;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brewmapp.R;
import com.brewmapp.app.environment.BeerMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateBeerFragment extends Fragment {


    private OnFragmentInteractionListener mListener;


    public CreateBeerFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beer_create, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mListener.setTitle(getString(R.string.title_friends_add_beer));
        BeerMap.getAppComponent().plus().inject(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.send,menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    public interface OnFragmentInteractionListener {
        void commonError(String... message);
        void invalidateOptionsMenu();
        void setTitle(CharSequence name);
    }

}
