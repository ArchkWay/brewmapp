package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.BeerMapPresenter;
import com.brewmapp.presentation.presenter.impl.LocationFragment;
import com.brewmapp.presentation.view.contract.BeerMapView;
import com.brewmapp.presentation.view.impl.activity.FilterMapActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;
import com.brewmapp.presentation.view.impl.widget.FinderView;
import com.brewmapp.presentation.view.impl.widget.RestoInfoWindow;
import com.brewmapp.utils.events.ShowRestoOnMapEvent;
import com.brewmapp.utils.events.markerCluster.ClusterRender;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.paperdb.Paper;
import ru.frosteye.ovsa.data.entity.SimpleLocation;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

/**
 * Created by ovcst on 24.08.2017.
 */
public class BeerMapFragment extends LocationFragment implements BeerMapView, OnMapReadyCallback, ClusterManager.OnClusterItemInfoWindowClickListener<FilterRestoLocation>,
        ClusterManager.OnClusterClickListener<FilterRestoLocation>, ClusterManager.OnClusterInfoWindowClickListener<FilterRestoLocation>, ClusterManager.OnClusterItemClickListener<FilterRestoLocation>{

    @BindView(R.id.fragment_map_map)
    MapView mapView;
    @BindView(R.id.activity_search_search)
    FinderView finder;

    @Inject
    BeerMapPresenter presenter;

    private GoogleMap googleMap;
    private Marker marker;
    private RestoLocation location;

    private ClusterManager<FilterRestoLocation> mClusterManager;

    public static BeerMapFragment newInstance(RestoLocation restoLocation) {
        BeerMapFragment fragment = new BeerMapFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Keys.LOCATION, restoLocation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(getContext());
        Bundle arguments = getArguments();
        if (arguments != null) {
            location = (RestoLocation) arguments.getSerializable(Keys.LOCATION);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void enableControls(boolean enabled, int code) {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
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
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mClusterManager = new ClusterManager<>(getContext(), googleMap);

        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        googleMap.setOnMyLocationButtonClickListener(() -> {
            lookForLocation();
            return true;
        });

        googleMap.setOnMapClickListener(latLng -> presenter.onGeocodeRequest(latLng));

        if (location != null) {
            setSingleMarker();
        } else {
            lookForLocation();
        }
    }

    private void setSingleMarker() {
        if (marker != null) marker.remove();
        MarkerOptions markerOptions = new MarkerOptions()
                .title(location.getName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_green))
                .snippet(String.valueOf(location.getId()))
                .position(new LatLng(location.getLocation_lat(), location.getLocation_lon()));

        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLocation_lat(),
                location.getLocation_lon()) , 15));
    }

    private void setMarker(List<FilterRestoLocation> restoLocations, boolean animateCamera) {
        mClusterManager.setRenderer(new ClusterRender(getContext(), googleMap, mClusterManager));
        mClusterManager.addItems(restoLocations);

        if (animateCamera) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(restoLocations.get(0).getLocationLat(),
                    restoLocations.get(0).getLocationLon()) , 9));
        }
    }

    @Override
    protected void onLocationFound(Location location) {
        presenter.onLocationChanged(new SimpleLocation(location));
        presenter.onLoadedCity(MapUtils.getCityName(location, getActivity()));
        googleMap.setInfoWindowAdapter(new RestoInfoWindow(getActivity(), location));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                location.getLatitude(), location.getLongitude()
        ), 14));
    }

    public void showResult() {
    }

    @Override
    public void showGeolocationResult(List<FilterRestoLocation> restoLocations) {
        setMarker(restoLocations, false);
    }

    @Override
    public void onBarAction(int id) {
        Intent intent = new Intent(getContext(), FilterMapActivity.class);
        startActivity(intent);
    }

    @Subscribe
    public void onEvent(ShowRestoOnMapEvent event) {
        googleMap.clear();
        setMarker(event.getRestoLocationList(), true);
    }

    @Override
    public void onClusterItemInfoWindowClick(FilterRestoLocation filterRestoLocation) {
        Interest interest = new Interest();
        Interest_info interest_info = new Interest_info();
        interest_info.setId(filterRestoLocation.getRestoId());
        interest.setInterest_info(interest_info);
        Intent intent = new Intent(getContext(), RestoDetailActivity.class);
        intent.putExtra(RESTO_ID, interest);
        startActivity(intent);
    }

    @Override
    public boolean onClusterClick(Cluster<FilterRestoLocation> cluster) {
        Log.i("clusterClik222", String.valueOf(cluster.getPosition()));
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<FilterRestoLocation> cluster) {
        Log.i("clusterInfoClick111", String.valueOf(cluster.getPosition()));
    }

    @Override
    public boolean onClusterItemClick(FilterRestoLocation filterRestoLocation) {
        Log.i("clusterInfoClick111", String.valueOf(filterRestoLocation.getmName()));
        return false;
    }
}
