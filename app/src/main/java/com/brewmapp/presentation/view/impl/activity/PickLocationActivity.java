package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.wrapper.CommonItemInfo;
import com.brewmapp.data.model.ICommonItem;
import com.brewmapp.data.pojo.GeolocatorResultPackage;
import com.brewmapp.presentation.presenter.impl.LocationActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.PickLocationPresenter;
import com.brewmapp.presentation.view.contract.PickLocationView;

import ru.frosteye.ovsa.data.entity.SimpleLocation;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

import com.brewmapp.R;

import java.util.ArrayList;
import java.util.List;

public class PickLocationActivity extends LocationActivity implements PickLocationView,
        OnMapReadyCallback {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_pickLocation_map) MapView map;
    @BindView(R.id.activity_pickLocation_input) EditText input;
    @BindView(R.id.activity_pickLocation_list) RecyclerView list;
    @BindView(R.id.activity_pickLocation_fade) View fade;

    @Inject PickLocationPresenter presenter;

    private GoogleMap googleMap;
    private Marker marker;
    private FlexibleModelAdapter<CommonItemInfo> adapter;
    private GeolocatorResultPackage resultPackage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
    }

    @Override
    protected void onLocationFound(Location location) {
        presenter.onLocationChanged(new SimpleLocation(location));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                location.getLatitude(), location.getLongitude()
        ), 14));
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void enableControls(boolean enabled, int code) {
        showTopBarLoading(!enabled);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        menu.findItem(R.id.action_allDone).setEnabled(resultPackage != null);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_allDone) {
            Intent intent = new Intent();
            intent.putExtra(Keys.LOCATION, resultPackage);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        enableBackButton();

        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        adapter = new FlexibleModelAdapter<>(new ArrayList<>(), this, (code, payload) -> {
            Resto resto = ((Resto) payload);
            presenter.onSelectLocation(resto.getLocationId());
            hideKeyboard();
        });
        list.setAdapter(adapter);

        registerTextChangeListeners(s -> {
            if(s.length() < 3) return;
            presenter.onQuery(s.toString());
        }, input);

        map.onCreate(null);
        map.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
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
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        lookForLocation(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(() -> {
            lookForLocation(true);
            return true;
        });
        googleMap.setOnMapClickListener(latLng -> {
            presenter.onGeocodeRequest(latLng);
        });
    }

    private void setMarker(LatLng latLng, String title) {
        if(marker != null) marker.remove();
        MarkerOptions markerOptions = new MarkerOptions()
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_green))
                .position(latLng);

        marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        invalidateOptionsMenu();
    }

    @Override
    public void showVariants(List<? extends ICommonItem> restos) {
        adapter.updateDataSet(CommonItemInfo.prepareItems(restos));
    }

    @Override
    public void showGeolocationResult(GeolocatorResultPackage resultPackage) {
        this.resultPackage = resultPackage;
        setMarker(new LatLng(resultPackage.getLocation().getLat(), resultPackage.getLocation().getLng()),
                resultPackage.getAddress());
        invalidateOptionsMenu();
    }

    @Override
    public void enableSearchPanel(boolean enabled) {
        fade.setVisibility(enabled ? View.GONE : View.VISIBLE);
    }
}
