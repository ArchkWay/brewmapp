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
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.callback.SimpleTextChangeCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateBeerFragment extends Fragment {

    //region BindView
    @BindView(R.id.fragment_create_beer_name)    TextInputEditText beer_name;
    //endregion

    //region Private
    private OnFragmentInteractionListener mListener;
    private WrapperParams wrapperParamsResto =new WrapperParams(Keys.CAP_BEER);
    //endregion


    public CreateBeerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beer_create, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Prepare fragment
        setHasOptionsMenu(true);
        mListener.setTitle(getString(R.string.title_friends_add_beer));
        BeerMap.getAppComponent().plus().inject(this);
        ButterKnife.bind(this,view);
        //endregion

        //region Parse Intent
        Intent intent=mListener.getIntent();
        //endregion

        //region Prepare views

        mListener.changeListeners(s->{
                    wrapperParamsResto.addParam(Keys.NAME,s.toString());
                    mListener.invalidateOptionsMenu();},
                beer_name);

        beer_name.setText(intent.getData().toString());
        //endregion


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

    @Override
    public void onResume() {
        super.onResume();
        beer_name.postDelayed(() -> mListener.hideKeyboard(),500);
    }


    public interface OnFragmentInteractionListener {
        void commonError(String... message);
        void invalidateOptionsMenu();
        void setTitle(CharSequence name);
        Intent getIntent();
        void changeListeners(SimpleTextChangeCallback changeCallback, TextView... views);
        void hideKeyboard();
    }

}
