package com.brewmapp.presentation.view.impl.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.ChatReceiveMessage;
import com.brewmapp.execution.services.ChatService;

import com.brewmapp.presentation.view.contract.OnLocationInteractionListener;
import com.brewmapp.presentation.view.impl.fragment.Chat.ChatResultReceiver;

import butterknife.ButterKnife;
import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by ovcst on 01.05.2017.
 */

public abstract class BaseActivity extends PresenterActivity implements OnLocationInteractionListener, LocationListener {
    private LocationManager locationManager;
    private String provider;
    private Callback<Location> callbackLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //region Inject components
        PresenterComponent component = BeerMap.getAppComponent().plus(new PresenterModule(this));
        component.inject(this);
        inject(component);
        //endregion

        //region set CreateChatReseiver
        chatResultReceiver=new ChatResultReceiver(new Handler(getMainLooper()),new SimpleSubscriber<Bundle>(){
            @Override
            public void onNext(Bundle bundle) {
                super.onNext(bundle);
                String action=bundle.getString(ChatService.EXTRA_PARAM1);
                switch (action){
                    case ChatService.ACTION_RECEIVE_MESSAGE:
                        ChatReceiveMessage chatReceiveMessage = (ChatReceiveMessage) bundle.getSerializable(ChatService.EXTRA_PARAM2);
                        String text=chatReceiveMessage.getText()+getString(R.string.chat_new_message_text,chatReceiveMessage.getFrom().getFormattedName());
                        showSnackbar(text);
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
        //endregion


        if(this instanceof MainActivity){

        }
    }

    protected abstract void inject(PresenterComponent component);

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
        Toolbar toolbar = findActionBar();
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setElevation(0);
        }
    }

    protected Toolbar findActionBar() {
        return null;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerSnackbarReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterSnackbarReceiver();
        unregisterLocationManager();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestCodes.MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    registerLocationManager();
                } else {
                    callbackLocation.onResult(null);
                }
                return;
            }

        }
    }
    @Override
    public void getLocation(Callback<Location> callback) {
        this.callbackLocation = callback;
        if(checkLocationPermission())
            registerLocationManager();
    }

    @Override
    public void onLocationChanged(Location location) {
        unregisterLocationManager();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //******************************************
    private ChatResultReceiver chatResultReceiver;

    public void showSnackbar(String text) {
        View view=getWindow().getDecorView().findViewById(android.R.id.content);
        if(view!=null) {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(BaseActivity.this, R.color.mdtp_accent_color));
            snackbar.show();
        }
    }

    private void unRegisterSnackbarReceiver() {
        Intent intent=new Intent(ChatService.ACTION_CLEAR_RECEIVER,null,this, ChatService.class);
        startService(intent);
    }

    private void registerSnackbarReceiver() {
        Intent intent=new Intent(ChatService.ACTION_SET_RECEIVER,null,this, ChatService.class);
        intent.putExtra(ChatService.RECEIVER,chatResultReceiver);
        startService(intent);
    }

    private void registerLocationManager() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria=new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            provider = locationManager.getBestProvider(criteria, false);
            callbackLocation.onResult(locationManager.getLastKnownLocation(provider));
            long minTime = 10000;
            float minDistance=50.0f;
            locationManager.requestLocationUpdates(provider, minTime, minDistance, this);
        }
    }

    private void unregisterLocationManager(){
        if(this.locationManager!=null) {
            this.locationManager.removeUpdates(this);
            this.locationManager=null;
            this.callbackLocation=null;
        }
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        RequestCodes.MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        RequestCodes.MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

}
