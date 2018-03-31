package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.data.entity.wrapper.FriendsTitleInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.FriendsPresenter;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.presentation.view.impl.activity.InviteActivity;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.brewmapp.presentation.view.impl.widget.FinderView;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

import static android.app.Activity.RESULT_OK;

public class FriendsFragment extends BaseFragment implements FriendsView
{

    //region BindView
    @BindView(R.id.fragment_friends_addFriend) View addFriend;
    @BindView(R.id.fragment_friends_search) FinderView search;
    @BindView(R.id.fragment_friends_swipe) SwipeRefreshLayout swipe;
    @BindView(R.id.fragment_friends_requests) RecyclerView requests;
    @BindView(R.id.fragment_friends_friends) RecyclerView friends;
    //endregion

    //region Inject
    @Inject FriendsPresenter presenter;
    //endregion

    //region Private
    private FlexibleAdapter<IFlexible> adapter_friends;
    private FlexibleAdapter<IFlexible> adapter_requests;
    private List<IFlexible> original_friends=new ArrayList<>();
    private List<IFlexible> original_requests=new ArrayList<>();
    //endregion

    //region Impl FriendsFragment
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_friends;
    }

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
        addFriend.setOnClickListener(v -> {
            startActivityForResult(new Intent(getActivity(), InviteActivity.class), RequestCodes.REQUEST_INVITE_FRIEND);
        });

        search.setListener(string -> {
            adapter_friends.setSearchText(string);
            adapter_friends.filterItems(original_friends);
            adapter_requests.setSearchText(string);
            adapter_requests.filterItems(original_requests);
        });

        swipe.setOnRefreshListener(() -> presenter.loadFriends(false));
        adapter_friends = new FlexibleModelAdapter<>(original_friends, (code, payload) -> presenter.onClickItem(code, payload,getActivity()));
        friends.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        friends.setLayoutManager(new LinearLayoutManager(getActivity()));
        friends.setAdapter(adapter_friends);

        adapter_requests = new FlexibleModelAdapter<>(original_requests, (code, payload) -> presenter.onClickItem(code, payload,getActivity()));
        requests.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        requests.setLayoutManager(new LinearLayoutManager(getActivity()));
        requests.setAdapter(adapter_requests);

        presenter.setItemTouchHelper(friends);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add,menu);
        super.onCreateOptionsMenu(menu, inflater);
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
    protected void prepareView(View view) {
        super.prepareView(view);
    }

    @Override
    public void onBarAction(int id) {
        switch (id){
            case R.id.action_add:
                startActivityForResult(
                        new Intent(
                                Keys.CAP_USER_FRIENDS,
                                null,getActivity(),
                                MultiListActivity.class),
                        RequestCodes.REQUEST_INTEREST
                );
                break;
            default:
                super.onBarAction(id);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case RequestCodes.REQUEST_INTEREST:
                if(resultCode==RESULT_OK){
                    presenter.requestNewFriend(data);
                }
                return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    //endregion

    //region Impl FriendsView
    @Override
    public void enableControls(boolean enabled, int code) {
        swipe.setRefreshing(!enabled);
    }

    @Override
    public void showFriends(List<IFlexible> list) {
        original_requests.clear();
        original_friends.clear();

        Iterator<IFlexible> iterator=list.iterator();
        while (iterator.hasNext()){

            IFlexible iFlexible=iterator.next();

            //region get status
            int status=0;
            if(iFlexible instanceof FriendsTitleInfo)
                status =((FriendsTitleInfo)iFlexible).getStatus();
            else if (iFlexible instanceof ContactInfo){
                status =((ContactInfo)iFlexible).getModel().getStatus();
            }
            //endregion

            //region fill items
            switch (status){
                case FriendsView.FRIENDS_REQUEST_IN:
                case FriendsView.FRIENDS_REQUEST_OUT:
                    original_requests.add(iFlexible);
                    break;
                case FriendsView.FRIENDS_NOW:
                    original_friends.add(iFlexible);
                    break;
            }
            //endregion
        }

        if(original_requests.size()>0)
            original_requests.add(0,new FriendsTitleInfo(getString(R.string.title_friends_request),FRIENDS_DEFAULT));

        adapter_requests.notifyDataSetChanged();
        adapter_friends.notifyDataSetChanged();
    }
    //endregion

}
