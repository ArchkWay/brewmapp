package com.brewmapp.presentation.view.impl.dialogs;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.Location;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by Kras on 10.12.2017.
 */

public class DialogSelectAddress extends DialogFragment {
    private MapView mapView;
    private TextView textView;
    private Location location;
    private Button buttonOk;
    private Button buttonCancel;
    private OnSelectAddress onSelectAddress;
    private Address address;
    private LatLng latLng;

    public DialogSelectAddress() {

    }

    public void showDialog(FragmentManager fragmentManager, OnSelectAddress onSelectAddress) {
        this.onSelectAddress = onSelectAddress;
        show(fragmentManager, "qqq");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialog_select_address, null);
        textView = (TextView) rootView.findViewById(R.id.fragment_dialog_text);
        buttonOk = (Button) rootView.findViewById(R.id.fragment_dialog_button_ok);
        buttonOk.setOnClickListener(view -> {
            if (address != null) {
                location.setCity_id(address.getLocality());
                location.getLocation().setStreet(address.getThoroughfare());
                location.getLocation().setHouse(address.getSubThoroughfare());
                if (latLng != null) {
                    location.getLocation().setLat(latLng.latitude);
                    location.getLocation().setLon(latLng.longitude);
                }
                onSelectAddress.onOk(location);
            }
            dismiss();
        });
        buttonCancel = (Button) rootView.findViewById(R.id.fragment_dialog_button_cancel);
        buttonCancel.setOnClickListener(v -> dismiss());
        mapView = (MapView) rootView.findViewById(R.id.fragment_dialog_map);
        mapView.onCreate(null);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                GoogleMapOptions googleMapsOptions = new GoogleMapOptions();
                googleMapsOptions.zOrderOnTop(true);
                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng _latLng) {
                        latLng = _latLng;
                        Geocoder geocoder = new Geocoder(getActivity());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(_latLng.latitude, _latLng.longitude, 1);
                            address = addresses.get(0);
                            if (address.getMaxAddressLineIndex() > 0)
                                textView.setText(String.format("%s, %s", address.getAddressLine(1), address.getAddressLine(0)));
                            else
                                textView.setText(address.getAddressLine(0));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                BaseActivity baseActivity = (BaseActivity) getActivity();
                baseActivity.requestLastLocation(result -> {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        dismiss();
                        return;
                    }else {
                        googleMap.setMyLocationEnabled(true);
                    }
                });

                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        try {
                            Double lat = Double.valueOf(location.getLocation().getLat());
                            Double lon = Double.valueOf(location.getLocation().getLon());
                            if (lat != 0.0 && lon != 0.0)
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14.0f));
                        } catch (Exception e) {
                        }
                    }
                });

            }
        });

        return rootView;
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


    public DialogSelectAddress setLocation(Location location) {
        this.location = location;
        return this;
    }

    public interface OnSelectAddress{
        void onOk(Location location);
    }

}
