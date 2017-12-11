package com.brewmapp.presentation.presenter.impl;

import android.text.TextUtils;

import com.brewmapp.R;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.container.RestoDetails;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import com.brewmapp.presentation.presenter.contract.RestoEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.RestoEditFragmentView;
import com.brewmapp.presentation.view.impl.dialogs.DialogSelectAddress;
import com.brewmapp.presentation.view.impl.widget.AddPhotoSliderView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Key;
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
    private String[] checkListRestoDetails={};
    private String[] checkListResto={"getAvgCost"};
    private String[] checkListLocation={};
    private RestoDetail restoDetail_old_data=new RestoDetail();
    private RestoDetail restoDetail_new_data=new RestoDetail();


    @Inject
    public RestoEditFragmentPresenterImpl(LoadRestoDetailTask loadRestoDetailTask){
        this.loadRestoDetailTask=loadRestoDetailTask;
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
        for (String s:checkListResto)
            if(isNeedSaveField(s,restoDetail_old_data.getResto(),restoDetail_new_data.getResto()))
                return true;


        return false;
    }

    @Override
    public void storeChanges() {

    }

    @Override
    public RestoDetail getRestoDetail_new_data() {
        return restoDetail_new_data;
    }

    @Override
    public DialogSelectAddress DialogSelectAddress() {
        return new DialogSelectAddress().setLocation(restoDetail_old_data.getResto().getLocation());
    }

    private boolean isNeedSaveField(String getMethod_name, Object oldObject, Object newObject) {
        try {
            Class c=oldObject.getClass();
            String type=c.getMethod(getMethod_name).getGenericReturnType().toString();
            if(type.contains("String")){
                Method m_get = c.getMethod(getMethod_name);
                Method m_set = c.getMethod(new StringBuilder().append(getMethod_name).replace(0, 1, "s").toString(), String.class);

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

}
