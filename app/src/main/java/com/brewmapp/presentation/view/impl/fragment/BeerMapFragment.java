package com.brewmapp.presentation.view.impl.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.BeerMapPresenter;
import com.brewmapp.presentation.presenter.impl.LocationFragment;
import com.brewmapp.presentation.view.contract.BeerMapView;
import com.brewmapp.presentation.view.impl.activity.FilterMapActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;
import com.brewmapp.presentation.view.impl.widget.FinderView;
import com.brewmapp.presentation.view.impl.widget.RestoInfoWindow;
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
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import ru.frosteye.ovsa.data.entity.SimpleLocation;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_MAP_RESULT;
import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

/**
 * Created by ovcst on 24.08.2017.
 */
public class BeerMapFragment extends LocationFragment implements BeerMapView, OnMapReadyCallback,
        ClusterManager.OnClusterItemInfoWindowClickListener<FilterRestoLocation> {

    @BindView(R.id.fragment_map_map)
    MapView mapView;
    @BindView(R.id.activity_search_search)
    FinderView finder;
    @BindView(R.id.restoList)
    RecyclerView list;

    @Inject
    BeerMapPresenter presenter;

    private GoogleMap googleMap;
    private Marker marker;
    private RestoLocation location;
    private ProgressDialog dialog;
    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage searchPackage;
    private EndlessRecyclerOnScrollListener scrollListener;

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
    protected void initView(View view) {
        dialog = ProgressDialog.show(getContext(), "Загрузка...",
                "Поиск заведений...", true, false);
        interractor().processSetActionBar(Actions.ACTION_FILTER);

        searchPackage = new FullSearchPackage();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                searchPackage.setPage(currentPage - 1);
            }
        };
        list.addOnScrollListener(scrollListener);
        list.addItemDecoration(new ListDivider(getContext(), ListDivider.VERTICAL_LIST));
        list.setLayoutManager(manager);
        adapter = new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        list.setAdapter(adapter);
//        list.setOnTouchListener((view, motionEvent) -> {
//            UITools.hideKeyboard(getContext());
//            return false;
//        });

        finder.setListener(this::prepareQuery);

        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    private void prepareQuery(String stringSearch) {
        searchPackage.setPage(0);
        searchPackage.setStringSearch(stringSearch);
        if (stringSearch.length() > 3) {
            presenter.sendQueryRestoSearch(searchPackage);
        } else if (stringSearch.length() == 0) {
            adapter.clear();
            list.setVisibility(View.GONE);
        }
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
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mClusterManager = new ClusterManager<>(getContext(), googleMap);

        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        googleMap.setOnMyLocationButtonClickListener(() -> {
            lookForLocation();
            return true;
        });

        googleMap.setOnMapClickListener(latLng -> presenter.onGeocodeRequest(latLng));
        VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();
        Log.i("nearLeft", String.valueOf(visibleRegion.nearLeft.latitude));
        Log.i("neartRight", String.valueOf(visibleRegion.nearRight.latitude));
        Log.i("farLeft", String.valueOf(visibleRegion.farLeft.latitude));
        Log.i("farRight", String.valueOf(visibleRegion.farRight.latitude));

        if (location != null) {
            setSingleMarker();
        } else {
            lookForLocation();
        }
    }

    private void setSingleMarker() {
        showProgressBar(false);
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
        showProgressBar(false);
        presenter.onLocationChanged(new SimpleLocation(location));
        presenter.onLoadedCity(MapUtils.getCityName(location, getActivity()));
        googleMap.setInfoWindowAdapter(new RestoInfoWindow(getActivity(), location));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                location.getLatitude(), location.getLongitude()
        ), 14));
    }

    @Override
    public void showGeolocationResult(List<FilterRestoLocation> restoLocations) {
        showProgressBar(false);
        setMarker(restoLocations, true);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            dialog.show();
        } else {
            dialog.cancel();
        }
    }

    @Override
    public void onBarAction(int id) {
        Intent intent = new Intent(getContext(), FilterMapActivity.class);
        getActivity().startActivityForResult(intent, REQUEST_CODE_MAP_RESULT);
    }

    public void showResult(boolean isBeer, int checkBox) {
        showProgressBar(true);
        if (!isBeer) {
            presenter.loadRestoCoordinates(Paper.book().read("restoCategoryList"), checkBox);
        } else {
            presenter.loadBeerCoordinates(Paper.book().read("beerCategoryList"),  checkBox);
        }
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

    private void processAction(int action, Object payload) {
        FilterRestoLocation filterRestoLocation = (FilterRestoLocation) payload;
        Log.i("filterLoc:", filterRestoLocation.getmName());
    }

    @Override
    public void appendItems(List<IFlexible> restoList) {
        list.setVisibility(View.VISIBLE);
//        if(searchPackage.getPage() == 0) {
//            adapter.clear();
//            scrollListener.reset();
//            this.list.addOnScrollListener(scrollListener);
//            list.setVisibility(View.GONE);
//        }
        adapter.addItems(adapter.getItemCount(), restoList);
    }

}
