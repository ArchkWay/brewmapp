package com.brewmapp.presentation.view.impl.dialogs;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kras on 10.12.2017.
 */

public class DialogSelectAddress extends DialogFragment {
    private MapView mapView;
    private TextView textView;
    private Location location;
    private Button button;
    private OnSelectAddress onSelectAddress;
    private Address address;

    public DialogSelectAddress(){

    }

    public void showDialod(FragmentManager fragmentManager, OnSelectAddress onSelectAddress){
        this.onSelectAddress=onSelectAddress;
        show(fragmentManager,"qqq");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView=inflater.inflate(R.layout.dialog_select_address, null);
        textView= (TextView) rootView.findViewById(R.id.fragment_dialog_text);
        button= (Button) rootView.findViewById(R.id.fragment_dialog_button_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location.setCity_id(address.getLocality());
                location.getLocation().setStreet(address.getThoroughfare());
                location.getLocation().setHouse(address.getSubThoroughfare());
                onSelectAddress.onOk(location);
                dismiss();
            }
        });
        mapView= (MapView) rootView.findViewById(R.id.fragment_dialog_map);
        mapView.onCreate(null);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                GoogleMapOptions googleMapsOptions = new GoogleMapOptions();
                googleMapsOptions.zOrderOnTop( true );
                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                try {
                    Double lat=Double.valueOf(location.getMetro().getLat());
                    Double lon=Double.valueOf(location.getMetro().getLon());
                    if(lat!=0.0&&lon!=0.0)
                        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon) , 14.0f) );
                }   catch (Exception e){}
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Geocoder geocoder = new Geocoder(getActivity());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            address=addresses.get(0);
                            textView.setText(address.getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
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
