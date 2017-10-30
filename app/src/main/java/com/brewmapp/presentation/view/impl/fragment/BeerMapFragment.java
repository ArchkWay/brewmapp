package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.data.pojo.GeolocatorResultPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.BeerMapPresenter;
import com.brewmapp.presentation.presenter.impl.LocationFragment;
import com.brewmapp.presentation.view.contract.BeerMapView;
import com.brewmapp.presentation.view.impl.activity.FilterMapActivity;
import com.brewmapp.presentation.view.impl.activity.NewPostActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;
import com.brewmapp.presentation.view.impl.activity.SearchActivity;
import com.brewmapp.presentation.view.impl.widget.RestoInfoWindow;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.data.entity.SimpleLocation;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_MAP_REFRESH;
import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

/**
 * Created by ovcst on 24.08.2017.
 */

public class BeerMapFragment extends LocationFragment implements BeerMapView, OnMapReadyCallback,
                                                                 GoogleMap.OnInfoWindowClickListener {

    @BindView(R.id.fragment_map_map) MapView mapView;

    @Inject BeerMapPresenter presenter;

    private GoogleMap googleMap;
    private Marker marker;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void enableControls(boolean enabled, int code) {
    }

    @Override
    protected void initView(View view) {
        interractor().processSetActionBar(Actions.ACTION_FILTER);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
        lookForLocation();
        googleMap.setMyLocationEnabled(true);
        googleMap.setInfoWindowAdapter(new RestoInfoWindow(getActivity()));
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(() -> {
            lookForLocation();
            return true;
        });

        googleMap.setOnMapClickListener(latLng -> {
            presenter.onGeocodeRequest(latLng);
        });
    }

    private void setMarker(List<RestoLocation> restoLocationList) {
        if(marker != null) marker.remove();
        for (RestoLocation restoLocation : restoLocationList) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .title(restoLocation.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_green))
                    .snippet(String.valueOf(restoLocation.getId()))
                    .position(new LatLng(restoLocation.getLocation_lat(), restoLocation.getLocation_lon()));

            googleMap.addMarker(markerOptions);
        }

//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(forTestLatng, 14));
    }

    @Override
    protected void onLocationFound(Location location) {
        presenter.onLocationChanged(new SimpleLocation(location));
        presenter.onLoadedCity(getCityName(location));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                location.getLatitude(), location.getLongitude()
        ), 14));
    }

    private String getCityName(Location location) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses != null ? addresses.get(0).getLocality() : null;
    }

    public void showResult() {

    }

    @Override
    public void showGeolocationResult(List<RestoLocation> resultPackage) {
        setMarker(resultPackage);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Interest interest = new Interest();
        Interest_info interest_info = new Interest_info();
        interest_info.setId(marker.getSnippet());
        interest.setInterest_info(interest_info);
        Intent intent = new Intent(getContext(), RestoDetailActivity.class);
        intent.putExtra(RESTO_ID, interest);
        startActivity(intent);
    }

    @Override
    public void onBarAction(int id) {
        interractor().processStartActivityWithRefresh(new Intent(getActivity(), FilterMapActivity.class), REQUEST_CODE_MAP_REFRESH);
    }
}
