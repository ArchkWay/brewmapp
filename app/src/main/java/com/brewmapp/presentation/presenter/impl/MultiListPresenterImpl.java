package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.brewmapp.R;
import com.brewmapp.data.entity.EvaluationBeer;
import com.brewmapp.data.entity.EvaluationResto;
import com.brewmapp.data.entity.MenuResto;
import com.brewmapp.data.entity.wrapper.EvaluationData;
import com.brewmapp.data.entity.wrapper.EvaluationItem;
import com.brewmapp.data.entity.wrapper.MenuInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.EvaluationPackage;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.FullSearchTask;
import com.brewmapp.execution.task.LoadBeerEvaluationTask;
import com.brewmapp.execution.task.LoadMenu;
import com.brewmapp.execution.task.LoadRestoEvaluationTask;
import com.brewmapp.execution.task.QuickSearchTask;
import com.brewmapp.execution.task.containers.contract.ContainerTasks;
import com.brewmapp.presentation.presenter.contract.MultiListPresenter;
import com.brewmapp.presentation.view.contract.MultiListView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 10/20/2017.
 */

public class MultiListPresenterImpl extends BasePresenter<MultiListView> implements MultiListPresenter {


    private FullSearchTask fullSearchTask;
    private QuickSearchTask quickSearchTask;
    private ContainerTasks containerTasks;
    private LoadRestoEvaluationTask loadRestoEvaluationTask;
    private LoadBeerEvaluationTask loadBeerEvaluationTask;
    private LoadMenu loadMenu;
    private Context context;

    private ArrayList<IFlexible> itemArrayList=new ArrayList<>();


    @Inject
    public MultiListPresenterImpl(FullSearchTask fullSearchTask, QuickSearchTask quickSearchTask,ContainerTasks containerTasks,LoadRestoEvaluationTask loadRestoEvaluationTask,LoadBeerEvaluationTask loadBeerEvaluationTask,LoadMenu loadMenu,Context context){
        this.fullSearchTask = fullSearchTask;
        this.quickSearchTask = quickSearchTask;
        this.containerTasks = containerTasks;
        this.loadRestoEvaluationTask = loadRestoEvaluationTask;
        this.loadBeerEvaluationTask = loadBeerEvaluationTask;
        this.loadMenu = loadMenu;
        this.context = context;

    }

    @Override
    public void onAttach(MultiListView multiListView) {
        super.onAttach(multiListView);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void sendQueryFullSearch(FullSearchPackage fullSearchPackage) {
        view.showProgress(true);
        fullSearchTask.cancel();
        fullSearchTask.execute(fullSearchPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                view.appendItems(iFlexibles);
                view.showProgress(false);
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.onError();
                view.showMessage(e.getMessage(),0);
                view.showProgress(false);
            }
        });

    }

    @Override
    public String parseIntent(Intent intent) {
        switch (intent.getAction()) {
            case MultiListView.MODE_SHOW_ALL_MY_EVALUATION:
                return MultiListView.MODE_SHOW_ALL_MY_EVALUATION;
            case MultiListView.MODE_SHOW_AND_SELECT_BEER:
                return MultiListView.MODE_SHOW_AND_SELECT_BEER;
            case MultiListView.MODE_SHOW_AND_SELECT_RESTO:
                return MultiListView.MODE_SHOW_AND_SELECT_RESTO;
            case MultiListView.MODE_SHOW_REVIEWS_BEER:
                return MultiListView.MODE_SHOW_REVIEWS_BEER;
            case MultiListView.MODE_SHOW_REVIEWS_RESTO:
                return MultiListView.MODE_SHOW_REVIEWS_RESTO;
            case Keys.HASHTAG:
                return MultiListView.MODE_SHOW_HASHTAG;
            case Keys.CAP_USER_FRIENDS:
                return MultiListView.MODE_SHOW_AND_SELECT_FRIENDS;
            case MultiListView.MODE_SHOW_AND_CREATE_RESTO:
                return MultiListView.MODE_SHOW_AND_CREATE_RESTO;
            case MultiListView.MODE_SHOW_AND_CREATE_BEER:
                return MultiListView.MODE_SHOW_AND_CREATE_BEER;
            case MultiListView.MODE_SHOW_MENU:
                return MultiListView.MODE_SHOW_MENU;
            case MultiListView.MODE_SHOW_MY_RESUME:
                return MultiListView.MODE_SHOW_MY_RESUME;
            case MultiListView.MODE_SHOW_MY_WORK:
                return MultiListView.MODE_SHOW_MY_WORK;
            default:
                return MultiListView.MODE_ACTIVTY_ERROR;
        }
    }

    @Override
    public void sentQueryQuickSearch(FullSearchPackage fullSearchPackage) {
        view.showProgress(true);
        quickSearchTask.cancel();
        quickSearchTask.execute(fullSearchPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                view.appendItems(iFlexibles);
                view.showProgress(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.appendItems(new ArrayList<IFlexible>());
                view.showProgress(false);
            }
        });

    }

    @Override
    public void loadReviewsResto(int id_resto) {
        ReviewPackage reviewPackage=new ReviewPackage();
        reviewPackage.setRelated_model(Keys.CAP_RESTO);
        reviewPackage.setRelated_id(String.valueOf(id_resto));
        containerTasks.loadReviewsTask(reviewPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override public void onNext(List<IFlexible> iFlexibles ) {
                super.onNext(iFlexibles);
                view.appendItems(iFlexibles);
            }
            @Override public void onError(Throwable e) {
                super.onError(e);view.commonError(e.getMessage());
            }
        });
    }

    @Override
    public void loadReviewsBeer(int id_beer) {
        ReviewPackage reviewPackage=new ReviewPackage();
        reviewPackage.setRelated_model(Keys.CAP_BEER);
        reviewPackage.setRelated_id(String.valueOf(id_beer));
        containerTasks.loadReviewsTask(reviewPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override public void onNext(List<IFlexible> iFlexibles ) {
                super.onNext(iFlexibles);
                view.appendItems(iFlexibles);
            }
            @Override public void onError(Throwable e) {
                super.onError(e);view.commonError(e.getMessage());
            }
        });
    }

    @Override
    public void loadMyEvaluation(int step) {

        switch (step){
            case 0:{
                loadRestoEvaluationTask.execute(new EvaluationPackage(Wrappers.RESTO_EVALUATION),new SimpleSubscriber<List<EvaluationResto>>(){

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(List<EvaluationResto> evaluationRestos) {
                        super.onNext(evaluationRestos);
                        //collect by id_resto
                        String id_resto=null;
                        HashMap<String,EvaluationData> hashMap=new HashMap<>();
                        for (EvaluationResto evaluationResto:evaluationRestos){
                            id_resto=evaluationResto.getResto_id();
                            EvaluationData evaluationData=null;
                            if(hashMap.containsKey(id_resto))
                                evaluationData=hashMap.get(id_resto);
                            else
                                evaluationData=new EvaluationData(id_resto,Keys.CAP_RESTO);
                            evaluationData.add(evaluationResto);
                            hashMap.put(id_resto,evaluationData);
                        }


                        Iterator<EvaluationData> iterator=hashMap.values().iterator();
                        while (iterator.hasNext())
                            itemArrayList.add(new EvaluationItem(iterator.next()));

                        loadMyEvaluation(1);
                    }

                });
            }break;
            case 1:{
                loadBeerEvaluationTask.execute(0,
                        new Subscriber<ArrayList<EvaluationBeer>>() {
                            @Override
                            public void onSubscribe(Subscription s) {

                            }

                            @Override
                            public void onNext(ArrayList<EvaluationBeer> evaluationBeers) {
                                //collect by id_beer
                                String id_beer=null;
                                HashMap<String,EvaluationData> hashMap=new HashMap<>();
                                for (EvaluationBeer evaluationBeer:evaluationBeers){
                                    id_beer=evaluationBeer.getProduct_id();
                                    EvaluationData evaluationData=null;
                                    if(hashMap.containsKey(id_beer))
                                        evaluationData=hashMap.get(id_beer);
                                    else
                                        evaluationData=new EvaluationData(id_beer,Keys.CAP_BEER);
                                    evaluationData.add(evaluationBeer);
                                    hashMap.put(id_beer,evaluationData);
                                }


                                Iterator<EvaluationData> iterator=hashMap.values().iterator();
                                while (iterator.hasNext())
                                    itemArrayList.add(new EvaluationItem(iterator.next()));

                                view.appendItems(itemArrayList);
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
            }break;
        }
    }

    @Override
    public void loadMenu(FullSearchPackage fullSearchPackage) {
        loadMenu.cancel();
        view.showProgress(true);
        loadMenu.execute(fullSearchPackage,new SimpleSubscriber<ListResponse<MenuResto>>(){
            @Override
            public void onNext(ListResponse<MenuResto> menuRestoListResponse) {
                super.onNext(menuRestoListResponse);

                //region Collect by key_beer
                String[] capacity= context.getResources().getStringArray(R.array.resto_menu_capacity);
                HashMap<String,MenuResto> hmCollectBy_key_beer=new HashMap<>();
                Iterator<MenuResto> menuRestoIterator=menuRestoListResponse.getModels().iterator();
                while (menuRestoIterator.hasNext()){
                    MenuResto menuResto=menuRestoIterator.next();
                    String key_beer=menuResto.getBeer_id();
                    String key_capacity = capacity[Integer.valueOf(menuResto.getResto_menu_capacity_id())];
                    String value_price = menuResto.getPrice();
                    if(hmCollectBy_key_beer.containsKey(key_beer))
                        hmCollectBy_key_beer.get(key_beer).getCapacity_price().put(key_capacity,value_price);
                    else {
                        menuResto.getCapacity_price().put(key_capacity, value_price);
                        hmCollectBy_key_beer.put(key_beer, menuResto);
                    }
                }
                //endregion

                //region Make Items
                ArrayList<IFlexible> iFlexibleArrayList=new ArrayList<>();
                menuRestoIterator=hmCollectBy_key_beer.values().iterator();
                while (menuRestoIterator.hasNext())
                    iFlexibleArrayList.add(new MenuInfo(menuRestoIterator.next()));
                //endregion

                view.appendItems(iFlexibleArrayList);

                view.showProgress(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showProgress(false);
            }
        });
    }

}
