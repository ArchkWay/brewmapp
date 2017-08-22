package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.FriendsPresenter;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.presentation.view.impl.widget.FinderView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

/**
 * Created by oleg on 17.08.17.
 */

public class FriendsActivity extends BaseActivity implements FriendsView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_friends_addFriend) View addFriend;
    @BindView(R.id.activity_friends_search) FinderView search;
    @BindView(R.id.activity_friends_swipe) SwipeRefreshLayout swipe;
    @BindView(R.id.activity_friends_list) RecyclerView list;

    @Inject FriendsPresenter presenter;

    private FlexibleAdapter<IFlexible> adapter;
    private List<IFlexible> original;
    private boolean subscribers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    protected void initView() {
        enableBackButton();
        subscribers = getIntent().getBooleanExtra(Keys.SUBSCRIBERS, false);
        addFriend.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, InviteActivity.class), RequestCodes.REQUEST_INVITE_FRIEND);
        });
        search.setListener(string -> {
            adapter.setSearchText(string);
            adapter.filterItems(original);
        });
        swipe.setOnRefreshListener(() -> presenter.loadFriends(subscribers));
        adapter = new FlexibleAdapter<>(new ArrayList<>());
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.loadFriends(subscribers);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public void enableControls(boolean enabled, int code) {
        swipe.setRefreshing(!enabled);
    }

    @Override
    public void showFriends(List<IFlexible> list) {
        original = list;
        adapter.updateDataSet(list);
    }
}
