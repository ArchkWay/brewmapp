package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.MultiListPresenter;
import com.brewmapp.presentation.view.contract.UiCustomControl;
import com.brewmapp.presentation.view.contract.MultiListView;
import com.brewmapp.presentation.view.impl.widget.FinderView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;

public class MultiListActivity extends BaseActivity implements MultiListView,UiCustomControl {
    @BindView(R.id.common_toolbar_search)    Toolbar toolbarSearch;
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_search_search)    FinderView finder;
    @BindView(R.id.activity_add_interest_list)    RecyclerView recyclerview;
    @BindView(R.id.activity_add_interest_swipe)    RefreshableSwipeRefreshLayout swipe;

    @Inject    MultiListPresenter presenter;

    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage fullSearchPackage;
    private EndlessRecyclerOnScrollListener scrollListener;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interest);
    }

    @Override
    protected void initView() {
        enableBackButton();
        mode=presenter.parseIntent(getIntent());
        fullSearchPackage = new FullSearchPackage();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                fullSearchPackage.setPage(currentPage-1);
                sendQuery();
            }
        };

        switch (mode){
            case MODE_ACTIVTY_SHOW_AND_SELECT_BEER:
                fullSearchPackage.setType(Keys.TYPE_BEER);
                setTitle(R.string.action_find_beer);
                finder.setListener(string -> prepareQuery(string));
                recyclerview.addOnScrollListener(scrollListener);
                break;
            case MODE_ACTIVTY_SHOW_AND_SELECT_RESTO:
                fullSearchPackage.setType(Keys.TYPE_RESTO);
                setTitle(R.string.action_find_resto);
                finder.setListener(string -> prepareQuery(string));
                recyclerview.addOnScrollListener(scrollListener);
                break;
            case MODE_ACTIVTY_SHOW_HASHTAG:
                fullSearchPackage.setType(Keys.HASHTAG);

                toolbarSearch.setVisibility(View.GONE);
                try{
                    String strRequest=getIntent().getData().toString().replace("#","").replace("\n","");
                    if(strRequest.length()>0) {
                        prepareQuery(strRequest);
                        setTitle("Хэштаг - "+strRequest);
                    }
                    else
                        commonError();
                }catch (Exception e){
                    commonError(e.getMessage());
                }
                break;
            default:
                commonError();
        }

        swipe.setOnRefreshListener(this::refreshItems);
        recyclerview.setLayoutManager(manager);
        adapter= new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        recyclerview.setAdapter(adapter);


    }

    private void prepareQuery(String stringSearch) {
        fullSearchPackage.setPage(0);
        fullSearchPackage.setStringSearch(stringSearch);
        sendQuery();
    }

    private void sendQuery() {
        if(fullSearchPackage.getStringSearch().length() == 0){
            fullSearchPackage.setPage(0);
            appendItems(new ArrayList<>());
        }else {
            swipe.setRefreshing(true);
            switch (mode){
                case MODE_ACTIVTY_SHOW_AND_SELECT_BEER:
                case MODE_ACTIVTY_SHOW_AND_SELECT_RESTO:
                    presenter.sendQueryFullSearch(fullSearchPackage);
                    break;
                case MODE_ACTIVTY_SHOW_HASHTAG:
                    presenter.sentQueryQuickSearch(fullSearchPackage);
                    break;
                default:
                    commonError();
            }

        }
    }

    private void refreshItems() {
        sendQuery();
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
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public void enableControls(boolean enabled, int code) {
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void appendItems(List<IFlexible> list) {
        if(fullSearchPackage.getPage() == 0)
            adapter.clear();
        adapter.addItems(adapter.getItemCount(), list);
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }

    @Override
    public void onError() {
        swipe.setRefreshing(false);
    }

    @Override
    public void commonError(String... strings) {
        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();

    }

    private void processAction(int action, Object payload) {
        switch (action){
            case SELECT_MODEL: {
                Intent intent = new Intent(this, InterestListActivity.class);
                intent.putExtra(getString(R.string.key_serializable_extra), (Serializable) payload);
                intent.setAction(String.valueOf(SELECT_MODEL));
                setResult(RESULT_OK, intent);
                finish();
            }break;
            case VIEW_MODEL: {
                if(payload instanceof Resto){
                    Interest interest=new Interest();
                    Interest_info interest_info=new Interest_info();
                    interest_info.setId(String.valueOf(((Resto)payload).getId()));
                    interest.setInterest_info(interest_info);
                    Intent intent=new Intent(this, RestoDetailActivity.class);
                    intent.putExtra(Keys.RESTO_ID,interest);
                    startActivityForResult(intent, REQUEST_CODE_REFRESH_ITEMS);
                }else if(payload instanceof Beer){
                    Intent intent=new Intent(this, BeerDetailActivity.class);
                    Interest interest=new Interest();
                    Interest_info interest_info=new Interest_info();
                    interest_info.setId(String.valueOf(((Beer)payload).getId()));
                    interest.setInterest_info(interest_info);
                    intent.putExtra(getString(R.string.key_serializable_extra),interest);
                    startActivityForResult(intent, REQUEST_CODE_REFRESH_ITEMS);
                }
            }break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_REFRESH_ITEMS:
                if(resultCode==RESULT_OK)
                    setResult(RESULT_OK,new Intent(String.valueOf(VIEW_MODEL)));
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
