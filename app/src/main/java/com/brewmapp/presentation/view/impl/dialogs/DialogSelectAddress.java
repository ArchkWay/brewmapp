package com.brewmapp.presentation.view.impl.dialogs;

import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.brewmapp.R;
import com.google.android.gms.maps.GoogleMap;
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
    public DialogSelectAddress(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.dialog_select_address, null);
        mapView= (MapView) rootView.findViewById(R.id.fragment_dialog_map);
        mapView.onCreate(null);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        List<Address> addresses;
                        Geocoder geocoder = new Geocoder(getActivity());
                        try {
                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10);
                            if(addresses.size()>0) {
                                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
                                Iterator<Address> iterator=addresses.iterator();
                                while (iterator.hasNext()) {
                                    Address address=iterator.next();
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(address.getAddressLine(0));
                                    arrayAdapter.add(sb.toString());
                                }
                                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builderSingle.show();
                            }

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
}
