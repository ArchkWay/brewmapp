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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.Location;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogSelectAddress;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.frosteye.ovsa.execution.network.request.RequestParams;
import ru.frosteye.ovsa.presentation.callback.SimpleTextChangeCallback;
import ru.frosteye.ovsa.stub.listener.SelectListener;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateRestoFragment extends Fragment implements View.OnClickListener {

    //region BindView
    @BindView(R.id.fragment_create_resto_name_resto)    TextInputEditText name_resto;
    @BindView(R.id.fragment_create_resto_address)    TextInputEditText address;
    @BindView(R.id.fragment_create_resto_chevron_address)    ImageView chevron_address;
    @BindView(R.id.fragment_create_resto_type_resto)    TextInputEditText type_resto;
    @BindView(R.id.fragment_create_resto_chevron_type_resto)    ImageView chevron_type_resto;
    @BindView(R.id.fragment_create_resto_kitchen)    TextInputEditText kitchen;
    @BindView(R.id.fragment_create_resto_chevron_kitchen)    ImageView chevron_kitchen;
    @BindView(R.id.fragment_create_resto_features)    TextInputEditText features;
    @BindView(R.id.fragment_create_resto_chevron_features)    ImageView chevron_features;
    @BindView(R.id.fragment_create_resto_prices)    TextInputEditText prices;
    @BindView(R.id.fragment_create_resto_chevron_prices)    ImageView resto_chevron_prices;
    @BindView(R.id.fragment_create_resto_photo)    TextInputEditText photo;
    @BindView(R.id.fragment_create_resto_chevron_photo)    ImageView chevron_photo;
    //endregion

    //region Private
    private OnFragmentInteractionListener mListener;
    private WrapperParams wrapperParams=new WrapperParams(Keys.CAP_RESTO);
    private Location location=new Location();
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

        mListener.changeListeners(s->{
                    wrapperParams.put(Keys.NAME,s.toString());
                    mListener.invalidateOptionsMenu();},
                name_resto);

        name_resto.setText(intent.getData().toString());
        name_resto.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.hideKeyboard();
            }
        },500);

        chevron_type_resto.setOnClickListener(this);
        type_resto.setOnClickListener(this);

        chevron_address.setOnClickListener(this);
        address.setOnClickListener(this);

        chevron_kitchen.setOnClickListener(this);
        kitchen.setOnClickListener(this);

        chevron_features.setOnClickListener(this);
        features.setOnClickListener(this);

        resto_chevron_prices.setOnClickListener(this);
        prices.setOnClickListener(this);

        chevron_photo.setOnClickListener(this);
        photo.setOnClickListener(this);


        //endregion
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.send,menu);
        menu.findItem(R.id.action_send).setEnabled(
                !TextUtils.isEmpty(wrapperParams.get(Keys.NAME))&&
                !TextUtils.isEmpty(wrapperParams.get(Keys.RESTO_TYPE))&&
                !TextUtils.isEmpty(address.getText())
        );

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
            case R.id.fragment_create_resto_chevron_address:
            case R.id.fragment_create_resto_address:
                new DialogSelectAddress()
                        .setLocation(location)
                        .showDialog(
                                getFragmentManager(),
                                locationFound -> {
                                    address.setText(locationFound.getFormatLocation());
                                    location=locationFound;
                                    mListener.invalidateOptionsMenu();
                                });
                break;
            case R.id.fragment_create_resto_chevron_type_resto:
            case R.id.fragment_create_resto_type_resto:
                Starter.SelectCategoryActivity(
                        CreateRestoFragment.this,
                        SearchFragment.CATEGORY_LIST_RESTO,
                        FilterRestoField.TYPE,
                        wrapperParams.get(Keys.RESTO_TYPE),
                        type_resto.getText().toString(),
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                break;
            case R.id.fragment_create_resto_chevron_kitchen:
            case R.id.fragment_create_resto_kitchen:
                Starter.SelectCategoryActivity(
                        CreateRestoFragment.this,
                        SearchFragment.CATEGORY_LIST_RESTO,
                        FilterRestoField.KITCHEN,
                        wrapperParams.get(Keys.RESTO_KITCHEN),
                        kitchen.getText().toString(),
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                break;
            case R.id.fragment_create_resto_chevron_features:
            case R.id.fragment_create_resto_features:
                Starter.SelectCategoryActivity(
                        CreateRestoFragment.this,
                        SearchFragment.CATEGORY_LIST_RESTO,
                        FilterRestoField.FEATURES,
                        wrapperParams.get(Keys.RESTO_FEATURES),
                        features.getText().toString(),
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                break;
            case R.id.fragment_create_resto_chevron_prices:
            case R.id.fragment_create_resto_prices:
                Starter.SelectCategoryActivity(
                        CreateRestoFragment.this,
                        SearchFragment.CATEGORY_LIST_RESTO,
                        FilterRestoField.PRICE,
                        wrapperParams.get(Keys.RESTO_AVERAGE),
                        prices.getText().toString(),
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                break;
            case R.id.fragment_create_resto_chevron_photo:
            case R.id.fragment_create_resto_photo:
                mListener.selectPhoto((text, position) -> {
                    switch (position) {
                        case 0:
                            RxPaparazzo.single(getActivity())
                                    .usingGallery()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(response -> {
                                        if (response.resultCode() != RESULT_OK) return;
                                        //sendImage(response.data().getFile());
                                    });

                            break;
                        case 1:
                            RxPaparazzo.single(getActivity())
                                    .usingCamera()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(response -> {
                                        if (response.resultCode() != RESULT_OK) return;
                                        //sendImage(response.data().getFile());
                                    });
                            break;
                    }
                });

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
                            wrapperParams.put(Keys.RESTO_TYPE,data.getStringExtra(Actions.PARAM3));
                            type_resto.setText(data.getStringExtra(Actions.PARAM4));
                            mListener.invalidateOptionsMenu();
                            break;
                        case FilterRestoField.KITCHEN:
                            wrapperParams.put(Keys.RESTO_KITCHEN,data.getStringExtra(Actions.PARAM3));
                            kitchen.setText(data.getStringExtra(Actions.PARAM4));
                            break;
                        case FilterRestoField.FEATURES:
                            wrapperParams.put(Keys.RESTO_FEATURES,data.getStringExtra(Actions.PARAM3));
                            features.setText(data.getStringExtra(Actions.PARAM4));
                            break;
                        case FilterRestoField.PRICE:
                            wrapperParams.put(Keys.RESTO_AVERAGE,data.getStringExtra(Actions.PARAM3));
                            prices.setText(data.getStringExtra(Actions.PARAM4));
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

        void hideKeyboard();

        void selectPhoto(SelectListener listener);

        void changeListeners(SimpleTextChangeCallback changeCallback, TextView... views);
    }

}
