package ru.frosteye.beermap.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.data.entity.SocialContact;
import ru.frosteye.beermap.data.entity.wrapper.ContactInfo;
import ru.frosteye.beermap.data.entity.wrapper.SocialContactInfo;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.presentation.presenter.contract.InviteListPresenter;
import ru.frosteye.beermap.presentation.view.contract.InviteListView;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.beermap.R;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

public class InviteListActivity extends BaseActivity implements InviteListView {

    @BindView(R.id.activity_list_list) RecyclerView list;
    @BindView(R.id.activity_list_swipe) SwipeRefreshLayout swipe;
    @BindView(R.id.common_toolbar) Toolbar toolbar;

    @Inject InviteListPresenter presenter;

    private FlexibleModelAdapter<SocialContactInfo> adapter;

    private int networkId;
    private boolean enabled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void enableControls(boolean enabled, int code) {
        showTopBarLoading(!enabled);
        this.enabled = enabled;
        if(enabled) swipe.setRefreshing(false);
    }

    @Override
    protected void initView() {
        enableBackButton();
        networkId = getIntent().getIntExtra(Keys.NETWORK, 0);
        swipe.setOnRefreshListener(() -> {
            presenter.loadContactsForNetwork(networkId);
        });
        adapter = new FlexibleModelAdapter<>(new ArrayList<>(), (code, payload) -> {
            if(!enabled) return;
            presenter.onRequestInvite(((SocialContact) payload));
        });
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        list.setAdapter(adapter);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.loadContactsForNetwork(networkId);
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
    public void showContacts(List<SocialContactInfo> contactInfos) {
        adapter.updateDataSet(contactInfos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
    }
}
