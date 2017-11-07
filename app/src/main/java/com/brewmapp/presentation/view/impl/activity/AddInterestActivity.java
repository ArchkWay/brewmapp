package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.AddInterestPresenter;
import com.brewmapp.presentation.view.contract.AddInterestView;
import com.brewmapp.presentation.view.impl.widget.FinderView;
import com.brewmapp.presentation.view.impl.widget.InterestAddViewResto;
import com.brewmapp.utils.Cons;

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

public class AddInterestActivity extends BaseActivity implements AddInterestView {
    @BindView(R.id.common_toolbar_search)    Toolbar toolbarSearch;
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_search_search)    FinderView finder;
    @BindView(R.id.activity_add_interest_list)    RecyclerView recyclerview;
    @BindView(R.id.activity_add_interest_swipe)    RefreshableSwipeRefreshLayout swipe;

    @Inject    AddInterestPresenter presenter;



    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage fullSearchPackage;
    private EndlessRecyclerOnScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interest);
    }

    @Override
    protected void initView() {
        enableBackButton();
        fullSearchPackage=new FullSearchPackage();

        switch (getIntent().getAction()){
            case Keys.CAP_BEER:
                fullSearchPackage.setType(Keys.TYPE_BEER);
                break;
            case Keys.CAP_RESTO:
                fullSearchPackage.setType(Keys.TYPE_RESTO);
                break;
        }


        finder.setListener(string -> prepareQuery(string));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                fullSearchPackage.setPage(currentPage-1);
                sendQuery();
            }
        };
        recyclerview.setLayoutManager(manager);
        recyclerview.addOnScrollListener(scrollListener);
        adapter= new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        recyclerview.setAdapter(adapter);
        swipe.setOnRefreshListener(this::refreshItems);
        setTitle(R.string.action_add);
    }

    private void prepareQuery(String stringSearch) {
        fullSearchPackage.setPage(0);
        fullSearchPackage.setStringSearch(stringSearch);
        sendQuery();
    }

    private void sendQuery() {
        if(fullSearchPackage.getStringSearch().length()==0){
            fullSearchPackage.setPage(0);
            appendItems(new ArrayList<>());
        }else {
            swipe.setRefreshing(true);
            presenter.sendQueryFullSearch(fullSearchPackage);
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
        if(fullSearchPackage.getPage()==0)
            adapter.clear();

        adapter.addItems(adapter.getItemCount(), list);
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }

    @Override
    public void onError() {
        swipe.setRefreshing(false);

    }

    private void processAction(int action, Object payload) {
        switch (action){
            case InterestAddViewResto.ACTION_SELECT_INTEREST: {
                Intent intent = new Intent(this, InterestListActivity.class);
                intent.putExtra(getString(R.string.key_serializable_extra), (Serializable) payload);
                intent.setAction(String.valueOf(InterestAddViewResto.ACTION_SELECT_INTEREST));
                setResult(RESULT_OK, intent);
                finish();
            }break;
            case InterestAddViewResto.ACTION_VIEW_INTEREST: {
                if(payload instanceof Resto){
                    Interest interest=new Interest();
                    Interest_info interest_info=new Interest_info();
                    interest_info.setId(String.valueOf(((Resto)payload).getId()));
                    interest.setInterest_info(interest_info);
                    Intent intent=new Intent(this, RestoDetailActivity.class);
                    intent.putExtra(Keys.RESTO_ID,interest);
                    startActivityForResult(intent, Cons.REQUEST_CODE_REFRESH_ITEMS);
                }
            }break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Cons.REQUEST_CODE_REFRESH_ITEMS:
                if(resultCode==RESULT_OK)
                    setResult(RESULT_OK,new Intent(String.valueOf(InterestAddViewResto.ACTION_VIEW_INTEREST)));
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
