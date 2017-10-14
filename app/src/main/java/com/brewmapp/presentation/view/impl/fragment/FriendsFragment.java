package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.presentation.presenter.contract.FriendsPresenter;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.presentation.view.impl.activity.InviteActivity;
import com.brewmapp.presentation.view.impl.widget.FinderView;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

public class FriendsFragment extends BaseFragment implements FriendsView {

    @BindView(R.id.fragment_friends_addFriend) View addFriend;
    @BindView(R.id.fragment_friends_search) FinderView search;
    @BindView(R.id.fragment_friends_swipe) SwipeRefreshLayout swipe;
    @BindView(R.id.fragment_friends_list) RecyclerView list;

    @Inject FriendsPresenter presenter;

    private FlexibleAdapter<IFlexible> adapter;
    private List<IFlexible> original;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_friends;
    }


    @Override
    public void enableControls(boolean enabled, int code) {
        swipe.setRefreshing(!enabled);
    }

    @Override
    protected void initView(View view) {
        addFriend.setOnClickListener(v -> {
            startActivityForResult(new Intent(getActivity(), InviteActivity.class), RequestCodes.REQUEST_INVITE_FRIEND);
        });
        search.setListener(string -> {
            adapter.setSearchText(string);
            adapter.filterItems(original);
        });
        swipe.setOnRefreshListener(() -> presenter.loadFriends(false));
        adapter = new FlexibleAdapter<>(new ArrayList<>());
        list.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.loadFriends(false);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public int getMenuToInflate() {
        return 0;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return ResourceHelper.getString(R.string.friends);
    }

    @Override
    public void showFriends(List<IFlexible> list) {
        original = list;
        adapter.updateDataSet(list);
    }
}
