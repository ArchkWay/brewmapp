package com.brewmapp.presentation.view.impl.fragment.Simple;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.container.FilterBeer;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.callback.SimpleTextChangeCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateBeerFragment extends Fragment implements View.OnClickListener{

    //region BindView
    @BindView(R.id.fragment_create_beer_name)    TextInputEditText beer_name;
    @BindView(R.id.fragment_create_beer_country)    TextInputEditText beer_country;
    @BindView(R.id.fragment_create_beer_country_chevron)    ImageView country_chevron;
    @BindView(R.id.fragment_create_beer_brand)    TextInputEditText beer_brand;
    @BindView(R.id.fragment_create_beer_brand_chevron)    ImageView brand_chevron;
    //endregion

    //region Private
    private OnFragmentInteractionListener mListener;
    private WrapperParams wrapperParamsBeer =new WrapperParams(Keys.CAP_BEER);
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
                    wrapperParamsBeer.addParam(Keys.NAME,s.toString());
                    mListener.invalidateOptionsMenu();},
                beer_name);
        beer_name.setText(intent.getData().toString());

        beer_country.setOnClickListener(this);
        country_chevron.setOnClickListener(this);

        beer_brand.setOnClickListener(this);
        brand_chevron.setOnClickListener(this);

        //endregion


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.send,menu);
        menu.findItem(R.id.action_send).setEnabled(
            !TextUtils.isEmpty(beer_name.getText())&&
            !TextUtils.isEmpty(wrapperParamsBeer.get(wrapperParamsBeer.createKey(Keys.COUNTRY_ID)))&&
            !TextUtils.isEmpty(wrapperParamsBeer.get(wrapperParamsBeer.createKey(Keys.BRAND_ID)))
        );
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_create_beer_country:
            case R.id.fragment_create_beer_country_chevron:{
                //region Select country
                Starter.SelectCategoryActivity(
                        CreateBeerFragment.this,
                        SearchFragment.CATEGORY_LIST_BEER,
                        FilterBeerField.COUNTRY,
                        wrapperParamsBeer.get(wrapperParamsBeer.createKey(Keys.COUNTRY_ID)),
                        beer_country.getText().toString(),
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                //endregion
            }break;
            case R.id.fragment_create_beer_brand:
            case R.id.fragment_create_beer_brand_chevron:{
                //region Select brand
                Starter.SelectCategoryActivity(
                        CreateBeerFragment.this,
                        SearchFragment.CATEGORY_LIST_BEER,
                        FilterBeerField.BRAND,
                        wrapperParamsBeer.get(wrapperParamsBeer.createKey(Keys.BRAND_ID)),
                        beer_brand.getText().toString(),
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                //endregion
            }break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodes.REQUEST_SEARCH_CODE:
                if(resultCode== Activity.RESULT_OK){
                    String param3=data.getStringExtra(Actions.PARAM3);
                    String param4=data.getStringExtra(Actions.PARAM4);
                    switch (data.getIntExtra(Actions.PARAM2,Integer.MAX_VALUE)) {
                        case FilterBeerField.COUNTRY:
                            try {
                                wrapperParamsBeer.addParam(Keys.COUNTRY_ID,param3.split(",")[0]);
                                beer_country.setText(param4.split(",")[0]);
                                mListener.invalidateOptionsMenu();
                            }catch (Exception e){}
                            break;
                        case FilterBeerField.BRAND:
                            try {
                                wrapperParamsBeer.addParam(Keys.BRAND_ID,param3.split(",")[0]);
                                beer_brand.setText(param4.split(",")[0]);
                                mListener.invalidateOptionsMenu();
                            }catch (Exception e){}
                            break;
                    }
                }
                break;
        }
                super.onActivityResult(requestCode, resultCode, data);
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
