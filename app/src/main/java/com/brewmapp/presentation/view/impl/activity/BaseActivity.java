package com.brewmapp.presentation.view.impl.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.ChatReceiveMessage;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.services.ChatService;

import com.brewmapp.execution.task.LoadCityTask;
import com.brewmapp.presentation.view.contract.OnLocationInteractionListener;
import com.brewmapp.presentation.view.contract.ReceiverAction;
import com.brewmapp.presentation.view.impl.fragment.Chat.ChatResultReceiver;
import com.brewmapp.presentation.view.impl.fragment.FriendsFragment;
import com.brewmapp.utils.events.markerCluster.MapUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by ovcst on 01.05.2017.
 */

public abstract class BaseActivity extends PresenterActivity implements OnLocationInteractionListener {

    private Callback<Boolean> callbackRequestPermissionLocation;
    private Callback<Boolean> callbackRequestPermissionWriteStorage;
    private ChatResultReceiver chatResultReceiver;
    private RelativeLayout containerProgressBar;
    private ResultReceiver resultReceiverVisibleParentActivity;

    @Inject
    public LoadCityTask loadCityTask;

    protected abstract void inject(PresenterComponent component);

    //region Impl BaseActivity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultReceiverVisibleParentActivity =getIntent().getParcelableExtra(getString(R.string.key_start_activity_invisible));

        setHideChildActivity();

        //region Inject components
        PresenterComponent component = BeerMap.getAppComponent().plus(new PresenterModule(this));
        component.inject(this);
        inject(component);
        //endregion

        //region set CreateChatReseiver
        chatResultReceiver = new ChatResultReceiver(new Handler(getMainLooper()), new SimpleSubscriber<Bundle>() {
            @Override
            public void onNext(Bundle bundle) {
                super.onNext(bundle);
                String action = bundle.getString(ChatService.EXTRA_PARAM1);
                switch (action) {
                    case ChatService.ACTION_RECEIVE_MESSAGE:
                        ChatReceiveMessage chatReceiveMessage = (ChatReceiveMessage) bundle.getSerializable(ChatService.EXTRA_PARAM2);
                        String text = chatReceiveMessage.getText() + getString(R.string.chat_new_message_text, chatReceiveMessage.getFrom().getFormattedName());
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
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
        Toolbar toolbar = findActionBar();
        if (toolbar != null) {
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
    protected void onDestroy() {
        super.onDestroy();
        stopProgressParentActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterSnackbarReceiver();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestCodes.MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callbackRequestPermissionLocation.onResult(true);
                } else {
                    callbackRequestPermissionLocation.onResult(false);
                }
                return;
            }
            case RequestCodes.MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callbackRequestPermissionWriteStorage.onResult(true);
                } else {
                    callbackRequestPermissionWriteStorage.onResult(false);
                }
                return;

        }
    }

    @Override
    protected void enableBackButton() {
        //super.enableBackButton();
        View textView= findViewById(R.id.action_bar_back);
        if(textView!=null) {
            textView.setVisibility(View.VISIBLE);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

    }
    //endregion

    //region Location

    @Override
    public void requestLastLocation(Callback<Location> callback) {
        requestPermissionLocation(new Callback<Boolean>() {
            @Override
            public void onResult(Boolean aBoolean) {
                if(aBoolean) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    List<String> providers = locationManager.getProviders(true);
                    for (int i = providers.size() - 1; i >= 0; i--) {
                        if (ActivityCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        } else {
                            Location l = locationManager.getLastKnownLocation(providers.get(i));
                            if (l != null) {
                                callback.onResult(l);
                                return;
                            }
                        }
                    }
                    callback.onResult(null);
                }else {
                    callback.onResult(null);
                    requestRefreshLocation();
                }
            }
        });
    }

    @Override
    public void requestRefreshLocation() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = locationManager.getProviders(true);
            for (int i = providers.size() - 1; i >= 0; i--) {
                if (ActivityCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    locationManager.requestLocationUpdates(providers.get(i), 0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            locationManager.removeUpdates(this);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            locationManager.removeUpdates(this);
                        }
                    });

                }
            }
        }catch (Exception e){}
    }

    public void requestCity(Callback<City> callbackCity){
        requestLastLocation(new Callback<Location>() {
            @Override
            public void onResult(Location location) {
                decodeLocation(location,callbackCity);
                if(location==null){
                    requestRefreshLocation();
                }
            }
        });
    }

    private void decodeLocation(Location location, Callback<City> callback) {
        if(location==null) {
            location = getDefaultLocation();
        }
        Locale ru= MapUtils.getLocaleRu();
        if(ru!=null) {
            Geocoder geocoder = new Geocoder(BaseActivity.this, ru);
            try {
                List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                loadCityTask.cancel();
                GeoPackage geoPackage = new GeoPackage();
                geoPackage.setCityName(list.get(0).getLocality());
                loadCityTask.execute(geoPackage, new SimpleSubscriber<List<City>>() {
                    @Override
                    public void onNext(List<City> cities) {
                        super.onNext(cities);
                        if(cities.size()==1) {
                            callback.onResult(cities.get(0));
                        }else {
                            Starter.InfoAboutCrashSendToServer("size of list not eq 1 (List<City>)", getClass().getCanonicalName());
                            callback.onResult(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Starter.InfoAboutCrashSendToServer(e.getMessage(), getClass().getCanonicalName());
                        callback.onResult(null);
                    }
                });
            } catch (IOException e) {
                Starter.InfoAboutCrashSendToServer(e.getMessage(), getClass().getCanonicalName());
                callback.onResult(null);
            }
        }
    }

    public void requestPermissionWriteStorage(Callback<Boolean> callbackRequestPermissionWriteStorage) {
        this.callbackRequestPermissionWriteStorage=callbackRequestPermissionWriteStorage;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_write_storage_permission)
                        .setMessage(R.string.text_write_storage_permission)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        RequestCodes.MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        RequestCodes.MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
        } else {
            this.callbackRequestPermissionWriteStorage.onResult(true);
        }

    }

    private void requestPermissionLocation(Callback<Boolean> callbackRequestPermitionLocation) {
        this.callbackRequestPermissionLocation = callbackRequestPermitionLocation;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
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
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        RequestCodes.MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            this.callbackRequestPermissionLocation.onResult(true);
        }
    }




    public Location getDefaultLocation(){
        Location location=new Location("gps");
        location.setLatitude(Float.valueOf(getString(R.string.default_Latitude)));
        location.setLongitude(Float.valueOf(getString(R.string.default_Longitude)));
        return location;
    }
    //endregion

    //region Chat Snackbar
    public void showSnackbar(String text) {
        showSnackbar(text, null);
    }

    public void showSnackbar(String text, Snackbar.Callback callback) {
        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(BaseActivity.this, R.color.mdtp_accent_color));
            if(callback!=null)
                snackbar.addCallback(callback);
            snackbar.show();
        }
    }

    public void showSnackbarRed(String text) {
        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
            View viewSnackbar=snackbar.getView();
            viewSnackbar.setBackgroundColor(ContextCompat.getColor(BaseActivity.this, android.R.color.holo_red_dark));
            TextView tv = (TextView) viewSnackbar.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTypeface(null, Typeface.BOLD_ITALIC);
            snackbar.show();
        }
    }

    private void unRegisterSnackbarReceiver() {
        Intent intent = new Intent(ChatService.ACTION_CLEAR_RECEIVER, null, this, ChatService.class);
        startService(intent);
    }

    private void registerSnackbarReceiver() {
        Intent intent = new Intent(ChatService.ACTION_SET_RECEIVER, null, this, ChatService.class);
        intent.putExtra(ChatService.RECEIVER, chatResultReceiver);
        startService(intent);
    }
    //endregion

    //region Call Phone
    protected void callPhone(String phoneNumber) {
        if (isPermissionCallPhone()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            }else {
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
            }
        }
    }

    private boolean isPermissionCallPhone() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_call_phone_permission)
                        .setMessage(R.string.text_call_phone_permission)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        RequestCodes.MY_PERMISSIONS_CALL_PHONE);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        RequestCodes.MY_PERMISSIONS_CALL_PHONE);
            }
            return false;
        } else {
            return true;
        }

    }
    //endregion

    //region Progress In ParentActivity and Hide ChildActivity while load data

    public ResultReceiver StartProgressBarInParentActivity(){

        //region Start Progress In Parent Activity
        if(containerProgressBar ==null) {
            containerProgressBar = (RelativeLayout) getLayoutInflater().inflate(R.layout.view_progress, null);
            addContentView(containerProgressBar,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        containerProgressBar.setVisibility(View.VISIBLE);
        //endregion

        return new ResultReceiver(new Handler(getMainLooper())){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                switch (resultCode){
                    case Actions.ACTION_STOP_PROGRESS_BAR_IN_PARENT_ACTIVITY:
                        //region Stop Progress In Parent Activity
                        if(containerProgressBar !=null)
                            containerProgressBar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(containerProgressBar!=null) {
                                        ViewGroup viewGroup= (ViewGroup) containerProgressBar.getParent();
                                        viewGroup.removeView(containerProgressBar);
                                        containerProgressBar=null;
                                    }
                                }
                            },500);
                        //endregion
                        break;
                    case Actions.ACTION_REFRESH:{
                        FriendsFragment friendsFragment= (FriendsFragment) getSupportFragmentManager().findFragmentByTag(FriendsFragment.class.getName());
                        if(friendsFragment!=null)
                                friendsFragment.onAction(Actions.ACTION_REFRESH, resultData);
                    }
                }

            }
        };
    }

    private void setHideChildActivity() {
        if(isModeActivityInVisible())
            moveTaskToBack(true);
    }

    public void setVisibleChildActivity(){
        if(isModeActivityInVisible()) {
            Intent intent = getIntent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            stopProgressParentActivity();
        }
    }

    public void stopProgressParentActivity(){
        sendActionParentActivity(Actions.ACTION_STOP_PROGRESS_BAR_IN_PARENT_ACTIVITY);
    }

    public void StartProgressBarInActivity(){
        resultReceiverVisibleParentActivity=StartProgressBarInParentActivity();
    }

    public void sendActionParentActivity(int action){
        if(isModeActivityInVisible()) {
            resultReceiverVisibleParentActivity.send(action, null);
        }
    }

    private boolean isModeActivityInVisible(){
        return resultReceiverVisibleParentActivity !=null;
    }

    //endregion

    //region Common Error
    public void commonError(String... strings) {
        String message;
        message=strings.length==0?getString(R.string.error):strings[1];
        showMessage(message);
        Starter.InfoAboutCrashSendToServer(message,getClass().getName());
        stopProgressParentActivity();
        finish();
    }
    //endregion

}
