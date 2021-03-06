package com.brewmapp.presentation.view.impl.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.entity.FilterRestoOnMap;
import com.brewmapp.data.entity.MenuField;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.data.entity.wrapper.FilterRestoLocationInfo;
import com.brewmapp.data.entity.wrapper.SearchRestoInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.MapFragment_presenter;
import com.brewmapp.presentation.view.contract.MapFragment_view;
import com.brewmapp.presentation.view.contract.OnLocationInteractionListener;
import com.brewmapp.presentation.view.impl.activity.FilterMapActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogConfirm;
import com.brewmapp.presentation.view.impl.widget.FinderView;
import com.brewmapp.presentation.view.impl.widget.InfoWindowContainer;
import com.brewmapp.presentation.view.impl.widget.InfoWindowMapContentList;
import com.brewmapp.presentation.view.impl.widget.InfoWindowMapContent;
import com.brewmapp.utils.events.markerCluster.ClusterRender;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
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

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;
import ru.frosteye.ovsa.tool.UITools;

import static android.view.View.VISIBLE;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_MAP_RESULT;

/**
 * Created by ovcst on 24.08.2017.
 */
public class MapFragment extends BaseFragment implements
                                                            MapFragment_view,
                                                            OnMapReadyCallback,
                                                            GoogleMap.OnCameraIdleListener,
                                                            GoogleMap.OnInfoWindowClickListener,
                                                            GoogleMap.InfoWindowAdapter,
                                                            ClusterManager.OnClusterClickListener<FilterRestoLocation>,
                                                            ClusterManager.OnClusterItemClickListener<FilterRestoLocation>,
                                                            GoogleMap.OnMyLocationButtonClickListener,
                                                            GoogleMap.OnMapClickListener,
                                                            GoogleMap.OnMapLoadedCallback,
                                                            View.OnKeyListener,
                                                            View.OnFocusChangeListener,
                                                            View.OnClickListener


{

    //region BIND
    @BindView(R.id.fragment_map_map)
    MapView mapView;
    @BindView(R.id.activity_search_search)
    FinderView finder;
    @BindView(R.id.restoList)
    RecyclerView list;
    @BindView(R.id.fragment_map_fab_location_off)
    FloatingActionButton fab_location_off;
    //endregion

    //region INJECT
    @Inject
    MapFragment_presenter presenter;
    //endregion

    //region Privates
    private OnFragmentInteractionListener mListener;
    private OnLocationInteractionListener mLocationListener;
    private GoogleMap googleMap;
    private ProgressDialog dialog;
    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage searchPackage;
    private EndlessRecyclerOnScrollListener scrollListener;
    private ClusterManager<FilterRestoLocation> mClusterManager;
    private GeoPackage geoPackage=new GeoPackage();
    private HashMap<String,FilterRestoLocation> hmVisibleResto =new HashMap<>();
    private HashMap<String,String> hmResultSearch =new HashMap<>();
    private LatLngBounds latLngBounds;
    private float maxZoomPref=18.0f;
    private float minZoomPref=10.0f;
    private Location userLocation=null;
    private ArrayList arrayList=new ArrayList<>();
    private ArrayList<RestoLocation> restoLocations;
    private HashMap<String,ArrayList<String>> hmBeersInResto =new HashMap<>();
    private final int MODE_SHOW_UNKNOW=0;
    private final int MODE_SHOW_RESTO_BY_USER_LOCATION =1;
    private final int MODE_SHOW_RESTO_BY_LIST_RESTO =3;
    private int mode;



    //координаты отслеживаемой при прокрутке точки на карте
    private LatLng trackedPosition;
    //смещения всплывающего окна, позволяющее скорректировать его положение относительно маркера
    private int popupXOffset;
    private int popupYOffset;
    //высота маркера
    private int markerHeight;
    //слушатель, который будет обновлять смещения при изменении размеров окна
    private ViewTreeObserver.OnGlobalLayoutListener infoWindowLayoutListener;
    //контейнер всплывающего окна
    @BindView(R.id.container_popup)
    InfoWindowContainer infoWindowContainer;
    //интервал обновления положения всплывающего окна.
    //для плавности необходимо 60 fps, то есть 1000 ms / 60 = 16 ms между обновлениями.
    private final int POPUP_POSITION_REFRESH_INTERVAL = 16;
    //Handler, запускающий обновление окна с заданным интервалом
    private Handler handler;
    //Runnable, который обновляет положение окна
    private PositionUpdaterRunnable positionUpdaterRunnable;

    private PrepareLoadResto prepareLoadResto=null;
    //endregion

    //region Impl MapFragment
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

        list.addItemDecoration(new ListDivider(getContext(), ListDivider.VERTICAL_LIST));
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        scrollListener = new EndlessRecyclerOnScrollListener((LinearLayoutManager) list.getLayoutManager()) {
            @Override
            public void onLoadMore(int currentPage) {
                searchPackage.setPage(currentPage - 1);
                startQuery();
            }
        };

        adapter = new FlexibleModelAdapter<>(arrayList, this::processAction);
        list.setAdapter(adapter);
        finder.setListener(string -> {
            adapter.setSearchText(string);
            searchPackage.setStringSearch(string);
            prepareQuery();
            if(string.length()==0)
                finder.clearFocus();
        });
        finder.clearFocus();
        finder.getInput().setOnFocusChangeListener(this);
        finder.getInput().setOnKeyListener(this);

        fab_location_off.setOnClickListener(this);
        fab_location_off.setVisibility(View.GONE);

        mapView.onCreate(null);
        mapView.getMapAsync(this);

        infoWindowLayoutListener = new InfoWindowLayoutListener();
        infoWindowContainer.getViewTreeObserver().addOnGlobalLayoutListener(infoWindowLayoutListener );
        infoWindowContainer.setInfoWindowLayoutListener(infoWindowLayoutListener);
                handler = new Handler(Looper.getMainLooper());
        positionUpdaterRunnable = new PositionUpdaterRunnable();

        //запускаем периодическое обновление
        handler.post(positionUpdaterRunnable);
        clearInfoWindow();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        getView().setOnKeyListener(null);
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
        infoWindowContainer.getViewTreeObserver().removeGlobalOnLayoutListener(infoWindowLayoutListener );
        handler.removeCallbacks(positionUpdaterRunnable);
        handler = null;
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

    //region Impl MapFragment_view
    @Override
    public void appendItems(List<IFlexible> iFlexibles) {

        ArrayList<IFlexible> arrayListfound=new ArrayList<>();
        Iterator<IFlexible> iterator=iFlexibles.iterator();
        while (iterator.hasNext()) {
            SearchRestoInfo searchRestoInfo = (SearchRestoInfo) iterator.next();
            Resto resto= searchRestoInfo.getModel();
            arrayListfound.add(new FilterRestoLocationInfo(new FilterRestoOnMap(resto)));
        }

        int oldSize=arrayList.size();
        arrayList.addAll(arrayListfound);
        adapter.notifyItemRangeChanged(oldSize,arrayList.size());
    }

    @Override
    public void commonError(String message) {
        mListener.commonError(message);
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
        mListener.showTopBarLoading(true);
//        finder.findViewById(R.id.progressBar).setVisibility(VISIBLE);
//        finder.findViewById(R.id.finder_cancel).setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        mListener.showTopBarLoading(false);
//        finder.findViewById(R.id.progressBar).setVisibility(View.GONE);
//        finder.findViewById(R.id.finder_cancel).setVisibility(VISIBLE);
    }

    @Override
    public void addRestoToMap(List<FilterRestoLocationInfo> filterRestoLocationInfos) {


        //region changeVisibleResto
        Iterator<FilterRestoLocationInfo> infoIterator=filterRestoLocationInfos.iterator();
        while (infoIterator.hasNext()){
            FilterRestoOnMap filterRestoOnMap=infoIterator.next().getModel();
            String key = filterRestoOnMap.getRestoId();
            if (!hmVisibleResto.containsKey(key)) {
                FilterRestoLocation filterRestoLocation=new FilterRestoLocation(filterRestoOnMap);
                switch (mode){
                    case MODE_SHOW_RESTO_BY_USER_LOCATION: {
                        mClusterManager.addItem(filterRestoLocation);
                        hmVisibleResto.put(key, filterRestoLocation);
                    }break;
                    case MODE_SHOW_RESTO_BY_LIST_RESTO: {
                        if(hmResultSearch.containsKey(key)) {
                            mClusterManager.addItem(filterRestoLocation);
                            hmVisibleResto.put(key, filterRestoLocation);
                        }
                    }break;
                }
            }
        }
        hideProgressBar();
        //endregion


        mClusterManager.cluster();

    }

    //endregion

    //region Events GoogleMap
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMinZoomPreference(minZoomPref);
        this.googleMap.setMaxZoomPreference(maxZoomPref);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mClusterManager = new ClusterManager<>(getContext(), googleMap);
        mClusterManager.setRenderer(new ClusterRender(getContext(), googleMap, mClusterManager));
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        this.googleMap.setOnMarkerClickListener(mClusterManager);
        this.googleMap.setInfoWindowAdapter(this);
        this.googleMap.setOnInfoWindowClickListener(this);
        this.googleMap.setOnMyLocationButtonClickListener(this);
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.setOnCameraIdleListener(this);
        this.googleMap.setOnMapLoadedCallback(this);
    }

    @Override
    public void onCameraIdle() {

        infoWindowContainer.processVisibleAnimation();

        if(prepareLoadResto==null) {
            prepareLoadResto=new PrepareLoadResto();
            prepareLoadResto.execute();
        }else {
            mClusterManager.cluster();
        }


    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;

    }

    @Override
    public View getInfoContents(Marker marker) {

        Projection projection = googleMap.getProjection();
        trackedPosition = marker.getPosition();
        Point trackedPoint = projection.toScreenLocation(trackedPosition);
        trackedPoint.y -= popupYOffset / 2;

        infoWindowContainer.setMarker(marker);

        return null;
    }

    @Override
    public void onMapLoaded() {
        mode =MODE_SHOW_UNKNOW;
        mLocationListener.requestLastLocation(location -> {
            userLocation=(location==null?mLocationListener.getDefaultLocation():location);
            parseArgumentsAndSetMode();
            showNewLocation();
            if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                fab_location_off.setVisibility(VISIBLE);
            }else {
                this.googleMap.setMyLocationEnabled(true);
                fab_location_off.setVisibility(View.GONE);
            }

        });

    }


    //endregion

    //region Events User
    @Override
    public boolean onMyLocationButtonClick() {
        finder.clearFocus();
        clearInfoWindow();
        mLocationListener.requestLastLocation(location -> {
            userLocation=(location==null?mLocationListener.getDefaultLocation():location);
            showNewLocation();
        });
        mLocationListener.requestRefreshLocation();
        return true;
    }

    @Override
    public boolean onClusterClick(Cluster<FilterRestoLocation> cluster) {

        finder.clearFocus();
        clearInfoWindow();
        if(googleMap.getCameraPosition().zoom==maxZoomPref){
            InfoWindowMapContentList infoWindowMapList= (InfoWindowMapContentList) getActivity().getLayoutInflater().inflate(R.layout.layout_info_window_list,null);
            infoWindowMapList.setContent(cluster,this);
            infoWindowContainer.setInfoWindowMapContent_view(infoWindowMapList);
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

    @Override
    public boolean onClusterItemClick(FilterRestoLocation filterRestoLocation) {
        finder.clearFocus();
        clearInfoWindow();
        infoWindowContainer.setInfoWindowMapContent_view(
                createInfoWindowForOneResto(filterRestoLocation)
        );

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }

    @Override
    public void onMapClick(LatLng latLng) {
        finder.clearFocus();
        clearInfoWindow();

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
            if(finder.getInput().isFocusable()) {
                finder.clearFocus();
                return true;
            }
        }else if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER){
            finder.hideInputKeyboard();
            return true;
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.view_finder_input:
                    hideList(!hasFocus);
                    clearInfoWindow();
                break;
        }
    }


    @Override
    public void onClick(View v) {
        onMyLocationButtonClick();
        mListener.showSnackbarRed(getString(R.string.location_disabled));
    }

    //endregion

    //region Functions

    private void prepareQuery() {
        scrollListener.reset();
        searchPackage.setPage(0);
        if (searchPackage.getStringSearch().length() > 1) {
            arrayList.clear();
            startQuery();
        }else if (searchPackage.getStringSearch().length() == 0) {
            resetFinder();
        }
    }

    private void resetFinder() {
        arrayList.clear();
        adapter.notifyDataSetChanged();
        hideProgressBar();
    }

    private void startQuery() {
        showProgressBar();
        presenter.sendQueryRestoSearch(searchPackage);

    }

    private void processAction(int action, Object payload) {
        UITools.hideKeyboard(getActivity());
        FilterRestoOnMap filterOnMapResto = (FilterRestoOnMap) payload;
        finder.clearFocus();
        
        if(mode==MODE_SHOW_RESTO_BY_LIST_RESTO){
            if(!hmResultSearch.containsKey(filterOnMapResto.getRestoId()))
                hmResultSearch.put(filterOnMapResto.getRestoId(),filterOnMapResto.getRestoId());
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(
                new LatLng(Double.valueOf(filterOnMapResto.getLocationLat()), Double.valueOf(filterOnMapResto.getLocationLon())))
                , 500, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        googleMap.animateCamera(CameraUpdateFactory.zoomBy(maxZoomPref),500,null);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

    }

    private void hideList(boolean hide) {
        list.setVisibility(hide ? View.GONE : VISIBLE);
    }

    private void showNewLocation() {
        hmResultSearch.clear();

        //region Prepare LatLngBounds AUSTRALIA
        LatLngBounds AUSTRALIA=null;
        switch (mode){
            case MODE_SHOW_RESTO_BY_USER_LOCATION: {
                Location newLocation = userLocation;
                double delta=0.01;
                AUSTRALIA = new LatLngBounds(
                        new LatLng(
                                newLocation.getLatitude()-delta,
                                newLocation.getLongitude()
                        ),
                        new LatLng(
                                newLocation.getLatitude()+delta,
                                newLocation.getLongitude()
                        )
                );
            }break;
            case MODE_SHOW_RESTO_BY_LIST_RESTO:{
                double delta=0;
                if(restoLocations.size()==1)
                    delta=0.001;

                for (RestoLocation restoLocation:restoLocations){
                    hmResultSearch.put(restoLocation.getResto_id(),restoLocation.getResto_id());
                    if(AUSTRALIA==null){
                        AUSTRALIA=new LatLngBounds(
                                new LatLng(
                                        restoLocation.getLocation_lat()-delta,
                                        restoLocation.getLocation_lon()-delta
                                ),
                                new LatLng(
                                        restoLocation.getLocation_lat()+delta,
                                        restoLocation.getLocation_lon()+delta
                                )
                        );
                    }else {
                        if(
                                Math.abs(AUSTRALIA.getCenter().latitude-restoLocation.getLocation_lat())<0.05
                                &&
                                Math.abs(AUSTRALIA.getCenter().longitude-restoLocation.getLocation_lon())<0.05
                        )
                        AUSTRALIA=AUSTRALIA.including(
                                new LatLng(
                                        restoLocation.getLocation_lat(),
                                        restoLocation.getLocation_lon()
                                )
                        );
                    }
                }
            }
        }

        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 14)
        );
    }

    public InfoWindowMapContent createInfoWindowForOneResto(FilterRestoLocation filterRestoLocation) {
        LayoutInflater inflater= getActivity().getLayoutInflater();
        InfoWindowMapContent infoWindowMap= (InfoWindowMapContent)inflater.inflate(R.layout.layout_info_window,null);
        infoWindowMap.setContent(filterRestoLocation,hmBeersInResto,userLocation,inflater);
        return infoWindowMap;
    }


    private void parseArgumentsAndSetMode() {

        restoLocations=(ArrayList<RestoLocation>) getArguments().getSerializable(Keys.LOCATION);

        if(restoLocations==null||restoLocations.size()==0){
            mode = MODE_SHOW_RESTO_BY_USER_LOCATION;
        }else  {
            mode = MODE_SHOW_RESTO_BY_LIST_RESTO;
        }


        hmBeersInResto.clear();
        switch (mode){
            case MODE_SHOW_RESTO_BY_LIST_RESTO:{
                for (RestoLocation restoLocation:restoLocations)
                    hmBeersInResto.put(restoLocation.getResto_id(),new ArrayList<>(restoLocation.getBeersId().values()));
            }break;
        }
    }

    private void clearInfoWindow() {
        infoWindowContainer.clearContainer();
    }

    private void requestResto() {

        VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();
        LatLng farRight = visibleRegion.farRight;
        LatLng nearLeft = visibleRegion.nearLeft;
        String coordStart = String.format(MapUtils.getLocaleEn(), "%.2f|%.2f", nearLeft.latitude-0.004 , nearLeft.longitude-0.004 );
        String coordEnd = String.format(MapUtils.getLocaleEn(), "%.2f|%.2f", farRight.latitude+0.004 , farRight.longitude+0.004);
        geoPackage.setCoordStart(coordStart);
        geoPackage.setCoordEnd(coordEnd);
        presenter.loadRestoByLatLngBounds(geoPackage);
    }

    //endregion

    public interface OnFragmentInteractionListener {
        void commonError(String... message);
        void setTitle(CharSequence name);
        OnLocationInteractionListener getLocationListener();
        void processChangeFragment(int id);
        void showTopBarLoading(boolean b);
        void showSnackbarRed(String text);
    }

    private class InfoWindowLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            //размеры окна изменились, обновляем смещения
            popupXOffset = infoWindowContainer.getWidth() / 2;
            popupYOffset = infoWindowContainer.getHeight()/2;
        }
    }

    public class PositionUpdaterRunnable implements Runnable {
        public int lastXPosition = Integer.MIN_VALUE;
        public int lastYPosition = Integer.MIN_VALUE;

        private int lastPopupXOffset = 0;

        @Override
        public void run() {
            //помещаем в очередь следующий цикл обновления
            handler.postDelayed(this, POPUP_POSITION_REFRESH_INTERVAL);

            //если всплывающее окно скрыто, ничего не делаем
            if (trackedPosition != null && infoWindowContainer.getVisibility() == VISIBLE ) {
                Point targetPosition = googleMap.getProjection().toScreenLocation(trackedPosition);

                //если положение окна не изменилось, ничего не делаем
                if (lastXPosition != targetPosition.x || lastYPosition != targetPosition.y || lastPopupXOffset!=popupXOffset) {
                    //обновляем положение
                    AbsoluteLayout.LayoutParams overlayLayoutParams = (AbsoluteLayout.LayoutParams) infoWindowContainer.getLayoutParams();
                    overlayLayoutParams.x = targetPosition.x - popupXOffset;
                    overlayLayoutParams.y = targetPosition.y - popupYOffset;// - markerHeight;
                    infoWindowContainer.setLayoutParams(overlayLayoutParams);

                    //запоминаем текущие координаты
                    lastXPosition = targetPosition.x;
                    lastYPosition = targetPosition.y;
                    lastPopupXOffset=popupXOffset;
                }
            }
        }
    }

    class PrepareLoadResto extends android.os.AsyncTask<Object,Object,Object>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            latLngBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            //region RemoveInvisibleResto
            Collection<FilterRestoLocation> oldRestoLocations = mClusterManager.getAlgorithm().getItems();
            for (FilterRestoLocation filterRestoLocation : oldRestoLocations) {
                boolean contain = latLngBounds.contains(new LatLng(filterRestoLocation.getLocationLat(), filterRestoLocation.getLocationLon()));
                if (!contain) {
                    String key = filterRestoLocation.getRestoId();
                    mClusterManager.removeItem(hmVisibleResto.get(key));
                    hmVisibleResto.remove(key);
                }
            }
            //endregion
            return null;
        }

        @Override
        protected void onPostExecute(Object objects) {
            super.onPostExecute(objects);
            mClusterManager.cluster();
            if(mClusterManager.getAlgorithm().getItems().size()<1500)
                requestResto();
            else
                new DialogConfirm(getString(R.string.text_too_many_resto), getFragmentManager(), new DialogConfirm.OnConfirm() {
                    @Override
                    public void onOk() {
                        requestResto();
                    }

                    @Override
                    public void onCancel() {
                        googleMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        googleMap.getProjection().getVisibleRegion().latLngBounds.getCenter(),
                                        googleMap.getCameraPosition().zoom+1
                                ),
                                500,
                                null
                        );
                    }
                });
            prepareLoadResto=null;
        }

    }
}
