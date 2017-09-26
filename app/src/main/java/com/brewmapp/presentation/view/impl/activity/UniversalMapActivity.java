package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.model.ILocation;
import com.brewmapp.presentation.presenter.contract.MapPresenter;
import com.brewmapp.presentation.view.contract.UniversalMapView;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import com.brewmapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

public class UniversalMapActivity extends BaseActivity implements UniversalMapView, OnMapReadyCallback {

    @BindView(R.id.activity_map_map) MapView mapView;
    @BindView(R.id.common_toolbar) Toolbar toolbar;

    @Inject MapPresenter presenter;

    private GoogleMap googleMap;
    private ILocation point;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void initView() {
        enableBackButton();
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public void showLocation(ILocation location) {
        this.point = location;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions()
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_green))
                .title(point.title())
                .position(point.position()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point.position(), 14));
    }
}
