package com.brewmapp.presentation.presenter.impl;

import android.text.TextUtils;

import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.pojo.LoadProductPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.LoadProductTask;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import com.brewmapp.presentation.presenter.contract.BeerEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.BeerEditFragmentView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 12/8/2017.
 */

public class BeerEditFragmentPresenterImpl extends BasePresenter<BeerEditFragmentView> implements BeerEditFragmentPresenter {

    private LoadProductTask loadProductTask;
    private Beer beer_old_data=new Beer();
    private Beer beer_new_data=new Beer();
    private String[] checkListAll={"getBrand_id","getId","getTitle"};

    @Inject
    public BeerEditFragmentPresenterImpl(LoadProductTask loadProductTask){
        this.loadProductTask = loadProductTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(BeerEditFragmentView beerEditFragmentView) {
        super.onAttach(beerEditFragmentView);
    }

    @Override
    public void requestContent(String id_beer) {
        LoadProductPackage loadProductPackage=new LoadProductPackage();
        loadProductPackage.setId(id_beer);
        loadProductTask.execute(loadProductPackage,new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                    beer_old_data=((BeerInfo)iFlexibles.get(0)).getModel();
                    beer_new_data=beer_old_data.clone();
                    view.setContent(beer_old_data);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });

    }

    @Override
    public boolean isNeedSave() {

        for (String s:checkListAll)
            if(isNeedSaveField(s,beer_old_data,beer_new_data))
                return true;

        return false;
    }

    @Override
    public Beer getBeer_new_data() {
        return beer_new_data;
    }

    @Override
    public Beer getBeer_old_data() {
        return beer_old_data;
    }

    @Override
    public void storeChanges() {
        class EditBeerTask  extends BaseNetworkTask<Void, Beer> {

            @Inject
            public EditBeerTask(MainThread mainThread, Executor executor, Api api) {
                super(mainThread, executor, api);
            }

            @Override
            protected Observable<Beer> prepareObservable(Void aVoid) {
                return Observable.create(subscriber -> {
                    try {
                        WrapperParams wrapperParams = new WrapperParams(Wrappers.BEER);
                        wrapperParams.addParam(Keys.ID,beer_new_data.getId());
                        wrapperParams.addParam(Keys.COUNTRY_ID,beer_new_data.getCountry_id());
                        wrapperParams.addParam(Keys.BRAND_ID,beer_new_data.getBrand_id());
                        wrapperParams.addParam(Keys.TYPE_ID,beer_new_data.getType_id());
                        wrapperParams.addParam(Keys.BREWERY_ID,beer_new_data.getBrewery_id());
                        if(isNeedSaveField("getTitle",beer_old_data,beer_new_data))
                            wrapperParams.addParam(Keys.TITLE, beer_new_data.getTitle());

                        SingleResponse<Beer> response=executeCall(getApi().editBeer(wrapperParams));
                        subscriber.onNext(response.getData());
                        subscriber.onComplete();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        }
        new EditBeerTask(
                BeerMap.getAppComponent().mainThread(),
                BeerMap.getAppComponent().executor(),
                BeerMap.getAppComponent().api()
        ).execute(null,new SimpleSubscriber<Beer>(){
            @Override
            public void onNext(Beer beer) {
                super.onNext(beer);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
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
