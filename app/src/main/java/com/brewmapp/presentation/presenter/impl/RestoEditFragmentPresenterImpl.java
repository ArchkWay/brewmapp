package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.brewmapp.R;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.Location;
import com.brewmapp.data.entity.LocationChild;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.LoadCityTask;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import com.brewmapp.presentation.presenter.contract.RestoEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.RestoEditFragmentView;
import com.brewmapp.presentation.view.impl.widget.AddPhotoSliderView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 10.12.2017.
 */

public class RestoEditFragmentPresenterImpl extends BasePresenter<RestoEditFragmentView> implements RestoEditFragmentPresenter {

    private LoadRestoDetailTask loadRestoDetailTask;
    private LoadRestoDetailPackage loadRestoDetailPackage=new LoadRestoDetailPackage();
    private String[] checkListRestoDetails={"getFormattedKitchen"};
    private String[] checkListResto={"getAvgCost","getName"};
    private String[] checkListLocation={"getFormatLocation"};
    private RestoDetail restoDetail_old_data=new RestoDetail();
    private RestoDetail restoDetail_new_data=new RestoDetail();
    private Context context;
    private UserRepo userRepo;
    private LoadCityTask loadCityTask;


    @Inject
    public RestoEditFragmentPresenterImpl(LoadRestoDetailTask loadRestoDetailTask,Context context,UserRepo userRepo,LoadCityTask loadCityTask){
        this.loadRestoDetailTask=loadRestoDetailTask;
        this.context=context;
        this.userRepo=userRepo;
        this.loadCityTask=loadCityTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void requestContent(String id_resto) {
        loadRestoDetailPackage.setId(id_resto);
        loadRestoDetailTask.execute(loadRestoDetailPackage,new SimpleSubscriber<RestoDetail>(){
            @Override
            public void onNext(RestoDetail restoDetail) {
                super.onNext(restoDetail);
                restoDetail_old_data=restoDetail;
                restoDetail_new_data=restoDetail.clone();
                view.setContent(restoDetail_old_data);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });

    }

    @Override
    public void loadAllPhoto(SliderLayout sliderLayout) {
        class LoadPhotoResto extends BaseNetworkTask<String, List<Photo>> {

            public LoadPhotoResto(MainThread mainThread, Executor executor, Api api) {
                super(mainThread, executor, api);
            }

            @Override
            protected Observable<List<Photo>> prepareObservable(String resto_id) {
                return Observable.create(subscriber -> {
                    try {
                        WrapperParams wrapperParams=new WrapperParams(Wrappers.PHOTO);
                        wrapperParams.addParam(Keys.RELATED_MODEL,Keys.CAP_RESTO);
                        wrapperParams.addParam(Keys.RELATED_ID,resto_id);
                        ListResponse<Photo> listResponse= executeCall(getApi().loadPhotosResto(wrapperParams));
                        subscriber.onNext(listResponse.getModels());
                        subscriber.onComplete();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        }
        new LoadPhotoResto(
                BeerMap.getAppComponent().mainThread(),
                BeerMap.getAppComponent().executor(),
                BeerMap.getAppComponent().api()
        ).execute(
                String.valueOf(restoDetail_old_data.getResto().getId()),
                new SimpleSubscriber<List<Photo>>(){
                    @Override
                    public void onNext(List<Photo> photos) {
                        super.onNext(photos);
                        Iterator<Photo> iterator=photos.iterator();
                        while (iterator.hasNext())
                            try {
                                sliderLayout.addSlider(
                                        new DefaultSliderView(sliderLayout.getContext())
                                                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                                .image(iterator.next().getThumb().getUrl())
                                                .setOnSliderClickListener(slider1 -> {showMessage(sliderLayout.getContext().getString(R.string.message_develop));})
                                );
                            }catch (Exception e){};

                        sliderLayout.addSlider(new AddPhotoSliderView(sliderLayout.getContext(), v -> {showMessage(sliderLayout.getContext().getString(R.string.message_develop));}));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        sliderLayout.addSlider(new AddPhotoSliderView(sliderLayout.getContext(), v -> {showMessage(sliderLayout.getContext().getString(R.string.message_develop));}));
                    }
                }
        );
    }

    @Override
    public boolean isNeedSave() {
        try {
            for (String s:checkListResto)
                if(isNeedSaveField(s,restoDetail_old_data.getResto(),restoDetail_new_data.getResto()))
                    return true;
            for (String s:checkListRestoDetails)
                if(isNeedSaveField(s,restoDetail_old_data,restoDetail_new_data))
                    return true;
            for (String s:checkListLocation)
                if(isNeedSaveField(s,restoDetail_old_data.getResto().getLocation(),restoDetail_new_data.getResto().getLocation()))
                    return true;

        }catch (Exception e){}


        return false;
    }

    @Override
    public void storeChanges() {

        new prepParam(new SimpleSubscriber(){
            @Override
            public void onNext(Object o) {
                super.onNext(o);
                new EditRestoTask(
                        BeerMap.getAppComponent().mainThread(),
                        BeerMap.getAppComponent().executor(),
                        BeerMap.getAppComponent().api()
                ).execute(null,new SimpleSubscriber<RestoDetail>(){
                    @Override
                    public void onNext(RestoDetail restoDetail) {
                        super.onNext(restoDetail);
                        view.onDataSent();
                        restoDetail_old_data=restoDetail_new_data.clone();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(context.getString(R.string.error_send_data));
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(context.getString(R.string.error_prepare_data));
            }
        });

    }

    @Override
    public RestoDetail getRestoDetail_new_data() {
        return restoDetail_new_data;
    }
    //***********************************
    private boolean isNeedSaveField(String getMethod_name, Object oldObject, Object newObject) {
        try {
            Class c=oldObject.getClass();
            String type=c.getMethod(getMethod_name).getGenericReturnType().toString();
            if(type.contains("String")){
                Method m_get = c.getMethod(getMethod_name);
                //Method m_set = c.getMethod(new StringBuilder().append(getMethod_name).replace(0, 1, "s").toString(), String.class);

                String val_old = (String) m_get.invoke(oldObject);
                String val_new = (String) m_get.invoke(newObject);

                if (!TextUtils.isEmpty(val_new) && !val_new.equals(val_old))
                    return true;

            }else if(type.contains("int")){
                Method m_get = c.getMethod(getMethod_name);

                int val_old = (int) m_get.invoke(oldObject);
                int val_new = (int ) m_get.invoke(newObject);

                return val_old!=val_new;

            }else if(type.contains("Date")){
                Method m_get = c.getMethod(getMethod_name);
                Date val_old = (Date) m_get.invoke(oldObject);
                Date val_new = (Date) m_get.invoke(newObject);

                return val_old!=val_new;
            }
        } catch(NoSuchMethodException e){
            e.printStackTrace();
        } catch(IllegalAccessException e){
            e.printStackTrace();
        } catch(InvocationTargetException e){
            e.printStackTrace();
        }


        return false;
    }

    class EditRestoTask  extends BaseNetworkTask<Void, RestoDetail> {


        public EditRestoTask(MainThread mainThread, Executor executor, Api api) {
            super(mainThread, executor, api);
        }

        @Override
        protected Observable<RestoDetail> prepareObservable(Void aVoid) {
            return Observable.create(subscriber -> {
                try {
                    WrapperParams wrapperParams = new WrapperParams(Wrappers.RESTO);
                    wrapperParams.addParam(Keys.ID,restoDetail_new_data.getResto().getId());
                    wrapperParams.addParam(Keys.RESTO_TYPE,restoDetail_new_data.getResto_type_RestFormat());
                    if(isNeedSaveField("getName",restoDetail_old_data.getResto(),restoDetail_new_data.getResto()))
                        wrapperParams.addParam(Keys.NAME, restoDetail_new_data.getResto().getName());
                    if(isNeedSaveField("getAvgCost",restoDetail_old_data.getResto(),restoDetail_new_data.getResto()))
                        wrapperParams.addParam(Keys.AVG_COST, restoDetail_new_data.getResto().getAvgCost());
                    if(isNeedSaveField("getFormatLocation",restoDetail_old_data.getResto().getLocation(),restoDetail_new_data.getResto().getLocation()))
                        wrapperParams.addParam(Keys.LOCATION_ID, restoDetail_new_data.getResto().getLocation().getLocation().getId());
                    SingleResponse<RestoDetail> response=executeCall(getApi().editResto(wrapperParams));
                    subscriber.onNext(response.getData());
                    subscriber.onComplete();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            });
        }

    }
    class CreateLocation extends BaseNetworkTask<LocationChild, String>{

        public CreateLocation(MainThread mainThread, Executor executor, Api api) {
            super(mainThread, executor, api);
        }

        @Override
        protected Observable<String> prepareObservable(LocationChild locationChild) {
            return Observable.create(subscriber -> {
                try {
                    WrapperParams wrapperParams = new WrapperParams(Wrappers.LOCATION);
                    wrapperParams.addParam(Keys.COUNTRY_ID,locationChild.getCountry_id());
                    wrapperParams.addParam(Keys.CITY_ID,locationChild.getCity_id());
                    wrapperParams.addParam(Keys.STREED,locationChild.getStreet());
                    wrapperParams.addParam(Keys.HOUSE,locationChild.getHouse());
                    wrapperParams.addParam(Keys.NAME,restoDetail_new_data.getResto().getName());
                    wrapperParams.addParam(Keys.LAT,locationChild.getLat());
                    wrapperParams.addParam(Keys.LON,locationChild.getLon());

                    SingleResponse<LocationChild> response=executeCall(getApi().createLocation(wrapperParams));
                    locationChild.setId(response.getData().getId());
                    subscriber.onNext("");
                    subscriber.onComplete();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            });
        }
    }
    class prepParam {

        private SimpleSubscriber mainSubscriber;

        public prepParam(SimpleSubscriber simpleSubscriber){
            mainSubscriber = simpleSubscriber;
            startPrep();
        }
        private void startPrep() {
            prepLocation(new SimpleSubscriber(){
                @Override
                public void onNext(Object o) {
                    super.onNext(o);
                    mainSubscriber.onNext("");
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    mainSubscriber.onError(e);
                }
            });
        }
        private void checkCity(Location location, LocationChild locationChild, SimpleSubscriber citySubscriber) {
            if(location.getCity_id()!=null) {
                GeoPackage geoPackage = new GeoPackage();
                geoPackage.setCityName(location.getCity_id());
                loadCityTask.execute(geoPackage,new SimpleSubscriber<List<City>>(){
                    @Override
                    public void onNext(List<City> cities) {
                        super.onNext(cities);
                        if(cities.size()==1){
                            City city=cities.get(0);
                            locationChild.setCity_id(String.valueOf(city.getId()));
                            locationChild.setCountry_id(city.getCountryId());
                            location.setCity_id(city.getName());
                            citySubscriber.onNext(null);
                        }else {
                            citySubscriber.onError(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        citySubscriber.onError(e);
                    }
                });
            }else {
                citySubscriber.onError(null);
            }

        }
        private void prepLocation(SimpleSubscriber locationSubscriber) {
            if(isNeedSaveField("getFormatLocation",restoDetail_old_data.getResto().getLocation(),restoDetail_new_data.getResto().getLocation())) {
                Location location=restoDetail_new_data.getResto().getLocation();
                LocationChild locationChild=location.getLocation();

                locationChild.setId(null);
                locationChild.setBy_user_id(String.valueOf(userRepo.load().getId()));

                checkCity(location,locationChild,new SimpleSubscriber(){
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        addLocation(locationChild,locationSubscriber);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        locationSubscriber.onError(e);
                    }
                });
            }else {
                locationSubscriber.onNext(null);
            }
        }
        private void addLocation(LocationChild locationChild, SimpleSubscriber locationSubscriber) {
            new CreateLocation(
                    BeerMap.getAppComponent().mainThread(),
                    BeerMap.getAppComponent().executor(),
                    BeerMap.getAppComponent().api()
            ).execute(locationChild,locationSubscriber);
        }
    }

}
