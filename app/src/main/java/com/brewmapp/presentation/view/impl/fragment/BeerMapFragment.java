package com.brewmapp.presentation.view.impl.fragment;

import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.BeerMapPresenter;
import com.brewmapp.presentation.view.contract.BeerMapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class BeerMapFragment extends BaseFragment implements BeerMapView, OnMapReadyCallback {

    @BindView(R.id.fragment_map_map) MapView mapView;

    @Inject BeerMapPresenter presenter;

    private GoogleMap googleMap;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView(View view) {
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    protected void attachPresenter() {

    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return null;
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
        return ResourceHelper.getString(R.string.map);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }
}
