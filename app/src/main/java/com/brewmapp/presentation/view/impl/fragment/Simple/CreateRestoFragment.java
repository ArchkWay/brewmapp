package com.brewmapp.presentation.view.impl.fragment.Simple;


import android.app.Activity;
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
import android.widget.LinearLayout;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.Location;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogSelectAddress;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateRestoFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.fragment_create_resto_name_resto)    TextInputEditText name_resto;
    @BindView(R.id.fragment_create_resto_address)    TextInputEditText address;
    @BindView(R.id.fragment_create_resto_type_resto)    TextInputEditText type_resto;
    @BindView(R.id.fragment_create_resto_kitchen)    TextInputEditText kitchen;
    @BindView(R.id.fragment_create_resto_features)    TextInputEditText features;

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

        //region Prepare fragment
        setHasOptionsMenu(true);
        mListener.setTitle(getString(R.string.title_friends_add_resto));
        BeerMap.getAppComponent().plus().inject(this);
        ButterKnife.bind(this,view);
        //endregion

        //region Parse Intent
        Intent intent=mListener.getIntent();
        //endregion

        //region Prepare views
        name_resto.setText(intent.getData().toString());
        address.setOnClickListener(this);
        type_resto.setOnClickListener(this);
        kitchen.setOnClickListener(this);
        features.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_create_resto_address:
                new DialogSelectAddress().setLocation(new Location()).showDialog(getFragmentManager(), location -> address.setText(location.getFormatLocation()));
                break;
            case R.id.fragment_create_resto_type_resto:
                Starter.SelectCategoryActivity(
                        CreateRestoFragment.this,
                        SearchFragment.CATEGORY_LIST_RESTO,
                        FilterRestoField.TYPE,
                        "",
                        "",
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                break;
            case R.id.fragment_create_resto_kitchen:
                Starter.SelectCategoryActivity(
                        CreateRestoFragment.this,
                        SearchFragment.CATEGORY_LIST_RESTO,
                        FilterRestoField.KITCHEN,
                        "",
                        "",
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                break;
            case R.id.fragment_create_resto_features:
                Starter.SelectCategoryActivity(
                        CreateRestoFragment.this,
                        SearchFragment.CATEGORY_LIST_RESTO,
                        FilterRestoField.FEATURES,
                        "",
                        "",
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RequestCodes.REQUEST_SEARCH_CODE:
                if(resultCode== Activity.RESULT_OK){
                    switch (data.getIntExtra(Actions.PARAM2,Integer.MAX_VALUE)){
                        case FilterRestoField.TYPE:
                            type_resto.setText(data.getStringExtra(Actions.PARAM4));
                            break;
                        case FilterRestoField.KITCHEN:
                            kitchen.setText(data.getStringExtra(Actions.PARAM4));
                            break;
                        case FilterRestoField.FEATURES:
                            features.setText(data.getStringExtra(Actions.PARAM4));
                            break;
                    }

                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public interface OnFragmentInteractionListener {
        void commonError(String... message);
        void invalidateOptionsMenu();
        void setTitle(CharSequence name);

        Intent getIntent();
    }

}
