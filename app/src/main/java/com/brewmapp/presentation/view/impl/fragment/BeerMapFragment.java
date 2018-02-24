package com.brewmapp.presentation.view.impl.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.entity.FilterRestoOnMap;
import com.brewmapp.data.entity.MenuField;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.BeerMapPresenter;
import com.brewmapp.presentation.view.contract.BeerMapView;
import com.brewmapp.presentation.view.contract.OnLocationInteractionListener;
import com.brewmapp.presentation.view.impl.activity.FilterMapActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogShowView;
import com.brewmapp.presentation.view.impl.widget.FinderView;
import com.brewmapp.presentation.view.impl.widget.InfoWindowMap;
import com.brewmapp.utils.events.markerCluster.ClusterRender;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
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
public class BeerMapFragment extends BaseFragment implements
                                                            BeerMapView,
                                                            OnMapReadyCallback,
                                                            GoogleMap.OnCameraIdleListener,
                                                            GoogleMap.OnInfoWindowClickListener,
                                                            GoogleMap.InfoWindowAdapter,
                                                            ClusterManager.OnClusterClickListener<FilterRestoLocation>,
                                                            ClusterManager.OnClusterItemClickListener<FilterRestoLocation>
{

    //region BIND
    @BindView(R.id.fragment_map_map)
    MapView mapView;
    @BindView(R.id.activity_search_search)
    FinderView finder;
    @BindView(R.id.restoList)
    RecyclerView list;
    //endregion

    //region INJECT
    @Inject
    BeerMapPresenter presenter;
    //endregion

    //region mVALUES
    private OnFragmentInteractionListener mListener;
    private OnLocationInteractionListener mLocationListener;
    private GoogleMap googleMap;
    private Marker marker;
    private ProgressDialog dialog;
    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage searchPackage;
    private EndlessRecyclerOnScrollListener scrollListener;
    private ClusterManager<FilterRestoLocation> mClusterManager;
    private GeoPackage geoPackage=new GeoPackage();
    private HashMap<String,FilterRestoLocation> hashMap=new HashMap<>();
    private LatLngBounds latLngBounds;
    private int cntRequestFromUI =0;
    private InfoWindowMap viewInfoWindow =null;
    private float maxZoomPref=18.0f;
    private float minZoomPref=12.0f;
    private DialogShowView dialogShowView;
    private Location userLocation=null;


    //endregion

    //region FRAGMENT
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        list.addOnScrollListener(scrollListener);
        list.addItemDecoration(new ListDivider(getContext(), ListDivider.VERTICAL_LIST));
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        scrollListener = new EndlessRecyclerOnScrollListener((LinearLayoutManager) list.getLayoutManager()) {
            @Override
            public void onLoadMore(int currentPage) {
                searchPackage.setPage(currentPage - 1);
            }
        };

        adapter = new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        list.setAdapter(adapter);
        finder.setListener(this::prepareQuery);
        finder.clearFocus();

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
        mListener.processChangeFragment(MenuField.SEARCH);
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
    //endregion

    //region GoogleMapp
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMinZoomPreference(minZoomPref);
        googleMap.setMaxZoomPreference(maxZoomPref);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mClusterManager = new ClusterManager<>(getContext(), googleMap);
        mClusterManager.setRenderer(new ClusterRender(getContext(), googleMap, mClusterManager));
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setInfoWindowAdapter(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMyLocationButtonClickListener(() -> {
            mLocationListener.requestLocation(result -> onLocationFound(result));
            mLocationListener.refreshLocation();
            return true;
        });
        googleMap.setOnMapClickListener(latLng -> clearDialog());
        googleMap.setOnMapLoadedCallback(() -> mLocationListener.requestLocation(result -> onLocationFound(result)));
    }

    @Override
    public void showGeolocationResult(List<FilterRestoLocation> restoLocations) {

        for(FilterRestoLocation filterRestoLocation:restoLocations) {
            String key = filterRestoLocation.getRestoId();
            if (!hashMap.containsKey(key)) {
                mClusterManager.addItem(filterRestoLocation);
                hashMap.put(key, filterRestoLocation);
            }
        }
        Log.i("MappMarkerSpeed","setMarker - End");
        mClusterManager.cluster();


        if(cntRequestFromUI >1){
            cntRequestFromUI =0;
            onCameraIdle();
        }else if(cntRequestFromUI==1){
            cntRequestFromUI =0;
        }
    }

    @Override
    public void appendItems(List<IFlexible> restoList) {
        adapter.clear();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        list.setLayoutParams(params);
        adapter.addItems(adapter.getItemCount(), restoList);
        adapter.notifyDataSetChanged();
        animFlowBrandList(false);
    }

    @Override
    public void onCameraIdle() {
        Log.i("MappMarkerSpeed","onCameraIdle");

        //region NextStartOnlyAfterFinish
        cntRequestFromUI++;
        if(cntRequestFromUI >1){
            cntRequestFromUI++;
            return;
        }
        //endregion


        latLngBounds=googleMap.getProjection().getVisibleRegion().latLngBounds;
        Collection<FilterRestoLocation> oldRestoLocations=mClusterManager.getAlgorithm().getItems();
        for(FilterRestoLocation filterRestoLocation:oldRestoLocations){
            boolean contain=latLngBounds.contains(new LatLng(filterRestoLocation.getLocationLat(),filterRestoLocation.getLocationLon()));
            if(!contain) {
                String key = filterRestoLocation.getRestoId();
                mClusterManager.getAlgorithm().removeItem(hashMap.get(key ));
                hashMap.remove(key);
            }
        }

        presenter.cancelLoadRestoByLatLngBounds();
        mClusterManager.cluster();
//        if(mClusterManager.getAlgorithm().getItems().size()>0) return;;

        VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();
        LatLng farRight = visibleRegion.farRight;
        LatLng nearLeft = visibleRegion.nearLeft;
        String coordStart = String.format(Locale.getDefault(), "%.2f|%.2f", nearLeft.latitude-0.004, nearLeft.longitude-0.004);
        String coordEnd = String.format(Locale.getDefault(), "%.2f|%.2f", farRight.latitude+0.004, farRight.longitude+0.004);
        Log.i("onCameraMove", "coordStart - " + coordStart + "  coordEnd-" + coordEnd);
        geoPackage.setCoordStart(coordStart);
        geoPackage.setCoordEnd(coordEnd);
        presenter.loadRestoByLatLngBounds(geoPackage);


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        this.marker=marker;
        if(dialogShowView==null)
            Starter.RestoDetailActivity(getContext(),marker.getSnippet());
        else
            dialogShowView.show(getActivity().getFragmentManager(),"dialog");

        marker.hideInfoWindow();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        viewInfoWindow.setMarker(marker);
        return viewInfoWindow;
    }

    @Override
    public boolean onClusterItemClick(FilterRestoLocation filterRestoLocation) {
        viewInfoWindow = createItemInfoWindow(filterRestoLocation);
        return false;
    }

    @Override
    public boolean onClusterClick(Cluster<FilterRestoLocation> cluster) {
        if(googleMap.getCameraPosition().zoom==maxZoomPref){
            ScrollView scrollView=new ScrollView(getContext());
            LinearLayout linearLayout=new LinearLayout(getContext());
            LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(param);
            Collection<FilterRestoLocation> collection=cluster.getItems();
            Iterator<FilterRestoLocation> iterator=collection.iterator();
            while (iterator.hasNext())
                linearLayout.addView(createItemInfoWindow(iterator.next()));
            scrollView.addView(linearLayout);
            dialogShowView=new DialogShowView();
            dialogShowView.setView(scrollView);
            dialogShowView.setCallBack(result -> clearDialog());
            viewInfoWindow = createClusterInfoWindow(cluster);
            return false;
        }else {
            googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            cluster.getPosition(),
                            googleMap.getCameraPosition().zoom+1
                    ),
                    500,
                    null
            );
        }
        return true;
    }


    //endregion

    //region FUNCTIONS
    private void prepareQuery(String stringSearch) {
    searchPackage.setPage(0);
    searchPackage.setStringSearch(stringSearch);
    if (stringSearch.length() > 0) {
        showProgressBar();
        presenter.sendQueryRestoSearch(searchPackage);
    } else if (stringSearch.length() == 0) {
        //UITools.hideKeyboard(getActivity());
        adapter.clear();
        hideProgressBar();
        animFlowBrandList(true);

    }
}

    private void processAction(int action, Object payload) {
        UITools.hideKeyboard(getActivity());
        FilterRestoOnMap filterOnMapResto = (FilterRestoOnMap) payload;
        prepareQuery("");

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(
                new LatLng(Double.valueOf(filterOnMapResto.getLocationLat()), Double.valueOf(filterOnMapResto.getLocationLon())))
                , 2000, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        googleMap.animateCamera(CameraUpdateFactory.zoomBy(maxZoomPref),2000,null);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

//        Uri.Builder builder = new Uri.Builder();
//        builder.scheme("https")
//                .authority("www.google.com").appendPath("maps").appendPath("dir").appendPath("").appendQueryParameter("api", "1")
//                .appendQueryParameter("destination", filterOnMapResto.getLocationLat() + "," + filterOnMapResto.getLocationLon());
//        String url = builder.build().toString();
//        Log.d("Directions", url);
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse(url));
//        startActivity(i);
    }

    private void animFlowBrandList(boolean hide) {
        list.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void onLocationFound(Location location) {
        if(location==null)
            userLocation = MapUtils.getDefaultLocation(getContext());
        else
            userLocation = location;

        Bundle arguments = getArguments();
        RestoLocation restoLocation = (RestoLocation) arguments.getSerializable(Keys.LOCATION);
        if (restoLocation  != null) {
            Location restoLocation_1=new Location("gps");
            restoLocation_1.setLatitude(restoLocation.getLocation_lat());
            restoLocation_1.setLongitude(restoLocation.getLocation_lon());
            showLocation(restoLocation_1,0.001);
        }else {
            // 1 градус долготы примерно 111 км
            showLocation(userLocation,0.01);
        }

        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }else {
            googleMap.setMyLocationEnabled(true);
        }

    }

    private void showLocation(Location newLocation,double delta) {

        LatLngBounds AUSTRALIA = new LatLngBounds(
                new LatLng(
                        newLocation.getLatitude()-delta,
                        newLocation.getLongitude()
                ),
                new LatLng(
                        newLocation.getLatitude()+delta,
                        newLocation.getLongitude()
                )
        );

        googleMap.setOnCameraIdleListener(this);
        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 14)
        );
    }

    private InfoWindowMap createItemInfoWindow(FilterRestoLocation filterRestoLocation) {
        InfoWindowMap view= (InfoWindowMap) getLayoutInflater().inflate(R.layout.layout_info_window, null);
        TextView restoTitle = view.findViewById(R.id.title);
        restoTitle.setTypeface(null, Typeface.BOLD_ITALIC);
        restoTitle.setText(filterRestoLocation.getmName());
        view.setOnClickListener(v -> Starter.RestoDetailActivity(getContext(), filterRestoLocation.getRestoId()));
        if(userLocation!=null)
            view.countDistanceResto(filterRestoLocation.getRestoId(),userLocation.getLatitude(),userLocation.getLongitude());
        return view;
    }

    private InfoWindowMap createClusterInfoWindow(Cluster<FilterRestoLocation> cluster) {
        InfoWindowMap view= (InfoWindowMap) getLayoutInflater().inflate(R.layout.layout_info_window, null);
        TextView restoTitle = view.findViewById(R.id.title);
        restoTitle.setTypeface(null, Typeface.BOLD_ITALIC);
        restoTitle.setText(getString(R.string.select_resto,String.valueOf(cluster.getItems().size())));
        view.setOnClickListener(v -> clearDialog());
        if(userLocation!=null)
            if(cluster.getItems().size()>0)
            view.countDistanceCluster(cluster.getItems().iterator().next().getRestoId(),userLocation.getLatitude(),userLocation.getLongitude());
        return view;
    }

    private void clearDialog() {
        if(dialogShowView!=null)
            try {
                dialogShowView.dismiss();
            }catch (Exception e){};
        dialogShowView=null;
        if(marker!=null) {
            try {
                marker.hideInfoWindow();
            }catch (Exception e){};
        }
        marker=null;
    }


    //endregion

    public interface OnFragmentInteractionListener {
        void commonError(String... message);
        void setTitle(CharSequence name);
        OnLocationInteractionListener getLocationListener();
        void processChangeFragment(int id);
    }

}
