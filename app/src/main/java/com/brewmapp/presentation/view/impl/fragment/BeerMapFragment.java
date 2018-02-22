package com.brewmapp.presentation.view.impl.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.entity.FilterRestoOnMap;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.BeerMapPresenter;
import com.brewmapp.presentation.view.contract.BeerMapView;
import com.brewmapp.presentation.view.contract.OnLocationInteractionListener;
import com.brewmapp.presentation.view.impl.activity.FilterMapActivity;
import com.brewmapp.presentation.view.impl.widget.FinderView;
import com.brewmapp.utils.events.markerCluster.ClusterRender;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;
import ru.frosteye.ovsa.tool.UITools;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_MAP_RESULT;

/**
 * Created by ovcst on 24.08.2017.
 */
public class BeerMapFragment extends BaseFragment implements BeerMapView,
        OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.InfoWindowAdapter

{

    @BindView(R.id.fragment_map_map)
    MapView mapView;
    @BindView(R.id.activity_search_search)
    FinderView finder;
    @BindView(R.id.restoList)
    RecyclerView list;

    @Inject
    BeerMapPresenter presenter;

    private OnFragmentInteractionListener mListener;
    private OnLocationInteractionListener mLocationListener;

    private GoogleMap googleMap;
    private Marker marker;
    private RestoLocation location;
    private ProgressDialog dialog;
    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage searchPackage;
    private EndlessRecyclerOnScrollListener scrollListener;
    private ClusterManager<FilterRestoLocation> mClusterManager;
    private GeoPackage geoPackage=new GeoPackage();
    private HashMap<String,String> hashMap=new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    protected void initView(View fragmentView) {
        setHasOptionsMenu(true);
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

        finder.setListener(this::prepareQuery);

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
        UITools.hideKeyboard(getActivity());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu!=null)
            menu.clear();
        inflater.inflate(R.menu.filter,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mClusterManager = new ClusterManager<>(getContext(), googleMap);
        mClusterManager.setRenderer(new ClusterRender(getContext(), googleMap, mClusterManager));
        googleMap.setInfoWindowAdapter(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMyLocationButtonClickListener(() -> {
            mLocationListener.getLocation(result -> onLocationFound(result));
            return true;
        });

        googleMap.setOnMapClickListener(latLng -> presenter.onGeocodeRequest(latLng));

        if (location != null) {
            setSingleMarker();
        } else {
            mLocationListener.getLocation(this::onLocationFound);
        }
    }

    protected void onLocationFound(Location location) {
        if(location==null)
            location=MapUtils.getDefaultLocation(getContext());

        if(location!=null) {
                // 1 градус долготы примерно 111 км
                LatLngBounds AUSTRALIA = new LatLngBounds(
                        new LatLng(
                                location.getLatitude()-0.1,//минус 10 км от центра
                                location.getLongitude()
                        ), new LatLng(
                                location.getLatitude()+0.1,//плюс 10 км от центра
                                location.getLongitude()
                        )
                );
            googleMap.setOnCameraIdleListener(this);
                googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 14));
            }
        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }else {
            googleMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void showGeolocationResult(List<FilterRestoLocation> restoLocations) {
        setMarker(restoLocations, true);
    }

    @Override
    public void showDialogProgressBar(boolean show) {
        if (show) {
            dialog = ProgressDialog.show(getContext(), getString(R.string.loading),
                    getString(R.string.search_resto_message), true, false);
        } else {
            if (dialog != null) {
                dialog.cancel();
            }
        }
    }

    @Override
    public void showProgressBar() {
        finder.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        finder.findViewById(R.id.finder_cancel).setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        finder.findViewById(R.id.progressBar).setVisibility(View.GONE);
        finder.findViewById(R.id.finder_cancel).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBarAction(int id) {
        Intent intent = new Intent(getContext(), FilterMapActivity.class);
        getActivity().startActivityForResult(intent, REQUEST_CODE_MAP_RESULT);
    }

    @Override
    public void appendItems(List<IFlexible> restoList) {
        adapter.clear();
        if (restoList.size() > 5) {
            list.getLayoutParams().height = 290;
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            list.setLayoutParams(params);
        }
        showLogicAnimation();
        adapter.addItems(adapter.getItemCount(), restoList);
    }

    @Override
    protected void prepareView(View view) {
        super.prepareView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mLocationListener = mListener.getLocationListener();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCameraIdle() {
        VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();
        LatLng farRight = visibleRegion.farRight;
        LatLng nearLeft = visibleRegion.nearLeft;
        String coordStart = String.format(Locale.getDefault(),"%.2f|%.2f",nearLeft.latitude,nearLeft.longitude);
        String coordEnd = String.format(Locale.getDefault(),"%.2f|%.2f",farRight.latitude,farRight.longitude);
        Log.i("onCameraMove","coordStart - "+coordStart+"  coordEnd-"+coordEnd );
        geoPackage.setCoordStart(coordStart);
        geoPackage.setCoordEnd(coordEnd);
        presenter.loadRestoByLatLngBounds(geoPackage);
    }


//*************************************
    private void prepareQuery(String stringSearch) {
    searchPackage.setPage(0);
    searchPackage.setStringSearch(stringSearch);
    if (stringSearch.length() > 3) {
        showProgressBar();
        presenter.sendQueryRestoSearch(searchPackage);
    } else if (stringSearch.length() == 0) {
        UITools.hideKeyboard(getActivity());
        adapter.clear();
        hideProgressBar();
        showLogicAnimation();
    }
}

    private void processAction(int action, Object payload) {
        UITools.hideKeyboard(getActivity());
        FilterRestoOnMap filterOnMapResto = (FilterRestoOnMap) payload;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com").appendPath("maps").appendPath("dir").appendPath("").appendQueryParameter("api", "1")
                .appendQueryParameter("destination", filterOnMapResto.getLocationLat() + "," + filterOnMapResto.getLocationLon());
        String url = builder.build().toString();
        Log.d("Directions", url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void showLogicAnimation() {
        animFlowBrandList();
    }

    private void animFlowBrandList() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), list.isShown() ? R.anim.fadeout : R.anim.fadein);
        list.startAnimation(animation);
        list.setVisibility(list.isShown() ? View.GONE : View.VISIBLE);
    }

    private void setSingleMarker() {
        showDialogProgressBar(false);
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

        for(FilterRestoLocation filterRestoLocation:restoLocations) {
            String key = filterRestoLocation.getRestoId();
            if (!hashMap.containsKey(key)) {
                mClusterManager.addItem(filterRestoLocation);
                hashMap.put(key,key);
            }
        }
        mClusterManager.cluster();

    }

    public void showResult(boolean isBeer, int checkBox) {
        showDialogProgressBar(true);
        if (!isBeer) {
            presenter.loadRestoCoordinates(Paper.book().read("restoCategoryList"), checkBox);
        } else {
            presenter.loadBeerCoordinates(Paper.book().read("beerCategoryList"),  checkBox);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Starter.RestoDetailActivity(getContext(),marker.getSnippet());
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view=null;
        if(marker.getSnippet()!=null) {
            view = getLayoutInflater().inflate(R.layout.layout_info_window, null);
            TextView restoTitle = ((TextView) view.findViewById(R.id.title));
            restoTitle.setTypeface(null, Typeface.BOLD_ITALIC);
            restoTitle.setText(marker.getTitle());
        }
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    public interface OnFragmentInteractionListener {
        void commonError(String... message);
        void setTitle(CharSequence name);
        OnLocationInteractionListener getLocationListener();
    }

}
