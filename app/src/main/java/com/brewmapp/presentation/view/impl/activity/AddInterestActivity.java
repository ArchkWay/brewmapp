package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Product;
import com.brewmapp.data.pojo.FindInterestPackage;
import com.brewmapp.presentation.presenter.contract.AddInterestPresenter;
import com.brewmapp.presentation.view.contract.AddInterestView;
import com.brewmapp.presentation.view.impl.widget.FinderView;

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
    private FindInterestPackage findInterestPackage;
    private EndlessRecyclerOnScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interest);
    }

    @Override
    protected void initView() {
        enableBackButton();

        findInterestPackage =new FindInterestPackage();
        findInterestPackage.setInterestFilter(getIntent().getAction());
        finder.setListener(string -> prepareQuery(string));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                findInterestPackage.setPage(currentPage-1);
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
        findInterestPackage.setPage(0);
        findInterestPackage.setStringSearch(stringSearch);
        sendQuery();
    }

    private void sendQuery() {
        if(findInterestPackage.getStringSearch().length()==0){
            findInterestPackage.setPage(0);
            appendItems(new ArrayList<IFlexible>());
        }else {
            swipe.setRefreshing(true);
            presenter.sendQuery(findInterestPackage);
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
        if(findInterestPackage.getPage()==0)
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
        Intent intent=new Intent();
        intent.putExtra(getString(R.string.key_serializable_extra),(Product)payload);
        setResult(RESULT_OK,intent);
        finish();
    }

}
