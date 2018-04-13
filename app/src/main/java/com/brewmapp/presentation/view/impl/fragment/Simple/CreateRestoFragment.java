package com.brewmapp.presentation.view.impl.fragment.Simple;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brewmapp.R;
import com.brewmapp.app.environment.BeerMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateRestoFragment extends Fragment {

    @BindView(R.id.fragment_create_resto_name_resto)    TextInputEditText name_resto;

    //region Private
    private OnFragmentInteractionListener mListener;
    //endregion


    public CreateRestoFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resto_create, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //region Prepare view
        setHasOptionsMenu(true);
        mListener.setTitle(getString(R.string.title_friends_add_resto));
        BeerMap.getAppComponent().plus().inject(this);
        ButterKnife.bind(this,view);
        //endregion

        Intent intent=mListener.getIntent();

        name_resto.setText(intent.getData().toString());
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

        Intent getIntent();
    }

}
