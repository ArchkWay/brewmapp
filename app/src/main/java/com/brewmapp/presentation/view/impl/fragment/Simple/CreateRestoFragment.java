package com.brewmapp.presentation.view.impl.fragment.Simple;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.Location;
import com.brewmapp.data.entity.LocationChild;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.CreateLocationTask;
import com.brewmapp.execution.task.CreateRestoTask;
import com.brewmapp.presentation.view.impl.dialogs.DialogSelectAddress;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

import java.io.File;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
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
    @BindView(R.id.fragment_create_resto_description)    TextInputEditText description;
    //endregion

    //region Private
    private OnFragmentInteractionListener mListener;
    private WrapperParams wrapperParamsResto =new WrapperParams(Keys.CAP_RESTO);
    private WrapperParams wrapperParamsLocation =new WrapperParams(Wrappers.LOCATION);
    private Location location=new Location();
    private HashMap<String,File> hashPhotos=new HashMap<>();
    //endregion

    @Inject    CreateRestoTask createRestoTask;
    @Inject    CreateLocationTask createLocationTask;

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
                    wrapperParamsResto.addParam(Keys.NAME,s.toString());
                    mListener.invalidateOptionsMenu();},
                name_resto);

        name_resto.setText(intent.getData().toString());

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
                !TextUtils.isEmpty(wrapperParamsResto.get(wrapperParamsResto.createKey(Keys.NAME)))&&
                !TextUtils.isEmpty(wrapperParamsResto.get(wrapperParamsResto.createKey(Keys.RESTO_TYPE)))&&
                !TextUtils.isEmpty(address.getText())&&
                !TextUtils.isEmpty(wrapperParamsLocation.get(wrapperParamsLocation.createKey(Keys.COUNTRY_ID)))
        );

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_send:
                wrapperParamsLocation.addParam(Keys.NAME,wrapperParamsResto.get(wrapperParamsResto.createKey(Keys.NAME)));
                createLocationTask.execute(wrapperParamsLocation,new SimpleSubscriber<LocationChild>(){
                    @Override
                    public void onNext(LocationChild locationChild) {
                        super.onNext(locationChild);
                        wrapperParamsResto.addParam(Keys.LOCATION_ID,locationChild.getId());
                        wrapperParamsResto.addParam(Keys.TEXT,description.getText().toString().trim());
                        createRestoTask.execute(wrapperParamsResto,new SimpleSubscriber<String>(){
                            @Override
                            public void onNext(String s) {
                                super.onNext(s);
                                mListener.showSnackbar(getString(R.string.text_resto_sended_to_moderation),new Snackbar.Callback(){
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        super.onDismissed(transientBottomBar, event);
                                        mListener.finish();
                                    }
                                });


                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                    mListener.showSnackbarRed(getString(R.string.error));
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mListener.showSnackbarRed(getString(R.string.error));
                    }
                });
                break;
        }
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
                //region Select address
                new DialogSelectAddress()
                        .setLocation(location)
                        .showDialog(
                                getFragmentManager(),
                                locationFound -> {
                                    address.setText(locationFound.getFormatLocation());
                                    location=locationFound;
                                    wrapperParamsLocation.addParam(Keys.COUNTRY_ID,"1");
                                    wrapperParamsLocation.addParam(Keys.LAT,String.valueOf(location.getLocation().getLat()));
                                    wrapperParamsLocation.addParam(Keys.LON,String.valueOf(location.getLocation().getLon()));
                                    mListener.invalidateOptionsMenu();
                                });
                //endregion
                break;
            case R.id.fragment_create_resto_chevron_type_resto:
            case R.id.fragment_create_resto_type_resto:
                //region Select type resto
                Starter.SelectCategoryActivity(
                        CreateRestoFragment.this,
                        SearchFragment.CATEGORY_LIST_RESTO,
                        FilterRestoField.TYPE,
                        wrapperParamsResto.get(wrapperParamsResto.createKey(Keys.RESTO_TYPE)),
                        type_resto.getText().toString(),
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                //endregion
                break;
            case R.id.fragment_create_resto_chevron_kitchen:
            case R.id.fragment_create_resto_kitchen:
                //region select Kitchen
                Starter.SelectCategoryActivity(
                        CreateRestoFragment.this,
                        SearchFragment.CATEGORY_LIST_RESTO,
                        FilterRestoField.KITCHEN,
                        wrapperParamsResto.get(wrapperParamsResto.createKey(Keys.RESTO_KITCHEN)),
                        kitchen.getText().toString(),
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                //endregion
                break;
            case R.id.fragment_create_resto_chevron_features:
            case R.id.fragment_create_resto_features:
                //region Select Features
                Starter.SelectCategoryActivity(
                        CreateRestoFragment.this,
                        SearchFragment.CATEGORY_LIST_RESTO,
                        FilterRestoField.FEATURES,
                        wrapperParamsResto.get(wrapperParamsResto.createKey(Keys.RESTO_FEATURES)),
                        features.getText().toString(),
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                //endregion
                break;
            case R.id.fragment_create_resto_chevron_prices:
            case R.id.fragment_create_resto_prices:
                //region Select Price
                Starter.SelectCategoryActivity(
                        CreateRestoFragment.this,
                        SearchFragment.CATEGORY_LIST_RESTO,
                        FilterRestoField.PRICE,
                        wrapperParamsResto.get(wrapperParamsResto.createKey(Keys.RESTO_AVERAGE)),
                        prices.getText().toString(),
                        RequestCodes.REQUEST_SEARCH_CODE
                );
                //endregion
                break;
            case R.id.fragment_create_resto_chevron_photo:
            case R.id.fragment_create_resto_photo:
                //region Select Photo
                mListener.selectPhoto((text, position) -> {
                    switch (position) {
                        case 0:
                            RxPaparazzo.single(getActivity())
                                    .usingGallery()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(response -> {
                                        if (response.resultCode() == RESULT_OK) {
                                            File file=response.data().getFile();
                                            hashPhotos.put(file.getName(),file);
                                            photo.setText(MapUtils.strJoin(hashPhotos.keySet().toArray(),","));
                                        }
                                        //sendImage(response.data().getFile());
                                    });

                            break;
                        case 1:
                            RxPaparazzo.single(getActivity())
                                    .usingCamera()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(response -> {
                                        if (response.resultCode() == RESULT_OK) {
                                            File file=response.data().getFile();
                                            hashPhotos.put(file.getName(),file);
                                            photo.setText(MapUtils.strJoin(hashPhotos.keySet().toArray(),","));
                                        }
                                        //sendImage(response.data().getFile());
                                    });
                            break;
                    }
                });
                //endregion

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
                            wrapperParamsResto.addParam(Keys.RESTO_TYPE,data.getStringExtra(Actions.PARAM3));
                            type_resto.setText(data.getStringExtra(Actions.PARAM4));
                            mListener.invalidateOptionsMenu();
                            break;
                        case FilterRestoField.KITCHEN:
                            wrapperParamsResto.addParam(Keys.RESTO_KITCHEN,data.getStringExtra(Actions.PARAM3));
                            kitchen.setText(data.getStringExtra(Actions.PARAM4));
                            break;
                        case FilterRestoField.FEATURES:
                            wrapperParamsResto.addParam(Keys.RESTO_FEATURES,data.getStringExtra(Actions.PARAM3));
                            features.setText(data.getStringExtra(Actions.PARAM4));
                            break;
                        case FilterRestoField.PRICE:
                            try {
                                String parseText[] = data.getStringExtra(Actions.PARAM4).trim().split(",")[0].split(" ");
                                wrapperParamsResto.addParam(Keys.AVG_COST,parseText[1]);
                                wrapperParamsResto.addParam(Keys.AVG_COST_MAX,parseText[3]);
                                prices.setText(String.format("%s %s %s %s",parseText[0],parseText[1],parseText[2],parseText[3]));
                                break;
                            }catch (Exception e){}

                            try {
                                String parseText[] = data.getStringExtra(Actions.PARAM4).trim().split(",")[0].split(" ");
                                wrapperParamsResto.addParam(Keys.AVG_COST,parseText[1]);
                                wrapperParamsResto.remove(Keys.AVG_COST_MAX);
                                prices.setText(String.format("%s %s",parseText[0],parseText[1]));
                                break;
                            }catch (Exception e){}

                            prices.setText(null);
                            wrapperParamsResto.remove(Keys.AVG_COST_MAX);
                            wrapperParamsResto.remove(Keys.AVG_COST);
                    }
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        name_resto.postDelayed(() -> mListener.hideKeyboard(),500);
    }

    public interface OnFragmentInteractionListener {
        void commonError(String... message);
        void invalidateOptionsMenu();
        void setTitle(CharSequence name);
        Intent getIntent();
        void hideKeyboard();
        void selectPhoto(SelectListener listener);
        void changeListeners(SimpleTextChangeCallback changeCallback, TextView... views);

        void showSnackbarRed(String string);

        void showSnackbar(String string, Snackbar.Callback callback);

        void finish();
    }

}
